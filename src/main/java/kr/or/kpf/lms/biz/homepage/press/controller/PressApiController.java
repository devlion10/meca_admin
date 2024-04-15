package kr.or.kpf.lms.biz.homepage.press.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.common.upload.service.FileMasterService;
import kr.or.kpf.lms.biz.homepage.press.service.PressService;
import kr.or.kpf.lms.biz.homepage.press.vo.CreatePress;
import kr.or.kpf.lms.biz.homepage.press.vo.UpdatePress;
import kr.or.kpf.lms.biz.homepage.press.vo.request.PressApiRequestVO;
import kr.or.kpf.lms.biz.homepage.press.vo.response.PressApiResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.repository.entity.homepage.LmsDataFile;
import kr.or.kpf.lms.repository.entity.homepage.PressRelease;
import kr.or.kpf.lms.repository.homepage.PressReleaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Example;
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
 * 홈페이지 > 행사소개 API 관련 Controller
 */
@Tag(name = "Homepage Management", description = "홈페이지 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/homepage/press/")
public class PressApiController extends CSApiControllerSupport {
    private final FileMasterService fileMasterService;
    private final PressService pressService;
    private final PressReleaseRepository pressReleaseRepository;

    /**
     * 행사소개 등록 API
     *
     * @param request
     * @param response
     * @param pressApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "행사소개 생성 성공", content = @Content(schema = @Schema(implementation = PressApiResponseVO.class)))})
    @Operation(operationId = "Press", summary = "행사소개 생성", description = "행사소개 데이터를 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PressApiResponseVO> createPress(HttpServletRequest request, HttpServletResponse response,
                                                                  @Validated(value = {CreatePress.class}) @NotNull @RequestBody PressApiRequestVO pressApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(pressService.createInfo(pressApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7101, "행사소개 데이터 생성 실패")));
    }

    /**
     * 행사소개 업데이트 API
     *
     * @param request
     * @param response
     * @param pressApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "행사소개 업데이트 성공", content = @Content(schema = @Schema(implementation = PressApiResponseVO.class)))})
    @Operation(operationId = "Press", summary = "행사소개 업데이트", description = "행사소개 데이터를 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PressApiResponseVO> updatePress(HttpServletRequest request, HttpServletResponse response,
                                                                              @Validated(value = {UpdatePress.class}) @NotNull @RequestBody PressApiRequestVO pressApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(pressService.updateInfo(pressApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7102, "행사소개 데이터 업데이트 실패")));
    }

    /**
     * 이벤트/설문 삭제
     *
     * @param request
     * @param response
     * @param pressApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "행사소개 삭제 성공", content = @Content(schema = @Schema(implementation = PressApiResponseVO.class)))})
    @Operation(operationId="Press", summary = "행사소개 삭제", description = "행사소개 삭제 한다.")
    @DeleteMapping(value = "/delete/{seq}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deletePress(HttpServletRequest request, HttpServletResponse response,
                                                          @NotNull @RequestBody PressApiRequestVO pressApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(pressService.deleteInfo(pressApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3544, "행사소개 삭제 실패")));
    }

    /**
     * 행사소개 첨부파일 업로드 API
     *
     * @param request
     * @param response
     * @param sequenceNo
     * @param atchFile
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "행사소개 첨부파일 업로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "Press", summary = "행사소개 첨부파일 업로드", description = "행사소개 첨부파일을 업로드한다.")
    @PutMapping(value = "/upload/{sequenceNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CSResponseVOSupport> fileUpload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "행사소개 시리얼 번호") @PathVariable(value = "sequenceNo", required = true) Long sequenceNo,
                                                          @Parameter(description = "첨부파일") @RequestPart(value = "atchFile") MultipartFile atchFile) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(pressService.fileUpload(sequenceNo, atchFile))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9005, "행사소개 첨부파일 업로드 실패")));
    }

    /**
     * 행사소개 첨부파일 다운로드 API
     *
     * @param request
     * @param response
     * @param attachFilePath
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "행사소개 첨부파일 다운로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "Press", summary = "행사소개 첨부파일 다운로드", description = "행사소개 첨부파일을 다운로드한다.")
    @GetMapping(value = "/download")
    public ResponseEntity<ByteArrayResource> fileDownload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "첨부파일 명") @RequestParam(value = "attachFilePath", required = true) String attachFilePath) {
        return Optional.ofNullable(attachFilePath).map(filePath -> {

            byte[] data = fileMasterService.fileDownload(attachFilePath);
            ByteArrayResource resource = new ByteArrayResource(data);
            try {
                PressRelease pressRelease = pressReleaseRepository.findOne(Example.of(PressRelease.builder()
                        .atchFilePath(attachFilePath)
                        .build())).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9006, "파일 미존재"));
                String originName = attachFilePath;
                if (pressRelease != null) {
                    if (pressRelease.getAtchFileOrigin() != null)
                        originName = pressRelease.getAtchFileOrigin();
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

}
