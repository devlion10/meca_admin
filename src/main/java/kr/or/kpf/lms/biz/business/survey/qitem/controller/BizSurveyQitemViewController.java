package kr.or.kpf.lms.biz.business.survey.qitem.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.business.survey.qitem.service.BizSurveyQitemService;
import kr.or.kpf.lms.biz.business.survey.qitem.vo.request.BizSurveyQitemViewRequestVO;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/business/survey/qitem")

public class BizSurveyQitemViewController extends CSViewControllerSupport {
    private static final String BUSINESS = "views/business/survey/";
    private final BizSurveyQitemService bizSurveyQitemService;

    @GetMapping(path={"", "/page"})
    public String getList(HttpServletRequest request, Pageable pageable, Model model) {
        modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                .map(searchMap -> bizSurveyQitemService.getBizSurveyQitemList((BizSurveyQitemViewRequestVO) params(BizSurveyQitemViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
        return new StringBuilder(BUSINESS).append("surveyItem").toString();
    }

    @GetMapping(path={"/"})
    public String getWrite() {
        return new StringBuilder(BUSINESS).append("surveyItemForm").toString();
    }

    @GetMapping(path={"/{bizSurveyQitemNo}"})
    public String getForm(HttpServletRequest request, Pageable pageable, Model model,
                          @Parameter(description = "상호평가 문항 일련 번호") @PathVariable(value = "bizSurveyQitemNo", required = true) String bizSurveyQitemNo) {
        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("bizSurveyQitemNo", bizSurveyQitemNo);
        modelSetting(model, Optional.ofNullable(requestParam)
                .map(searchMap -> bizSurveyQitemService.getBizSurveyQitemList((BizSurveyQitemViewRequestVO) params(BizSurveyQitemViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
        return new StringBuilder(BUSINESS).append("surveyItemForm").toString();
    }

}
