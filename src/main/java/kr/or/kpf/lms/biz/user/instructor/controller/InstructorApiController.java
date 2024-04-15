package kr.or.kpf.lms.biz.user.instructor.controller;

import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.request.JournalismschoolApiRequestVO;
import kr.or.kpf.lms.biz.business.pbanc.master.vo.request.BizPbancViewRequestVO;
import kr.or.kpf.lms.biz.user.instructor.service.InstructorService;
import kr.or.kpf.lms.biz.user.instructor.vo.request.*;
import kr.or.kpf.lms.biz.user.instructor.vo.response.InstructorApiResponseVO;
import kr.or.kpf.lms.biz.user.instructor.vo.response.InstructorExcelVO;
import kr.or.kpf.lms.biz.user.webuser.vo.request.WebUserViewRequestVO;
import kr.or.kpf.lms.biz.user.webuser.vo.response.WebUserExcelVO;
import kr.or.kpf.lms.common.exceldownload.excel.OneSheetExcelFile;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
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
 * 강사 관리 > 강사 관리 API 관련 Controller
 */
@Tag(name = "Instructor Management", description = "강사 관리 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/user/instructor")
public class InstructorApiController extends CSApiControllerSupport {

    private final InstructorService instructorService;

    /**
     * 강사 정보 조회
     *
     * @param request
     * @param response
     * @param pageable
     * @return
     */
    @Tag(name = "Instructor Management", description = "강사 관리 API")
    @Operation(operationId="Instructor", summary = "강사 정보 조회", description = "강사 정보 조회한다.")
    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getList(HttpServletRequest request, HttpServletResponse response, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> instructorService.getList((InstructorViewRequestVO) params(InstructorViewRequestVO.class, searchMap, pageable)))
                        .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
    }

    /**
     * 강사 정보 조회
     *
     * @param request
     * @param response
     * @param pageable
     * @return
     */
    @Tag(name = "Instructor Management", description = "강사 관리 API")
    @Operation(operationId="Instructor", summary = "강사 이력 조회", description = "강사 이력 조회한다.")
    @GetMapping(value = "/career", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getCareer(HttpServletRequest request, HttpServletResponse response, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> instructorService.getCareer((InstructorCareerRequestVO) params(InstructorCareerRequestVO.class, searchMap, pageable)))
                        .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
    }

    /**
     * 강사 정보 생성
     *
     * @param request
     * @param response
     * @param instructorApiRequestVO
     * @return
     */
    @Tag(name = "Instructor Management", description = "강사 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강사 정보 생성 성공", content = @Content(schema = @Schema(implementation = InstructorApiResponseVO.class)))})
    @Operation(operationId="Instructor", summary = "강사 정보 생성", description = "강사 정보 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InstructorApiResponseVO> createInfo(HttpServletRequest request, HttpServletResponse response,
                                                            @Validated(value = {CreateInstructor.class}) @NotNull @RequestBody InstructorApiRequestVO instructorApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(instructorService.createInfo(instructorApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3501, "강사 정보 생성 실패")));
    }

    public interface CreateInstructor {}

    /**
     * 강사 정보 업데이트
     *
     * @param request
     * @param response
     * @param instructorApiRequestVO
     * @return
     */
    @Tag(name = "Instructor Management", description = "강사 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강사 정보 수정 성공", content = @Content(schema = @Schema(implementation = InstructorApiResponseVO.class)))})
    @Operation(operationId="Instructor", summary = "강사 정보 업데이트", description = "강사 정보를 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InstructorApiResponseVO> updateInfo(HttpServletRequest request, HttpServletResponse response,
                                                               @Validated(value = {UpdateInstructor.class}) @NotNull @RequestBody InstructorApiRequestVO instructorApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(instructorService.updateInfo(instructorApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1003, "강사 정보 수정 실패")));
    }

    public interface UpdateInstructor {};

    /**
     * 강사 정보 관련 파일 업로드 API
     *
     * @param request
     * @param response
     * @param instrSerialNo
     * @param pictureFile
     * @param signFile
     * @return
     */
    @Tag(name = "Instructor Management", description = "강사 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강사 정보 관련 파일 업로드 성공", content = @Content(schema = @Schema(implementation = InstructorApiResponseVO.class)))})
    @Operation(operationId = "Instructor", summary = "강사 정보 관련 파일 업로드", description = "강사 정보 관련 파일을 업로드한다.")
    @PutMapping(value = "/upload/{instrSerialNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CSResponseVOSupport> fileUpload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "강사 정보 시리얼 번호") @PathVariable(value="instrSerialNo", required = true) Long instrSerialNo,
                                                          @Parameter(description = "사진파일") @RequestPart(value="pictureFile", required = true) MultipartFile pictureFile,
                                                          @Parameter(description = "서명/도장 파일") @RequestPart(value="signFile", required = true) MultipartFile signFile) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(instructorService.fileUpload(instrSerialNo, pictureFile, signFile))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9005, "강사 정보 관련 파일 업로드 실패")));
    }


    /**
     * 강사 정보 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Instructor Management", description = "강사 관리 API")
    @Operation(operationId="Instructor", summary = "강사 정보 엑셀", description = "강사 정보 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/excel")
    public void getListExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Date date = new Date();
        String dateToStr = DateFormatUtils.format(date, "yyyyMMdd_HHmmSS_");

        List<InstructorExcelVO> instructorExcelVOList = instructorService.getExcel((InstructorCustomViewRequestVO) params(InstructorCustomViewRequestVO.class, CSSearchMap.of(request), null));
        OneSheetExcelFile<InstructorExcelVO> excelFile = new OneSheetExcelFile<>(instructorExcelVOList, InstructorExcelVO.class);
        response.setHeader("Content-Disposition", "attachment; filename=" + dateToStr+URLEncoder.encode("강사정보", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");

        excelFile.write(response.getOutputStream());
    }

    @Tag(name = "journalismschool Lctr Old History", description = "언론인교육센터 강의이력 API")
    @Operation(operationId = "lctrhistory", summary = "언론인교육센터 강의이력 API", description = "강의이력을 조회한다.")
    @GetMapping(value = "/lctrhistory", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getJournaliLctrList(HttpServletRequest request, HttpServletResponse response, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> instructorService.getJournaliLctrList((InstructorLctrApiRequestVO) params(InstructorLctrApiRequestVO.class, searchMap, pageable)))
                        .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
    }
}
