package kr.or.kpf.lms.biz.homepage.archive.guide.classsubject.controller;

import kr.or.kpf.lms.biz.homepage.archive.guide.classsubject.service.ClassSubjectService;
import kr.or.kpf.lms.biz.homepage.archive.guide.classsubject.vo.request.ClassSubjectViewRequestVO;
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
 * 수업 지도안 View 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/homepage/archive/class-guide/class-subject")
public class ClassSubjectViewController extends CSViewControllerSupport {

    private static final String CLASSGUIDE = "views/homepage/";

    private final ClassSubjectService classSubjectService;

    /**
     * 수업 지도안 과목 조회
     */
    @GetMapping(path={"", "/page"})
    public String getList(HttpServletRequest request, Pageable pageable, Model model) {
        modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                .map(searchMap -> classSubjectService.getList((ClassSubjectViewRequestVO) params(ClassSubjectViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
        return new StringBuilder(CLASSGUIDE).append("classSubject").toString();
    }
}
