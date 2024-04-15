package kr.or.kpf.lms.biz.business.survey.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.business.survey.service.BizSurveyService;
import kr.or.kpf.lms.biz.business.survey.vo.request.BizSurveyViewRequestVO;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/business/survey/list")

public class BizSurveyViewController extends CSViewControllerSupport {
    private static final String BUSINESS = "views/business/survey/";
    private final BizSurveyService bizSurveyService;

    @GetMapping(path={"", "/page"})
    public String getList(HttpServletRequest request, Pageable pageable, Model model) {
        modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                .map(searchMap -> bizSurveyService.getBizSurveyList((BizSurveyViewRequestVO) params(BizSurveyViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
        return new StringBuilder(BUSINESS).append("survey").toString();
    }

    @GetMapping(path={"/"})
    public String getWrite() {
        return new StringBuilder(BUSINESS).append("surveyForm").toString();
    }

    @GetMapping(path={"/{bizSurveyNo}"})
    public String getForm(HttpServletRequest request, Pageable pageable, Model model,
                          @Parameter(description = "상호평가 일련 번호") @PathVariable(value = "bizSurveyNo", required = true) String bizSurveyNo) {
        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("bizSurveyNo", bizSurveyNo);
        modelSetting(model, Optional.ofNullable(requestParam)
                .map(searchMap -> bizSurveyService.getBizSurveyList((BizSurveyViewRequestVO) params(BizSurveyViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
        return new StringBuilder(BUSINESS).append("surveyForm").toString();
    }

}
