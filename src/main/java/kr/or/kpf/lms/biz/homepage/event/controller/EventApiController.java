package kr.or.kpf.lms.biz.homepage.event.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.common.upload.service.FileMasterService;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.CreateArchiveClassGuide;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.UpdateArchiveClassGuide;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.request.ArchiveClassGuideApiRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.response.ArchiveClassGuideApiResponseVO;
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
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.Optional;

/**
 * 홈페이지 > 이벤트/설문 API 관련 Controller
 */
@Tag(name = "Homepage Management", description = "홈페이지 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/homepage/event/")
public class EventApiController extends CSApiControllerSupport {
    private final FileMasterService fileMasterService;
    private final EventService eventService;

    /**
     * 이벤트/설문 등록 API
     *
     * @param request
     * @param response
     * @param eventApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이벤트/설문 생성 성공", content = @Content(schema = @Schema(implementation = EventApiResponseVO.class)))})
    @Operation(operationId = "Event", summary = "이벤트/설문 생성", description = "이벤트/설문 데이터를 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventApiResponseVO> createEvent(HttpServletRequest request, HttpServletResponse response,
                                                                  @Validated(value = {CreateEvent.class}) @NotNull @RequestBody EventApiRequestVO eventApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(eventService.createInfo(eventApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7101, "이벤트/설문 데이터 생성 실패")));
    }

    /**
     * 이벤트/설문 업데이트 API
     *
     * @param request
     * @param response
     * @param eventApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이벤트/설문 업데이트 성공", content = @Content(schema = @Schema(implementation = EventApiResponseVO.class)))})
    @Operation(operationId = "Event", summary = "이벤트/설문 업데이트", description = "이벤트/설문 데이터를 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventApiResponseVO> updateEvent(HttpServletRequest request, HttpServletResponse response,
                                                                              @Validated(value = {UpdateEvent.class}) @NotNull @RequestBody EventApiRequestVO eventApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(eventService.updateInfo(eventApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7102, "이벤트/설문 데이터 업데이트 실패")));
    }

    /**
     * 이벤트/설문 삭제
     *
     * @param request
     * @param response
     * @param eventApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이벤트/설문 삭제 성공", content = @Content(schema = @Schema(implementation = EventApiResponseVO.class)))})
    @Operation(operationId="Event", summary = "이벤트/설문 삭제", description = "이벤트/설문 삭제 한다.")
    @DeleteMapping(value = "/delete/{seq}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteEvent(HttpServletRequest request, HttpServletResponse response,
                                                          @NotNull @RequestBody EventApiRequestVO eventApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(eventService.deleteInfo(eventApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3544, "이벤트/설문 삭제 실패")));
    }

    /**
     * 이벤트/설문 첨부파일 업로드 API
     *
     * @param request
     * @param response
     * @param serialNo
     * @param attachFile
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이벤트/설문 첨부파일 업로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "Event", summary = "이벤트/설문 첨부파일 업로드", description = "이벤트/설문 첨부파일을 업로드한다.")
    @PutMapping(value = "/upload/{serialNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CSResponseVOSupport> fileUpload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "이벤트/설문 시리얼 번호") @PathVariable(value = "serialNo", required = true) Long serialNo,
                                                          @Parameter(description = "첨부파일") @RequestPart(value = "attachFile", required = true) MultipartFile attachFile) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(eventService.fileUpload(serialNo, attachFile))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9005, "이벤트/설문 첨부파일 업로드 실패")));
    }
}
