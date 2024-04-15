package kr.or.kpf.lms.biz.homepage.page.controller;

import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.homepage.page.service.PageService;
import kr.or.kpf.lms.biz.homepage.page.vo.request.PageApiRequestVO;
import kr.or.kpf.lms.biz.homepage.page.vo.request.PageViewRequestVO;
import kr.or.kpf.lms.biz.homepage.page.vo.response.PageApiResponseVO;
import kr.or.kpf.lms.biz.homepage.page.vo.response.PageViewResponseVO;
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
import java.util.Optional;

/**
 * 홈페이지 관리 > 문서 API 관련 Controller
 */
@Tag(name = "Homepage Management", description = "홈페이지 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/homepage/page")
public class PageApiController extends CSApiControllerSupport {

    private final PageService pageService;

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
    public ResponseEntity<PageViewResponseVO> swaggerUse1(HttpServletRequest request, HttpServletResponse response, @RequestBody PageViewRequestVO myQnaViewRequestVO) {
        return null;
    }

    /**
     * 문서 등록 API
     *
     * @param request
     * @param response
     * @param pageApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "문서 생성 성공", content = @Content(schema = @Schema(implementation = PageApiResponseVO.class)))})
    @Operation(operationId = "Page", summary = "문서 생성", description = "문서 데이터를 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageApiResponseVO> createPage(HttpServletRequest request, HttpServletResponse response,
                                                        @Validated(value = {PageApiController.CreatePage.class}) @NotNull @RequestBody PageApiRequestVO pageApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(pageService.createPage(pageApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7043, "문서 데이터 생성 실패")));
    }

    public interface CreatePage {}

    /**
     * 문서 업데이트 API
     *
     * @param request
     * @param response
     * @param pageApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "문서 업데이트 성공", content = @Content(schema = @Schema(implementation = PageApiResponseVO.class)))})
    @Operation(operationId = "Page", summary = "문서 업데이트", description = "문서 데이터를 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageApiResponseVO> updatePage(HttpServletRequest request, HttpServletResponse response,
                                                          @Validated(value = {UpdatePage.class}) @NotNull @RequestBody PageApiRequestVO pageApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(pageService.updatePage(pageApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7023, "문서 데이터 업데이트 실패")));
    }

    public interface UpdatePage {}

    /**
     * 문서 삭제
     *
     * @param request
     * @param response
     * @param pageApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "문서 삭제 성공", content = @Content(schema = @Schema(implementation = PageApiResponseVO.class)))})
    @Operation(operationId="Page", summary = "문서 삭제", description = "문서 삭제 한다.")
    @DeleteMapping(value = "/delete/{sequenceNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteEvent(HttpServletRequest request, HttpServletResponse response,
                                                           @NotNull @RequestBody PageApiRequestVO pageApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(pageService.deleteInfo(pageApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3544, "문서 삭제 실패")));
    }
}
