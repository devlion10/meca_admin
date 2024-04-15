package kr.or.kpf.lms.biz.business.instructor.clcln.controller;

import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.business.instructor.clcln.ddln.service.BizInstructorClclnDdlnService;
import kr.or.kpf.lms.biz.business.instructor.clcln.ddln.vo.CreateBizInstructorClclnDdln;
import kr.or.kpf.lms.biz.business.instructor.clcln.ddln.vo.UpdateBizInstructorClclnDdln;
import kr.or.kpf.lms.biz.business.instructor.clcln.ddln.vo.request.BizInstructorClclnDdlnApiRequestVO;
import kr.or.kpf.lms.biz.business.instructor.clcln.ddln.vo.request.BizInstructorClclnDdlnViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.clcln.ddln.vo.response.BizInstructorClclnDdlnApiResponseVO;
import kr.or.kpf.lms.biz.business.instructor.clcln.ddln.vo.response.BizInstructorClclnDdlnExcelVO;
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

@Tag(name = "Business Instructor Calculate", description = "강사료 정산 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/business/instructor/calculate")
public class BizInstructorClclnApiController extends CSViewControllerSupport {
    private final BizInstructorClclnDdlnService bizInstructorClclnDdlnService;

    /**
     * 정산 마감일 조회 API
     *
     * @param request
     * @param response
     * @param pageable
     * @return
     */
    @Tag(name = "Business Instructor Calculate", description = "강사료 정산 API")
    @Operation(operationId = "Instructor Calculate Deadline", summary = "정산 마감일 생성", description = "정산 마감일 신청 조회한다.")
    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getDeadlineList(HttpServletRequest request, HttpServletResponse response, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> bizInstructorClclnDdlnService.getBizInstructorClclnDdlnList((BizInstructorClclnDdlnViewRequestVO) params(BizInstructorClclnDdlnViewRequestVO.class, searchMap, pageable)))
                        .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
    }

    /**
     * 정산 마감일 상세 조회 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Business Instructor Calculate", description = "강사료 정산 API")
    @Operation(operationId = "Instructor Calculate Deadline", summary = "정산 마감일 상세 조회", description = "정산 마감일 상세 조회한다.")
    @GetMapping(value = "/{bizInstrClclnDdlnNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getDeadlineObject(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> bizInstructorClclnDdlnService.getBizInstructorClclnDdln((BizInstructorClclnDdlnViewRequestVO) params(BizInstructorClclnDdlnViewRequestVO.class, searchMap, null))));

    }

    /**
     * 정산 마감일 생성
     *
     * @param request
     * @param response
     * @param bizInstructorClclnDdlnApiRequestVO
     * @return
     */
    @Tag(name = "Business Instructor Calculate", description = "강사료 정산 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정산 마감일 생성 성공", content = @Content(schema = @Schema(implementation = BizInstructorClclnDdlnApiResponseVO.class)))})
    @Operation(operationId="Instructor Calculate Deadline", summary = "정산 마감일 생성", description = "정산 마감일 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BizInstructorClclnDdlnApiResponseVO> createDeadlineInfo(HttpServletRequest request, HttpServletResponse response,
                                                                          @Validated(value = {CreateBizInstructorClclnDdln.class}) @NotNull @RequestBody BizInstructorClclnDdlnApiRequestVO bizInstructorClclnDdlnApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizInstructorClclnDdlnService.createBizInstructorClclnDdln(bizInstructorClclnDdlnApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3601, "정산 마감일 생성 실패")));
    }

    /**
     * 정산 마감일 업데이트
     *
     * @param request
     * @param response
     * @param bizInstructorClclnDdlnApiRequestVO
     * @return
     */
    @Tag(name = "Business Instructor Calculate", description = "강사료 정산 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정산 마감일 수정 성공", content = @Content(schema = @Schema(implementation = BizInstructorClclnDdlnApiResponseVO.class)))})
    @Operation(operationId="Instructor Calculate Deadline", summary = "정산 마감일 업데이트", description = "정산 마감일 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BizInstructorClclnDdlnApiResponseVO> updateDeadlineInfo(HttpServletRequest request, HttpServletResponse response,
                                                                 @Validated(value = {UpdateBizInstructorClclnDdln.class}) @NotNull @RequestBody BizInstructorClclnDdlnApiRequestVO bizInstructorClclnDdlnApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizInstructorClclnDdlnService.updateBizInstructorClclnDdln(bizInstructorClclnDdlnApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3603, "정산 마감일 수정 실패")));
    }

    /**
     * 정산 마감일 삭제
     *
     * @param request
     * @param response
     * @param bizInstructorClclnDdlnApiRequestVO
     * @return
     */
    @Tag(name = "Business Instructor Calculate", description = "강사료 정산 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정산 마감일 삭제 성공", content = @Content(schema = @Schema(implementation = BizInstructorClclnDdlnApiResponseVO.class)))})
    @Operation(operationId="Instructor Calculate Deadline", summary = "정산 마감일 삭제", description = "정산 마감일 삭제 한다.")
    @DeleteMapping(value = "/delete/{instuctorClclnDdlnNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteDeadlineInfo(HttpServletRequest request, HttpServletResponse response,
                                                              @NotNull @RequestBody BizInstructorClclnDdlnApiRequestVO bizInstructorClclnDdlnApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizInstructorClclnDdlnService.deleteBizInstructorClclnDdln(bizInstructorClclnDdlnApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3604, "정산 마감일 삭제 실패")));
    }
    /**
     * 정산 마감일 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Business Instructor Calculate", description = "강사료 정산 API")
    @Operation(operationId = "Instructor Calculate Deadline", summary = "정산 마감일 엑셀", description = "정산 마감일 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/excel")
    public void getDeadlineListExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Date date = new Date();
        String dateToStr = DateFormatUtils.format(date, "yyyyMMdd_HHmmSS_");
        List<BizInstructorClclnDdlnExcelVO> BizInstructorDistExcelVOList = bizInstructorClclnDdlnService.getExcel((BizInstructorClclnDdlnViewRequestVO) params(BizInstructorClclnDdlnViewRequestVO.class, CSSearchMap.of(request), null));
        OneSheetExcelFile<BizInstructorClclnDdlnExcelVO> excelFile = new OneSheetExcelFile<>(BizInstructorDistExcelVOList, BizInstructorClclnDdlnExcelVO.class);
        response.setHeader("Content-Disposition", "attachment; filename=" + dateToStr+URLEncoder.encode("정산마감일", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");
        excelFile.write(response.getOutputStream());
    }
}
