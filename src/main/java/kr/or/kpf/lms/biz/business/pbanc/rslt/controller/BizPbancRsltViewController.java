package kr.or.kpf.lms.biz.business.pbanc.rslt.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.business.organization.aply.service.BizOrganizationAplyService;
import kr.or.kpf.lms.biz.business.organization.aply.vo.request.BizOrganizationAplyViewRequestVO;
import kr.or.kpf.lms.biz.business.pbanc.master.service.BizPbancService;
import kr.or.kpf.lms.biz.business.pbanc.master.vo.request.BizPbancViewRequestVO;
import kr.or.kpf.lms.biz.business.pbanc.master.vo.response.BizPbancCustomApiResponseVO;
import kr.or.kpf.lms.biz.business.pbanc.rslt.service.BizPbancRsltService;
import kr.or.kpf.lms.biz.business.pbanc.rslt.vo.request.BizPbancRsltViewRequestVO;
import kr.or.kpf.lms.biz.business.pbanc.rslt.vo.response.BizPbancRsltCustomApiResponseVO;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import kr.or.kpf.lms.repository.entity.BizOrganizationAply;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 사업 공고 조회 Controller
 */
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/business/pbanc/list/result")
public class BizPbancRsltViewController extends CSViewControllerSupport {
    private static final String BUSINESS = "views/business/pbanc/";

    private final BizPbancRsltService bizPbancRsltService;
    private final BizPbancService bizPbancService;
    private final BizOrganizationAplyService bizOrganizationAplyService;

    @GetMapping("/{bizPbancNo}")
    public String getForm(HttpServletRequest request, Pageable pageable, Model model,
                          @Parameter(description = "사업 공고 일련 번호") @PathVariable(value = "bizPbancNo", required = true) String bizPbancNo) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("bizPbancNo", bizPbancNo);
            Page<BizPbancRsltCustomApiResponseVO> bizPbancRsltCustomApiResponseVOs = bizPbancRsltService.getBizPbancRsltList((BizPbancRsltViewRequestVO) params(BizPbancRsltViewRequestVO.class, requestParam, pageable));

            if (!bizPbancRsltCustomApiResponseVOs.isEmpty()) {
                modelSetting(model, Optional.ofNullable(requestParam)
                        .map(searchMap -> bizPbancRsltService.getBizPbancRsltList((BizPbancRsltViewRequestVO) params(BizPbancRsltViewRequestVO.class, searchMap, pageable)))
                        .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
            } else {
                Page<BizPbancCustomApiResponseVO> bizPbancCustomApiResponseVOs = bizPbancService.getBizPbancList((BizPbancViewRequestVO) params(BizPbancViewRequestVO.class, requestParam, pageable));
                List<BizPbancCustomApiResponseVO> bizPbancCustomApiResponseVOList = bizPbancCustomApiResponseVOs.getContent();

                BizPbancRsltCustomApiResponseVO bizPbancRsltCustomApiResponseVO = new BizPbancRsltCustomApiResponseVO();
                bizPbancRsltCustomApiResponseVO.setBizPbancNo(bizPbancNo);
                bizPbancRsltCustomApiResponseVO.setBizPbancNm(bizPbancCustomApiResponseVOList.get(0).getBizPbancNm());
                bizPbancRsltCustomApiResponseVO.setBizPbancCtgrNm(bizPbancCustomApiResponseVOList.get(0).getBizPbancCtgrNm());
                bizPbancRsltCustomApiResponseVO.setBizPbancType(bizPbancCustomApiResponseVOList.get(0).getBizPbancType());

                List<BizPbancRsltCustomApiResponseVO> dataList = new ArrayList<>();
                dataList.add(bizPbancRsltCustomApiResponseVO);
                model.addAttribute("content", dataList);
            }
            return new StringBuilder(BUSINESS).append("businessResultForm").toString();

        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }
}