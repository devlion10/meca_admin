package kr.or.kpf.lms.biz.user.webuser.controller;

import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.education.curriculum.vo.request.CurriculumViewRequestVO;
import kr.or.kpf.lms.biz.user.organization.vo.request.OrganizationViewRequestVO;
import kr.or.kpf.lms.biz.user.webuser.service.WebUserService;
import kr.or.kpf.lms.biz.user.webuser.vo.ChangePassword;
import kr.or.kpf.lms.biz.user.webuser.vo.request.WebUserApiRequestVO;
import kr.or.kpf.lms.biz.user.webuser.vo.request.WebUserViewRequestVO;
import kr.or.kpf.lms.biz.user.webuser.vo.response.WebUserApiResponseVO;
import kr.or.kpf.lms.biz.user.webuser.vo.response.WebUserExcelVO;
import kr.or.kpf.lms.common.exceldownload.excel.OneSheetExcelFile;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 사용자 관리 > 웹 회원 관리 API 관련 Controller
 */
@Tag(name = "User Management", description = "사용자 관리 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/user/web-user")
public class WebUserApiController extends CSApiControllerSupport {

    private final WebUserService webUserService;

    @Tag(name = "User Management", description = "사용자 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "웹 회원 정보 조회", content = @Content(schema = @Schema(implementation = WebUserApiResponseVO.class)))})
    @Operation(operationId="WebUser", summary = "웹 회원 정보 조회", description = "웹 회원 정보 조회")
    @GetMapping(path = {"", "/"})
    public ResponseEntity<Object> getUserList(HttpServletRequest request, Pageable pageable, Model model) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> resultPaging(webUserService.getList((WebUserViewRequestVO) params(WebUserViewRequestVO.class, searchMap, pageable)), new ArrayList<>()))
                        .orElse(new HashMap<>()));
    }


    /**
     * 웹 회원 정보 업데이트
     *
     * @param request
     * @param response
     * @param webUserApiRequestVO
     * @return
     */
    @Tag(name = "User Management", description = "사용자 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "웹 회원 정보 수정 성공", content = @Content(schema = @Schema(implementation = WebUserApiResponseVO.class)))})
    @Operation(operationId="WebUser", summary = "웹 회원 정보 업데이트", description = "웹 회원 정보를 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebUserApiResponseVO> updateUserInfo(HttpServletRequest request, HttpServletResponse response,
                                                               @Validated(value = {UpdateWebUser.class}) @NotNull @RequestBody WebUserApiRequestVO webUserApiRequestVO) {

        logger.info("updateUserInfo____");
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(webUserService.updateUserInfo(webUserApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1003, "웹 회원 정보 수정 실패")));
    }

    public interface UpdateWebUser {}

    /**
     * 비밀번호 변경 (비밀번호 분실 시...)
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "User Management", description = "유저 정보 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "WebUser", summary = "비밀번호 변경", description = "비밀번호를 변경한다.")
    @PutMapping(value = "/change-password", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> changePassword(HttpServletRequest request, HttpServletResponse response,
                                                              @Validated(value = {ChangePassword.class}) @NotNull @RequestBody WebUserApiRequestVO webUserApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(webUserService.changePassword(webUserApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1003, "회원 정보 수정 실패")));
    }

    /**
     * 웹 회원 권한해제
     *
     * @param request
     * @param response
     * @param webUserApiRequestVO
     * @return
     */
    @Tag(name = "User Management", description = "사용자 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "웹 회원 권한해제", content = @Content(schema = @Schema(implementation = WebUserApiResponseVO.class)))})
    @Operation(operationId="WebUser", summary = "웹 회원 권한해제", description = "웹 회원 정보를 권한해제한다.")
    @PutMapping(value = "/clear", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebUserApiResponseVO> removeUserAuth(HttpServletRequest request, HttpServletResponse response,
                                                               @Validated(value = {UpdateWebUser.class}) @NotNull @RequestBody WebUserApiRequestVO webUserApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(webUserService.removeUserAuth(webUserApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1003, "웹 회원 권한해제 실패")));
    }

    /**
     * 웹 회원 잠금해제
     *
     * @param request
     * @param response
     * @param userId
     * @return
     */
    @Tag(name = "User Management", description = "사용자 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "웹 회원 잠금해제", content = @Content(schema = @Schema(implementation = WebUserApiResponseVO.class)))})
    @Operation(operationId="WebUser", summary = "웹 회원 잠금해제", description = "웹 회원 정보를 잠금해제한다.")
    @PutMapping(value = "/unlock/{userId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebUserApiResponseVO> unlockUserInfo(HttpServletRequest request, HttpServletResponse response,
                                                               @Parameter(description = "회원 아이디") @PathVariable(value = "userId", required = true) String userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(webUserService.unlockUserInfo(userId))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1003, "웹 회원 잠금해제 실패")));
    }

    /**
     * 기관 정보 조회 API
     *
     * @param request
     * @param response
     * @param pageable
     * @param organizationCode
     * @param organizationName
     * @return
     */
    @Tag(name = "User Management", description = "유저 정보 API")
    @Operation(operationId = "WebUser", summary = "기관 정보 조회", description = "기관 정보 조회한다.")
    @GetMapping(value = "/organization", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getOrganizationInfo(HttpServletRequest request, HttpServletResponse response, Pageable pageable,
                                                      @RequestParam(value="organizationCode", required = false) String organizationCode,
                                                      @RequestParam(value="organizationName", required = false) String organizationName,
                                                      @RequestParam(value="organizationType", required = false) String organizationType) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> webUserService.getOrganizationInfo((OrganizationViewRequestVO) params(OrganizationViewRequestVO.class, searchMap, pageable)))
                        .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
    }

    /**
     * 재직증명서 업로드 API
     *
     * @param request
     * @param response
     * @param userId
     * @param attachFile
     * @return
     */
    @Tag(name = "User Management", description = "재직증명서 업로드 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "재직증명서 업로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "WebUser", summary = "재직증명서 업로드", description = "재직증명서 업로드한다.")
    @PutMapping(value = "/upload/{userId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CSResponseVOSupport> fileUpload(HttpServletRequest request, HttpServletResponse response,
                                                          @PathVariable(value = "userId", required = true) String userId,
                                                          @Parameter(description = "첨부파일") @RequestPart(value = "attachFile", required = true) MultipartFile attachFile) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(webUserService.fileUpload(userId, attachFile))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9005, "재직증명서 업로드 실패")));
    }


    /**
     * 회원 정보 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "User Management", description = "회원 정보 API")
    @Operation(operationId="WebUser", summary = "회원 정보 엑셀", description = "회원 정보 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/excel")
    public void getListExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Date date = new Date();
        String dateToStr = DateFormatUtils.format(date, "yyyyMMdd_HHmmSS_");

        List<WebUserExcelVO> webUserExcelVOList = webUserService.getExcel((WebUserViewRequestVO) params(WebUserViewRequestVO.class, CSSearchMap.of(request), null));
        OneSheetExcelFile<WebUserExcelVO> excelFile = new OneSheetExcelFile<>(webUserExcelVOList, WebUserExcelVO.class);
        response.setHeader("Content-Disposition", "attachment; filename=" +dateToStr+ URLEncoder.encode("회원정보", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");

        excelFile.write(response.getOutputStream());
    }

}
