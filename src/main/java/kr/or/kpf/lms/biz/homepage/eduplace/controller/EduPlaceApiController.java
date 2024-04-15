package kr.or.kpf.lms.biz.homepage.eduplace.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.common.upload.service.FileMasterService;
import kr.or.kpf.lms.biz.homepage.eduplace.service.EduPlaceService;
import kr.or.kpf.lms.biz.homepage.eduplace.vo.CreateEduPlace;
import kr.or.kpf.lms.biz.homepage.eduplace.vo.UpdateEduPlace;
import kr.or.kpf.lms.biz.homepage.eduplace.vo.request.EduPlaceApiRequestVO;
import kr.or.kpf.lms.biz.homepage.eduplace.vo.response.EduPlaceApiResponseVO;
import kr.or.kpf.lms.biz.homepage.event.vo.response.EventApiResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Optional;

/**
 * 홈페이지 > 교육장 장소 신청 API 관련 Controller
 */
@Tag(name = "Homepage Management", description = "홈페이지 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/homepage/edu/place")
public class EduPlaceApiController extends CSApiControllerSupport {
    private final FileMasterService fileMasterService;
    private final EduPlaceService eduPlaceService;

    /**
     * 교육장 장소 신청 등록 API
     *
     * @param request
     * @param response
     * @param eduPlaceApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "교육장 장소 신청 생성 성공", content = @Content(schema = @Schema(implementation = EduPlaceApiResponseVO.class)))})
    @Operation(operationId = "EduPlaceAply", summary = "교육장 장소 신청 생성", description = "교육장 장소 신청 데이터를 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EduPlaceApiResponseVO> createEduPlaceAply(HttpServletRequest request, HttpServletResponse response,
                                                                  @Validated(value = {CreateEduPlace.class}) @NotNull @RequestBody EduPlaceApiRequestVO eduPlaceApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(eduPlaceService.createInfo(eduPlaceApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7101, "교육장 장소 신청 데이터 생성 실패")));
    }

    /**
     * 교육장 장소 신청 업데이트 API
     *
     * @param request
     * @param response
     * @param eduPlaceApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "교육장 장소 신청 업데이트 성공", content = @Content(schema = @Schema(implementation = EventApiResponseVO.class)))})
    @Operation(operationId = "EduPlaceAply", summary = "교육장 장소 신청 업데이트", description = "교육장 장소 신청 데이터를 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EduPlaceApiResponseVO> updateEduPlaceAply(HttpServletRequest request, HttpServletResponse response,
                                                                              @Validated(value = {UpdateEduPlace.class}) @NotNull @RequestBody EduPlaceApiRequestVO eduPlaceApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(eduPlaceService.updateInfo(eduPlaceApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7102, "교육장 장소 신청 데이터 업데이트 실패")));
    }

    /**
     * 교육장 장소 신청 삭제
     *
     * @param request
     * @param response
     * @param eduPlaceApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "교육장 장소 신청 삭제 성공", content = @Content(schema = @Schema(implementation = EduPlaceApiResponseVO.class)))})
    @Operation(operationId="EduPlaceAply", summary = "교육장 장소 신청 삭제", description = "교육장 장소 신청 삭제 한다.")
    @DeleteMapping(value = "/delete/{seq}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteEduPlaceAply(HttpServletRequest request, HttpServletResponse response,
                                                          @NotNull @RequestBody EduPlaceApiRequestVO eduPlaceApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(eduPlaceService.deleteInfo(eduPlaceApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3544, "교육장 장소 신청 삭제 실패")));
    }

    /**
     * 교육장 장소 신청 첨부파일 업로드 API
     *
     * @param request
     * @param response
     * @param serialNo
     * @param attachFile
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "교육장 장소 신청 첨부파일 업로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "EduPlaceAply", summary = "교육장 장소 신청 첨부파일 업로드", description = "교육장 장소 신청 첨부파일을 업로드한다.")
    @PutMapping(value = "/upload/{serialNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CSResponseVOSupport> fileUpload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "교육장 장소 신청 시리얼 번호") @PathVariable(value = "serialNo", required = true) Long serialNo,
                                                          @Parameter(description = "첨부파일") @RequestPart(value = "attachFile", required = true) MultipartFile attachFile) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(eduPlaceService.fileUpload(serialNo, attachFile))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9005, "교육장 장소 신청 첨부파일 업로드 실패")));
    }

    /**
     * 교육장 장소 신청 첨부파일 다운로드 API
     *
     * @param request
     * @param response
     * @param attachFilePath
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "교육장 장소 신청 첨부파일 다운로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "EduPlaceAply", summary = "교육장 장소 신청 첨부파일 다운로드", description = "교육장 장소 신청 첨부파일을 다운로드한다.")
    @GetMapping(value = "/download")
    public ResponseEntity<ByteArrayResource> fileDownload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "첨부파일 명") @RequestParam(value = "attachFilePath", required = true) String attachFilePath) {
        return Optional.ofNullable(attachFilePath).map(filePath -> {

            byte[] data = fileMasterService.fileDownload(attachFilePath);
            ByteArrayResource resource = new ByteArrayResource(data);
            try {
                return ResponseEntity.status(HttpStatus.OK)
                        .contentLength(data.length)
                        .header("Content-type", "application/octet-stream")
                        .header("Content-disposition", "attachment; filename=\"" + URLEncoder.encode(attachFilePath, "UTF-8") + "\"")
                        .body(resource);
            } catch (UnsupportedEncodingException e) {
                throw new KPFException(KPF_RESULT.ERROR9006, "파일명 URL 인코드 실패");
            }
        }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9006, "파일 패스 미존재"));
    }

}
