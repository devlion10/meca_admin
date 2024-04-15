package kr.or.kpf.lms.biz.homepage.archive.data.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.homepage.archive.data.service.ArchiveService;
import kr.or.kpf.lms.biz.homepage.archive.data.vo.request.ArchiveViewRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.data.vo.request.PublicationFileManagementViewRequestVO;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.common.support.Code;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
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
 * 홈페이지 관리 > 교재자료 View 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/homepage/archive")
public class ArchiveViewController extends CSViewControllerSupport {

    private static final String ARCHIVE = "views/homepage/";
    private final ArchiveService archiveService;

    /**
     * 홈페이지 관리 > 자료실 > 0: 기타 자료, 1: 교재 자료, 2: 연구/통계 자료, 3: 영상 자료, 4: 사업결과물 상세 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path={"/etc-data"})
    public String getEtcDataList(HttpServletRequest request, Pageable pageable, Model model) {
        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("materialCategory", Code.MTRL_CTGR.ETC.enumCode);
        modelSetting(model, Optional.ofNullable(requestParam)
                .map(searchMap -> archiveService.getList((ArchiveViewRequestVO) params(ArchiveViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("TEAM_CTGR", "MTRL_CTGR", "MTRL_TYPE"));
        return new StringBuilder(ARCHIVE).append("etcFile").toString();
    }

    /**
     * 홈페이지 관리 > 자료실 > 0: 기타 자료, 1: 교재 자료, 2: 연구/통계 자료, 3: 영상 자료, 4: 사업결과물 등록
     */
    @GetMapping(path={"/etc-data/"})
    public String getEtcDataWrite(Model model) {
        commonCodeSetting(model, Arrays.asList("TEAM_CTGR", "MTRL_CTGR", "MTRL_TYPE"));
        return new StringBuilder(ARCHIVE).append("etcFileForm").toString();
    }

    /**
     * 홈페이지 관리 > 자료실 > 0: 기타 자료, 1: 교재 자료, 2: 연구/통계 자료, 3: 영상 자료, 4: 사업결과물 상세 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @param sequenceNo
     * @return
     */
    @GetMapping(path={"/etc-data/{sequenceNo}"})
    public String getEtcDataForm(HttpServletRequest request, Pageable pageable, Model model,
                                 @Parameter(description = "자료실 시리얼 번호") @PathVariable(value = "sequenceNo", required = true) Long sequenceNo) {
        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("materialCategory", Code.MTRL_CTGR.ETC.enumCode);
        requestParam.put("sequenceNo", sequenceNo);
        modelSetting(model, Optional.ofNullable(requestParam)
                .map(searchMap -> archiveService.getList((ArchiveViewRequestVO) params(ArchiveViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("TEAM_CTGR", "MTRL_CTGR", "MTRL_TYPE"));
        return new StringBuilder(ARCHIVE).append("etcFileForm").toString();
    }

    /**
     * 홈페이지 관리 > 자료실 > 0: 기타 자료, 1: 교재 자료, 2: 연구/통계 자료, 3: 영상 자료, 4: 사업결과물 상세 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path={"/edu-data"})
    public String getEduDataList(HttpServletRequest request, Pageable pageable, Model model) {
        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("materialCategory", Code.MTRL_CTGR.A.enumCode);
        modelSetting(model, Optional.ofNullable(requestParam)
                .map(searchMap -> archiveService.getList((ArchiveViewRequestVO) params(ArchiveViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("TEAM_CTGR", "MTRL_CTGR", "MTRL_TYPE"));
        return new StringBuilder(ARCHIVE).append("eduFile").toString();
    }

    /**
     * 홈페이지 관리 > 자료실 > 0: 기타 자료, 1: 교재 자료, 2: 연구/통계 자료, 3: 영상 자료, 4: 사업결과물 등록
     */
    @GetMapping(path={"/edu-data/"})
    public String getEduDataWrite(Model model) {
        commonCodeSetting(model, Arrays.asList("TEAM_CTGR", "MTRL_CTGR", "MTRL_TYPE"));
        return new StringBuilder(ARCHIVE).append("eduFileForm").toString();
    }

    /**
     * 홈페이지 관리 > 자료실 > 0: 기타 자료, 1: 교재 자료, 2: 연구/통계 자료, 3: 영상 자료, 4: 사업결과물 상세 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @param sequenceNo
     * @return
     */
    @GetMapping(path={"/edu-data/{sequenceNo}"})
    public String getEduDataForm(HttpServletRequest request, Pageable pageable, Model model,
                                 @Parameter(description = "자료실 시리얼 번호") @PathVariable(value = "sequenceNo", required = true) Long sequenceNo) {
        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("materialCategory", Code.MTRL_CTGR.A.enumCode);
        requestParam.put("sequenceNo", sequenceNo);
        modelSetting(model, Optional.ofNullable(requestParam)
                .map(searchMap -> archiveService.getList((ArchiveViewRequestVO) params(ArchiveViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("TEAM_CTGR", "MTRL_CTGR", "MTRL_TYPE"));
        return new StringBuilder(ARCHIVE).append("eduFileForm").toString();
    }

    /** 홈페이지 관리 > 자료실 > 0: 기타 자료, 1: 교재 자료, 2: 연구/통계 자료, 3: 영상 자료, 4: 사업결과물
     * 연구/간행물 */
    @GetMapping(path={"/publicing"})
    public String getPublication() {
        return new StringBuilder(ARCHIVE).append("publicing").toString();
    }

    /** 홈페이지 관리 > 자료실 > 0: 기타 자료, 1: 교재 자료, 2: 연구/통계 자료, 3: 영상 자료, 4: 사업결과물
     * 미디어리터러시 */
    @GetMapping(path={"/publicing/publication"})
    public String getResearchBook(HttpServletRequest request, Pageable pageable, Model model) {
        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("materialCategory", Code.MTRL_CTGR.E.enumCode);
        modelSetting(model, Optional.ofNullable(requestParam)
                .map(searchMap -> archiveService.getList((ArchiveViewRequestVO) params(ArchiveViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("TEAM_CTGR", "MTRL_CTGR", "MTRL_TYPE"));
        return new StringBuilder(ARCHIVE).append("publication").toString();
    }
    @GetMapping(path={"/publicing/publication/"})
    public String getResearchBookForm(Model model) {
        commonCodeSetting(model, Arrays.asList("TEAM_CTGR", "MTRL_CTGR", "MTRL_TYPE"));
        return new StringBuilder(ARCHIVE).append("publicationForm").toString();
    }
    @GetMapping(path={"/publicing/publication/{sequenceNo}"})
    public String getResearchBookForm(HttpServletRequest request, Pageable pageable, Model model,
                                      @Parameter(description = "자료실 시리얼 번호") @PathVariable(value = "sequenceNo", required = true) Long sequenceNo) {
        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("sequenceNo", sequenceNo);

        modelSetting(model, Optional.ofNullable(requestParam)
                .map(searchMap -> archiveService.getFolderManagement((PublicationFileManagementViewRequestVO) params(PublicationFileManagementViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("TEAM_CTGR", "MTRL_CTGR", "MTRL_TYPE"));
        return new StringBuilder(ARCHIVE).append("publicationForm").toString();
    }

    /**
     * 홈페이지 관리 > 자료실 > 0: 기타 자료, 1: 교재 자료, 2: 연구/통계 자료, 3: 영상 자료, 4: 사업결과물 상세 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path={"/publicing/research-data"})
    public String getResearchDataList(HttpServletRequest request, Pageable pageable, Model model) {
        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("materialCategory", Code.MTRL_CTGR.B.enumCode);
        modelSetting(model, Optional.ofNullable(requestParam)
                .map(searchMap -> archiveService.getList((ArchiveViewRequestVO) params(ArchiveViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("TEAM_CTGR", "MTRL_CTGR", "MTRL_TYPE"));
        return new StringBuilder(ARCHIVE).append("researchFile").toString();
    }

    /**
     * 홈페이지 관리 > 자료실 > 0: 기타 자료, 1: 교재 자료, 2: 연구/통계 자료, 3: 영상 자료, 4: 사업결과물 등록
     */
    @GetMapping(path={"/publicing/research-data/"})
    public String getResearchDataWrite(Model model) {
        commonCodeSetting(model, Arrays.asList("TEAM_CTGR", "MTRL_CTGR", "MTRL_TYPE"));
        return new StringBuilder(ARCHIVE).append("researchFileForm").toString();
    }

    /**
     * 홈페이지 관리 > 자료실 > 0: 기타 자료, 1: 교재 자료, 2: 연구/통계 자료, 3: 영상 자료, 4: 사업결과물 상세 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @param sequenceNo
     * @return
     */
    @GetMapping(path={"/publicing/research-data/{sequenceNo}"})
    public String getResearchDataForm(HttpServletRequest request, Pageable pageable, Model model,
                                      @Parameter(description = "자료실 시리얼 번호") @PathVariable(value = "sequenceNo", required = true) Long sequenceNo) {
        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("materialCategory", Code.MTRL_CTGR.B.enumCode);
        requestParam.put("sequenceNo", sequenceNo);
        modelSetting(model, Optional.ofNullable(requestParam)
                .map(searchMap -> archiveService.getList((ArchiveViewRequestVO) params(ArchiveViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("TEAM_CTGR", "MTRL_CTGR", "MTRL_TYPE"));
        return new StringBuilder(ARCHIVE).append("researchFileForm").toString();
    }

    /**
     * 홈페이지 관리 > 자료실 > 0: 기타 자료, 1: 교재 자료, 2: 연구/통계 자료, 3: 영상 자료, 4: 사업결과물 상세 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path={"/media"})
    public String getMediaList(HttpServletRequest request, Pageable pageable, Model model) {
        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("materialCategory", Code.MTRL_CTGR.C.enumCode);
        modelSetting(model, Optional.ofNullable(requestParam)
                .map(searchMap -> archiveService.getList((ArchiveViewRequestVO) params(ArchiveViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("TEAM_CTGR", "MTRL_CTGR", "MTRL_TYPE"));
        return new StringBuilder(ARCHIVE).append("media").toString();
    }

    /**
     * 홈페이지 관리 > 자료실 > 0: 기타 자료, 1: 교재 자료, 2: 연구/통계 자료, 3: 영상 자료, 4: 사업결과물 등록
     */
    @GetMapping(path={"/media/"})
    public String getMediaWrite(Model model) {
        commonCodeSetting(model, Arrays.asList("TEAM_CTGR", "MTRL_CTGR", "MTRL_TYPE"));
        return new StringBuilder(ARCHIVE).append("mediaForm").toString();
    }

    /**
     * 홈페이지 관리 > 자료실 > 0: 기타 자료, 1: 교재 자료, 2: 연구/통계 자료, 3: 영상 자료, 4: 사업결과물 상세 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @param sequenceNo
     * @return
     */
    @GetMapping(path={"/media/{sequenceNo}"})
    public String getMediaForm(HttpServletRequest request, Pageable pageable, Model model,
                               @Parameter(description = "자료실 시리얼 번호") @PathVariable(value = "sequenceNo", required = true) Long sequenceNo) {
        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("materialCategory", Code.MTRL_CTGR.C.enumCode);
        requestParam.put("sequenceNo", sequenceNo);
        modelSetting(model, Optional.ofNullable(requestParam)
                .map(searchMap -> archiveService.getList((ArchiveViewRequestVO) params(ArchiveViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("TEAM_CTGR", "MTRL_CTGR", "MTRL_TYPE"));
        return new StringBuilder(ARCHIVE).append("mediaForm").toString();
    }

    /** 홈페이지 관리 > 자료실 > 0: 기타 자료, 1: 교재 자료, 2: 연구/통계 자료, 3: 영상 자료, 4: 사업결과물
     * 사업결과물 선택 */
    @GetMapping(path={"/result"})
    public String getResult() {
        return new StringBuilder(ARCHIVE).append("results").toString();
    }

    /**
     * 홈페이지 관리 > 자료실 > 0: 기타 자료, 1: 교재 자료, 2: 연구/통계 자료, 3: 영상 자료, 4: 사업결과물
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path={"/results"})
    public String getResultList(HttpServletRequest request, Pageable pageable, Model model) {
        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("materialCategory", Code.MTRL_CTGR.D.enumCode);
        modelSetting(model, Optional.ofNullable(requestParam)
                .map(searchMap -> archiveService.getList((ArchiveViewRequestVO) params(ArchiveViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("TEAM_CTGR", "MTRL_CTGR", "MTRL_TYPE"));
        return new StringBuilder(ARCHIVE).append("results").toString();
    }

    /**
     * 홈페이지 관리 > 자료실 > 0: 기타 자료, 1: 교재 자료, 2: 연구/통계 자료, 3: 영상 자료, 4: 사업결과물 등록
     */
    @GetMapping(path={"/results/"})
    public String getResultWrite(Model model) {
        commonCodeSetting(model, Arrays.asList("TEAM_CTGR", "MTRL_CTGR", "MTRL_TYPE"));
        return new StringBuilder(ARCHIVE).append("resultsForm").toString();
    }

    /**
     * 홈페이지 관리 > 자료실 > 0: 기타 자료, 1: 교재 자료, 2: 연구/통계 자료, 3: 영상 자료, 4: 사업결과물
     *
     * @param request
     * @param pageable
     * @param model
     * @param sequenceNo
     * @return
     */
    @GetMapping(path={"/results/{sequenceNo}"})
    public String getResultView(HttpServletRequest request, Pageable pageable, Model model,
                                @Parameter(description = "자료실 시리얼 번호") @PathVariable(value = "sequenceNo", required = true) BigInteger sequenceNo) {

        CSSearchMap requestParam = CSSearchMap.of(request);
        requestParam.put("materialCategory", Code.MTRL_CTGR.D.enumCode);
        requestParam.put("sequenceNo", sequenceNo);
        modelSetting(model, Optional.ofNullable(requestParam)
                .map(searchMap -> archiveService.getList((ArchiveViewRequestVO) params(ArchiveViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("TEAM_CTGR", "MTRL_CTGR", "MTRL_TYPE"));
        return new StringBuilder(ARCHIVE).append("resultsForm").toString();
    }
}
