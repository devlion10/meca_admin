package kr.or.kpf.lms.biz.business.apply.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.business.apply.vo.request.BizAplyViewRequestVO;
import kr.or.kpf.lms.biz.business.apply.service.BizAplyService;
import kr.or.kpf.lms.biz.business.apply.vo.response.BizAplyFreeExcelVO;
import kr.or.kpf.lms.biz.education.application.vo.request.EducationApplicationApiRequestVO;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import kr.or.kpf.lms.repository.entity.BizAply;
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
import java.util.List;
import java.util.Optional;

/**
 * 사업 공고 신청 - 언론인 조회 Controller
 */
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/business/apply")
public class BizAplyViewController extends CSViewControllerSupport {
    private static final String BUSINESS = "views/business/organization/";
    private final BizAplyService bizAplyService;

    @GetMapping(path={"/journalist"})
    public String getJournalistList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("bizAplyType", "journalist");

            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> bizAplyService.getBizAplyList((BizAplyViewRequestVO) params(BizAplyViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList());
            return new StringBuilder(BUSINESS).append("pbancApplyJournalist").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    @GetMapping(path={"/general"})
    public String getGeneralList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("bizAplyType", "general");

            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> bizAplyService.getBizAplyList((BizAplyViewRequestVO) params(BizAplyViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList());
            return new StringBuilder(BUSINESS).append("pbancApplyGeneral").toString();
        } catch (KPFException e1) {
            logger.error("{}- {}", e1.getClass().getCanonicalName(), e1.getMessage(), e1);
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            logger.error("{}- {}", e2.getClass().getCanonicalName(), e2.getMessage(), e2);
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    @GetMapping(path={"/free"})
    public String getFreeList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("bizAplyType", "free");

            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> bizAplyService.getBizAplyList((BizAplyViewRequestVO) params(BizAplyViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList());
            return new StringBuilder(BUSINESS).append("pbancApplyFree").toString();
        } catch (KPFException e1) {
            logger.error("{}- {}", e1.getClass().getCanonicalName(), e1.getMessage(), e1);
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            logger.error("{}- {}", e2.getClass().getCanonicalName(), e2.getMessage(), e2);
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    @GetMapping(path={"/free/{sequenceNo}"})
    public String getFreeForm(HttpServletRequest request, Pageable pageable, Model model,
                              @Parameter(description = "사업 공고 신청 - 언론인/기본형 시리얼 번호") @PathVariable(value = "sequenceNo", required = true) BigInteger sequenceNo) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("bizAplyType", "free");
            requestParam.put("sequenceNo", sequenceNo);

            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> bizAplyService.getBizAplyList((BizAplyViewRequestVO) params(BizAplyViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList());
            return new StringBuilder(BUSINESS).append("pbancApplyFreeForm").toString();
        } catch (KPFException e1) {
            logger.error("{}- {}", e1.getClass().getCanonicalName(), e1.getMessage(), e1);
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            logger.error("{}- {}", e2.getClass().getCanonicalName(), e2.getMessage(), e2);
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    @GetMapping(path={"/free/excel/{bizPbancNo}"})
    public String getFree5Excel(Model model,
                                @Parameter(description = "사업 공고 번호") @PathVariable(value = "bizPbancNo", required = true) String bizPbancNo) {
        BizAplyViewRequestVO bizAplyViewRequestVO = BizAplyViewRequestVO.builder().build();
        bizAplyViewRequestVO.setBizPbancNo(bizPbancNo);
        bizAplyViewRequestVO.setBizAplyType("free");
        List<BizAplyFreeExcelVO> bizAplyList = bizAplyService.getFreeExcel(bizAplyViewRequestVO);
        model.addAttribute("content", bizAplyList);
        return new StringBuilder(BUSINESS).append("pbancApplyFreeExcel").toString();
    }
}
