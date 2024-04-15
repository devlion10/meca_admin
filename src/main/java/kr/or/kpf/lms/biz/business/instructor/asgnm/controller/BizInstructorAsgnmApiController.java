package kr.or.kpf.lms.biz.business.instructor.asgnm.controller;

import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.business.instructor.aply.vo.request.BizInstructorAplyViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.asgnm.service.BizInstructorAsgnmService;
import kr.or.kpf.lms.biz.business.instructor.asgnm.vo.CreateBizInstructorAsgnm;
import kr.or.kpf.lms.biz.business.instructor.asgnm.vo.UpdateBizInstructorAsgnm;
import kr.or.kpf.lms.biz.business.instructor.asgnm.vo.request.BizInstructorAsgnmApiRequestVO;
import kr.or.kpf.lms.biz.business.instructor.asgnm.vo.request.BizInstructorAsgnmViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.asgnm.vo.response.BizInstructorAsgnmApiResponseVO;
import kr.or.kpf.lms.biz.business.instructor.dist.vo.request.BizInstructorDistViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.dist.vo.response.BizInstructorDistExcelVO;
import kr.or.kpf.lms.biz.business.instructor.vo.response.BizInstructorExcelVO;
import kr.or.kpf.lms.biz.business.organization.aply.vo.request.BizOrganizationAplyCustomViewRequestVO;
import kr.or.kpf.lms.common.exceldownload.excel.OneSheetExcelFile;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Tag(name = "Business Instructor Assignment", description = "강사 배정 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/business/instructor/assignment")
public class BizInstructorAsgnmApiController extends CSViewControllerSupport {
    private final BizInstructorAsgnmService bizInstructorAsgnmService;

    /**
     * 강사 배정 조회 API
     *
     * @param request
     * @param response
     * @param pageable
     * @return
     */
    @Tag(name = "Business Instructor Assignment", description = "강사 배정 API")
    @Operation(operationId = "Instructor Assignment", summary = "강사 배정 생성", description = "강사 배정 조회한다.")
    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getList(HttpServletRequest request, HttpServletResponse response, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> bizInstructorAsgnmService.getBizInstructorAsgnmList((BizInstructorAsgnmViewRequestVO) params(BizInstructorAsgnmViewRequestVO.class, searchMap, pageable)))
                        .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
    }

    /**
     * 강사 배정 상세 조회 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Business Instructor Assignment", description = "강사 배정 API")
    @Operation(operationId = "Instructor Assignment", summary = "강사 배정 상세 조회", description = "강사 배정 상세 조회한다.")
    @GetMapping(value = "/{bizInstrAsgnmNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getObject(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> bizInstructorAsgnmService.getBizInstructorAsgnm((BizInstructorAsgnmViewRequestVO) params(BizInstructorAsgnmViewRequestVO.class, searchMap, null))));

    }

    /**
     * 강사 배정 생성
     *
     * @param request
     * @param response
     * @param bizInstructorAsgnmApiRequestVO
     * @return
     */
    @Tag(name = "Business Instructor Assignment", description = "강사 배정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강사 배정 생성 성공", content = @Content(schema = @Schema(implementation = BizInstructorAsgnmApiResponseVO.class)))})
    @Operation(operationId="Instructor Assignment", summary = "강사 배정 생성", description = "강사 배정 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BizInstructorAsgnmApiResponseVO> createInfo(HttpServletRequest request, HttpServletResponse response,
                                                                      @Validated(value = {CreateBizInstructorAsgnm.class}) @NotNull @RequestBody BizInstructorAsgnmApiRequestVO bizInstructorAsgnmApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizInstructorAsgnmService.createBizInstructorAsgnm(bizInstructorAsgnmApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3601, "강사모집 배정 생성 실패")));
    }

    /**
     * 강사 배정 업데이트
     *
     * @param request
     * @param response
     * @param bizInstructorAsgnmApiRequestVO
     * @return
     */
    @Tag(name = "Business Instructor Assignment", description = "강사 배정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강사 배정 수정 성공", content = @Content(schema = @Schema(implementation = BizInstructorAsgnmApiResponseVO.class)))})
    @Operation(operationId="Instructor Assignment", summary = "강사 배정 업데이트", description = "강사 배정 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BizInstructorAsgnmApiResponseVO> updateInfo(HttpServletRequest request, HttpServletResponse response,
                                                                 @Validated(value = {UpdateBizInstructorAsgnm.class}) @NotNull @RequestBody BizInstructorAsgnmApiRequestVO bizInstructorAsgnmApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizInstructorAsgnmService.updateBizInstructorAsgnm(bizInstructorAsgnmApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3603, "강사 배정 수정 실패")));
    }

    /**
     * 강사 배정 삭제
     *
     * @param request
     * @param response
     * @param bizInstructorAsgnmApiRequestVO
     * @return
     */
    @Tag(name = "Business Instructor Assignment", description = "강사 배정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강사 배정 삭제 성공", content = @Content(schema = @Schema(implementation = BizInstructorAsgnmApiResponseVO.class)))})
    @Operation(operationId="Instructor Assignment", summary = "강사 배정 삭제", description = "강사 배정 삭제 한다.")
    @DeleteMapping(value = "/delete/{instuctorAsgnmNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteInfo(HttpServletRequest request, HttpServletResponse response,
                                                              @NotNull @RequestBody BizInstructorAsgnmApiRequestVO bizInstructorAsgnmApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizInstructorAsgnmService.deleteBizInstructorAsgnm(bizInstructorAsgnmApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3604, "강사 배정 삭제 실패")));
    }

    /**
     * 강사 배정 삭제
     *
     * @param request
     * @param response
     * @param bizInstructorAsgnmApiRequestVO
     * @return
     */
    @Tag(name = "Business Instructor Assignment", description = "강사 배정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강사 배정 삭제 성공", content = @Content(schema = @Schema(implementation = BizInstructorAsgnmApiResponseVO.class)))})
    @Operation(operationId="Instructor Assignment", summary = "강사 배정 삭제", description = "강사 배정 삭제 한다.")
    @DeleteMapping(value = "/deletes", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteAll(HttpServletRequest request, HttpServletResponse response,
                                                         @NotNull @RequestBody BizInstructorAsgnmApiRequestVO bizInstructorAsgnmApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizInstructorAsgnmService.deleteAllBizInstructorAsgnm(bizInstructorAsgnmApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3604, "강사 배정 삭제 실패")));
    }

    /**
     * 강사 배정 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Business Instructor Assignment", description = "강사 배정 API")
    @Operation(operationId = "Instructor Assignment", summary = "강사 배정 엑셀", description = "강사배정 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/excel")
    public void getListExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Date date = new Date();
        String dateToStr = DateFormatUtils.format(date, "yyyyMMdd_HHmmSS_");
        List<BizInstructorExcelVO> bizInstructorExcelVOList = bizInstructorAsgnmService.getExcel((BizOrganizationAplyCustomViewRequestVO) params(BizOrganizationAplyCustomViewRequestVO.class, CSSearchMap.of(request), null));
        OneSheetExcelFile<BizInstructorExcelVO> excelFile = new OneSheetExcelFile<>(bizInstructorExcelVOList, BizInstructorExcelVO.class);
        response.setHeader("Content-Disposition", "attachment; filename=" + dateToStr+URLEncoder.encode("강사배정", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");
        excelFile.write(response.getOutputStream());
    }
}
