package kr.or.kpf.lms.biz.statistics.survey.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.statistics.education.service.EducationStateService;
import kr.or.kpf.lms.biz.statistics.evaluation.vo.request.EvaluateQuestionStatisticsRequestVO;
import kr.or.kpf.lms.biz.statistics.evaluation.vo.response.EvaluateQuestionStatisticsViewResponseVO;
import kr.or.kpf.lms.biz.statistics.privacy.vo.request.PrivacyRequestVO;
import kr.or.kpf.lms.biz.statistics.privacy.vo.response.PrivacyResponseVO;
import kr.or.kpf.lms.biz.statistics.survey.service.SurveyStateService;
import kr.or.kpf.lms.biz.statistics.survey.vo.request.SurveyApplyStateRequestVO;
import kr.or.kpf.lms.biz.statistics.survey.vo.request.SurveyQuestionStateRequestVO;
import kr.or.kpf.lms.biz.statistics.survey.vo.response.SurveyApplyStateResponseVO;
import kr.or.kpf.lms.biz.statistics.survey.vo.response.SurveyQuestionStateResponseVO;
import kr.or.kpf.lms.common.exceldownload.excel.OneSheetExcelFile;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
 * 통계 관리 > 학습 운영 통계 API 관련 Controller
 */
@Tag(name = "Statistics Management", description = "통계 관리 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/statistics/survey")
public class SurveyStateApiController extends CSApiControllerSupport {

    private final SurveyStateService surveyStateService;

    /**
     * 상호평가 통계 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Statistics Management", description = "통계 관리 API")
    @Operation(operationId = "Survey", summary = "상호평가 통계", description = "상호평가 통계 - 평가지별 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/question/excel")
    public void getQuestionExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Date date = new Date();
        String dateToStr = DateFormatUtils.format(date, "yyyyMMdd_HHmmSS_");
        List<SurveyQuestionStateResponseVO> evaluateExcelVOList = surveyStateService.getQuestionExcel((SurveyQuestionStateRequestVO) params(SurveyQuestionStateRequestVO.class, CSSearchMap.of(request), null));
        OneSheetExcelFile<SurveyQuestionStateResponseVO> excelFile = new OneSheetExcelFile<>(evaluateExcelVOList, SurveyQuestionStateResponseVO.class);

        response.setHeader("Content-Disposition", "attachment; filename=" + dateToStr+ URLEncoder.encode("평가지별", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");
        excelFile.write(response.getOutputStream());
    }

    @Tag(name = "Statistics Management", description = "통계 관리 API")
    @Operation(operationId = "Survey", summary = "상호평가 통계", description = "상호평가 통계 - 신청서별 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/apply/excel")
    public void getApplyExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Date date = new Date();
        String dateToStr = DateFormatUtils.format(date, "yyyyMMdd_HHmmSS_");
        List<SurveyApplyStateResponseVO> evaluateExcelVOList = surveyStateService.getApplyExcel((SurveyApplyStateRequestVO) params(SurveyApplyStateRequestVO.class, CSSearchMap.of(request), null));
        OneSheetExcelFile<SurveyApplyStateResponseVO> excelFile = new OneSheetExcelFile<>(evaluateExcelVOList, SurveyApplyStateResponseVO.class);

        response.setHeader("Content-Disposition", "attachment; filename=" + dateToStr+ URLEncoder.encode("신청서별", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");
        excelFile.write(response.getOutputStream());
    }
}