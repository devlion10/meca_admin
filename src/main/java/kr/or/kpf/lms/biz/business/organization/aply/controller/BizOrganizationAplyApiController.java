package kr.or.kpf.lms.biz.business.organization.aply.controller;

import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.business.instructor.aply.vo.request.BizInstructorAplyViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.service.BizInstructorService;
import kr.or.kpf.lms.biz.business.instructor.vo.CreateBizInstructor;
import kr.or.kpf.lms.biz.business.instructor.vo.UpdateBizInstructor;
import kr.or.kpf.lms.biz.business.instructor.vo.request.BizInstructorApiRequestVO;
import kr.or.kpf.lms.biz.business.instructor.vo.response.BizInstructorApiResponseVO;
import kr.or.kpf.lms.biz.business.organization.aply.service.BizOrganizationAplyService;
import kr.or.kpf.lms.biz.business.organization.aply.vo.CreateBizOrganizationAply;
import kr.or.kpf.lms.biz.business.organization.aply.vo.UpdateBizOrganizationAply;
import kr.or.kpf.lms.biz.business.organization.aply.vo.request.BizOrganizationAplyApiRequestVO;
import kr.or.kpf.lms.biz.business.organization.aply.vo.request.BizOrganizationAplyViewRequestVO;
import kr.or.kpf.lms.biz.business.organization.aply.vo.response.BizOrganizationAplyApiResponseVO;
import kr.or.kpf.lms.biz.business.organization.aply.vo.response.BizOrganizationAplyDistExcelVO;
import kr.or.kpf.lms.biz.business.organization.aply.vo.response.BizOrganizationAplyExcelVO;
import kr.or.kpf.lms.biz.business.pbanc.master.vo.request.BizPbancViewRequestVO;
import kr.or.kpf.lms.biz.business.pbanc.master.vo.response.BizPbancCustomApiResponseVO;
import kr.or.kpf.lms.biz.common.upload.service.FileMasterService;
import kr.or.kpf.lms.common.exceldownload.excel.OneSheetExcelFile;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import kr.or.kpf.lms.repository.BizOrganizationAplyRepository;
import kr.or.kpf.lms.repository.entity.BizOrganizationAply;
import kr.or.kpf.lms.repository.entity.FileMaster;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

@Tag(name = "Business Organization Apply", description = "사업 공고 신청 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/business/organization/apply")
public class BizOrganizationAplyApiController extends CSViewControllerSupport {
    private final FileMasterService fileMasterService;
    private final BizOrganizationAplyService bizOrganizationAplyService;
    private final BizOrganizationAplyRepository bizOrganizationAplyRepository;

    /**
     * 사업 공고 조회 API
     *
     * @param request
     * @param response
     * @param pageable
     * @return
     */
    @Tag(name = "Business Organization Apply", description = "사업 공고 신청 API")
    @Operation(operationId = "Business Organization Apply", summary = "사업 공고 신청 조회", description = "사업 공고 신청 조회한다.")
    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getList(HttpServletRequest request, HttpServletResponse response, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> bizOrganizationAplyService.getBizOrganizationAplyList((BizOrganizationAplyViewRequestVO) params(BizOrganizationAplyViewRequestVO.class, searchMap, pageable)))
                        .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
    }

    /**
     * 사업 공고 신청 상세 조회 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Business Organization Apply", description = "사업 공고 신청 API")
    @Operation(operationId = "Organization Apply", summary = "사업 공고 상세 조회", description = "사업 공고 상세 조회한다.")
    @GetMapping(value = "/{bizOrgAplyNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getObject(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> bizOrganizationAplyService.getBizOrganizationAply((BizOrganizationAplyViewRequestVO) params(BizOrganizationAplyViewRequestVO.class, searchMap, null))));

    }

    /**
     * 사업 공고 신청 생성
     *
     * @param request
     * @param response
     * @param bizOrganizationAplyApiRequestVO
     * @return
     */
    @Tag(name = "Business Organization Apply", description = "사업 공고 신청 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사업 공고 신청 생성 성공", content = @Content(schema = @Schema(implementation = BizOrganizationAplyApiResponseVO.class)))})
    @Operation(operationId="Business Organization Apply", summary = "사업 공고 신청 생성", description = "사업 공고 신청 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BizOrganizationAplyApiResponseVO> createInfo(HttpServletRequest request, HttpServletResponse response,
                                                                @Validated(value = {CreateBizOrganizationAply.class}) @NotNull @RequestBody BizOrganizationAplyApiRequestVO bizOrganizationAplyApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizOrganizationAplyService.createBizOrganizationAply(bizOrganizationAplyApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3541, "사업 공고 신청 생성 실패")));
    }

    /**
     * 사업 공고 신청 업데이트
     *
     * @param request
     * @param response
     * @param bizOrganizationAplyApiRequestVO
     * @return
     */
    @Tag(name = "Business Organization Apply", description = "사업 공고 신청 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사업 공고 신청 수정 성공", content = @Content(schema = @Schema(implementation = BizOrganizationAplyApiResponseVO.class)))})
    @Operation(operationId="Business Organization Apply", summary = "사업 공고 신청 업데이트", description = "사업 공고 신청 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BizOrganizationAplyApiResponseVO> updateInfo(HttpServletRequest request, HttpServletResponse response,
                                                                 @Validated(value = {UpdateBizOrganizationAply.class}) @NotNull @RequestBody BizOrganizationAplyApiRequestVO bizOrganizationAplyApiRequestVO) {
/*        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizOrganizationAplyService.updateBizOrganizationAply(bizOrganizationAplyApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3543, "사업 공고 신청 수정 실패")));*/

        if (bizOrganizationAplyService.vailedBizOrganizationAply(bizOrganizationAplyApiRequestVO)) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Optional.ofNullable(bizOrganizationAplyService.updateBizOrganizationAply(bizOrganizationAplyApiRequestVO))
                            .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3543, "사업 공고 신청 수정 실패")));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizOrganizationAplyService.updateBizOrganizationAply(bizOrganizationAplyApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3543, "사업 공고 신청 승인 실패 (승인 수 초과)")));
    }

    /**
     * 사업 공고 신청 삭제
     *
     * @param request
     * @param response
     * @param bizOrganizationAplyApiRequestVO
     * @return
     */
    @Tag(name = "Business Organization Apply", description = "사업 공고 신청 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사업 공고 신청 삭제 성공", content = @Content(schema = @Schema(implementation = BizOrganizationAplyApiResponseVO.class)))})
    @Operation(operationId="Business Organization Apply", summary = "사업 공고 신청 삭제", description = "사업 공고 신청 삭제 한다.")
    @DeleteMapping(value = "/delete/{bizOrgAplyNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteInfo(HttpServletRequest request, HttpServletResponse response,
                                                              @NotNull @RequestBody BizOrganizationAplyApiRequestVO bizOrganizationAplyApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizOrganizationAplyService.deleteBizOrganizationAply(bizOrganizationAplyApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3544, "사업 공고 신청 삭제 실패")));
    }

    /**
     * 사업 공고 신청서 첨부파일 업로드 API
     *
     * @param request
     * @param response
     * @param bizOrgAplyNo
     * @param attachFile
     * @return
     */
    @Tag(name = "Business Organization Apply", description = "사업 공고 신청 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사업 공고 신청 첨부파일 업로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "Business Organization Apply", summary = "사업 공고 신청 첨부파일 업로드", description = "사업 공고 신청 첨부파일을 업로드한다.")
    @PutMapping(value = "/upload/{bizOrgAplyNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CSResponseVOSupport> fileUpload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "사업 공고 신청 시리얼 번호") @PathVariable(value = "bizOrgAplyNo", required = true) String bizOrgAplyNo,
                                                          @Parameter(description = "첨부파일") @RequestPart(value = "attachFile", required = true) MultipartFile attachFile) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizOrganizationAplyService.fileUpload(bizOrgAplyNo, attachFile))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9005, "사업 공고 신청 첨부파일 업로드 실패")));
    }

    /**
     * 사업 공고 신청 신청서 첨부파일 다운로드 API
     *
     * @param request
     * @param response
     * @param attachFilePath
     * @return
     */
    @Tag(name = "Business Organization Apply", description = "사업 공고 신청 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사업 공고 신청 첨부파일 다운로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "Business Organization Apply", summary = "사업 공고 신청 첨부파일 다운로드", description = "사업 공고 신청 첨부파일을 다운로드한다.")
    @GetMapping(value = "/download")
    public ResponseEntity<ByteArrayResource> fileDownload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "첨부파일 명") @RequestParam(value = "attachFilePath", required = true) String attachFilePath) {
        return Optional.ofNullable(attachFilePath).map(filePath -> {

            byte[] data = fileMasterService.fileDownload(attachFilePath);
            ByteArrayResource resource = new ByteArrayResource(data);
            try {
                BizOrganizationAply bizOrganizationAply = bizOrganizationAplyRepository.findOne(Example.of(BizOrganizationAply.builder()
                        .bizOrgAplyFile(attachFilePath)
                        .build())).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9006, "파일 미존재"));
                String originName = attachFilePath;
                if (bizOrganizationAply != null) {
                    if (bizOrganizationAply.getBizOrgAplyFileOrigin() != null)
                        originName = bizOrganizationAply.getBizOrgAplyFileOrigin();
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
     * 사업 공고 신청 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Business Organization Apply", description = "사업 공고 신청 API")
    @Operation(operationId = "Business Organization Apply", summary = "사업 공고 신청 엑셀", description = "사업 공고 신청 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/excel")
    public void getListExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Date date = new Date();
        String dateToStr = DateFormatUtils.format(date, "yyyyMMdd_HHmmSS_");

        List<BizOrganizationAplyExcelVO> bizOrganizationAplyExcelVOList = bizOrganizationAplyService.getExcel((BizOrganizationAplyViewRequestVO) params(BizOrganizationAplyViewRequestVO.class, CSSearchMap.of(request), null));
        OneSheetExcelFile<BizOrganizationAplyExcelVO> excelFile = new OneSheetExcelFile<>(bizOrganizationAplyExcelVOList, BizOrganizationAplyExcelVO.class);
        response.setHeader("Content-Disposition", "attachment; filename=" + dateToStr+URLEncoder.encode("사업신청리스트", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");

        excelFile.write(response.getOutputStream());
    }

    /**
     * 사업 공고 신청 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Business Organization Apply", description = "사업 공고 신청 API")
    @Operation(operationId = "Business Organization Apply", summary = "사업 공고 신청 엑셀", description = "사업 공고 신청 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/dist/excel")
    public void getListDistExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Date date = new Date();
        String dateToStr = DateFormatUtils.format(date, "yyyyMMdd_HHmmSS_");

        List<BizOrganizationAplyDistExcelVO> bizOrganizationAplyExcelVOList = bizOrganizationAplyService.getDistExcel((BizOrganizationAplyViewRequestVO) params(BizOrganizationAplyViewRequestVO.class, CSSearchMap.of(request), null));
        OneSheetExcelFile<BizOrganizationAplyDistExcelVO> excelFile = new OneSheetExcelFile<>(bizOrganizationAplyExcelVOList, BizOrganizationAplyDistExcelVO.class);
        response.setHeader("Content-Disposition", "attachment; filename=" + dateToStr+URLEncoder.encode("사업신청리스트(거리증빙)", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");

        excelFile.write(response.getOutputStream());
    }

}
