package kr.or.kpf.lms.biz.homepage.archive.data.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.common.upload.service.FileMasterService;
import kr.or.kpf.lms.biz.homepage.archive.data.service.ArchiveService;
import kr.or.kpf.lms.biz.homepage.archive.data.vo.CreateArchive;
import kr.or.kpf.lms.biz.homepage.archive.data.vo.UpdateArchive;
import kr.or.kpf.lms.biz.homepage.archive.data.vo.request.ArchiveApiRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.data.vo.request.PublicationFileManagementApiRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.data.vo.request.PublicationFileManagementViewRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.data.vo.response.ArchiveApiResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import kr.or.kpf.lms.repository.entity.homepage.LmsDataFile;
import kr.or.kpf.lms.repository.homepage.LmsDataFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 홈페이지 > 자료실 API 관련 Controller
 */
@Tag(name = "Homepage Management", description = "홈페이지 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/homepage/archive")
public class ArchiveApiController extends CSApiControllerSupport {
    private final FileMasterService fileMasterService;
    private final ArchiveService archiveService;
    private final LmsDataFileRepository lmsDataFileRepository;

    /**
     * 자료실 등록 API
     *
     * @param request
     * @param response
     * @param archiveApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "자료실 생성 성공", content = @Content(schema = @Schema(implementation = ArchiveApiResponseVO.class)))})
    @Operation(operationId = "LmsData", summary = "자료실 생성", description = "자료실 데이터를 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArchiveApiResponseVO> createLmsData(HttpServletRequest request, HttpServletResponse response,
                                                              @Validated(value = {CreateArchive.class}) @NotNull @RequestBody ArchiveApiRequestVO archiveApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(archiveService.createInfo(archiveApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7101, "자료실 데이터 생성 실패")));
    }

    /**
     * 자료실 업데이트 API
     *
     * @param request
     * @param response
     * @param archiveApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "자료실 업데이트 성공", content = @Content(schema = @Schema(implementation = ArchiveApiResponseVO.class)))})
    @Operation(operationId = "LmsData", summary = "자료실 업데이트", description = "자료실 데이터를 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArchiveApiResponseVO> updateLmsData(HttpServletRequest request, HttpServletResponse response,
                                                              @Validated(value = {UpdateArchive.class}) @NotNull @RequestBody ArchiveApiRequestVO archiveApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(archiveService.updateInfo(archiveApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7102, "자료실 데이터 업데이트 실패")));
    }

    /**
     * 자료실 삭제
     *
     * @param request
     * @param response
     * @param archiveApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "자료실 삭제 성공", content = @Content(schema = @Schema(implementation = ArchiveApiResponseVO.class)))})
    @Operation(operationId="LmsData", summary = "자료실 삭제", description = "자료실 삭제 한다.")
    @DeleteMapping(value = "/delete/{seq}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteEvent(HttpServletRequest request, HttpServletResponse response,
                                                          @NotNull @RequestBody ArchiveApiRequestVO archiveApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(archiveService.deleteInfo(archiveApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3544, "자료실 삭제 실패")));
    }

    /**
     * 자료실 썸네일 업로드 API
     *
     * @param request
     * @param response
     * @param sequenceNo
     * @param attachFile
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "자료실 썸네일 업로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "LmsData", summary = "자료실 썸네일 업로드", description = "자료실 썸네일을 업로드한다.")
    @PutMapping(value = "/thumbnail/{sequenceNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CSResponseVOSupport> thumbnailUpload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "자료실 시리얼 번호") @PathVariable(value = "sequenceNo", required = true) Long sequenceNo,
                                                          @Parameter(description = "첨부파일") @RequestPart(value = "attachFile", required = true) MultipartFile attachFile) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(archiveService.thumbnailUpload(sequenceNo, attachFile))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9005, "자료실 첨부파일 업로드 실패")));
    }

    /**
     * 자료실 첨부파일 업로드 API
     *
     * @param request
     * @param response
     * @param sequenceNo
     * @param attachFiles
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "자료실 첨부파일 업로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "LmsData", summary = "자료실 첨부파일 업로드", description = "자료실 첨부파일을 업로드한다.")
    @PutMapping(value = "/upload/{sequenceNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CSResponseVOSupport> fileUpload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "자료실 시리얼 번호") @PathVariable(value = "sequenceNo", required = true) Long sequenceNo,
                                                          @Parameter(description = "첨부파일") @RequestPart(value = "attachFiles", required = true) List<MultipartFile> attachFiles) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(archiveService.fileUpload(sequenceNo, attachFiles))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9005, "자료실 첨부파일 업로드 실패")));
    }

    /**
     * 자료실 첨부파일 다운로드 API
     *
     * @param request
     * @param response
     * @param attachFilePath
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "자료실 첨부파일 다운로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "LmsData", summary = "자료실 첨부파일 다운로드", description = "자료실 첨부파일을 다운로드한다.")
    @GetMapping(value = "/download")
    public ResponseEntity<ByteArrayResource> fileDownload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "첨부파일 명") @RequestParam(value = "attachFilePath", required = true) String attachFilePath) {
        return Optional.ofNullable(attachFilePath).map(filePath -> {
            byte[] data = fileMasterService.fileDownload(attachFilePath);
            ByteArrayResource resource = new ByteArrayResource(data);

            try {
                LmsDataFile lmsDataFile = lmsDataFileRepository.findOne(Example.of(LmsDataFile.builder()
                        .filePath(attachFilePath)
                        .build())).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9006, "파일 미존재"));
                String originName = attachFilePath;
                if (lmsDataFile != null) {
                    if (lmsDataFile.getOriginalFileName() != null)
                        originName = lmsDataFile.getOriginalFileName();
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
     * 파일 삭제
     *
     * @param request
     * @param response
     * @param fileSn
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "파일 삭제 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId="LmsData", summary = "파일 삭제", description = "파일 삭제 한다.")
    @DeleteMapping(value = "/upload/delete/{fileSn}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteFile(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "삭제할 파일 번호") @PathVariable(value = "fileSn", required = true) Long fileSn) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(archiveService.deleteFile(fileSn))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9007, "자료실 파일 삭제 실패")));
    }

    /**
     * 출간물 폴더 관리
     *
     * @param request
     * @param response
     * @param sequenceNo
     * @param folderList
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "출간물 폴더 변경 성공", content = @Content(schema = @Schema(implementation = ArchiveApiResponseVO.class)))})
    @Operation(operationId = "LmsData", summary = "출간물 폴더 관리", description = "출간물 폴더 추가/이동/삭제를 관리한다.")
    @PutMapping(value = "/folder-management/{sequenceNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CSResponseVOSupport> folderManagement(HttpServletRequest request, HttpServletResponse response,
                                                                @Parameter(description = "출간물 코드") @PathVariable(value = "sequenceNo", required = true) Long sequenceNo,
                                                                @NotNull @RequestBody List<PublicationFileManagementApiRequestVO> folderList) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(archiveService.folderManagement(sequenceNo, folderList))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2005, "폴더 업데이트 실패")));
    }

    /**
     * 출간물 파일 업로드 API
     *
     * @param request
     * @param response
     * @param sequenceNo
     * @param publicationFileList
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "출간물 파일 업로드 성공", content = @Content(schema = @Schema(implementation = ArchiveApiResponseVO.class)))})
    @Operation(operationId = "LmsData", summary = "출간물 파일 업로드", description = "출간물 파일을 업로드한다.")
    @PutMapping(value = "/folder-management/file/{sequenceNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CSResponseVOSupport> fileUpload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "출간물 코드") @PathVariable(value = "sequenceNo", required = true) Long sequenceNo,
                                                          @Parameter(description = "출간물 파일 경로") @RequestParam(value = "filePath", required = true) String filePath,
                                                          @Parameter(description = "출간물 파일") @RequestPart(value = "publicationFileList", required = true) MultipartFile[] publicationFileList) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(archiveService.fileListUpload(sequenceNo, filePath, publicationFileList))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9005, "출간물 파일 업로드 실패")));
    }

    /**
     * 출간물 압축 파일 업로드 API
     *
     * @param request
     * @param response
     * @param sequenceNo
     * @param zipFileList
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "출간물 압축 파일 업로드 성공", content = @Content(schema = @Schema(implementation = ArchiveApiResponseVO.class)))})
    @Operation(operationId = "LmsData", summary = "출간물 압축 파일 업로드", description = "출간물 압축 파일을 업로드한다.")
    @PutMapping(value = "/folder-management/zip-file/{sequenceNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CSResponseVOSupport> zipFileUpload(HttpServletRequest request, HttpServletResponse response,
                                                             @Parameter(description = "출간물 코드") @PathVariable(value = "sequenceNo", required = true) Long sequenceNo,
                                                             @Parameter(description = "출간물 파일 경로") @RequestParam(value = "filePath", required = true) String filePath,
                                                             @Parameter(description = "압축 파일") @RequestPart(value = "zipFileList", required = true) MultipartFile[] zipFileList) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(archiveService.zipFileUpload(sequenceNo, filePath, zipFileList))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9005, "출간물 압축 파일 업로드 실패")));
    }

    /**
     * 출간물 폴더 관련 파일리스트 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "파일 조회 성공", content = @Content(schema = @Schema(implementation = ArchiveApiResponseVO.class)))})
    @Operation(operationId = "LmsData", summary = "출간물 파일 조회", description = "출간물 파일 조회한다.")
    @GetMapping(path = {"/folder-management/file-list"})
    public ResponseEntity<Object> getFileList(HttpServletRequest request, Pageable pageable, Model model){
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> archiveService.getFileList((PublicationFileManagementViewRequestVO) params(PublicationFileManagementViewRequestVO.class, searchMap, pageable)))
                        .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
    }

}
