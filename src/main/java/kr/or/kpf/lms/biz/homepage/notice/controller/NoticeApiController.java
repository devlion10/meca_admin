package kr.or.kpf.lms.biz.homepage.notice.controller;

import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.business.pbanc.master.vo.request.BizPbancApiRequestVO;
import kr.or.kpf.lms.biz.business.pbanc.master.vo.response.BizPbancApiResponseVO;
import kr.or.kpf.lms.biz.homepage.notice.service.NoticeService;
import kr.or.kpf.lms.biz.homepage.notice.vo.request.NoticeApiRequestVO;
import kr.or.kpf.lms.biz.homepage.notice.vo.request.NoticeViewRequestVO;
import kr.or.kpf.lms.biz.homepage.notice.vo.response.NoticeApiResponseVO;
import kr.or.kpf.lms.biz.homepage.notice.vo.response.NoticeViewResponseVO;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Optional;

/**
 * 홈페이지 관리 > 공지사항 API 관련 Controller
 */
@Tag(name = "Homepage Management", description = "홈페이지 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/homepage/notice")
public class NoticeApiController extends CSApiControllerSupport {

    private final NoticeService noticeService;

    /**
     * Sample (객체 Swagger 표출을 위해서 사용)
     *
     * @param request
     * @param response
     * @param noticeViewRequestVO
     * @return
     */
    @Tag(name = "Swagger USE(NOT API)", description = "사용하지 않는 API(객체 표출용)")
    @PostMapping(value = "/sample", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NoticeViewResponseVO> swaggerUse1(HttpServletRequest request, HttpServletResponse response, @RequestBody NoticeViewRequestVO noticeViewRequestVO) {
        return null;
    }

    /**
     * 공지사항 등록 API
     *
     * @param request
     * @param response
     * @param noticeApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공지사항 생성 성공", content = @Content(schema = @Schema(implementation = NoticeApiResponseVO.class)))})
    @Operation(operationId = "Notice", summary = "공지사항 생성", description = "공지사항 데이터를 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NoticeApiResponseVO> createNotice(HttpServletRequest request, HttpServletResponse response,
                                                          @Validated(value = {CreateNotice.class}) @NotNull @RequestBody NoticeApiRequestVO noticeApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(noticeService.createNotice(noticeApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7043, "공지사항 데이터 생성 실패")));
    }

    public interface CreateNotice {}

    /**
     * 공지사항 업데이트 API
     *
     * @param request
     * @param response
     * @param noticeApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공지사항 업데이트 성공", content = @Content(schema = @Schema(implementation = NoticeApiResponseVO.class)))})
    @Operation(operationId = "Notice", summary = "공지사항 업데이트", description = "공지사항 데이터를 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NoticeApiResponseVO> updateNotice(HttpServletRequest request, HttpServletResponse response,
                                                            @Validated(value = {UpdateNotice.class}) @NotNull @RequestBody NoticeApiRequestVO noticeApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(noticeService.updateNotice(noticeApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7042, "공지사항 데이터 업데이트 실패")));
    }

    public interface UpdateNotice {}

    /**
     * 공지사항 첨부파일 업로드 API
     *
     * @param request
     * @param response
     * @param noticeSerialNo
     * @param attachFile
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공지사항 첨부파일 업로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "Notice", summary = "공지사항 첨부파일 업로드", description = "공지사항 첨부파일을 업로드한다.")
    @PutMapping(value = "/upload/{noticeSerialNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CSResponseVOSupport> fileUpload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "공지사항 시리얼 번호") @PathVariable(value = "noticeSerialNo", required = true) String noticeSerialNo,
                                                          @Parameter(description = "첨부파일") @RequestPart(value = "attachFile", required = true) MultipartFile attachFile) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(noticeService.fileUpload(noticeSerialNo, attachFile))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9005, "공지사항 첨부파일 업로드 실패")));
    }

    /**
     * 공지사항 삭제
     *
     * @param request
     * @param response
     * @param noticeApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "공지사항 삭제 성공", content = @Content(schema = @Schema(implementation = BizPbancApiResponseVO.class)))})
    @Operation(operationId="Notice", summary = "공지사항 삭제", description = "공지사항 삭제 한다.")
    @DeleteMapping(value = "/delete/{noticeSerialNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteInfo(HttpServletRequest request, HttpServletResponse response,
                                                          @NotNull @RequestBody NoticeApiRequestVO noticeApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(noticeService.deleteInfo(noticeApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3504, "공지사항 삭제 실패")));
    }
}
