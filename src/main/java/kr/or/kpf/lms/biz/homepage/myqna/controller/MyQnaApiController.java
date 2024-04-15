package kr.or.kpf.lms.biz.homepage.myqna.controller;

import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.common.upload.service.FileMasterService;
import kr.or.kpf.lms.biz.homepage.myqna.service.MyQnaService;
import kr.or.kpf.lms.biz.homepage.myqna.vo.request.MyQnaApiRequestVO;
import kr.or.kpf.lms.biz.homepage.myqna.vo.request.MyQnaViewRequestVO;
import kr.or.kpf.lms.biz.homepage.myqna.vo.response.MyQnaApiResponseVO;
import kr.or.kpf.lms.biz.homepage.myqna.vo.response.MyQnaViewResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.repository.entity.homepage.LmsDataFile;
import kr.or.kpf.lms.repository.entity.homepage.MyQna;
import kr.or.kpf.lms.repository.homepage.MyQnaRepository;
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
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.Optional;

/**
 * 홈페이지 관리 > 1:1 문의 API 관련 Controller
 */
@Tag(name = "Homepage Management", description = "홈페이지 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/homepage/my-qna")
public class MyQnaApiController extends CSApiControllerSupport {
    private final FileMasterService fileMasterService;
    private final MyQnaService myQnaService;
    private final MyQnaRepository myQnaRepository;

    /**
     * Sample (객체 Swagger 표출을 위해서 사용)
     *
     * @param request
     * @param response
     * @param myQnaViewRequestVO
     * @return
     */
    @Tag(name = "Swagger USE(NOT API)", description = "사용하지 않는 API(객체 표출용)")
    @PostMapping(value = "/sample", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MyQnaViewResponseVO> swaggerUse1(HttpServletRequest request, HttpServletResponse response, @RequestBody MyQnaViewRequestVO myQnaViewRequestVO) {
        return null;
    }

    /**
     * 1:1 문의 업데이트 API
     *
     * @param request
     * @param response
     * @param myQnaApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "1:1 문의 업데이트 성공", content = @Content(schema = @Schema(implementation = MyQnaApiResponseVO.class)))})
    @Operation(operationId = "MyQna", summary = "1:1 문의 업데이트", description = "1:1 문의 데이터를 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MyQnaApiResponseVO> updateMyQna(HttpServletRequest request, HttpServletResponse response,
                                                          @Validated(value = {UpdateMyQna.class}) @NotNull @RequestBody MyQnaApiRequestVO myQnaApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(myQnaService.updateMyQna(myQnaApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7023, "1:1 문의 데이터 업데이트 실패")));
    }

    public interface UpdateMyQna {}

    /**
     * 1:1 문의 첨부파일 업로드 API
     *
     * @param request
     * @param response
     * @param serialNo
     * @param attachFile
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "1:1 문의 첨부파일 업로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "MyQna", summary = "1:1 문의 첨부파일 업로드", description = "1:1 문의 첨부파일을 업로드한다.")
    @PutMapping(value = "/upload/{serialNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CSResponseVOSupport> fileUpload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "1:1 문의 시리얼 번호") @PathVariable(value = "serialNo", required = true) BigInteger serialNo,
                                                          @Parameter(description = "첨부파일") @RequestPart(value = "attachFile", required = true) MultipartFile attachFile) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(myQnaService.fileUpload(serialNo, attachFile))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9005, "1:1 문의 첨부파일 업로드 실패")));
    }

    /**
     * 1:1 문의 첨부파일 다운로드 API
     *
     * @param request
     * @param response
     * @param attachFilePath
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "1:1 문의 첨부파일 다운로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "MyQna", summary = "1:1 문의 첨부파일 다운로드", description = "1:1 문의 첨부파일을 다운로드한다.")
    @GetMapping(value = "/download")
    public ResponseEntity<ByteArrayResource> fileDownload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "첨부파일 명") @RequestParam(value = "attachFilePath", required = true) String attachFilePath,
                                                          @Parameter(description = "다운로드 유형") @RequestParam(value = "fileType", required = true) String fileType) {
        return Optional.ofNullable(attachFilePath).map(filePath -> {
            byte[] data = fileMasterService.fileDownload(attachFilePath);
            ByteArrayResource resource = new ByteArrayResource(data);
            try {
                String originName = attachFilePath;
                if (fileType.equals("req")) {
                    MyQna myQna = myQnaRepository.findOne(Example.of(MyQna.builder()
                            .reqAttachFilePath(attachFilePath)
                            .build())).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9006, "파일 미존재"));
                    if (myQna != null) {
                        if (myQna.getReqFileOrigin() != null)
                            originName = myQna.getReqFileOrigin();
                    }
                } else {
                    MyQna myQna = myQnaRepository.findOne(Example.of(MyQna.builder()
                            .resAttachFilePath(attachFilePath)
                            .build())).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9006, "파일 미존재"));
                    if (myQna != null) {
                        if (myQna.getResFileOrigin() != null)
                            originName = myQna.getResFileOrigin();
                    }
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
