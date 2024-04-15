package kr.or.kpf.lms.biz.business.instructor.asgnm.service;

import kr.or.kpf.lms.biz.business.instructor.asgnm.vo.request.BizInstructorAsgnmApiRequestVO;
import kr.or.kpf.lms.biz.business.instructor.asgnm.vo.request.BizInstructorAsgnmViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.asgnm.vo.response.BizInstructorAsgnmApiResponseVO;
import kr.or.kpf.lms.biz.business.instructor.vo.response.BizInstructorExcelVO;
import kr.or.kpf.lms.biz.business.organization.aply.vo.request.BizOrganizationAplyCustomViewRequestVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.BizInstructorAsgnmRepository;
import kr.or.kpf.lms.repository.CommonBusinessRepository;
import kr.or.kpf.lms.repository.entity.BizInstructorAsgnm;
import kr.or.kpf.lms.repository.entity.BizSurveyQitemItem;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BizInstructorAsgnmService extends CSServiceSupport {
    private static final String PREFIX_INSTR_ASGNM = "BIG";

    private final CommonBusinessRepository commonBusinessRepository;

    private final BizInstructorAsgnmRepository bizInstructorAsgnmRepository;

    /**
     강사 모집 배정 정보 생성
     */
    public BizInstructorAsgnmApiResponseVO createBizInstructorAsgnm(BizInstructorAsgnmApiRequestVO requestObject) {
        BizInstructorAsgnm entity = BizInstructorAsgnm.builder().build();
        BeanUtils.copyProperties(requestObject, entity);
        BizInstructorAsgnmApiResponseVO result = BizInstructorAsgnmApiResponseVO.builder().build();
        BeanUtils.copyProperties(bizInstructorAsgnmRepository.saveAndFlush(entity), result);
        return result;
    }

    /**
     강사 모집 배정 정보 업데이트
     */
    public BizInstructorAsgnmApiResponseVO updateBizInstructorAsgnm(BizInstructorAsgnmApiRequestVO requestObject) {
        return bizInstructorAsgnmRepository.findById(requestObject.getBizInstrAsgnmNo())
                .map(bizInstructorAsgnm -> {
                    copyNonNullObject(requestObject, bizInstructorAsgnm);

                    BizInstructorAsgnmApiResponseVO result = BizInstructorAsgnmApiResponseVO.builder().build();
                    BeanUtils.copyProperties(bizInstructorAsgnmRepository.saveAndFlush(bizInstructorAsgnm), result);

                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3605, "해당 강사 모집 미존재"));
    }

    /**
     강사 모집 배정 정보 삭제
     */
    public CSResponseVOSupport deleteBizInstructorAsgnm(BizInstructorAsgnmApiRequestVO requestObject) {
        bizInstructorAsgnmRepository.delete(bizInstructorAsgnmRepository.findById(requestObject.getBizInstrAsgnmNo())
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3607, "삭제된 강사 모집 입니다.")));
        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     강사 모집 배정 정보 재생성 시 전체 삭제
     */
    public CSResponseVOSupport deleteAllBizInstructorAsgnm(BizInstructorAsgnmApiRequestVO requestObject) {
        bizInstructorAsgnmRepository.deleteAll(bizInstructorAsgnmRepository.findAll(Example.of(BizInstructorAsgnm.builder()
                .bizOrgAplyNo(requestObject.getBizOrgAplyNo())
                .build())));
        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }


    /**
     강사 모집 배정 리스트
     */
    public <T> Page<T> getBizInstructorAsgnmList(BizInstructorAsgnmViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonBusinessRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     강사 모집 배정 상세
     */
    public <T> T getBizInstructorAsgnm(BizInstructorAsgnmViewRequestVO requestObject) {
        return (T) commonBusinessRepository.findEntity(requestObject);
    }


    /**
     엑셀 다운로드
     */
    public <T> List<BizInstructorExcelVO> getExcel(BizOrganizationAplyCustomViewRequestVO requestObject) {
        List<BizInstructorExcelVO> bizInstructorExcelVOList = (List<BizInstructorExcelVO>) commonBusinessRepository.findEntityListExcel(requestObject);
        return bizInstructorExcelVOList;
    }
}
