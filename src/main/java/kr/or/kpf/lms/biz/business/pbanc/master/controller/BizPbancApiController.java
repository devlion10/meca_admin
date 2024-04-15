package kr.or.kpf.lms.biz.business.pbanc.master.controller;

import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.business.pbanc.master.service.BizPbancService;
import kr.or.kpf.lms.biz.business.pbanc.master.vo.CreateBizPbanc;
import kr.or.kpf.lms.biz.business.pbanc.master.vo.UpdateBizPbanc;
import kr.or.kpf.lms.biz.business.pbanc.master.vo.request.BizPbancApiRequestVO;
import kr.or.kpf.lms.biz.business.pbanc.master.vo.request.BizPbancViewRequestVO;
import kr.or.kpf.lms.biz.business.pbanc.master.vo.response.BizPbancApiResponseVO;
import kr.or.kpf.lms.biz.business.pbanc.master.vo.response.BizPbancCustomApiResponseVO;
import kr.or.kpf.lms.biz.business.survey.qitem.vo.request.BizSurveyQitemViewRequestVO;
import kr.or.kpf.lms.biz.business.survey.qitem.vo.response.BizSurveyQitemExcelVO;
import kr.or.kpf.lms.common.exceldownload.excel.OneSheetExcelFile;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import kr.or.kpf.lms.repository.BizPbancMasterRepository;
import kr.or.kpf.lms.repository.entity.BizPbancMaster;
import kr.or.kpf.lms.repository.entity.role.AdminRoleMenu;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

@Tag(name = "Business Pbanc", description = "사업 공고 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/business/pbanc/list")
public class BizPbancApiController extends CSViewControllerSupport {
    private final BizPbancService bizPbancService;
    private final BizPbancMasterRepository bizPbancMasterRepository;

    /**
     * 사업 공고 조회 API
     *
     * @param request
     * @param response
     * @param pageable
     * @return
     */
    @Tag(name = "Business Pbanc", description = "사업 공고 API")
    @Operation(operationId = "Business Pbanc", summary = "사업 공고 조회", description = "사업 공고 조회한다.")
    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getList(HttpServletRequest request, HttpServletResponse response, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> bizPbancService.getBizPbancList((BizPbancViewRequestVO) params(BizPbancViewRequestVO.class, searchMap, pageable)))
                        .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
    }

    /**
     * 사업 공고 자유형 조회 API
     *
     * @param request
     * @param response
     * @param pageable
     * @return
     */
    @Tag(name = "Business Pbanc", description = "사업 공고 API")
    @Operation(operationId = "Business Pbanc", summary = "사업 공고 조회", description = "사업 공고 조회한다.")
    @GetMapping(value = "/5/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BizPbancMaster> get5List(HttpServletRequest request, HttpServletResponse response, Pageable pageable) {
        List<BizPbancMaster> bizPbancMasters = bizPbancMasterRepository.findAll(Example.of(BizPbancMaster.builder()
                .bizPbancType(5)
                .build()));

        if (bizPbancMasters != null && bizPbancMasters.size() > 0) {
            return bizPbancMasters;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 사업 공고 생성
     *
     * @param request
     * @param response
     * @param bizPbancApiRequestVO
     * @return
     */
    @Tag(name = "Business Pbanc", description = "사업 공고 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사업 공고 생성 성공", content = @Content(schema = @Schema(implementation = BizPbancApiResponseVO.class)))})
    @Operation(operationId="Business Pbanc", summary = "사업 공고 생성", description = "사업 공고 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BizPbancApiResponseVO> createInfo(HttpServletRequest request, HttpServletResponse response,
                                                            @Validated(value = {CreateBizPbanc.class}) @NotNull @RequestBody BizPbancApiRequestVO bizPbancApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizPbancService.createBizPbanc(bizPbancApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3501, "사업 공고 생성 실패")));
    }

    /**
     * 사업 공고 업데이트
     *
     * @param request
     * @param response
     * @param bizPbancApiRequestVO
     * @return
     */
    @Tag(name = "Business Pbanc", description = "사업 공고 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사업 공고 수정 성공", content = @Content(schema = @Schema(implementation = BizPbancApiResponseVO.class)))})
    @Operation(operationId="Business Pbanc", summary = "사업 공고 업데이트", description = "사업 공고 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BizPbancApiResponseVO> updateInfo(HttpServletRequest request, HttpServletResponse response,
                                                            @Validated(value = {UpdateBizPbanc.class}) @NotNull @RequestBody BizPbancApiRequestVO bizPbancApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizPbancService.updateBizPbanc(bizPbancApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3503, "사업 공고 수정 실패")));
    }

    /**
     * 사업 공고 삭제
     *
     * @param request
     * @param response
     * @param bizPbancApiRequestVO
     * @return
     */
    @Tag(name = "Business Pbanc", description = "사업 공고 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사업 공고 삭제 성공", content = @Content(schema = @Schema(implementation = BizPbancApiResponseVO.class)))})
    @Operation(operationId="Business Pbanc", summary = "사업 공고 삭제", description = "사업 공고 삭제 한다.")
    @DeleteMapping(value = "/delete/{bizPbancNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteInfo(HttpServletRequest request, HttpServletResponse response,
                                                              @NotNull @RequestBody BizPbancApiRequestVO bizPbancApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizPbancService.deleteBizPbanc(bizPbancApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3504, "사업 공고 삭제 실패")));
    }

    /**
     * 사업 공고 강사 모집 진행 확인
     *
     * @param request
     * @param response
     * @param bizPbancApiRequestVO
     * @return
     */
    @Tag(name = "Business Pbanc", description = "사업 공고 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사업 공고 강사 모집 진행 확인", content = @Content(schema = @Schema(implementation = BizPbancApiResponseVO.class)))})
    @Operation(operationId="Business Pbanc", summary = "사업 공고 강사 모집 진행 확인", description = "사업 공고 강사 모집 진행 확인 한다.")
    @PostMapping(value = "/checkInstr", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> checkBizInstructorPbanc(HttpServletRequest request, HttpServletResponse response,
                                                                       @NotNull @RequestBody BizPbancApiRequestVO bizPbancApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizPbancService.checkBizInstructorPbanc(bizPbancApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3505, "사업 공고를 찾을 수 없습니다.")));
    }
}
