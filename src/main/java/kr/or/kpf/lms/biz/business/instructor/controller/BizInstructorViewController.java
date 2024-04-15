package kr.or.kpf.lms.biz.business.instructor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.business.instructor.service.BizInstructorService;
import kr.or.kpf.lms.biz.business.instructor.vo.request.BizInstructorViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.vo.response.BizInstructorTestExcelVO;
import kr.or.kpf.lms.biz.business.pbanc.master.service.BizPbancService;
import kr.or.kpf.lms.biz.business.pbanc.master.vo.request.BizPbancViewRequestVO;
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
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/business/instructor/list")
public class BizInstructorViewController extends CSViewControllerSupport {
    private static final String BUSINESS = "views/business/instructor/";
    private final BizInstructorService bizInstructorService;

    @GetMapping(path={"", "/page"})
    public String getList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                    .map(searchMap -> bizInstructorService.getBizInstructorList((BizInstructorViewRequestVO) params(BizInstructorViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("BIZ_INSTR_STTS"));
            return new StringBuilder(BUSINESS).append("instructor").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    @GetMapping("/")
    public String getWrite() {
        return new StringBuilder(BUSINESS).append("instructorForm").toString();
    }

    @GetMapping("/{bizInstrNo}")
    public String getForm(HttpServletRequest request, Pageable pageable, Model model,
                          @Parameter(description = "강사 모집 공고 일련 번호") @PathVariable(value = "bizInstrNo", required = true) String bizInstrNo) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("bizInstrNo", bizInstrNo);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> bizInstructorService.getBizInstructorList((BizInstructorViewRequestVO) params(BizInstructorViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
            return new StringBuilder(BUSINESS).append("instructorForm").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /**
     * 거리증빙 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Business Instructor", description = "강사 모집 API")
    @Operation(operationId = "Instructor", summary = "강사 모집 엑셀", description = "강사 모집 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/excel")
    public String getListExcel(HttpServletRequest request, HttpServletResponse response, Pageable pageable, Model model) throws IOException {
        List<BizInstructorTestExcelVO> bizInstructorExcelVOList = bizInstructorService.getExcel((BizInstructorViewRequestVO) params(BizInstructorViewRequestVO.class, CSSearchMap.of(request), null));
        model.addAttribute("content", bizInstructorExcelVOList);
        return new StringBuilder(BUSINESS).append("instructorExcelTable").toString();
    }
}
