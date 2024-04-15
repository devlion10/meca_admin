package kr.or.kpf.lms.biz.statistics.evaluation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.statistics.evaluation.service.EvaluateStatisticsService;
import kr.or.kpf.lms.biz.statistics.evaluation.vo.request.EvaluateCurriculumStatisticsRequestVO;
import kr.or.kpf.lms.biz.statistics.evaluation.vo.request.EvaluateQuestionStatisticsRequestVO;
import kr.or.kpf.lms.biz.statistics.evaluation.vo.response.EvaluateCurriculumStatisticsViewResponseVO;
import kr.or.kpf.lms.biz.statistics.evaluation.vo.response.EvaluateQuestionStatisticsViewResponseVO;
import kr.or.kpf.lms.common.exceldownload.excel.OneSheetExcelFile;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 통계 관리 > 강의 평가 관리 View 관련 Controller
 */
@Tag(name = "evaluationState Management", description = "통계 관리 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/statistics/evaluation")
public class EvaluationStateApiController extends CSViewControllerSupport {

    private final EvaluateStatisticsService evaluateStatisticsService;

    /**
     * 설문지별 답변 조회 API
     *
     * @param request
     * @param response
     * @param pageable
     * @return
     */
    @Tag(name = "evaluationState Management", description = "통계 관리 API")
    @Operation(operationId="evaluationState", summary = "강의평가 통계 엑셀", description = "강의평가 통계 - 설문지별 답변을 조회한다.")
    @GetMapping(value = "/{evaluateSerialNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getQuestionList(HttpServletRequest request, HttpServletResponse response, Pageable pageable,
                                          @Parameter(description = "강의 평가 일련 번호") @PathVariable(value = "evaluateSerialNo", required = true) String evaluateSerialNo) {

        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("evaluateSerialNo", evaluateSerialNo);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(requestParam)
                        .map(searchMap -> evaluateStatisticsService.getQuestionStatisticsDetail((EvaluateQuestionStatisticsRequestVO) params(EvaluateQuestionStatisticsRequestVO.class, searchMap, pageable)))
                        .orElse(new ArrayList<>()));
    }

    /**
     * 강좌(과정)별 답변 조회 API
     *
     * @param request
     * @param response
     * @param pageable
     * @return
     */
    @Tag(name = "evaluationState Management", description = "통계 관리 API")
    @Operation(operationId="evaluationState", summary = "강의평가 통계 엑셀", description = "강의평가 통계 - 강좌(과정)별 답변을 조회한다.")
    @GetMapping(value = "/education/{curriculumCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getList(HttpServletRequest request, HttpServletResponse response, Pageable pageable,
                                          @Parameter(description = "과정 코드") @PathVariable(value = "curriculumCode", required = true) String curriculumCode) {

        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("curriculumCode", curriculumCode);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(requestParam)
                        .map(searchMap -> evaluateStatisticsService.getCurriculumStatisticsDetail((EvaluateCurriculumStatisticsRequestVO) params(EvaluateCurriculumStatisticsRequestVO.class, searchMap, pageable)))
                        .orElse(new ArrayList<>()));
    }
    
    /**
     * 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "evaluationState Management", description = "통계 관리 API")
    @Operation(operationId="evaluationState", summary = "강의평가 통계 엑셀", description = "강의평가 통계 - 설문지별 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/question/excel")
    public void getQuestionExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Date date = new Date();
        String dateToStr = DateFormatUtils.format(date, "yyyyMMdd_HHmmSS_");
        List<EvaluateQuestionStatisticsViewResponseVO> evaluateExcelVOList = evaluateStatisticsService.getQuestionExcel((EvaluateQuestionStatisticsRequestVO) params(EvaluateQuestionStatisticsRequestVO.class, CSSearchMap.of(request), null));
        OneSheetExcelFile<EvaluateQuestionStatisticsViewResponseVO> excelFile = new OneSheetExcelFile<>(evaluateExcelVOList, EvaluateQuestionStatisticsViewResponseVO.class);

        response.setHeader("Content-Disposition", "attachment; filename=" + dateToStr+ URLEncoder.encode("설문지별", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");
        excelFile.write(response.getOutputStream());
    }

    @Tag(name = "evaluationState Management", description = "통계 관리 API")
    @Operation(operationId="evaluationState", summary = "강의평가 통계 엑셀", description = "강의평가 통계 - 강의(과정)별 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/curriculum/excel")
    public void getCurriculumExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Date date = new Date();
        String dateToStr = DateFormatUtils.format(date, "yyyyMMdd_HHmmSS_");
        List<EvaluateCurriculumStatisticsViewResponseVO> evaluateExcelVOList = evaluateStatisticsService.getCurriculumExcel((EvaluateCurriculumStatisticsRequestVO) params(EvaluateCurriculumStatisticsRequestVO.class, CSSearchMap.of(request), null));
        OneSheetExcelFile<EvaluateCurriculumStatisticsViewResponseVO> excelFile = new OneSheetExcelFile<>(evaluateExcelVOList, EvaluateCurriculumStatisticsViewResponseVO.class);

        response.setHeader("Content-Disposition", "attachment; filename=" + dateToStr+ URLEncoder.encode("설문지별", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");
        excelFile.write(response.getOutputStream());
    }
}
