package kr.or.kpf.lms.biz.education.exam.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.education.curriculum.service.CurriculumService;
import kr.or.kpf.lms.biz.education.curriculum.vo.request.CurriculumViewRequestVO;
import kr.or.kpf.lms.biz.education.exam.service.ExamService;
import kr.or.kpf.lms.biz.education.exam.vo.request.ExamViewRequestVO;
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
import java.util.Optional;

/**
 * 교육 관리 > 교육 과정 관리 VIEW 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/education/curriculum/e-learning/exam")
public class ExamViewController extends CSViewControllerSupport {

    private static final String EDUCATION = "views/education/curriculum/";

    private final ExamService examService;

    /**
     * 시험 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path = {"/{curriculumCode}"})
    public String getList(HttpServletRequest request, Pageable pageable, Model model,
                          @Parameter(description = "교육 과정 코드") @PathVariable(value = "curriculumCode", required = true) String curriculumCode) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("curriculumCode", curriculumCode);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> examService.getList((ExamViewRequestVO) params(ExamViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("CTS_CTGR"));
            CurriculumMaster curriculumMaster = examService.getCurriculumInfo(curriculumCode);
            model.addAttribute("curriculumCode", curriculumMaster.getCurriculumCode());
            model.addAttribute("curriculumName", curriculumMaster.getCurriculumName());
            model.addAttribute("educationCategoryCode", curriculumMaster.getCategoryCode());
            return new StringBuilder(EDUCATION).append("ElearningExam").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /**
     * 시험 등록
     */
    @GetMapping(path = {"/{curriculumCode}/regist"})
    public String getWrite(HttpServletRequest request, Pageable pageable, Model model,
                           @Parameter(description = "교육 과정 코드") @PathVariable(value = "curriculumCode", required = true) String curriculumCode) {
        try {
            model.addAttribute("curriculumCode", curriculumCode);
            model.addAttribute("content", new ArrayList<>());
            return new StringBuilder(EDUCATION).append("ElearningExamForm").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /**
     * 시험 개별 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path = {"/{curriculumCode}/{examSerialNo}"})
    public String getForm(HttpServletRequest request, Pageable pageable, Model model,
                          @Parameter(description = "교육 과정 코드") @PathVariable(value = "curriculumCode", required = true) String curriculumCode,
                          @Parameter(description = "시험 일련 번호") @PathVariable(value = "examSerialNo", required = true) String examSerialNo) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("curriculumCode", curriculumCode);
            requestParam.put("examSerialNo", examSerialNo);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> examService.getList((ExamViewRequestVO) params(ExamViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
            model.addAttribute("curriculumCode", curriculumCode);
            model.addAttribute("examSerialNo", examSerialNo);
            return new StringBuilder(EDUCATION).append("ElearningExamForm").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }
}
