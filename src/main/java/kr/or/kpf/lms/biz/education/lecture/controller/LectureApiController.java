package kr.or.kpf.lms.biz.education.lecture.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.education.lecture.service.LectureService;
import kr.or.kpf.lms.biz.education.lecture.vo.request.LectureApiRequestVO;
import kr.or.kpf.lms.biz.education.lecture.vo.request.LectureViewRequestVO;
import kr.or.kpf.lms.biz.education.lecture.vo.response.LectureApiResponseVO;
import kr.or.kpf.lms.biz.education.lecture.vo.response.LectureViewResponseVO;
import kr.or.kpf.lms.biz.education.schedule.controller.ScheduleApiController;
import kr.or.kpf.lms.biz.education.schedule.vo.request.ScheduleApiRequestVO;
import kr.or.kpf.lms.biz.education.schedule.vo.response.ScheduleApiResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

/**
 * 교육 관리 > 교육 개설 API 관련 Controller
 */
@Tag(name = "Education Management", description = "교육 관리 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/education/curriculum/lecture")
public class LectureApiController extends CSApiControllerSupport {

    private final LectureService lectureService;

    /**
     * Sample (객체 Swagger 표출을 위해서 사용)
     *
     * @param request
     * @param response
     * @param lectureViewRequestVO
     * @return
     */
    @Tag(name = "Swagger USE(NOT API)", description = "사용하지 않는 API(객체 표출용)")
    @PostMapping(value = "/sample", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LectureViewResponseVO> swaggerUse1(HttpServletRequest request, HttpServletResponse response, @RequestBody LectureViewRequestVO lectureViewRequestVO) {
        return null;
    }

    /**
     * 일반 교육 강의 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일반 교육 강의 조회 성공", content = @Content(schema = @Schema(implementation = LectureApiResponseVO.class)))})
    @Operation(operationId="Schedule", summary = "일반 교육 강의 조회", description = "일반 교육 강의 조회한다.")
    @GetMapping(path = {"/{educationPlanCode}"})
    public ResponseEntity<Object> getLectureInfo(HttpServletRequest request, Pageable pageable, Model model,
                                                 @Parameter(description = "교육 과정 개설 코드") @PathVariable(value = "educationPlanCode", required = true) String educationPlanCode) {
        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("educationPlanCode", educationPlanCode);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(requestParam)
                        .map(searchMap -> resultPaging(lectureService.getList((LectureViewRequestVO) params(LectureViewRequestVO.class, searchMap, pageable)), new ArrayList<>()))
                        .orElse(new HashMap<>()));
    }

    /**
     * 일반 교육 강의 삭제
     *
     * @param request
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일반 교육 강의 삭제", content = @Content(schema = @Schema(implementation = LectureApiResponseVO.class)))})
    @Operation(operationId="Schedule", summary = "일반 교육 강의 삭제", description = "일반 교육 강의를 삭제한다.")
    @PutMapping(value = "/delete/{lectureCode}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LectureApiResponseVO> updateEducationLecture(HttpServletRequest request, HttpServletResponse response,
                                                                         @Parameter(description = "과정 코드") @PathVariable(value = "lectureCode", required = true) String lectureCode) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(lectureService.deleteLecture(lectureCode))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3063, "교육 일정 수정 실패")));
    }
}
