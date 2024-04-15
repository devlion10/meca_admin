package kr.or.kpf.lms.biz.contents.contents.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.contents.contents.service.ContentsService;
import kr.or.kpf.lms.biz.contents.contents.vo.request.ContentsFileManagementViewRequestVO;
import kr.or.kpf.lms.biz.contents.contents.vo.request.ContentsViewRequestVO;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * 콘텐츠 관리 > 콘텐츠 관리 View 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/contents/list")
public class ContentsViewController extends CSViewControllerSupport {

    private static final String CONTENTS = "views/contents/contents/";

    private final ContentsService contentsService;

    /**
     * 콘텐츠 관리 > 콘텐츠 관리 목록 조회
     * 
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path = {"", "/", "/page"})
    public String getList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                    .map(searchMap -> contentsService.getList((ContentsViewRequestVO) params(ContentsViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("CTS_CTGR"));
            return new StringBuilder(CONTENTS).append("contents").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /**
     * 콘텐츠 관리 > 콘텐츠 관리 상세 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path={"/view/{contentsCode}"})
    public String getContentsView(HttpServletRequest request, Pageable pageable, Model model,
                                @Parameter(description = "콘텐츠 코드") @PathVariable(value = "contentsCode", required = true) String contentsCode) {
        try {
            CSSearchMap csSearchMap = CSSearchMap.of(request);
            csSearchMap.put("contentsCode", contentsCode);
            modelSetting(model, Optional.ofNullable(csSearchMap)
                    .map(searchMap -> contentsService.getList((ContentsViewRequestVO) params(ContentsViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("CTS_CTGR"));
            model.addAttribute("viewType", "GENERAL");
            return new StringBuilder(CONTENTS).append("contentsView").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /**
     * 콘텐츠 관리 > 콘텐츠 등록
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path={"/regist"})
    public String getContentsForm(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            commonCodeSetting(model, Arrays.asList("CTS_CTGR"));
            return new StringBuilder(CONTENTS).append("contentsForm").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /**
     * 콘텐츠 관리 > 콘텐츠 관리 목록 > 콘텐츠 폴더 관리 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path = {"/folder-management/{contentsCode}", "/folder-management/page/{contentsCode}"})
    public String getFolderManagement(HttpServletRequest request, Pageable pageable, Model model,
                                      @Parameter(description = "콘텐츠 코드") @PathVariable(value = "contentsCode", required = true) String contentsCode) {
        try {
            CSSearchMap csSearchMap = CSSearchMap.of(request);
            csSearchMap.put("contentsCode", contentsCode);
            modelSetting(model, Optional.ofNullable(csSearchMap)
                    .map(searchMap -> contentsService.getFolderManagement((ContentsFileManagementViewRequestVO) params(ContentsFileManagementViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
            model.addAttribute("viewType", "FILE");
            return new StringBuilder(CONTENTS).append("contentsView").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /**
     * 콘텐츠 관리 > 콘텐츠 관리 목록 > 콘텐츠 폴더 관련 파일리스트 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path = {"/folder-management/file-list"})
    public String getFileList(HttpServletRequest request, Pageable pageable, Model model){
        try {
            modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                    .map(searchMap -> contentsService.getFileList((ContentsFileManagementViewRequestVO) params(ContentsFileManagementViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
            return new StringBuilder(CONTENTS).append("contentsView").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /**
     * 콘텐츠 관리 > 콘텐츠 관리 > 챕터 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path={"/chapter/view/{contentsCode}"})
    public String getChapterView(HttpServletRequest request, Pageable pageable, Model model,
                                  @Parameter(description = "콘텐츠 코드") @PathVariable(value = "contentsCode", required = true) String contentsCode) {
        try {
            CSSearchMap csSearchMap = CSSearchMap.of(request);
            csSearchMap.put("contentsCode", contentsCode);
            modelSetting(model, Optional.ofNullable(csSearchMap)
                    .map(searchMap -> contentsService.getList((ContentsViewRequestVO) params(ContentsViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
            model.addAttribute("viewType", "CHAPTER");
            return new StringBuilder(CONTENTS).append("contentsView").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }
}
