package kr.or.kpf.lms.biz.common.bizppurio.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.or.kpf.lms.biz.common.bizppurio.vo.request.BizPPurioApiRequestVO;
import kr.or.kpf.lms.biz.common.bizppurio.vo.request.BodyOfBizPPurioRequestVO;
import kr.or.kpf.lms.biz.common.bizppurio.vo.response.BodyOfBizPPurioResponseVO;
import kr.or.kpf.lms.common.client.CSWebClient;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.repository.entity.homepage.SmsSendDetail;
import kr.or.kpf.lms.repository.entity.homepage.SmsSendMaster;
import kr.or.kpf.lms.repository.homepage.SmsSendDetailRepository;
import kr.or.kpf.lms.repository.homepage.SmsSendMasterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 비즈뿌리오 관련 Service
 */
@Service
@RequiredArgsConstructor
public class BizPPurioService extends CSServiceSupport {

    /** SMS 발송 이력 (마스터) */
    private final SmsSendMasterRepository smsSendMasterRepository;
    /** SMS 발송 세부 정보 */
    private final SmsSendDetailRepository smsSendDetailRepository;

    /** HTTP 통신 모듈 */
    private final CSWebClient csWebClient;
    /** 토큰 획득 인증 타입 */
    private static final String TOKEN_AUTHORIZATION_TYPE = "Basic";
    /** 일반 인증 타입 */
    private static final String GENERAL_AUTHORIZATION_TYPE = "Bearer";

    private static final String BIZ_PPURIO_SUCCESS = "1000";

/**
 * 비즈뿌리오 API URI
 */
/*  /v1/token POST 인증 토큰을 발급을 요청하는 기능입니다.
    /v3/message POST 메시지 전송을 요청하는 기능입니다.
    /v2/file POST MMS 발송에 사용될 이미지 파일을 업로드하는 기능입니다.
    /v2/report POST 전송 결과 재요청하는 기능입니다.
    /v1/result/request POST 전송 결과를 요청하는 기능입니다. (POLLING 방식)
    /v1/result/confirm POST 전송 결과를 완료 처리하는 기능입니다. (POLLING 방식)*/

    private static final String TOKEN_URI = "/v1/token";
    private static final String SEND_MESSAGE = "/v3/message";

    public CSResponseVOSupport sendSMS(BizPPurioApiRequestVO requestObject) {
        UriComponents bizPPurioUri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(appConfig.getBizPPurio().getUrl())
                .path(SEND_MESSAGE)
                .build();

        RestTemplate restTemplate = "production".equals(System.getenv("spring.profiles.active"))
//        RestTemplate restTemplate = "local".equals(System.getenv("spring.profiles.active"))
                ? csWebClient.settingWebProtocol()
                : csWebClient.settingWebProtocol(true);

        BodyOfBizPPurioRequestVO requestBody = BodyOfBizPPurioRequestVO.builder().build();
        BeanUtils.copyProperties(requestObject, requestBody);
        Map<String, String> sms = new HashMap<>();
        sms.put("message", requestObject.getContent());
        Map<String, Map<String,String>> content = new HashMap<>();
        content.put("sms", sms);
        requestBody.setContent(content);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(requestObject.getReceiver().size());

        /** 마스터 정보 생성 */
        SmsSendMaster smsSendMaster = smsSendMasterRepository.saveAndFlush(SmsSendMaster.builder()
                .content(requestObject.getContent())
                .sender(requestObject.getSender())
                .sendDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .totalCount(requestObject.getReceiver().size())
                .successCount(successCount.get())
                .failureCount(failureCount.get())
                .build());

        requestObject.getReceiver().forEach(receiverPhoneNo -> {
            /** 상세 정보 생성 */
            SmsSendDetail smsSendDetail = SmsSendDetail.builder()
                    .masterSequenceNo(smsSendMaster.getSequenceNo())
                    .sender(smsSendMaster.getSender())
                    .receiver(receiverPhoneNo)
                    .build();

            /** 토근 획득 */
            String accessToken = getToken();
            /** 헤더 셋팅 */
            HttpHeaders headers = new HttpHeaders();

            headers.add("Authorization", GENERAL_AUTHORIZATION_TYPE + " " + accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

            requestBody.setReceiver(receiverPhoneNo);
            requestBody.setReferenceKey(appConfig.getBizPPurio().getPassword());

            try {
                logger.info("비즈뿌리오 SMS 요청: {}", requestBody);
                HttpEntity<BodyOfBizPPurioRequestVO> request = new HttpEntity<>(requestBody, headers);
                ResponseEntity<BodyOfBizPPurioResponseVO> result = restTemplate.exchange(bizPPurioUri.toString(), HttpMethod.POST, request, BodyOfBizPPurioResponseVO.class);
                logger.info("비즈뿌리오 SMS 응답: {}", result);

                if (BIZ_PPURIO_SUCCESS.equals(Objects.requireNonNull(result.getBody()).getResultCode())) { /** 성공 */
                    successCount.getAndIncrement();
                    failureCount.getAndDecrement();
                    smsSendDetail.setIsSuccess(true);
                    smsSendDetailRepository.saveAndFlush(smsSendDetail);
                } else { /** 실패 */
                    smsSendDetail.setIsSuccess(false);
                    smsSendDetailRepository.saveAndFlush(smsSendDetail);
                }
            } catch (HttpClientErrorException | HttpServerErrorException | ResourceAccessException e1) {
                logger.error("비즈뿌리오 통신 오류 발생({} - {})", smsSendMaster.getSequenceNo(), receiverPhoneNo);
                smsSendDetail.setIsSuccess(false);
                smsSendDetailRepository.saveAndFlush(smsSendDetail);
            } catch (Exception e2) {
                logger.error("{}- {}", e2.getClass().getCanonicalName(), e2.getMessage(), e2);
                smsSendDetail.setIsSuccess(false);
                smsSendDetailRepository.saveAndFlush(smsSendDetail);
            }
        });

        /** 성공 수량 업데이트 */
        smsSendMaster.setSuccessCount(successCount.get());
        /** 실패 수량 업데이트 */
        smsSendMaster.setFailureCount(failureCount.get());

        smsSendMasterRepository.saveAndFlush(smsSendMaster);
        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     * 토큰 획득
     *  
     * @return
     */
    public String getToken() {
        /** 헤더 셋팅 */
        HttpHeaders headers = new HttpHeaders();

        String tokenAuthorization = TOKEN_AUTHORIZATION_TYPE + " " + Base64.getEncoder().encodeToString((appConfig.getBizPPurio().getId()
                + ":" + appConfig.getBizPPurio().getPassword()).getBytes());

        headers.add("Authorization", tokenAuthorization);
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        UriComponents bizPPurioUri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(appConfig.getBizPPurio().getUrl())
                .path(TOKEN_URI)
                .build();

        RestTemplate restTemplate = "production".equals(System.getenv("spring.profiles.active"))
//        RestTemplate restTemplate = "local".equals(System.getenv("spring.profiles.active"))
                ? csWebClient.settingWebProtocol()
                : csWebClient.settingWebProtocol(true);

        logger.info("비즈뿌리오 Token 요청: {}", tokenAuthorization);
        HttpEntity<BizPPurioApiRequestVO> request = new HttpEntity<>(headers);

        ResponseEntity<String> result = restTemplate.exchange(bizPPurioUri.toString(), HttpMethod.POST, request, String.class);
        logger.info("비즈뿌리오 Token 응답: {}", result);

        try {
            return (String) new ObjectMapper().readValue(result.getBody(), Map.class).get("accesstoken");
        } catch (JsonProcessingException e) {
            throw new KPFException(KPF_RESULT.ERROR9003, "데이터 파싱 실패");
        }
    }
}