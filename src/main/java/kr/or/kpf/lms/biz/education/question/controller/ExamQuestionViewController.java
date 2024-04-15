package kr.or.kpf.lms.biz.education.question.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.education.exam.vo.request.ExamViewRequestVO;
import kr.or.kpf.lms.biz.education.question.service.ExamQuestionService;
import kr.or.kpf.lms.biz.education.question.vo.request.ExamQuestionViewRequestVO;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import kr.or.kpf.lms.repository.entity.education.CurriculumMaster;
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
import java.util.List;
import java.util.Optional;

/**
 * 교육 관리 > 교육 과정 관리 VIEW 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/education/curriculum/e-learning/question")
public class ExamQuestionViewController extends CSViewControllerSupport {

    private static final String EDUCATION = "views/education/curriculum/";

    private final ExamQuestionService examQuestionService;

    /**
     * 시험 문항 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path = {"/{curriculumCode}"})
    public String getList(HttpServletRequest request, Pageable pageable, Model model,
                          @Parameter(description = "교육 과정 코드") @PathVariable(value = "curriculumCode", required = true) String curriculumCode) throws KPFException {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("curriculumCode", curriculumCode);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> examQuestionService.getList((ExamQuestionViewRequestVO) params(ExamQuestionViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("QUE_LEVEL", "QUES_TYPE", "CTS_CTGR"));
            CurriculumMaster curriculumMaster = examQuestionService.getCurriculumInfo(curriculumCode);
            model.addAttribute("curriculumCode", curriculumMaster.getCurriculumCode());
            model.addAttribute("curriculumName", curriculumMaster.getCurriculumName());
            model.addAttribute("educationCategoryCode", curriculumMaster.getCategoryCode());
            return new StringBuilder(EDUCATION).append("ElearningQuestion").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /**
     * 시험 문항 개별 등록
     */
    @GetMapping(path = {"/{curriculumCode}/regist"})
    public String getWrite(HttpServletRequest request, Pageable pageable, Model model,
                           @Parameter(description = "교육 과정 코드") @PathVariable(value = "curriculumCode", required = true) String curriculumCode) {
        try {
            modelSetting(model, CSPageImpl.of(new ArrayList<>(), pageable, 0), Arrays.asList("QUE_LEVEL", "QUES_TYPE", "CTS_CTGR"));
            CurriculumMaster curriculumMaster = examQuestionService.getCurriculumInfo(curriculumCode);
            model.addAttribute("curriculumCode", curriculumMaster.getCurriculumCode());
            model.addAttribute("curriculumName", curriculumMaster.getCurriculumName());
            model.addAttribute("educationCategoryCode", curriculumMaster.getCategoryCode());
            return new StringBuilder(EDUCATION).append("ElearningQuestionForm").toString();
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
     * 시험 문항 일괄 등록
     */
    @GetMapping(path = {"/{curriculumCode}/file"})
    public String getSubmit(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            return new StringBuilder(EDUCATION).append("ElearningQuestionSubmit").toString();
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
     * 시험 문항 수정
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path = {"/{curriculumCode}/{questionSerialNo}"})
    public String getForm(HttpServletRequest request, Pageable pageable, Model model,
                          @Parameter(description = "교육 과정 코드") @PathVariable(value = "curriculumCode", required = true) String curriculumCode,
                          @Parameter(description = "교육 과정 시험 문제 코드") @PathVariable(value = "questionSerialNo", required = true) String questionSerialNo) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("curriculumCode", curriculumCode);
            requestParam.put("questionSerialNo", questionSerialNo);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> examQuestionService.getList((ExamQuestionViewRequestVO) params(ExamQuestionViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("QUE_LEVEL", "QUES_TYPE", "CTS_CTGR"));
            CurriculumMaster curriculumMaster = examQuestionService.getCurriculumInfo(curriculumCode);
            model.addAttribute("curriculumCode", curriculumMaster.getCurriculumCode());
            model.addAttribute("curriculumName", curriculumMaster.getCurriculumName());
            model.addAttribute("educationCategoryCode", curriculumMaster.getCategoryCode());
            return new StringBuilder(EDUCATION).append("ElearningQuestionForm").toString();
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
