package kr.or.kpf.lms.biz.business.pbanc.master.service;

import kr.or.kpf.lms.biz.business.pbanc.master.template.service.BizPbancTmplService;
import kr.or.kpf.lms.biz.business.pbanc.master.vo.request.BizPbancApiRequestVO;
import kr.or.kpf.lms.biz.business.pbanc.master.vo.request.BizPbancViewRequestVO;
import kr.or.kpf.lms.biz.business.pbanc.master.vo.response.BizPbancApiResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.BizInstructorPbancRepository;
import kr.or.kpf.lms.repository.BizOrganizationAplyRepository;
import kr.or.kpf.lms.repository.BizPbancMasterRepository;
import kr.or.kpf.lms.repository.CommonBusinessRepository;
import kr.or.kpf.lms.repository.entity.BizInstructorPbanc;
import kr.or.kpf.lms.repository.entity.BizOrganizationAply;
import kr.or.kpf.lms.repository.entity.BizPbancMaster;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BizPbancService extends CSServiceSupport {

    private static final String PREFIX_PBANC = "PAC";

    private final CommonBusinessRepository commonBusinessRepository;
    private final BizInstructorPbancRepository bizInstructorPbancRepository;
    private final BizPbancMasterRepository bizPbancMasterRepository;
    private final BizOrganizationAplyRepository bizOrganizationAplyRepository;
    private final BizPbancTmplService bizPbancTmplService;

    String temp;

    /**
     사업 공고 정보 생성
     */
    public BizPbancApiResponseVO createBizPbanc(BizPbancApiRequestVO requestObject) {
        temp = null;
        if(requestObject.getCkBox() != null) {
            for (String item : requestObject.getCkBox()) {
                if(temp == null  || temp.isEmpty()){
                    temp = item;
                }else{
                    temp = temp + ","+ item;
                }
            }
        }
        BizPbancMaster entity = BizPbancMaster.builder().build();
        if(temp == null){
            entity.setCkBox("99"); // 미선택시 기본 디폴트 값 (기본형 )
        }else{
            entity.setCkBox(temp);
        }
        BeanUtils.copyProperties(requestObject, entity);
        entity.setBizPbancNo(commonBusinessRepository.generateCode(PREFIX_PBANC));
        entity.setBizPbancRnd(commonBusinessRepository.generatePbancAutoIncrease(entity.getBizPbancType(), entity.getBizPbancYr()));
        BizPbancApiResponseVO result = BizPbancApiResponseVO.builder().build();

        /** 미디어교육 구분일 때, 템플릿에 따른 ctgr sub 분기 처리 (1: 사회미디어, 2: 학교미디어) */
        if (requestObject.getBizPbancCtgr() == 0) {
            if (requestObject.getBizPbancType() == 0){
                entity.setBizPbancCtgrSub(requestObject.getBizPbancCtgrSub());
            } else if (requestObject.getBizPbancType() == 1){
                entity.setBizPbancCtgrSub(1);
            } else {
                entity.setBizPbancCtgrSub(2);
            }
        } else entity.setBizPbancCtgrSub(0);

        BeanUtils.copyProperties(bizPbancMasterRepository.saveAndFlush(entity), result);

        if (result != null){
            if (result.getBizPbancType() == 1){
                /** 미디어교육 평생교실 */
                requestObject.getBizPbancTmpl1ApiRequestVO().setBizPbancNo(result.getBizPbancNo());
                bizPbancTmplService.createBizPbancTmpl1(requestObject.getBizPbancTmpl1ApiRequestVO());
            } else if (result.getBizPbancType() == 2){
                /** 미디어교육 운영학교 */
                requestObject.getBizPbancTmpl2ApiRequestVO().setBizPbancNo(result.getBizPbancNo());
                bizPbancTmplService.createBizPbancTmpl2(requestObject.getBizPbancTmpl2ApiRequestVO());
            } else if (result.getBizPbancType() == 3){
                /** 자유학기제 미디어 교육지원 */
                requestObject.getBizPbancTmpl3ApiRequestVO().setBizPbancNo(result.getBizPbancNo());
                bizPbancTmplService.createBizPbancTmpl3(requestObject.getBizPbancTmpl3ApiRequestVO());
            } else if (result.getBizPbancType() == 4){
                /** 팩트체크 교실 공고 */
                requestObject.getBizPbancTmpl4ApiRequestVO().setBizPbancNo(result.getBizPbancNo());
                bizPbancTmplService.createBizPbancTmpl4(requestObject.getBizPbancTmpl4ApiRequestVO());
            } else if(result.getBizPbancType() == 5){
                bizPbancTmplService.createBizPbancTmpl5(requestObject.getBizPbancTmpl5ApiRequestVO(), result.getBizPbancNo());
            }else if (result.getBizPbancType() == 0){
                /** 기본형 */
                requestObject.getBizPbancTmpl0ApiRequestVO().setBizPbancNo(result.getBizPbancNo());
                bizPbancTmplService.createBizPbancTmpl0(requestObject.getBizPbancTmpl0ApiRequestVO());
            }
        }
        return result;
    }

    /**
     사업 공고 정보 업데이트
     */
    public BizPbancApiResponseVO updateBizPbanc(BizPbancApiRequestVO requestObject) {
        return bizPbancMasterRepository.findOne(Example.of(BizPbancMaster.builder()
                        .bizPbancNo(requestObject.getBizPbancNo())
                        .build()))
                .map(bizPbancMaster -> {
                    copyNonNullObject(requestObject, bizPbancMaster);
                    temp = null;
                    if(requestObject.getCkBox() != null) {
                        for (String item : requestObject.getCkBox()) {
                            if(temp == null  || temp.isEmpty()){
                                temp = item;
                            }else{
                                temp = temp + ","+ item;
                            }
                        }
                    }
                    if(temp == null){
                        bizPbancMaster.setCkBox("99"); // 미선택시 기본 디폴트 값 (기본형 )
                    }else{
                        bizPbancMaster.setCkBox(temp);
                    }
                    BizPbancApiResponseVO result = BizPbancApiResponseVO.builder().build();
                    BeanUtils.copyProperties(bizPbancMasterRepository.saveAndFlush(bizPbancMaster), result);

                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3505, "해당 사업 공고 미존재"));
    }

    /**
     사업 공고 정보 삭제
     */
    public CSResponseVOSupport deleteBizPbanc(BizPbancApiRequestVO requestObject) {
        bizPbancMasterRepository.delete(bizPbancMasterRepository.findOne(Example.of(BizPbancMaster.builder()
                        .bizPbancNo(requestObject.getBizPbancNo())
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3507, "삭제된 사업 공고 입니다.")));

        /** 사업 공고 신청 - 임시 저장 상태 */
        bizOrganizationAplyRepository.deleteAll(bizOrganizationAplyRepository.findAll(Example.of(BizOrganizationAply.builder()
                        .bizPbancNo(requestObject.getBizPbancNo())
                        .bizOrgAplyStts(0)
                        .build())));

        /** 사업 공고 신청 - 반려 상태 */
        bizOrganizationAplyRepository.deleteAll(bizOrganizationAplyRepository.findAll(Example.of(BizOrganizationAply.builder()
                .bizPbancNo(requestObject.getBizPbancNo())
                .bizOrgAplyStts(3)
                .build())));

        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     사업 공고 리스트
     */
    public <T> Page<T> getBizPbancList(BizPbancViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonBusinessRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     사업 공고 강사 모집 진행 확인
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport checkBizInstructorPbanc(BizPbancApiRequestVO bizPbancApiRequestVO) {
        if (bizInstructorPbancRepository.findAll(Example.of(BizInstructorPbanc.builder()
                .bizPbancNo(bizPbancApiRequestVO.getBizPbancNo())
                .build())).isEmpty()) {
            return CSResponseVOSupport.of(KPF_RESULT.ERROR3605);
        } else {
            return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
        }
    }
}