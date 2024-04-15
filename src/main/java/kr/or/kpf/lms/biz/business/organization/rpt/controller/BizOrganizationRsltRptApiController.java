package kr.or.kpf.lms.biz.business.organization.rpt.controller;

import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.business.organization.rpt.service.BizOrganizationRsltRptService;
import kr.or.kpf.lms.biz.business.organization.rpt.vo.CreateBizOrganizationRsltRpt;
import kr.or.kpf.lms.biz.business.organization.rpt.vo.UpdateBizOrganizationRsltRpt;
import kr.or.kpf.lms.biz.business.organization.rpt.vo.request.BizOrganizationRsltRptApiRequestVO;
import kr.or.kpf.lms.biz.business.organization.rpt.vo.request.BizOrganizationRsltRptSttsApiRequestVO;
import kr.or.kpf.lms.biz.business.organization.rpt.vo.request.BizOrganizationRsltRptViewRequestVO;
import kr.or.kpf.lms.biz.business.organization.rpt.vo.response.BizOrganizationRsltRptApiResponseVO;
import kr.or.kpf.lms.biz.business.organization.rpt.vo.response.BizOrganizationRsltRptExcelVO;
import kr.or.kpf.lms.biz.common.upload.service.FileMasterService;
import kr.or.kpf.lms.common.exceldownload.excel.OneSheetExcelFile;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import kr.or.kpf.lms.repository.BizOrganizationRsltRptRepository;
import kr.or.kpf.lms.repository.entity.BizOrganizationRsltRpt;
import kr.or.kpf.lms.repository.entity.FileMaster;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Tag(name = "Business Organization Result Report", description = "결과보고서 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/business/organization/report")
public class BizOrganizationRsltRptApiController extends CSViewControllerSupport {
    private final FileMasterService fileMasterService;
    private final BizOrganizationRsltRptService bizOrganizationRsltRptService;
    private final BizOrganizationRsltRptRepository bizOrganizationRsltRptRepository;

    /**
     * 결과보고서 조회 API
     *
     * @param request
     * @param response
     * @param pageable
     * @return
     */
    @Tag(name = "Business Organization Result Report", description = "결과보고서 API")
    @Operation(operationId = "Business Organization Result Report", summary = "결과보고서 조회", description = "결과보고서 조회한다.")
    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getList(HttpServletRequest request, HttpServletResponse response, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> bizOrganizationRsltRptService.getBizOrganizationRsltRptList((BizOrganizationRsltRptViewRequestVO) params(BizOrganizationRsltRptViewRequestVO.class, searchMap, pageable)))
                        .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
    }

    /**
     * 결과보고서 상세 조회 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Business Organization Result Report", description = "결과보고서 API")
    @Operation(operationId = "Business Organization Result Report", summary = "결과보고서 상세 조회", description = "결과보고서 상세 조회한다.")
    @GetMapping(value = "/{bizInstrRsltRptNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getObject(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> bizOrganizationRsltRptService.getBizOrganizationRsltRpt((BizOrganizationRsltRptViewRequestVO) params(BizOrganizationRsltRptViewRequestVO.class, searchMap, null))));

    }

    /**
     * 결과보고서 생성
     *
     * @param request
     * @param response
     * @param bizOrganizationRsltRptApiRequestVO
     * @return
     */
    @Tag(name = "Business Organization Result Report", description = "결과보고서 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결과보고서 생성 성공", content = @Content(schema = @Schema(implementation = BizOrganizationRsltRptApiResponseVO.class)))})
    @Operation(operationId="Business Organization Result Report", summary = "결과보고서 생성", description = "결과보고서 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BizOrganizationRsltRptApiResponseVO> createInfo(HttpServletRequest request, HttpServletResponse response,
                                                                          @Validated(value = {CreateBizOrganizationRsltRpt.class}) @NotNull @RequestBody BizOrganizationRsltRptApiRequestVO bizOrganizationRsltRptApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizOrganizationRsltRptService.createBizOrganizationRsltRpt(bizOrganizationRsltRptApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3641, "결과보고서 생성 실패")));
    }

    /**
     * 결과보고서 업데이트
     *
     * @param request
     * @param response
     * @param bizOrganizationRsltRptApiRequestVO
     * @return
     */
    @Tag(name = "Business Organization Result Report", description = "결과보고서 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결과보고서 수정 성공", content = @Content(schema = @Schema(implementation = BizOrganizationRsltRptApiResponseVO.class)))})
    @Operation(operationId="Business Organization Result Report", summary = "결과보고서 업데이트", description = "결과보고서 신청 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BizOrganizationRsltRptApiResponseVO> updateInfo(HttpServletRequest request, HttpServletResponse response,
                                                                          @Validated(value = {UpdateBizOrganizationRsltRpt.class}) @NotNull @RequestBody BizOrganizationRsltRptApiRequestVO bizOrganizationRsltRptApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizOrganizationRsltRptService.updateBizOrganizationRsltRpt(bizOrganizationRsltRptApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3643, "결과보고서 수정 실패")));
    }

    /**
     * 결과보고서 삭제
     *
     * @param request
     * @param response
     * @param bizOrganizationRsltRptApiRequestVO
     * @return
     */
    @Tag(name = "Business Organization Result Report", description = "결과보고서 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결과보고서 삭제 성공", content = @Content(schema = @Schema(implementation = BizOrganizationRsltRptApiResponseVO.class)))})
    @Operation(operationId="Business Organization Result Report", summary = "결과보고서 삭제", description = "결과보고서 삭제 한다.")
    @DeleteMapping(value = "/delete/{bizOrgRsltRptNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteInfo(HttpServletRequest request, HttpServletResponse response,
                                                          @NotNull @RequestBody BizOrganizationRsltRptApiRequestVO bizOrganizationRsltRptApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizOrganizationRsltRptService.deleteBizOrganizationRsltRpt(bizOrganizationRsltRptApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3644, "결과보고서 삭제 실패")));
    }

    /**
     * 결과보고서 첨부파일 업로드 API
     *
     * @param request
     * @param response
     * @param bizOrgRsltRptNo
     * @param attachFile
     * @return
     */
    @Tag(name = "Business Organization Result Report", description = "결과보고서 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결과보고서 첨부파일 업로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "Business Organization Result Report", summary = "결과보고서 첨부파일 업로드", description = "결과보고서 첨부파일을 업로드한다.")
    @PutMapping(value = "/upload/{bizOrgRsltRptNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CSResponseVOSupport> fileUpload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "결과보고서 시리얼 번호") @PathVariable(value = "bizOrgRsltRptNo", required = true) String bizOrgRsltRptNo,
                                                          @Parameter(description = "첨부파일") @RequestPart(value = "attachFile", required = true) MultipartFile attachFile) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizOrganizationRsltRptService.fileUpload(bizOrgRsltRptNo, attachFile))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9005, "사업 공고 첨부파일 업로드 실패")));
    }

    /**
     * 첨부파일 다운로드 API
     *
     * @param request
     * @param response
     * @param attachFilePath
     * @return
     */
    @Tag(name = "Business Organization Result Report", description = "결과보고서 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "파일 업로드 첨부파일 다운로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "Business Organization Result Report", summary = "파일 업로드 첨부파일 다운로드", description = "파일 업로드 첨부파일을 다운로드한다.")
    @GetMapping(value = "/download")
    public ResponseEntity<ByteArrayResource> fileDownload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "첨부파일 경로") @RequestParam(value = "attachFilePath", required = true) String attachFilePath) {
        return Optional.ofNullable(attachFilePath).map(filePath -> {

            byte[] data = fileMasterService.fileDownload(attachFilePath);
            ByteArrayResource resource = new ByteArrayResource(data);
            try {
                BizOrganizationRsltRpt bizOrganizationRsltRpt = bizOrganizationRsltRptRepository.findOne(Example.of(BizOrganizationRsltRpt.builder()
                        .bizOrgRsltRptFile(attachFilePath)
                        .build())).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9006, "파일 미존재"));
                String originName = attachFilePath;
                if (bizOrganizationRsltRpt != null) {
                    if (bizOrganizationRsltRpt.getBizOrgRsltRptFileOrigin() != null)
                        originName = bizOrganizationRsltRpt.getBizOrgRsltRptFileOrigin();
                }

                return ResponseEntity.status(HttpStatus.OK)
                        .contentLength(data.length)
                        .header("Content-type", "application/octet-stream")
                        .header("Content-disposition", "attachment; filename=\"" + URLEncoder.encode(originName, "UTF-8") + "\"")
                        .body(resource);
            } catch (UnsupportedEncodingException e) {
                throw new KPFException(KPF_RESULT.ERROR9006, "파일명 URL 인코드 실패");
            }
        }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9006, "파일 패스 미존재"));
    }

    /**
     * 결과보고서 상태 (복수) 업데이트
     *
     * @param request
     * @param response
     * @param bizOrganizationRsltRptSttsApiRequestVO
     * @return
     */
    @Tag(name = "Business Organization Result Report", description = "결과보고서 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결과보고서 상태 (복수) 수정 성공", content = @Content(schema = @Schema(implementation = BizOrganizationRsltRptSttsApiRequestVO.class)))})
    @Operation(operationId="Business Organization Result Report", summary = "결과보고서 상태 (복수) 업데이트", description = "결과보고서 상태 (복수)  업데이트한다.")
    @PutMapping(value = "/update/stts", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateInfoStts(HttpServletRequest request, HttpServletResponse response, @NotNull @RequestBody BizOrganizationRsltRptSttsApiRequestVO bizOrganizationRsltRptSttsApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizOrganizationRsltRptService.updateBizOrganizationRsltRptStt(bizOrganizationRsltRptSttsApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3643, "결과보고서 수정 실패")));
    }

    /**
     * 결과보고서 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Business Organization Result Report", description = "결과보고서 API")
    @Operation(operationId = "Business Organization Result Report", summary = "결과보고서 엑셀", description = "결과보고서 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/excel")
    public void getListExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Date date = new Date();
        String dateToStr = DateFormatUtils.format(date, "yyyyMMdd_HHmmSS_");

        List<BizOrganizationRsltRptExcelVO> bizOrganizationRsltRptExcelVOList = bizOrganizationRsltRptService.getExcel((BizOrganizationRsltRptViewRequestVO) params(BizOrganizationRsltRptViewRequestVO.class, CSSearchMap.of(request), null));
        OneSheetExcelFile<BizOrganizationRsltRptExcelVO> excelFile = new OneSheetExcelFile<>(bizOrganizationRsltRptExcelVOList, BizOrganizationRsltRptExcelVO.class);
        response.setHeader("Content-Disposition", "attachment; filename=" + dateToStr+URLEncoder.encode("결과보고서", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");

        excelFile.write(response.getOutputStream());
    }
}
