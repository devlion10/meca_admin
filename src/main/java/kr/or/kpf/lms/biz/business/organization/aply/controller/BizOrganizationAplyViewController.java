package kr.or.kpf.lms.biz.business.organization.aply.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.business.organization.aply.edithist.service.BizEditHistService;
import kr.or.kpf.lms.biz.business.organization.aply.edithist.vo.request.BizEditHistViewRequestVO;
import kr.or.kpf.lms.biz.business.organization.aply.service.BizOrganizationAplyService;
import kr.or.kpf.lms.biz.business.organization.aply.vo.request.BizOrganizationAplyViewRequestVO;
import kr.or.kpf.lms.biz.business.organization.aply.vo.response.BizOrganizationAplyDistExcelVO;
import kr.or.kpf.lms.common.exceldownload.excel.OneSheetExcelFile;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 사업 공고 신청 조회 Controller
 */
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/business/organization/apply")
public class BizOrganizationAplyViewController extends CSViewControllerSupport {
    private static final String BUSINESS = "views/business/organization/";
    private final BizOrganizationAplyService bizOrganizationAplyService;

    @GetMapping(path={"", "/page"})
    public String getList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                    .map(searchMap -> bizOrganizationAplyService.getBizOrganizationAplyList((BizOrganizationAplyViewRequestVO) params(BizOrganizationAplyViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("CON_TEXT_TYPE", "BIZ_PBANC_TYPE", "BIZ_PBANC_CTGR", "BIZ_PBANC_INSTR_SLCTN_METH" ));
            return new StringBuilder(BUSINESS).append("pbancApply").toString();
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
        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("bizOrgAplyNo", bizOrgAplyNo);
        modelSetting(model, Optional.ofNullable(requestParam)
                .map(searchMap -> bizOrganizationAplyService.getBizOrganizationAplyList((BizOrganizationAplyViewRequestVO) params(BizOrganizationAplyViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
        return new StringBuilder(BUSINESS).append("pbancApplyForm").toString();
    }
    /**
     * 사업 공고 신청 엑셀 다운로드 API
     *
     * @param request
     * @return
     */
    @Tag(name = "Business Organization Apply", description = "사업 공고 신청 API")
    @Operation(operationId = "Business Organization Apply", summary = "사업 공고 신청 엑셀", description = "사업 공고 신청 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/pdf/{totalElements}")
    public String getListPdf(HttpServletRequest request, Pageable pageable, Model model,
                             @Parameter(description = "최대 사이즈") @PathVariable(value = "totalElements", required = true) String totalElements) {

        Pageable totalPage = Pageable.ofSize(30);

        if(totalElements!=null && Integer.parseInt(totalElements)>0){
            totalPage = Pageable.ofSize(Integer.parseInt(totalElements));
        }

        Pageable finalTotalPage = totalPage;
        modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                .map(searchMap -> bizOrganizationAplyService.getBizOrganizationAplyList((BizOrganizationAplyViewRequestVO) params(BizOrganizationAplyViewRequestVO.class, searchMap, finalTotalPage)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
        return new StringBuilder(BUSINESS).append("pbancApplyPdf").toString();
    }
}
