package kr.or.kpf.lms.biz.statistics.report.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.business.apply.vo.request.BizAplyApiRequestVO;
import kr.or.kpf.lms.biz.business.apply.vo.request.BizAplyViewRequestVO;
import kr.or.kpf.lms.biz.education.application.controller.EducationApplicaitonApiController;
import kr.or.kpf.lms.biz.education.application.service.EducationApplicationService;
import kr.or.kpf.lms.biz.education.application.vo.request.EducationApplicationViewRequestVO;
import kr.or.kpf.lms.biz.education.application.vo.response.EducationApplicationApiResponseVO;
import kr.or.kpf.lms.biz.education.schedule.controller.ScheduleApiController;
import kr.or.kpf.lms.biz.education.schedule.service.ScheduleService;
import kr.or.kpf.lms.biz.education.schedule.vo.request.ScheduleApiRequestVO;
import kr.or.kpf.lms.biz.education.schedule.vo.request.ScheduleViewRequestVO;
import kr.or.kpf.lms.biz.education.schedule.vo.response.ScheduleApiResponseVO;
import kr.or.kpf.lms.biz.statistics.adminuser.vo.StatisticsAdminUserExcelVO;
import kr.or.kpf.lms.biz.statistics.adminuser.vo.request.StatisticsAdminUserViewRequestVO;
import kr.or.kpf.lms.biz.statistics.report.service.ReportService;
import kr.or.kpf.lms.biz.statistics.report.vo.request.ReportEducationRequestVO;
import kr.or.kpf.lms.biz.statistics.report.vo.request.ReportScheduleRequestVO;
import kr.or.kpf.lms.biz.statistics.report.vo.request.ReportUserRequestVO;
import kr.or.kpf.lms.biz.statistics.report.vo.response.ReportEducationResponseVO;
import kr.or.kpf.lms.biz.statistics.report.vo.response.ReportScheduleResponseVO;
import kr.or.kpf.lms.biz.statistics.report.vo.response.ReportUserResponseVO;
import kr.or.kpf.lms.biz.statistics.webuser.service.UserStateService;
import kr.or.kpf.lms.common.exceldownload.excel.OneSheetExcelFile;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import kr.or.kpf.lms.repository.entity.education.EducationPlan;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 통계 관리 > 통계 보고서 View 관련 Controller
 */
@Tag(name = "Statistics Management", description = "통계 관리 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/statistics/report")
public class ReportApiController extends CSViewControllerSupport {
    private final ReportService reportService;

    /**
     * 통계 보고서 조회
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Statistics Management", description = "통계 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "경영평가 보고서 데이터 조회", content = @Content(schema = @Schema(implementation = ReportEducationResponseVO.class)))})
    @Operation(operationId="Report Education", summary = "과정별", description = "과정별 데이터를 조회한다.")
    @GetMapping(value = "/education", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReportEducationResponseVO> getEducation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CSSearchMap csSearchMap = CSSearchMap.of(request);
        List<ReportEducationResponseVO> list = reportService.getEducation((ReportEducationRequestVO) params(ReportEducationRequestVO.class, csSearchMap, null));
        return list;
    }

    @Tag(name = "Statistics Management", description = "통계 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "경영평가 보고서 데이터 조회", content = @Content(schema = @Schema(implementation = ReportScheduleResponseVO.class)))})
    @Operation(operationId="Report Schedule", summary = "차시별", description = "차시별 데이터를 조회한다.")
    @GetMapping(value = "/schedule", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReportScheduleResponseVO> getSchedule(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CSSearchMap csSearchMap = CSSearchMap.of(request);
        List<ReportScheduleResponseVO> list = reportService.getSchedule((ReportScheduleRequestVO) params(ReportScheduleRequestVO.class, csSearchMap, null));
        return list;
    }

    @Tag(name = "Statistics Management", description = "통계 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "경영평가 보고서 데이터 조회", content = @Content(schema = @Schema(implementation = ReportUserResponseVO.class)))})
    @Operation(operationId="Report User", summary = "신청자별", description = "신청자별 데이터를 조회한다.")
    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReportUserResponseVO> getUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CSSearchMap csSearchMap = CSSearchMap.of(request);
        List<ReportUserResponseVO> list = reportService.getUser((ReportUserRequestVO) params(ReportUserRequestVO.class, csSearchMap, null));
        return list;
    }
}
