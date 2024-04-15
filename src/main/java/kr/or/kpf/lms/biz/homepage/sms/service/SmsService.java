package kr.or.kpf.lms.biz.homepage.sms.service;

import kr.or.kpf.lms.biz.homepage.sms.vo.request.SmsApiRequestVO;
import kr.or.kpf.lms.biz.homepage.sms.vo.request.SmsViewRequestVO;
import kr.or.kpf.lms.biz.homepage.sms.vo.response.SmsApiResponseVO;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonHomepageRepository;
import kr.or.kpf.lms.repository.CommonSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

/**
 * 홈페이지 관리 > SMS 발송 관리 관련 Service
 */
@Service
@RequiredArgsConstructor
public class SmsService extends CSServiceSupport {
    private final CommonHomepageRepository commonHomepageRepository;

    /**
     * 홈페이지 관리 > SMS발송 관리 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(SmsViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonHomepageRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }
}
