package kr.or.kpf.lms.biz.business.instructor.clcln.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.business.instructor.clcln.ddln.service.BizInstructorClclnDdlnService;
import kr.or.kpf.lms.biz.business.instructor.clcln.ddln.vo.request.BizInstructorClclnDdlnViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.dist.crtramt.service.BizInstructorDistCrtrAmtService;
import kr.or.kpf.lms.biz.business.instructor.dist.crtramt.vo.request.BizInstructorDistCrtrAmtViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.identify.service.BizInstructorIdentifyService;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.request.BizInstructorIdentifyCalculateViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.request.BizInstructorIdentifyViewRequestVO;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/business/instructor/calculate")
public class BizInstructorClclnViewController extends CSViewControllerSupport {
    private static final String BUSINESS = "views/business/calculation/";
    private final BizInstructorClclnDdlnService bizInstructorClclnDdlnService;
    private final BizInstructorIdentifyService bizInstructorIdentifyService;
    private final BizInstructorDistCrtrAmtService bizInstructorDistCrtrAmtService;

    /* 정산 마감일 */
    @GetMapping({"", "/page"})
    public String getDeadlineList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                    .map(searchMap -> bizInstructorClclnDdlnService.getBizInstructorClclnDdlnList((BizInstructorClclnDdlnViewRequestVO) params(BizInstructorClclnDdlnViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
            return new StringBuilder(BUSINESS).append("calculateDeadline").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    @GetMapping({"/"})
    public String getDeadlineWrite() {
        return new StringBuilder(BUSINESS).append("calculateDeadlineForm").toString();
    }

    @GetMapping({"/{bizInstrClclnDdlnNo}"})
    public String getDeadlineForm(HttpServletRequest request, Pageable pageable, Model model,
                                  @Parameter(description = "정산마감일 일련 번호") @PathVariable(value = "bizInstrClclnDdlnNo", required = true) String bizInstrClclnDdlnNo) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("bizInstrClclnDdlnNo", bizInstrClclnDdlnNo);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> bizInstructorClclnDdlnService.getBizInstructorClclnDdlnList((BizInstructorClclnDdlnViewRequestVO) params(BizInstructorClclnDdlnViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
            return new StringBuilder(BUSINESS).append("calculateDeadlineForm").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /* 정산 금액 조회 */
    @GetMapping({"/list", "/list/page"})
    public String getList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("bizInstrIdntyStts", 4);/** 지출 완료 상태 */
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> bizInstructorIdentifyService.getBizInstructorIdentifyCalculateList((BizInstructorIdentifyCalculateViewRequestVO) params(BizInstructorIdentifyCalculateViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
            return new StringBuilder(BUSINESS).append("calculate").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }
    /* 정산 금액 조회 */
    @GetMapping({"/list/excel"})
    public String getListExcel(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            CSSearchMap requestValue = CSSearchMap.of(request);
            Page<?> clclDdlnAmt = bizInstructorDistCrtrAmtService.getBizInstructorDistCrtrAmtList((BizInstructorDistCrtrAmtViewRequestVO) params(BizInstructorDistCrtrAmtViewRequestVO.class, requestValue, pageable));
            if (clclDdlnAmt != null && !clclDdlnAmt.isEmpty() && clclDdlnAmt.getTotalElements() > 0) {
                model.addAttribute("clclDdlnAmt", clclDdlnAmt.getContent().get(0));
            }
            CSSearchMap requestParam = CSSearchMap.of(request);
            Pageable excelPage = pageable;
            if(requestParam.get("groupType").equals("org")){
                requestParam.put("groupType", "orgExcel");
            }
            Page<?> data = bizInstructorIdentifyService.getBizInstructorIdentifyCalculateList((BizInstructorIdentifyCalculateViewRequestVO) params(BizInstructorIdentifyCalculateViewRequestVO.class, requestParam, Pageable.ofSize(1)));
            if(data !=null && !data.isEmpty()){
                excelPage = Pageable.ofSize((int) data.getTotalElements());
            }
            requestParam.put("bizInstrIdntyStts", 4);/** 지출 완료 상태 */
            Pageable finalExcelPage = excelPage;
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> bizInstructorIdentifyService.getBizInstructorIdentifyCalculateList((BizInstructorIdentifyCalculateViewRequestVO) params(BizInstructorIdentifyCalculateViewRequestVO.class, searchMap, finalExcelPage)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
            return new StringBuilder(BUSINESS).append("calculateExcel").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    @GetMapping(value = "/list/pdf/{totalElements}")
    public String getListPdf(HttpServletRequest request, Pageable pageable, Model model,
                              @Parameter(description = "강의확인서 총 개수") @PathVariable(value = "totalElements", required = true) String totalElements) {
        try {
            Pageable totalPage = Pageable.ofSize(30);

            if(totalElements!=null && Integer.parseInt(totalElements)>0){
                totalPage = Pageable.ofSize(Integer.parseInt(totalElements));
            }

            Pageable finalTotalPage = totalPage;
            CSSearchMap requestParam = CSSearchMap.of(request);

            requestParam.put("bizInstrIdntyStts", 4);/** 지출 완료 상태 */
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> bizInstructorIdentifyService.getBizInstructorIdentifyCalculateList((BizInstructorIdentifyCalculateViewRequestVO) params(BizInstructorIdentifyCalculateViewRequestVO.class, searchMap, finalTotalPage)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
            return new StringBuilder(BUSINESS).append("calculatePdf").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /* 정산 대상 조회 */
    @GetMapping({"/target", "/target/page"})
    public String getTrgtList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("sttsType", 0);/** 지출 대기 상태만 출력하기 위한 구분값 */
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> bizInstructorIdentifyService.getBizInstructorIdentifyList((BizInstructorIdentifyViewRequestVO) params(BizInstructorIdentifyViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
            return new StringBuilder(BUSINESS).append("calculateTarget").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    @GetMapping(value = "/target/pdf/{totalElements}")
    public String getPdfTrgtList(HttpServletRequest request, Pageable pageable, Model model,
                              @Parameter(description = "강의확인서 일련 번호") @PathVariable(value = "totalElements", required = true) String totalElements) {
        try {
            Pageable totalPage = Pageable.ofSize(30);

            if(totalElements!=null && Integer.parseInt(totalElements)>0){
                totalPage = Pageable.ofSize(Integer.parseInt(totalElements));
            }

            Pageable finalTotalPage = totalPage;
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("sttsType", 0);/** 지출 대기 상태만 출력하기 위한 구분값 */
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> bizInstructorIdentifyService.getBizInstructorIdentifyList((BizInstructorIdentifyViewRequestVO) params(BizInstructorIdentifyViewRequestVO.class, searchMap, finalTotalPage)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
            return new StringBuilder("views/business/instructor/").append("identifyPdf").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }
}
