package kr.or.kpf.lms.biz.user.webauthority.controller;


import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.business.survey.qitem.vo.request.BizSurveyQitemViewRequestVO;
import kr.or.kpf.lms.biz.user.webauthority.service.WebAuthorityService;
import kr.or.kpf.lms.biz.user.webauthority.vo.request.WebAuthorityApiRequestVO;
import kr.or.kpf.lms.biz.user.webauthority.vo.request.WebAuthorityOrgApiVO;
import kr.or.kpf.lms.biz.user.webauthority.vo.request.WebAuthorityViewRequestVO;
import kr.or.kpf.lms.biz.user.webauthority.vo.response.WebAuthorityApiResponseVO;
import kr.or.kpf.lms.biz.user.webauthority.vo.response.WebAuthorityExcelVO;
import kr.or.kpf.lms.biz.user.webuser.vo.response.WebUserApiResponseVO;
import kr.or.kpf.lms.common.exceldownload.excel.OneSheetExcelFile;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.data.domain.Page;
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

/**
 * 사용자 관리 > 권한 관리 API 관련 Controller
 */
@Tag(name = "User Management", description = "사용자 관리 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/user/web/authority")
public class WebAuthorityApiController extends CSApiControllerSupport {

    private final WebAuthorityService webAuthorityService;

    /**
     * 사업 참여 권한 조회
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "User Management", description = "사용자 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사업 참여 권한 조회", content = @Content(schema = @Schema(implementation = WebAuthorityApiResponseVO.class)))})
    @Operation(operationId = "Authority", summary = "사업 참여 권한 조회", description = "사업 참여 권한 조회한다.")
    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Object>> getList(HttpServletRequest request, HttpServletResponse response, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> webAuthorityService.getList((WebAuthorityViewRequestVO) params(WebAuthorityViewRequestVO.class, searchMap, pageable)))
                        .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
    }

    /**
     * 사업 참여 권한 승인 상태 업데이트
     *
     * @param request
     * @param response
     * @param webAuthorityApiRequestVO
     * @return
     */
    @Tag(name = "User Management", description = "사용자 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사업 참여 권한 승인 상태 업데이트 성공", content = @Content(schema = @Schema(implementation = WebAuthorityApiResponseVO.class)))})
    @Operation(operationId = "Authority", summary = "사업 참여 권한 승인 상태 업데이트", description = "사업 참여 권한 승인 상태 업데이트한다.")
    @PutMapping(value = "/approval-state", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebAuthorityApiResponseVO> updateBusinessAuthorityApprovalState(HttpServletRequest request, HttpServletResponse response,
                                                                  @Validated(value = {UpdateWebAuthority.class}) @NotNull @RequestBody WebAuthorityApiRequestVO webAuthorityApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(webAuthorityService.updateBusinessAuthorityApprovalState(webAuthorityApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR8003, "사업 참여 권한 승인 상태 업데이트 실패")));
    }

    public interface UpdateWebAuthority {}

    /**
     * 사업 참여 권한 해제
     *
     * @param request
     * @param response
     * @param webAuthorityApiRequestVO
     * @return
     */
    @Tag(name = "User Management", description = "사용자 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사업 참여 권한 해제 성공", content = @Content(schema = @Schema(implementation = WebUserApiResponseVO.class)))})
    @Operation(operationId="Authority", summary = "사업 참여 권한 해제", description = "사업 참여 권한 해제한다.")
    @PutMapping(value = "/remove", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebAuthorityApiResponseVO> removeBusinessAuthority(HttpServletRequest request, HttpServletResponse response,
                                                                             @Validated(value = {DeleteWebAuthority.class}) @NotNull @RequestBody WebAuthorityApiRequestVO webAuthorityApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(webAuthorityService.removeBusinessAuthority(webAuthorityApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR8003, "웹 회원 사업 참여 권한 해제 실패")));
    }

    /**
     * 사업 참여 권한 기관변경
     *
     * @param request
     * @param response
     * @param webAuthorityOrgApiVO
     * @return
     */
    @Tag(name = "User Management", description = "사용자 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사업 참여 권한 기관변경 성공", content = @Content(schema = @Schema(implementation = WebUserApiResponseVO.class)))})
    @Operation(operationId="Authority", summary = "사업 참여 권한 기관변경", description = "사업 참여 권한 기관을 변경한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebAuthorityApiResponseVO> updateBusinessAuthority(HttpServletRequest request, HttpServletResponse response,
                                                                             @Validated(value = {DeleteWebAuthority.class}) @NotNull @RequestBody WebAuthorityOrgApiVO webAuthorityOrgApiVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(webAuthorityService.updateBusinessAuthority(webAuthorityOrgApiVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR8003, "웹 회원 사업 참여 권한 수정 실패")));
    }
    public interface DeleteWebAuthority {}

    /**
     * 권한신청 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "User Management", description = "사용자 관리 API")
    @Operation(operationId="Authority", summary = "권한신청 엑셀", description = "권한신청 파일을 다운로드한다.")
    @GetMapping(value = "/excel")
    public void getListExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Date date = new Date();
        String dateToStr = DateFormatUtils.format(date, "yyyyMMdd_HHmmSS_");

        List<WebAuthorityExcelVO> webAuthorityExcelVOList = webAuthorityService.getExcel((WebAuthorityViewRequestVO) params(WebAuthorityViewRequestVO.class, CSSearchMap.of(request), null));
        OneSheetExcelFile<WebAuthorityExcelVO> excelFile = new OneSheetExcelFile<>(webAuthorityExcelVOList, WebAuthorityExcelVO.class);
        response.setHeader("Content-Disposition", "attachment; filename=" +dateToStr+ URLEncoder.encode("권한신청", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");

        excelFile.write(response.getOutputStream());
    }

}
