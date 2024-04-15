package kr.or.kpf.lms.biz.business.instructor.asgnm.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.business.instructor.asgnm.service.BizInstructorAsgnmService;
import kr.or.kpf.lms.biz.business.instructor.asgnm.vo.request.BizInstructorAsgnmViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.vo.request.BizInstructorViewRequestVO;
import kr.or.kpf.lms.biz.business.organization.aply.service.BizOrganizationAplyService;
import kr.or.kpf.lms.biz.business.organization.aply.vo.request.BizOrganizationAplyCustomViewRequestVO;
import kr.or.kpf.lms.biz.business.organization.aply.vo.request.BizOrganizationAplyViewRequestVO;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/business/instructor/assignment")
public class BizInstructorAsgnmViewController extends CSViewControllerSupport {
    private static final String BUSINESS = "views/business/instructor/";
    private final BizOrganizationAplyService bizOrganizationAplyService;

    @GetMapping(path={"", "/page"})
    public String getList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                    .map(searchMap -> bizOrganizationAplyService.getBizOrganizationAplyCustomList((BizOrganizationAplyCustomViewRequestVO) params(BizOrganizationAplyCustomViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
            return new StringBuilder(BUSINESS).append("instructorAssignment").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    @GetMapping("/{bizOrgAplyNo}")
    public String getForm(HttpServletRequest request, Pageable pageable, Model model,
                          @Parameter(description = "사업 공고 신청 일련 번호") @PathVariable(value = "bizOrgAplyNo", required = true) String bizOrgAplyNo) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("bizOrgAplyNo", bizOrgAplyNo);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> bizOrganizationAplyService.getBizOrganizationAplyCustomList((BizOrganizationAplyCustomViewRequestVO) params(BizOrganizationAplyCustomViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
            return new StringBuilder(BUSINESS).append("instructorApproveForm").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }
}
