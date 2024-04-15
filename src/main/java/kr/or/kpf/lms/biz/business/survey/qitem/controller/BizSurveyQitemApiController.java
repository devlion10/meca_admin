package kr.or.kpf.lms.biz.business.survey.qitem.controller;

import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.business.survey.qitem.service.BizSurveyQitemService;
import kr.or.kpf.lms.biz.business.survey.qitem.vo.request.BizSurveyQitemApiRequestVO;
import kr.or.kpf.lms.biz.business.survey.qitem.vo.request.BizSurveyQitemViewRequestVO;
import kr.or.kpf.lms.biz.business.survey.qitem.vo.response.BizSurveyQitemApiResponseVO;
import kr.or.kpf.lms.biz.business.survey.qitem.vo.response.BizSurveyQitemExcelVO;
import kr.or.kpf.lms.biz.business.survey.vo.CreateBizSurvey;
import kr.or.kpf.lms.biz.business.survey.vo.UpdateBizSurvey;
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

@Tag(name = "Survey Qitem", description = "상호평가 문항 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/business/survey/qitem")
public class BizSurveyQitemApiController extends CSViewControllerSupport {

    private final BizSurveyQitemService bizSurveyQitemService;

    /**
     * 상호평가 문항 조회 API
     *
     * @param request
     * @param response
     * @param pageable
     * @return
     */
    @Tag(name = "Survey Qitem", description = "상호평가 문항 API")
    @Operation(operationId = "Survey Qitem", summary = "상호평가 문항 조회", description = "상호평가 문항 조회한다.")
    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getList(HttpServletRequest request, HttpServletResponse response, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> bizSurveyQitemService.getBizSurveyQitemList((BizSurveyQitemViewRequestVO) params(BizSurveyQitemViewRequestVO.class, searchMap, pageable)))
                        .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
    }

    /**
     * 상호평가 문항 상세 조회 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Survey Qitem", description = "상호평가 문항 API")
    @Operation(operationId = "Survey Qitem", summary = "상호평가 문항 상세 조회", description = "상호평가 문항 상세 조회한다.")
    @GetMapping(value = "/{SurveyNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getObject(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> bizSurveyQitemService.getBizSurvey((BizSurveyQitemViewRequestVO) params(BizSurveyQitemViewRequestVO.class, searchMap, null))));

    }

    /**
     * 상호평가 문항 생성
     *
     * @param request
     * @param response
     * @param bizSurveyQitemApiRequestVO
     * @return
     */
    @Tag(name = "Survey Qitem", description = "상호평가 문항 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상호평가 문항 생성 성공", content = @Content(schema = @Schema(implementation = BizSurveyQitemApiResponseVO.class)))})
    @Operation(operationId="Survey Qitem", summary = "상호평가 문항 생성", description = "상호평가 문항 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BizSurveyQitemApiResponseVO> createInfo(HttpServletRequest request, HttpServletResponse response,
                                                                  @Validated(value = {CreateBizSurvey.class}) @NotNull @RequestBody BizSurveyQitemApiRequestVO bizSurveyQitemApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizSurveyQitemService.createBizSurveyQitem(bizSurveyQitemApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3581, "상호평가 문항 생성 실패")));
    }

    /**
     * 상호평가 문항 업데이트
     *
     * @param request
     * @param response
     * @param bizSurveyQitemApiRequestVO
     * @return
     */
    @Tag(name = "Survey Qitem", description = "상호평가 문항 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상호평가 문항 수정 성공", content = @Content(schema = @Schema(implementation = BizSurveyQitemApiResponseVO.class)))})
    @Operation(operationId="Survey Qitem", summary = "상호평가 문항 업데이트", description = "상호평가 문항 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BizSurveyQitemApiResponseVO> updateInfo(HttpServletRequest request, HttpServletResponse response,
                                                                  @Validated(value = {UpdateBizSurvey.class}) @NotNull @RequestBody BizSurveyQitemApiRequestVO bizSurveyQitemApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizSurveyQitemService.updateBizSurveyQitem(bizSurveyQitemApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3583, "상호평가 문항 수정 실패")));
    }

    /**
     * 상호평가 문항 삭제
     *
     * @param request
     * @param response
     * @param bizSurveyQitemApiRequestVO
     * @return
     */
    @Tag(name = "Survey Qitem", description = "상호평가 문항 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상호평가 문항 삭제 성공", content = @Content(schema = @Schema(implementation = BizSurveyQitemApiResponseVO.class)))})
    @Operation(operationId="Survey Qitem", summary = "상호평가 문항 삭제", description = "상호평가 문항 삭제 한다.")
    @DeleteMapping(value = "/delete/{SurveyQitemNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteInfo(HttpServletRequest request, HttpServletResponse response,
                                                          @NotNull @RequestBody BizSurveyQitemApiRequestVO bizSurveyQitemApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizSurveyQitemService.deleteBizSurveyQitem(bizSurveyQitemApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3584, "상호평가 문항 삭제 실패")));
    }

    /**
     * 상호평가 문항 삭제
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Survey Qitem", description = "상호평가 문항 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상호평가 문항 항목 삭제 성공", content = @Content(schema = @Schema(implementation = BizSurveyQitemApiResponseVO.class)))})
    @Operation(operationId="Survey Qitem", summary = "상호평가 문항 항목 삭제", description = "상호평가 문항 항목을 삭제 한다.")
    @DeleteMapping(value = "/item/delete/{bizSurveyQitemItemNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteInfoItem(HttpServletRequest request, HttpServletResponse response,
                                                              @PathVariable(value = "bizSurveyQitemItemNo", required = true) String bizSurveyQitemItemNo) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizSurveyQitemService.deleteBizSurveyQitemItem(bizSurveyQitemItemNo))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3584, "상호평가 문항 삭제 실패")));
    }
    /**
     * 상호평가 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Survey Qitem", description = "상호평가 문항 API")
    @Operation(operationId = "Survey Qitem", summary = "상호평가 문항 엑셀", description = "상호평가 문항 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/excel")
    public void getListExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Date date = new Date();
        String dateToStr = DateFormatUtils.format(date, "yyyyMMdd_HHmmSS_");

        List<BizSurveyQitemExcelVO> bizSurveyQitemExcelVOList = bizSurveyQitemService.getExcel((BizSurveyQitemViewRequestVO) params(BizSurveyQitemViewRequestVO.class, CSSearchMap.of(request), null));
        OneSheetExcelFile<BizSurveyQitemExcelVO> excelFile = new OneSheetExcelFile<>(bizSurveyQitemExcelVOList, BizSurveyQitemExcelVO.class);
        response.setHeader("Content-Disposition", "attachment; filename=" + dateToStr+URLEncoder.encode("상호평가문항", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");
        excelFile.write(response.getOutputStream());
    }
}
