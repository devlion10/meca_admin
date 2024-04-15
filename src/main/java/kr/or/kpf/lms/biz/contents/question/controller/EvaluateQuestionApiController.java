package kr.or.kpf.lms.biz.contents.question.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.contents.question.service.EvaluateQuestionService;
import kr.or.kpf.lms.biz.contents.question.vo.request.EvaluateQuestionApiRequestVO;
import kr.or.kpf.lms.biz.contents.question.vo.request.EvaluateQuestionViewRequestVO;
import kr.or.kpf.lms.biz.contents.question.vo.response.EvaluateQuestionApiResponseVO;
import kr.or.kpf.lms.biz.contents.question.vo.response.EvaluateQuestionExcelVO;
import kr.or.kpf.lms.biz.contents.question.vo.response.EvaluateQuestionViewResponseVO;
import kr.or.kpf.lms.common.exceldownload.excel.OneSheetExcelFile;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 콘텐츠 관리 > 강의 평가 관리 > 문항 관리 API 관련 Controller
 */
@Tag(name = "Contents Management", description = "콘텐츠 관리 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/contents/question")
public class EvaluateQuestionApiController extends CSApiControllerSupport {

    private final EvaluateQuestionService evaluateQuestionService;

    /**
     * Sample (객체 Swagger 표출을 위해서 사용)
     *
     * @param request
     * @param response
     * @param evaluateQuestionViewRequestVO
     * @return
     */
    @Tag(name = "Swagger USE(NOT API)", description = "사용하지 않는 API(객체 표출용)")
    @PostMapping(value = "/sample", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EvaluateQuestionViewResponseVO> swaggerUse1(HttpServletRequest request, HttpServletResponse response, @RequestBody EvaluateQuestionViewRequestVO evaluateQuestionViewRequestVO) {
        return null;
    }

    /**
     * 강의 평가 질문 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @Tag(name = "Contents Management", description = "콘텐츠 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강의 평가 질문 조회 성공", content = @Content(schema = @Schema(implementation = EvaluateQuestionApiResponseVO.class)))})
    @Operation(operationId="Question", summary = "강의 평가 질문 조회", description = "강의 평가 질문을 조회한다.")
    @GetMapping(path = {"", "/"})
    public ResponseEntity<Object> getQuestionInfo(HttpServletRequest request, Pageable pageable, Model model){
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(CSSearchMap.of(request))
                        .map(searchMap -> resultPaging(evaluateQuestionService.getList((EvaluateQuestionViewRequestVO) params(EvaluateQuestionViewRequestVO.class, searchMap, pageable)), new ArrayList<>()))
                        .orElse(new HashMap<>()));
    }

    /**
     * 강의 평가 질문 정보 생성
     *
     * @param request
     * @param response
     * @param evaluateQuestionApiRequestVO
     * @return
     */
    @Tag(name = "Contents Management", description = "콘텐츠 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강의 평가 질문 정보 생성 성공", content = @Content(schema = @Schema(implementation = EvaluateQuestionApiResponseVO.class)))})
    @Operation(operationId="Question", summary = "강의 평가 질문 정보 생성", description = "강의 평가 질문 정보를 생성한다.")
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EvaluateQuestionApiResponseVO> createQuestionInfo(HttpServletRequest request, HttpServletResponse response,
                                                                        @Validated(value = {CreateEvaluateQuestion.class}) @NotNull @RequestBody EvaluateQuestionApiRequestVO evaluateQuestionApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(evaluateQuestionService.createQuestionInfo(evaluateQuestionApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2061, "강의 평가 질문 정보 생성 실패")));
    }

    public interface CreateEvaluateQuestion {}

    /**
     * 강의 평가 질문 정보 업데이트
     *
     * @param request
     * @param response
     * @param evaluateQuestionApiRequestVO
     * @return
     */
    @Tag(name = "Contents Management", description = "콘텐츠 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강의 평가 질문 정보 업데이트 성공", content = @Content(schema = @Schema(implementation = EvaluateQuestionApiResponseVO.class)))})
    @Operation(operationId="Question", summary = "강의 평가 질문 정보 업데이트", description = "강의 평가 질문 정보를 업데이트한다.")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EvaluateQuestionApiResponseVO> updateQuestionInfo(HttpServletRequest request, HttpServletResponse response,
                                                                            @Validated(value = {UpdateEvaluateQuestion.class}) @NotNull @RequestBody EvaluateQuestionApiRequestVO evaluateQuestionApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(evaluateQuestionService.updateQuestionInfo(evaluateQuestionApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2066, "강의 평가 질문 정보 업데이트 실패")));
    }

    public interface UpdateEvaluateQuestion {}

    /**
     * 강의 평가 질문 정보 삭제
     *
     * @param request
     * @param response
     * @param evaluateQuestionApiRequestVO
     * @return
     */
    @Tag(name = "Contents Management", description = "콘텐츠 관리 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강의 평가 질문 정보 삭제 성공", content = @Content(schema = @Schema(implementation = CSResponseVOSupport.class)))})
    @Operation(operationId="Question", summary = "강의 평가 질문 정보 삭제", description = "강의 평가 질문 정보를 삭제한다.")
    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSResponseVOSupport> deleteQuestionInfo(HttpServletRequest request, HttpServletResponse response,
                                                                            @Validated(value = {DeleteEvaluateQuestion.class}) @NotNull @RequestBody EvaluateQuestionApiRequestVO evaluateQuestionApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Optional.ofNullable(evaluateQuestionService.deleteQuestionInfo(evaluateQuestionApiRequestVO))
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2067, "강의 평가 질문 정보 삭제 실패")));
    }

    public interface DeleteEvaluateQuestion {}



    /**
     * 엑셀 다운로드 API
     *
     * @param request
     * @param response
     * @return
     */
    @Tag(name = "Contents Management", description = "콘텐츠 관리 API")
    @Operation(operationId="Question", summary = "문항 엑셀", description = "문항 엑셀 파일을 다운로드한다.")
    @GetMapping(value = "/excel")
    public void getListExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Date date = new Date();
        String dateToStr = DateFormatUtils.format(date, "yyyyMMdd_HHmmSS_");

        List<EvaluateQuestionExcelVO> evaluateQuestionExcelVOList = evaluateQuestionService.getExcel((EvaluateQuestionViewRequestVO) params(EvaluateQuestionViewRequestVO.class, CSSearchMap.of(request), null));
        OneSheetExcelFile<EvaluateQuestionExcelVO> excelFile = new OneSheetExcelFile<>(evaluateQuestionExcelVOList, EvaluateQuestionExcelVO.class);
        response.setHeader("Content-Disposition", "attachment; filename=" + dateToStr+URLEncoder.encode("문항리스트", "UTF-8") + ".xlsx;");
        response.setHeader("Content-Transfer-Encoding", "binary");

        excelFile.write(response.getOutputStream());
    }
}
