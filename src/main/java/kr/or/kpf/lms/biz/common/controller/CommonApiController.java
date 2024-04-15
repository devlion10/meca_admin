package kr.or.kpf.lms.biz.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.common.service.CommonService;
import kr.or.kpf.lms.biz.education.application.vo.response.EducationApplicationApiResponseVO;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Tag(name = "Main Management", description = "공통 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/common")
public class CommonApiController extends CSApiControllerSupport {

    private final CommonService commonService;

    /**
     * 메일 발송 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Main Management", description = "공통 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메일 발송 성공", content = @Content(schema = @Schema(implementation = EducationApplicationApiResponseVO.class)))})
    @Operation(operationId = "Mail", summary = "메일 발송", description = "메일을 발송한다.")
    @GetMapping(value = "/mail")
    public ResponseEntity<CSResponseVOSupport> sendMail(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(commonService.sendMail())
                        .orElseThrow(() -> new RuntimeException("메일 발송에 실패하였습니다.")));
    }
}
