package kr.or.kpf.lms.biz.education.application.controller;

import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.request.BizInstructorIdentifyViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.request.FormeBizlecinfoApiRequestVO;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.request.JournalismschoolApiRequestVO;
import kr.or.kpf.lms.biz.education.application.service.EducationApplicationService;
import kr.or.kpf.lms.biz.education.application.vo.CreateEducationApplication;
import kr.or.kpf.lms.biz.education.application.vo.UpdateEducationApplication;
import kr.or.kpf.lms.biz.education.application.vo.request.EducationApplicationApiRequestVO;
import kr.or.kpf.lms.biz.education.application.vo.request.EducationApplicationViewRequestVO;
import kr.or.kpf.lms.biz.education.application.vo.request.EducationStateApiRequestVO;
import kr.or.kpf.lms.biz.education.application.vo.response.EducationApplicationApiResponseVO;
import kr.or.kpf.lms.biz.education.application.vo.response.EducationApplicationExcelVO;
import kr.or.kpf.lms.biz.education.application.vo.response.EducationCompleteExcelVO;
import kr.or.kpf.lms.biz.education.curriculum.vo.request.CurriculumViewRequestVO;
import kr.or.kpf.lms.biz.education.curriculum.vo.response.CurriculumViewResponseVO;
import kr.or.kpf.lms.common.exceldownload.excel.OneSheetExcelFile;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.Code;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 교육 관리 > 교육 개설 API 관련 Controller
 */
@Tag(name = "Education Management", description = "교육 관리 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/education/application")
public class EducationApplicaitonApiController extends CSApiControllerSupport {

    private final EducationApplicationService educationApplicationService;

    /**
     * Sample (객체 Swagger 표출을 위해서 사용)
     *
     * @param request
     * @param response
     * @param educationApplicationApiRequestVO
     * @return
     */
    @Tag(name = "Swagger USE(NOT API)", description = "사용하지 않는 API(객체 표출용)")
    @PostMapping(value = "/sample", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EducationApplicationApiResponseVO> swaggerUse(HttpServletRequest request, HttpServletResponse response, @RequestBody EducationApplicationApiRequestVO educationApplicationApiRequestVO) {
        return null;
    }

    /**
     * 강제 교육 신청 API
     *
     * @param request
     * @param response
     * @param educationApplicationApiRequestVO
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강제 교육 신청 성공", content = @Content(schema = @Schema(implementation = EducationApplicationApiResponseVO.class)))})
    @Operation(operationId = "EducationApplication", summary = "강제 교육 신청", description = "강제 교육 신청 데이터를 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EducationApplicationApiResponseVO> createEducationApplication(HttpServletRequest request, HttpServletResponse response,
                                                                                        @Validated(value = {CreateEducationApplication.class}) @NotNull @RequestBody EducationApplicationApiRequestVO educationApplicationApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(educationApplicationService.createEducationApplication(educationApplicationApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3021, "교육 과정 신청 데이터 생성 실패")));
    }

    /**
     * 강제 교육 신청 API By 엑셀파일
     *
     * @param request
     * @param response
     * @param curriculumCode
     * @param excelFile
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강제 교육 신청 성공", content = @Content(schema = @Schema(implementation = EducationApplicationApiResponseVO.class)))})
    @Operation(operationId = "EducationApplication", summary = "강제 교육 신청 By 엑셀파일", description = "강제 교육 신청을 일괄적으로 생성한다.")
    @PostMapping(value = "/create-by-file/{curriculumCode}/{educationPlanCode}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CSResponseVOSupport> fileUpload(HttpServletRequest request, HttpServletResponse response,
                                                 @Parameter(description = "교육 과정 코드") @PathVariable(value = "curriculumCode", required = true) String curriculumCode,
                                                          @PathVariable(value = "educationPlanCode", required = true) String educationPlanCode,
                                                          @Parameter(description = "엑셀 파일") @RequestPart(value = "excelFile", required = true) MultipartFile excelFile) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(educationApplicationService.createEducationApplicationByFile(curriculumCode,educationPlanCode, excelFile))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3021, "교육 과정 신청 데이터 생성 실패")));
    }

    /**
     * 교육 신청 교육 유형
     *
     * @param request
     * @param response
     * @param educationApplicationApiRequestVO
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "교육 신청 교육 유형 성공", content = @Content(schema = @Schema(implementation = EducationApplicationApiResponseVO.class)))})
    @Operation(operationId = "EducationApplication", summary = "교육 신청 교육 유형", description = "교육 신청 교육 유형를 한다.")
    @PutMapping(value = "/update-approval-state", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<EducationApplicationApiResponseVO> updateEducationApplicationApprovalState(HttpServletRequest request, HttpServletResponse response,
                                                                                                     @Validated(value = {UpdateEducationApplication.class}) @NotNull @RequestBody EducationApplicationApiRequestVO educationApplicationApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(educationApplicationService.updateEducationApplicationApprovalState(educationApplicationApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3024, "교육 과정 신청 데이터 업데이트 실패")));
    }

    /**
     * 교육 신청 교육 유형
     *
     * @param request
     * @param response
     * @param educationApplicationApiRequestVO
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "교육 신청 교육 유형 성공", content = @Content(schema = @Schema(implementation = EducationApplicationApiResponseVO.class)))})
    @Operation(operationId = "EducationApplication", summary = "교육 신청 교육 유형", description = "교육 신청 교육 유형를 한다.")
    @PutMapping(value = "/update-education-type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<EducationApplicationApiResponseVO> updateEducationApplicationEducationType(HttpServletRequest request, HttpServletResponse response,
                                                                                                     @Validated(value = {UpdateEducationApplication.class}) @NotNull @RequestBody EducationApplicationApiRequestVO educationApplicationApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(educationApplicationService.updateEducationApplicationEducationType(educationApplicationApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3024, "교육 과정 신청 데이터 업데이트 실패")));
    }

    /**
     * 숙박 여부 변경
     *
     * @param request
     * @param response
     * @param educationApplicationApiRequestVO
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "숙박 여부 변경 성공", content = @Content(schema = @Schema(implementation = EducationApplicationApiResponseVO.class)))})
    @Operation(operationId = "EducationApplication", summary = "숙박 여부 변경", description = "숙박 여부를 변경 한다.")
    @PutMapping(value = "/update-accommodation-state", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<EducationApplicationApiResponseVO> updateAccommodationState(HttpServletRequest request, HttpServletResponse response,
                                                                                                     @Validated(value = {UpdateEducationApplication.class}) @NotNull @RequestBody EducationApplicationApiRequestVO educationApplicationApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(educationApplicationService.updateAccommodationState(educationApplicationApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3024, "교육 과정 신청 데이터 업데이트 실패")));
    }

    /**
     * 수강 교육 과정 상태 변경 처리(수료, 미수료)
     *
     * @param request
     * @param response
     * @param educationApplicationApiRequestVO
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수강 교육 과정 상태 변경 처리 성공", content = @Content(schema = @Schema(implementation = EducationApplicationApiResponseVO.class)))})
    @Operation(operationId = "EducationApplication", summary = "수강 교육 과정 상태 변경 처리(수료, 미수료)", description = "수강 교육 과정 상태 변경 처리를 한다.")
    @PutMapping(value = "/update-education-state", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<EducationApplicationApiResponseVO> updateEducationState(HttpServletRequest request, HttpServletResponse response,
                                                                                                     @Validated(value = {UpdateEducationApplication.class}) @NotNull @RequestBody EducationApplicationApiRequestVO educationApplicationApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(educationApplicationService.updateEducationState(educationApplicationApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3024, "교육 과정 신청 데이터 업데이트 실패")));
    }

    /**
     * 시험 응시 내역 초기화
     *
     * @param request
     * @param response
     * @param educationApplicationApiRequestVO
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "시험 응시 내역 성공", content = @Content(schema = @Schema(implementation = EducationApplicationApiResponseVO.class)))})
    @Operation(operationId = "EducationApplication", summary = "시험 응시 내역 초기화 처리", description = "시험 응시 내역을 초기화 한다.")
    @PutMapping(value = "/update-exam-reset", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<EducationApplicationApiResponseVO> updateExamReset(HttpServletRequest request, HttpServletResponse response,
                                                                                  @Validated(value = {UpdateEducationApplication.class}) @NotNull @RequestBody EducationApplicationApiRequestVO educationApplicationApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(educationApplicationService.updateExamReset(educationApplicationApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3028, "시험 응시 내역 초기화 실패")));
    }

    /**
     * 수강 교육 콘텐츠 완료 / 미완료 처리
     *
     * @param request
     * @param response
     * @param educationApplicationApiRequestVO
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수강 교육 콘텐츠 완료 / 미완료 처리 성공", content = @Content(schema = @Schema(implementation = EducationApplicationApiResponseVO.class)))})
    @Operation(operationId = "EducationApplication", summary = "수강 교육 콘텐츠 완료 / 미완료 처리", description = "수강 교육 콘텐츠 완료 / 미완료 처리를 한다.")
    @PutMapping(value = "/update-contents-complete", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<EducationApplicationApiResponseVO> updateContentsComplete(HttpServletRequest request, HttpServletResponse response,
                                                                                   @Validated(value = {UpdateEducationApplication.class}) @NotNull @RequestBody EducationApplicationApiRequestVO educationApplicationApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(educationApplicationService.updateContentsComplete(educationApplicationApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3024, "교육 과정 신청 데이터 업데이트 실패")));
    }

    /**
     * 이러닝 교육 챕터 완료 / 미완료 처리
     *
     * @param request
     * @param response
     * @param educationApplicationApiRequestVO
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수강 교육 챕터 완료 / 미완료 처리 성공", content = @Content(schema = @Schema(implementation = EducationApplicationApiResponseVO.class)))})
    @Operation(operationId = "EducationApplication", summary = "수강 교육 챕터 완료 / 미완료 처리", description = "수강 교육 챕터 완료 / 미완료 처리를 한다.")
    @PutMapping(value = "/update-chapter-complete", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<EducationApplicationApiResponseVO> updateChapterComplete(HttpServletRequest request, HttpServletResponse response,
                                                                                  @Validated(value = {UpdateEducationApplication.class}) @NotNull @RequestBody EducationApplicationApiRequestVO educationApplicationApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(educationApplicationService.updateChapterComplete(educationApplicationApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3024, "교육 과정 신청 데이터 업데이트 실패")));
    }

    /**
     * 차시 - 절 학습 진행 API (이러닝 교육)
     *
     * @param request
     * @param response
     * @param educationStateApiRequestVO
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수강 교육 절 완료 / 미완료 처리 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "Education Management", summary = "수강 교육 절 완료 / 미완료 처리", description = "절 학습 상태를 변경한다.")
    @PutMapping(value = "/chapter/section/complete", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> updateSectionComplete(HttpServletRequest request, HttpServletResponse response,
                                                                     @Validated(value = {SubmitSectionProgress.class}) @NotNull @RequestBody List<EducationStateApiRequestVO> educationStateApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(educationApplicationService.submitSectionProgress(educationStateApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3024, "절 학습 처리 실패")));
    }

    public interface SubmitSectionProgress {}

    /**
     * 화상 & 집합 교육 강의 완료 / 미완료 처리
     *
     * @param request
     * @param response
     * @param educationApplicationApiRequestVO
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "화상 & 집합 교육 강의 완료 / 미완료 처리 성공", content = @Content(schema = @Schema(implementation = EducationApplicationApiResponseVO.class)))})
    @Operation(operationId = "EducationApplication", summary = "화상 & 집합 교육 강의 완료 / 미완료 처리", description = "화상 & 집합 교육 강의 완료 / 미완료 처리를 한다.")
    @PutMapping(value = "/update-lecture-complete", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<EducationApplicationApiResponseVO> updateLectureComplete(HttpServletRequest request, HttpServletResponse response,
                                                                                   @Validated(value = {UpdateEducationApplication.class}) @NotNull @RequestBody EducationApplicationApiRequestVO educationApplicationApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(educationApplicationService.updateLectureComplete(educationApplicationApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3024, "교육 강의 데이터 업데이트 실패")));
    }

    /**
     * 이러닝교육 신청 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @Operation(operationId = "EducationApplication", summary = "이러닝교육 신청 엑셀", description = "이러닝교육 신청 목록 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/e-learning/excel")
    public void getListExcelElearning(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String dateToStr = DateFormatUtils.format(new Date(), "yyyyMMdd_HHmmSS_");

        CSSearchMap csSearchMap = CSSearchMap.of(request);
        csSearchMap.put("educationType", Code.EDU_TYPE.E_LEARNING.enumCode);

        List<EducationApplicationExcelVO> educationApplicationExcelVOList = educationApplicationService.getExcelApply((EducationApplicationViewRequestVO) params(EducationApplicationViewRequestVO.class, csSearchMap, null));
        OneSheetExcelFile<EducationApplicationExcelVO> excelFile = new OneSheetExcelFile<>(educationApplicationExcelVOList, EducationApplicationExcelVO.class);
        response.setHeader("Content-Disposition", "attachment; filename=" + dateToStr+ URLEncoder.encode("이러닝교육신청목록", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");
        excelFile.write(response.getOutputStream());
    }

    /**
     * 화상/집합교육 신청 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @Operation(operationId = "EducationApplication", summary = "화상/집합교육 신청 엑셀", description = "화상/집합교육 신청 목록 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/parallel/excel")
    public void getListExcelLecture(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String dateToStr = DateFormatUtils.format(new Date(), "yyyyMMdd_HHmmSS_");

        CSSearchMap csSearchMap = CSSearchMap.of(request);
        csSearchMap.put("educationType", "0");

        List<EducationApplicationExcelVO> educationApplicationExcelVOList = educationApplicationService.getExcelApply((EducationApplicationViewRequestVO) params(EducationApplicationViewRequestVO.class, csSearchMap, null));
        OneSheetExcelFile<EducationApplicationExcelVO> excelFile = new OneSheetExcelFile<>(educationApplicationExcelVOList, EducationApplicationExcelVO.class);
        response.setHeader("Content-Disposition", "attachment; filename=" + dateToStr+ URLEncoder.encode("화상/집합교육신청목록", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");
        excelFile.write(response.getOutputStream());
    }

    /**
     * 이러닝교육 수료 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @Operation(operationId = "EducationApplication", summary = "이러닝교육 수료 엑셀", description = "이러닝교육 수료 목록 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/e-learning/complete/excel")
    public void getListExcelElearningComplete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String dateToStr = DateFormatUtils.format(new Date(), "yyyyMMdd_HHmmSS_");

        CSSearchMap csSearchMap = CSSearchMap.of(request);
        csSearchMap.put("educationType", Code.EDU_TYPE.E_LEARNING.enumCode);

        List<EducationCompleteExcelVO> educationCompleteExcelVOList = educationApplicationService.getExcelComplete((EducationApplicationViewRequestVO) params(EducationApplicationViewRequestVO.class, csSearchMap, null));
        OneSheetExcelFile<EducationCompleteExcelVO> excelFile = new OneSheetExcelFile<>(educationCompleteExcelVOList, EducationCompleteExcelVO.class);
        response.setHeader("Content-Disposition", "attachment; filename=" + dateToStr+ URLEncoder.encode("이러닝교육수료목록", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");
        excelFile.write(response.getOutputStream());
    }

    /**
     * 화상교육 수료 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @Operation(operationId = "EducationApplication", summary = "화상교육 수료 엑셀", description = "화상교육 수료 목록 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/e-lecture/complete/excel")
    public void getListExcelElectureComplete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String dateToStr = DateFormatUtils.format(new Date(), "yyyyMMdd_HHmmSS_");

        CSSearchMap csSearchMap = CSSearchMap.of(request);
        csSearchMap.put("educationType", Code.EDU_TYPE.VIDEO.enumCode);

        List<EducationCompleteExcelVO> educationCompleteExcelVOList = educationApplicationService.getExcelComplete((EducationApplicationViewRequestVO) params(EducationApplicationViewRequestVO.class, csSearchMap, null));
        OneSheetExcelFile<EducationCompleteExcelVO> excelFile = new OneSheetExcelFile<>(educationCompleteExcelVOList, EducationCompleteExcelVO.class);
        response.setHeader("Content-Disposition", "attachment; filename=" + dateToStr+ URLEncoder.encode("화상교육수료목록", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");
        excelFile.write(response.getOutputStream());
    }
    /**
     * 집합교육 수료 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @Operation(operationId = "EducationApplication", summary = "집합교육 수료 엑셀", description = "집합교육 수료 목록 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/lecture/complete/excel")
    public void getListExcelLectureComplete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String dateToStr = DateFormatUtils.format(new Date(), "yyyyMMdd_HHmmSS_");

        CSSearchMap csSearchMap = CSSearchMap.of(request);
        csSearchMap.put("educationType", Code.EDU_TYPE.CONVOCATION.enumCode);

        List<EducationCompleteExcelVO> educationCompleteExcelVOList = educationApplicationService.getExcelComplete((EducationApplicationViewRequestVO) params(EducationApplicationViewRequestVO.class, csSearchMap, null));
        OneSheetExcelFile<EducationCompleteExcelVO> excelFile = new OneSheetExcelFile<>(educationCompleteExcelVOList, EducationCompleteExcelVO.class);
        response.setHeader("Content-Disposition", "attachment; filename=" + dateToStr+ URLEncoder.encode("집합교육수료목록", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");
        excelFile.write(response.getOutputStream());
    }

    /**
     * 포미 강의확인서 조회 API
     *
     * @param request
     * @param response
     * @param pageable
     * @return
     */
    @Tag(name = "journalismschool Old History", description = "언론인교육센터 API")
    @Operation(operationId = "journalismschool", summary = "언론인교육센터 API", description = "강의확인서 신청 조회한다.")
    @GetMapping(value = "/journalismschool", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getJournalismschoolList(HttpServletRequest request, HttpServletResponse response, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> educationApplicationService.getJournalismschoolList((JournalismschoolApiRequestVO) params(JournalismschoolApiRequestVO.class, searchMap, pageable)))
                        .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
    }
}
