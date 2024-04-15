package kr.or.kpf.lms.biz.business.instructor.identify.dtl.controller;

import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.business.instructor.aply.vo.request.BizInstructorAplyViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.identify.dtl.service.BizInstructorIdentifyDtlService;
import kr.or.kpf.lms.biz.business.instructor.identify.dtl.vo.CreateBizInstructorIdentifyDtl;
import kr.or.kpf.lms.biz.business.instructor.identify.dtl.vo.UpdateBizInstructorIdentifyDtl;
import kr.or.kpf.lms.biz.business.instructor.identify.dtl.vo.request.BizInstructorIdentifyDtlApiRequestVO;
import kr.or.kpf.lms.biz.business.instructor.identify.dtl.vo.request.BizInstructorIdentifyDtlExcelRequestVO;
import kr.or.kpf.lms.biz.business.instructor.identify.dtl.vo.request.BizInstructorIdentifyDtlViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.identify.dtl.vo.response.BizInstructorIdentifyDtlApiResponseVO;
import kr.or.kpf.lms.biz.business.instructor.identify.dtl.vo.response.BizInstructorIdentifyDtlExcelVO;
import kr.or.kpf.lms.biz.business.instructor.identify.service.BizInstructorIdentifyService;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.CreateBizInstructorIdentify;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.UpdateBizInstructorIdentify;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.request.BizInstructorIdentifyApiRequestVO;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.request.BizInstructorIdentifyViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.response.BizInstructorIdentifyApiResponseVO;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.request.ArchiveClassGuideExcelRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.response.ArchiveClassGuideExcelResponseVO;
import kr.or.kpf.lms.common.exceldownload.excel.OneSheetExcelFile;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateFormatUtils;
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

@Tag(name = "Business Instructor Identify Detail", description = "강의확인서 강의시간표 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/business/instructor/identify/detail")
public class BizInstructorIdentifyDtlApiController extends CSViewControllerSupport {
    private final BizInstructorIdentifyDtlService bizInstructorIdentifyDtlService;

    /**
     * 강의확인서 강의시간표 조회 API
     *
     * @param request
     * @param response
     * @param pageable
     * @return
     */
    @Tag(name = "Business Instructor Identify Detail", description = "강의확인서 강의시간표 API")
    @Operation(operationId = "Instructor Identify Detail", summary = "강의확인서 강의시간표 생성", description = "강의확인서 강의시간표 신청 조회한다.")
    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getList(HttpServletRequest request, HttpServletResponse response, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> bizInstructorIdentifyDtlService.getBizInstructorIdentifyDtlList((BizInstructorIdentifyDtlViewRequestVO) params(BizInstructorIdentifyDtlViewRequestVO.class, searchMap, pageable)))
                        .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
    }

    /**
     * 강의확인서 강의시간표 상세 조회 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Business Instructor Identify Detail", description = "강의확인서 강의시간표 API")
    @Operation(operationId = "Instructor Identify Detail", summary = "강의확인서 강의시간표 상세 조회", description = "강의확인서 강의시간표 상세 조회한다.")
    @GetMapping(value = "/{bizInstrIdntyDtlNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getObject(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> bizInstructorIdentifyDtlService.getBizInstructorIdentifyDtl((BizInstructorIdentifyDtlViewRequestVO) params(BizInstructorIdentifyDtlViewRequestVO.class, searchMap, null))));

    }

    /**
     * 강의확인서 강의시간표 업데이트
     *
     * @param request
     * @param response
     * @param bizInstructorIdentifyDtlApiRequestVO
     * @return
     */
    @Tag(name = "Business Instructor Identify Detail", description = "강의확인서 강의시간표 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강의확인서 강의시간표 수정 성공", content = @Content(schema = @Schema(implementation = BizInstructorIdentifyDtlApiResponseVO.class)))})
    @Operation(operationId="Instructor Identify Detail", summary = "강의확인서 강의시간표 업데이트", description = "강의확인서 강의시간표 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BizInstructorIdentifyDtlApiResponseVO> updateInfo(HttpServletRequest request, HttpServletResponse response,
                                                                 @Validated(value = {UpdateBizInstructorIdentifyDtl.class}) @NotNull @RequestBody BizInstructorIdentifyDtlApiRequestVO bizInstructorIdentifyDtlApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizInstructorIdentifyDtlService.updateBizInstructorIdentifyDtl(bizInstructorIdentifyDtlApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3603, "강의확인서 강의시간표 수정 실패")));
    }

    /**
     * 강의확인서 강의시간표 삭제
     *
     * @param request
     * @param response
     * @param bizInstructorIdentifyDtlApiRequestVO
     * @return
     */
    @Tag(name = "Business Instructor Identify Detail", description = "강의확인서 강의시간표 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강의확인서 강의시간표 삭제 성공", content = @Content(schema = @Schema(implementation = BizInstructorIdentifyDtlApiResponseVO.class)))})
    @Operation(operationId="Instructor Identify Detail", summary = "강의확인서 강의시간표 삭제", description = "강의확인서 강의시간표 삭제 한다.")
    @DeleteMapping(value = "/delete/{instuctorIdentifyDtlNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteInfo(HttpServletRequest request, HttpServletResponse response,
                                                              @NotNull @RequestBody BizInstructorIdentifyDtlApiRequestVO bizInstructorIdentifyDtlApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(bizInstructorIdentifyDtlService.deleteBizInstructorIdentifyDtl(bizInstructorIdentifyDtlApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3604, "강의확인서 강의시간표 삭제 실패")));
    }

    /**
     * 강의확인서 주제 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Business Instructor Identify Detail", description = "강의확인서 강의시간표 API")
    @Operation(operationId = "Instructor Identify Detail", summary = "강의확인서 강의시간표 엑셀", description = "강의확인서 강의시간표 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/excel")
    public void getListExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Date date = new Date();
        String dateToStr = DateFormatUtils.format(date, "yyyyMMdd_HHmmSS_");

        List<BizInstructorIdentifyDtlExcelVO> list = bizInstructorIdentifyDtlService.getExcel((BizInstructorIdentifyDtlExcelRequestVO) params(BizInstructorIdentifyDtlExcelRequestVO.class, CSSearchMap.of(request), null));
        OneSheetExcelFile<BizInstructorIdentifyDtlExcelVO> excelFile = new OneSheetExcelFile<>(list, BizInstructorIdentifyDtlExcelVO.class);
        response.setHeader("Content-Disposition", "attachment; filename=" + dateToStr+ URLEncoder.encode("강의확인서 주제", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");
        excelFile.write(response.getOutputStream());
    }

}
