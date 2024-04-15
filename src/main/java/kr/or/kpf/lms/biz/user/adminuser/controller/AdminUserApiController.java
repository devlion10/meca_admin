package kr.or.kpf.lms.biz.user.adminuser.controller;

import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.user.adminuser.service.AdminUserService;
import kr.or.kpf.lms.biz.user.adminuser.vo.request.AdminUserApiRequestVO;
import kr.or.kpf.lms.biz.user.adminuser.vo.request.AdminUserViewRequestVO;
import kr.or.kpf.lms.biz.user.adminuser.vo.response.AdminUserApiResponseVO;
import kr.or.kpf.lms.biz.user.adminuser.vo.response.AdminUserExcelVO;
import kr.or.kpf.lms.biz.user.webuser.vo.response.WebUserApiResponseVO;
import kr.or.kpf.lms.common.exceldownload.excel.OneSheetExcelFile;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 사용자 관리 > 어드민 계정 관리 API 관련 Controller
 */
@Tag(name = "User Management", description = "사용자 관리 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/user/admin-user")
public class AdminUserApiController extends CSApiControllerSupport {

    private final AdminUserService adminUserService;

    /**
     * 어드민 계정 조회
     *
     * @param request
     * @return
     */
    @Tag(name = "User Management", description = "사용자 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "어드민 정보 조회", content = @Content(schema = @Schema(implementation = AdminUserApiResponseVO.class)))})
    @Operation(operationId="AdminUser", summary = "어드민 정보 조회", description = "어드민 정보 조회")
    @GetMapping(path = {"", "/"})
    public ResponseEntity<Object> getUserList(HttpServletRequest request, Pageable pageable, Model model) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> resultPaging(adminUserService.getList((AdminUserViewRequestVO) params(AdminUserViewRequestVO.class, searchMap, pageable)), new ArrayList<>()))
                        .orElse(new HashMap<>()));
    }

    /**
     * 어드민 계정 생성
     *
     * @param request
     * @param response
     * @param adminUserApiRequestVO
     * @return
     */
    @Tag(name = "User Management", description = "사용자 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "어드민 계정 생성 성공", content = @Content(schema = @Schema(implementation = AdminUserApiResponseVO.class)))})
    @Operation(operationId="AdminUser", summary = "어드민 계정 생성", description = "어드민 계정을 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminUserApiResponseVO> createUserInfo(HttpServletRequest request, HttpServletResponse response,
                                                                 @Validated(value = {CreateAdminUser.class}) @NotNull @RequestBody AdminUserApiRequestVO adminUserApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(adminUserService.createUserInfo(adminUserApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1001, "어드민 계정 생성 실패")));
    }

    public interface CreateAdminUser {}

    /**
     * 어드민 계정 업데이트
     *
     * @param request
     * @param response
     * @param adminUserApiRequestVO
     * @return
     */
    @Tag(name = "User Management", description = "사용자 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "어드민 계정 수정 성공", content = @Content(schema = @Schema(implementation = AdminUserApiResponseVO.class)))})
    @Operation(operationId="AdminUser", summary = "어드민 계정 업데이트", description = "어드민 계정를 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminUserApiResponseVO> updateUserInfo(HttpServletRequest request, HttpServletResponse response,
                                                                 @Validated(value = {UpdateAdminUser.class}) @NotNull @RequestBody AdminUserApiRequestVO adminUserApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(adminUserService.updateUserInfo(adminUserApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1003, "어드민 계정 수정 실패")));
    }

    public interface UpdateAdminUser {}

    /**
     * 어드민 계정 삭제(회원 탈퇴)
     *
     * @param request
     * @param response
     * @param adminUserApiRequestVO
     * @return
     */
    @Tag(name = "User Management", description = "사용자 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "어드민 계정 삭제(회원 탈퇴) 성공", content = @Content(schema = @Schema(implementation = AdminUserApiResponseVO.class)))})
    @Operation(operationId="AdminUser", summary = "어드민 계정 삭제(회원 탈퇴)", description = "어드민 계정 삭제(회원 탈퇴) 한다.")
    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteUserInfo(HttpServletRequest request, HttpServletResponse response,
                                                              @Validated(value = {DeleteAdminUser.class}) @NotNull @RequestBody AdminUserApiRequestVO adminUserApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(adminUserService.deleteUserInfo(adminUserApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1003, "어드민 계정 삭제 실패")));
    }

    public interface DeleteAdminUser {}

    /**
     * 비밀번호 변경 (비밀번호 분실 시...)
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "User Management", description = "사용자 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId="AdminUser", summary = "비밀번호 변경", description = "비밀번호를 변경한다.")
    @PutMapping(value = "/change-password", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> changePassword(HttpServletRequest request, HttpServletResponse response,
                                                              @Validated(value = {ChangeAdminPassword.class}) @NotNull @RequestBody AdminUserApiRequestVO adminUserApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(adminUserService.changePassword(adminUserApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1003, "어드민 계정 수정 실패")));
    }

    public interface ChangeAdminPassword {}

    /**
     * 관리자 정보 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "User Management", description = "사용자 관리 API")
    @Operation(operationId="AdminUser", summary = "관리자 정보 엑셀", description = "관리자 정보 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/excel")
    public void getListExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Date date = new Date();
        String dateToStr = DateFormatUtils.format(date, "yyyyMMdd_HHmmSS_");

        List<AdminUserExcelVO> adminUserExcelVOList = adminUserService.getExcel((AdminUserViewRequestVO) params(AdminUserViewRequestVO.class, CSSearchMap.of(request), null));
        OneSheetExcelFile<AdminUserExcelVO> excelFile = new OneSheetExcelFile<>(adminUserExcelVOList, AdminUserExcelVO.class);
        response.setHeader("Content-Disposition", "attachment; filename=" +dateToStr+ URLEncoder.encode("관리자정보", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");

        excelFile.write(response.getOutputStream());
    }
}
