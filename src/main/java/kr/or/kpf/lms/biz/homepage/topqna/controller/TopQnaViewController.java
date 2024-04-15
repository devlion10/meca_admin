package kr.or.kpf.lms.biz.homepage.topqna.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.homepage.topqna.service.TopQnaService;
import kr.or.kpf.lms.biz.homepage.topqna.vo.request.TopQnaViewRequestVO;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * 홈페이지 관리 > 자주묻는 질문 View 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/homepage/top-qna")
public class TopQnaViewController extends CSViewControllerSupport {

    private static final String TOP_QNA = "views/homepage/";

    private final TopQnaService topQnaService;

    /**
     * 홈페이지 관리 > 자주묻는 질문 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path={"", "/page"})
    public String getList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                    .map(searchMap -> topQnaService.getTopQnaList((TopQnaViewRequestVO) params(TopQnaViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("CON_TEXT_TYPE"));
            return new StringBuilder(TOP_QNA).append("faq").toString();
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

    /**
     * 홈페이지 관리 > 자주묻는 질문 등록
     */
    @GetMapping(path={"/"})
    public String getList() {
        return new StringBuilder(TOP_QNA).append("faqForm").toString();
    }

    /**
     * 홈페이지 관리 > 자주묻는 질문 상세 및 수정
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path={"/{sequenceNo}"})
    public String getForm(HttpServletRequest request, Pageable pageable, Model model,
                          @Parameter(description = "페이지 일련 번호") @PathVariable(value = "sequenceNo", required = true) Long sequenceNo) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("sequenceNo", sequenceNo);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> topQnaService.getTopQnaList((TopQnaViewRequestVO) params(TopQnaViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("CON_TEXT_TYPE"));
            return new StringBuilder(TOP_QNA).append("faqForm").toString();
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
}
