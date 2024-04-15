package kr.or.kpf.lms.biz.statistics.education.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.statistics.education.service.EducationStateService;
import kr.or.kpf.lms.biz.statistics.privacy.service.PrivacyService;
import kr.or.kpf.lms.biz.statistics.privacy.vo.request.PrivacyRequestVO;
import kr.or.kpf.lms.biz.statistics.privacy.vo.response.PrivacyResponseVO;
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
@RequestMapping(value = "/api/statistics/education")
public class EducationStateApiController extends CSApiControllerSupport {

    private final EducationStateService educationStateService;

    /**
     * 관리자 접속 이력 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Statistics Management", description = "통계 관리 API")
    @Operation(operationId = "EducationState", summary = "학습 운영 통계 엑셀", description = "학습 운영 통계 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/excel")
    public void getExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String dateToStr = DateFormatUtils.format(new Date(), "yyyyMMdd_HHmmSS_");
        CSSearchMap csSearchMap = CSSearchMap.of(request);
    }
}