package kr.or.kpf.lms.biz.business.organization.rpt.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.business.organization.rpt.service.BizOrganizationRsltRptService;
import kr.or.kpf.lms.biz.business.organization.rpt.vo.request.BizOrganizationRsltRptViewRequestVO;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * 결과보고서 조회 Controller
 */
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/business/organization/report")
public class BizOrganizationRsltRptViewController extends CSViewControllerSupport {
    private static final String BUSINESS = "views/business/organization/";
    private final BizOrganizationRsltRptService bizOrganizationRsltRptService;

    @GetMapping(path={"", "/page"})
    public String getList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                    .map(searchMap -> bizOrganizationRsltRptService.getBizOrganizationRsltRptList((BizOrganizationRsltRptViewRequestVO) params(BizOrganizationRsltRptViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("CON_TEXT_TYPE", "BIZ_PBANC_TYPE"));
            return new StringBuilder(BUSINESS).append("resultReport").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    @GetMapping("/{bizOrgRsltRptNo}")
    public String getForm(HttpServletRequest request, Pageable pageable, Model model,
                          @Parameter(description = "결과보고서 일련 번호") @PathVariable(value = "bizOrgRsltRptNo", required = true) String bizOrgRsltRptNo) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("bizOrgRsltRptNo", bizOrgRsltRptNo);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> bizOrganizationRsltRptService.getBizOrganizationRsltRptList((BizOrganizationRsltRptViewRequestVO) params(BizOrganizationRsltRptViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
            return new StringBuilder(BUSINESS).append("resultReportForm").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    @GetMapping(value = "/pdf/{totalElements}")
    public String getPdfList(HttpServletRequest request, Pageable pageable, Model model,
                             @Parameter(description = "강의확인서 일련 번호") @PathVariable(value = "totalElements", required = true) String totalElements) {
        try {
            Pageable totalPage = Pageable.ofSize(30);

            if(totalElements!=null && Integer.parseInt(totalElements)>0){
                totalPage = Pageable.ofSize(Integer.parseInt(totalElements));
            }

            Pageable finalTotalPage = totalPage;

            modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                    .map(searchMap -> bizOrganizationRsltRptService.getBizOrganizationRsltRptList((BizOrganizationRsltRptViewRequestVO) params(BizOrganizationRsltRptViewRequestVO.class, searchMap, finalTotalPage)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("CON_TEXT_TYPE", "BIZ_PBANC_TYPE"));
            return new StringBuilder(BUSINESS).append("resultReportPdf").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }
}
