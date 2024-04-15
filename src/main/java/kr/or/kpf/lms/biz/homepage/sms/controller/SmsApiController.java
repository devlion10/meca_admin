package kr.or.kpf.lms.biz.homepage.sms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.homepage.sms.service.SmsService;
import kr.or.kpf.lms.biz.homepage.sms.vo.request.SmsApiRequestVO;
import kr.or.kpf.lms.biz.homepage.sms.vo.request.SmsViewRequestVO;
import kr.or.kpf.lms.biz.homepage.sms.vo.response.SmsApiResponseVO;
import kr.or.kpf.lms.biz.homepage.sms.vo.response.SmsViewResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Optional;

/**
 * 홈페이지 관리 > SMS API 관련 Controller
 */
@Tag(name = "Homepage Management", description = "홈페이지 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/homepage/sms")
public class SmsApiController extends CSApiControllerSupport {

    private final SmsService smsService;

    /**
     * Sample (객체 Swagger 표출을 위해서 사용)
     *
     * @param request
     * @param response
     * @param reviewViewRequestVO
     * @return
     */
    @Tag(name = "Swagger USE(NOT API)", description = "사용하지 않는 API(객체 표출용)")
    @PostMapping(value = "/sample", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SmsViewResponseVO> swaggerUse1(HttpServletRequest request, HttpServletResponse response, @RequestBody SmsViewRequestVO reviewViewRequestVO) {
        return null;
    }

    /**
     * SMS 조회 API
     *
     * @param request
     * @param response
     * @param pageable
     * @param sequenceNo
     * @param containTextType
     * @param containText
     * @return
     */
    @Tag(name = "Homepage Management", description = "홈페이지 API")
    @Operation(operationId = "Sms", summary = "SMS 조회", description = "SMS 조회한다.")
    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getList(HttpServletRequest request, HttpServletResponse response, Pageable pageable,
                                          @RequestParam(value="sequenceNo", required = false) BigInteger sequenceNo,
                                          @RequestParam(value="containTextType", required = false) String containTextType,
                                          @RequestParam(value="containText", required = false) String containText) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> smsService.getList((SmsViewRequestVO) params(SmsViewRequestVO.class, searchMap, pageable)))
                        .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
    }

}
