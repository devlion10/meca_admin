package kr.or.kpf.lms.biz.user.authority.menu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.education.reference.vo.request.ReferenceRoomApiRequestVO;
import kr.or.kpf.lms.biz.education.reference.vo.response.ReferenceRoomApiResponseVO;
import kr.or.kpf.lms.biz.user.authority.menu.service.AuthorityMenuService;
import kr.or.kpf.lms.biz.user.authority.menu.vo.request.AuthorityMenuApiRequestVO;
import kr.or.kpf.lms.biz.user.authority.menu.vo.request.AuthorityMenuViewRequestVO;
import kr.or.kpf.lms.biz.user.authority.menu.vo.response.AuthorityMenuApiResponseVO;
import kr.or.kpf.lms.biz.user.authority.service.AuthorityService;
import kr.or.kpf.lms.biz.user.authority.vo.request.AuthorityApiRequestVO;
import kr.or.kpf.lms.biz.user.authority.vo.response.AuthorityApiResponseVO;
import kr.or.kpf.lms.biz.user.instructor.vo.request.InstructorViewRequestVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 사용자 관리 > 권한 관리 API 관련 Controller
 */
@Tag(name = "User Management", description = "사용자 관리 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/user/authority/menu")
public class AuthorityMenuApiController extends CSApiControllerSupport {
    
    private final AuthorityMenuService authorityMenuService;

    /**
     * 권한 메뉴 조회
     *
     * @param request
     * @param response
     * @param pageable
     * @return
     */
    @Tag(name = "User Management", description = "사용자 관리 API")
    @Operation(operationId="Authority Menu", summary = "권한 메뉴 조회", description = "권한 메뉴 조회한다.")
    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getList(HttpServletRequest request, HttpServletResponse response, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> authorityMenuService.getList((AuthorityMenuViewRequestVO) params(AuthorityMenuViewRequestVO.class, searchMap, pageable)))
                        .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
    }

    /**
     * 권한 메뉴 생성
     *
     * @param request
     * @param response
     * @param requestVOs
     * @return
     */
    @Tag(name = "User Management", description = "사용자 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "권한 메뉴 생성 성공", content = @Content(schema = @Schema(implementation = AuthorityApiResponseVO.class)))})
    @Operation(operationId = "Authority Menu", summary = "권한 메뉴 생성", description = "권한 메뉴를 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthorityMenuApiResponseVO> createAuthority(HttpServletRequest request, HttpServletResponse response,
                                                                      @Validated(value = {CreateAuthorityMenu.class}) @NotNull @RequestBody List<AuthorityMenuApiRequestVO> requestVOs) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(authorityMenuService.createInfo(requestVOs))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR8001, "권한 생성 실패")));
    }

    public interface CreateAuthorityMenu {}

    /**
     * 권한 메뉴 삭제
     *
     * @param request
     * @param response
     * @param requestVO
     * @return
     */
    @Tag(name = "User Management", description = "사용자 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "권한 메뉴 삭제 성공", content = @Content(schema = @Schema(implementation = ReferenceRoomApiResponseVO.class)))})
    @Operation(operationId="Authority Menu", summary = "권한 메뉴 삭제", description = "권한 메뉴를 삭제한다.")
    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteInfo(HttpServletRequest request, HttpServletResponse response,
                                                          @NotNull @RequestBody AuthorityMenuApiRequestVO requestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(authorityMenuService.deleteInfo(requestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR8004, "권한 메뉴 정보 삭제 실패")));
    }
}
