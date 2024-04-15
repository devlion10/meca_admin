package kr.or.kpf.lms.biz.business.instructor.aply.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.business.instructor.aply.service.BizInstructorAplyService;
import kr.or.kpf.lms.biz.business.instructor.aply.vo.request.BizInstructorAplyViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.service.BizInstructorService;
import kr.or.kpf.lms.biz.business.instructor.vo.request.BizInstructorViewRequestVO;
import kr.or.kpf.lms.biz.business.pbanc.master.vo.request.BizPbancViewRequestVO;
import kr.or.kpf.lms.biz.user.instructor.service.InstructorService;
import kr.or.kpf.lms.biz.user.instructor.vo.request.InstructorViewRequestVO;
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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/business/instructor/assignment/apply")
public class BizInstructorAplyViewController extends CSViewControllerSupport {
    private static final String BUSINESS = "views/business/instructor/";
    private final BizInstructorAplyService bizInstructorAplyService;

    @GetMapping("/{bizOrgAplyNo}")
    public String getForm(HttpServletRequest request, Pageable pageable, Model model,
                          @Parameter(description = "사업 공고 신청 일련 번호") @PathVariable(value = "bizOrgAplyNo", required = true) String bizOrgAplyNo) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("bizOrgAplyNo", bizOrgAplyNo);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> bizInstructorAplyService.getBizInstructorAplyList((BizInstructorAplyViewRequestVO) params(BizInstructorAplyViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
            return new StringBuilder(BUSINESS).append("instructorApplyForm").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }
}
