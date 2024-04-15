package kr.or.kpf.lms.biz.education.question.controller;

import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.common.upload.service.FileMasterService;
import kr.or.kpf.lms.biz.education.question.service.ExamQuestionService;
import kr.or.kpf.lms.biz.education.question.vo.request.ExamQuestionApiRequestVO;
import kr.or.kpf.lms.biz.education.question.vo.request.ExamQuestionApiRequestVOS;
import kr.or.kpf.lms.biz.education.question.vo.request.ExamQuestionViewRequestVO;
import kr.or.kpf.lms.biz.education.question.vo.response.ExamQuestionApiResponseVO;
import kr.or.kpf.lms.biz.education.question.vo.response.ExamQuestionViewResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.repository.education.ExamQuestionMasterRepository;
import kr.or.kpf.lms.repository.entity.FileMaster;
import kr.or.kpf.lms.repository.entity.education.ExamQuestionMaster;
import lombok.RequiredArgsConstructor;
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
import javax.validation.constraints.NotEmpty;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;

/**
 * 교육 관리 > 교육 과정 관리 API 관련 Controller
 */
@Tag(name = "Education Management", description = "교육 관리 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/education/curriculum/e-learning/question")
public class ExamQuestionApiController extends CSApiControllerSupport {
    private final FileMasterService fileMasterService;
    private final ExamQuestionService examQuestionService;
    private final ExamQuestionMasterRepository examQuestionMasterRepository;

    /**
     * Sample (객체 Swagger 표출을 위해서 사용)
     *
     * @param request
     * @param response
     * @param examQuestionViewRequestVO
     * @return
     */
    @Tag(name = "Swagger USE(NOT API)", description = "사용하지 않는 API(객체 표출용)")
    @PostMapping(value = "/sample", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExamQuestionViewResponseVO> swaggerUse1(HttpServletRequest request, HttpServletResponse response, @RequestBody ExamQuestionViewRequestVO examQuestionViewRequestVO) {
        return null;
    }

    /**
     * 시험 문제 정보 생성
     *
     * @param request
     * @param response
     * @param examQuestionApiRequestVO
     * @param questionFile
     * @param fileSortNo
     * @param itemFiles
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "시험 문제 정보 생성 성공", content = @Content(schema = @Schema(implementation = ExamQuestionApiResponseVO.class)))})
    @Operation(operationId="Question", summary = "시험 문제 정보 생성", description = "시험 문제 정보를 생성한다.")
    @PostMapping(value = "/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ExamQuestionApiResponseVO> createQuestionInfo(HttpServletRequest request, HttpServletResponse response,
                                                                        @Validated(value = {CreateExamQuestion.class}) @NotNull @RequestPart(required = true, value = "requestObject") ExamQuestionApiRequestVO examQuestionApiRequestVO,
                                                                        @RequestPart(required = false, value = "questionFile") MultipartFile questionFile,
                                                                        @RequestPart(required = false, value = "fileSortNo") List<String> fileSortNo,
                                                                        @RequestPart(required = false, value = "itemFiles") List<MultipartFile> itemFiles) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(examQuestionService.createQuestionInfo(examQuestionApiRequestVO, questionFile, itemFiles, fileSortNo))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3081, "시험 문제 정보 생성 실패")));
    }

    public interface CreateExamQuestion {}

    /**
     * 시험 문제 정보 업데이트
     *
     * @param request
     * @param response
     * @param examQuestionApiRequestVO
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "시험 문제 정보 수정 성공", content = @Content(schema = @Schema(implementation = ExamQuestionApiResponseVO.class)))})
    @Operation(operationId="Question", summary = "시험 문제 정보 업데이트", description = "시험 문제 정보를 업데이트한다.")
    @PutMapping(value = "/update", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ExamQuestionApiResponseVO> updateQuestionInfo(HttpServletRequest request, HttpServletResponse response,
                                                                        @Validated(value = {CreateExamQuestion.class}) @NotNull @RequestPart(required = true, value = "requestObject") ExamQuestionApiRequestVO examQuestionApiRequestVO,
                                                                        @RequestPart(required = false, value = "questionFile") MultipartFile questionFile,
                                                                        @RequestPart(required = false, value = "fileSortNo") List<String> fileSortNo,
                                                                        @RequestPart(required = false, value = "itemFiles") List<MultipartFile> itemFiles) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(examQuestionService.updateQuestionInfo(examQuestionApiRequestVO, questionFile, itemFiles, fileSortNo))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3083, "시험 문제 정보 수정 실패")));
    }

    public interface UpdateExamQuestion {}

    /**
     * 시험 문제 정보 삭제
     *
     * @param request
     * @param response
     * @param examQuestions
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "시험 문제 정보 삭제 성공", content = @Content(schema = @Schema(implementation = ExamQuestionApiResponseVO.class)))})
    @Operation(operationId="User", summary = "시험 문제 정보 삭제", description = "시험 문제 정보 삭제 한다.")
    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteUserInfo(HttpServletRequest request, HttpServletResponse response,
                                                              @Validated(value = {DeleteExamQuestion.class}) @NotNull @RequestBody ExamQuestionApiRequestVOS examQuestions) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(examQuestionService.deleteQuestionInfo(examQuestions))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3086, "시험 문제 정보 삭제 실패")));
    }

    public interface DeleteExamQuestion {}

    /**
     * 시험 문제 정보 생성 API By 엑셀 파일
     *
     * @param request
     * @param response
     * @param excelFile
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "시험 문제 정보 생성 성공", content = @Content(schema = @Schema(implementation = ExamQuestionApiResponseVO.class)))})
    @Operation(operationId="Chapter", summary = "시험 문제 정보 생성", description = "엑셀 파일을 이용하여 챕터 정보를 생성한다.")
    @PostMapping(value = "/create-by-file", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CSResponseVOSupport> createQuestionInfoByFile(HttpServletRequest request, HttpServletResponse response,
                                                                       @Parameter(description = "엑셀 파일") @RequestPart(value = "excelFile", required = true) MultipartFile excelFile) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(examQuestionService.createQuestionInfoByFile(excelFile))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3081, "시험 문제 정보 생성 실패")));
    }

    /**
     * 시험 문제 첨부파일 업로드 API
     *
     * @param request
     * @param response
     * @param questionSerialNo
     * @param questionFile
     * @return
     */
    @Tag(name = "Education Management", description = "교육 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "시험 문제 첨부파일 성공", content = @Content(schema = @Schema(implementation = ExamQuestionApiResponseVO.class)))})
    @Operation(operationId = "Contents", summary = "시험 문제 첨부파일", description = "콘텐츠 파일을 업로드한다.")
    @PutMapping(value = "/upload/questionFile/{questionSerialNo}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CSResponseVOSupport> fileUpload(HttpServletRequest request, HttpServletResponse response,
                                                          @Parameter(description = "시험 문제 일련 번호") @PathVariable(value = "questionSerialNo", required = true) String questionSerialNo,
                                                          @Parameter(description = "첨부파일") @RequestPart(value = "questionFile", required = true) MultipartFile questionFile) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(examQuestionService.examQuestionFileUpload(questionSerialNo, questionFile))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9005, "시험 문제 첨부파일 업로드 실패")));
    }
}
