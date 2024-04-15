package kr.or.kpf.lms.biz.contents.question.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.contents.evaluate.service.EvaluateService;
import kr.or.kpf.lms.biz.contents.evaluate.vo.request.EvaluateViewRequestVO;
import kr.or.kpf.lms.biz.contents.question.service.EvaluateQuestionService;
import kr.or.kpf.lms.biz.contents.question.vo.request.EvaluateQuestionApiRequestVO;
import kr.or.kpf.lms.biz.contents.question.vo.request.EvaluateQuestionViewRequestVO;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.config.security.KoreaPressFoundationUser;
import kr.or.kpf.lms.config.security.vo.KoreaPressFoundationUserDetails;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
 * 콘텐츠 관리 > 강의 평가 관리 문항 View 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/contents/question")
public class EvaluateQuestionViewController extends CSViewControllerSupport {

    private static final String EVALUATE = "views/contents/evaluate/";

    private final EvaluateQuestionService evaluateQuestionService;

    /**
     * 강의 평가 관리 문항 목록 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path={"", "/", "/page"})
    public String getList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                    .map(searchMap -> evaluateQuestionService.getList((EvaluateQuestionViewRequestVO) params(EvaluateQuestionViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("QUES_TYPE", "EVAL_QUES_CTGR"));
            return new StringBuilder(EVALUATE).append("questionItem").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /**
     * 강의 평가 관리 문항 등록
     */
    @GetMapping(path={"/regist"})
    public String getRegistForm(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            modelSetting(model, CSPageImpl.of(new ArrayList<>(), pageable, 0), Arrays.asList("QUES_TYPE", "EVAL_QUES_CTGR"));
            return new StringBuilder(EVALUATE).append("questionItemForm").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /**
     * 강의 평가 관리 문항 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path={"/{questionSerialNo}"})
    public String getForm(HttpServletRequest request, Pageable pageable, Model model,
                          @Parameter(description = "강의 평가 질문 일련 번호") @PathVariable(value = "questionSerialNo", required = true) String questionSerialNo) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("questionSerialNo", questionSerialNo);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> evaluateQuestionService.getList((EvaluateQuestionViewRequestVO) params(EvaluateQuestionViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("QUES_TYPE", "EVAL_QUES_CTGR"));
            return new StringBuilder(EVALUATE).append("questionItemForm").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }
}