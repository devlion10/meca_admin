package kr.or.kpf.lms.biz.statistics.privacy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 * 통계 관리 > 관리자 접속 이력 API 관련 Controller
 */
@Tag(name = "Statistics Management", description = "통계 관리 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/statistics/privacy")
public class PrivacyApiController extends CSApiControllerSupport {

    private final PrivacyService privacyService;

    /**
     * 관리자 접속 이력 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Statistics Management", description = "통계 관리 API")
    @Operation(operationId = "Privacy", summary = "개인정보 수정이력 엑셀", description = "개인정보 수정이력 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/excel")
    public void getExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String dateToStr = DateFormatUtils.format(new Date(), "yyyyMMdd_HHmmSS_");
        CSSearchMap csSearchMap = CSSearchMap.of(request);

        List<PrivacyResponseVO> privacyResponseVOS = privacyService.getExcel((PrivacyRequestVO) params(PrivacyRequestVO.class, csSearchMap, null));
        OneSheetExcelFile<PrivacyResponseVO> excelFile = new OneSheetExcelFile<>(privacyResponseVOS, PrivacyResponseVO.class);
        response.setHeader("Content-Disposition", "attachment; filename=" + dateToStr + URLEncoder.encode("개인정보 수정이력", StandardCharsets.UTF_8) + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");
        excelFile.write(response.getOutputStream());
    }
}