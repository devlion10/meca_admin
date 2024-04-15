package kr.or.kpf.lms.biz.business.instructor.identify.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.business.instructor.clcln.ddln.service.BizInstructorClclnDdlnService;
import kr.or.kpf.lms.biz.business.instructor.clcln.ddln.vo.request.BizInstructorClclnDdlnViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.dist.crtramt.service.BizInstructorDistCrtrAmtService;
import kr.or.kpf.lms.biz.business.instructor.dist.crtramt.vo.request.BizInstructorDistCrtrAmtViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.identify.service.BizInstructorIdentifyService;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.request.BizInstructorIdentifyViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.request.BizInstructorIdentifyManageViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.request.MyInstructorStateViewRequestVO;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import kr.or.kpf.lms.repository.entity.BizInstructorClclnDdln;
import kr.or.kpf.lms.repository.entity.BizInstructorIdentify;
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
@RequestMapping(value = "/business/instructor/identify")
public class BizInstructorIdentifyViewController extends CSViewControllerSupport {
    private static final String BUSINESS = "views/business/instructor/";
    private final BizInstructorIdentifyService bizInstructorIdentifyService;
    private final BizInstructorClclnDdlnService bizInstructorClclnDdlnService;
    private final BizInstructorDistCrtrAmtService bizInstructorDistCrtrAmtService;

    @GetMapping(path={"", "/page"})
    public String getList(HttpServletRequest request, Pageable pageable, Model model) {
        /*try {
            Page<BizInstructorClclnDdln> bizInstructorClclnDdlns = bizInstructorClclnDdlnService.getBizInstructorClclnDdlnList((BizInstructorClclnDdlnViewRequestVO) params(BizInstructorClclnDdlnViewRequestVO.class, CSSearchMap.of(request), pageable));
            model.addAttribute("bizInstructorClclnDdlns", bizInstructorClclnDdlns);

            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("sttsType", 1);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> bizInstructorIdentifyService.getBizInstructorIdentifyList((BizInstructorIdentifyViewRequestVO) params(BizInstructorIdentifyViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
            return new StringBuilder(BUSINESS).append("identify").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }*/
        Page<BizInstructorClclnDdln> bizInstructorClclnDdlns = bizInstructorClclnDdlnService.getBizInstructorClclnDdlnList((BizInstructorClclnDdlnViewRequestVO) params(BizInstructorClclnDdlnViewRequestVO.class, CSSearchMap.of(request), pageable));
        model.addAttribute("bizInstructorClclnDdlns", bizInstructorClclnDdlns);

        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("sttsType", 1);
        modelSetting(model, Optional.ofNullable(requestParam)
                .map(searchMap -> bizInstructorIdentifyService.getBizInstructorIdentifyList((BizInstructorIdentifyViewRequestVO) params(BizInstructorIdentifyViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
        return new StringBuilder(BUSINESS).append("identify").toString();
    }

    @GetMapping(path={"/{bizInstrIdntyNo}"})
    public String getForm(HttpServletRequest request, Pageable pageable, Model model,
                          @Parameter(description = "강의확인서 일련 번호") @PathVariable(value = "bizInstrIdntyNo", required = true) String bizInstrIdntyNo) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("bizInstrIdntyNo", bizInstrIdntyNo);
            Page<?> getIdentify= bizInstructorIdentifyService.getBizInstructorIdentifyList((BizInstructorIdentifyViewRequestVO) params(BizInstructorIdentifyViewRequestVO.class, requestParam, pageable));
            if (getIdentify != null && !getIdentify.isEmpty() && getIdentify.getTotalElements() > 0) {
                BizInstructorIdentify identify = (BizInstructorIdentify) getIdentify.getContent().get(0);
                /** 거리 비교할 dtl 조회 */
                CSSearchMap requestAnother = CSSearchMap.of(request);
                requestAnother.put("bizInstrAplyInstrId", identify.getRegistUserId());
                requestAnother.put("notBizInstrAplyNo", identify.getBizInstructorDist().getBizInstrAplyNo());
                requestAnother.put("bizInstrIdntyYm", identify.getBizInstrIdntyYm());
                Pageable anotherPage = Pageable.ofSize(50);
                Page<?> anotherIdentify = bizInstructorIdentifyService.getInstructorStateList((MyInstructorStateViewRequestVO) params(MyInstructorStateViewRequestVO.class, requestAnother, anotherPage));
                if (anotherIdentify != null && !anotherIdentify.isEmpty() && anotherIdentify.getTotalElements() > 0) {
                    model.addAttribute("anotherIdentify", anotherIdentify);
                }
            }

            int year = LocalDate.now().getYear();
            CSSearchMap requestValue = CSSearchMap.of(request);
            requestValue.put("bizInstrDistCrtrAmtYr", year);
            Page<?> clclDdlnAmt = bizInstructorDistCrtrAmtService.getBizInstructorDistCrtrAmtList((BizInstructorDistCrtrAmtViewRequestVO) params(BizInstructorDistCrtrAmtViewRequestVO.class, requestValue, pageable));
            if (clclDdlnAmt != null && !clclDdlnAmt.isEmpty() && clclDdlnAmt.getTotalElements() > 0) {
                model.addAttribute("clclDdlnAmt", clclDdlnAmt);
            }

            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> bizInstructorIdentifyService.getBizInstructorIdentifyList((BizInstructorIdentifyViewRequestVO) params(BizInstructorIdentifyViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
            return new StringBuilder(BUSINESS).append("identifyForm").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    @GetMapping(path={"/result", "/result/page"})
    public String getResultList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            Page<BizInstructorClclnDdln> bizInstructorClclnDdlns = bizInstructorClclnDdlnService.getBizInstructorClclnDdlnList((BizInstructorClclnDdlnViewRequestVO) params(BizInstructorClclnDdlnViewRequestVO.class, CSSearchMap.of(request), pageable));
            model.addAttribute("bizInstructorClclnDdlns", bizInstructorClclnDdlns);

            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("sttsType", 2);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> bizInstructorIdentifyService.getBizInstructorIdentifyList((BizInstructorIdentifyViewRequestVO) params(BizInstructorIdentifyViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
            return new StringBuilder(BUSINESS).append("identify").toString();
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
            CSSearchMap requestParam = CSSearchMap.of(request);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> bizInstructorIdentifyService.getBizInstructorIdentifyList((BizInstructorIdentifyViewRequestVO) params(BizInstructorIdentifyViewRequestVO.class, searchMap, finalTotalPage)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
            return new StringBuilder(BUSINESS).append("identifyPdf").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    @GetMapping(path={"/result/{bizInstrIdntyNo}"})
    public String getResultForm(HttpServletRequest request, Pageable pageable, Model model,
                          @Parameter(description = "강의확인서 일련 번호") @PathVariable(value = "bizInstrIdntyNo", required = true) String bizInstrIdntyNo) {
        try {
            int year = LocalDate.now().getYear();
            CSSearchMap requestValue = CSSearchMap.of(request);
            requestValue.put("bizInstrDistCrtrAmtYr", year);
            Page<?> clclDdlnAmt = bizInstructorDistCrtrAmtService.getBizInstructorDistCrtrAmtList((BizInstructorDistCrtrAmtViewRequestVO) params(BizInstructorDistCrtrAmtViewRequestVO.class, requestValue, pageable));
            model.addAttribute("clclDdlnAmt", clclDdlnAmt);

            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("bizInstrIdntyNo", bizInstrIdntyNo);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> bizInstructorIdentifyService.getBizInstructorIdentifyList((BizInstructorIdentifyViewRequestVO) params(BizInstructorIdentifyViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
            return new StringBuilder(BUSINESS).append("identifyForm").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    @GetMapping(path={"/manage", "/manage/page"})
    public String getManageList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> bizInstructorIdentifyService.getBizInstructorIdentifyManageList((BizInstructorIdentifyManageViewRequestVO) params(BizInstructorIdentifyManageViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
            return new StringBuilder(BUSINESS).append("identifyManage").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    @GetMapping(path={"/manage/{bizInstrAplyNo}"})
    public String getManageForm(HttpServletRequest request, Pageable pageable, Model model,
                                @Parameter(description = "강사 신청 일련 번호") @PathVariable(value = "bizInstrAplyNo", required = true) String bizInstrAplyNo) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("bizInstrAplyNo", bizInstrAplyNo);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> bizInstructorIdentifyService.getBizInstructorIdentifyManageList((BizInstructorIdentifyManageViewRequestVO) params(BizInstructorIdentifyManageViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
            return new StringBuilder(BUSINESS).append("identifyManageForm").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }
}
