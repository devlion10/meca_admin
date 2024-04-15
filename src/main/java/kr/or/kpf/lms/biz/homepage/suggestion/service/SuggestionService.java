package kr.or.kpf.lms.biz.homepage.suggestion.service;

import kr.or.kpf.lms.biz.homepage.suggestion.vo.request.SuggestionApiRequestVO;
import kr.or.kpf.lms.biz.homepage.suggestion.vo.request.SuggestionViewRequestVO;
import kr.or.kpf.lms.biz.homepage.suggestion.vo.response.SuggestionApiResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.education.EducationSuggestionRepository;
import kr.or.kpf.lms.repository.entity.BizSurveyQitem;
import kr.or.kpf.lms.repository.entity.homepage.EducationSuggestion;
import kr.or.kpf.lms.repository.CommonHomepageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Optional;

/**
 * 홈페이지 관리 > 교육 주제 제안 관련 Service
 */
@Service
@RequiredArgsConstructor
public class SuggestionService extends CSServiceSupport {

    /** 홈페이지 관리 공통 Repository */
    private final CommonHomepageRepository commonHomepageRepository;
    /** 교육 주제 제안 Repository */
    private final EducationSuggestionRepository educationSuggestionRepository;

    /**
     * 교육 주제 제안 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(SuggestionViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonHomepageRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 교육 주제 제안 업데이트
     *
     * @param suggestionApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public SuggestionApiResponseVO updateSuggestion(SuggestionApiRequestVO suggestionApiRequestVO) {
        return educationSuggestionRepository.findOne(Example.of(EducationSuggestion.builder().sequenceNo(suggestionApiRequestVO.getSequenceNo()).build()))
                .map(suggestion -> {

                    copyNonNullObject(suggestionApiRequestVO, suggestion);
                    SuggestionApiResponseVO result = SuggestionApiResponseVO.builder().build();
                    BeanUtils.copyProperties(educationSuggestionRepository.saveAndFlush(suggestion), result);

                    return result;
                })
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7064, "해당 자주 묻는 질문 미존재"));
    }

    /**
     * 교육 주제 제안 댓글 작성
     *
     * @param suggestionApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public SuggestionApiResponseVO commentSuggestion(SuggestionApiRequestVO suggestionApiRequestVO) {
        return educationSuggestionRepository.findOne(Example.of(EducationSuggestion.builder().sequenceNo(suggestionApiRequestVO.getSequenceNo()).build()))
                .map(suggestion -> {
                    copyNonNullObject(suggestionApiRequestVO, suggestion);

                    if (suggestionApiRequestVO.getComment().equals("") || suggestionApiRequestVO.getComment().equals(" ")) {
                        suggestion.setCommentUserId(null);
                        suggestion.setComment(null);
                    }

                    SuggestionApiResponseVO result = SuggestionApiResponseVO.builder().build();
                    BeanUtils.copyProperties(educationSuggestionRepository.saveAndFlush(suggestion), result);

                    return result;
                })
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7064, "해당 자주 묻는 질문 미존재"));
    }

    /**
     * 교육 주제 제안 삭제
     */
    public CSResponseVOSupport deleteSuggestion(BigInteger sequenceNo) {
        educationSuggestionRepository.delete(educationSuggestionRepository.findOne(Example.of(EducationSuggestion.builder()
                                .sequenceNo(sequenceNo)
                                .build()))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7084, "존재하지 않는 교육 주제 제안")));
        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }
}
