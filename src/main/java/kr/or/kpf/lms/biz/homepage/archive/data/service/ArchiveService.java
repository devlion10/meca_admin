package kr.or.kpf.lms.biz.homepage.archive.data.service;

import kr.or.kpf.lms.biz.homepage.archive.data.vo.PublicationFileInfo;
import kr.or.kpf.lms.biz.homepage.archive.data.vo.PublicationFolderInfo;
import kr.or.kpf.lms.biz.homepage.archive.data.vo.request.ArchiveViewRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.data.vo.request.ArchiveApiRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.data.vo.request.PublicationFileManagementApiRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.data.vo.request.PublicationFileManagementViewRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.data.vo.response.ArchiveApiResponseVO;
import kr.or.kpf.lms.biz.homepage.archive.data.vo.response.ArchiveViewResponseVO;
import kr.or.kpf.lms.biz.homepage.archive.data.vo.response.PublicationFileManagementViewResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.ResponseSummary;
import kr.or.kpf.lms.repository.CommonHomepageRepository;
import kr.or.kpf.lms.repository.entity.homepage.LmsData;
import kr.or.kpf.lms.repository.entity.homepage.LmsDataFile;
import kr.or.kpf.lms.repository.homepage.LmsDataFileRepository;
import kr.or.kpf.lms.repository.homepage.LmsDataRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 자료실 관련 Service
 */
@Service
@RequiredArgsConstructor
public class ArchiveService extends CSServiceSupport {

    private static final String LMS_DATA_TAG = "_LMS_DATA";

    /** 홈페이지 공통 Repository */
    private final CommonHomepageRepository commonHomepageRepository;

    /** 자료실 Repository */
    private final LmsDataRepository lmsDataRepository;
    private final LmsDataFileRepository lmsDataFileRepository;

    /**
     * 자료실 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(ArchiveViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonHomepageRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 자료실 생성
     *
     * @param requestVO
     * @return
     */
    public ArchiveApiResponseVO createInfo(ArchiveApiRequestVO requestVO) {
        LmsData entity = LmsData.builder().build();
        copyNonNullObject(requestVO, entity);

        ArchiveApiResponseVO result = ArchiveApiResponseVO.builder().build();
        BeanUtils.copyProperties(lmsDataRepository.saveAndFlush(entity), result);
        return result;
    }

    /**
     * 자료실 업데이트
     *
     * @param requestVO
     * @return
     */
    public ArchiveApiResponseVO updateInfo(ArchiveApiRequestVO requestVO) {
        return lmsDataRepository.findById(requestVO.getSequenceNo())
                .map(lmsData -> {
                    copyNonNullObject(requestVO, lmsData);

                    ArchiveApiResponseVO result = ArchiveApiResponseVO.builder().build();
                    BeanUtils.copyProperties(lmsDataRepository.saveAndFlush(lmsData), result);

                    return result;
                })
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7125, "해당 자료실 미존재"));
    }

    /**
     자료실 정보 삭제
     */
    public CSResponseVOSupport deleteInfo(ArchiveApiRequestVO requestObject) {
        lmsDataRepository.delete(lmsDataRepository.findById(requestObject.getSequenceNo())
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR4007, "이미 삭제된 자료실 입니다.")));

        lmsDataFileRepository.deleteAll(lmsDataFileRepository.findAll(Example.of(LmsDataFile.builder()
                .sequenceNo(requestObject.getSequenceNo())
                .build())));

        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     * 자료실 썸네일 업로드
     *
     * @param sequenceNo
     * @param attachFile
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport thumbnailUpload(Long sequenceNo, MultipartFile attachFile) {
        /** 자료실 이력 확인 */
        LmsData lmsData = lmsDataRepository.findOne(Example.of(LmsData.builder()
                        .sequenceNo(sequenceNo)
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7041, "대상 자료실 없음."));

        /** 파일 저장 및 파일 경로 셋팅 */
        Optional.ofNullable(attachFile)
                .ifPresent(file -> {
                    Path directoryPath = Paths.get(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getLmsDataFolder()).toString());
                    try {
                        Files.createDirectories(directoryPath);
                        try {
                            String imageSequence = new StringBuilder("_").append(new SimpleDateFormat("yyMMddHHmmss").format(new Date())).toString();

                            String attachFilepath = new StringBuilder(appConfig.getUploadFile().getUploadContextPath())
                                    .append(appConfig.getUploadFile().getLmsDataFolder())
                                    .append("/")
                                    .append(authenticationInfo().getUserId() + LMS_DATA_TAG + imageSequence + "." + StringUtils.substringAfter(file.getOriginalFilename(), ".")).toString();
                            file.transferTo(new File(attachFilepath));
                            lmsData.setThumbFilePath(attachFilepath.replace(appConfig.getUploadFile().getUploadContextPath(), ""));

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
     * 자료실 첨부파일 업로드
     *
     * @param sequenceNo
     * @param attachFiles
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport fileUpload(Long sequenceNo, List<MultipartFile> attachFiles) {
        for (MultipartFile multipartFile : attachFiles) {
            /** 파일 저장 및 파일 경로 셋팅 */
            Optional.ofNullable(multipartFile)
                    .ifPresent(file -> {
                        Path directoryPath = Paths.get(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getLmsDataFolder()).toString());
                        try {
                            Files.createDirectories(directoryPath);
                            try {
                                String imageSequence = new StringBuilder("_").append(new SimpleDateFormat("yyMMddHHmmss").format(new Date())).toString();

                                String attachFilepath = new StringBuilder(appConfig.getUploadFile().getUploadContextPath())
                                        .append(appConfig.getUploadFile().getLmsDataFolder())
                                        .append("/")
                                        .append(authenticationInfo().getUserId() + LMS_DATA_TAG + imageSequence + "." + StringUtils.substringAfter(file.getOriginalFilename(), ".")).toString();
                                file.transferTo(new File(attachFilepath));

                                LmsDataFile lmsDataFile = LmsDataFile.builder().build();
                                lmsDataFile.setSequenceNo(sequenceNo);
                                lmsDataFile.setFilePath(attachFilepath.replace(appConfig.getUploadFile().getUploadContextPath(), ""));
                                lmsDataFile.setFileName(authenticationInfo().getUserId() + LMS_DATA_TAG + imageSequence);
                                lmsDataFile.setOriginalFileName(file.getOriginalFilename());
                                lmsDataFile.setFileExtension(StringUtils.substringAfterLast(file.getOriginalFilename(), "."));
                                lmsDataFile.setFileSize(BigInteger.valueOf(file.getSize()));
                                lmsDataFileRepository.save(lmsDataFile);

                            } catch (IOException e) {
                                throw new KPFException(KPF_RESULT.ERROR9005, "파일 업로드 실패");
                            }
                        } catch (IOException e2) {
                            throw new KPFException(KPF_RESULT.ERROR9005, "파일 경로 확인 또는, 생성 실패");
                        }
                    });
        }
        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     자료실 첨부파일 삭제
     */
    public CSResponseVOSupport deleteFile(Long fileSn) {
        lmsDataFileRepository.delete(lmsDataFileRepository.findOne(Example.of(LmsDataFile.builder()
                        .fileSn(fileSn)
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9009, "이미 삭제된 자료실 파일 입니다.")));
        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     * 출간물 폴더 관리 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getFolderManagement(PublicationFileManagementViewRequestVO requestObject) {
        if(!Optional.ofNullable(requestObject.getSequenceNo()).isPresent()) {
            throw new KPFException(KPF_RESULT.ERROR9001, "출간물 코드 누락.");
        }
        return (Page<T>) Optional.ofNullable(requestObject)
                .filter(object -> StringUtils.isNotEmpty(object.getSequenceNo().toString()))
                .map(object -> {
                    ArchiveViewResponseVO result = ArchiveViewResponseVO.builder().build();
                    BeanUtils.copyProperties(lmsDataRepository.findOne(Example.of(LmsData.builder()
                            .sequenceNo(object.getSequenceNo())
                            .build())).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2004, "관련 출간물 정보 미존재")), result);
                   /* LmsData lmsData = lmsDataRepository.findOne(Example.of(LmsData.builder()
                            .sequenceNo(object.getSequenceNo())
                            .build())).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2004, "관련 출간물 정보 미존재"));
                    result.setSequenceNo(object.getSequenceNo());
                    result.setTitle(lmsData.getTitle());
                    result.setThumbFilePath(lmsData.getThumbFilePath());
                    result.setViewCnt(lmsData.getViewCnt());
                    result.setIsUse(lmsData.getIsUse());*/

                    List<PublicationFolderInfo> folderList = new ArrayList<>();
                    File mainFolder = new File(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getLmsDataFolder())
                            .append("/")
                            .append(object.getSequenceNo()).toString());

                    if (!mainFolder.exists()) { /** 요청 폴더가 존재하지 않을 경우에만 생성 */
                        try {
                            FileUtils.forceMkdir(mainFolder);
                        } catch (IOException e) {
                            logger.error("{}- {}", e.getClass().getCanonicalName(), e.getMessage(), e);
                            throw new KPFException(KPF_RESULT.ERROR2005, "폴더 생성 실패");
                        }
                    }

                    FileUtils.listFilesAndDirs(mainFolder, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE).stream()
                            .filter(data -> !requestObject.getSequenceNo().equals(data.getName()))
                            .map(data -> {
                                if (data.isDirectory()) {
                                    PublicationFolderInfo publicationFolderInfo = PublicationFolderInfo.builder()
                                            .folderName(data.getName())
                                            .folderPath(data.getPath().replaceAll(Matcher.quoteReplacement("\\"), "/").replaceAll(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getLmsDataFolder())
                                                    .append("/")
                                                    .append(object.getSequenceNo()).toString(), ""))
                                            .publicationFolderInfos(new ArrayList<>())
                                            .build();
                                    return publicationFolderInfo;
                                    /*return PublicationFolderInfo.builder()
                                            .folderName(data.getName())
                                            .folderPath(data.getPath().replaceAll(Matcher.quoteReplacement("\\"), "/").replaceAll(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getLmsDataFolder())
                                                    .append("/")
                                                    .append(object.getSequenceNo()).toString(), ""))
                                            .publicationFileInfos(new ArrayList<>())
                                            .build();*/
                                }
                                return null;
                            }).filter(data -> data != null)
                            .forEach(data -> {
                                if (data.getFolderPath().split("/").length == 2) {
                                    folderList.add(data);
                                } else if (data.getFolderPath().split("/").length == 3) {
                                    folderList.stream().forEach(depth1 -> {
                                        if (data.getFolderPath().contains(depth1.getFolderPath())) {
                                            depth1.getPublicationFolderInfos().add(data);
                                        }
                                    });
                                } else if (data.getFolderPath().split("/").length == 4) {
                                    folderList.stream().forEach(depth1 -> {
                                        depth1.getPublicationFolderInfos().stream().forEach(depth2 -> {
                                            if (data.getFolderPath().contains(depth2.getFolderPath())) {
                                                depth2.getPublicationFolderInfos().add(data);
                                            }
                                        });
                                    });
                                } else if (data.getFolderPath().split("/").length == 5) {
                                    folderList.stream().forEach(depth1 -> {
                                        depth1.getPublicationFolderInfos().stream().forEach(depth2 -> {
                                            depth2.getPublicationFolderInfos().stream().forEach(depth3 -> {
                                                if (data.getFolderPath().contains(depth3.getFolderPath())) {
                                                    depth3.getPublicationFolderInfos().add(data);
                                                }
                                            });
                                        });
                                    });
                                }
                            });
                    result.setPublicationFolderInfos(folderList);

                    ResponseSummary summary = ResponseSummary.builder()
                            .count(Arrays.asList(result).size())
                            .offset(requestObject.getPageNum())
                            .limit(requestObject.getPageSize())
                            .build();
                    Pageable pageableToApply = summary.ensureValidPageable(requestObject.getPageable());

                    return CSPageImpl.of(Arrays.asList(result), pageableToApply, Arrays.asList(result).size());
                })
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 출간물 폴더 관련 파일리스트 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getFileList(PublicationFileManagementViewRequestVO requestObject) {
        if(!Optional.ofNullable(requestObject.getSequenceNo()).isPresent() || !Optional.ofNullable(requestObject.getFolderPath()).isPresent()) {
            throw new KPFException(KPF_RESULT.ERROR9001, "출간물 코드 누락.");
        }
        return (Page<T>) Optional.ofNullable(requestObject)
                .filter(object -> StringUtils.isNotEmpty(object.getSequenceNo().toString()))
                .map(object -> {
                    PublicationFolderInfo resultVO = PublicationFolderInfo.builder()
                            .folderName(requestObject.getFolderPath().split("/")[requestObject.getFolderPath().split("/").length - 1])
                            .folderPath(requestObject.getFolderPath())
                            .build();

                    File filePath = new File(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getLmsDataFolder())
                            .append("/")
                            .append(object.getSequenceNo()).append(object.getFolderPath()).toString());

                    if (!filePath.exists()) { /** 요청 폴더가 존재하지 않을 경우에만 생성 */
                        try {
                            FileUtils.forceMkdir(filePath);
                        } catch (IOException e) {
                            logger.error("{}- {}", e.getClass().getCanonicalName(), e.getMessage(), e);
                            throw new KPFException(KPF_RESULT.ERROR2005, "폴더 생성 실패");
                        }
                    }

                    resultVO.setPublicationFileInfos(FileUtils.listFiles(filePath, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE).stream()
                            .filter(data -> String.valueOf(data.getParent()).equals(String.valueOf(filePath)))
                            .map(data -> {
                                try {
                                    return PublicationFileInfo.builder()
                                            .fileName(data.getName())
                                            .fileSize(new StringBuilder(String.valueOf(data.length())).append("Bytes").toString())
                                            .updateDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                                    .format(new Date(Files.readAttributes(data.toPath(), BasicFileAttributes.class)
                                                            .creationTime().toMillis())))
                                            .build();
                                } catch (IOException e) {
                                    throw new KPFException(KPF_RESULT.ERROR2010, "파일 파싱 중 오류");
                                }
                            }).collect(Collectors.toList()));

                    ResponseSummary summary = ResponseSummary.builder()
                            .count(resultVO.getPublicationFileInfos().size())
                            .offset(requestObject.getPageNum())
                            .limit(requestObject.getPageSize())
                            .build();
                    Pageable pageableToApply = summary.ensureValidPageable(requestObject.getPageable());

                    return CSPageImpl.of(Arrays.asList(resultVO), pageableToApply, resultVO.getPublicationFileInfos().size());
                })
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 출간물 폴더 관리
     *
     * @param sequenceNo
     * @param folderList
     * @return
     */
    public CSResponseVOSupport folderManagement(Long sequenceNo, List<PublicationFileManagementApiRequestVO> folderList) {
        folderList.stream().forEach(folderInfo -> {
            String newFolderPath = folderInfo.getNewFolderPath();
            String beforeFolderPath = folderInfo.getBeforeFolderPath();
            String renameFolderPath = folderInfo.getRenameFolderPath();
            String moveFolderPath = folderInfo.getMoveFolderPath();
            String deleteFolderPath = folderInfo.getDeleteFolderPath();
            List<String> deleteFileList = folderInfo.getDeleteFileList();

            if (Optional.ofNullable(beforeFolderPath).isPresent() && Optional.ofNullable(renameFolderPath).isPresent()) { /** 폴더명 변경(동일 Depth에서만 가능... Depth가 다를 경우 폴더 이동으로 요청) */
                if (beforeFolderPath.split("/").length != renameFolderPath.split("/").length) {
                    throw new KPFException(KPF_RESULT.ERROR2006, "폴더명 변경 실패. Depth가 다를 경우 폴더 이동으로 요청 가능.");
                }
                /** 변경 전 폴더 경로 */
                File beforeFolder = FileUtils.getFile(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getLmsDataFolder())
                        .append("/")
                        .append(sequenceNo)
                        .append(beforeFolderPath).toString());
                /** 변경 후 폴더 경로 */
                File afterFolder = FileUtils.getFile(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getLmsDataFolder())
                        .append("/")
                        .append(sequenceNo)
                        .append(renameFolderPath).toString());
                if(!beforeFolder.exists()) { /** 변경하고자 하는 폴더가 존재하지 않을 경우 */
                    throw new KPFException(KPF_RESULT.ERROR2008, "존재하지 않는 폴더");
                }
                if(afterFolder.exists()) { /** 변경하고자 하는 폴더가 존재할 경우 */
                    throw new KPFException(KPF_RESULT.ERROR2009, "이미 존재하는 폴더");
                }

                StringBuilder imsiFolderPath = new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getLmsDataFolder())
                        .append("/")
                        .append(sequenceNo);

                Arrays.stream(renameFolderPath.split("/")).limit(renameFolderPath.split("/").length - 1).filter(StringUtils::isNotEmpty).forEach(data -> {
                    imsiFolderPath.append("/").append(data);
                    File imsiFolder = FileUtils.getFile(imsiFolderPath.toString());
                    if (!imsiFolder.exists()) { /** 요청 폴더가 존재하지 않을 경우에만 생성 */
                        try {
                            FileUtils.forceMkdir(imsiFolder);
                        } catch (IOException e) {
                            logger.error("{}- {}", e.getClass().getCanonicalName(), e.getMessage(), e);
                            throw new KPFException(KPF_RESULT.ERROR2005, "폴더 생성 실패");
                        }
                    }
                });
                /** 폴더 명 변경 */
                beforeFolder.renameTo(afterFolder);
            } else if (Optional.ofNullable(beforeFolderPath).isPresent() && Optional.ofNullable(moveFolderPath).isPresent()) { /** 폴더 이동(다른 Depth에서만 가능... Depth가 동일할 경우 폴더명 변경으로 요청) */
                if (beforeFolderPath.split("/").length == moveFolderPath.split("/").length) {
                    throw new KPFException(KPF_RESULT.ERROR2007, "폴더 이동 실패. Depth가 같을 경우 폴더명으로 요청 가능.");
                }
                /** 변경 전 폴더 경로 */
                File beforeFolder = FileUtils.getFile(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getLmsDataFolder())
                        .append("/")
                        .append(sequenceNo)
                        .append(beforeFolderPath).toString());
                /** 이동 후 상위 폴더 경로 */
                File afterFolder = FileUtils.getFile(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getLmsDataFolder())
                        .append("/")
                        .append(sequenceNo)
                        .append(moveFolderPath).toString());
                try {
                    /** 폴더 이동 */
                    FileUtils.moveDirectoryToDirectory(beforeFolder, afterFolder, true);
                } catch (IOException e) {
                    logger.error("{}- {}", e.getClass().getCanonicalName(), e.getMessage(), e);
                    throw new KPFException(KPF_RESULT.ERROR2005, "폴더 이동 실패");
                }
            } else if (Optional.ofNullable(newFolderPath).isPresent()) { /** 새폴더 생성 */
                /** 새폴더 경로 */
                File folder = FileUtils.getFile(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getLmsDataFolder())
                        .append("/")
                        .append(sequenceNo)
                        .append(newFolderPath).toString());
                if (!folder.exists()) { /** 요청 폴더가 존재하지 않을 경우에만 생성 */
                    try {
                        FileUtils.forceMkdir(folder);
                    } catch (IOException e) {
                        logger.error("{}- {}", e.getClass().getCanonicalName(), e.getMessage(), e);
                        throw new KPFException(KPF_RESULT.ERROR2005, "폴더 생성 실패");
                    }
                }
            } else if (Optional.ofNullable(deleteFolderPath).isPresent()) { /** 폴더 삭제 */
                /** 삭제 대상 폴더 경로 */
                File folder = FileUtils.getFile(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getLmsDataFolder())
                        .append("/")
                        .append(sequenceNo)
                        .append(deleteFolderPath).toString());
                if (folder.exists()) { /** 삭제 대상 폴더가 존재할 경우 */
                    try {
                        FileUtils.deleteDirectory(folder);
                    } catch (IOException e) {
                        logger.error("{}- {}", e.getClass().getCanonicalName(), e.getMessage(), e);
                        throw new KPFException(KPF_RESULT.ERROR2005, "폴더 삭제 실패");
                    }
                }
            } else if(Optional.ofNullable(beforeFolderPath).isPresent() && Optional.ofNullable(deleteFileList).filter(data -> data.size() > 0).isPresent()) { /** 파일 삭제 */
                /** 삭제 파일 대상 폴더 경로 */
                File folder = FileUtils.getFile(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getLmsDataFolder())
                        .append("/")
                        .append(sequenceNo)
                        .append(beforeFolderPath).toString());

                if (folder.exists()) { /** 폴더 존재 여부 확인 */
                    FileUtils.listFiles(folder, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE).stream()
                            .forEach(data -> {
                                deleteFileList.stream().forEach(subData -> {
                                    if (data.getName().equals(subData)) { /** 삭제 요청 파일명과 동일할 경우 */
                                        data.delete();
                                    }
                                });
                            });
                }
            }
        });
        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     * 출간물 파일 업로드
     *
     * @param sequenceNo
     * @param filePath
     * @param publicationFileList
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport fileListUpload(Long sequenceNo, String filePath, MultipartFile[] publicationFileList) {
        /** 출간물 존재 여부 확인 */
        lmsDataRepository.findOne(Example.of(LmsData.builder()
                        .sequenceNo(sequenceNo)
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2003, "해당 출간물 미존재"));
        /** 파일 저장 */
        Optional.ofNullable(publicationFileList)
                .filter(fileList -> fileList.length > 0)
                .ifPresent(fileList -> {
                    Arrays.stream(fileList).forEach(file -> {
                        Path directoryPath = Paths.get(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getLmsDataFolder())
                                .append("/")
                                .append(sequenceNo)
                                .append(filePath).toString());
                        try {
                            Files.createDirectories(directoryPath);
                            try {
                                String uploadFilePath = new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getLmsDataFolder())
                                        .append("/")
                                        .append(sequenceNo)
                                        .append(filePath)
                                        .append("/")
                                        .append(file.getOriginalFilename()).toString();
                                file.transferTo(new File(uploadFilePath));
                            } catch (IOException e) {
                                throw new KPFException(KPF_RESULT.ERROR9004, "파일 업로드 실패");
                            }
                        } catch (IOException e2) {
                            throw new KPFException(KPF_RESULT.ERROR9005, "파일 경로 확인 또는, 생성 실패");
                        }
                    });
                });
        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     * 압축 파일 해제 및 저장
     *
     * @param sequenceNo
     * @param filePath
     * @param zipFileList
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport zipFileUpload(Long sequenceNo, String filePath, MultipartFile[] zipFileList) {
        /** 출간물 존재 여부 확인 */
        lmsDataRepository.findOne(Example.of(LmsData.builder()
                        .sequenceNo(sequenceNo)
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2003, "해당 출간물 미존재"));
        /** 압축 해제 및 파일 저장 */
        Optional.ofNullable(zipFileList)
                .filter(fileList -> fileList.length > 0)
                .ifPresent(fileList -> {
                    Arrays.stream(fileList).forEach(file -> {
                        Path directoryPath = Paths.get(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getLmsDataFolder())
                                .append("/")
                                .append(sequenceNo)
                                .append(filePath).toString());
                        try {
                            Files.createDirectories(directoryPath);
                            /** 임시 디렉토리 생성 */
                            File tempDir = Files.createTempDirectory("temp").toFile();

                            /** 업로드된 파일 저장 */
                            File uploadedFile = new File(tempDir.getAbsolutePath() + File.separator + file.getOriginalFilename());
                            file.transferTo(uploadedFile);

                            /** 압축 해제 */
                            unzip(uploadedFile.getAbsolutePath(), new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getLmsDataFolder()) + File.separator + sequenceNo + filePath);
                        } catch (IOException e2) {
                            throw new KPFException(KPF_RESULT.ERROR9005, "파일 경로 확인 또는, 생성 실패");
                        }
                    });
                });

        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    public static void unzip(String zipFilePath, String destDirectory) throws IOException {
        byte[] buffer = new byte[4096];
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                String filePath = destDirectory + File.separator + zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    new File(filePath).mkdirs();
                } else {
                    new File(new File(filePath).getParent()).mkdirs();
                    try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath))) {
                        int length;
                        while ((length = zipInputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }
                    } catch (IOException e) {
                        /** 압축 해제 중에 오류가 발생한 경우 예외 처리 */
                        throw new RuntimeException("압축 해제 중 오류가 발생하였습니다.");
                    }
                }
                zipEntry = zipInputStream.getNextEntry();
            }
        }
    }

    /**
     엑셀 다운로드(사용시 수정 필요)
     */
    /*public <T> List<ContentsExcelVO> getExcel(ContentsViewRequestVO requestObject) {
        List<ContentsExcelVO> contentsExcelVOList = (List<ContentsExcelVO>) commonHomepageRepository.findEntityListExcel(requestObject);
        return contentsExcelVOList;
    }*/

}
