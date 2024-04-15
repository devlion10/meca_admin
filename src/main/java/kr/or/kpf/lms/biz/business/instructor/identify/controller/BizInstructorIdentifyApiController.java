package kr.or.kpf.lms.biz.business.instructor.identify.controller;

import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.business.instructor.identify.service.BizInstructorIdentifyService;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.CreateBizInstructorIdentify;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.UpdateBizInstructorIdentify;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.request.BizInstructorIdentifyApiRequestVO;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.request.BizInstructorIdentifyViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.request.FormeBizlecinfoApiRequestVO;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.response.BizInstructorIdentifyApiResponseVO;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.response.BizInstructorIdentifyExcelVO;
import kr.or.kpf.lms.biz.common.upload.service.FileMasterService;
import kr.or.kpf.lms.common.exceldownload.excel.OneSheetExcelFile;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import kr.or.kpf.lms.repository.BizInstructorIdentifyRepository;
import kr.or.kpf.lms.repository.entity.BizInstructorIdentify;
import kr.or.kpf.lms.repository.entity.homepage.MyQna;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bind.annotation.Default;
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

@Tag(name = "Business Instructor Identify", description = "강의확인서 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/business/instructor/identify")
public class BizInstructorIdentifyApiController extends CSViewControllerSupport {
    private final FileMasterService fileMasterService;
    private final BizInstructorIdentifyService bizInstructorIdentifyService;
    private final BizInstructorIdentifyRepository bizInstructorIdentifyRepository;

    /**
     * 강의확인서 조회 API
     *
     * @param request
     * @param response
     * @param pageable
     * @return
     */
    @Tag(name = "Business Instructor Identify", description = "강의확인서 API")
    @Operation(operationId = "Instructor Identify", summary = "강의확인서 생성", description = "강의확인서 신청 조회한다.")
    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getList(HttpServletRequest request, HttpServletResponse response, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> bizInstructorIdentifyService.getBizInstructorIdentifyList((BizInstructorIdentifyViewRequestVO) params(BizInstructorIdentifyViewRequestVO.class, searchMap, pageable)))
                        .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
    }
    /**
     * 포미 강의확인서 조회 API
     *
     * @param request
     * @param response
     * @param pageable
     * @return
     */
    @Tag(name = "Business Instructor Identify", description = "강의확인서 API")
    @Operation(operationId = "Instructor Identify", summary = "강의확인서 생성", description = "강의확인서 신청 조회한다.")
    @GetMapping(value = "/forme", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getFormeList(HttpServletRequest request, HttpServletResponse response, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> bizInstructorIdentifyService.getBizInstructorIdentifyList((FormeBizlecinfoApiRequestVO) params(FormeBizlecinfoApiRequestVO.class, searchMap, pageable)))
                        .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
    }

    /**
     * 포미 강의확인서 조회 API
     *
     * @param request
     * @param response
     * @param pageable
     * @return
     */
    @Tag(name = "Business Instructor Identify", description = "강의확인서 API")
    @Operation(operationId = "Instructor Identify", summary = "강의확인서 생성", description = "강의확인서 신청 조회한다.")
    @GetMapping(value = "/forme/{blciId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getFormeData(HttpServletRequest request, HttpServletResponse response, Pageable pageable,
                                               @Parameter(description = "강사 모집 공고 일련 번호") @PathVariable(value = "blciId", required = true) String blciId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> bizInstructorIdentifyService.getBizInstructorIdentify(blciId))
                        .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
    }
    /**
     * 강의확인서 상세 조회 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Business Instructor", description = "강의확인서 API")
    @Operation(operationId = "Instructor", summary = "강의확인서 상세 조회", description = "강의확인서 상세 조회한다.")
    @GetMapping(value = "/{bizInstrIdntyNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getObject(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> bizInstructorIdentifyService.getBizInstructorIdentify((BizInstructorIdentifyViewRequestVO) params(BizInstructorIdentifyViewRequestVO.class, searchMap, null))));

    }

    /**
     * 강의확인서 생성
     *
     * @param request
     * @param response
     * @param bizInstructorIdentifyApiRequestVO
     * @return
     */
    @Tag(name = "Business Instructor Identify", description = "강의확인서 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강의확인서 생성 성공", content = @Content(schema = @Schema(implementation = BizInstructorIdentifyApiResponseVO.class)))})
    @Operation(operationId="Instructor Identify", summary = "강의확인서 생성", description = "강의확인서 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BizInstructorIdentifyApiResponseVO> createInfo(HttpServletRequest request, HttpServletResponse response,
                                                                @Validated(value = {CreateBizInstructorIdentify.class}) @NotNull @RequestBody BizInstructorIdentifyApiRequestVO bizInstructorIdentifyApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizInstructorIdentifyService.createBizInstructorIdentify(bizInstructorIdentifyApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3601, "강의확인서 생성 실패")));
    }

    /**
     * 강의확인서 업데이트
     *
     * @param request
     * @param response
     * @param bizInstructorIdentifyApiRequestVO
     * @return
     */
    @Tag(name = "Business Instructor Identify", description = "강의확인서 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강의확인서 수정 성공", content = @Content(schema = @Schema(implementation = BizInstructorIdentifyApiResponseVO.class)))})
    @Operation(operationId="Instructor Identify", summary = "강의확인서 업데이트", description = "강의확인서 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BizInstructorIdentifyApiResponseVO> updateInfo(HttpServletRequest request, HttpServletResponse response,
                                                                 @Validated(value = {UpdateBizInstructorIdentify.class}) @NotNull @RequestBody BizInstructorIdentifyApiRequestVO bizInstructorIdentifyApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizInstructorIdentifyService.updateBizInstructorIdentify(bizInstructorIdentifyApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3603, "강의확인서 수정 실패")));
    }
    /**
     * 강의확인서 초기화
     *
     * @param request
     * @param response
     * @param bizInstructorIdentifyApiRequestVO
     * @return
     */
    @Tag(name = "Business Instructor Identify", description = "강의확인서 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강의확인서 초기화", content = @Content(schema = @Schema(implementation = BizInstructorIdentifyApiResponseVO.class)))})
    @Operation(operationId="Instructor Identify", summary = "강의확인서 초기화", description = "강의확인서 초기화한다.")
    @PutMapping(value = "/remove", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BizInstructorIdentifyApiResponseVO> updateAprv(HttpServletRequest request, HttpServletResponse response,
                                                                         @Validated(value = {UpdateBizInstructorIdentify.class}) @NotNull @RequestBody BizInstructorIdentifyApiRequestVO bizInstructorIdentifyApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizInstructorIdentifyService.removeBizInstructorIdentify(bizInstructorIdentifyApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3603, "강의확인서 수정 실패")));
    }
    /**
     * 강의확인서 삭제
     *
     * @param request
     * @param response
     * @param bizInstructorIdentifyApiRequestVO
     * @return
     */
    @Tag(name = "Business Instructor Identify", description = "강의확인서 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강의확인서 삭제 성공", content = @Content(schema = @Schema(implementation = BizInstructorIdentifyApiResponseVO.class)))})
    @Operation(operationId="Instructor Identify", summary = "강의확인서 삭제", description = "강의확인서 삭제 한다.")
    @DeleteMapping(value = "/delete/{instuctorIdentifyNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteInfo(HttpServletRequest request, HttpServletResponse response,
                                                              @NotNull @RequestBody BizInstructorIdentifyApiRequestVO bizInstructorIdentifyApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizInstructorIdentifyService.deleteBizInstructorIdentify(bizInstructorIdentifyApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3604, "강의확인서 삭제 실패")));
    }

    /**
     * 강의확인서 첨부파일 업로드 API
     *
     */
    @Tag(name = "Business Instructor Identify", description = "강의확인서 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강의확인서 첨부파일 업로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "Business Instructor Identify", summary = "강의확인서 첨부파일 업로드", description = "강의확인서 첨부파일을 업로드한다.")
    @PutMapping(value = "/upload/{instuctorIdentifyNo}/{fileType}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CSResponseVOSupport> fileUpload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "강의확인서 번호") @PathVariable(value="instuctorIdentifyNo") String instuctorIdentifyNo,
                                                          @Parameter(description = "첨부파일") @RequestPart(value="attachFile") MultipartFile attachFile,
                                                          @Parameter(description = "첨부파일 구분") @PathVariable(value="fileType") String fileType){
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizInstructorIdentifyService.fileUpload(instuctorIdentifyNo, attachFile, fileType ))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9005, "강의확인서 첨부파일 업로드 실패")));
    }

    /**
     * 강의확인서 첨부파일 다운로드 API
     *
     */
    @Tag(name = "Business Instructor Identify", description = "강의확인서 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강의확인서 첨부파일 다운로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "Business Instructor Identify", summary = "강의확인서 첨부파일 다운로드", description = "강의확인서 첨부파일을 다운로드한다.")
    @GetMapping(value = "/download")
    public ResponseEntity<ByteArrayResource> fileDownload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "첨부파일 명") @RequestParam(value = "attachFilePath", required = true) String attachFilePath,
                                                          @Parameter(description = "다운로드 유형") @RequestParam(value = "fileType", required = true) String fileType){
        return Optional.ofNullable(attachFilePath).map(filePath -> {
            byte[] data = fileMasterService.fileDownload(attachFilePath);
            ByteArrayResource resource = new ByteArrayResource(data);
            try {
                String originName = attachFilePath;
                if (fileType.equals("atch")) {
                    BizInstructorIdentify bizInstructorIdentify = bizInstructorIdentifyRepository.findOne(Example.of(BizInstructorIdentify.builder()
                            .bizInstrIdntyAtchFile(attachFilePath)
                            .build())).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9006, "파일 미존재"));
                    if (bizInstructorIdentify != null) {
                        if (bizInstructorIdentify.getBizInstrIdntyAtchFileOrgn() != null)
                            originName = bizInstructorIdentify.getBizInstrIdntyAtchFileOrgn();
                    }
                } else {
                    BizInstructorIdentify bizInstructorIdentify = bizInstructorIdentifyRepository.findOne(Example.of(BizInstructorIdentify.builder()
                            .bizInstrIdntyLsnFile(attachFilePath)
                            .build())).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9006, "파일 미존재"));
                    if (bizInstructorIdentify != null) {
                        if (bizInstructorIdentify.getBizInstrIdntyLsnFileOrgn() != null)
                            originName = bizInstructorIdentify.getBizInstrIdntyLsnFileOrgn();
                    }
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
     * 강의확인서 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Business Instructor Identify", description = "강의확인서 API")
    @Operation(operationId = "Instructor Identify", summary = "강의확인서 엑셀", description = "강의확인서 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/excel")
    public void getListExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Date date = new Date();
        String dateToStr = DateFormatUtils.format(date, "yyyyMMdd_HHmmSS_");
        List<BizInstructorIdentifyExcelVO> bizInstructorIdentifyExcelVOList = bizInstructorIdentifyService.getExcel((BizInstructorIdentifyViewRequestVO) params(BizInstructorIdentifyViewRequestVO.class, CSSearchMap.of(request), null));
        OneSheetExcelFile<BizInstructorIdentifyExcelVO> excelFile = new OneSheetExcelFile<>(bizInstructorIdentifyExcelVOList, BizInstructorIdentifyExcelVO.class);
        response.setHeader("Content-Disposition", "attachment; filename=" + dateToStr+URLEncoder.encode("강의확인서", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");
        excelFile.write(response.getOutputStream());
    }
    /**
     * 정산요청목록 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Business Instructor Identify", description = "강의확인서 API")
    @Operation(operationId = "Instructor Identify", summary = "정산요청목록 엑셀", description = "정산요청목록 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/calculate/list/excel")
    public void getListCalculateExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Date date = new Date();
        String dateToStr = DateFormatUtils.format(date, "yyyyMMdd_HHmmSS_");
        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("sttsType", 0);/** 지출 대기 상태만 출력하기 위한 구분값 */
        List<BizInstructorIdentifyExcelVO> bizInstructorIdentifyExcelVOList = bizInstructorIdentifyService.getListCalculateExcel((BizInstructorIdentifyViewRequestVO) params(BizInstructorIdentifyViewRequestVO.class, requestParam, null));
        OneSheetExcelFile<BizInstructorIdentifyExcelVO> excelFile = new OneSheetExcelFile<>(bizInstructorIdentifyExcelVOList, BizInstructorIdentifyExcelVO.class);
        response.setHeader("Content-Disposition", "attachment; filename=" + dateToStr+URLEncoder.encode("정산요청목록", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");
        excelFile.write(response.getOutputStream());
    }
}
