package kr.or.kpf.lms.biz.business.instructor.aply.service;

import kr.or.kpf.lms.biz.business.instructor.aply.vo.request.BizInstructorAplyApiRequestVO;
import kr.or.kpf.lms.biz.business.instructor.aply.vo.request.BizInstructorAplyViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.aply.vo.response.BizInstructorAplyApiResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.BizInstructorAplyRepository;
import kr.or.kpf.lms.repository.CommonBusinessRepository;
import kr.or.kpf.lms.repository.entity.BizInstructorAply;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BizInstructorAplyService extends CSServiceSupport {
    private static final String PREFIX_INSTR_APLY = "BIA";

    private final CommonBusinessRepository commonBusinessRepository;

    private final BizInstructorAplyRepository bizInstructorAplyRepository;

    public boolean vailedBizInstructorAply(BizInstructorAplyApiRequestVO requestObject){
        requestObject.setLoginUserId(authenticationInfo().getUserId());

        Long aplyCnt = commonBusinessRepository.countEntity(requestObject);
        if (aplyCnt + 1 < 6){
            return true;
        }
        return false;
    }

    /**
     강사 모집 신청 정보 생성
     */
    public BizInstructorAplyApiResponseVO createBizInstructorAply(BizInstructorAplyApiRequestVO requestObject) {
        BizInstructorAplyApiResponseVO result = BizInstructorAplyApiResponseVO.builder().build();
        BizInstructorAply check = bizInstructorAplyRepository.findByBizInstrNoAndBizOrgAplyNoAndBizInstrAplyInstrId(requestObject.getBizInstrNo(), requestObject.getBizOrgAplyNo(), requestObject.getBizInstrAplyInstrId());
        if(check==null){
            BizInstructorAply entity = BizInstructorAply.builder().build();
            BeanUtils.copyProperties(requestObject, entity);
            entity.setBizOrgAplyNo(requestObject.getBizOrgAplyNo());
            entity.setBizInstrNo(requestObject.getBizInstrNo());
            entity.setBizInstrAplyNo(commonBusinessRepository.generateCode(PREFIX_INSTR_APLY));
            BeanUtils.copyProperties(bizInstructorAplyRepository.saveAndFlush(entity), result);

            return result;
        } else {
            return null;
        }
    }

    /**
     강사 모집 신청 정보 업데이트
     */
    public BizInstructorAplyApiResponseVO updateBizInstructorAply(BizInstructorAplyApiRequestVO requestObject) {
        return bizInstructorAplyRepository.findById(requestObject.getBizInstrAplyNo())
                .map(bizInstructorAply -> {
                    copyNonNullObject(requestObject, bizInstructorAply);

                    BizInstructorAplyApiResponseVO result = BizInstructorAplyApiResponseVO.builder().build();
                    BeanUtils.copyProperties(bizInstructorAplyRepository.saveAndFlush(bizInstructorAply), result);

                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3605, "해당 강사 모집 미존재"));
    }

    /**
     강사 모집 신청 정보 삭제
     */
    public CSResponseVOSupport deleteBizInstructorAply(BizInstructorAplyApiRequestVO requestObject) {
        bizInstructorAplyRepository.delete(bizInstructorAplyRepository.findById(requestObject.getBizInstrAplyNo())
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3607, "삭제된 강사 모집 입니다.")));
        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     강사 모집 신청 리스트
     */
    public <T> Page<T> getBizInstructorAplyList(BizInstructorAplyViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonBusinessRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     강사 모집 신청 상세
     */
    public <T> T getBizInstructorAply(BizInstructorAplyViewRequestVO requestObject) {
        return (T) commonBusinessRepository.findEntity(requestObject);
    }

}
