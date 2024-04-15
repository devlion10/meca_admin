package kr.or.kpf.lms.biz.education.schedule.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.education.schedule.service.ScheduleService;
import kr.or.kpf.lms.biz.education.schedule.vo.request.ScheduleViewRequestVO;
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
 * 교육 관리 > 교육 개설 View 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/education/schedule")
public class ScheduleViewController extends CSViewControllerSupport {

    private static final String CONTENTS = "views/education/schedule/";

    private final ScheduleService scheduleService;

    /**
     * 교육 관리 > 교육 개설 목록 조회
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
                    .map(searchMap -> scheduleService.getList((ScheduleViewRequestVO) params(ScheduleViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("EDU_TYPE", "CTS_CTGR", "OPER_TYPE", "APLYABLE_TYPE", "PROVINCE_CD"));
            return new StringBuilder(CONTENTS).append("schedule").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /**
     * 교육 과정 개설
     *
     */
    @GetMapping(path = {"/regist/{educationCode}"})
    public String getLectureWrite(HttpServletRequest request, Pageable pageable, Model model,
                                  @Parameter(description = "교육 유형") @PathVariable(value = "educationCode", required = true) String educationCode) {
        try {
            modelSetting(model, CSPageImpl.of(new ArrayList<>(), pageable, 0), Arrays.asList("EDU_TYPE", "CTS_CTGR", "OPER_TYPE", "APLYABLE_TYPE"));
            if ("lecture".equals(educationCode)) {
                return new StringBuilder(CONTENTS).append("LectureForm").toString();
            } else if("e-lecture".equals(educationCode)) {
                return new StringBuilder(CONTENTS).append("ElectureForm").toString();
            } else if ("e-learning".equals(educationCode)) {
                return new StringBuilder(CONTENTS).append("ElearningForm").toString();
            } else if ("parallel".equals(educationCode)) {
                return new StringBuilder(CONTENTS).append("ParallelForm").toString();
            } else {
                throw new KPFException(KPF_RESULT.ERROR9004, "유효하지 않는 교육 과정 생성 코드 타입");
            }
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    @GetMapping(path = {"/{educationCode}/{educationPlanCode}"})
    public String getForm(HttpServletRequest request, Pageable pageable, Model model,
                          @Parameter(description = "교육 유형") @PathVariable(value = "educationCode", required = true) String educationCode,
                          @Parameter(description = "교육 과정 개설 코드") @PathVariable(value = "educationPlanCode", required = true) String educationPlanCode) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("educationPlanCode", educationPlanCode);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> scheduleService.getList((ScheduleViewRequestVO) params(ScheduleViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("EDU_TYPE", "CTS_CTGR", "OPER_TYPE", "APLYABLE_TYPE"));

            if ("lecture".equals(educationCode)) {
                return new StringBuilder(CONTENTS).append("LectureForm").toString();
            } else if("e-lecture".equals(educationCode)) {
                return new StringBuilder(CONTENTS).append("ElectureForm").toString();
            } else if ("e-learning".equals(educationCode)) {
                return new StringBuilder(CONTENTS).append("ElearningForm").toString();
            } else if ("parallel".equals(educationCode)) {
                return new StringBuilder(CONTENTS).append("ParallelForm").toString();
            } else {
                throw new KPFException(KPF_RESULT.ERROR9004, "유효하지 않는 교육 과정 생성 코드 타입");
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
