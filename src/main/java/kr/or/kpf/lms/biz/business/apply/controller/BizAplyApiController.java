package kr.or.kpf.lms.biz.business.apply.controller;

import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.business.apply.vo.request.BizAplyApiRequestVO;
import kr.or.kpf.lms.biz.business.apply.vo.request.BizAplyViewRequestVO;
import kr.or.kpf.lms.biz.business.apply.vo.response.BizAplyApiResponseVO;
import kr.or.kpf.lms.biz.business.apply.vo.response.BizAplyExcelVO;
import kr.or.kpf.lms.biz.business.apply.service.BizAplyService;
import kr.or.kpf.lms.biz.business.apply.vo.response.BizAplyFreeExcelVO;
import kr.or.kpf.lms.biz.common.upload.service.FileMasterService;
import kr.or.kpf.lms.common.exceldownload.excel.OneSheetExcelFile;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import kr.or.kpf.lms.repository.BizAplyDtlFileRepository;
import kr.or.kpf.lms.repository.BizAplyRepository;
import kr.or.kpf.lms.repository.entity.BizAply;
import kr.or.kpf.lms.repository.entity.BizAplyDtlFile;
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
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Tag(name = "Business Aply", description = "사업 공고 신청 - 언론인/기본형 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/business/apply")
public class BizAplyApiController extends CSViewControllerSupport {
    private final FileMasterService fileMasterService;
    private final BizAplyService bizAplyService;
    private final BizAplyRepository bizAplyRepository;
    private final BizAplyDtlFileRepository bizAplyDtlFileRepository;

    /**
     * 사업 공고 신청 - 언론인/기본형 조회 API
     *
     * @param request
     * @param response
     * @param pageable
     * @return
     */
    @Tag(name = "Business Aply", description = "사업 공고 신청 - 언론인/기본형 API")
    @Operation(operationId = "Business Aply", summary = "사업 공고 신청 - 언론인/기본형 조회", description = "사업 공고 신청 - 언론인/기본형 조회한다.")
    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getList(HttpServletRequest request, HttpServletResponse response, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> bizAplyService.getBizAplyList((BizAplyViewRequestVO) params(BizAplyViewRequestVO.class, searchMap, pageable)))
                        .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
    }

    public interface CreateBizAply {}

    /**
     * 사업 공고 신청 - 언론인/기본형 삭제
     *
     * @param request
     * @param response
     * @param bizAplyApiRequestVO
     * @return
     */
    @Tag(name = "Business Aply", description = "사업 공고 신청 - 언론인/기본형 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사업 공고 신청 - 언론인/기본형 삭제 성공", content = @Content(schema = @Schema(implementation = BizAplyApiResponseVO.class)))})
    @Operation(operationId="Business Aply", summary = "사업 공고 신청 - 언론인/기본형 삭제", description = "사업 공고 신청 - 언론인/기본형 삭제 한다.")
    @DeleteMapping(value = "/delete/{sequenceNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteInfo(HttpServletRequest request, HttpServletResponse response,
                                                              @NotNull @RequestBody BizAplyApiRequestVO bizAplyApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizAplyService.deleteBizAply(bizAplyApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3544, "사업 공고 신청 - 언론인/기본형 삭제 실패")));
    }

    /**
     * 사업 공고 신청 - 언론인/기본형 첨부파일 업로드 API
     *
     * @param request
     * @param response
     * @param sequenceNo
     * @param attachFile
     * @return
     */
    @Tag(name = "Business Aply", description = "사업 공고 신청 - 언론인/기본형 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사업 공고 신청 - 언론인/기본형 첨부파일 업로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "Business Aply", summary = "사업 공고 신청 - 언론인/기본형 첨부파일 업로드", description = "사업 공고 신청 - 언론인/기본형 첨부파일을 업로드한다.")
    @PutMapping(value = "/upload/{sequenceNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CSResponseVOSupport> fileUpload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "사업 공고 신청 - 언론인/기본형 시리얼 번호") @PathVariable(value = "sequenceNo", required = true) BigInteger sequenceNo,
                                                          @Parameter(description = "첨부파일") @RequestPart(value = "attachFile", required = true) MultipartFile attachFile) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizAplyService.fileUpload(sequenceNo, attachFile))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9005, "사업 공고 첨부파일 - 언론인/기본형 업로드 실패")));
    }

    /**
     * 사업 공고 신청 - 언론인/기본형 첨부파일 다운로드 API
     *
     * @param request
     * @param response
     * @param attachFilePath
     * @return
     */
    @Tag(name = "Business Aply", description = "사업 공고 신청 - 언론인/기본형 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사업 공고 신청 - 언론인/기본형 첨부파일 다운로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "Business Aply", summary = "사업 공고 신청 - 언론인/기본형 첨부파일 다운로드", description = "사업 공고 신청 - 언론인/기본형 첨부파일을 다운로드한다.")
    @GetMapping(value = "/download")
    public ResponseEntity<ByteArrayResource> fileDownload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "첨부파일 명") @RequestParam(value = "attachFilePath", required = true) String attachFilePath) {
        return Optional.ofNullable(attachFilePath).map(filePath -> {
            byte[] data = fileMasterService.fileDownload(attachFilePath);
            ByteArrayResource resource = new ByteArrayResource(data);
            try {
                BizAply bizAply = bizAplyRepository.findOne(Example.of(BizAply.builder()
                        .bizAplyFile(attachFilePath)
                        .build())).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9006, "파일 미존재"));
                String originName = attachFilePath;
                if (bizAply != null) {
                    if (bizAply.getBizAplyFileOrigin() != null)
                        originName = bizAply.getBizAplyFileOrigin();
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
     * 사업 공고 신청 - 언론인/기본형 첨부파일 다운로드 API
     *
     * @param request
     * @param response
     * @param attachFilePath
     * @return
     */
    @Tag(name = "Business Aply", description = "사업 공고 신청 - 언론인/기본형 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사업 공고 신청 - 언론인/기본형 첨부파일 다운로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "Business Aply", summary = "사업 공고 신청 - 언론인/기본형 첨부파일 다운로드", description = "사업 공고 신청 - 언론인/기본형 첨부파일을 다운로드한다.")
    @GetMapping(value = "/dtl/download")
    public ResponseEntity<ByteArrayResource> dtlFileDownload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "첨부파일 명") @RequestParam(value = "attachFilePath", required = true) String attachFilePath) {
        return Optional.ofNullable(attachFilePath).map(filePath -> {
            byte[] data = fileMasterService.fileDownload(attachFilePath);
            ByteArrayResource resource = new ByteArrayResource(data);
            try {
                BizAplyDtlFile bizAplyDtlFile = bizAplyDtlFileRepository.findOne(Example.of(BizAplyDtlFile.builder()
                        .filePath(attachFilePath)
                        .build())).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9006, "파일 미존재"));
                String originName = attachFilePath;
                if (bizAplyDtlFile != null) {
                    if (bizAplyDtlFile.getOriginalFileName() != null)
                        originName = bizAplyDtlFile.getOriginalFileName();
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
     * 사업 공고 신청 - 언론인 리스트 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Business Aply", description = "사업 공고 신청 - 언론인/기본형 API")
    @Operation(operationId = "Business Aply", summary = "언론인/기본형 사업신청 엑셀", description = "언론인/기본형 사업신청 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/journalist/excel")
    public void getJournalistExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Date date = new Date();
        String dateToStr = DateFormatUtils.format(date, "yyyyMMdd_HHmmSS_");

        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("bizAplyType", "journalist");
        List<BizAplyExcelVO> bizAplyExcelVOList = bizAplyService.getExcel((BizAplyViewRequestVO) params(BizAplyViewRequestVO.class, requestParam, null));
        OneSheetExcelFile<BizAplyExcelVO> excelFile = new OneSheetExcelFile<>(bizAplyExcelVOList, BizAplyExcelVO.class);
        response.setHeader("Content-Disposition", "attachment; filename=" +dateToStr+ URLEncoder.encode("언론인신청리스트", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");

        excelFile.write(response.getOutputStream());
    }

    /**
     * 사업 공고 신청 - 기본형 리스트 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Business Aply", description = "사업 공고 신청 - 언론인/기본형 API")
    @Operation(operationId = "Business Aply", summary = "언론인/기본형 사업신청 엑셀", description = "언론인/기본형 사업신청 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/general/excel")
    public void getGeneralExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Date date = new Date();
        String dateToStr = DateFormatUtils.format(date, "yyyyMMdd_HHmmSS_");

        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("bizAplyType", "general");
        List<BizAplyExcelVO> bizAplyExcelVOList = bizAplyService.getExcel((BizAplyViewRequestVO) params(BizAplyViewRequestVO.class, requestParam, null));
        OneSheetExcelFile<BizAplyExcelVO> excelFile = new OneSheetExcelFile<>(bizAplyExcelVOList, BizAplyExcelVO.class);

        response.setHeader("Content-Disposition", "attachment; filename=" +dateToStr+ URLEncoder.encode("기본형신청리스트", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");
        excelFile.write(response.getOutputStream());
    }

    /**
     * 사업 공고 신청 - 기본형 리스트 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Business Aply", description = "사업 공고 신청 - 언론인/기본형 API")
    @Operation(operationId = "Business Aply", summary = "언론인/기본형 사업신청 엑셀", description = "언론인/기본형 사업신청 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/free/excel")
    public void getFreeExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Date date = new Date();
        String dateToStr = DateFormatUtils.format(date, "yyyyMMdd_HHmmSS_");

        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("bizAplyType", "free");
        List<BizAplyFreeExcelVO> bizAplyFreeExcelVOList = bizAplyService.getFreeExcel((BizAplyViewRequestVO) params(BizAplyViewRequestVO.class, requestParam, null));
        OneSheetExcelFile<BizAplyFreeExcelVO> excelFile = new OneSheetExcelFile<>(bizAplyFreeExcelVOList, BizAplyFreeExcelVO.class);

        response.setHeader("Content-Disposition", "attachment; filename=" +dateToStr+ URLEncoder.encode("자유형신청리스트", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");
        excelFile.write(response.getOutputStream());
    }


    /**
     * 사업 공고 신청 - 언론인/기본형 첨부파일 업로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Business Aply", description = "사업 공고 신청 - 언론인/기본형 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사업 공고 신청 - 언론인/기본형 첨부파일 업로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "Business Aply", summary = "사업 공고 신청 - 언론인/기본형 첨부파일 업로드", description = "사업 공고 신청 - 언론인/기본형 첨부파일을 업로드한다.")
    @PutMapping(value = "/update/stts", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> updateStts(HttpServletRequest request, HttpServletResponse response,
                                                          @NotNull @RequestBody BizAplyApiRequestVO bizAplyApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizAplyService.updateStts(bizAplyApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9005, "사업 공고 첨부파일 - 언론인/기본형 상태변경 실패")));
    }


    /**
     * 사업 공고 신청 - 언론인/기본형 첨부파일 업로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Business Aply", description = "사업 공고 신청 - 언론인/기본형 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사업 공고 신청 - 언론인/기본형 첨부파일 업로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "Business Aply", summary = "사업 공고 신청 - 언론인/기본형 첨부파일 업로드", description = "사업 공고 신청 - 언론인/기본형 첨부파일을 업로드한다.")
    @DeleteMapping(value = "/delete/all", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteAll(HttpServletRequest request, HttpServletResponse response,
                                                          @NotNull @RequestBody BizAplyApiRequestVO bizAplyApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizAplyService.deleteAll(bizAplyApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9005, "사업 공고 첨부파일 - 언론인/기본형 상태변경 실패")));
    }
}
