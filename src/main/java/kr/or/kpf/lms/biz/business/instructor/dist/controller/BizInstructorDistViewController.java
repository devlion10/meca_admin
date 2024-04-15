package kr.or.kpf.lms.biz.business.instructor.dist.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.business.instructor.dist.crtramt.service.BizInstructorDistCrtrAmtService;
import kr.or.kpf.lms.biz.business.instructor.dist.crtramt.vo.request.BizInstructorDistCrtrAmtViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.dist.service.BizInstructorDistService;
import kr.or.kpf.lms.biz.business.instructor.dist.vo.request.BizInstructorDistViewRequestVO;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/business/instructor/distance")
public class BizInstructorDistViewController extends CSViewControllerSupport {
    private static final String BUSINESS = "views/business/calculation/";
    private final BizInstructorDistService bizInstructorDistService;
    private final BizInstructorDistCrtrAmtService bizInstructorDistCrtrAmtService;

    @GetMapping(path={"", "/page"})
    public String getList(HttpServletRequest request, Pageable pageable, Model model) {
        CSSearchMap requestCS = CSSearchMap.of(request);
        requestCS.put("bizDistStts", 0);/** 확인 전 개수 받아오기 */
        Long getTotal = bizInstructorDistService.getBizInstructorDistList((BizInstructorDistViewRequestVO) params(BizInstructorDistViewRequestVO.class, requestCS, pageable)).getTotalElements();

        CSSearchMap requestResultCS = CSSearchMap.of(request);
        requestResultCS.put("bizDistStts", 1);/** 확인 완료 개수 받아오기 */
        Long resultTotal = bizInstructorDistService.getBizInstructorDistList((BizInstructorDistViewRequestVO) params(BizInstructorDistViewRequestVO.class, requestResultCS, pageable)).getTotalElements();

        try {
            model.addAttribute("getTotal", getTotal);
            model.addAttribute("resultTotal", resultTotal);
            modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                    .map(searchMap -> bizInstructorDistService.getBizInstructorDistList((BizInstructorDistViewRequestVO) params(BizInstructorDistViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
            return new StringBuilder(BUSINESS).append("distance").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    @GetMapping("/{bizInstrDistNo}")
    public String getForm(HttpServletRequest request, Pageable pageable, Model model,
                          @Parameter(description = "거리 증빙 일련 번호") @PathVariable(value = "bizInstrDistNo", required = true) String bizInstrDistNo) {
        try {
            int year = LocalDate.now().getYear();
            CSSearchMap requestValue = CSSearchMap.of(request);
            requestValue.put("bizInstrDistCrtrAmtYr", year);
            Page<?> clclDdlnAmt = bizInstructorDistCrtrAmtService.getBizInstructorDistCrtrAmtList((BizInstructorDistCrtrAmtViewRequestVO) params(BizInstructorDistCrtrAmtViewRequestVO.class, requestValue, pageable));
            model.addAttribute("clclDdlnAmt", clclDdlnAmt);

            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("bizInstrDistNo", bizInstrDistNo);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> bizInstructorDistService.getBizInstructorDistList((BizInstructorDistViewRequestVO) params(BizInstructorDistViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
            return new StringBuilder(BUSINESS).append("distanceForm").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }
}
