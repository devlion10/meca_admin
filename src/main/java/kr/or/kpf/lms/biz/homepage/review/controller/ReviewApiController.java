package kr.or.kpf.lms.biz.homepage.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.business.instructor.dist.vo.request.BizInstructorDistApiRequestVO;
import kr.or.kpf.lms.biz.business.instructor.dist.vo.response.BizInstructorDistApiResponseVO;
import kr.or.kpf.lms.biz.homepage.review.service.ReviewService;
import kr.or.kpf.lms.biz.homepage.review.vo.request.ReviewApiRequestVO;
import kr.or.kpf.lms.biz.homepage.review.vo.request.ReviewViewRequestVO;
import kr.or.kpf.lms.biz.homepage.review.vo.response.ReviewApiResponseVO;
import kr.or.kpf.lms.biz.homepage.review.vo.response.ReviewViewResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Optional;

/**
 * 홈페이지 관리 > 교육 후기 API 관련 Controller
 */
@Tag(name = "Homepage Management", description = "홈페이지 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/homepage/review")
public class ReviewApiController extends CSApiControllerSupport {

    private final ReviewService reviewService;

    /**
     * Sample (객체 Swagger 표출을 위해서 사용)
     *
     * @param request
     * @param response
     * @param reviewViewRequestVO
     * @return
     */
    @Tag(name = "Swagger USE(NOT API)", description = "사용하지 않는 API(객체 표출용)")
    @PostMapping(value = "/sample", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewViewResponseVO> swaggerUse1(HttpServletRequest request, HttpServletResponse response, @RequestBody ReviewViewRequestVO reviewViewRequestVO) {
        return null;
    }

    /**
     * 교육 후기방 조회 API
     *
     * @param request
     * @param response
     * @param pageable
     * @param sequenceNo
     * @param containTextType
     * @param containText
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @Operation(operationId = "Review", summary = "교육 후기 조회", description = "교육 후기 조회한다.")
    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getList(HttpServletRequest request, HttpServletResponse response, Pageable pageable,
                                          @RequestParam(value="sequenceNo", required = false) BigInteger sequenceNo,
                                          @RequestParam(value="containTextType", required = false) String containTextType,
                                          @RequestParam(value="containText", required = false) String containText,
                                          @RequestParam(value="userId", required = false) String userId,
                                          @RequestParam(value="userName", required = false) String userName) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> reviewService.getList((ReviewViewRequestVO) params(ReviewViewRequestVO.class, searchMap, pageable)))
                        .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
    }

    /**
     * 교육 후기방 등록 API
     *
     * @param request
     * @param response
     * @param reviewApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "참여 / 소통 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "교육 후기 생성 성공", content = @Content(schema = @Schema(implementation = ReviewApiResponseVO.class)))})
    @Operation(operationId = "Review", summary = "교육 후기 생성", description = "교육 후기 데이터를 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewApiResponseVO> createReview(HttpServletRequest request, HttpServletResponse response,
                                                            @Validated(value = {CreateReview.class}) @NotNull @RequestBody ReviewApiRequestVO reviewApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(reviewService.createReview(reviewApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7101, "교육 후기 데이터 생성 실패")));
    }

    public interface CreateReview {}

    /**
     * 교육 후기 업데이트 API
     *
     * @param request
     * @param response
     * @param reviewApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "참여 / 소통 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "교육 후기 업데이트 성공", content = @Content(schema = @Schema(implementation = ReviewApiResponseVO.class)))})
    @Operation(operationId = "Review", summary = "교육 후기 업데이트", description = "교육 후기 데이터를 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewApiResponseVO> updateReview(HttpServletRequest request, HttpServletResponse response,
                                                            @Validated(value = {UpdateReview.class}) @NotNull @RequestBody ReviewApiRequestVO reviewApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(reviewService.updateReview(reviewApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7102, "교육 후기 데이터 업데이트 실패")));
    }

    public interface UpdateReview {}

    /**
     * 교육 후기방 삭제
     *
     * @param request
     * @param response
     * @param reviewApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "교육 후기 삭제 성공", content = @Content(schema = @Schema(implementation = ReviewApiRequestVO.class)))})
    @Operation(operationId="Review", summary = "교육 후기 삭제", description = "교육 후기 삭제 한다.")
    @DeleteMapping(value = "/delete/{sequenceNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteInfo(HttpServletRequest request, HttpServletResponse response,
                                                          @NotNull @RequestBody ReviewApiRequestVO reviewApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(reviewService.deleteInfo(reviewApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3604, "교육 후기 삭제 실패")));
    }

}
