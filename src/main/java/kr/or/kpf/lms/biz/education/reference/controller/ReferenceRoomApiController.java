package kr.or.kpf.lms.biz.education.reference.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.business.pbanc.master.vo.request.BizPbancApiRequestVO;
import kr.or.kpf.lms.biz.business.pbanc.master.vo.response.BizPbancApiResponseVO;
import kr.or.kpf.lms.biz.common.upload.service.FileMasterService;
import kr.or.kpf.lms.biz.education.application.vo.request.EducationApplicationViewRequestVO;
import kr.or.kpf.lms.biz.education.application.vo.response.EducationApplicationExcelVO;
import kr.or.kpf.lms.biz.education.reference.service.ReferenceRoomService;
import kr.or.kpf.lms.biz.education.reference.vo.request.ReferenceRoomApiRequestVO;
import kr.or.kpf.lms.biz.education.reference.vo.request.ReferenceRoomViewRequestVO;
import kr.or.kpf.lms.biz.education.reference.vo.response.ReferenceRoomApiResponseVO;
import kr.or.kpf.lms.biz.education.reference.vo.response.ReferenceRoomExcelVO;
import kr.or.kpf.lms.biz.education.reference.vo.response.ReferenceRoomViewResponseVO;
import kr.or.kpf.lms.common.exceldownload.excel.OneSheetExcelFile;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.Code;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import kr.or.kpf.lms.repository.education.CurriculumReferenceRoomRepository;
import kr.or.kpf.lms.repository.entity.BizInstructor;
import kr.or.kpf.lms.repository.entity.BizSurveyQitem;
import kr.or.kpf.lms.repository.entity.education.CurriculumReferenceRoom;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 교육 관리 > 교육 과정 관리 API 관련 Controller
 */
@Tag(name = "Education Management", description = "교육 관리 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/education/curriculum/reference-room")
public class ReferenceRoomApiController extends CSApiControllerSupport {
    private final FileMasterService fileMasterService;
    private final ReferenceRoomService referenceRoomService;
    private final CurriculumReferenceRoomRepository curriculumReferenceRoomRepository;

    /**
     * Sample (객체 Swagger 표출을 위해서 사용)
     *
     * @param request
     * @param response
     * @param referenceRoomViewRequestVO
     * @return
     */
    @Tag(name = "Swagger USE(NOT API)", description = "사용하지 않는 API(객체 표출용)")
    @PostMapping(value = "/sample", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReferenceRoomViewResponseVO> swaggerUse1(HttpServletRequest request, HttpServletResponse response, @RequestBody ReferenceRoomViewRequestVO referenceRoomViewRequestVO) {
        return null;
    }

    /**
     * 자료실 정보 생성
     *
     * @param request
     * @param response
     * @param referenceRoomApiRequestVO
     * @param file
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "자료실 정보 생성 성공", content = @Content(schema = @Schema(implementation = ReferenceRoomApiResponseVO.class)))})
    @Operation(operationId="Reference Room", summary = "자료실 정보 생성", description = "자료실 정보를 생성한다.")
    @PostMapping(value = "/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ReferenceRoomApiResponseVO> createQuestionInfo(HttpServletRequest request, HttpServletResponse response,
            @Validated(value = {CreateReferenceRoom.class}) @NotNull @RequestPart(required = true, value = "requestObject") ReferenceRoomApiRequestVO referenceRoomApiRequestVO,
            @RequestPart(required = false, value = "file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(referenceRoomService.createReferenceRoom(referenceRoomApiRequestVO, file))
                        .orElseThrow(() -> new RuntimeException("자료실 정보 생성에 실패하였습니다.")));
    }

    public interface CreateReferenceRoom {}

    /**
     * 자료실 정보 수정
     *
     * @param request
     * @param response
     * @param referenceRoomApiRequestVO
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "자료실 정보 수정 성공", content = @Content(schema = @Schema(implementation = ReferenceRoomApiResponseVO.class)))})
    @Operation(operationId="Reference Room", summary = "자료실 정보 수정", description = "자료실 정보를 수정한다.")
    @PutMapping(value = "/update", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ReferenceRoomApiResponseVO> updateQuestionInfo(HttpServletRequest request, HttpServletResponse response,
            @Validated(value = {UpdateReferenceRoom.class}) @NotNull @RequestPart(required = true, value = "requestObject") ReferenceRoomApiRequestVO referenceRoomApiRequestVO,
            @RequestPart(required = false, value = "file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(referenceRoomService.updateReferenceRoom(referenceRoomApiRequestVO, file))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3081, "시험 문제 정보 생성 실패")));
    }

    public interface UpdateReferenceRoom {}

    /**
     * 자료실 정보 삭제
     *
     * @param request
     * @param response
     * @param referenceRoomApiRequestVO
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "자료실 정보 삭제 성공", content = @Content(schema = @Schema(implementation = ReferenceRoomApiResponseVO.class)))})
    @Operation(operationId="Reference Room", summary = "자료실 정보 삭제", description = "자료실 정보를 삭제한다.")
    @DeleteMapping(value = "/delete/{sequenceNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteInfo(HttpServletRequest request, HttpServletResponse response,
                                                                         @NotNull @RequestBody ReferenceRoomApiRequestVO referenceRoomApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(referenceRoomService.deleteInfo(referenceRoomApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3022, "자료실 정보 삭제 실패")));
    }

    /**
     * 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @Operation(operationId = "Reference Room", summary = "자료실 엑셀", description = "자료실 목록 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/excel/{curriculumCode}")
    public void getListExcel(HttpServletRequest request, HttpServletResponse response,
                             @Parameter(description = "교육 과정 코드") @PathVariable(value = "curriculumCode", required = true) String curriculumCode) throws IOException {

        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("curriculumCode", curriculumCode);
        List<ReferenceRoomExcelVO> referenceRoomExcelVOList = referenceRoomService.getExcel((ReferenceRoomViewRequestVO) params(ReferenceRoomViewRequestVO.class, requestParam, null));
        OneSheetExcelFile<ReferenceRoomExcelVO> excelFile = new OneSheetExcelFile<>(referenceRoomExcelVOList, ReferenceRoomExcelVO.class);
        String dateToStr = DateFormatUtils.format(new Date(), "yyyyMMdd_HHmmSS_");
        response.setHeader("Content-Disposition", "attachment; filename=" + dateToStr+ URLEncoder.encode("자료실 파일 목록", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");
        excelFile.write(response.getOutputStream());
    }

    /**
     * 첨부파일 다운로드 API
     *
     * @param request
     * @param response
     * @param attachFilePath
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "자료실 첨부파일 다운로드 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId = "Reference Room", summary = "자료실 첨부파일 다운로드", description = "자료실 첨부파일을 다운로드한다.")
    @GetMapping(value = "/download")
    public ResponseEntity<ByteArrayResource> fileDownload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "첨부파일 명") @RequestParam(value = "attachFilePath", required = true) String attachFilePath) {
        return Optional.ofNullable(attachFilePath).map(filePath -> {
            byte[] data = fileMasterService.fileDownload(attachFilePath);
            ByteArrayResource resource = new ByteArrayResource(data);

            try {
                CurriculumReferenceRoom curriculumReferenceRoom = curriculumReferenceRoomRepository.findOne(Example.of(CurriculumReferenceRoom.builder()
                        .filePath(attachFilePath)
                        .build())).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9006, "파일 미존재"));
                String originName = attachFilePath;
                if (curriculumReferenceRoom != null) {
                    if (curriculumReferenceRoom.getFileOriginName() != null)
                        originName = curriculumReferenceRoom.getFileOriginName();
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
}
