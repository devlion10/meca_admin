package kr.or.kpf.lms.biz.education.schedule.controller;

import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.common.upload.service.FileMasterService;
import kr.or.kpf.lms.biz.education.schedule.service.ScheduleService;
import kr.or.kpf.lms.biz.education.schedule.vo.request.ScheduleApiRequestVO;
import kr.or.kpf.lms.biz.education.schedule.vo.request.ScheduleViewRequestVO;
import kr.or.kpf.lms.biz.education.schedule.vo.response.ScheduleApiResponseVO;
import kr.or.kpf.lms.biz.education.schedule.vo.response.ScheduleExcelVO;
import kr.or.kpf.lms.biz.education.schedule.vo.response.ScheduleViewResponseVO;
import kr.or.kpf.lms.common.exceldownload.excel.OneSheetExcelFile;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 교육 관리 > 교육 개설 API 관련 Controller
 */
@Tag(name = "Education Management", description = "교육 관리 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/education/schedule")
public class ScheduleApiController extends CSApiControllerSupport {
    private final FileMasterService fileMasterService;
    private final ScheduleService scheduleService;

    /**
     * Sample (객체 Swagger 표출을 위해서 사용)
     *
     * @param request
     * @param response
     * @param scheduleViewRequestVO
     * @return
     */
    @Tag(name = "Swagger USE(NOT API)", description = "사용하지 않는 API(객체 표출용)")
    @PostMapping(value = "/sample", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScheduleViewResponseVO> swaggerUse1(HttpServletRequest request, HttpServletResponse response, @RequestBody ScheduleViewRequestVO scheduleViewRequestVO) {
        return null;
    }

    /**
     * 교육 일정 생성
     *
     * @param request
     * @param response
     * @param scheduleApiRequestVO
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "교육 일정 생성 성공", content = @Content(schema = @Schema(implementation = ScheduleApiResponseVO.class)))})
    @Operation(operationId="Schedule", summary = "교육 일정 생성", description = "교육 일정를 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScheduleApiResponseVO> createEducationSchedule(HttpServletRequest request, HttpServletResponse response,
        @Validated(value = {CreateEducationSchedule.class}) @NotNull @RequestBody ScheduleApiRequestVO scheduleApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(scheduleService.createEducationSchedule(scheduleApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3061, "교육 일정 생성 실패")));
    }

    public interface CreateEducationSchedule {}

    /**
     * 교육 일정 업데이트
     *
     * @param request
     * @param response
     * @param scheduleApiRequestVO
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "교육 일정 수정 성공", content = @Content(schema = @Schema(implementation = ScheduleApiResponseVO.class)))})
    @Operation(operationId="Schedule", summary = "교육 일정 업데이트", description = "교육 일정를 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScheduleApiResponseVO> updateEducationSchedule(HttpServletRequest request, HttpServletResponse response,
                                                                        @Validated(value = {UpdateEducationSchedule.class}) @NotNull @RequestBody ScheduleApiRequestVO scheduleApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(scheduleService.updateEducationSchedule(scheduleApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3063, "교육 일정 수정 실패")));
    }

    public interface UpdateEducationSchedule {}

    /**
     * 교육 일정 썸네일 파일 업로드 API
     *
     * @param request
     * @param response
     * @param educationPlanCode
     * @param thumbnailFile
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "교육 일정 썸네일 파일 업로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId="Schedule", summary = "교육 일정 썸네일 파일 업로드", description = "교육 일정 썸네일 파일을 업로드한다.")
    @PutMapping(value = "/upload/thumbnailFile/{educationPlanCode}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CSResponseVOSupport> thumbnailFileUpload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "콘텐츠 코드") @PathVariable(value = "educationPlanCode", required = true) String educationPlanCode,
                                                          @Parameter(description = "썸네일 파일") @RequestPart(value = "thumbnailFile", required = true) MultipartFile thumbnailFile) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(scheduleService.thumbnailFileUpload(educationPlanCode, thumbnailFile))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9005, "교육 일정 썸네일 파일 업로드 실패")));
    }
    /**
     * 교육 일정 썸네일 파일 업로드 API
     *
     * @param request
     * @param response
     * @param educationPlanCode
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "교육 일정 썸네일 파일 업로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId="Schedule", summary = "교육 일정 썸네일 파일 업로드", description = "교육 일정 썸네일 파일을 업로드한다.")
    @DeleteMapping(value = "/delete/thumbnailFile/{educationPlanCode}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CSResponseVOSupport> thumbnailFileDelete(HttpServletRequest request, HttpServletResponse response,
                                                                   @Parameter(description = "콘텐츠 코드") @PathVariable(value = "educationPlanCode", required = true) String educationPlanCode) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(scheduleService.thumbnailFileDelete(educationPlanCode))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3063, "교육 일정 썸네일 파일 삭제 실패")));
    }

    /**
     * 교육 일정 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @Operation(operationId = "Schedule", summary = "교육 과정 엑셀", description = "교육 과정 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/excel")
    public void getListExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Date date = new Date();
        String dateToStr = DateFormatUtils.format(date, "yyyyMMdd_HHmmSS_");

        List<ScheduleExcelVO> scheduleExcelVOList = scheduleService.getExcel((ScheduleViewRequestVO) params(ScheduleViewRequestVO.class, CSSearchMap.of(request), null));
        OneSheetExcelFile<ScheduleExcelVO> excelFile = new OneSheetExcelFile<>(scheduleExcelVOList, ScheduleExcelVO.class);
        response.setHeader("Content-Disposition", "attachment; filename=" + dateToStr+ URLEncoder.encode("과정개설관리", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");
        excelFile.write(response.getOutputStream());
    }
}
