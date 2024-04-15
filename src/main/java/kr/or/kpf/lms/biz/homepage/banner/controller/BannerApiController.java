package kr.or.kpf.lms.biz.homepage.banner.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.common.upload.service.FileMasterService;
import kr.or.kpf.lms.biz.homepage.banner.service.BannerService;
import kr.or.kpf.lms.biz.homepage.banner.vo.CreateBanner;
import kr.or.kpf.lms.biz.homepage.banner.vo.UpdateBanner;
import kr.or.kpf.lms.biz.homepage.banner.vo.request.BannerApiRequestVO;
import kr.or.kpf.lms.biz.homepage.banner.vo.response.BannerApiResponseVO;
import kr.or.kpf.lms.biz.homepage.event.service.EventService;
import kr.or.kpf.lms.biz.homepage.event.vo.CreateEvent;
import kr.or.kpf.lms.biz.homepage.event.vo.UpdateEvent;
import kr.or.kpf.lms.biz.homepage.event.vo.request.EventApiRequestVO;
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
 * 홈페이지 > 배너 API 관련 Controller
 */
@Tag(name = "Homepage Management", description = "홈페이지 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/homepage/banner")
public class BannerApiController extends CSApiControllerSupport {
    private final FileMasterService fileMasterService;
    private final BannerService bannerService;

    /**
     * 배너 등록 API
     *
     * @param request
     * @param response
     * @param bannerApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "배너 생성 성공", content = @Content(schema = @Schema(implementation = BannerApiResponseVO.class)))})
    @Operation(operationId = "Banner", summary = "배너 생성", description = "배너 데이터를 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BannerApiResponseVO> createEvent(HttpServletRequest request, HttpServletResponse response,
                                                                  @Validated(value = {CreateBanner.class}) @NotNull @RequestBody BannerApiRequestVO bannerApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bannerService.createInfo(bannerApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7101, "배너 데이터 생성 실패")));
    }

    /**
     * 배너 업데이트 API
     *
     * @param request
     * @param response
     * @param bannerApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "배너 업데이트 성공", content = @Content(schema = @Schema(implementation = BannerApiResponseVO.class)))})
    @Operation(operationId = "Banner", summary = "배너 업데이트", description = "배너 데이터를 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BannerApiResponseVO> updateEvent(HttpServletRequest request, HttpServletResponse response,
                                                                              @Validated(value = {UpdateBanner.class}) @NotNull @RequestBody BannerApiRequestVO bannerApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bannerService.updateInfo(bannerApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7102, "배너 데이터 업데이트 실패")));
    }

    /**
     * 배너 삭제
     *
     * @param request
     * @param response
     * @param bannerApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "배너 삭제 성공", content = @Content(schema = @Schema(implementation = BannerApiResponseVO.class)))})
    @Operation(operationId="Banner", summary = "배너 삭제", description = "배너 삭제 한다.")
    @DeleteMapping(value = "/delete/{seq}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteEvent(HttpServletRequest request, HttpServletResponse response,
                                                          @NotNull @RequestBody BannerApiRequestVO bannerApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bannerService.deleteInfo(bannerApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3544, "배너 삭제 실패")));
    }

    /**
     * 배너 첨부파일 업로드 API
     *
     * @param request
     * @param response
     * @param bannerSn
     * @param thumbnailFile
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "배너 첨부파일 업로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "Banner", summary = "배너 첨부파일 업로드", description = "배너 첨부파일을 업로드한다.")
    @PutMapping(value = "/upload/{bannerSn}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CSResponseVOSupport> fileUpload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "배너 시리얼 번호") @PathVariable(value = "bannerSn", required = true) String bannerSn,
                                                          @Parameter(description = "첨부파일") @RequestPart(value = "thumbnailFile", required = true) MultipartFile thumbnailFile) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bannerService.fileUpload(bannerSn, thumbnailFile))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9005, "배너 첨부파일 업로드 실패")));
    }

    /**
     * 배너 첨부파일 다운로드 API
     *
     * @param request
     * @param response
     * @param attachFilePath
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "배너 첨부파일 다운로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "Banner", summary = "배너 첨부파일 다운로드", description = "배너 첨부파일을 다운로드한다.")
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
