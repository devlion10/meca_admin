package kr.or.kpf.lms.biz.business.instructor.dist.crtramt.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.business.instructor.dist.crtramt.service.BizInstructorDistCrtrAmtService;
import kr.or.kpf.lms.biz.business.instructor.dist.crtramt.vo.request.BizInstructorDistCrtrAmtViewRequestVO;
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
@RequestMapping(value = "/business/instructor/calculate/distance/amount")
public class BizInstructorDistCrtrAmtViewController extends CSViewControllerSupport {
    private static final String BUSINESS = "views/business/calculation/";
    private final BizInstructorDistCrtrAmtService bizInstructorDistCrtrAmtService;

    @GetMapping(path={"", "/page"})
    public String getList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                    .map(searchMap -> bizInstructorDistCrtrAmtService.getBizInstructorDistCrtrAmtList((BizInstructorDistCrtrAmtViewRequestVO) params(BizInstructorDistCrtrAmtViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
            return new StringBuilder(BUSINESS).append("distanceAmount").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    @GetMapping(path={"/"})
    public String getWrite() {
        return new StringBuilder(BUSINESS).append("distanceAmountForm").toString();
    }

    @GetMapping("/{bizInstrDistCrtrAmtNo}")
    public String getForm(HttpServletRequest request, Pageable pageable, Model model,
                          @Parameter(description = "이동거리 기준단가 일련 번호") @PathVariable(value = "bizInstrDistCrtrAmtNo", required = true) String bizInstrDistCrtrAmtNo) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("bizInstrDistCrtrAmtNo", bizInstrDistCrtrAmtNo);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> bizInstructorDistCrtrAmtService.getBizInstructorDistCrtrAmtList((BizInstructorDistCrtrAmtViewRequestVO) params(BizInstructorDistCrtrAmtViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
            return new StringBuilder(BUSINESS).append("distanceAmountForm").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }
}
