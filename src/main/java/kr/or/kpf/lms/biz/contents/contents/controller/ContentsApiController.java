package kr.or.kpf.lms.biz.contents.contents.controller;

import javax.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.or.kpf.lms.biz.contents.contents.service.ContentsService;
import kr.or.kpf.lms.biz.contents.contents.vo.CreateContents;
import kr.or.kpf.lms.biz.contents.contents.vo.UpdateContents;
import kr.or.kpf.lms.biz.contents.contents.vo.request.ContentsApiRequestVO;
import kr.or.kpf.lms.biz.contents.contents.vo.request.ContentsFileManagementApiRequestVO;
import kr.or.kpf.lms.biz.contents.contents.vo.request.ContentsFileManagementViewRequestVO;
import kr.or.kpf.lms.biz.contents.contents.vo.request.ContentsViewRequestVO;
import kr.or.kpf.lms.biz.contents.contents.vo.response.ContentsApiResponseVO;
import kr.or.kpf.lms.biz.contents.contents.vo.response.ContentsExcelVO;
import kr.or.kpf.lms.biz.contents.contents.vo.response.ContentsViewResponseVO;
import kr.or.kpf.lms.common.exceldownload.excel.OneSheetExcelFile;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSApiControllerSupport;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 콘텐츠 관리 > 콘텐츠 관리 API 관련 Controller
 */
@Tag(name = "Contents Management", description = "콘텐츠 관리 API")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/contents/list")
public class ContentsApiController extends CSApiControllerSupport {
	
	private final ContentsService contentsService;

	/**
	 * Sample (객체 Swagger 표출을 위해서 사용)
	 *
	 * @param request
	 * @param response
	 * @param contentsViewRequestVO
	 * @return
	 */
	@Tag(name = "Swagger USE(NOT API)", description = "사용하지 않는 API(객체 표출용)")
	@PostMapping(value = "/sample", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ContentsViewResponseVO> swaggerUse1(HttpServletRequest request, HttpServletResponse response, @RequestBody ContentsViewRequestVO contentsViewRequestVO) {
		return null;
	}

	/**
	 * 콘텐츠 정보 조회
	 *
	 * @param request
	 * @param pageable
	 * @param model
	 * @return
	 */
	@Tag(name = "Contents Management", description = "콘텐츠 관리 API")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "콘텐츠 정보 조회 성공", content = @Content(schema = @Schema(implementation = ContentsApiResponseVO.class)))})
	@Operation(operationId="Contents", summary = "콘텐츠 정보 조회", description = "콘텐츠 정보을 조회한다.")
	@GetMapping(path = {"", "/"})
	public ResponseEntity<Object> getContentsInfo(HttpServletRequest request, Pageable pageable, Model model){
		return ResponseEntity.status(HttpStatus.OK)
				.body(Optional.ofNullable(CSSearchMap.of(request))
						.map(searchMap -> resultPaging(contentsService.getList((ContentsViewRequestVO) params(ContentsViewRequestVO.class, searchMap, pageable)), new ArrayList<>()))
						.orElse(new HashMap<>()));
	}

	/**
	 * 콘텐츠 정보 생성 API
	 *
	 * @param request
	 * @param response
	 * @param contentsApiRequestVO
	 * @return
	 */
	@Tag(name = "Contents Management", description = "콘텐츠 관리 API")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "콘텐츠 정보 생성 성공", content = @Content(schema = @Schema(implementation = ContentsApiResponseVO.class)))})
	@Operation(operationId="Contents", summary = "콘텐츠 정보 생성", description = "콘텐츠 정보를 생성한다.")
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ContentsApiResponseVO> createContentsInfo(HttpServletRequest request, HttpServletResponse response,
		 @Validated(value = {CreateContents.class}) @NotNull @RequestBody ContentsApiRequestVO contentsApiRequestVO) {
        return ResponseEntity.status(HttpStatus.OK)
    			.body(Optional.ofNullable(contentsService.createContentsInfo(contentsApiRequestVO))
						.orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2001, "콘텐츠 정보 생성 실패")));
	}

	/**
	 * 콘텐츠 정보 업데이트 API
	 *
	 * @param request
	 * @param response
	 * @param contentsApiRequestVO
	 * @return
	 */
	@Tag(name = "Contents Management", description = "콘텐츠 관리 API")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "콘텐츠 정보 수정 성공", content = @Content(schema = @Schema(implementation = ContentsApiResponseVO.class)))})
	@Operation(operationId="Contents", summary = "콘텐츠 정보 업데이트", description = "콘텐츠 정보를 업데이트한다.")
	@PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ContentsApiResponseVO> updateContentsInfo(HttpServletRequest request, HttpServletResponse response,
		 @Validated(value = {UpdateContents.class}) @NotNull @RequestBody ContentsApiRequestVO contentsApiRequestVO) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(Optional.ofNullable(contentsService.updateContentsInfo(contentsApiRequestVO))
						.orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2003, "콘텐츠 정보 수정 실패")));
	}

	/**
	 * 콘텐츠 썸네일 파일 업로드 API
	 *
	 * @param request
	 * @param response
	 * @param contentsCode
	 * @param thumbnailFile
	 * @return
	 */
	@Tag(name = "Contents Management", description = "콘텐츠 관리 API")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "콘텐츠 썸네일 파일 업로드 성공", content = @Content(schema = @Schema(implementation = ContentsApiResponseVO.class)))})
	@Operation(operationId = "Contents", summary = "콘텐츠 썸네일 파일 업로드", description = "콘텐츠 썸네일 파일을 업로드한다.")
	@PutMapping(value = "/upload/thumbnailFile/{contentsCode}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<CSResponseVOSupport> fileUpload(HttpServletRequest request, HttpServletResponse response,
														  @Parameter(description = "콘텐츠 코드") @PathVariable(value = "contentsCode", required = true) String contentsCode,
														  @Parameter(description = "썸네일 파일") @RequestPart(value = "thumbnailFile", required = true) MultipartFile thumbnailFile) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(Optional.ofNullable(contentsService.thumbnailFileUpload(contentsCode, thumbnailFile))
						.orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9005, "콘텐츠 썸네일 파일 업로드 실패")));
	}

	/**
	 * 콘텐츠 폴더 관리
	 *
	 * @param request
	 * @param response
	 * @param contentsCode
	 * @param folderList
	 * @return
	 */
	@Tag(name = "Contents Management", description = "콘텐츠 관리 API")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "콘텐츠 폴더 변경 성공", content = @Content(schema = @Schema(implementation = ContentsApiResponseVO.class)))})
	@Operation(operationId = "Contents", summary = "콘텐츠 폴더 관리", description = "콘텐츠 폴더 추가/이동/삭제를 관리한다.")
	@PutMapping(value = "/folder-management/{contentsCode}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<CSResponseVOSupport> folderManagement(HttpServletRequest request, HttpServletResponse response,
																@Parameter(description = "콘텐츠 코드") @PathVariable(value = "contentsCode", required = true) String contentsCode,
																@NotNull @RequestBody List<ContentsFileManagementApiRequestVO> folderList) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(Optional.ofNullable(contentsService.folderManagement(contentsCode, folderList))
						.orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2005, "폴더 업데이트 실패")));
	}

	/**
	 * 콘텐츠 파일 업로드 API
	 *
	 * @param request
	 * @param response
	 * @param contentsCode
	 * @param contentsFileList
	 * @return
	 */
	@Tag(name = "Contents Management", description = "콘텐츠 관리 API")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "콘텐츠 파일 업로드 성공", content = @Content(schema = @Schema(implementation = ContentsApiResponseVO.class)))})
	@Operation(operationId = "Contents", summary = "콘텐츠 파일 업로드", description = "콘텐츠 파일을 업로드한다.")
	@PutMapping(value = "/folder-management/file/{contentsCode}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<CSResponseVOSupport> fileUpload(HttpServletRequest request, HttpServletResponse response,
														  @Parameter(description = "콘텐츠 코드") @PathVariable(value = "contentsCode", required = true) String contentsCode,
														  @Parameter(description = "콘텐츠 파일 경로") @RequestParam(value = "filePath", required = true) String filePath,
														  @Parameter(description = "콘텐츠 파일") @RequestPart(value = "contentsFile", required = true) MultipartFile[] contentsFileList) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(Optional.ofNullable(contentsService.fileUpload(contentsCode, filePath, contentsFileList))
						.orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9005, "콘텐츠 파일 업로드 실패")));
	}

	/**
	 * 콘텐츠 압축 파일 업로드 API
	 *
	 * @param request
	 * @param response
	 * @param contentsCode
	 * @param zipFileList
	 * @return
	 */
	@Tag(name = "Contents Management", description = "콘텐츠 관리 API")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "콘텐츠 압축 파일 업로드 성공", content = @Content(schema = @Schema(implementation = ContentsApiResponseVO.class)))})
	@Operation(operationId = "Contents", summary = "콘텐츠 압축 파일 업로드", description = "콘텐츠 압축 파일을 업로드한다.")
	@PutMapping(value = "/folder-management/zip-file/{contentsCode}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<CSResponseVOSupport> zipFileUpload(HttpServletRequest request, HttpServletResponse response,
														  @Parameter(description = "콘텐츠 코드") @PathVariable(value = "contentsCode", required = true) String contentsCode,
														  @Parameter(description = "콘텐츠 파일 경로") @RequestParam(value = "filePath", required = true) String filePath,
														  @Parameter(description = "압축 파일") @RequestPart(value = "zipFileList", required = true) MultipartFile[] zipFileList) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(Optional.ofNullable(contentsService.zipFileUpload(contentsCode, filePath, zipFileList))
						.orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9005, "콘텐츠 압축 파일 업로드 실패")));
	}

	/**
	 * 콘텐츠 폴더 관련 파일리스트 조회
	 *
	 * @param request
	 * @param pageable
	 * @param model
	 * @return
	 */
	@Tag(name = "Contents Management", description = "콘텐츠 관리 API")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "파일 조회 성공", content = @Content(schema = @Schema(implementation = ContentsApiResponseVO.class)))})
	@Operation(operationId = "Contents", summary = "콘텐츠 파일 조회", description = "콘텐츠 파일 조회한다.")
	@GetMapping(path = {"/folder-management/file-list"})
	public ResponseEntity<Object> getFileList(HttpServletRequest request, Pageable pageable, Model model){
		return ResponseEntity.status(HttpStatus.OK)
					.body(Optional.ofNullable(CSSearchMap.of(request))
							.map(searchMap -> contentsService.getFileList((ContentsFileManagementViewRequestVO) params(ContentsFileManagementViewRequestVO.class, searchMap, pageable)))
							.orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
	}



	/**
	 * 엑셀 다운로드 API
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@Tag(name = "Contents Management", description = "콘텐츠 관리 API")
	@Operation(operationId = "Contents", summary = "콘텐츠 엑셀", description = "콘텐츠 엑셀 파일을 다운로드한다.")
	@GetMapping(value = "/excel")
	public void getListExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Date date = new Date();
		String dateToStr = DateFormatUtils.format(date, "yyyyMMdd_HHmmSS_");

		List<ContentsExcelVO> contentsExcelVOList = contentsService.getExcel((ContentsViewRequestVO) params(ContentsViewRequestVO.class, CSSearchMap.of(request), null));
		OneSheetExcelFile<ContentsExcelVO> excelFile = new OneSheetExcelFile<>(contentsExcelVOList, ContentsExcelVO.class);
		response.setHeader("Content-Disposition", "attachment; filename=" + dateToStr+URLEncoder.encode("콘텐츠리스트", "UTF-8") + ".xlsx;");
		response.setHeader("Content-Transfer-Encoding", "binary");

		excelFile.write(response.getOutputStream());
	}
}
