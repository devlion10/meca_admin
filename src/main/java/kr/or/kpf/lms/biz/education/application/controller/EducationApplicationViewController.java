package kr.or.kpf.lms.biz.education.application.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.education.application.service.EducationApplicationService;
import kr.or.kpf.lms.biz.education.application.vo.request.EducationApplicationViewRequestVO;
import kr.or.kpf.lms.biz.education.schedule.service.ScheduleService;
import kr.or.kpf.lms.biz.education.schedule.vo.request.ScheduleViewRequestVO;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.common.support.Code;
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
 * 교육 관리 > 교육 운영 관리 View 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/education/application")
public class EducationApplicationViewController extends CSViewControllerSupport {

    private static final String EDUCATION_APPLICATION = "views/education/application/";

    private final EducationApplicationService educationApplicationService;
    private final ScheduleService scheduleService;

    /**
     * 교육 관리 > 교육 운영 관리 - 이러닝 신청 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path={"/e-learning/apply"})
    public String getELearningApplyList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            CSSearchMap csSearchMap = CSSearchMap.of(request);
            csSearchMap.put("educationType", Code.EDU_TYPE.E_LEARNING.enumCode);
            modelSetting(model, Optional.ofNullable(csSearchMap)
                    .map(searchMap -> educationApplicationService.getList((EducationApplicationViewRequestVO) params(EducationApplicationViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("EDU_TYPE", "CTS_CTGR", "APLYABLE_TYPE", "OPER_TYPE", "ADM_APL_STATE"));
            return new StringBuilder(EDUCATION_APPLICATION).append("ElearningApply").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /**
     * 교육 관리 > 교육 운영 관리 - 이러닝 수료 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path={"/e-learning/complete"})
    public String getELearningCompleteBeforeList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            CSSearchMap csSearchMap = CSSearchMap.of(request);
            csSearchMap.put("educationType", Code.EDU_TYPE.E_LEARNING.enumCode);
            /** 관리자 승인 상태가 승인 처리된 신청에 대해서만 교육 수료 처리 가능 */
            csSearchMap.put("adminApprovalState", Code.ADM_APL_STATE.APPROVAL.enumCode);
            modelSetting(model, Optional.ofNullable(csSearchMap)
                    .map(searchMap -> educationApplicationService.getList((EducationApplicationViewRequestVO) params(EducationApplicationViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("EDU_TYPE", "CTS_CTGR", "APLYABLE_TYPE", "OPER_TYPE", "ADM_APL_STATE"));
            return new StringBuilder(EDUCATION_APPLICATION).append("ElearningCompletion").toString();

        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /**
     * 교육 관리 > 교육 운영 관리 - 화상/집합 신청 조회
     */
    @GetMapping(path={"/lecture/apply"})
    public String getLectureApplyList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            CSSearchMap csSearchMap = CSSearchMap.of(request);
            csSearchMap.put("educationType", "0");
            modelSetting(model, Optional.ofNullable(csSearchMap)
                    .map(searchMap -> scheduleService.getList((ScheduleViewRequestVO) params(ScheduleViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("EDU_TYPE", "CTS_CTGR", "APLYABLE_TYPE", "OPER_TYPE", "ADM_APL_STATE", "PROVINCE_CD"));
            return new StringBuilder(EDUCATION_APPLICATION).append("LectureApply").toString();

        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /**
     * 교육 관리 > 교육 운영 관리 - 화상/집합 신청 상세
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path={"/lecture/apply/{educationPlanCode}"})
    public String getLectureApplyDetailList(HttpServletRequest request, Pageable pageable, Model model, @PathVariable(value = "educationPlanCode") String educationPlanCode) {
        try {
            CSSearchMap csSearchMap = CSSearchMap.of(request);
            csSearchMap.put("educationPlanCode", educationPlanCode);
            modelSetting(model, Optional.ofNullable(csSearchMap)
                    .map(searchMap -> educationApplicationService.getList((EducationApplicationViewRequestVO) params(EducationApplicationViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("EDU_TYPE", "CTS_CTGR", "APLYABLE_TYPE", "OPER_TYPE", "ADM_APL_STATE"));
            return new StringBuilder(EDUCATION_APPLICATION).append("LectureApplyForm").toString();

        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /**
     * 교육 관리 > 교육 운영 관리 - 화상/집합 수료 조회 (교육 단위)
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path={"/lecture/complete"})
    public String getLectureCompleteBeforeList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            CSSearchMap csSearchMap = CSSearchMap.of(request);
            csSearchMap.put("educationType", "0");
            modelSetting(model, Optional.ofNullable(csSearchMap)
                    .map(searchMap -> scheduleService.getList((ScheduleViewRequestVO) params(ScheduleViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("EDU_TYPE", "CTS_CTGR", "APLYABLE_TYPE", "OPER_TYPE", "ADM_APL_STATE", "PROVINCE_CD"));
            return new StringBuilder(EDUCATION_APPLICATION).append("LectureCompletionBefore").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /**
     * 교육 관리 > 교육 운영 관리 - 화상 집합 수료 상세 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path={"/lecture/complete/{educationPlanCode}"})
    public String getLectureCompleteList(HttpServletRequest request, Pageable pageable, Model model,
        @Parameter(description = "교육 시리얼 번호") @PathVariable(value = "educationPlanCode", required = true) String educationPlanCode) {
        try {
            CSSearchMap requestParams = CSSearchMap.of(request);
            requestParams.put("educationType", "0");
            /** 관리자 승인 상태가 승인 처리된 신청에 대해서만 교육 수료 처리 가능 */
            requestParams.put("adminApprovalState", Code.ADM_APL_STATE.APPROVAL.enumCode);
            requestParams.put("educationPlanCode", educationPlanCode);
            modelSetting(model, Optional.ofNullable(requestParams)
                    .map(searchMap -> educationApplicationService.getList((EducationApplicationViewRequestVO) params(EducationApplicationViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("EDU_TYPE", "CTS_CTGR", "APLYABLE_TYPE", "OPER_TYPE", "ADM_APL_STATE"));
            return new StringBuilder(EDUCATION_APPLICATION).append("LectureCompletion").toString();

        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /**
     * 교육 관리 > 교육 운영 관리 - 집합교육 출석률
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path={"/lecture/complete/attend/{applicationNo}"})
    public String getLectureAttend(HttpServletRequest request, Pageable pageable, Model model,@PathVariable(value = "applicationNo") String applicationNo) {
        try {
            CSSearchMap requestParams = CSSearchMap.of(request);
            requestParams.put("educationType", "0");
            requestParams.put("applicationNo", applicationNo);
            requestParams.put("adminApprovalState", Code.ADM_APL_STATE.APPROVAL.enumCode);
            modelSetting(model, Optional.ofNullable(requestParams)
                    .map(searchMap -> educationApplicationService.getList((EducationApplicationViewRequestVO) params(EducationApplicationViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList());
            return new StringBuilder(EDUCATION_APPLICATION).append("pop/attend").toString();

        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /**
     * 수료증(일반)
     */
    @GetMapping(path={"/lecture/complete/certification", "/e-lecture/complete/certification"})
    public String getEducationCertification(Model model) {
        try {
            return new StringBuilder(EDUCATION_APPLICATION).append("pop/certification").toString();
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
     * 이수증
     */
    @GetMapping(path={"/lecture/complete/teacher/certification", "/e-lecture/complete/teacher/certification"})
    public String getTeacherEducationCertification(Model model) {
        try {
            return new StringBuilder(EDUCATION_APPLICATION).append("pop/certi_achievement").toString();
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
     * 수료증(이러닝)
     */
    @GetMapping(path={"/e-learning/complete/certification"})
    public String getElearningCertification(Model model) {
        try {
            return new StringBuilder(EDUCATION_APPLICATION).append("pop/certification_eLearning").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }
}
