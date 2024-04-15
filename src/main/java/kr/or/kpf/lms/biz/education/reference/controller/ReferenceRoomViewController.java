package kr.or.kpf.lms.biz.education.reference.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.education.reference.service.ReferenceRoomService;
import kr.or.kpf.lms.biz.education.reference.vo.request.ReferenceRoomViewRequestVO;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import kr.or.kpf.lms.repository.entity.education.CurriculumMaster;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * 교육 관리 > 교육 과정 관리 VIEW 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/education/curriculum/reference-room")
public class ReferenceRoomViewController extends CSViewControllerSupport {

    private static final String EDUCATION = "views/education/curriculum/";

    private final ReferenceRoomService referenceRoomService;

    /**
     * 자료실 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path = {"/{curriculumCode}"})
    public String getList(HttpServletRequest request, Pageable pageable, Model model,
                          @Parameter(description = "교육 과정 코드") @PathVariable(value = "curriculumCode", required = true) String curriculumCode) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("curriculumCode", curriculumCode);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> referenceRoomService.getList((ReferenceRoomViewRequestVO) params(ReferenceRoomViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("CTS_CTGR", "EDU_TYPE"));
            CurriculumMaster curriculumMaster = referenceRoomService.getCurriculumInfo(curriculumCode);
            model.addAttribute("curriculumCode", curriculumMaster.getCurriculumCode());
            model.addAttribute("curriculumName", curriculumMaster.getCurriculumName());
            model.addAttribute("educationCategoryCode", curriculumMaster.getCategoryCode());
            model.addAttribute("educationType", curriculumMaster.getEducationType());
            return new StringBuilder(EDUCATION).append("archive").toString();
        } catch (KPFException e1) {
            logger.error("{}- {}", e1.getClass().getCanonicalName(), e1.getMessage(), e1);
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            logger.error("{}- {}", e2.getClass().getCanonicalName(), e2.getMessage(), e2);
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /**
     * 자료실 등록
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path = {"/regist/{curriculumCode}"})
    public String regist(HttpServletRequest request, Pageable pageable, Model model,
                          @Parameter(description = "교육 과정 코드") @PathVariable(value = "curriculumCode", required = true) String curriculumCode) {
        try {
            commonCodeSetting(model, Arrays.asList("CTS_CTGR", "EDU_TYPE"));
            CurriculumMaster curriculumMaster = referenceRoomService.getCurriculumInfo(curriculumCode);
            model.addAttribute("curriculumCode", curriculumMaster.getCurriculumCode());
            model.addAttribute("curriculumName", curriculumMaster.getCurriculumName());
            model.addAttribute("educationCategoryCode", curriculumMaster.getCategoryCode());
            model.addAttribute("educationType", curriculumMaster.getEducationType());
            return new StringBuilder(EDUCATION).append("archiveForm").toString();
        } catch (KPFException e1) {
            logger.error("{}- {}", e1.getClass().getCanonicalName(), e1.getMessage(), e1);
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            logger.error("{}- {}", e2.getClass().getCanonicalName(), e2.getMessage(), e2);
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /**
     * 자료실 개별 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path = {"/{curriculumCode}/{sequenceNo}"})
    public String getForm(HttpServletRequest request, Pageable pageable, Model model,
                          @Parameter(description = "교육 과정 코드") @PathVariable(value = "curriculumCode", required = true) String curriculumCode,
                          @Parameter(description = "자료실 시퀀스 번호") @PathVariable(value = "sequenceNo", required = true) BigInteger sequenceNo) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("curriculumCode", curriculumCode);
            requestParam.put("sequenceNo", sequenceNo);
            modelSetting(model, Optional.ofNullable(requestParam)
                    .map(searchMap -> referenceRoomService.getList((ReferenceRoomViewRequestVO) params(ReferenceRoomViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("CTS_CTGR", "EDU_TYPE"));
            CurriculumMaster curriculumMaster = referenceRoomService.getCurriculumInfo(curriculumCode);
            model.addAttribute("curriculumCode", curriculumMaster.getCurriculumCode());
            model.addAttribute("curriculumName", curriculumMaster.getCurriculumName());
            model.addAttribute("educationCategoryCode", curriculumMaster.getCategoryCode());
            model.addAttribute("educationType", curriculumMaster.getEducationType());
            return new StringBuilder(EDUCATION).append("archiveForm").toString();
        } catch (KPFException e1) {
            logger.error("{}- {}", e1.getClass().getCanonicalName(), e1.getMessage(), e1);
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            logger.error("{}- {}", e2.getClass().getCanonicalName(), e2.getMessage(), e2);
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }
}
