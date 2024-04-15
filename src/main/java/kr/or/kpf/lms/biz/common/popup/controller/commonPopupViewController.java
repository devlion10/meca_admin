package kr.or.kpf.lms.biz.common.popup.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.business.organization.aply.edithist.service.BizEditHistService;
import kr.or.kpf.lms.biz.business.organization.aply.edithist.vo.request.BizEditHistViewRequestVO;
import kr.or.kpf.lms.biz.business.organization.aply.service.BizOrganizationAplyService;
import kr.or.kpf.lms.biz.business.organization.aply.vo.request.BizOrganizationAplyViewRequestVO;
import kr.or.kpf.lms.biz.business.pbanc.master.service.BizPbancService;
import kr.or.kpf.lms.biz.business.pbanc.master.vo.request.BizPbancViewRequestVO;
import kr.or.kpf.lms.biz.business.pbanc.master.vo.response.BizPbancCustomApiResponseVO;
import kr.or.kpf.lms.biz.business.pbanc.rslt.service.BizPbancRsltService;
import kr.or.kpf.lms.biz.business.pbanc.rslt.vo.request.BizPbancRsltViewRequestVO;
import kr.or.kpf.lms.biz.business.pbanc.rslt.vo.response.BizPbancRsltCustomApiResponseVO;
import kr.or.kpf.lms.biz.business.survey.qitem.service.BizSurveyQitemService;
import kr.or.kpf.lms.biz.business.survey.qitem.vo.request.BizSurveyQitemViewRequestVO;
import kr.or.kpf.lms.biz.education.application.service.EducationApplicationService;
import kr.or.kpf.lms.biz.education.application.vo.request.EducationApplicationViewRequestVO;
import kr.or.kpf.lms.biz.education.schedule.service.ScheduleService;
import kr.or.kpf.lms.biz.education.schedule.vo.request.ScheduleViewRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.guide.classsubject.service.ClassSubjectService;
import kr.or.kpf.lms.biz.homepage.archive.guide.classsubject.vo.request.ClassSubjectViewRequestVO;
import kr.or.kpf.lms.biz.user.adminuser.service.AdminUserService;
import kr.or.kpf.lms.biz.user.adminuser.vo.request.AdminUserViewRequestVO;
import kr.or.kpf.lms.biz.user.instructor.service.InstructorService;
import kr.or.kpf.lms.biz.user.instructor.vo.request.InstructorViewRequestVO;
import kr.or.kpf.lms.biz.user.organization.service.OrganizationService;
import kr.or.kpf.lms.biz.user.organization.vo.request.OrganizationViewRequestVO;
import kr.or.kpf.lms.biz.user.webuser.service.WebUserService;
import kr.or.kpf.lms.biz.user.webuser.vo.request.WebUserViewRequestVO;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.common.support.Code;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import kr.or.kpf.lms.repository.entity.BizOrganizationAply;
import kr.or.kpf.lms.repository.entity.role.AdminRoleGroup;
import kr.or.kpf.lms.repository.role.AdminRoleGroupRepository;
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
import java.util.List;
import java.util.Optional;

/**
 * 팝업 View 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/popup")
public class commonPopupViewController extends CSViewControllerSupport {
    /** 임시 페이지 경로 */
    private static final String POPUP = "views/popup/";
    /** 임시 페이지 경로 */
    private static final String POPUPEDU = "views/education/application/pop/";

    private final OrganizationService organizationService;
    private final BizPbancService bizPbancService;
    private final BizOrganizationAplyService bizOrganizationAplyService;
    private final BizEditHistService bizEditHistService;
    private final InstructorService instructorService;
    private final BizSurveyQitemService bizSurveyQitemService;
    private final ClassSubjectService classSubjectService;
    private final ScheduleService scheduleService;
    private final WebUserService webUserService;
    private final AdminUserService adminUserService;
    private final AdminRoleGroupRepository adminRoleGroupRepository;

    /** 사용자 관리 - 매체사 검색(언론인) */
    @GetMapping(path = {"/find/company"})
    public String getCompany(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("organizationType", "1");
            requestParam.put("isUsable", true);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> organizationService.getList((OrganizationViewRequestVO) params(OrganizationViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("ORG_TYPE"));
            return new StringBuilder(POPUP).append("findCompany").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /** 사용자 관리 - 학교/기관 검색(사업 권한자) */
    @GetMapping(path = {"/find/organization"})
    public String getOrganization(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("notOrganizationType", "1");
            requestParam.put("isUsable", true);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> organizationService.getList((OrganizationViewRequestVO) params(OrganizationViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("ORG_TYPE"));
            return new StringBuilder(POPUP).append("findOrganization").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /** 강사 검색 */
    @GetMapping(path = {"/find/instructor", "/check/instructor"})
    public String getInstructors(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("popup", "popup");
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> instructorService.getList((InstructorViewRequestVO) params(InstructorViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("INSTR_CTGR", "USER_STATE"));
            return new StringBuilder(POPUP).append("addInstructor").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /** 사업 검색 */
    @GetMapping("/find/pbanc")
    public String getPbanc(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                    .map(searchMap -> bizPbancService.getBizPbancList((BizPbancViewRequestVO) params(BizPbancViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("CON_TEXT_TYPE", "BIZ_PBANC_TYPE", "BIZ_PBANC_CTGR", "BIZ_PBANC_INSTR_SLCTN_METH" ));
            return new StringBuilder(POPUP).append("addBusiness").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /** 사업 관리 - 변경 이력 관리 */
    @GetMapping("/organization/apply/history/{bizOrgAplyNo}")
    public String getHistory(HttpServletRequest request, Pageable pageable, Model model,
                             @Parameter(description = "사업 공고 신청 일련 번호") @PathVariable(value = "bizOrgAplyNo", required = true) String bizOrgAplyNo) {
        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("bizEditHistTrgtNo", bizOrgAplyNo);
        modelSetting(model, Optional.ofNullable(requestParam)
                .map(searchMap -> bizEditHistService.getBizEditHistList((BizEditHistViewRequestVO) params(BizEditHistViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
        return new StringBuilder(POPUP).append("pbancApplyHistory").toString();
    }

    /** 문항 검색 */
    @GetMapping("/find/survey/qitem")
    public String getQitem(HttpServletRequest request, Pageable pageable, Model model) {
        modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                .map(searchMap -> bizSurveyQitemService.getBizSurveyQitemList((BizSurveyQitemViewRequestVO) params(BizSurveyQitemViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
        return new StringBuilder(POPUP).append("addSurveyQitem").toString();
    }

    /** 교과 검색 */
    @GetMapping("/class-subject")
    public String getSubject(HttpServletRequest request, Pageable pageable, Model model) {
        modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                .map(searchMap -> classSubjectService.getList((ClassSubjectViewRequestVO) params(ClassSubjectViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
        return new StringBuilder(POPUP).append("addSubject").toString();
    }

    /** 교육관리신청 개별 */
    @GetMapping("/education/apply/each/{educationPlanCode}")
    public String getEducationEach(HttpServletRequest request, Pageable pageable, Model model, @PathVariable(value = "educationPlanCode") String educationPlanCode){

        CSSearchMap requestValue = CSSearchMap.of(request);
        requestValue.put("educationPlanCode", educationPlanCode);
        Page <?> educationPlan = scheduleService.getList((ScheduleViewRequestVO) params(ScheduleViewRequestVO.class, requestValue,pageable.first()));

        model.addAttribute("educationPlan", educationPlan);

        modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                .map(searchMap -> webUserService.getList((WebUserViewRequestVO) params(WebUserViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
        return new StringBuilder(POPUPEDU).append("applyEach").toString();
    }

    /** 교육관리신청 일괄(excel to DB) */
    @GetMapping("/education/apply/every/{educationPlanCode}")
    public String getEducationEvery(HttpServletRequest request, Pageable pageable, Model model, @PathVariable(value = "educationPlanCode") String educationPlanCode){

        CSSearchMap requestValue = CSSearchMap.of(request);
        requestValue.put("educationPlanCode", educationPlanCode);

        modelSetting(model, Optional.ofNullable(requestValue)
                .map(searchMap -> scheduleService.getList((ScheduleViewRequestVO) params(ScheduleViewRequestVO.class, searchMap,pageable.first())))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList(""));
        return new StringBuilder(POPUPEDU).append("applyEvery").toString();
    }

    /** 리스트 */
    @GetMapping("/organization/apply/{bizPbancNo}")
    public String getBizOrgAplyList (HttpServletRequest request, Pageable pageable, Model model,
                                     @Parameter(description = "사업 공고 일련 번호") @PathVariable(value = "bizPbancNo", required = true) String bizPbancNo) {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("bizPbancNo", bizPbancNo);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> bizOrganizationAplyService.getBizOrganizationAplyList((BizOrganizationAplyViewRequestVO) params(BizOrganizationAplyViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
            return new StringBuilder(POPUP).append("bizOrgAplyList").toString();
    }

    /** 사업신청서 담당자 변경 */
    @GetMapping("/find/user")
    public String getUserList (HttpServletRequest request, Pageable pageable, Model model) {
        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("manager", "manager");
        modelSetting(model, Optional.ofNullable(requestParam)
                .map(searchMap -> webUserService.getList((WebUserViewRequestVO) params(WebUserViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("WEB_USER_ROLE", "BIZ_AUTH"));
        return new StringBuilder(POPUP).append("findUser").toString();
    }

    /** 교육 담당자 변경 */
    @GetMapping("/find/admin/user")
    public String getAdminUserList (HttpServletRequest request, Pageable pageable, Model model) {
        List<AdminRoleGroup> adminRoleGroups = adminRoleGroupRepository.findAll();
        if (adminRoleGroups != null && adminRoleGroups.size() > 0 && !adminRoleGroups.isEmpty())
            model.addAttribute("roleGroups", adminRoleGroups);

        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("manager", "manager");
        modelSetting(model, Optional.ofNullable(requestParam)
                .map(searchMap -> adminUserService.getList((AdminUserViewRequestVO) params(AdminUserViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("PROVINCE_CD", "TEAM_CTGR"));
        return new StringBuilder(POPUP).append("findUser").toString();
    }
}
