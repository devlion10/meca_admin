package kr.or.kpf.lms.biz.user.organization.controller;

import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.user.instructor.vo.request.InstructorViewRequestVO;
import kr.or.kpf.lms.biz.user.instructor.vo.response.InstructorExcelVO;
import kr.or.kpf.lms.biz.user.organization.service.OrganizationService;
import kr.or.kpf.lms.biz.user.organization.vo.request.OrganizationApiRequestVO;
import kr.or.kpf.lms.biz.user.organization.vo.request.OrganizationMediaViewRequestVO;
import kr.or.kpf.lms.biz.user.organization.vo.request.OrganizationViewRequestVO;
import kr.or.kpf.lms.biz.user.organization.vo.response.OrganizationApiResponseVO;
import kr.or.kpf.lms.biz.user.organization.vo.response.OrganizationExcelVO;
import kr.or.kpf.lms.biz.user.organization.vo.response.OrganizationMediaExcelVO;
import kr.or.kpf.lms.common.exceldownload.excel.OneSheetExcelFile;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 사용자 관리 > 기관 관리 API 관련 Controller
 */
@Tag(name = "User Management", description = "사용자 관리 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/user/organization")
public class OrganizationApiController extends CSApiControllerSupport {
    
    private final OrganizationService organizationService;

    /**
     * 기관 정보 생성
     *
     * @param request
     * @param response
     * @param organizationApiRequestVO
     * @return
     */
    @Tag(name = "User Management", description = "사용자 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "기관 정보 생성", content = @Content(schema = @Schema(implementation = OrganizationApiResponseVO.class)))})
    @Operation(operationId="Organization", summary = "기관 정보 생성", description = "기관 정보를 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrganizationApiResponseVO> createOrganizationInfo(HttpServletRequest request, HttpServletResponse response,
                                                                    @Validated(value = {CreateOrganization.class}) @NotNull @RequestBody OrganizationApiRequestVO organizationApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(organizationService.createOrganizationInfo(organizationApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1201, "기관 정보 생성 실패")));
    }

    public interface CreateOrganization {}

    /**
     * 기관 정보 업데이트
     *
     * @param request
     * @param response
     * @param organizationApiRequestVO
     * @return
     */
    @Tag(name = "User Management", description = "사용자 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "기관 정보 수정 성공", content = @Content(schema = @Schema(implementation = OrganizationApiResponseVO.class)))})
    @Operation(operationId="Organization", summary = "기관 정보 업데이트", description = "기관 정보를 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrganizationApiResponseVO> updateOrganizationInfo(HttpServletRequest request, HttpServletResponse response,
                                                               @Validated(value = {UpdateOrganization.class}) @NotNull @RequestBody OrganizationApiRequestVO organizationApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(organizationService.updateOrganizationInfo(organizationApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1202, "기관 정보 수정 실패")));
    }

    public interface UpdateOrganization {}

    /**
     * 기관정보 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "User Management", description = "사용자 관리 API")
    @Operation(operationId="Organization", summary = "기관 정보 엑셀", description = "기관 정보 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/excel")
    public void getListExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Date date = new Date();
        String dateToStr = DateFormatUtils.format(date, "yyyyMMdd_HHmmSS_");

        List<OrganizationExcelVO> organizationExcelVOList = organizationService.getExcel((OrganizationViewRequestVO) params(OrganizationViewRequestVO.class, CSSearchMap.of(request), null));
        OneSheetExcelFile<OrganizationExcelVO> excelFile = new OneSheetExcelFile<>(organizationExcelVOList, OrganizationExcelVO.class);
        response.setHeader("Content-Disposition", "attachment; filename=" +dateToStr+ URLEncoder.encode("기관정보", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");

        excelFile.write(response.getOutputStream());
    }

    /**
     * 매체정보 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "User Management", description = "사용자 관리 API")
    @Operation(operationId="Organization", summary = "매체 정보 엑셀", description = "매체 정보 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/media/excel")
    public void getMediaExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Date date = new Date();
        String dateToStr = DateFormatUtils.format(date, "yyyyMMdd_HHmmSS_");

        List<OrganizationMediaExcelVO> excelVOList = organizationService.getMediaExcel((OrganizationMediaViewRequestVO) params(OrganizationMediaViewRequestVO.class, CSSearchMap.of(request), null));
        OneSheetExcelFile<OrganizationMediaExcelVO> excelFile = new OneSheetExcelFile<>(excelVOList, OrganizationMediaExcelVO.class);
        response.setHeader("Content-Disposition", "attachment; filename=" +dateToStr+ URLEncoder.encode("매체정보", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");

        excelFile.write(response.getOutputStream());
    }
}
