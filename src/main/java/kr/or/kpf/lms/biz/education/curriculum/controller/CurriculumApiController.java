package kr.or.kpf.lms.biz.education.curriculum.controller;

import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.request.BizInstructorIdentifyViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.response.BizInstructorIdentifyExcelVO;
import kr.or.kpf.lms.biz.contents.contents.vo.response.ContentsApiResponseVO;
import kr.or.kpf.lms.biz.education.curriculum.service.CurriculumService;
import kr.or.kpf.lms.biz.education.curriculum.vo.CurriculumExcelVO;
import kr.or.kpf.lms.biz.education.curriculum.vo.request.CurriculumApiRequestVO;
import kr.or.kpf.lms.biz.education.curriculum.vo.request.CurriculumViewRequestVO;
import kr.or.kpf.lms.biz.education.curriculum.vo.response.CurriculumApiResponseVO;
import kr.or.kpf.lms.biz.education.curriculum.vo.response.CurriculumViewResponseVO;
import kr.or.kpf.lms.common.exceldownload.excel.OneSheetExcelFile;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 교육 관리 > 교육 과정 관리 API 관련 Controller
 */
@Tag(name = "Education Management", description = "교육 관리 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/education/curriculum")
public class CurriculumApiController extends CSApiControllerSupport {

    private final CurriculumService curriculumService;

    /**
     * Sample (객체 Swagger 표출을 위해서 사용)
     *
     * @param request
     * @param response
     * @param curriculumViewRequestVO
     * @return
     */
    @Tag(name = "Swagger USE(NOT API)", description = "사용하지 않는 API(객체 표출용)")
    @PostMapping(value = "/sample", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CurriculumViewResponseVO> swaggerUse1(HttpServletRequest request, HttpServletResponse response, @RequestBody CurriculumViewRequestVO curriculumViewRequestVO) {
        return null;
    }

    /**
     * 교육 과정 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "교육 과정 조회 성공", content = @Content(schema = @Schema(implementation = CurriculumApiResponseVO.class)))})
    @Operation(operationId="Curriculum", summary = "교육 과정 조회", description = "교육 과정을 조회한다.")
    @GetMapping(path = {"", "/"})
    public ResponseEntity<Object> getCurriculumInfo(HttpServletRequest request, Pageable pageable, Model model){
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> resultPaging(curriculumService.getList((CurriculumViewRequestVO) params(CurriculumViewRequestVO.class, searchMap, pageable)), new ArrayList<>()))
                        .orElse(new HashMap<>()));
    }

    /**
     * 교육 과정 조회 (엑셀)
     *
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "교육 과정 조회 엑셀 다운로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId="Curriculum", summary = "교육 과정 조회 엑셀 다운로드 ", description = "교육 과정 조회 엑셀 다운로드 한다.")
    @GetMapping(path = {"/excel"})
    public void getListExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Date date = new Date();
        String dateToStr = DateFormatUtils.format(date, "yyyyMMdd_HHmmSS_");
        List<CurriculumExcelVO> bizInstructorIdentifyExcelVOList = curriculumService.getExcel((CurriculumViewRequestVO) params(CurriculumViewRequestVO.class, CSSearchMap.of(request), null));
        OneSheetExcelFile<CurriculumExcelVO> excelFile = new OneSheetExcelFile<>(bizInstructorIdentifyExcelVOList, CurriculumExcelVO.class);
        response.setHeader("Content-Disposition", "attachment; filename=" + dateToStr+ URLEncoder.encode("과정관리", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");
        excelFile.write(response.getOutputStream());
    }

    /**
     * 교육 과정 생성 (일반)
     *
     * @param request
     * @param response
     * @param curriculumApiRequestVO
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "교육 과정 생성 성공", content = @Content(schema = @Schema(implementation = CurriculumApiResponseVO.class)))})
    @Operation(operationId="Curriculum", summary = "교육 과정 생성", description = "교육 과정를 생성한다.")
    @PostMapping(value = "/lecture/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CurriculumApiResponseVO> createGeneralCurriculumInfo(HttpServletRequest request, HttpServletResponse response,
        @Validated(value = {CreateGeneral.class}) @NotNull @RequestBody CurriculumApiRequestVO curriculumApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(curriculumService.createCurriculumInfo(curriculumApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3001, "교육 과정 생성 실패")));
    }

    public interface CreateGeneral {}

    /**
     * 교육 과정 생성 (이러닝)
     *
     * @param request
     * @param response
     * @param curriculumApiRequestVO
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "교육 과정 생성 성공", content = @Content(schema = @Schema(implementation = CurriculumApiResponseVO.class)))})
    @Operation(operationId="Curriculum", summary = "교육 과정 생성", description = "교육 과정를 생성한다.")
    @PostMapping(value = "/e-learning/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CurriculumApiResponseVO> createElearningCurriculumInfo(HttpServletRequest request, HttpServletResponse response,
                                                                        @Validated(value = {CreateElearning.class}) @NotNull @RequestBody CurriculumApiRequestVO curriculumApiRequestVO) {

        logger.info("___e-learning/create___");
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(curriculumService.createCurriculumInfo(curriculumApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3001, "교육 과정 생성 실패")));
    }

    public interface CreateElearning {}

    /**
     * 교육 과정 업데이트 (일반)
     *
     * @param request
     * @param response
     * @param curriculumApiRequestVO
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "교육 과정 수정 성공", content = @Content(schema = @Schema(implementation = CurriculumApiResponseVO.class)))})
    @Operation(operationId="Curriculum", summary = "교육 과정 업데이트", description = "교육 과정를 업데이트한다.")
    @PutMapping(value = "/lecture/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CurriculumApiResponseVO> updateGeneralCurriculumInfo(HttpServletRequest request, HttpServletResponse response,
        @Validated(value = {UpdateGeneral.class}) @NotNull @RequestBody CurriculumApiRequestVO curriculumApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(curriculumService.updateCurriculumInfo(curriculumApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3003, "교육 과정 수정 실패")));
    }

    public interface UpdateGeneral {}

    /**
     * 교육 과정 업데이트 (이러닝)
     *
     * @param request
     * @param response
     * @param curriculumApiRequestVO
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "교육 과정 수정 성공", content = @Content(schema = @Schema(implementation = CurriculumApiResponseVO.class)))})
    @Operation(operationId="Curriculum", summary = "교육 과정 업데이트", description = "교육 과정를 업데이트한다.")
    @PutMapping(value = "/e-learning/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CurriculumApiResponseVO> updateElearningCurriculumInfo(HttpServletRequest request, HttpServletResponse response,
                                                                        @Validated(value = {UpdateElearning.class}) @NotNull @RequestBody CurriculumApiRequestVO curriculumApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(curriculumService.updateCurriculumInfo(curriculumApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3003, "교육 과정 수정 실패")));
    }

    public interface UpdateElearning {}

    /**
     * 교육과정 신청서 양식 업로드 API
     *
     * @param request
     * @param response
     * @param curriculumCode
     * @param applicationFormFile
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "교육과정 신청서 양식 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "Contents", summary = "교육과정 신청서 양식", description = "교육과정 신청서 양식 파일을 업로드한다.")
    @PutMapping(value = "/upload/form/{curriculumCode}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CSResponseVOSupport> fileUpload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "교육과정 코드") @PathVariable(value = "curriculumCode", required = true) String curriculumCode,
                                                          @Parameter(description = "교육과정 신청서 양식 파일") @RequestPart(value = "applicationFormFile", required = true) MultipartFile applicationFormFile) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(curriculumService.fileUpload(curriculumCode, applicationFormFile))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9005, "교육과정 신청서 양식 업로드 실패")));
    }
}
