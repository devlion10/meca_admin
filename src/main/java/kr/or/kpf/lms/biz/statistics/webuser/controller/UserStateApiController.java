package kr.or.kpf.lms.biz.statistics.webuser.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.statistics.adminuser.service.StatisticsAdminUserService;
import kr.or.kpf.lms.biz.statistics.adminuser.vo.StatisticsAdminUserExcelVO;
import kr.or.kpf.lms.biz.statistics.adminuser.vo.request.StatisticsAdminUserViewRequestVO;
import kr.or.kpf.lms.biz.statistics.webuser.service.UserStateService;
import kr.or.kpf.lms.biz.statistics.webuser.vo.request.UserStateRequestVO;
import kr.or.kpf.lms.biz.statistics.webuser.vo.response.UserStateResponseVO;
import kr.or.kpf.lms.common.exceldownload.excel.OneSheetExcelFile;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import kr.or.kpf.lms.repository.entity.statistics.WebUserStateHistorySummary;
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
 * 통계 관리 > 이용통계 API 관련 Controller
 */
@Tag(name = "Statistics Management", description = "통계 관리 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/statistics/web-user")
public class UserStateApiController extends CSApiControllerSupport {

    private final UserStateService userStateService;

    /**
     * 관리자 접속 이력 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Statistics Management", description = "통계 관리 API")
    @Operation(operationId = "UserState", summary = "이용통계 엑셀", description = "이용통계 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/excel")
    public void getExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String dateToStr = DateFormatUtils.format(new Date(), "yyyyMMdd_HHmmSS_");
        CSSearchMap csSearchMap = CSSearchMap.of(request);

        List<WebUserStateHistorySummary> webUserStateHistorySummaries = userStateService.getExcel((UserStateRequestVO) params(UserStateRequestVO.class, csSearchMap, null));
        OneSheetExcelFile<WebUserStateHistorySummary> excelFile = new OneSheetExcelFile<>(webUserStateHistorySummaries, WebUserStateHistorySummary.class);
        response.setHeader("Content-Disposition", "attachment; filename=" + dateToStr + URLEncoder.encode("이용통계", StandardCharsets.UTF_8) + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");
        excelFile.write(response.getOutputStream());
    }
}