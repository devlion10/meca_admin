package kr.or.kpf.lms.biz.statistics.adminuser.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.education.application.vo.request.EducationApplicationViewRequestVO;
import kr.or.kpf.lms.biz.education.application.vo.response.EducationCompleteExcelVO;
import kr.or.kpf.lms.biz.statistics.adminuser.service.StatisticsAdminUserService;
import kr.or.kpf.lms.biz.statistics.adminuser.vo.StatisticsAdminUserExcelVO;
import kr.or.kpf.lms.biz.statistics.adminuser.vo.request.StatisticsAdminUserViewRequestVO;
import kr.or.kpf.lms.common.exceldownload.excel.OneSheetExcelFile;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.common.support.Code;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import kr.or.kpf.lms.repository.entity.statistics.AdminAccessHistory;
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
@RequestMapping(value = "/api/statistics/admin-user")
public class StatisticsAdminUserApiController extends CSApiControllerSupport {

    private final StatisticsAdminUserService statisticsAdminUserService;

    /**
     * 관리자 접속 이력 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Statistics Management", description = "통계 관리 API")
    @Operation(operationId = "AdminUserHistory", summary = "관리자 접속 이력 엑셀", description = "관리자 접속 이력 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/excel")
    public void getExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String dateToStr = DateFormatUtils.format(new Date(), "yyyyMMdd_HHmmSS_");
        CSSearchMap csSearchMap = CSSearchMap.of(request);

        List<StatisticsAdminUserExcelVO> statisticsAdminUserExcelVOS = statisticsAdminUserService.getExcel((StatisticsAdminUserViewRequestVO) params(StatisticsAdminUserViewRequestVO.class, csSearchMap, null));
        OneSheetExcelFile<StatisticsAdminUserExcelVO> excelFile = new OneSheetExcelFile<>(statisticsAdminUserExcelVOS, StatisticsAdminUserExcelVO.class);
        response.setHeader("Content-Disposition", "attachment; filename=" + dateToStr + URLEncoder.encode("관리자접속이력", StandardCharsets.UTF_8) + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");
        excelFile.write(response.getOutputStream());
    }
}
