package kr.or.kpf.lms.biz.contents.evaluate.controller;

import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.contents.evaluate.service.EvaluateService;
import kr.or.kpf.lms.biz.contents.evaluate.vo.request.EvaluateApiRequestVO;
import kr.or.kpf.lms.biz.contents.evaluate.vo.request.EvaluateViewRequestVO;
import kr.or.kpf.lms.biz.contents.evaluate.vo.response.EvaluateApiResponseVO;
import kr.or.kpf.lms.biz.contents.evaluate.vo.response.EvaluateExcelVO;
import kr.or.kpf.lms.biz.contents.evaluate.vo.response.EvaluateViewResponseVO;
import kr.or.kpf.lms.common.exceldownload.excel.OneSheetExcelFile;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.data.domain.Page;
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
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 콘텐츠 관리 > 강의 평가 관리 API 관련 Controller
 */
@Tag(name = "Evaluate Management", description = "콘텐츠 관리 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/contents/evaluate")
public class EvaluateApiController extends CSApiControllerSupport {

	private final EvaluateService evaluateService;

	/**
	 * Sample (객체 Swagger 표출을 위해서 사용)
	 *
	 * @param request
	 * @param response
	 * @param evaluateViewRequestVO
	 * @return
	 */
	@Tag(name = "Swagger USE(NOT API)", description = "사용하지 않는 API(객체 표출용)")
	@PostMapping(value = "/sample", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EvaluateViewResponseVO> swaggerUse1(HttpServletRequest request, HttpServletResponse response, @RequestBody EvaluateViewRequestVO evaluateViewRequestVO) {
		return null;
	}

	/**
	 * 강의 평가 정보 조회
	 *
	 * @param request
	 * @param pageable
	 * @param model
	 * @return
	 */
	@Tag(name = "Evaluate Management", description = "콘텐츠 관리 API")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "강의 평가 정보 조회 성공", content = @Content(schema = @Schema(implementation = EvaluateApiResponseVO.class)))})
	@Operation(operationId="Evaluate", summary = "강의 평가 정보 조회", description = "강의 평가 정보을 조회한다.")
	@GetMapping(path = {"", "/"})
	public ResponseEntity<Object> getEvaluateInfo(HttpServletRequest request, Pageable pageable, Model model){
		return ResponseEntity.status(HttpStatus.OK)
				.body(Optional.ofNullable(CSSearchMap.of(request))
						.map(searchMap -> {
							Page<Object> result = evaluateService.getEvaluateList((EvaluateViewRequestVO) params(EvaluateViewRequestVO.class, searchMap, pageable));
							Map<String, Object> resultMap = new HashMap<>();
							resultMap.put("content", result.getContent());
							/** 페이징 정보 */
							Map<String, Object> page = new HashMap<>();
							/** 첫번째 페이지 여부 */
							page.put("first", result.isFirst());
							/** 마지막 페이지 여부 */
							page.put("last", result.isLast());
							/** 현재 페이지 데이터 갯수 */
							page.put("currentElements", result.getNumberOfElements());
							/** 검색 조건에 해당하는 데이터 총 갯수 */
							page.put("totalElements", result.getTotalElements());
							/** 페이지 당 요청 갯수 */
							page.put("size", result.getSize());
							/** 요청 페이지 */
							page.put("page", result.getNumber());
							/** 전체 페이지 */
							page.put("totalPages", result.getTotalPages());
							/** 이전 블록 존재 여부(한 블록당 10페이지) */
							if(result.getNumber() / 10 < 1) page.put("hasPrevious", false);
							else page.put("hasPrevious", true);
							/** 이후 블록 존재 여부(한 블록당 10페이지) */
							if(Math.floor(result.getNumber() / 10) < Math.floor((result.getTotalPages() - 1) / 10)) page.put("hasNext", true);
							else page.put("hasNext", false);

							int blockStart = ((result.getNumber() / 10) * 10);
							page.put("blockStart", blockStart);
							int blockEnd = blockStart + 9;
							page.put("blockEnd", blockEnd < result.getTotalPages() - 1 ? blockEnd : (result.getTotalPages() - 1 > 0 ? result.getTotalPages() - 1 : 0));

							resultMap.put("pageable", page);

							return resultMap;
						})
						.orElse(new HashMap<>()));
	}

	/**
	 * 강의 평가 정보 생성
	 *
	 * @param request
	 * @param response
	 * @param evaluateApiRequestVO
	 * @return
	 */
	@Tag(name = "Evaluate Management", description = "콘텐츠 관리 API")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "강의 평가 정보 생성 성공", content = @Content(schema = @Schema(implementation = EvaluateApiResponseVO.class)))})
	@Operation(operationId="Evaluate", summary = "강의 평가 정보 생성", description = "강의 평가 정보를 생성한다.")
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EvaluateApiResponseVO> createEvaluateInfo(HttpServletRequest request, HttpServletResponse response,
		@Validated(value = {CreateEvaluate.class}) @NotNull @RequestBody EvaluateApiRequestVO evaluateApiRequestVO) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(Optional.ofNullable(evaluateService.createEvaluateInfo(evaluateApiRequestVO))
						.orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3041, "강의 평가 정보 생성 실패")));
	}

	public interface CreateEvaluate {}

	/**
	 * 강의 평가 정보 업데이트
	 *
	 * @param request
	 * @param response
	 * @param evaluateApiRequestVO
	 * @return
	 */
	@Tag(name = "Evaluate Management", description = "콘텐츠 관리 API")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "강의 평가 정보 수정 성공", content = @Content(schema = @Schema(implementation = EvaluateApiResponseVO.class)))})
	@Operation(operationId="Evaluate", summary = "강의 평가 정보 업데이트", description = "강의 평가 정보를 업데이트한다.")
	@PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EvaluateApiResponseVO> updateEvaluateInfo(HttpServletRequest request, HttpServletResponse response,
		@Validated(value = {UpdateEvaluate.class}) @NotNull @RequestBody EvaluateApiRequestVO evaluateApiRequestVO) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(Optional.ofNullable(evaluateService.updateEvaluateInfo(evaluateApiRequestVO))
						.orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3003, "강의 평가 정보 수정 실패")));
	}

	public interface UpdateEvaluate {}



	/**
	 * 엑셀 다운로드 API
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@Tag(name = "Evaluate Management", description = "콘텐츠 관리 API")
	@Operation(operationId="Evaluate", summary = "평가지 엑셀", description = "평가지 엑셀 파일을 다운로드한다.")
	@GetMapping(value = "/excel")
	public void getListExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Date date = new Date();
		String dateToStr = DateFormatUtils.format(date, "yyyyMMdd_HHmmSS_");

		List<EvaluateExcelVO> evaluateExcelVOList = evaluateService.getExcel((EvaluateViewRequestVO) params(EvaluateViewRequestVO.class, CSSearchMap.of(request), null));
		OneSheetExcelFile<EvaluateExcelVO> excelFile = new OneSheetExcelFile<>(evaluateExcelVOList, EvaluateExcelVO.class);
		response.setHeader("Content-Disposition", "attachment; filename=" + dateToStr+URLEncoder.encode("평가지리스트", "UTF-8") + ".xlsx;");
		response.setHeader("Content-Transfer-Encoding", "binary");

		excelFile.write(response.getOutputStream());
	}
}
