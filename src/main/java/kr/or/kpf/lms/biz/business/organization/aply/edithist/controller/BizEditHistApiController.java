package kr.or.kpf.lms.biz.business.organization.aply.edithist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.business.organization.aply.edithist.service.BizEditHistService;
import kr.or.kpf.lms.biz.business.organization.aply.edithist.vo.request.BizEditHistViewRequestVO;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Optional;

@Tag(name = "Business Edit Hist", description = "사업 공고 수정 이력 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/business/edithist")
public class BizEditHistApiController extends CSViewControllerSupport {
    private final BizEditHistService bizEditHistService;


    /**
     * 사업 공고 수정 이력 조회 API
     *
     * @param request
     * @param response
     * @param pageable
     * @return
     */
    @Tag(name = "Business Edit Hist", description = "사업 공고 수정 이력 API")
    @Operation(operationId = "Business Edit Hist", summary = "사업 공고 수정 이력 조회", description = "사업 공고 수정 이력 조회한다.")
    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getList(HttpServletRequest request, HttpServletResponse response, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> bizEditHistService.getBizEditHistList((BizEditHistViewRequestVO) params(BizEditHistViewRequestVO.class, searchMap, pageable)))
                        .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
    }

}
