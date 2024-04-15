package kr.or.kpf.lms.biz.user.organization.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.user.organization.service.OrganizationMediaService;
import kr.or.kpf.lms.biz.user.organization.service.OrganizationService;
import kr.or.kpf.lms.biz.user.organization.vo.request.OrganizationMediaViewRequestVO;
import kr.or.kpf.lms.biz.user.organization.vo.request.OrganizationViewRequestVO;
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
 * 사용자 관리 > 기관 관리 View 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/user/organization")
public class OrganizationViewController  extends CSViewControllerSupport {
    private static final String ORGANIZATION = "views/user/organization/";
    private final OrganizationService organizationService;
    private final OrganizationMediaService organizationMediaService;

    /**
     * 사용자 관리 > 기관 목록 조회
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
                    .map(searchMap -> organizationService.getList((OrganizationViewRequestVO) params(OrganizationViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("ORG_TYPE"));
            return new StringBuilder(ORGANIZATION).append("organization").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }


    /**
     * 사용자 관리 > 기관 등록
     */
    @GetMapping(path = {"/"})
    public String getWrite() {
        return new StringBuilder(ORGANIZATION).append("organizationForm").toString();
    }


    /**
     * 사용자 관리 > 기관 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path = {"/{organizationCode}"})
    public String getForm(HttpServletRequest request, Pageable pageable, Model model,
                          @Parameter(description = "기관 코드") @PathVariable(value = "organizationCode", required = true) String organizationCode) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("organizationCode", organizationCode);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> organizationService.getList((OrganizationViewRequestVO) params(OrganizationViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("ORG_TYPE"));
            return new StringBuilder(ORGANIZATION).append("organizationForm").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    @GetMapping(path = {"/media", "/media/page"})
    public String getMedia(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                    .map(searchMap -> organizationMediaService.getList((OrganizationMediaViewRequestVO) params(OrganizationMediaViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("MDIA_CLS_CD1"));
            return new StringBuilder(ORGANIZATION).append("organizationMedia").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }

    }

}
