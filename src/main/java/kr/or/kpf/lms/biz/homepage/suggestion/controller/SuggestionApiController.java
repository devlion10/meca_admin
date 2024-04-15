package kr.or.kpf.lms.biz.homepage.suggestion.controller;

import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.homepage.suggestion.controller.SuggestionApiController;
import kr.or.kpf.lms.biz.homepage.suggestion.vo.request.SuggestionApiRequestVO;
import kr.or.kpf.lms.biz.homepage.suggestion.vo.response.SuggestionApiResponseVO;
import kr.or.kpf.lms.biz.homepage.suggestion.service.SuggestionService;
import kr.or.kpf.lms.biz.homepage.suggestion.vo.request.SuggestionViewRequestVO;
import kr.or.kpf.lms.biz.homepage.suggestion.vo.response.SuggestionViewResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.Optional;

/**
 * 홈페이지 관리 > 교육 주제 제안 API 관련 Controller
 */
@Tag(name = "Homepage Management", description = "홈페이지 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/homepage/suggestion")
public class SuggestionApiController extends CSApiControllerSupport {

    private final SuggestionService suggestionService;

    /**
     * Sample (객체 Swagger 표출을 위해서 사용)
     *
     * @param request
     * @param response
     * @param suggestionViewRequestVO
     * @return
     */
    @Tag(name = "Swagger USE(NOT API)", description = "사용하지 않는 API(객체 표출용)")
    @PostMapping(value = "/sample", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuggestionViewResponseVO> swaggerUse1(HttpServletRequest request, HttpServletResponse response, @RequestBody SuggestionViewRequestVO suggestionViewRequestVO) {
        return null;
    }

    /**
     * 교육 주제 제안 업데이트 API
     *
     * @param request
     * @param response
     * @param suggestionApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "교육 주제 제안 업데이트 성공", content = @Content(schema = @Schema(implementation = SuggestionApiResponseVO.class)))})
    @Operation(operationId = "Suggestion", summary = "교육 주제 제안 업데이트", description = "교육 주제 제안 데이터를 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuggestionApiResponseVO> updateSuggestion(HttpServletRequest request, HttpServletResponse response,
                                                            @Validated(value = {UpdateSuggestion.class}) @NotNull @RequestBody SuggestionApiRequestVO suggestionApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(suggestionService.updateSuggestion(suggestionApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7082, "교육 주제 제안 데이터 업데이트 실패")));
    }

    public interface UpdateSuggestion {}

    /**
     * 교육 주제 제안 업데이트 API
     *
     * @param request
     * @param response
     * @param suggestionApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "교육 주제 제안 댓글 작성/수정/삭제 성공", content = @Content(schema = @Schema(implementation = SuggestionApiResponseVO.class)))})
    @Operation(operationId = "Suggestion", summary = "교육 주제 제안 댓글 작성/수정/삭제", description = "교육 주제 제안 댓글 작성/수정/삭제 한다.")
    @PutMapping(value = "/comment", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuggestionApiResponseVO> commentSuggestion(HttpServletRequest request, HttpServletResponse response,
                                                                    @Validated(value = {CommentSuggestion.class}) @NotNull @RequestBody SuggestionApiRequestVO suggestionApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(suggestionService.commentSuggestion(suggestionApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7082, "교육 주제 제안 데이터 업데이트 실패")));
    }

    public interface CommentSuggestion {}

    /**
     * 교육 주제 제안 삭제 API
     *
     * @param request
     * @param response
     * @param sequenceNo
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "교육 주제 제안 삭제", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "Suggestion", summary = "교육 주제 제안 삭제", description = "교육 주제 제안 데이터를 삭제한다.")
    @DeleteMapping(value = "/delete/{sequenceNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteSuggestion(HttpServletRequest request, HttpServletResponse response,
                                                                @Parameter(description = "교육 주제 제안 시퀀스 번호") @PathVariable(value = "sequenceNo", required = true) BigInteger sequenceNo) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(suggestionService.deleteSuggestion(sequenceNo))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7083, "교육 주제 제안 삭제 실패")));
    }
}
