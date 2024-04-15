package kr.or.kpf.lms.biz.contents.chapter.controller;

import kr.or.kpf.lms.biz.contents.chapter.service.ChapterService;
import kr.or.kpf.lms.biz.contents.chapter.vo.request.ChapterViewRequestVO;
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
import java.util.Optional;

/**
 * 콘텐츠 관리 > 콘텐츠 관리 View 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/contents/list/chapter")
public class ChapterViewController extends CSViewControllerSupport {

    private static final String CONTENTS = "views/contents/contents/";

    private final ChapterService chapterService;

    /**
     *
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path={"", "/page"})
    public String getList(HttpServletRequest request, Pageable pageable, Model model) {
        modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                .map(searchMap -> chapterService.getList((ChapterViewRequestVO) params(ChapterViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
        return new StringBuilder(CONTENTS).append("contentsView").toString();
    }
}
