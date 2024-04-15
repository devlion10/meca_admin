package kr.or.kpf.lms.biz.homepage.review.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.homepage.review.service.ReviewService;
import kr.or.kpf.lms.biz.homepage.review.vo.request.ReviewViewRequestVO;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * 홈페이지 관리 > 교육 후기 View 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/homepage/review")
public class ReviewViewController extends CSViewControllerSupport {

    private static final String REVIEW = "views/homepage/";

    private final ReviewService reviewService;

    /**
     * 홈페이지 관리 > 교육 후기 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path={"", "/page"})
    public String getList(HttpServletRequest request, Pageable pageable, Model model) {
        modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                .map(searchMap -> reviewService.getList((ReviewViewRequestVO) params(ReviewViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("TEAM_CTGR", "CON_TEXT_TYPE", "EDU_REVIEW_TYPE"));
        return new StringBuilder(REVIEW).append("eduReview").toString();
    }

    /**
     * 홈페이지 관리 > 교육 후기 등록
     */
    @GetMapping(path={"/"})
    public String getWrite(Model model) {
        commonCodeSetting(model, Arrays.asList("TEAM_CTGR", "EDU_REVIEW_TYPE"));
        return new StringBuilder(REVIEW).append("eduReviewForm").toString();
    }

    /**
     * 홈페이지 관리 > 교육 후기 상세 및 수정
     *
     * @param request
     * @param pageable
     * @param model
     * @param sequenceNo
     * @return
     */
    @GetMapping(path={"/{sequenceNo}"})
    public String getForm(HttpServletRequest request, Pageable pageable, Model model,
                                @Parameter(description = "교육 후기 시리얼 번호") @PathVariable(value = "sequenceNo", required = true) BigInteger sequenceNo) {
        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("sequenceNo", sequenceNo);
        modelSetting(model, Optional.ofNullable(requestParam)
                .map(searchMap -> reviewService.getList((ReviewViewRequestVO) params(ReviewViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("TEAM_CTGR", "EDU_REVIEW_TYPE"));
        return new StringBuilder(REVIEW).append("eduReviewForm").toString();
    }
}
