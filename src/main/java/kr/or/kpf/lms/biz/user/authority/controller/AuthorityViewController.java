package kr.or.kpf.lms.biz.user.authority.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.system.menu.service.MenuService;
import kr.or.kpf.lms.biz.user.authority.menu.service.AuthorityMenuService;
import kr.or.kpf.lms.biz.user.authority.menu.vo.request.AuthorityMenuViewRequestVO;
import kr.or.kpf.lms.biz.user.authority.service.AuthorityService;
import kr.or.kpf.lms.biz.user.authority.vo.request.AuthorityViewRequestVO;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import kr.or.kpf.lms.repository.entity.system.AdminMenu;
import kr.or.kpf.lms.repository.system.AdminMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 사용자 관리 > 권한 관리 View 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/user/authority")
public class AuthorityViewController extends CSViewControllerSupport {

    private static final String AUTHORITY = "views/user/authority/";

    private final AuthorityService authorityService;
    private final AdminMenuRepository adminMenuRepository;

    /**
     * 사용자 관리 > 권한 관리 > 관리 시스템 권한 목록 조회
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
                    .map(searchMap -> authorityService.getList((AuthorityViewRequestVO) params(AuthorityViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
            return new StringBuilder(AUTHORITY).append("authority").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /**
     * 사용자 관리 > 권한 관리 > 관리 시스템 권한 등록
     */
    @GetMapping(path = {"/"})
    public String getWrite() {
        return new StringBuilder(AUTHORITY).append("authorityForm").toString();
    }

    /**
     * 사용자 관리 > 권한 관리 > 관리 시스템 권한 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path = {"/{roleGroupCode}"})
    public String getForm(HttpServletRequest request, Pageable pageable, Model model,
                          @Parameter(description = "권한 코드") @PathVariable(value = "roleGroupCode", required = true) String roleGroupCode) {
        try {
            model.addAttribute("menus", adminMenuRepository.findAll().stream()
                    .filter(adminMenu -> adminMenu.getIsUsable() == true && adminMenu.getSort() != 0)
                    .sorted(Comparator.comparing(AdminMenu::getDepth).thenComparing(Comparator.comparing(AdminMenu::getSort)))
                    .collect(Collectors.toList()));

            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("roleGroupCode", roleGroupCode);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> authorityService.getList((AuthorityViewRequestVO) params(AuthorityViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
            return new StringBuilder(AUTHORITY).append("authorityForm").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }
}
