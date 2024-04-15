package kr.or.kpf.lms.biz.statistics.adminuser.controller;

import kr.or.kpf.lms.biz.statistics.adminuser.service.StatisticsAdminUserService;
import kr.or.kpf.lms.biz.statistics.adminuser.vo.request.StatisticsAdminUserViewRequestVO;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
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
 * 통계 관리 > 관리자 접속 이력 View 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/statistics/admin-user")
public class StatisticsAdminUserViewController extends CSViewControllerSupport {
    private static final String STATISTICS = "views/statistics/";

    private final StatisticsAdminUserService statisticsAdminUserService;

    /**
     * 통계 관리 > 관리자 접속 이력 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path = {"", "/page"})
    public String getList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                    .map(searchMap -> statisticsAdminUserService.getList((StatisticsAdminUserViewRequestVO) params(StatisticsAdminUserViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
            return new StringBuilder(STATISTICS).append("adminStatistics").toString();
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
