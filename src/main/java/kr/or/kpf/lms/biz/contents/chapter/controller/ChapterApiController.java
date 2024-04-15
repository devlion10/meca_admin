package kr.or.kpf.lms.biz.contents.chapter.controller;

import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.contents.chapter.service.ChapterService;
import kr.or.kpf.lms.biz.contents.chapter.vo.request.ChapterApiRequestVO;
import kr.or.kpf.lms.biz.contents.chapter.vo.request.ChapterViewRequestVO;
import kr.or.kpf.lms.biz.contents.chapter.vo.response.ChapterApiResponseVO;
import kr.or.kpf.lms.biz.contents.chapter.vo.response.ChapterViewResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

/**
 * 콘텐츠 관리 > 콘텐츠 관리 API 관련 Controller
 */
@Tag(name = "Contents Management", description = "콘텐츠 관리 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/contents/list/chapter")
public class ChapterApiController extends CSApiControllerSupport {

    private final ChapterService chapterService;

    /**
     * Sample (객체 Swagger 표출을 위해서 사용)
     *
     * @param request
     * @param response
     * @param chapterViewRequestVO
     * @return
     */
    @Tag(name = "Swagger USE(NOT API)", description = "사용하지 않는 API(객체 표출용)")
    @PostMapping(value = "/sample", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChapterViewResponseVO> swaggerUse1(HttpServletRequest request, HttpServletResponse response, @RequestBody ChapterViewRequestVO chapterViewRequestVO) {
        return null;
    }

    /**
     * 챕터(차시) 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @Tag(name = "Contents Management", description = "콘텐츠 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "챕터(차시) 조회 성공", content = @Content(schema = @Schema(implementation = ChapterApiResponseVO.class)))})
    @Operation(operationId="Chapter", summary = "챕터(차시) 조회", description = "챕터(차시) 조회한다.")
    @GetMapping(path = {"/{contentsCode}"})
    public ResponseEntity<Object> getChapterInfo(HttpServletRequest request, Pageable pageable, Model model,
                                                 @Parameter(description = "콘텐츠 코드") @PathVariable(value = "contentsCode", required = true) String contentsCode) {
        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("contentsCode", contentsCode);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(requestParam)
                        .map(searchMap -> resultPaging(chapterService.getList((ChapterViewRequestVO) params(ChapterViewRequestVO.class, searchMap, pageable)), new ArrayList<>()))
                        .orElse(new HashMap<>()));
    }

    /**
     * 챕터 정보 생성 API
     *
     * @param request
     * @param response
     * @param chapterApiRequestVO
     * @return
     */
    @Tag(name = "Contents Management", description = "콘텐츠 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "챕터 정보 생성 성공", content = @Content(schema = @Schema(implementation = ChapterApiResponseVO.class)))})
    @Operation(operationId="Chapter", summary = "챕터 정보 생성", description = "챕터 정보를 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChapterApiResponseVO> createChapterInfo(HttpServletRequest request, HttpServletResponse response,
                                                                  @Validated(value = {CreateChapter.class}) @NotNull @RequestBody ChapterApiRequestVO chapterApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(chapterService.createChapterInfo(chapterApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2042, "챕터 정보 생성 실패")));
    }

    public interface CreateChapter {};

    /**
     * 챕터 정보 업데이트 API
     *
     * @param request
     * @param response
     * @param chapterApiRequestVO
     * @return
     */
    @Tag(name = "Contents Management", description = "콘텐츠 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "챕터 정보 수정 성공", content = @Content(schema = @Schema(implementation = ChapterApiResponseVO.class)))})
    @Operation(operationId="Chapter", summary = "챕터 정보 업데이트", description = "챕터 정보를 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChapterApiResponseVO> updateChapterInfo(HttpServletRequest request, HttpServletResponse response,
                                                                    @Validated(value = {UpdateChapter.class}) @NotNull @RequestBody ChapterApiRequestVO chapterApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(chapterService.updateChapterInfo(chapterApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2043, "챕터 정보 수정 실패")));
    }

    public interface UpdateChapter {};

    /**
     * 챕터 정보 삭제 API
     *
     * @param request
     * @param response
     * @param chapterApiRequestVO
     * @return
     */
    @Tag(name = "Contents Management", description = "콘텐츠 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "챕터 정보 삭제 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId="Chapter", summary = "챕터 정보 삭제", description = "챕터 정보를 삭제한다.")
    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteChapterInfo(HttpServletRequest request, HttpServletResponse response,
                                                                  @Validated(value = {UpdateChapter.class}) @NotNull @RequestBody ChapterApiRequestVO chapterApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(chapterService.deleteChapterInfo(chapterApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2045, "챕터 정보 삭제 실패")));
    }

    /**
     * 챕터 정보 수정 API (다건)
     *
     * @param request
     * @param response
     * @param chapterApiRequestVO
     * @return
     */
    @Tag(name = "Contents Management", description = "콘텐츠 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "챕터 정보 수정 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId="Chapter", summary = "챕터 정보 수정", description = "챕터 정보를 수정한다. (다건)")
    @PutMapping(value = "/modify-list", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> modifyChapterInfoList(HttpServletRequest request, HttpServletResponse response,
                                                                  @Validated(value = {ModifyChapter.class}) @NotNull @RequestBody ChapterApiRequestVO chapterApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(chapterService.modifyChapterInfoList(chapterApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2042, "챕터 정보 생성 실패")));
    }

    public interface ModifyChapter {};

    /**
     * 챕터 정보 생성 API By 엑셀 파일
     *
     * @param request
     * @param response
     * @param excelFile
     * @return
     */
    @Tag(name = "Contents Management", description = "콘텐츠 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "챕터 정보 생성 성공", content = @Content(schema = @Schema(implementation = ChapterApiResponseVO.class)))})
    @Operation(operationId = "Chapter", summary = "챕터 정보 생성", description = "엑셀 파일을 이용하여 챕터 정보를 생성한다.")
    @PostMapping(value = "/create-by-file/{contentsCode}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CSResponseVOSupport> createChapterInfoByFile(HttpServletRequest request, HttpServletResponse response,
                                                                       @Parameter(description = "콘텐츠 코드") @PathVariable(value = "contentsCode", required = true) String contentsCode,
                                                                       @Parameter(description = "엑셀 파일") @RequestPart(value = "excelFile", required = true) MultipartFile excelFile) throws IOException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(chapterService.createChapterInfoByFile(contentsCode, excelFile))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2042, "챕터 정보 생성 실패")));
    }
}
