package kr.or.kpf.lms.biz.education.curriculum.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.education.curriculum.service.CurriculumService;
import kr.or.kpf.lms.biz.education.curriculum.vo.request.CurriculumViewRequestVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
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
 * 교육 관리 > 교육 과정 관리 VIEW 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/education/curriculum")
public class CurriculumViewController extends CSViewControllerSupport {

    private static final String EDUCATION = "views/education/curriculum/";

    private final CurriculumService curriculumService;

    /**
     * 교육 과정 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path = {"", "/page"})
    public String getList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                    .map(searchMap -> curriculumService.getList((CurriculumViewRequestVO) params(CurriculumViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("EDU_TYPE", "CTS_CTGR", "EDU_CTGR", "EDU_CTGR_SUB", "APLY_APPR_TYPE", "EUD_FNSH_TYPE", "EDU_TARGET", "EUD_FNSH_TYPE"));
            return new StringBuilder(EDUCATION).append("curriculum").toString();
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
     * 교육 과정 등록
     *
     */
    @GetMapping(path = {"/regist/{educationCode}"})
    public String getLectureWrite(HttpServletRequest request, Pageable pageable, Model model,
                      @Parameter(description = "교육 유형") @PathVariable(value = "educationCode", required = true) String educationCode) {
        modelSetting(model, CSPageImpl.of(new ArrayList<>(), pageable, 0), Arrays.asList("CTS_CTGR", "EDU_TYPE", "EDU_CTGR", "EDU_CTGR_SUB", "APLY_APPR_TYPE", "ENFRC_TYPE", "EDU_TARGET", "EUD_FNSH_TYPE", "EVAL_TYPE"));
        try {
            if ("lecture".equals(educationCode)) {
                return new StringBuilder(EDUCATION).append("LectureForm").toString();
            } else if ("e-learning".equals(educationCode)) {
                return new StringBuilder(EDUCATION).append("ElearningForm").toString();
            } else {
                throw new KPFException(KPF_RESULT.ERROR9004, "유효하지 않는 교육 과정 생성 코드 타입");
            }
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
     * 교육 과정 수정 및 상세
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path = {"/{educationCode}/{curriculumCode}"})
    public String getLectureForm(HttpServletRequest request, Pageable pageable, Model model,
                                 @Parameter(description = "교육 유형") @PathVariable(value = "educationCode", required = true) String educationCode,
                                 @Parameter(description = "교육 과정 코드") @PathVariable(value = "curriculumCode", required = true) String curriculumCode) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("curriculumCode", curriculumCode);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> curriculumService.getList((CurriculumViewRequestVO) params(CurriculumViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("CTS_CTGR", "EDU_TYPE", "EDU_CTGR", "EDU_CTGR_SUB", "APLY_APPR_TYPE", "ENFRC_TYPE", "EDU_TARGET", "EUD_FNSH_TYPE", "EVAL_TYPE"));
            if ("lecture".equals(educationCode)) {
                return new StringBuilder(EDUCATION).append("LectureForm").toString();
            } else if ("e-learning".equals(educationCode)) {
                return new StringBuilder(EDUCATION).append("ElearningForm").toString();
            } else {
                throw new KPFException(KPF_RESULT.ERROR9004, "유효하지 않는 교육 과정 코드 타입");
            }
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
