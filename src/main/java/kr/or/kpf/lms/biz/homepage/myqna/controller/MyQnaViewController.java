package kr.or.kpf.lms.biz.homepage.myqna.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.homepage.myqna.service.MyQnaService;
import kr.or.kpf.lms.biz.homepage.myqna.vo.request.MyQnaViewRequestVO;
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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * 홈페이지 관리 > 1:1 문의 View 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/homepage/my-qna")
public class MyQnaViewController extends CSViewControllerSupport {

    private static final String MYQNA = "views/homepage/";

    private final MyQnaService myQnaService;

    /**
     * 홈페이지 관리 > 1:1 문의 조회
     */
    @GetMapping(path={"", "/page"})
    public String getList(HttpServletRequest request, Pageable pageable, Model model) {
        modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                .map(searchMap -> myQnaService.getList((MyQnaViewRequestVO) params(MyQnaViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("CON_TEXT_TYPE", "QNA_TYPE", "QNA_STATE"));
        return new StringBuilder(MYQNA).append("myqna").toString();
    }


    /**
     * 홈페이지 관리 > 1:1 문의 수정
     */
    @GetMapping(path={"/{sequenceNo}"})
    public String getForm(HttpServletRequest request, Pageable pageable, Model model,
                          @Parameter(description = "1:1문의 일련 번호") @PathVariable(value = "sequenceNo", required = true) BigInteger sequenceNo) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("sequenceNo", sequenceNo);
            modelSetting(model, Optional.ofNullable(requestParam)
                .map(searchMap -> myQnaService.getList((MyQnaViewRequestVO) params(MyQnaViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("CON_TEXT_TYPE", "QNA_TYPE", "QNA_STATE"));
            return new StringBuilder(MYQNA).append("myqnaForm").toString();
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
