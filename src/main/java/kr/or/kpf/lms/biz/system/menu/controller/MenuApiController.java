package kr.or.kpf.lms.biz.system.menu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.system.menu.service.MenuService;
import kr.or.kpf.lms.biz.system.menu.vo.request.MenuApiRequestVO;
import kr.or.kpf.lms.biz.system.menu.vo.response.MenuApiResponseVO;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * 시스템 관리 > 메뉴 관리 API 관련 Controller
 */
@Tag(name = "System Management", description = "시스템 관리 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/system/menu")
public class MenuApiController extends CSApiControllerSupport {

    private final MenuService menuService;

    /**
     * Sample (객체 Swagger 표출을 위해서 사용)
     *
     * @param request
     * @param response
     * @param educationApplicationApiRequestVO
     * @return
     */
    @Tag(name = "Swagger USE(NOT API)", description = "사용하지 않는 API(객체 표출용)")
    @PostMapping(value = "/sample", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuApiResponseVO> swaggerUse(HttpServletRequest request, HttpServletResponse response, @RequestBody MenuApiRequestVO educationApplicationApiRequestVO) {
        return null;
    }

    /**
     * 메뉴 생성 API
     *
     * @param request
     * @param response
     * @param menuApiRequestVO
     * @return
     */
    @Tag(name = "System Management", description = "시스템 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메뉴 생성 성공", content = @Content(schema = @Schema(implementation = MenuApiResponseVO.class)))})
    @Operation(operationId = "Menu", summary = "메뉴 생성", description = "메뉴를 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuApiResponseVO> createMenu(HttpServletRequest request, HttpServletResponse response,
                                                                                        @Validated(value = {CreateMenu.class}) @NotNull @RequestBody MenuApiRequestVO menuApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(menuService.createMenu(menuApiRequestVO))
                        .orElseThrow(() -> new RuntimeException("메뉴 생성 실패")));
    }

    public interface CreateMenu {}

    /**
     * 메뉴 업데이트 API
     *
     * @param request
     * @param response
     * @param menuApiRequestVO
     * @return
     */
    @Tag(name = "System Management", description = "시스템 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메뉴 업데이트 성공", content = @Content(schema = @Schema(implementation = MenuApiResponseVO.class)))})
    @Operation(operationId = "Menu", summary = "메뉴 업데이트", description = "메뉴를 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuApiResponseVO> updateMenu(HttpServletRequest request, HttpServletResponse response,
                                                        @Validated(value = {UpdateMenu.class}) @NotNull @RequestBody MenuApiRequestVO menuApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(menuService.updateMenu(menuApiRequestVO))
                        .orElseThrow(() -> new RuntimeException("메뉴 업데이트 실패")));
    }

    public interface UpdateMenu {}

    /**
     * 메뉴 삭제 API
     *
     * @param request
     * @param response
     * @param sequenceNo
     * @return
     */
    @Tag(name = "System Management", description = "시스템 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메뉴 삭제 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "Menu", summary = "메뉴 삭제", description = "메뉴를 삭제한다.")
    @PostMapping(value = "/delete/{sequenceNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteMenu(HttpServletRequest request, HttpServletResponse response,
            @Parameter(description = "메뉴 일련 번호") @PathVariable(value = "sequenceNo", required = true) Long sequenceNo) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(menuService.deleteMenu(sequenceNo))
                        .orElseThrow(() -> new RuntimeException("메뉴 삭제 실패")));
    }
}
