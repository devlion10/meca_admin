package kr.or.kpf.lms.biz.business.pbanc.master.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.business.pbanc.master.service.BizPbancService;
import kr.or.kpf.lms.biz.business.pbanc.master.vo.request.BizPbancViewRequestVO;
import kr.or.kpf.lms.biz.business.pbanc.rslt.service.BizPbancRsltService;
import kr.or.kpf.lms.biz.business.pbanc.rslt.vo.request.BizPbancRsltViewRequestVO;
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

/**
 * 사업 공고 조회 Controller
 */
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/business/pbanc/list")
public class BizPbancViewController extends CSViewControllerSupport {
    private static final String BUSINESS = "views/business/pbanc/";
    private final BizPbancService bizPbancService;

    @GetMapping(path={"", "/page"})
    public String getList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                    .map(searchMap -> bizPbancService.getBizPbancList((BizPbancViewRequestVO) params(BizPbancViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("CON_TEXT_TYPE", "BIZ_PBANC_TYPE", "BIZ_PBANC_CTGR", "BIZ_PBANC_INSTR_SLCTN_METH" ));
            return new StringBuilder(BUSINESS).append("business").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    @GetMapping("/{bizPbancType}")
    public String getWrite(HttpServletRequest request, Pageable pageable, Model model,
                          @Parameter(description = "사업 공고 템플릿 유형") @PathVariable(value = "bizPbancType", required = true) Integer bizPbancType) {
        try {
            if (bizPbancType == 1) {
                return new StringBuilder(BUSINESS).append("businessForm1").toString();
            } else if (bizPbancType == 2) {
                return new StringBuilder(BUSINESS).append("businessForm2").toString();
            } else if (bizPbancType == 3) {
                return new StringBuilder(BUSINESS).append("businessForm3").toString();
            } else if (bizPbancType == 4) {
                return new StringBuilder(BUSINESS).append("businessForm4").toString();
            }else if (bizPbancType == 5) {
                return new StringBuilder(BUSINESS).append("businessForm5").toString();
            } else {
                return new StringBuilder(BUSINESS).append("businessForm0").toString();
            }
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    @GetMapping("/{bizPbancType}/{bizPbancNo}")
    public String getForm(HttpServletRequest request, Pageable pageable, Model model,
                          @Parameter(description = "사업 공고 일련 번호") @PathVariable(value = "bizPbancNo", required = true) String bizPbancNo,
                           @Parameter(description = "사업 공고 템플릿 유형") @PathVariable(value = "bizPbancType", required = true) Integer bizPbancType) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("bizPbancNo", bizPbancNo);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> bizPbancService.getBizPbancList((BizPbancViewRequestVO) params(BizPbancViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));

            if (bizPbancType == 1) {
                return new StringBuilder(BUSINESS).append("businessForm1").toString();
            } else if (bizPbancType == 2) {
                return new StringBuilder(BUSINESS).append("businessForm2").toString();
            } else if (bizPbancType == 3) {
                return new StringBuilder(BUSINESS).append("businessForm3").toString();
            } else if (bizPbancType == 4) {
                return new StringBuilder(BUSINESS).append("businessForm4").toString();
            } else if (bizPbancType == 5) {
                return new StringBuilder(BUSINESS).append("businessForm5").toString();
            } else {
                return new StringBuilder(BUSINESS).append("businessForm0").toString();
            }
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }
}
