package kr.or.kpf.lms.biz.system.code.controller;

import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.system.code.service.CommonCodeService;
import kr.or.kpf.lms.biz.system.code.vo.CreateCommonCode;
import kr.or.kpf.lms.biz.system.code.vo.UpdateCommonCode;
import kr.or.kpf.lms.biz.system.code.vo.request.CommonCodeApiRequestVO;
import kr.or.kpf.lms.biz.system.code.vo.request.CommonCodeViewRequestVO;
import kr.or.kpf.lms.biz.system.code.vo.response.CommonCodeApiResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Optional;

/**
 * 시스템 관리 > 공통 코드 관리 API 관련 Controller
 */
@Tag(name = "System Management", description = "시스템 관리 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/system/code/")
public class CommonCodeApiController extends CSApiControllerSupport {

	private final CommonCodeService commonCodeService;

	/**
	 * 공통 코드 조회 API
	 *
	 * @param request
	 * @param response
	 * @param pageable
	 * @param upIndividualCode
	 * @param codeName
	 * @return
	 */
	@Tag(name = "System Management", description = "시스템 관리 API")
	@ApiImplicitParams(
			{@ApiImplicitParam(
					name = "codeName"
					, value = "상위 코드명"
					, required = false
					, dataType = "string"
					, example = "교육"
					, paramType = "query"
					, defaultValue = ""),
			@ApiImplicitParam(
					name = "upIndividualCode"
					, value = "상위 코드"
					, required = false
					, dataType = "string"
					, example = "CODE00008"
					, paramType = "query"
					, defaultValue = "")
			})
	@Operation(operationId = "System", summary = "공통 코드 조회 ", description = "공통 코드 조회 데이터를 생성한다.")
	@GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getList(HttpServletRequest request, HttpServletResponse response, Pageable pageable,
										  @RequestParam(value="upIndividualCode", required = false) String upIndividualCode, @RequestParam(value="codeName", required = false) String codeName) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(Optional.ofNullable(CSSearchMap.of(request))
						.map(searchMap -> commonCodeService.getList((CommonCodeViewRequestVO) params(CommonCodeViewRequestVO.class, searchMap, pageable)))
						.orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
	}
	
	/**
	 * 공통 코드 정보 생성
	 *
	 * @param request
	 * @param response
	 * @param commonCodeApiRequestVO
	 * @return
	 */
	@Tag(name = "System Management", description = "시스템 관리 API")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "공통 코드 정보 생성 성공", content = @Content(schema = @Schema(implementation = CommonCodeApiResponseVO.class)))})
	@Operation(operationId="CommonCode", summary = "공통 코드 정보 생성", description = "공통 코드 정보를 생성한다.")
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommonCodeApiResponseVO> createCommonCodeInfo(HttpServletRequest request, HttpServletResponse response,
		 @Validated(value = {CreateCommonCode.class}) @NotNull @RequestBody CommonCodeApiRequestVO commonCodeApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
    			.body(Optional.ofNullable(commonCodeService.createCommonCodeInfo(commonCodeApiRequestVO))
						.orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7001, "공통 코드 정보 생성 실패")));
	}

	/**
	 * 공통 코드 정보 업데이트
	 *
	 * @param request
	 * @param response
	 * @param commonCodeApiRequestVO
	 * @return
	 */
	@Tag(name = "System Management", description = "시스템 관리 API")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "공통 코드 정보 수정 성공", content = @Content(schema = @Schema(implementation = CommonCodeApiResponseVO.class)))})
	@Operation(operationId="CommonCode", summary = "공통 코드 정보 업데이트", description = "공통 코드 정보를 업데이트한다.")
	@PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommonCodeApiResponseVO> updateCommonCodeInfo(HttpServletRequest request, HttpServletResponse response,
		@Validated(value = {UpdateCommonCode.class}) @NotNull @RequestBody CommonCodeApiRequestVO commonCodeApiRequestVO) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(Optional.ofNullable(commonCodeService.updateCommonCodeInfo(commonCodeApiRequestVO))
						.orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7003, "공통 코드 정보 수정 실패")));
	}

	/**
	 * 공통 코드 교과 순서 변경 API
	 *
	 * @param request
	 * @param response
	 * @param commonCodeApiRequestVO
	 * @return
	 */
	@Tag(name = "System Management", description = "시스템 관리 API")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "공통 코드 교과 순서 변경 성공", content = @Content(schema = @Schema(implementation = CommonCodeApiResponseVO.class)))})
	@Operation(operationId = "CommonCode", summary = "공통 코드 교과 순서 변경", description = "공통 코드 교과 데이터 순서 변경한다.")
	@PutMapping(value = "/sort", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommonCodeApiResponseVO> changeCommonCode(HttpServletRequest request, HttpServletResponse response,
																		@Validated(value = {UpdateCommonCode.class}) @NotNull @RequestBody CommonCodeApiRequestVO commonCodeApiRequestVO) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(Optional.ofNullable(commonCodeService.changeOrder(commonCodeApiRequestVO))
						.orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7102, "공통 코드 교과 데이터 순서 변경 실패")));
	}
}
