package kr.or.kpf.lms.biz.homepage.suggestion.controller;

import kr.or.kpf.lms.biz.homepage.suggestion.service.SuggestionService;
import kr.or.kpf.lms.biz.homepage.suggestion.vo.request.SuggestionViewRequestVO;
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
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * 홈페이지 관리 > 교육 주제 제안 View 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/homepage/suggestion")
public class SuggestionViewController extends CSViewControllerSupport {

    private static final String SUGGESTION = "views/homepage/";

    private final SuggestionService suggestionService;

    /**
     * 홈페이지 관리 > 교육 주제 제안
     */
    @GetMapping(path={"", "page"})
    public String getList(HttpServletRequest request, Pageable pageable, Model model) {
        return new StringBuilder(SUGGESTION).append("proposition").toString();
    }

    /**
     * 홈페이지 관리 > 교육 주제 제안 조회 - 언론인
     */
    @GetMapping(path={"/journalist"})
    public String getJournalistList(HttpServletRequest request, Pageable pageable, Model model) {
        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("suggestionType", "1");
        modelSetting(model, Optional.ofNullable(requestParam)
                .map(searchMap -> suggestionService.getList((SuggestionViewRequestVO) params(SuggestionViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("SUGGESTION_TYPE"));
        return new StringBuilder(SUGGESTION).append("propView").toString();
    }

    /**
     * 홈페이지 관리 > 교육 주제 제안 조회 - 시민
     */
    @GetMapping(path={"/public"})
    public String getPublicList(HttpServletRequest request, Pageable pageable, Model model) {
        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("suggestionType", "2");
        modelSetting(model, Optional.ofNullable(requestParam)
                .map(searchMap -> suggestionService.getList((SuggestionViewRequestVO) params(SuggestionViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("SUGGESTION_TYPE"));
        return new StringBuilder(SUGGESTION).append("propView").toString();
    }
}
