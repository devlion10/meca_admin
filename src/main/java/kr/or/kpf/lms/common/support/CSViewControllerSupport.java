package kr.or.kpf.lms.common.support;

import kr.or.kpf.lms.repository.system.CommonCodeMasterRepository;
import kr.or.kpf.lms.repository.entity.system.CommonCodeMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * View Controller Support
 */
@ControllerAdvice
public abstract class CSViewControllerSupport extends CSComponentSupport {

	@Autowired
	protected CommonCodeMasterRepository commonCodeMasterRepository;

	protected CSViewControllerSupport() { super(); }

	/** Exception 시 ERROR 화면 리턴 */
	@ExceptionHandler({ NoHandlerFoundException.class })
	public ModelAndView handleBadRequest(Exception ex) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("views/error/errorForm");
		mv.addObject("errorMessage", ex.getMessage());
		return mv;
	}

	/** @PageableDefault(size = 20) 기본 크기 조정 요청으로 인한 추가 */
	protected void modelSetting(Model model, @PageableDefault(size = 20) Page<Object> result) {
		modelSetting(model, result, null);
	}

	/** @PageableDefault(size = 20) 기본 크기 조정 요청으로 인한 추가 */
	protected void modelSetting(Model model, @PageableDefault(size = 20) Page<Object> result, List<String> upIndividualCodeList) {
		/** 내용 */
		model.addAttribute("content", result.getContent());
		/** 페이징 정보 */
		Map<String, Object> pageable = new HashMap<>();
		/** 첫번째 페이지 여부 */
		pageable.put("first", result.isFirst());
		/** 마지막 페이지 여부 */
		pageable.put("last", result.isLast());
		/** 현재 페이지 데이터 갯수 */
		pageable.put("currentElements", result.getNumberOfElements());
		/** 검색 조건에 해당하는 데이터 총 갯수 */
		pageable.put("totalElements", result.getTotalElements());
		/** 페이지 당 요청 갯수 */
		pageable.put("size", result.getSize());
		/** 요청 페이지 */
		pageable.put("page", result.getNumber());
		/** 전체 페이지 */
		pageable.put("totalPages", result.getTotalPages());
		/** 이전 블록 존재 여부(한 블록당 10페이지) */
		if(result.getNumber() / 10 < 1) pageable.put("hasPrevious", false);
		else pageable.put("hasPrevious", true);
		/** 이후 블록 존재 여부(한 블록당 10페이지) */
		if(Math.floor(result.getNumber() / 10) < Math.floor((result.getTotalPages() - 1) / 10)) pageable.put("hasNext", true);
		else pageable.put("hasNext", false);

		int blockStart = ((result.getNumber() / 10) * 10);
		pageable.put("blockStart", blockStart);
		int blockEnd = blockStart + 9;
		pageable.put("blockEnd", blockEnd < result.getTotalPages() - 1 ? blockEnd : (result.getTotalPages() - 1 > 0 ? result.getTotalPages() - 1 : 0));

		model.addAttribute("pageable", pageable);
		/** 공통 코드 정보 셋팅 */
		commonCodeSetting(model, upIndividualCodeList);
	}

	/**
	 * 공통 코드 정보 셋팅
	 *
	 * @param model
	 * @param upIndividualCodeList
	 */
	protected void commonCodeSetting(Model model, List<String> upIndividualCodeList) {
		/** 제공할 공통 코드 리스트가 존재할 경우에만 공통 코드 조회 */
		Optional.ofNullable(upIndividualCodeList).filter(datas -> datas.size() > 0).ifPresent(codes -> {
			model.addAttribute("commonCode", codes.stream()
					.map(code -> Optional.ofNullable(Optional.ofNullable(commonCodeMasterRepository.findOne(Example.of(CommonCodeMaster.builder().code(code).build())).orElse(null))
									.map(topCode -> NameOfCode.builder()
											.code(topCode.getCode())
											.codeName(topCode.getCodeName())
											.subCode(commonCodeMasterRepository.findAll(Example.of(CommonCodeMaster.builder()
													.upIndividualCode(topCode.getIndividualCode())
													.codeDepth(1)
													.build())).stream().map(subCode -> NameOfCode.builder()
													.code(subCode.getCode())
													.codeName(subCode.getCodeName())
													.build()).collect(Collectors.toList()))
											.build()).orElse(null))
							.orElse(null))
					.filter(data -> data != null)
					.collect(Collectors.toList()));
		});
	}
}
