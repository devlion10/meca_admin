package kr.or.kpf.lms.biz.homepage.archive.guide.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.business.organization.aply.vo.request.BizOrganizationAplyApiRequestVO;
import kr.or.kpf.lms.biz.business.organization.aply.vo.request.BizOrganizationAplyViewRequestVO;
import kr.or.kpf.lms.biz.business.organization.aply.vo.response.BizOrganizationAplyApiResponseVO;
import kr.or.kpf.lms.biz.business.organization.aply.vo.response.BizOrganizationAplyExcelVO;
import kr.or.kpf.lms.biz.common.upload.service.FileMasterService;
import kr.or.kpf.lms.biz.common.upload.vo.request.FileMasterApiRequestVO;
import kr.or.kpf.lms.biz.common.upload.vo.response.FileMasterApiResponseVO;
import kr.or.kpf.lms.biz.homepage.archive.guide.service.ArchiveClassGuideService;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.CreateArchiveClassGuide;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.UpdateArchiveClassGuide;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.request.ArchiveClassGuideApiRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.request.ArchiveClassGuideExcelRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.request.ArchiveClassGuideViewRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.response.ArchiveClassGuideApiResponseVO;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.response.ArchiveClassGuideExcelResponseVO;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.response.ArchiveClassGuideViewResponseVO;
import kr.or.kpf.lms.common.exceldownload.excel.OneSheetExcelFile;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import kr.or.kpf.lms.repository.entity.FileMaster;
import kr.or.kpf.lms.repository.entity.education.ExamQuestionItem;
import kr.or.kpf.lms.repository.entity.education.ExamQuestionMaster;
import kr.or.kpf.lms.repository.entity.homepage.ClassGuideFile;
import kr.or.kpf.lms.repository.homepage.ClassGuideFileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 홈페이지 > 수업지도안 API 관련 Controller
 */
@Tag(name = "Homepage Management", description = "홈페이지 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/homepage/archive/class-guide")
public class ArchiveClassGuideApiController extends CSApiControllerSupport {
    private final FileMasterService fileMasterService;
    private final ArchiveClassGuideService archiveClassGuideService;
    private final ClassGuideFileRepository classGuideFileRepository;

    /**
     * Sample (객체 Swagger 표출을 위해서 사용)
     *
     * @param request
     * @param response
     * @param reviewViewRequestVO
     * @return
     */
    @Tag(name = "Swagger USE(NOT API)", description = "사용하지 않는 API(객체 표출용)")
    @PostMapping(value = "/sample", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArchiveClassGuideViewResponseVO> swaggerUse1(HttpServletRequest request, HttpServletResponse response, @RequestBody ArchiveClassGuideViewRequestVO reviewViewRequestVO) {
        return null;
    }

    /**
     * 수업지도안 조회 API
     *
     * @param request
     * @param response
     * @param pageable
     * @param classGuideCode
     * @param containTextType
     * @param containText
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @Operation(operationId = "EducationPlan", summary = "수업지도안 조회", description = "수업지도안 조회한다.")
    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getList(HttpServletRequest request, HttpServletResponse response, Pageable pageable,
                                          @RequestParam(value="classGuideCode", required = false) String classGuideCode,
                                          @RequestParam(value="containTextType", required = false) String containTextType,
                                          @RequestParam(value="containText", required = false) String containText) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> archiveClassGuideService.getList((ArchiveClassGuideViewRequestVO) params(ArchiveClassGuideViewRequestVO.class, searchMap, pageable)))
                        .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
    }

    /**
     * 수업지도안 등록 API
     *
     * @param request
     * @param response
     * @param archiveClassGuideApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수업지도안 생성 성공", content = @Content(schema = @Schema(implementation = ArchiveClassGuideApiResponseVO.class)))})
    @Operation(operationId = "EducationPlan", summary = "수업지도안 생성", description = "수업지도안 데이터를 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArchiveClassGuideApiResponseVO> createEducationPlan(HttpServletRequest request, HttpServletResponse response,
                                                            @Validated(value = {CreateArchiveClassGuide.class}) @NotNull @RequestBody ArchiveClassGuideApiRequestVO archiveClassGuideApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(archiveClassGuideService.createInfo(archiveClassGuideApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7101, "수업지도안 데이터 생성 실패")));
    }

    /**
     * 수업지도안 업데이트 API
     *
     * @param request
     * @param response
     * @param archiveClassGuideApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수업지도안 업데이트 성공", content = @Content(schema = @Schema(implementation = ArchiveClassGuideApiResponseVO.class)))})
    @Operation(operationId = "EducationPlan", summary = "수업지도안 업데이트", description = "수업지도안 데이터를 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArchiveClassGuideApiResponseVO> updateEducationPlan(HttpServletRequest request, HttpServletResponse response,
                                                            @Validated(value = {UpdateArchiveClassGuide.class}) @NotNull @RequestBody ArchiveClassGuideApiRequestVO archiveClassGuideApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(archiveClassGuideService.updateInfo(archiveClassGuideApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7102, "수업지도안 데이터 업데이트 실패")));
    }

    /**
     * 수업지도안 삭제
     *
     * @param request
     * @param response
     * @param archiveClassGuideApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수업지도안 삭제 성공", content = @Content(schema = @Schema(implementation = ArchiveClassGuideApiResponseVO.class)))})
    @Operation(operationId="EducationPlan", summary = "수업지도안 삭제", description = "수업지도안 삭제 한다.")
    @DeleteMapping(value = "/delete/{classGuideCode}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteInfo(HttpServletRequest request, HttpServletResponse response,
                                                          @NotNull @RequestBody ArchiveClassGuideApiRequestVO archiveClassGuideApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(archiveClassGuideService.deleteInfo(archiveClassGuideApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3544, "수업지도안 삭제 실패")));
    }

    /**
     * 수업지도안 첨부파일 업로드 API
     *
     * @param request
     * @param response
     * @param classGuideCode
     * @param attachFile
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수업지도안 첨부파일 업로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "EducationPlan", summary = "수업지도안 첨부파일 업로드", description = "수업지도안 첨부파일을 업로드한다.")
    @PutMapping(value = "/upload/{classGuideCode}/{fileType}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CSResponseVOSupport> fileUpload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "수업지도안 시리얼 번호") @PathVariable(value = "classGuideCode", required = true) String classGuideCode,
                                                          @Parameter(description = "첨부파일") @RequestPart(value = "attachFile", required = true) MultipartFile attachFile,
                                                          @Parameter(description = "수업지도안 파일 유형(1:수업지도안/길라잡이, 2:활동지, 3:예시답안, 4:10분 NIE)") @PathVariable(value = "fileType", required = true) String fileType) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(archiveClassGuideService.fileUpload(classGuideCode, attachFile, fileType))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9005, "수업지도안 첨부파일 업로드 실패")));
    }

    /**
     * 수업지도안 첨부파일 다중 업로드 API
     *
     * @param request
     * @param response
     * @param classGuideCode
     * @param attachFiles
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수업지도안 첨부파일 업로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "EducationPlan", summary = "수업지도안 첨부파일 업로드", description = "수업지도안 첨부파일을 업로드한다.")
    @PutMapping(value = "/uploads/{classGuideCode}/{fileType}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CSResponseVOSupport> fileUploads(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "수업지도안 시리얼 번호") @PathVariable(value = "classGuideCode", required = true) String classGuideCode,
                                                          @Parameter(description = "첨부파일") @RequestPart(value = "attachFile", required = true) List<MultipartFile> attachFiles,
                                                          @Parameter(description = "수업지도안 파일 유형 (TEACH: 수업지도안/길라잡이, ACTIVITY: 활동지, ANSWER: 예시답안, NIE: 10분 NIE)") @PathVariable(value = "fileType", required = true) String fileType) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(archiveClassGuideService.multifileUpload(classGuideCode, attachFiles, fileType))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9005, "수업지도안 첨부파일 업로드 실패")));
    }

    /**
     * 수업지도안 첨부파일 다운로드 API
     *
     * @param request
     * @param response
     * @param attachFilePath
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수업지도안 첨부파일 다운로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "EducationPlan", summary = "수업지도안 첨부파일 다운로드", description = "수업지도안 첨부파일을 다운로드한다.")
    @GetMapping(value = "/download")
    public ResponseEntity<ByteArrayResource> fileDownload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "첨부파일 명") @RequestParam(value = "attachFilePath", required = true) String attachFilePath) {
        return Optional.ofNullable(attachFilePath).map(filePath -> {

            byte[] data = fileMasterService.fileDownload(attachFilePath);
            ByteArrayResource resource = new ByteArrayResource(data);
            try {
                ClassGuideFile classGuideFile = classGuideFileRepository.findOne(Example.of(ClassGuideFile.builder()
                        .filePath(attachFilePath)
                        .build())).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9006, "파일 미존재"));
                String originName = attachFilePath;
                if (classGuideFile != null) {
                    if (classGuideFile.getOriginalFileName() != null)
                        originName = classGuideFile.getOriginalFileName();
                }

                return ResponseEntity.status(HttpStatus.OK)
                        .contentLength(data.length)
                        .header("Content-type", "application/octet-stream")
                        .header("Content-disposition", "attachment; filename=\"" + URLEncoder.encode(originName, "UTF-8") + "\"")
                        .body(resource);
            } catch (UnsupportedEncodingException e) {
                throw new KPFException(KPF_RESULT.ERROR9006, "파일명 URL 인코드 실패");
            }
        }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9006, "파일 패스 미존재"));
    }

    /**
     * 수업지도안 파일 삭제
     *
     * @param request
     * @param response
     * @param sequenceNo
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수업지도안 파일 삭제 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId="EducationPlan", summary = "수업지도안 파일 삭제", description = "수업지도안 파일 삭제 한다.")
    @DeleteMapping(value = "/upload/delete/{sequenceNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteFile(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "삭제할 파일 번호") @PathVariable(value = "sequenceNo", required = true) BigInteger sequenceNo) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(archiveClassGuideService.deleteFile(sequenceNo))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9007, "수업지도안 파일 삭제 실패")));
    }

    /**
     * 수업지도안 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @Operation(operationId = "EducationPlan", summary = "수업지도안 엑셀", description = "수업지도안 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/excel")
    public void getListExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Date date = new Date();
        String dateToStr = DateFormatUtils.format(date, "yyyyMMdd_HHmmSS_");

        List<ArchiveClassGuideExcelResponseVO> list = archiveClassGuideService.getExcel((ArchiveClassGuideExcelRequestVO) params(ArchiveClassGuideExcelRequestVO.class, CSSearchMap.of(request), null));
        OneSheetExcelFile<ArchiveClassGuideExcelResponseVO> excelFile = new OneSheetExcelFile<>(list, ArchiveClassGuideExcelResponseVO.class);
        response.setHeader("Content-Disposition", "attachment; filename=" + dateToStr+URLEncoder.encode("수업지도안", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");
        excelFile.write(response.getOutputStream());
    }
}
