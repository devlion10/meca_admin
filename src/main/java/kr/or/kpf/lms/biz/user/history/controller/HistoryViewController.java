package kr.or.kpf.lms.biz.user.history.controller;

import kr.or.kpf.lms.biz.business.instructor.identify.service.BizInstructorIdentifyService;
import kr.or.kpf.lms.biz.education.application.service.EducationApplicationService;
import kr.or.kpf.lms.biz.user.history.vo.FormeBizlecinfoViewRequestVO;
import kr.or.kpf.lms.biz.user.history.vo.InstructorLctrViewRequestVO;
import kr.or.kpf.lms.biz.user.history.vo.JournalismschoolViewRequestVO;
import kr.or.kpf.lms.biz.user.instructor.service.InstructorService;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
 * 사용자 관리 > 이력 관리 View 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/user/history")
public class HistoryViewController extends CSViewControllerSupport {
    private static final String WEB_USER = "views/user/history/";
    private final BizInstructorIdentifyService bizInstructorIdentifyService;
    private final EducationApplicationService educationApplicationService;
    private final InstructorService instructorService;

    /**
     * 사용자 관리 > 이력 관리 > 강의이력
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path = {"/instructor"})
    public String getInstructorList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                    .map(searchMap -> bizInstructorIdentifyService.getBizInstructorIdentifyList((FormeBizlecinfoViewRequestVO) params(FormeBizlecinfoViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("WEB_USER_ROLE", "BIZ_AUTH", "USER_STATE", "EASY_LGN_CD"));
            return new StringBuilder(WEB_USER).append("instructor").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /**
     * 사용자 관리 > 이력 관리 > 수강이력
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path = {"/journalismschool"})
    public String getJournalismschoolList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                    .map(searchMap -> educationApplicationService.getJournalismschoolList((JournalismschoolViewRequestVO) params(JournalismschoolViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("WEB_USER_ROLE", "BIZ_AUTH", "USER_STATE", "EASY_LGN_CD"));
            return new StringBuilder(WEB_USER).append("journalismschool").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /**
     * 사용자 관리 > 이력 관리 > 강의 이력(언론인)
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path = {"/lctrhistory"})
    public String getLctrHistorylList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                    .map(searchMap -> instructorService.getJournaliLctrList((InstructorLctrViewRequestVO) params(InstructorLctrViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("WEB_USER_ROLE", "BIZ_AUTH", "USER_STATE", "EASY_LGN_CD"));
            return new StringBuilder(WEB_USER).append("lctrhistory").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }
}
