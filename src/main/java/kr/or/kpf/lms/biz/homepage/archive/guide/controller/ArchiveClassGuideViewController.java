package kr.or.kpf.lms.biz.homepage.archive.guide.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.homepage.archive.guide.service.ArchiveClassGuideService;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.request.ArchiveClassGuideViewRequestVO;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * 수업 지도안 View 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/homepage/archive/class-guide")
public class ArchiveClassGuideViewController extends CSViewControllerSupport {

    private static final String CLASSGUIDE = "views/homepage/";

    private final ArchiveClassGuideService archiveClassGuideService;

    /**
     * 수업 지도안 > 1: 교사, 2: 학부모, 3: 기타(다문화/유아/일반) 조회
     */
    @GetMapping(path={"", "/page"})
    public String getList(HttpServletRequest request, Pageable pageable, Model model) {
        modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                .map(searchMap -> archiveClassGuideService.getList((ArchiveClassGuideViewRequestVO) params(ArchiveClassGuideViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("GUI_TYPE"));
        return new StringBuilder(CLASSGUIDE).append("eduPlan").toString();
    }

    /**
     * 수업 지도안 > 1: 교사, 2: 학부모, 3: 기타(다문화/유아/일반) 등록
     */
    @GetMapping(path={"/"})
    public String getWrite() {
        return new StringBuilder(CLASSGUIDE).append("eduPlanForm").toString();
    }

    /**
     * 수업 지도안 > 1: 교사, 2: 학부모, 3: 기타(다문화/유아/일반) 상세 조회 및 수정
     *
     * @param request
     * @param pageable
     * @param model
     * @param classGuideCode
     * @return
     */
    @GetMapping(path={"/{classGuideCode}"})
    public String getForm(HttpServletRequest request, Pageable pageable, Model model,
                                @Parameter(description = "수업 지도안 코드") @PathVariable(value = "classGuideCode", required = true) String classGuideCode) {
        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("classGuideCode", classGuideCode);
        modelSetting(model, Optional.ofNullable(requestParam)
                .map(searchMap -> archiveClassGuideService.getList((ArchiveClassGuideViewRequestVO) params(ArchiveClassGuideViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
        return new StringBuilder(CLASSGUIDE).append("eduPlanForm").toString();
    }
}
