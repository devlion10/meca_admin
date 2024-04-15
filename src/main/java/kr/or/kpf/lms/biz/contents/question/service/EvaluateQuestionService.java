package kr.or.kpf.lms.biz.contents.question.service;

import kr.or.kpf.lms.biz.contents.question.vo.request.EvaluateQuestionApiRequestVO;
import kr.or.kpf.lms.biz.contents.question.vo.request.EvaluateQuestionViewRequestVO;
import kr.or.kpf.lms.biz.contents.question.vo.response.EvaluateQuestionApiResponseVO;
import kr.or.kpf.lms.biz.contents.question.vo.response.EvaluateQuestionExcelVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonContentsRepository;
import kr.or.kpf.lms.repository.contents.EvaluateQuestionItemRepository;
import kr.or.kpf.lms.repository.contents.EvaluateQuestionMasterRepository;
import kr.or.kpf.lms.repository.entity.contents.EvaluateQuestionItem;
import kr.or.kpf.lms.repository.entity.contents.EvaluateQuestionMaster;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 콘텐츠 관리 > 강의 평가 관리 > 문항 관리 API 관련 Service
 */
@Service
@RequiredArgsConstructor
public class EvaluateQuestionService extends CSServiceSupport {

    private static final String PREFIX_QUESTION = "QUES";
    private static final String PREFIX_QUESTION_ITEM = "ITEM";

    /** 콘텐츠 관리 공통 */
    private final CommonContentsRepository commomContentsRepository;
    /** 강의평가 질문 관리 */
    private final EvaluateQuestionMasterRepository evaluateQuestionMasterRepository;
    /** 강의평가 질문 문항 */
    private final EvaluateQuestionItemRepository evaluateQuestionItemRepository;

    /**
     * 강의 평가 질문 정보 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(EvaluateQuestionViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commomContentsRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 강의 평가 질문 정보 생성
     *
     * @param evaluateQuestionApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public EvaluateQuestionApiResponseVO createQuestionInfo(EvaluateQuestionApiRequestVO evaluateQuestionApiRequestVO) {
        EvaluateQuestionMaster entity = EvaluateQuestionMaster.builder().build();
        copyNonNullObject(evaluateQuestionApiRequestVO, entity);
            EvaluateQuestionApiResponseVO result = EvaluateQuestionApiResponseVO.builder().build();
            entity.setQuestionSerialNo(commomContentsRepository.generateQuestionSerialNo(PREFIX_QUESTION));

            /** 강의 평가 질문 문항 생성 */
            if(evaluateQuestionApiRequestVO.getQuestionItemList() != null && evaluateQuestionApiRequestVO.getQuestionItemList().size() > 0) {
                List<EvaluateQuestionItem> questionItemList = evaluateQuestionApiRequestVO.getQuestionItemList().stream()
                        .map(item -> evaluateQuestionItemRepository.saveAndFlush(EvaluateQuestionItem.builder()
                                .questionSerialNo(entity.getQuestionSerialNo())
                                .questionItemSerialNo(commomContentsRepository.generateQuestionItemSerialNo(PREFIX_QUESTION_ITEM))
                                .questionSortNo(item.getSortNo())
                                .questionItemValue(item.getValue())
                                .build())).collect(Collectors.toList());
                /** 강의 평가 질문 문항 갯수 */
                entity.setQuestionItemCount(questionItemList.size());
            } else {
                /** 강의 평가 질문 문항 갯수 */
                entity.setQuestionItemCount(0);
            }
            /** 강의 평가 질문 정보 생성 */
            BeanUtils.copyProperties(evaluateQuestionMasterRepository.saveAndFlush(entity), result);
            return result;
    }

    /**
     * 강의 평가 질문 정보 업데이트
     *
     * @param evaluateQuestionApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public EvaluateQuestionApiResponseVO updateQuestionInfo(EvaluateQuestionApiRequestVO evaluateQuestionApiRequestVO) {
        return evaluateQuestionMasterRepository.findOne(Example.of(EvaluateQuestionMaster.builder()
                        .questionSerialNo(evaluateQuestionApiRequestVO.getQuestionSerialNo())
                        .build()))
                .map(evaluateQuestionMaster -> {
                    copyNonNullObject(evaluateQuestionApiRequestVO, evaluateQuestionMaster);
                    EvaluateQuestionApiResponseVO result = EvaluateQuestionApiResponseVO.builder().build();

                    /**
                     * 강의 평가 질문 문항 항목 업데이트
                     */
                    if(evaluateQuestionApiRequestVO.getQuestionItemList() != null && evaluateQuestionApiRequestVO.getQuestionItemList().size() == 0) {
                        /** 강의 평가 질문 문항 항목 초기화 */
                        evaluateQuestionItemRepository.deleteAll(evaluateQuestionItemRepository.findAll(Example.of(EvaluateQuestionItem.builder()
                                        .questionSerialNo(evaluateQuestionMaster.getQuestionSerialNo())
                                        .build())));
                        /** 강의 평가 질문 문항 갯수 */
                        evaluateQuestionMaster.setQuestionItemCount(0);
                    } else if(evaluateQuestionApiRequestVO.getQuestionItemList() != null && evaluateQuestionApiRequestVO.getQuestionItemList().size() > 0) {
                        /** 강의 평가 질문 문항 항목 초기화 */
                        evaluateQuestionItemRepository.deleteAll(evaluateQuestionItemRepository.findAll(Example.of(EvaluateQuestionItem.builder()
                                .questionSerialNo(evaluateQuestionMaster.getQuestionSerialNo())
                                .build())));
                        /** 강의 평가 질문 문항 항목 새로 갱신 */
                        List<EvaluateQuestionItem> questionItemList = evaluateQuestionApiRequestVO.getQuestionItemList().stream()
                                .map(item -> evaluateQuestionItemRepository.saveAndFlush(EvaluateQuestionItem.builder()
                                        .questionSerialNo(evaluateQuestionMaster.getQuestionSerialNo())
                                        .questionItemSerialNo(commomContentsRepository.generateQuestionItemSerialNo(PREFIX_QUESTION_ITEM))
                                        .questionSortNo(item.getSortNo())
                                        .questionItemValue(item.getValue())
                                        .build())).collect(Collectors.toList());
                        /** 강의 평가 질문 문항 갯수 */
                        evaluateQuestionMaster.setQuestionItemCount(questionItemList.size());
                    }

                    BeanUtils.copyProperties(evaluateQuestionMasterRepository.saveAndFlush(evaluateQuestionMaster), result);
                    return result;
                })
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2063, "해당 강의 평가 질문 미존재"));
    }

    /**
     * 강의 평가 질문 문항 정보 삭제
     *  
     * @param evaluateQuestionApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport deleteQuestionInfo(EvaluateQuestionApiRequestVO evaluateQuestionApiRequestVO) {
        /** 강의 평가 질문 문항 조회 */
        EvaluateQuestionMaster evaluateQuestionMaster = evaluateQuestionMasterRepository.findOne(Example.of(EvaluateQuestionMaster.builder()
                        .questionSerialNo(evaluateQuestionApiRequestVO.getQuestionSerialNo())
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2063, "해당 강의 평가 질문 미존재"));
        /** 강의 평가 질문 문항과 연결된 항목 정보 삭제 */
        if(evaluateQuestionMaster.getQuestionItemList().size() > 0) {
            evaluateQuestionItemRepository.deleteAll(evaluateQuestionMaster.getQuestionItemList());
        }
        /** 강의 평가 질문 문항 삭제 */
        evaluateQuestionMasterRepository.delete(evaluateQuestionMaster);

        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     엑셀 다운로드
     */
    public <T> List<EvaluateQuestionExcelVO> getExcel(EvaluateQuestionViewRequestVO requestObject) {
        List<EvaluateQuestionExcelVO> evaluateQuestionExcelVOList = (List<EvaluateQuestionExcelVO>) commomContentsRepository.findEntityListExcel(requestObject);
        return evaluateQuestionExcelVOList;
    }
}
