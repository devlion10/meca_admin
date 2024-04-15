package kr.or.kpf.lms.biz.homepage.archive.guide.classsubject.controller;

import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.homepage.archive.guide.classsubject.service.ClassSubjectService;
import kr.or.kpf.lms.biz.homepage.archive.guide.classsubject.vo.CreateClassSubject;
import kr.or.kpf.lms.biz.homepage.archive.guide.classsubject.vo.UpdateClassSubject;
import kr.or.kpf.lms.biz.homepage.archive.guide.classsubject.vo.request.ClassSubjectApiRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.guide.classsubject.vo.request.ClassSubjectViewRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.guide.classsubject.vo.response.ClassSubjectApiResponseVO;
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
import java.util.ArrayList;
import java.util.Optional;

/**
 * 수업지도안 교과 교과 API 관련 Controller
 */
@Tag(name = "Homepage Management", description = "홈페이지 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/homepage/class-guide/class-subject")
public class ClassSubjectApiController extends CSApiControllerSupport {

    private final ClassSubjectService classSubjectService;

    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiImplicitParams(
        {@ApiImplicitParam(
                name = "startDate"
                , value = "조회 시작일자(YYYY-MM-DD)"
                , required = false
                , dataType = "string"
                , example = "2022-09-01"
                , paramType = "query"
                , defaultValue = ""),
        @ApiImplicitParam(
                name = "endDate"
                , value = "조회 종료일자(YYYY-MM-DD)"
                , required = false
                , dataType = "string"
                , example = "2022-09-30"
                , paramType = "query"
                , defaultValue = ""),
        @ApiImplicitParam(
                name = "containTextType"
                , value = "검색어 범위"
                , required = false
                , paramType = "query"
                , defaultValue = ""),
        @ApiImplicitParam(
                name = "containText"
                , value = "검색어"
                , required = false
                , paramType = "query"
                , defaultValue = "")
    })
    @Operation(operationId = "ClassSubject", summary = "수업지도안 교과 조회", description = "수업지도안 교과를 조회한다.")
    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getClassSubjectList(HttpServletRequest request, HttpServletResponse response, Pageable pageable,
        @RequestParam(value="startDate", required = false) String startDate, @RequestParam(value="endDate", required = false) String endDate,
        @RequestParam(value="containTextType", required = false) String containTextType,
        @RequestParam(value="containText", required = false) String containText) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> classSubjectService.getList((ClassSubjectViewRequestVO) params(ClassSubjectViewRequestVO.class, searchMap, pageable)))
                        .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
    }

    /**
     * 수업지도안 교과 생성 API
     *
     * @param request
     * @param response
     * @param classSubjectApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수업지도안 교과 생성 성공", content = @Content(schema = @Schema(implementation = ClassSubjectApiResponseVO.class)))})
    @Operation(operationId = "ClassSubject", summary = "수업지도안 교과 생성", description = "수업지도안 교과 데이터를 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClassSubjectApiResponseVO> createClassSubject(HttpServletRequest request, HttpServletResponse response,
                                                                        @Validated(value = {CreateClassSubject.class}) @NotNull @RequestBody ClassSubjectApiRequestVO classSubjectApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(classSubjectService.createClassSubject(classSubjectApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR4001, "수업지도안 교과 생성 실패")));
    }

    /**
     * 수업지도안 교과 업데이트 API
     *
     * @param request
     * @param response
     * @param classSubjectApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수업지도안 교과 업데이트 성공", content = @Content(schema = @Schema(implementation = ClassSubjectApiResponseVO.class)))})
    @Operation(operationId = "ClassSubject", summary = "수업지도안 교과 업데이트", description = "수업지도안 교과 데이터를 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClassSubjectApiResponseVO> updateClassSubject(HttpServletRequest request, HttpServletResponse response,
                                                                              @Validated(value = {UpdateClassSubject.class}) @NotNull @RequestBody ClassSubjectApiRequestVO classSubjectApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(classSubjectService.updateInfo(classSubjectApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7102, "수업지도안 교과 데이터 업데이트 실패")));
    }

    /**
     * 수업지도안 교과 순서 변경 API
     *
     * @param request
     * @param response
     * @param classSubjectApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수업지도안 교과 순서 변경 성공", content = @Content(schema = @Schema(implementation = ClassSubjectApiResponseVO.class)))})
    @Operation(operationId = "ClassSubject", summary = "수업지도안 교과 순서 변경", description = "수업지도안 교과 데이터 순서 변경한다.")
    @PutMapping(value = "/order", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClassSubjectApiResponseVO> changeClassSubject(HttpServletRequest request, HttpServletResponse response,
                                                                        @Validated(value = {UpdateClassSubject.class}) @NotNull @RequestBody ClassSubjectApiRequestVO classSubjectApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(classSubjectService.changeOrder(classSubjectApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7102, "수업지도안 교과 데이터 순서 변경 실패")));
    }


    /**
     * 수업지도안 과목 삭제
     *
     * @param request
     * @param response
     * @param classSubjectApiRequestVO
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수업지도안 교과 삭제 성공", content = @Content(schema = @Schema(implementation = ClassSubjectApiResponseVO.class)))})
    @Operation(operationId="ClassSubject", summary = "수업지도안 교과 삭제", description = "수업지도안 교과 삭제 한다.")
    @DeleteMapping(value = "/delete/{individualCode}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteInfo(HttpServletRequest request, HttpServletResponse response,
                                                          @NotNull @RequestBody ClassSubjectApiRequestVO classSubjectApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(classSubjectService.deleteInfo(classSubjectApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3544, "수업지도안 삭제 실패")));
    }
}
