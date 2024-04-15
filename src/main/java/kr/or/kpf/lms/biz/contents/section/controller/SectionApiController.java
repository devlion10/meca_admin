package kr.or.kpf.lms.biz.contents.section.controller;

import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.contents.section.service.SectionService;
import kr.or.kpf.lms.biz.contents.section.vo.request.SectionApiRequestVO;
import kr.or.kpf.lms.biz.contents.section.vo.request.SectionViewRequestVO;
import kr.or.kpf.lms.biz.contents.section.vo.response.SectionViewResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
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
 * 콘텐츠 관리 > 콘텐츠 관리 API 관련 Controller
 */
@Tag(name = "Contents Management", description = "콘텐츠 관리 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/contents/list/chapter/section")
public class SectionApiController extends CSApiControllerSupport {

    private final SectionService sectionService;

    /**
     * Sample (객체 Swagger 표출을 위해서 사용)
     *
     * @param request
     * @param response
     * @param sectionViewRequestVO
     * @return
     */
    @Tag(name = "Swagger USE(NOT API)", description = "사용하지 않는 API(객체 표출용)")
    @PostMapping(value = "/sample", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SectionViewResponseVO> swaggerUse1(HttpServletRequest request, HttpServletResponse response, @RequestBody SectionViewRequestVO sectionViewRequestVO) {
        return null;
    }

    public interface CreateSection {};

    public interface UpdateSection {};

    public interface DeleteSection {};

    /**
     * 섹션(절) 정보 수정 API (다건)
     *
     * @param request
     * @param response
     * @param sectionApiRequestVO
     * @return
     */
    @Tag(name = "Contents Management", description = "콘텐츠 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "섹션(절) 정보 수정 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId="Section", summary = "섹션(절) 정보 수정", description = "섹션(절) 정보를 수정한다. (다건)")
    @PutMapping(value = "/modify-list", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> modifySectionInfoList(HttpServletRequest request, HttpServletResponse response,
                                                                     @Validated(value = {SectionApiController.ModifySection.class}) @NotNull @RequestBody SectionApiRequestVO sectionApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(sectionService.modifySectionInfoList(sectionApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2082, "섹션(절) 정보 생성 실패")));
    }

    public interface ModifySection {};
}
