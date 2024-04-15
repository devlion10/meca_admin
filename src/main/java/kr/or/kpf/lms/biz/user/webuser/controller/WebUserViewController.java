package kr.or.kpf.lms.biz.user.webuser.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.user.webuser.service.WebUserService;
import kr.or.kpf.lms.biz.user.webuser.vo.request.WebUserViewRequestVO;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * 사용자 관리 > 회원 관리 View 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/user/web-user")
public class WebUserViewController extends CSViewControllerSupport {
    private static final String WEB_USER = "views/user/web/";
    private final WebUserService webUserService;

    /**
     * 사용자 관리 > 웹 회원 목록 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path = {"", "/page"})
    public String getUserList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                    .map(searchMap -> webUserService.getList((WebUserViewRequestVO) params(WebUserViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("WEB_USER_ROLE", "BIZ_AUTH", "USER_STATE", "EASY_LGN_CD"));
            return new StringBuilder(WEB_USER).append("user").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /**
     * 사용자 관리 > 웹 회원 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path = {"/{userSerialNo}"})
    public String getUserForm(HttpServletRequest request, Pageable pageable, Model model,
                              @Parameter(description = "사용자 일련 번호") @PathVariable(value = "userSerialNo", required = true) String userSerialNo) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("userSerialNo", userSerialNo);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> webUserService.getList((WebUserViewRequestVO) params(WebUserViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
            return new StringBuilder(WEB_USER).append("userForm").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }
}
