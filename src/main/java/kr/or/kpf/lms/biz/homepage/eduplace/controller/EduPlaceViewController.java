package kr.or.kpf.lms.biz.homepage.eduplace.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.homepage.eduplace.service.EduPlaceService;
import kr.or.kpf.lms.biz.homepage.eduplace.vo.request.EduPlaceViewRequestVO;
import kr.or.kpf.lms.biz.homepage.event.service.EventService;
import kr.or.kpf.lms.biz.homepage.event.vo.request.EventViewRequestVO;
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
 * 홈페이지 관리 > 교육장 사용신청 관리 View 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/homepage/edu-center")
public class EduPlaceViewController extends CSViewControllerSupport {

    private static final String EDUCENTER = "views/homepage/";
    private final EduPlaceService eduPlaceService;

    /**
     * 홈페이지 관리 > 게시판 관리 조회
     */
    @GetMapping(path={"", "/page"})
    public String getList(HttpServletRequest request, Pageable pageable, Model model) {
        modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                .map(searchMap -> eduPlaceService.getList((EduPlaceViewRequestVO) params(EduPlaceViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
        return new StringBuilder(EDUCENTER).append("useApplication").toString();
    }

    /**
     * 홈페이지 관리 > 게시판 관리 등록
     */
    @GetMapping(path={"/"})
    public String getWrite() {
        return new StringBuilder(EDUCENTER).append("useApplicationForm").toString();
    }

    /**
     * 홈페이지 관리 > 게시판 관리 상세 및 수정
     */
    @GetMapping(path={"/{sequenceNo}"})
    public String getForm(HttpServletRequest request, Pageable pageable, Model model,
                          @Parameter(description = "일련 번호") @PathVariable(value = "sequenceNo", required = true) Long sequenceNo) {
        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("sequenceNo", sequenceNo);
        modelSetting(model, Optional.ofNullable(requestParam)
                .map(searchMap -> eduPlaceService.getList((EduPlaceViewRequestVO) params(EduPlaceViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
        return new StringBuilder(EDUCENTER).append("useApplicationForm").toString();
    }
}