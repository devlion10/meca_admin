package kr.or.kpf.lms.biz.business.survey.service;

import kr.or.kpf.lms.biz.business.survey.vo.request.BizSurveyApiRequestVO;
import kr.or.kpf.lms.biz.business.survey.vo.request.BizSurveyViewRequestVO;
import kr.or.kpf.lms.biz.business.survey.vo.response.BizSurveyApiResponseVO;
import kr.or.kpf.lms.biz.business.survey.vo.response.BizSurveyExcelVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.BizSurveyMasterRepository;
import kr.or.kpf.lms.repository.BizSurveyRepository;
import kr.or.kpf.lms.repository.CommonBusinessRepository;
import kr.or.kpf.lms.repository.entity.BizSurvey;
import kr.or.kpf.lms.repository.entity.BizSurveyMaster;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BizSurveyService extends CSServiceSupport {

    private final CommonBusinessRepository commonBusinessRepository;

    private final BizSurveyRepository bizSurveyRepository;
    private final BizSurveyMasterRepository bizSurveyMasterRepository;
    private static final String PREFIX_SURVEY_IDENTIFY = "BSQ";
    /**
     상호평가 정보 생성
     */
    public BizSurveyApiResponseVO createBizSurvey(BizSurveyApiRequestVO requestObject) {
        BizSurvey entity = BizSurvey.builder().build();
        BeanUtils.copyProperties(requestObject, entity);
        entity.setBizSurveyNo(commonBusinessRepository.generateCode(PREFIX_SURVEY_IDENTIFY));
        BizSurveyApiResponseVO result = BizSurveyApiResponseVO.builder().build();
        BeanUtils.copyProperties(bizSurveyRepository.saveAndFlush(entity), result);
        if(!result.equals(null) && !requestObject.getBizSurveyQitemNo().equals(null)){
            for(String qItemNo : requestObject.getBizSurveyQitemNo()){
                BizSurveyMaster surveyItem = new BizSurveyMaster().builder()
                        .bizSurveyQitemNo(qItemNo)
                        .bizSurveyNo(result.getBizSurveyNo())
                        .build();
                bizSurveyMasterRepository.saveAndFlush(surveyItem);
            }
        }

        return result;
    }

    /**
     상호평가 정보 업데이트
     */
    public BizSurveyApiResponseVO updateBizSurvey(BizSurveyApiRequestVO requestObject) {
        return bizSurveyRepository.findOne(Example.of(BizSurvey.builder()
                        .bizSurveyNo(requestObject.getBizSurveyNo())
                        .build()))
                .map(bizSurvey -> {
                    bizSurveyMasterRepository.deleteAll(bizSurveyMasterRepository.findAll(Example.of(BizSurveyMaster.builder()
                            .bizSurveyNo(requestObject.getBizSurveyNo())
                            .build())));
                    for(String qItemNo : requestObject.getBizSurveyQitemNo()){
                        BizSurveyMaster surveyItem = new BizSurveyMaster().builder()
                                .bizSurveyQitemNo(qItemNo)
                                .bizSurveyNo(requestObject.getBizSurveyNo())
                                .build();
                        bizSurveyMasterRepository.saveAndFlush(surveyItem);
                    }
                    copyNonNullObject(requestObject, bizSurvey);

                    BizSurveyApiResponseVO result = BizSurveyApiResponseVO.builder().build();
                    BeanUtils.copyProperties(bizSurveyRepository.saveAndFlush(bizSurvey), result);

                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3585, "해당 사업 공고 미존재"));
    }

    /**
     상호평가 정보 삭제
     */
    public CSResponseVOSupport deleteBizSurvey(BizSurveyApiRequestVO requestObject) {

        bizSurveyMasterRepository.deleteAll(bizSurveyMasterRepository.findAll(Example.of(BizSurveyMaster.builder()
                .bizSurveyNo(requestObject.getBizSurveyNo())
                .build())));

        bizSurveyRepository.delete(bizSurveyRepository.findOne(Example.of(BizSurvey.builder()
                        .bizSurveyNo(requestObject.getBizSurveyNo())
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3587, "삭제된 사업 공고 입니다.")));
        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     상호평가 리스트
     */
    public <T> Page<T> getBizSurveyList(BizSurveyViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonBusinessRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     상호평가 상세
     */
    public <T> T getBizSurvey(BizSurveyViewRequestVO requestObject) {
        return (T) commonBusinessRepository.findEntity(requestObject);
    }
    /**
     엑셀 다운로드
     */
    public <T> List<BizSurveyExcelVO> getExcel(BizSurveyViewRequestVO requestObject) {
        List<BizSurveyExcelVO> bizSurveyExcelVOList = (List<BizSurveyExcelVO>) commonBusinessRepository.findEntityListExcel(requestObject);
        return bizSurveyExcelVOList;
    }

    public CSResponseVOSupport deleteBizSurveyMaster( String bizSurveyNo, String bizSurveyQitemNo) {

        bizSurveyMasterRepository.delete(bizSurveyMasterRepository.findOne(Example.of(BizSurveyMaster.builder()
                .bizSurveyNo(bizSurveyNo)
                .bizSurveyQitemNo(bizSurveyQitemNo)
                .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3587, "삭제된 사업 공고 입니다.")));

        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }
}
