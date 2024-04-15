package kr.or.kpf.lms.biz.statistics.survey.service;

import kr.or.kpf.lms.biz.statistics.survey.vo.request.SurveyApplyStateRequestVO;
import kr.or.kpf.lms.biz.statistics.survey.vo.request.SurveyQuestionStateRequestVO;
import kr.or.kpf.lms.biz.statistics.survey.vo.response.SurveyApplyStateResponseVO;
import kr.or.kpf.lms.biz.statistics.survey.vo.response.SurveyQuestionStateResponseVO;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 통계 관리 > 상호평가 통계 관련 Service
 */
@Service
@RequiredArgsConstructor
public class SurveyStateService extends CSServiceSupport {
    /** 통계 관리 공통 */
    private final CommonStatisticsRepository commonStatisticsRepository;

    /**
     * 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    /** 평가지별 */
    public <T> Page<T> getQuestionList(SurveyQuestionStateRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonStatisticsRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }
    /** 신청서별 */
    public <T> Page<T> getApplyList(SurveyApplyStateRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonStatisticsRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     엑셀 다운로드
     */
    /** 평가지별 */
    public <T> List<SurveyQuestionStateResponseVO> getQuestionExcel(SurveyQuestionStateRequestVO requestObject) {
        List<SurveyQuestionStateResponseVO> evaluateExcelVOList = (List<SurveyQuestionStateResponseVO>) commonStatisticsRepository.excelDownload(requestObject);
        return evaluateExcelVOList;
    }
    /** 신청서별 */
    public <T> List<SurveyApplyStateResponseVO> getApplyExcel(SurveyApplyStateRequestVO requestObject) {
        List<SurveyApplyStateResponseVO> evaluateExcelVOList = (List<SurveyApplyStateResponseVO>) commonStatisticsRepository.excelDownload(requestObject);
        return evaluateExcelVOList;
    }
}
