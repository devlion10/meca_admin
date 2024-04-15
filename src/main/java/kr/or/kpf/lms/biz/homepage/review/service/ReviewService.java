package kr.or.kpf.lms.biz.homepage.review.service;

import kr.or.kpf.lms.biz.homepage.review.vo.request.ReviewApiRequestVO;
import kr.or.kpf.lms.biz.homepage.review.vo.request.ReviewViewRequestVO;
import kr.or.kpf.lms.biz.homepage.review.vo.response.ReviewApiResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonHomepageRepository;
import kr.or.kpf.lms.repository.entity.BizPbancMaster;
import kr.or.kpf.lms.repository.entity.homepage.EducationReview;
import kr.or.kpf.lms.repository.homepage.EducationReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Optional;

/**
 * 홈페이지 관리 > 교육 후기 관련 Service
 */
@Service
@RequiredArgsConstructor
public class ReviewService extends CSServiceSupport {

    /** 홈페이지 관리 공통 Repository */
    private final CommonHomepageRepository commonHomepageRepository;
    private final EducationReviewRepository educationReviewRepository;

    /**
     * 교육 후기 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(ReviewViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonHomepageRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 교육 후기 생성
     *
     * @param reviewApiRequestVO
     * @return
     */
    public ReviewApiResponseVO createReview(ReviewApiRequestVO reviewApiRequestVO) {
        EducationReview entity = EducationReview.builder().build();
        copyNonNullObject(reviewApiRequestVO, entity);

        /** 조회수 '0' 셋팅 */
        entity.setViewCount(BigInteger.ZERO);
        ReviewApiResponseVO result = ReviewApiResponseVO.builder().build();

        BeanUtils.copyProperties(educationReviewRepository.saveAndFlush(entity), result);
        return result;
    }

    /**
     * 교육 후기 업데이트
     *
     * @param reviewApiRequestVO
     * @return
     */
    public ReviewApiResponseVO updateReview(ReviewApiRequestVO reviewApiRequestVO) {
        return educationReviewRepository.findOne(Example.of(EducationReview.builder().sequenceNo(reviewApiRequestVO.getSequenceNo()).build()))
                .map(educationReview -> {
                    copyNonNullObject(reviewApiRequestVO, educationReview);

                    ReviewApiResponseVO result = ReviewApiResponseVO.builder().build();
                    BeanUtils.copyProperties(educationReviewRepository.saveAndFlush(educationReview), result);

                    return result;
                })
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7104, "해당 교육 후기 미존재"));
    }

    /**
     교육 후기 삭제
     */
    public CSResponseVOSupport deleteInfo(ReviewApiRequestVO requestObject) {
        educationReviewRepository.delete(educationReviewRepository.findOne(Example.of(EducationReview.builder()
                        .sequenceNo(requestObject.getSequenceNo())
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7103, "이미 삭제된 교육 후기입니다.")));

        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }
}
