package kr.or.kpf.lms.biz.homepage.popup.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.common.upload.service.FileMasterService;
import kr.or.kpf.lms.biz.homepage.popup.service.PopupService;
import kr.or.kpf.lms.biz.homepage.popup.vo.CreatePopup;
import kr.or.kpf.lms.biz.homepage.popup.vo.UpdatePopup;
import kr.or.kpf.lms.biz.homepage.popup.vo.request.PopupApiRequestVO;
import kr.or.kpf.lms.biz.homepage.popup.vo.response.PopupApiResponseVO;
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
 * 홈페이지 > 팝업 API 관련 Controller
 */
@Tag(name = "Homepage Management", description = "홈페이지 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/homepage/popup")
public class PopupApiController extends CSApiControllerSupport {
    private final FileMasterService fileMasterService;
    private final PopupService popupService;

    /**
     * 팝업 등록 API
     *
     * @param request
     * @param response
     * @param popupApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팝업 생성 성공", content = @Content(schema = @Schema(implementation = PopupApiResponseVO.class)))})
    @Operation(operationId = "Popup", summary = "팝업 생성", description = "팝업 데이터를 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PopupApiResponseVO> createEvent(HttpServletRequest request, HttpServletResponse response,
                                                                  @Validated(value = {CreatePopup.class}) @NotNull @RequestBody PopupApiRequestVO popupApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(popupService.createInfo(popupApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7101, "팝업 데이터 생성 실패")));
    }

    /**
     * 팝업 업데이트 API
     *
     * @param request
     * @param response
     * @param popupApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팝업 업데이트 성공", content = @Content(schema = @Schema(implementation = PopupApiResponseVO.class)))})
    @Operation(operationId = "Popup", summary = "팝업 업데이트", description = "팝업 데이터를 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PopupApiResponseVO> updateEvent(HttpServletRequest request, HttpServletResponse response,
                                                                              @Validated(value = {UpdatePopup.class}) @NotNull @RequestBody PopupApiRequestVO popupApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(popupService.updateInfo(popupApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7102, "팝업 데이터 업데이트 실패")));
    }

    /**
     * 팝업 삭제
     *
     * @param request
     * @param response
     * @param popupApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팝업 삭제 성공", content = @Content(schema = @Schema(implementation = PopupApiResponseVO.class)))})
    @Operation(operationId="Popup", summary = "팝업 삭제", description = "팝업 삭제 한다.")
    @DeleteMapping(value = "/delete/{seq}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteEvent(HttpServletRequest request, HttpServletResponse response,
                                                          @NotNull @RequestBody PopupApiRequestVO popupApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(popupService.deleteInfo(popupApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3544, "팝업 삭제 실패")));
    }

    /**
     * 팝업 첨부파일 업로드 API
     *
     * @param request
     * @param response
     * @param popupSn
     * @param thumbnailFile
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팝업 첨부파일 업로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "Banner", summary = "팝업 첨부파일 업로드", description = "팝업 첨부파일을 업로드한다.")
    @PutMapping(value = "/upload/{popupSn}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CSResponseVOSupport> fileUpload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "팝업 시리얼 번호") @PathVariable(value = "popupSn", required = true) String popupSn,
                                                          @Parameter(description = "첨부파일") @RequestPart(value = "thumbnailFile", required = true) MultipartFile thumbnailFile) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(popupService.fileUpload(popupSn, thumbnailFile))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9005, "팝업 첨부파일 업로드 실패")));
    }

    /**
     * 팝업 첨부파일 다운로드 API
     *
     * @param request
     * @param response
     * @param attachFilePath
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팝업 첨부파일 다운로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "Popup", summary = "팝업 첨부파일 다운로드", description = "팝업 첨부파일을 다운로드한다.")
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
