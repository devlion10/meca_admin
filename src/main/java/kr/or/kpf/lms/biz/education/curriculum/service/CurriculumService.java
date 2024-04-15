package kr.or.kpf.lms.biz.education.curriculum.service;

import kr.or.kpf.lms.biz.business.instructor.identify.vo.request.BizInstructorIdentifyViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.response.BizInstructorIdentifyExcelVO;
import kr.or.kpf.lms.biz.education.curriculum.vo.CurriculumExcelVO;
import kr.or.kpf.lms.biz.education.curriculum.vo.request.CurriculumApiRequestVO;
import kr.or.kpf.lms.biz.education.curriculum.vo.request.CurriculumViewRequestVO;
import kr.or.kpf.lms.biz.education.curriculum.vo.response.CurriculumApiResponseVO;
import kr.or.kpf.lms.common.exceldownload.excel.OneSheetExcelFile;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.common.support.Code;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonEducationRepository;
import kr.or.kpf.lms.repository.contents.ContentsMasterRepository;
import kr.or.kpf.lms.repository.education.CurriculumMasterRepository;
import kr.or.kpf.lms.repository.contents.EvaluateMasterRepository;
import kr.or.kpf.lms.repository.education.CurriculumCollaborationRepositroy;
import kr.or.kpf.lms.repository.education.CurriculumContentsRepository;
import kr.or.kpf.lms.repository.education.CurriculumEvaluateRepository;
import kr.or.kpf.lms.repository.entity.BizSurveyQitem;
import kr.or.kpf.lms.repository.entity.contents.ContentsMaster;
import kr.or.kpf.lms.repository.entity.contents.EvaluateMaster;
import kr.or.kpf.lms.repository.entity.education.CurriculumCollaboration;
import kr.or.kpf.lms.repository.entity.education.CurriculumContents;
import kr.or.kpf.lms.repository.entity.education.CurriculumEvaluate;
import kr.or.kpf.lms.repository.entity.education.CurriculumMaster;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 교육 관리 > 교육 과정 관리 API 관련 Service
 */
@Service
@RequiredArgsConstructor
public class CurriculumService extends CSServiceSupport {

    private static final String PREFIX_CURRICULUM = "CRCL";

    private static final String APLY_IMG_TAG = "_APLYFORM";

    /** 교육 관리 공통 */
    private final CommonEducationRepository commonEducationRepository;
    /** 교육 과정 관리 */
    private final CurriculumMasterRepository curriculumMasterRepository;
    /** 콘텐츠 관리 */
    private final ContentsMasterRepository contentsMasterRepository;
    /** 강의평가 관리 */
    private final EvaluateMasterRepository evaluateMasterRepository;
    /** 교육 과정 콘텐츠 목록 */
    private final CurriculumContentsRepository curriculumContentsRepository;
    /** 교육 과정 연계 교육 과정 목록 */
    private final CurriculumCollaborationRepositroy curriculumCollaborationRepository;
    /** 교육 과정 연계 설문 목록 */
    private final CurriculumEvaluateRepository curriculumEvaluateRepository;

    /**
     * 교육 과정 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(CurriculumViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonEducationRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 엑셀 다운로드 (메모리 이슈 발생 시 CSV 파일 다운로드 변경 필요)
     *
     */
    public <T> List<CurriculumExcelVO> getExcel(CurriculumViewRequestVO requestObject) {
        List<CurriculumExcelVO> bizInstructorIdentifyExcelVOList = (List<CurriculumExcelVO>) commonEducationRepository.excelDownload(requestObject);
        return bizInstructorIdentifyExcelVOList;

    }
    /**
     * 교육 과정 생성
     *
     * @param curriculumApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CurriculumApiResponseVO createCurriculumInfo(CurriculumApiRequestVO curriculumApiRequestVO) {
        CurriculumMaster entity = CurriculumMaster.builder().build();
        copyNonNullObject(curriculumApiRequestVO, entity);
        if (curriculumMasterRepository.findOne(Example.of(CurriculumMaster.builder()
                                                            .curriculumName(curriculumApiRequestVO.getCurriculumName())
                                                            .build()))
                .isPresent()) { /** 기존 등록된 교육 과정 정보 확인 */
            throw new KPFException(KPF_RESULT.ERROR3002, "동일 교육 과정 존재");
        } else {
            CurriculumApiResponseVO result = CurriculumApiResponseVO.builder().build();
            entity.setCurriculumCode(commonEducationRepository.generateCurriculumCode(PREFIX_CURRICULUM));

            /** 교육 과정 연결 콘텐츠 목록 생성 */
            Optional.ofNullable(curriculumApiRequestVO.getContentsList())
                    .filter(list -> list != null && list.size() > 0)
                    .ifPresent(list -> {
                        list.stream().map(data -> {
                            CurriculumContents imsiContents = CurriculumContents.builder()
                                    .curriculumCode(entity.getCurriculumCode())
                                    .contentsCode(data.getValue())
                                    .build();
                            return curriculumContentsRepository.findOne(Example.of(imsiContents))
                                    .map(con -> {
                                        con.setSortNo(data.getSortNo());
                                        return curriculumContentsRepository.save(con);
                                    })
                                    .orElseGet(() -> { /** 연결된 콘텐츠 정보가 존재하지 않으면... */
                                        imsiContents.setSortNo(data.getSortNo());
                                        /** 관련 콘텐츠 정보가 존재하는지 확인 */
                                        contentsMasterRepository.findOne(Example.of(ContentsMaster.builder()
                                                        .contentsCode(data.getValue())
                                                        .build()))
                                                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2004, "관련 콘텐츠 정보가 존재하지 않습니다. (" + data.getValue() + ")"));
                                        /** 생성된 교육과정과 해당 콘텐츠 연결 */
                                        return curriculumContentsRepository.save(imsiContents);
                                    });
                        }).collect(Collectors.toList());
                    });

            /** 교육 과정 연계 교육 과정 목록 생성 */
            Optional.ofNullable(curriculumApiRequestVO.getCurriculumCollaborationList())
                    .filter(list -> list != null && list.size() > 0)
                    .ifPresent(list -> {
                        list.stream().map(data -> {
                            CurriculumCollaboration imsiCurriculum = CurriculumCollaboration.builder()
                                    .curriculumCode(entity.getCurriculumCode())
                                    .referenceCurriculumCode(data.getValue())
                                    .build();
                            return curriculumCollaborationRepository.findOne(Example.of(imsiCurriculum))
                                    .orElseGet(() -> {
                                        /** 관련 교육과정 정보가 존재하는지 확인 */
                                        curriculumMasterRepository.findOne(Example.of(CurriculumMaster.builder()
                                                        .curriculumCode(data.getValue())
                                                        .build()))
                                                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3004, "관련 교육과정 정보가 존재하지 않습니다. (" + data.getValue() + ")"));
                                        /** 생성된 교육과정과 연계 교육과정 연결 */
                                        return curriculumCollaborationRepository.save(imsiCurriculum);
                                    });
                        }).collect(Collectors.toList());
                    });

            Optional.ofNullable(curriculumApiRequestVO.getEvaluateList())
                    .filter(list -> list != null && list.size() > 0)
                    .ifPresent(list -> {
                        list.stream().map(data -> {
                            /** 관련 강의평가 정보가 존재하는지 확인 */
                            evaluateMasterRepository.findOne(Example.of(EvaluateMaster.builder()
                                            .evaluateSerialNo(data.getValue())
                                            .build()))
                                    .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2063, "관련 강의평가 정보가 존재하지 않습니다. (" + data.getValue() + ")"));

                            CurriculumEvaluate imsiEvaluate = CurriculumEvaluate.builder()
                                    .curriculumCode(entity.getCurriculumCode())
                                    .evaluateSerialNo(data.getValue())
                                    .sortNo(data.getSortNo())
                                    .build();
                            return curriculumEvaluateRepository.save(imsiEvaluate);
                        }).collect(Collectors.toList());
                    });

            /** 조회 수 '0' 초기 셋팅 */
            entity.setViewCount(BigInteger.ZERO);
            if(entity.getProgressRateOfEnd() == null) { entity.setProgressRateOfEnd(NumberUtils.DOUBLE_ZERO); }
            if(entity.getExamRateOfEnd() == null) { entity.setExamRateOfEnd(NumberUtils.DOUBLE_ZERO); }
            if(entity.getAssignmentRateOfEnd() == null) { entity.setAssignmentRateOfEnd(NumberUtils.DOUBLE_ZERO); }
            if(entity.getProgressScoreOfEnd() == null) { entity.setProgressScoreOfEnd(NumberUtils.DOUBLE_ZERO); }
            if(entity.getExamScoreOfEnd() == null) { entity.setExamScoreOfEnd(NumberUtils.DOUBLE_ZERO); }
            if(entity.getAssignmentScoreOfEnd() == null) { entity.setAssignmentScoreOfEnd(NumberUtils.DOUBLE_ZERO); }
            if(entity.getTotalScore() == null) { entity.setTotalScore(NumberUtils.DOUBLE_ZERO); }
            if(entity.getProgressRate() == null) { entity.setProgressRate(NumberUtils.DOUBLE_ZERO); }
            BeanUtils.copyProperties(curriculumMasterRepository.saveAndFlush(entity), result);

            return result;
        }
    }

    /**
     * 교육 과정 수정
     *
     * @param curriculumApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CurriculumApiResponseVO updateCurriculumInfo(CurriculumApiRequestVO curriculumApiRequestVO) {
        return curriculumMasterRepository.findOne(Example.of(CurriculumMaster.builder()
                                                                .curriculumCode(curriculumApiRequestVO.getCurriculumCode())
                                                                .build()))
                .map(curriculumMaster -> {
                    copyNonNullObject(curriculumApiRequestVO, curriculumMaster);

                    CurriculumApiResponseVO result = CurriculumApiResponseVO.builder().build();

                    /**
                     * 교육과정 연계 교육과정 정보 업데이트
                     */
                    if(curriculumApiRequestVO.getCurriculumCollaborationList() != null && curriculumApiRequestVO.getCurriculumCollaborationList().size() == 0) {
                        /** 교육과정 연계 교육과정 목록 초기화 */
                        curriculumCollaborationRepository.deleteAll(
                                curriculumCollaborationRepository.findAll(Example.of(CurriculumCollaboration.builder()
                                        .curriculumCode(curriculumMaster.getCurriculumCode())
                                        .build())));
                    } else if(curriculumApiRequestVO.getCurriculumCollaborationList() != null && curriculumApiRequestVO.getCurriculumCollaborationList().size() > 0) {
                        /** 교육과정 연계 교육과정 목록 초기화 */
                        curriculumCollaborationRepository.deleteAll(
                                curriculumCollaborationRepository.findAll(Example.of(CurriculumCollaboration.builder()
                                        .curriculumCode(curriculumMaster.getCurriculumCode())
                                        .build())));
                        /** 교육과정 연계 교육과정 목록 새로 갱신 */
                        curriculumApiRequestVO.getCurriculumCollaborationList().stream()
                                .map(data -> {
                                    CurriculumCollaboration imsiCurriculum = CurriculumCollaboration.builder()
                                            .curriculumCode(curriculumMaster.getCurriculumCode())
                                            .referenceCurriculumCode(data.getValue())
                                            .build();
                                    return curriculumCollaborationRepository.findOne(Example.of(imsiCurriculum))
                                            .orElseGet(() -> { /** 교육과정 연계 교육과정 목록 정보가 존재하지 않으면... */
                                                /** 관련 교육과정 정보가 존재하는지 확인 */
                                                curriculumMasterRepository.findOne(Example.of(CurriculumMaster.builder()
                                                                .curriculumCode(data.getValue())
                                                                .build()))
                                                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3004, "관련 교육과정 정보가 존재하지 않습니다. (" + data.getValue() + ")"));
                                                /** 생성된 교육과정과 해당 교육과정 연결 */
                                                return curriculumCollaborationRepository.save(imsiCurriculum);
                                            });
                                }).collect(Collectors.toList());
                    }

                    /**
                     * 교육과정 연계 콘텐츠 정보 업데이트
                     */
                    if(curriculumApiRequestVO.getContentsList() != null && curriculumApiRequestVO.getContentsList().size() == 0) {
                        /** 교육과정 연계 콘텐츠 목록 초기화 */
                        curriculumContentsRepository.deleteAll(
                                curriculumContentsRepository.findAll(Example.of(CurriculumContents.builder()
                                        .curriculumCode(curriculumMaster.getCurriculumCode())
                                        .build())));
                    } else if(curriculumApiRequestVO.getContentsList() != null && curriculumApiRequestVO.getContentsList().size() > 0) {
                        /** 교육과정 연계 콘텐츠 목록 초기화 */
                        curriculumContentsRepository.deleteAll(
                                curriculumContentsRepository.findAll(Example.of(CurriculumContents.builder()
                                        .curriculumCode(curriculumMaster.getCurriculumCode())
                                        .build())));
                        /** 교육과정 연계 콘텐츠 목록 새로 갱신 */
                        curriculumApiRequestVO.getContentsList().stream()
                                .map(data -> {
                                    CurriculumContents imsiContents = CurriculumContents.builder()
                                            .curriculumCode(curriculumMaster.getCurriculumCode())
                                            .contentsCode(data.getValue())
                                            .build();
                                    return curriculumContentsRepository.findOne(Example.of(imsiContents))
                                            .map(con -> {
                                                con.setSortNo(data.getSortNo());
                                                return curriculumContentsRepository.save(con);
                                            })
                                            .orElseGet(() -> { /** 교육과정 연계 콘텐츠 목록 정보가 존재하지 않으면... */
                                                imsiContents.setSortNo(data.getSortNo());
                                                /** 관련 콘텐츠 정보가 존재하는지 확인 */
                                                contentsMasterRepository.findOne(Example.of(ContentsMaster.builder()
                                                                .contentsCode(data.getValue())
                                                                .build()))
                                                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2004, "관련 콘텐츠 정보가 존재하지 않습니다. (" + data.getValue() + ")"));
                                                /** 생성된 교육과정과 해당 콘텐츠 연결 */
                                                return curriculumContentsRepository.save(imsiContents);
                                            });
                                }).collect(Collectors.toList());
                    }

                    CurriculumEvaluate curriculumEvaluate = CurriculumEvaluate.builder()
                            .curriculumCode(curriculumMaster.getCurriculumCode())
                            .build();
                    curriculumEvaluateRepository.deleteAll(curriculumEvaluateRepository.findAll(Example.of(curriculumEvaluate)));

                    Optional.ofNullable(curriculumApiRequestVO.getEvaluateList())
                            .filter(list -> list != null && list.size() > 0)
                            .ifPresent(list -> {
                                list.stream().map(data -> {
                                    /** 관련 강의평가 정보가 존재하는지 확인 */
                                    evaluateMasterRepository.findOne(Example.of(EvaluateMaster.builder()
                                                    .evaluateSerialNo(data.getValue())
                                                    .build()))
                                            .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2063, "관련 강의평가 정보가 존재하지 않습니다. (" + data.getValue() + ")"));

                                    CurriculumEvaluate imsiEvaluate = CurriculumEvaluate.builder()
                                            .curriculumCode(curriculumMaster.getCurriculumCode())
                                            .evaluateSerialNo(data.getValue())
                                            .sortNo(data.getSortNo())
                                            .build();
                                    return curriculumEvaluateRepository.save(imsiEvaluate);
                                }).collect(Collectors.toList());
                            });
                    BeanUtils.copyProperties(curriculumMasterRepository.saveAndFlush(curriculumMaster), result);

                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3004, "해당 교육 과정 미존재"));
    }

    /**
     * 교육과정 신청서 양식 업로드 API
     *
     * @param curriculumCode
     * @param applicationFormFile
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport fileUpload(String curriculumCode, MultipartFile applicationFormFile) {
        /** 교육과정 존재 여부 확인 */
        CurriculumMaster curriculumMaster = curriculumMasterRepository.findOne(Example.of(CurriculumMaster.builder()
                                                        .curriculumCode(curriculumCode)
                                                        .build()))
                                                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3004, "해당 교육과정 미존재"));
        /** 파일 저장 및 파일 경로 셋팅 */
        Optional.ofNullable(applicationFormFile)
                .ifPresent(file -> {
                    Path directoryPath = Paths.get(new StringBuilder(appConfig.getUploadFile().getUploadContextPath())
                            .append(appConfig.getUploadFile().getApplicationFolder())
                            .append("/form").toString());
                    try {
                        Files.createDirectories(directoryPath);
                        try {
                            String imageSequence = new StringBuilder("_").append(new SimpleDateFormat("yyMMddHHmmss").format(new Date())).toString();

                            String formFilePath = new StringBuilder(appConfig.getUploadFile().getUploadContextPath())
                                    .append(appConfig.getUploadFile().getApplicationFolder())
                                    .append("/form")
                                    .append("/").append(curriculumCode + APLY_IMG_TAG + imageSequence + "." + StringUtils.substringAfter(file.getOriginalFilename(), ".")).toString();
                            file.transferTo(new File(formFilePath));
                            curriculumMaster.setApplicationFormFilePath(formFilePath.replace(appConfig.getUploadFile().getUploadContextPath(), ""));
                        } catch (IOException e) {
                            throw new KPFException(KPF_RESULT.ERROR9005, "파일 업로드 실패");
                        }
                    } catch (IOException e2) {
                        throw new KPFException(KPF_RESULT.ERROR9005, "파일 경로 확인 또는, 생성 실패");
                    }
                });

        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }
}
