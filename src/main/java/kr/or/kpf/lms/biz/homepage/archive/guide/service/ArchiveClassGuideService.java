package kr.or.kpf.lms.biz.homepage.archive.guide.service;

import kr.or.kpf.lms.biz.business.organization.aply.vo.request.BizOrganizationAplyApiRequestVO;
import kr.or.kpf.lms.biz.business.organization.aply.vo.request.BizOrganizationAplyViewRequestVO;
import kr.or.kpf.lms.biz.business.organization.aply.vo.response.BizOrganizationAplyExcelVO;
import kr.or.kpf.lms.biz.common.upload.vo.request.FileMasterApiRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.guide.classguidesubject.service.ClassGuideSubjectService;
import kr.or.kpf.lms.biz.homepage.archive.guide.classguidesubject.vo.request.ClassGuideSubjectApiRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.request.ArchiveClassGuideApiRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.request.ArchiveClassGuideExcelRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.request.ArchiveClassGuideViewRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.response.ArchiveClassGuideApiResponseVO;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.response.ArchiveClassGuideExcelResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.common.support.Code;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.entity.BizOrganizationAplyDtl;
import kr.or.kpf.lms.repository.entity.FileMaster;
import kr.or.kpf.lms.repository.entity.homepage.ClassGuide;
import kr.or.kpf.lms.repository.entity.homepage.ClassGuideFile;
import kr.or.kpf.lms.repository.entity.homepage.ClassGuideSubject;
import kr.or.kpf.lms.repository.entity.homepage.LmsDataFile;
import kr.or.kpf.lms.repository.homepage.ClassGuideFileRepository;
import kr.or.kpf.lms.repository.homepage.ClassGuideRepository;
import kr.or.kpf.lms.repository.CommonHomepageRepository;
import kr.or.kpf.lms.repository.homepage.ClassGuideSubjectRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 참여 / 소통 > 자료실 - 수업지도안 관련 Service
 */
@Service
@RequiredArgsConstructor
public class ArchiveClassGuideService extends CSServiceSupport {

    /** 수업지도안 코드 */
    private static final String PREFIX_GUIDE = "GUI";
    private static final String GUIDE_TAG = "_CLASS_GUIDE";

    /** 홈페이지 공통 Repository */
    private final CommonHomepageRepository commonHomepageRepository;
    /** 수업지도안 Repository */
    private final ClassGuideRepository classGuideRepository;
    private final ClassGuideSubjectRepository classGuideSubjectRepository;
    private final ClassGuideFileRepository classGuideFileRepository;
    private final ClassGuideSubjectService classGuideSubjectService;

    /**
     * 수업지도안 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(ArchiveClassGuideViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonHomepageRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 수업지도안 생성
     *
     * @param requestVO
     * @return
     */
    public ArchiveClassGuideApiResponseVO createInfo(ArchiveClassGuideApiRequestVO requestVO) {
        ClassGuide entity = ClassGuide.builder().build();
        copyNonNullObject(requestVO, entity);

        ArchiveClassGuideApiResponseVO result = ArchiveClassGuideApiResponseVO.builder().build();
        entity.setClassGuideCode(commonHomepageRepository.generateCode(PREFIX_GUIDE));
        BeanUtils.copyProperties(classGuideRepository.saveAndFlush(entity), result);

        String[] subjectCodes = null;
        if (requestVO.getReferenceSubject() != null) {
            subjectCodes = requestVO.getReferenceSubject().split(",");

            for (int i=0; i<subjectCodes.length; i++) {
                ClassGuideSubjectApiRequestVO requestObject = ClassGuideSubjectApiRequestVO.builder().build();
                requestObject.setClassGuideCode(result.getClassGuideCode());
                requestObject.setIndividualCode(subjectCodes[i]);
                classGuideSubjectService.createInfo(requestObject);
            }
        }

        return result;
    }

    /**
     * 수업지도안 업데이트
     *
     * @param requestVO
     * @return
     */
    public ArchiveClassGuideApiResponseVO updateInfo(ArchiveClassGuideApiRequestVO requestVO) {
        return classGuideRepository.findOne(Example.of(ClassGuide.builder().classGuideCode(requestVO.getClassGuideCode()).build()))
                .map(classGuide -> {
                    ArchiveClassGuideApiResponseVO result = ArchiveClassGuideApiResponseVO.builder().build();

                    if (classGuide.getReferenceSubject() != null && !classGuide.getReferenceSubject().isEmpty()) {
                        if (!classGuide.getReferenceSubject().equals(requestVO.getReferenceSubject())) {
                            classGuideSubjectRepository.deleteAll(classGuideSubjectRepository.findAll(Example.of(ClassGuideSubject.builder()
                                    .classGuideCode(requestVO.getClassGuideCode())
                                    .build())));
                            copyNonNullObject(requestVO, classGuide);

                            String[] subjectCodes = {};
                            if (requestVO.getReferenceSubject() != null && !requestVO.getReferenceSubject().isEmpty()) {
                                if (requestVO.getReferenceSubject().contains(",")) {
                                    subjectCodes = requestVO.getReferenceSubject().split(",");
                                } else {
                                    subjectCodes = new String[1];
                                    subjectCodes[0] = requestVO.getReferenceSubject();
                                }

                                for (int i=0; i<subjectCodes.length; i++) {
                                    ClassGuideSubjectApiRequestVO requestSubjectVO = ClassGuideSubjectApiRequestVO.builder().build();
                                    requestSubjectVO.setClassGuideCode(requestVO.getClassGuideCode());
                                    requestSubjectVO.setIndividualCode(subjectCodes[i]);
                                    classGuideSubjectService.createInfo(requestSubjectVO);
                                }
                            } else {
                                classGuide.setReferenceSubject(null);
                            }
                        } else {
                            copyNonNullObject(requestVO, classGuide);
                        }
                    } else {
                        copyNonNullObject(requestVO, classGuide);
                    }

                    BeanUtils.copyProperties(classGuideRepository.saveAndFlush(classGuide), result);
                    return result;
                })
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR4005, "해당 수업지도안 미존재"));
    }

    /**
     수업지도안 정보 삭제
     */
    public CSResponseVOSupport deleteInfo(ArchiveClassGuideApiRequestVO requestObject) {
        classGuideRepository.delete(classGuideRepository.findById(requestObject.getClassGuideCode())
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR4007, "이미 삭제된 수업지도안 입니다.")));

        classGuideFileRepository.deleteAll(classGuideFileRepository.findAll(Example.of(ClassGuideFile.builder()
                .classGuideCode(requestObject.getClassGuideCode())
                .build())));

        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     * 첨부파일 등록
     *
     * @param classGuideCode
     * @param attachFile
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport fileUpload(String classGuideCode, MultipartFile attachFile, String fileType) {
        /** 수업지도안 이력 확인 */
        ClassGuide classGuide = classGuideRepository.findOne(Example.of(ClassGuide.builder()
                        .classGuideCode(classGuideCode)
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR4005, "대상 수업지도안 이력 없음."));

        /** 파일 저장 및 파일 경로 셋팅 */
        Optional.ofNullable(attachFile)
                .ifPresent(file -> {
                    Path directoryPath = Paths.get(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getClassGuideFolder()).toString());
                    try {
                        Files.createDirectories(directoryPath);
                        try {
                            String imageSequence = new StringBuilder("_").append(new SimpleDateFormat("yyMMddHHmmss").format(new Date())).toString();

                            String attachFilepath = new StringBuilder(appConfig.getUploadFile().getUploadContextPath())
                                    .append(appConfig.getUploadFile().getClassGuideFolder())
                                    .append("/")
                                    .append(authenticationInfo().getUserId() + GUIDE_TAG + imageSequence + "." + StringUtils.substringAfter(file.getOriginalFilename(), ".")).toString();
                            file.transferTo(new File(attachFilepath));

                            if (fileType.equals("1")) {/** 수업지도안/길라잡이 */
                                classGuide.setClassGuideFilePath(attachFilepath.replace(appConfig.getUploadFile().getUploadContextPath(), ""));
                                classGuide.setClassGuideFileSize(file.getSize());
                            } else if (fileType.equals("2")) {/** 활동지 */
                                classGuide.setActivitiesFilePath(attachFilepath.replace(appConfig.getUploadFile().getUploadContextPath(), ""));
                                classGuide.setActivitiesFileSize(file.getSize());
                            } else if (fileType.equals("3")) {/** 예시답안 */
                                classGuide.setExampleAnswerFilePath(attachFilepath.replace(appConfig.getUploadFile().getUploadContextPath(), ""));
                                classGuide.setExampleAnswerFileSize(file.getSize());
                            } else if (fileType.equals("4")) {/** 10분 NIE */
                                classGuide.setNieFilePath(attachFilepath.replace(appConfig.getUploadFile().getUploadContextPath(), ""));
                                classGuide.setNieFileSize(file.getSize());
                            }

                        } catch (IOException e) {
                            throw new KPFException(KPF_RESULT.ERROR9005, "파일 업로드 실패");
                        }
                    } catch (IOException e2) {
                        throw new KPFException(KPF_RESULT.ERROR9005, "파일 경로 확인 또는, 생성 실패");
                    }
                });

        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     * 첨부파일 다중 등록
     *
     * @param classGuideCode
     * @param attachFiles
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport multifileUpload(String classGuideCode, List<MultipartFile> attachFiles, String fileType) {
        /** 수업지도안 이력 확인 */
        ClassGuide classGuide = classGuideRepository.findOne(Example.of(ClassGuide.builder()
                        .classGuideCode(classGuideCode)
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR4005, "대상 수업지도안 이력 없음."));

        if (classGuide != null && !classGuide.equals(null)) {
            for (MultipartFile multipartFile : attachFiles) {
                /** 파일 저장 및 파일 경로 셋팅 */
                Optional.ofNullable(multipartFile)
                        .ifPresent(file -> {
                            Path directoryPath = Paths.get(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getClassGuideFolder()).toString());
                            try {
                                Files.createDirectories(directoryPath);
                                try {
                                    String imageSequence = new StringBuilder("_").append(new SimpleDateFormat("yyMMddHHmmss").format(new Date())).toString();
                                    String attachFilepath = new StringBuilder(appConfig.getUploadFile().getUploadContextPath())
                                            .append(appConfig.getUploadFile().getClassGuideFolder())
                                            .append("/").toString();

                                    if (fileType.equals(Code.GUI_FILE_TYPE.TEACH.enumCode)) {/** 수업지도안/길라잡이 */
                                        attachFilepath = new StringBuilder(attachFilepath)
                                                .append(authenticationInfo().getUserId() + "_" + Code.GUI_FILE_TYPE.TEACH + imageSequence + "." + StringUtils.substringAfter(file.getOriginalFilename(), ".")).toString();

                                    } else if (fileType.equals(Code.GUI_FILE_TYPE.ACTIVITY.enumCode)) {/** 활동지 */
                                        attachFilepath = new StringBuilder(attachFilepath)
                                                .append(authenticationInfo().getUserId() + "_" + Code.GUI_FILE_TYPE.ACTIVITY + imageSequence + "." + StringUtils.substringAfter(file.getOriginalFilename(), ".")).toString();

                                    } else if (fileType.equals(Code.GUI_FILE_TYPE.ANSWER.enumCode)) {/** 예시답안 */
                                        attachFilepath = new StringBuilder(attachFilepath)
                                                .append(authenticationInfo().getUserId() + "_" + Code.GUI_FILE_TYPE.ANSWER + imageSequence + "." + StringUtils.substringAfter(file.getOriginalFilename(), ".")).toString();

                                    } else if (fileType.equals(Code.GUI_FILE_TYPE.NIE.enumCode)) {/** 10분 NIE */
                                        attachFilepath = new StringBuilder(attachFilepath)
                                                .append(authenticationInfo().getUserId() + "_" + Code.GUI_FILE_TYPE.NIE + imageSequence + "." + StringUtils.substringAfter(file.getOriginalFilename(), ".")).toString();
                                    }

                                    file.transferTo(new File(attachFilepath));
                                    ClassGuideFile entity = ClassGuideFile.builder().build();
                                    entity.setClassGuideCode(classGuideCode);
                                    entity.setFileType(fileType);
                                    entity.setFilePath(attachFilepath.replace(appConfig.getUploadFile().getUploadContextPath(), ""));
                                    if (fileType.equals(Code.GUI_FILE_TYPE.TEACH)) {/** 수업지도안/길라잡이 */
                                        entity.setFileName(file.getOriginalFilename() + "_" + Code.GUI_FILE_TYPE.TEACH + imageSequence);

                                    } else if (fileType.equals(Code.GUI_FILE_TYPE.ACTIVITY)) {/** 활동지 */
                                        entity.setFileName(file.getOriginalFilename() + "_" + Code.GUI_FILE_TYPE.ACTIVITY + imageSequence);

                                    } else if (fileType.equals(Code.GUI_FILE_TYPE.ANSWER)) {/** 예시답안 */
                                        entity.setFileName(file.getOriginalFilename() + "_" + Code.GUI_FILE_TYPE.ANSWER + imageSequence);

                                    } else if (fileType.equals(Code.GUI_FILE_TYPE.NIE)) {/** 10분 NIE */
                                        entity.setFileName(file.getOriginalFilename() + "_" + Code.GUI_FILE_TYPE.NIE + imageSequence);
                                    }
                                    entity.setOriginalFileName(file.getOriginalFilename());
                                    entity.setFileExtension(StringUtils.substringAfterLast(file.getOriginalFilename(), "."));
                                    entity.setFileSize(file.getSize());
                                    /** 조회수 '0' 셋팅 */
                                    entity.setViewCount(BigInteger.ZERO);
                                    classGuideFileRepository.save(entity);

                                } catch (IOException e) {
                                    throw new KPFException(KPF_RESULT.ERROR9005, "파일 업로드 실패");
                                }
                            } catch (IOException e2) {
                                throw new KPFException(KPF_RESULT.ERROR9005, "파일 경로 확인 또는, 생성 실패");
                            }
                        });
            }

            return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
        } else {
            return CSResponseVOSupport.of(KPF_RESULT.ERROR4005);
        }
    }

    /**
     첨부파일 삭제
     */
    public CSResponseVOSupport deleteFile(BigInteger sequenceNo) {
        classGuideFileRepository.delete(classGuideFileRepository.findOne(Example.of(ClassGuideFile.builder()
                        .sequenceNo(sequenceNo)
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9009, "이미 삭제된 수업지도안 파일 입니다.")));
        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     수업지도안 엑셀 다운로드
     */
    public <T> List<ArchiveClassGuideExcelResponseVO> getExcel(ArchiveClassGuideExcelRequestVO requestObject) {
        List<ArchiveClassGuideExcelResponseVO> list = (List<ArchiveClassGuideExcelResponseVO>) commonHomepageRepository.findEntityListExcel(requestObject);
        return list;
    }

}
