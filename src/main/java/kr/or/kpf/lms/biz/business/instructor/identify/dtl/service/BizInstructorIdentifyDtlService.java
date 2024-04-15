package kr.or.kpf.lms.biz.business.instructor.identify.dtl.service;

import kr.or.kpf.lms.biz.business.instructor.identify.dtl.vo.request.BizInstructorIdentifyDtlApiRequestVO;
import kr.or.kpf.lms.biz.business.instructor.identify.dtl.vo.request.BizInstructorIdentifyDtlExcelRequestVO;
import kr.or.kpf.lms.biz.business.instructor.identify.dtl.vo.request.BizInstructorIdentifyDtlViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.identify.dtl.vo.response.BizInstructorIdentifyDtlApiResponseVO;
import kr.or.kpf.lms.biz.business.instructor.identify.dtl.vo.response.BizInstructorIdentifyDtlExcelVO;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.request.ArchiveClassGuideExcelRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.response.ArchiveClassGuideExcelResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.BizInstructorIdentifyDtlRepository;
import kr.or.kpf.lms.repository.BizInstructorIdentifyRepository;
import kr.or.kpf.lms.repository.BizOrganizationAplyDtlRepository;
import kr.or.kpf.lms.repository.CommonBusinessRepository;
import kr.or.kpf.lms.repository.entity.BizInstructorIdentify;
import kr.or.kpf.lms.repository.entity.BizInstructorIdentifyDtl;
import kr.or.kpf.lms.repository.entity.BizOrganizationAplyDtl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BizInstructorIdentifyDtlService extends CSServiceSupport {
    private final CommonBusinessRepository commonBusinessRepository;
    private final BizInstructorIdentifyDtlRepository bizInstructorIdentifyDtlRepository;
    private final BizInstructorIdentifyRepository bizInstructorIdentifyRepository;
    private final BizOrganizationAplyDtlRepository bizOrganizationAplyDtlRepository;

    /**
     강의확인서 강의시간표 정보 업데이트
     */
    public BizInstructorIdentifyDtlApiResponseVO updateBizInstructorIdentifyDtl(BizInstructorIdentifyDtlApiRequestVO requestObject) {
        BizInstructorIdentifyDtlApiResponseVO result = BizInstructorIdentifyDtlApiResponseVO.builder().build();

        BizInstructorIdentify bizInstructorIdentify = bizInstructorIdentifyRepository.findOne(Example.of(BizInstructorIdentify.builder()
                .bizInstrIdntyNo(requestObject.getBizInstrIdntyNo())
                .build())).get();

        BizOrganizationAplyDtl bizOrganizationAplyDtl = bizOrganizationAplyDtlRepository.findOne(Example.of(BizOrganizationAplyDtl.builder()
                .bizOrgAplyDtlNo(requestObject.getBizOrgAplyDtlNo())
                .build())).get();

        if (bizInstructorIdentify.getBizOrgAplyNo().equals(bizOrganizationAplyDtl.getBizOrgAplyNo())) {
            return bizInstructorIdentifyDtlRepository.findById(requestObject.getBizInstrIdntyDtlNo())
                    .map(bizInstructorIdentifyDtl -> {
                        copyNonNullObject(requestObject, bizInstructorIdentifyDtl);

                        if(requestObject.getBizInstrIdntyEtc()==null)
                            bizInstructorIdentifyDtl.setBizInstrIdntyEtc(null);
                        if(requestObject.getBizInstrIdntyFormula()==null)
                            bizInstructorIdentifyDtl.setBizInstrIdntyFormula(null);

                        BeanUtils.copyProperties(bizInstructorIdentifyDtlRepository.saveAndFlush(bizInstructorIdentifyDtl), result);
                        return result;
                    }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3605, "해당 강의확인서 강의시간표 미존재"));
        } else {
            return result;
        }
    }

    /**
     강의확인서 강의시간표 정보 삭제
     */
    public CSResponseVOSupport deleteBizInstructorIdentifyDtl(BizInstructorIdentifyDtlApiRequestVO requestObject) {
        bizInstructorIdentifyDtlRepository.delete(bizInstructorIdentifyDtlRepository.findById(requestObject.getBizInstrIdntyDtlNo())
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3607, "삭제된 강의확인서 강의시간표 입니다.")));
        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     강의확인서 강의시간표 리스트
     */
    public <T> Page<T> getBizInstructorIdentifyDtlList(BizInstructorIdentifyDtlViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonBusinessRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     강의확인서 강의시간표 상세
     */
    public <T> T getBizInstructorIdentifyDtl(BizInstructorIdentifyDtlViewRequestVO requestObject) {
        return (T) commonBusinessRepository.findEntity(requestObject);
    }

    /**
     강의확인서 주제 엑셀 다운로드
     */
    public <T> List<BizInstructorIdentifyDtlExcelVO> getExcel(BizInstructorIdentifyDtlExcelRequestVO requestObject) {
        List<BizInstructorIdentifyDtlExcelVO> list = (List<BizInstructorIdentifyDtlExcelVO>) commonBusinessRepository.findEntityListExcel(requestObject);
        return list;
    }
}
