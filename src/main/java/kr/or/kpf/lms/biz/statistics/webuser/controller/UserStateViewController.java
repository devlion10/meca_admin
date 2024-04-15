package kr.or.kpf.lms.biz.statistics.webuser.controller;

import kr.or.kpf.lms.biz.statistics.webuser.service.UserStateService;
import kr.or.kpf.lms.biz.statistics.webuser.vo.request.UserStateRequestVO;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.common.support.Code;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import kr.or.kpf.lms.repository.entity.user.LmsUser;
import kr.or.kpf.lms.repository.user.LmsUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
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
 * 통계 관리 > 이용 통계 관리 View 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/statistics/web-user")
public class UserStateViewController extends CSViewControllerSupport {
    private static final String STATISTICS = "views/statistics/";
    private final UserStateService userStateService;
    private final LmsUserRepository lmsUserRepository;

    /**
     * 이용 통계 관리 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path = {"", "/", "/page"})
    public String getList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            Long countUser = lmsUserRepository.findAll(Example.of(LmsUser.builder()
                    .state(Code.USER_STATE.GENERAL.enumCode)
                    .build())).stream().count();
            model.addAttribute("countUser", countUser);

            Long countWithdrawalUser = lmsUserRepository.findAll(Example.of(LmsUser.builder()
                    .state(Code.USER_STATE.WITHDRAWAL.enumCode)
                    .build())).stream().count();
            model.addAttribute("countWithdrawalUser", countWithdrawalUser);

            Long countDormancyUser = lmsUserRepository.findAll(Example.of(LmsUser.builder()
                    .state(Code.USER_STATE.DORMANCY.enumCode)
                    .build())).stream().count();
            model.addAttribute("countDormancyUser", countDormancyUser);

            modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                    .map(searchMap -> userStateService.getList((UserStateRequestVO) params(UserStateRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList());
            return new StringBuilder(STATISTICS).append("usedStatistics").toString();
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
