package kr.or.kpf.lms.biz.statistics.survey.controller;

import kr.or.kpf.lms.biz.statistics.survey.service.SurveyStateService;
import kr.or.kpf.lms.biz.statistics.webuser.service.UserStateService;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 통계 관리 > 강의 평가 관리 View 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/statistics/survey")
public class SurveyStateViewController extends CSViewControllerSupport {
    private static final String STATISTICS = "views/statistics/";

    /**
     * 강의 평가 관리 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path = {"", "/", "/page"})
    public String getList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            return new StringBuilder(STATISTICS).append("surveyStatistics").toString();
        } catch (KPFException e1) {
            logger.error("{}- {}", e1.getClass().getCanonicalName(), e1.getMessage(), e1);
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            logger.error("{}- {}", e2.getClass().getCanonicalName(), e2.getMessage(), e2);
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }
}
