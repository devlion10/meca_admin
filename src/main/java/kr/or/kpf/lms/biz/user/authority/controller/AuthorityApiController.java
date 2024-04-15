package kr.or.kpf.lms.biz.user.authority.controller;

import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.user.authority.service.AuthorityService;
import kr.or.kpf.lms.biz.user.authority.vo.request.AuthorityApiRequestVO;
import kr.or.kpf.lms.biz.user.authority.vo.response.AuthorityApiResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * 사용자 관리 > 권한 관리 API 관련 Controller
 */
@Tag(name = "User Management", description = "사용자 관리 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/user/authority")
public class AuthorityApiController extends CSApiControllerSupport {
    
    private final AuthorityService authorityService;

    /**
     * 권한 생성
     *
     * @param request
     * @param response
     * @param authorityApiRequestVO
     * @return
     */
    @Tag(name = "User Management", description = "사용자 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "권한 생성 성공", content = @Content(schema = @Schema(implementation = AuthorityApiResponseVO.class)))})
    @Operation(operationId = "Authority", summary = "권한 생성", description = "권한를 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthorityApiResponseVO> createAuthority(HttpServletRequest request, HttpServletResponse response,
                                                                  @Validated(value = {CreateAuthority.class}) @NotNull @RequestBody AuthorityApiRequestVO authorityApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(authorityService.createAuthorityGroup(authorityApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR8001, "권한 생성 실패")));
    }

    public interface CreateAuthority {}

    /**
     * 권한 업데이트
     *
     * @param request
     * @param response
     * @param authorityApiRequestVO
     * @return
     */
    @Tag(name = "User Management", description = "사용자 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "권한 수정 성공", content = @Content(schema = @Schema(implementation = AuthorityApiResponseVO.class)))})
    @Operation(operationId="Authority", summary = "권한 업데이트", description = "권한을 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthorityApiResponseVO> updateAuthority(HttpServletRequest request, HttpServletResponse response,
                                                                      @Validated(value = {UpdateAuthority.class}) @NotNull @RequestBody AuthorityApiRequestVO authorityApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(authorityService.updateAuthorityGroup(authorityApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR8003, "권한 수정 실패")));
    }

    public interface UpdateAuthority {}
    public interface CreateAuthorityGroup {}
    public interface UpdateAuthorityGroup {}
}
