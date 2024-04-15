package kr.or.kpf.lms.biz.contents.evaluate.service;

import kr.or.kpf.lms.biz.contents.evaluate.vo.request.EvaluateApiRequestVO;
import kr.or.kpf.lms.biz.contents.evaluate.vo.request.EvaluateViewRequestVO;
import kr.or.kpf.lms.biz.contents.evaluate.vo.response.EvaluateApiResponseVO;
import kr.or.kpf.lms.biz.contents.evaluate.vo.response.EvaluateExcelVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonContentsRepository;
import kr.or.kpf.lms.repository.contents.EvaluateMasterRepository;
import kr.or.kpf.lms.repository.contents.EvaluateQuestionMasterRepository;
import kr.or.kpf.lms.repository.contents.EvaluateQuestionRepository;
import kr.or.kpf.lms.repository.entity.contents.EvaluateMaster;
import kr.or.kpf.lms.repository.entity.contents.EvaluateQuestion;
import kr.or.kpf.lms.repository.entity.contents.EvaluateQuestionMaster;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 콘텐츠 관리 > 강의 평가 관리 관련 Service
 */
@Service
@RequiredArgsConstructor
public class EvaluateService extends CSServiceSupport {

    private static final String PREFIX_EVALUATE = "EVAL";

    /** 콘텐츠 관리 공통 */
    private final CommonContentsRepository commonContentsRepository;
    /** 강의 평가 */
    private final EvaluateMasterRepository evaluateMasterRepository;
    /** 강의 평가 연결 질문 목록 */
    private final EvaluateQuestionRepository evaluateQuestionRepository;
    /** 강의 평가 질문 */
    private final EvaluateQuestionMasterRepository evaluateQuestionMasterRepository;

    /**
     * 강의 평가 정보 조회
     *
     * @param <T>
     * @param requestObject
     * @return
     */
    public <T> Page<T> getEvaluateList(EvaluateViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonContentsRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 강의 평가 정보 생성
     *
     * @param evaluateApiRequestVO
     * @return
     */
    public EvaluateApiResponseVO createEvaluateInfo(EvaluateApiRequestVO evaluateApiRequestVO) {
        EvaluateMaster entity = EvaluateMaster.builder().build();
        copyNonNullObject(evaluateApiRequestVO, entity);
        if (evaluateMasterRepository.findOne(Example.of(EvaluateMaster.builder()
                                                            .evaluateTitle(evaluateApiRequestVO.getEvaluateTitle())
                                                            .build()))
                .isPresent()) { /** 기존 등록된 시험 정보 확인 */
            throw new KPFException(KPF_RESULT.ERROR2022, "동일 강의 평가 정보 존재(시험 생성 시 RM 적용 검토 필요). 단순 시험 명으로 Validation 체크는 문제가 있음.");
        } else {
            EvaluateApiResponseVO result = EvaluateApiResponseVO.builder().build();
            entity.setEvaluateSerialNo(commonContentsRepository.generateEvaluateSerialNo(PREFIX_EVALUATE));
            BeanUtils.copyProperties(evaluateMasterRepository.saveAndFlush(entity), result);

            /** 강의평가 연결 질문 목록 새로 갱신 */
            evaluateApiRequestVO.getQuestionList().stream()
                    .map(data -> {
                        EvaluateQuestion imsiQuestion = EvaluateQuestion.builder()
                                .evaluateSerialNo(entity.getEvaluateSerialNo())
                                .questionSerialNo(data.getValue())
                                .build();
                        return evaluateQuestionRepository.findOne(Example.of(imsiQuestion))
                                .orElseGet(() -> { /** 연결된 질문 정보가 존재하지 않으면... */
                                    imsiQuestion.setSortNo(data.getSortNo());
                                    /** 관련 질문 정보가 존재하는지 확인 */
                                    evaluateQuestionMasterRepository.findOne(Example.of(EvaluateQuestionMaster.builder()
                                                    .questionSerialNo(data.getValue())
                                                    .build()))
                                            .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2063, "관련 강의평가 질문 정보가 존재하지 않습니다. (" + data.getValue() + ")"));
                                    /** 생성된 콘텐츠와 해당 챕터 연결 */
                                    return evaluateQuestionRepository.save(imsiQuestion);
                                });
                    }).collect(Collectors.toList());

            return result;
        }
    }

    /**
     * 강의 평가 정보 수정
     *
     * @param evaluateApiRequestVO
     * @return
     */
    public EvaluateApiResponseVO updateEvaluateInfo(EvaluateApiRequestVO evaluateApiRequestVO) {
        return evaluateMasterRepository.findOne(Example.of(EvaluateMaster.builder()
                                                            .evaluateSerialNo(evaluateApiRequestVO.getEvaluateSerialNo())
                                                            .build()))
                .map(evaluateMaster -> {
                    copyNonNullObject(evaluateApiRequestVO, evaluateMaster);

                    EvaluateApiResponseVO result = EvaluateApiResponseVO.builder().build();
                    BeanUtils.copyProperties(evaluateMasterRepository.saveAndFlush(evaluateMaster), result);

                    if(evaluateApiRequestVO.getQuestionList() != null && evaluateApiRequestVO.getQuestionList().size() == 0) {
                        /** 강의 평가 연결 질문 기존 목록 초기화 */
                        evaluateQuestionRepository.deleteAll(
                                evaluateQuestionRepository.findAll(Example.of(EvaluateQuestion.builder()
                                        .evaluateSerialNo(evaluateMaster.getEvaluateSerialNo())
                                        .build())));
                    }else if(evaluateApiRequestVO.getQuestionList() != null && evaluateApiRequestVO.getQuestionList().size() > 0) {
                        /** 강의 평가 연결 질문 기존 목록 초기화 */
                        evaluateQuestionRepository.deleteAll(
                                evaluateQuestionRepository.findAll(Example.of(EvaluateQuestion.builder()
                                        .evaluateSerialNo(evaluateMaster.getEvaluateSerialNo())
                                        .build())));
                        /** 강의평가 연결 질문 목록 새로 갱신 */
                        evaluateApiRequestVO.getQuestionList().stream()
                                .map(data -> {
                                    EvaluateQuestion imsiQuestion = EvaluateQuestion.builder()
                                            .evaluateSerialNo(evaluateMaster.getEvaluateSerialNo())
                                            .questionSerialNo(data.getValue())
                                            .build();
                                    return evaluateQuestionRepository.findOne(Example.of(imsiQuestion))
                                            .orElseGet(() -> { /** 연결된 질문 정보가 존재하지 않으면... */
                                                imsiQuestion.setSortNo(data.getSortNo());
                                                /** 관련 질문 정보가 존재하는지 확인 */
                                                evaluateQuestionMasterRepository.findOne(Example.of(EvaluateQuestionMaster.builder()
                                                                .questionSerialNo(data.getValue())
                                                                .build()))
                                                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2063, "관련 강의평가 질문 정보가 존재하지 않습니다. (" + data.getValue() + ")"));
                                                /** 생성된 콘텐츠와 해당 챕터 연결 */
                                                return evaluateQuestionRepository.save(imsiQuestion);
                                            });
                                }).collect(Collectors.toList());
                    }

                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2023, "해당 강의 평가 정보 미존재"));
    }

    /**
     엑셀 다운로드
     */
    public <T> List<EvaluateExcelVO> getExcel(EvaluateViewRequestVO requestObject) {
        List<EvaluateExcelVO> evaluateExcelVOList = (List<EvaluateExcelVO>) commonContentsRepository.findEntityListExcel(requestObject);
        return evaluateExcelVOList;
    }
}
