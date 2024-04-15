package kr.or.kpf.lms.biz.education.exam.controller;

import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.education.exam.service.ExamService;
import kr.or.kpf.lms.biz.education.exam.vo.request.ExamApiRequestVO;
import kr.or.kpf.lms.biz.education.exam.vo.request.ExamViewRequestVO;
import kr.or.kpf.lms.biz.education.exam.vo.response.ExamApiResponseVO;
import kr.or.kpf.lms.biz.education.exam.vo.response.ExamViewResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
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
 * 교육 관리 > 교육 과정 관리 API 관련 Controller
 */
@Tag(name = "Education Management", description = "교육 관리 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/education/curriculum/e-learning/exam")
public class ExamApiController extends CSApiControllerSupport {

    private final ExamService examService;

    /**
     * Sample (객체 Swagger 표출을 위해서 사용)
     *
     * @param request
     * @param response
     * @param examViewRequestVO
     * @return
     */
    @Tag(name = "Swagger USE(NOT API)", description = "사용하지 않는 API(객체 표출용)")
    @PostMapping(value = "/sample", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExamViewResponseVO> swaggerUse1(HttpServletRequest request, HttpServletResponse response, @RequestBody ExamViewRequestVO examViewRequestVO) {
        return null;
    }

    /**
     * 시험 정보 생성
     *
     * @param request
     * @param response
     * @param examApiRequestVO
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "시험 정보 생성 성공", content = @Content(schema = @Schema(implementation = ExamApiResponseVO.class)))})
    @Operation(operationId="Exam", summary = "시험 정보 생성", description = "시험 정보를 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExamApiResponseVO> createExamInfo(HttpServletRequest request, HttpServletResponse response,
        @Validated(value = {CreateExam.class}) @NotNull @RequestBody ExamApiRequestVO examApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(examService.createExamInfo(examApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3041, "시험 정보 생성 실패")));
    }

    public interface CreateExam {}

    /**
     * 시험 정보 업데이트
     *
     * @param request
     * @param response
     * @param examApiRequestVO
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "시험 정보 수정 성공", content = @Content(schema = @Schema(implementation = ExamApiResponseVO.class)))})
    @Operation(operationId="Exam", summary = "시험 정보 업데이트", description = "시험 정보를 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExamApiResponseVO> updateExamInfo(HttpServletRequest request, HttpServletResponse response,
        @Validated(value = {UpdateExam.class}) @NotNull @RequestBody ExamApiRequestVO examApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(examService.updateExamInfo(examApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3043, "시험 정보 수정 실패")));
    }

    public interface UpdateExam {}
}
