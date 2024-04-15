package kr.or.kpf.lms.biz.homepage.topqna.controller;

import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.homepage.topqna.service.TopQnaService;
import kr.or.kpf.lms.biz.homepage.topqna.vo.request.TopQnaApiRequestVO;
import kr.or.kpf.lms.biz.homepage.topqna.vo.request.TopQnaViewRequestVO;
import kr.or.kpf.lms.biz.homepage.topqna.vo.response.TopQnaApiResponseVO;
import kr.or.kpf.lms.biz.homepage.topqna.vo.response.TopQnaViewResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
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
 * 홈페이지 관리 > 자주하는 질문 API 관련 Controller
 */
@Tag(name = "Homepage Management", description = "홈페이지 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/homepage/top-qna")
public class TopQnaApiController extends CSViewControllerSupport {

    private final TopQnaService topQnaService;

    /**
     * Sample (객체 Swagger 표출을 위해서 사용)
     *
     * @param request
     * @param response
     * @param topQnaViewRequestVO
     * @return
     */
    @Tag(name = "Swagger USE(NOT API)", description = "사용하지 않는 API(객체 표출용)")
    @PostMapping(value = "/sample", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TopQnaViewResponseVO> swaggerUse1(HttpServletRequest request, HttpServletResponse response, @RequestBody TopQnaViewRequestVO topQnaViewRequestVO) {
        return null;
    }

    /**
     * 자주묻는 질문 등록 API
     *
     * @param request
     * @param response
     * @param topQnaApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "자주묻는 질문 생성 성공", content = @Content(schema = @Schema(implementation = TopQnaApiResponseVO.class)))})
    @Operation(operationId = "TopQna", summary = "자주묻는 질문 생성", description = "자주묻는 질문 데이터를 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TopQnaApiResponseVO> createTopQna(HttpServletRequest request, HttpServletResponse response,
                                                            @Validated(value = {TopQnaApiController.CreateTopQna.class}) @NotNull @RequestBody TopQnaApiRequestVO topQnaApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(topQnaService.createTopQna(topQnaApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7061, "자주묻는 질문 데이터 생성 실패")));
    }

    public interface CreateTopQna {}

    /**
     * 자주묻는 질문 업데이트 API
     *
     * @param request
     * @param response
     * @param topQnaApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "자주묻는 질문 업데이트 성공", content = @Content(schema = @Schema(implementation = TopQnaApiResponseVO.class)))})
    @Operation(operationId = "TopQna", summary = "자주묻는 질문 업데이트", description = "자주묻는 질문 데이터를 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TopQnaApiResponseVO> updateTopQna(HttpServletRequest request, HttpServletResponse response,
                                                            @Validated(value = {TopQnaApiController.UpdateTopQna.class}) @NotNull @RequestBody TopQnaApiRequestVO topQnaApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(topQnaService.updateTopQna(topQnaApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7062, "자주묻는 질문 데이터 업데이트 실패")));
    }
    
    public interface UpdateTopQna {}

    /**
     * 자주묻는 질문 삭제 API
     *
     * @param request
     * @param response
     * @param sequenceNo
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "자주묻는 질문 삭제 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "TopQna", summary = "자주묻는 질문 삭제", description = "자주묻는 질문 삭제한다.")
    @DeleteMapping(value = "/delete/{sequenceNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteTopQna(HttpServletRequest request, HttpServletResponse response,
                                                            @Parameter(description = "자주묻는 질문 번호") @PathVariable(value = "sequenceNo", required = true) BigInteger sequenceNo) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(topQnaService.deleteTopQna(sequenceNo))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7063, "자주묻는 질문 삭제 실패")));
    }
}
