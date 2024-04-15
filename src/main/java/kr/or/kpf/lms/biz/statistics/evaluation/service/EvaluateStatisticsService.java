package kr.or.kpf.lms.biz.statistics.evaluation.service;

import com.google.gson.Gson;
import kr.or.kpf.lms.biz.statistics.evaluation.vo.EvaluateDetailStatisticsItemVO;
import kr.or.kpf.lms.biz.statistics.evaluation.vo.EvaluateDetailStatisticsVO;
import kr.or.kpf.lms.biz.statistics.evaluation.vo.request.EvaluateCurriculumStatisticsRequestVO;
import kr.or.kpf.lms.biz.statistics.evaluation.vo.request.EvaluateQuestionStatisticsRequestVO;
import kr.or.kpf.lms.biz.statistics.evaluation.vo.response.EvaluateCurriculumStatisticsViewResponseVO;
import kr.or.kpf.lms.biz.statistics.evaluation.vo.response.EvaluateQuestionStatisticsViewResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonStatisticsRepository;
import kr.or.kpf.lms.repository.contents.EvaluateMasterRepository;
import kr.or.kpf.lms.repository.education.CurriculumApplicationEvaluateRepository;
import kr.or.kpf.lms.repository.education.CurriculumMasterRepository;
import kr.or.kpf.lms.repository.entity.contents.EvaluateMaster;
import kr.or.kpf.lms.repository.entity.education.CurriculumApplicationEvaluate;
import kr.or.kpf.lms.repository.entity.education.CurriculumMaster;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 통계 관리 > 강의평가 통계 관련 Service
 */
@Service
@RequiredArgsConstructor
public class EvaluateStatisticsService extends CSServiceSupport {

    /** 통계 관리 공통 */
    private final CommonStatisticsRepository commonStatisticsRepository;

    private final CurriculumMasterRepository curriculumMasterRepository;

    private final EvaluateMasterRepository evaluateMasterRepository;

    private final CurriculumApplicationEvaluateRepository curriculumApplicationEvaluateRepository;

    /**
     * 강의 평가 정보 조회
     *
     * @param <T>
     * @param requestObject
     * @return
     */
    /** 설문지별 */
    public <T> Page<T> getQuestionStatistics(EvaluateQuestionStatisticsRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonStatisticsRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }
    /** 강의(과정)별 */
    public <T> Page<T> getCurriculumStatistics(EvaluateCurriculumStatisticsRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonStatisticsRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 강의 평가 관리 - 설문지별 조회 (상세)
     *
     * @param requestObject
     * @return
     */
    public List<EvaluateDetailStatisticsVO> getQuestionStatisticsDetail(EvaluateQuestionStatisticsRequestVO requestObject) {
        return getQuestionStatisticsDetail(requestObject.getEvaluateSerialNo());
    }

    /**
     * 강의 평가 관리 - 강좌(과정)별 조회 (상세)
     *
     * @param requestObject
     * @return
     */
    public List<List<EvaluateDetailStatisticsVO>> getCurriculumStatisticsDetail(EvaluateCurriculumStatisticsRequestVO requestObject) {
        return curriculumApplicationEvaluateRepository.findAll(Example.of(CurriculumApplicationEvaluate.builder()
                        .curriculumCode(curriculumMasterRepository.findOne(Example.of(CurriculumMaster.builder()
                                        .curriculumCode(requestObject.getCurriculumCode())
                                        .build()))
                                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9009, "존재하지 않는 교육 과정 입니다.")).getCurriculumCode())
                        .build())).stream().map(CurriculumApplicationEvaluate::getEvaluateSerialNo).distinct()
                .map(this::getQuestionStatisticsDetail)
                .collect(Collectors.toList());
    }

    /**
     * 강의 평가 관리 - 설문지별 조회 (상세)
     *
     * @param evaluateSerialNo
     * @return
     */
    public List<EvaluateDetailStatisticsVO> getQuestionStatisticsDetail(String evaluateSerialNo) {
        EvaluateMaster evaluateMaster = evaluateMasterRepository.findOne(Example.of(EvaluateMaster.builder()
                .evaluateSerialNo(evaluateSerialNo)
                .build())).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9009, "존재하지 않는 강의 평가 입니다."));

        return evaluateMaster.getEvaluateQuestionList().stream()
                .map(evaluateQuestion -> EvaluateDetailStatisticsVO.builder()
                        .evaluateSerialNo(evaluateQuestion.getEvaluateSerialNo())
                        .evaluateTitle(evaluateMaster.getEvaluateTitle())
                        .evaluateType(evaluateMaster.getEvaluateType())
                        .questionType(evaluateQuestion.getEvaluateQuestionMaster().getQuestionType())
                        .questionSerialNo(evaluateQuestion.getEvaluateQuestionMaster().getQuestionSerialNo())
                        .questionTitle(evaluateQuestion.getEvaluateQuestionMaster().getQuestionTitle())
                        .evaluateStatisticsItems(evaluateQuestion.getEvaluateQuestionMaster().getQuestionItemList().stream()
                                .map(evaluateQuestionItem -> EvaluateDetailStatisticsItemVO.builder()
                                        .questionItemValue(evaluateQuestionItem.getQuestionItemValue())
                                        .count(0)
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList()).stream().peek(data -> {
                    if ("1".equals(data.getQuestionType())) { /** 단일 선택 */
                        data.setEvaluateStatisticsItems(data.getEvaluateStatisticsItems().stream()
                                .peek(evaluateDetailStatisticsItemVO -> evaluateDetailStatisticsItemVO.setCount(curriculumApplicationEvaluateRepository.findAll(Example.of(CurriculumApplicationEvaluate.builder()
                                        .evaluateSerialNo(evaluateSerialNo)
                                        .questionSerialNo(data.getQuestionSerialNo())
                                        .answerQuestionItemValue(new Gson().toJson(Arrays.asList(evaluateDetailStatisticsItemVO.getQuestionItemValue())))
                                        .build())).size()))
                                .collect(Collectors.toList()));
                    } else if ("2".equals(data.getQuestionType())) { /** 다중 선택 */
                        data.setEvaluateStatisticsItems(data.getEvaluateStatisticsItems().stream()
                                .peek(evaluateDetailStatisticsItemVO -> evaluateDetailStatisticsItemVO.setCount((int) curriculumApplicationEvaluateRepository.findAll(Example.of(CurriculumApplicationEvaluate.builder()
                                                .evaluateSerialNo(evaluateSerialNo)
                                                .questionSerialNo(data.getQuestionSerialNo())
                                                .build())).stream()
                                        .filter(curriculumApplicationEvaluate -> new Gson().fromJson(curriculumApplicationEvaluate.getAnswerQuestionItemValue(), List.class).contains(evaluateDetailStatisticsItemVO.getQuestionItemValue()))
                                        .count()))
                                .collect(Collectors.toList()));
                    } else if ("3".equals(data.getQuestionType())) { /** 단답형 */
                        data.setEvaluateStatisticsItems(curriculumApplicationEvaluateRepository.findAll(Example.of(CurriculumApplicationEvaluate.builder()
                                        .evaluateSerialNo(evaluateSerialNo)
                                        .questionSerialNo(data.getQuestionSerialNo())
                                        .build())).stream().map(curriculumApplicationEvaluate -> EvaluateDetailStatisticsItemVO.builder()
                                        .count(1)
                                        .questionItemValue(new Gson().fromJson(curriculumApplicationEvaluate.getAnswerQuestionItemValue(), List.class).get(0).toString())
                                        .build())
                                .collect(Collectors.toList()));
                    } else if ("4".equals(data.getQuestionType())) { /** 서술형 */
                        data.setEvaluateStatisticsItems(curriculumApplicationEvaluateRepository.findAll(Example.of(CurriculumApplicationEvaluate.builder()
                                        .evaluateSerialNo(evaluateSerialNo)
                                        .questionSerialNo(data.getQuestionSerialNo())
                                        .build())).stream().map(curriculumApplicationEvaluate -> EvaluateDetailStatisticsItemVO.builder()
                                        .count(1)
                                        .questionItemValue(new Gson().fromJson(curriculumApplicationEvaluate.getAnswerQuestionItemValue(), List.class).get(0).toString())
                                        .build())
                                .collect(Collectors.toList()));
                    }
                }).collect(Collectors.toList());
    }

    /**
     엑셀 다운로드
     */
    /** 설문지별 */
    public <T> List<EvaluateQuestionStatisticsViewResponseVO> getQuestionExcel(EvaluateQuestionStatisticsRequestVO requestObject) {
        List<EvaluateQuestionStatisticsViewResponseVO> evaluateExcelVOList = (List<EvaluateQuestionStatisticsViewResponseVO>) commonStatisticsRepository.excelDownload(requestObject);
        return evaluateExcelVOList;
    }
    /** 강의(과정)별 */
    public <T> List<EvaluateCurriculumStatisticsViewResponseVO> getCurriculumExcel(EvaluateCurriculumStatisticsRequestVO requestObject) {
        List<EvaluateCurriculumStatisticsViewResponseVO> evaluateExcelVOList = (List<EvaluateCurriculumStatisticsViewResponseVO>) commonStatisticsRepository.excelDownload(requestObject);
        return evaluateExcelVOList;
    }

}
