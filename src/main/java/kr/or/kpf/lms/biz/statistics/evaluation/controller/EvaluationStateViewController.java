package kr.or.kpf.lms.biz.statistics.evaluation.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.statistics.evaluation.service.EvaluateStatisticsService;
import kr.or.kpf.lms.biz.statistics.evaluation.vo.request.EvaluateCurriculumStatisticsRequestVO;
import kr.or.kpf.lms.biz.statistics.evaluation.vo.request.EvaluateQuestionStatisticsRequestVO;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
 * 통계 관리 > 강의 평가 관리 View 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/statistics/evaluation")
public class EvaluationStateViewController extends CSViewControllerSupport {
    private static final String STATISTICS = "views/statistics/";
    private final EvaluateStatisticsService evaluateStatisticsService;

    /**
     * 강의 평가 관리 - 설문지별 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path = {"", "/"})
    public String getQuestion(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            modelSetting(model, Optional.of(CSSearchMap.of(request))
                    .map(searchMap -> evaluateStatisticsService.getQuestionStatistics((EvaluateQuestionStatisticsRequestVO) params(EvaluateQuestionStatisticsRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("EVAL_TYPE"));
            return new StringBuilder(STATISTICS).append("evaluationQuestionStatistics").toString();
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
     * 강의 평가 관리 - 설문지별 조회 (상세)
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path = {"/{evaluateSerialNo}"})
    public String getQuestionDetail(HttpServletRequest request, Pageable pageable, Model model,
        @Parameter(description = "강의 평가 일련 번호") @PathVariable(value = "evaluateSerialNo", required = true) String evaluateSerialNo) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("evaluateSerialNo", evaluateSerialNo);
            model.addAttribute("content", Optional.ofNullable(requestParam)
                    .map(searchMap -> evaluateStatisticsService.getQuestionStatisticsDetail((EvaluateQuestionStatisticsRequestVO) params(EvaluateQuestionStatisticsRequestVO.class, searchMap, pageable)))
                    .orElse(new ArrayList<>()));
            return new StringBuilder(STATISTICS).append("evaluationQuestionStatisticsDetail").toString();
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
     * 강의 평가 관리 - 강좌(과정)별 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path = {"/education"})
    public String getEducation(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            modelSetting(model, Optional.of(CSSearchMap.of(request))
                    .map(searchMap -> evaluateStatisticsService.getCurriculumStatistics((EvaluateCurriculumStatisticsRequestVO) params(EvaluateCurriculumStatisticsRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("EDU_TYPE", "CTS_CTGR"));
            return new StringBuilder(STATISTICS).append("evaluationEducationStatistics").toString();
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
     * 강의 평가 관리 - 강좌(과정)별 조회 (상세)
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path = {"/education/{curriculumCode}"})
    public String getEducationDetail(HttpServletRequest request, Pageable pageable, Model model,
            @Parameter(description = "과정 코드") @PathVariable(value = "curriculumCode", required = true) String curriculumCode) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("curriculumCode", curriculumCode);
            model.addAttribute("content", Optional.of(requestParam)
                    .map(searchMap -> evaluateStatisticsService.getCurriculumStatisticsDetail((EvaluateCurriculumStatisticsRequestVO) params(EvaluateCurriculumStatisticsRequestVO.class, searchMap, pageable)))
                    .orElse(new ArrayList<>()));
            return new StringBuilder(STATISTICS).append("evaluationEducationStatisticsDetail").toString();
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
