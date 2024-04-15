package kr.or.kpf.lms.biz.system.code.service;

import kr.or.kpf.lms.biz.system.code.vo.request.CommonCodeApiRequestVO;
import kr.or.kpf.lms.biz.system.code.vo.request.CommonCodeViewRequestVO;
import kr.or.kpf.lms.biz.system.code.vo.response.CommonCodeApiResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.ResponseSummary;
import kr.or.kpf.lms.repository.system.CommonCodeMasterRepository;
import kr.or.kpf.lms.repository.CommonSystemRepository;
import kr.or.kpf.lms.repository.entity.system.CommonCodeMaster;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static kr.or.kpf.lms.common.support.CSServiceSupport.copyNonNullObject;

/**
 *  시스템 관리 > 공통 코드 관리 관련 Service
 */
@Service
@RequiredArgsConstructor
public class CommonCodeService {
    private static final String PREFIX_COMMON_CODE = "CODE";
    /** 시스템 관리 공통 */
    private final CommonSystemRepository commonSystemRepository;
    /** 공통 코드 관리 */
    private final CommonCodeMasterRepository commonCodeMasterRepository;

    /**
     * 공통 코드 조회
     *  
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(CommonCodeViewRequestVO requestObject) {
        AtomicInteger count = new AtomicInteger();
        return (Page<T>) Optional.ofNullable(Optional.ofNullable(commonSystemRepository.findTopCommonCode(requestObject))
                        .map(results -> {
                            count.set(results.size());
                            return results;
                        }).orElse(new ArrayList<>()))
                .filter(results -> !ListUtils.isEmpty(results))
                .map(results -> {
                    results.stream().sorted(Comparator.comparing(CommonCodeMaster::getCodeSort)).collect(Collectors.toList())
                            .forEach(sub -> sub.setSubCommonCodeMaster(commonCodeMasterRepository.findAll(Example.of(CommonCodeMaster.builder()
                            .upIndividualCode(sub.getIndividualCode())
                            .codeDepth(1)
                            .build())).stream().sorted(Comparator.comparing(CommonCodeMaster::getCodeSort)).collect(Collectors.toList())));
                    ResponseSummary summary = ResponseSummary.builder()
                            .count(results.size())
                            .build();
                    Pageable pageableToApply = summary.ensureValidPageable(requestObject.getPageable());
                    return CSPageImpl.of(results, pageableToApply, count.get());
                }).orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 공통 코드 생성
     *
     * @param commonCodeApiRequestVO
     * @return
     */
    public CommonCodeApiResponseVO createCommonCodeInfo(CommonCodeApiRequestVO commonCodeApiRequestVO) {
        CommonCodeMaster entity = CommonCodeMaster.builder().build();
        BeanUtils.copyProperties(commonCodeApiRequestVO, entity);
        if (commonCodeMasterRepository.findOne(Example.of(CommonCodeMaster.builder()
                                                        .individualCode(commonCodeApiRequestVO.getIndividualCode())
                                                        .build()))
                .isPresent()) { /** 기존 등록된 공통 코드 정보 확인 */
            throw new KPFException(KPF_RESULT.ERROR7002, "동일 코드 존재");
        } else {
            entity.setIndividualCode(commonSystemRepository.generateCode(PREFIX_COMMON_CODE));
            String upIndividualCode = commonCodeApiRequestVO.getUpIndividualCode();
            entity.setCodeSort(commonSystemRepository.generateOrderAutoIncrease(upIndividualCode));

            CommonCodeApiResponseVO result = CommonCodeApiResponseVO.builder().build();
            BeanUtils.copyProperties(commonCodeMasterRepository.saveAndFlush(entity), result);
            return result;
        }
    }

    /**
     * 공통 코드 수정
     *
     * @param commonCodeApiRequestVO
     * @return
     */
    public CommonCodeApiResponseVO updateCommonCodeInfo(CommonCodeApiRequestVO commonCodeApiRequestVO) {
        return commonCodeMasterRepository.findOne(Example.of(CommonCodeMaster.builder()
                                                        .individualCode(commonCodeApiRequestVO.getIndividualCode())
                                                        .build()))
                .map(codeMaster -> {
                    BeanUtils.copyProperties(commonCodeApiRequestVO, codeMaster);

                    CommonCodeApiResponseVO result = CommonCodeApiResponseVO.builder().build();
                    BeanUtils.copyProperties(commonCodeMasterRepository.saveAndFlush(codeMaster), result);

                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7003, "해당 코드 정보 미존재"));
    }

    /**
     * 수업지도안 교과 순서 변경
     *
     * @param requestObject
     * @return
     */
    public CommonCodeApiResponseVO changeOrder(CommonCodeApiRequestVO requestObject) {
        return commonCodeMasterRepository.findOne(Example.of(CommonCodeMaster.builder().individualCode(requestObject.getIndividualCode()).build()))
                .map(commonCodeMaster -> {
                    /** 순서 변경이 실행될 때, 순서가 기존 순서와 다른 경우 */
                    if (requestObject.getCodeSort() != commonCodeMaster.getCodeSort() && !requestObject.getCodeSort().equals(commonCodeMaster.getCodeSort())) {
                        /** 상위 코드와 깊이가 같은 코드들을 찾아서 순서 기준 정렬한 리스트 생성 */
                        List<CommonCodeMaster> codeMasters = commonCodeMasterRepository.findAll(Example.of(CommonCodeMaster.builder()
                                .upIndividualCode(requestObject.getUpIndividualCode())
                                .codeDepth(requestObject.getCodeDepth())
                                .build())).stream().sorted(Comparator.comparing(CommonCodeMaster::getCodeSort)).collect(Collectors.toList());

                        for (CommonCodeMaster codeMaster : codeMasters) {
                            if (requestObject.getCodeSort() <= commonCodeMaster.getCodeSort()) {
                                /** 리스트의 객체 중 순서가 같거나 큰 객체 찾기 */
                                if (codeMaster.getCodeSort() >= requestObject.getCodeSort()) {
                                    codeMaster.setCodeSort(codeMaster.getCodeSort() + 1);
                                    commonCodeMasterRepository.saveAndFlush(codeMaster);
                                }
                            } else {
                                /** 리스트의 객체 중 순서가 같거나 작은 객체 찾기 */
                                if (codeMaster.getCodeSort() < requestObject.getCodeSort()) {
                                    codeMaster.setCodeSort(codeMaster.getCodeSort() - 1);
                                    commonCodeMasterRepository.saveAndFlush(codeMaster);
                                }
                            }
                        }
                    }

                    copyNonNullObject(requestObject, commonCodeMaster);
                    CommonCodeApiResponseVO result = CommonCodeApiResponseVO.builder().build();
                    BeanUtils.copyProperties(commonCodeMasterRepository.saveAndFlush(commonCodeMaster), result);

                    return result;
                })
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7003, "해당 코드 정보 미존재"));
    }
}
