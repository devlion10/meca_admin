package kr.or.kpf.lms.biz.contents.contents.service;

import kr.or.kpf.lms.biz.contents.contents.vo.ContentsFolderInfo;
import kr.or.kpf.lms.biz.contents.contents.vo.ContentsFileInfo;
import kr.or.kpf.lms.biz.contents.contents.vo.request.ContentsApiRequestVO;
import kr.or.kpf.lms.biz.contents.contents.vo.request.ContentsFileManagementApiRequestVO;
import kr.or.kpf.lms.biz.contents.contents.vo.request.ContentsFileManagementViewRequestVO;
import kr.or.kpf.lms.biz.contents.contents.vo.request.ContentsViewRequestVO;
import kr.or.kpf.lms.biz.contents.contents.vo.response.ContentsApiResponseVO;
import kr.or.kpf.lms.biz.contents.contents.vo.response.ContentsExcelVO;
import kr.or.kpf.lms.biz.contents.contents.vo.response.ContentsFileManagementViewResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.ResponseSummary;
import kr.or.kpf.lms.repository.CommonContentsRepository;
import kr.or.kpf.lms.repository.contents.ChapterMasterRepository;
import kr.or.kpf.lms.repository.contents.ContentsChapterRepository;
import kr.or.kpf.lms.repository.contents.ContentsMasterRepository;
import kr.or.kpf.lms.repository.entity.contents.ChapterMaster;
import kr.or.kpf.lms.repository.entity.contents.ContentsChapter;
import kr.or.kpf.lms.repository.entity.contents.ContentsMaster;
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
 * 콘텐츠 관리 > 콘텐츠 관리 관련 Service
 */
@Service
@RequiredArgsConstructor
public class ContentsService extends CSServiceSupport {

    private static final String PREFIX_CONTENTS = "CTS";

    private static final String THUMB_IMG_TAG = "_THUMB";

    /** 콘텐츠 관리 공통 */
    private final CommonContentsRepository commonContentsRepository;
    /** 콘텐츠 마스터 */
    private final ContentsMasterRepository contentsMasterRepository;
    /** 콘텐츠 챕터 목록 */
    private final ContentsChapterRepository contentsChapterRepository;
    /** 챕터 마스터 */
    private final ChapterMasterRepository chapterMasterRepository;

    /**
     * 콘텐츠 정보 조회
     *
     * @param <T>
     * @param requestObject
     * @return
     */
    public <T> Page<T> getList(ContentsViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonContentsRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 콘텐츠 폴더 관리 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getFolderManagement(ContentsFileManagementViewRequestVO requestObject) {
        if(!Optional.ofNullable(requestObject.getContentsCode()).isPresent()) {
            throw new KPFException(KPF_RESULT.ERROR9001, "콘텐츠 코드 누락.");
        }
        return (Page<T>) Optional.ofNullable(requestObject)
                .filter(object -> StringUtils.isNotEmpty(object.getContentsCode()))
                .map(object -> {
                    ContentsFileManagementViewResponseVO result = ContentsFileManagementViewResponseVO.builder().build();
                    BeanUtils.copyProperties(contentsMasterRepository.findOne(Example.of(ContentsMaster.builder()
                            .contentsCode(object.getContentsCode())
                            .build())).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2004, "관련 콘텐츠 정보 미존재")), result);

                    List<ContentsFolderInfo> folderList = new ArrayList<>();

                    File mainFolder = new File(new StringBuilder(appConfig.getUploadFile().getContentsContextPath())
                            .append("/")
                            .append(object.getContentsCode()).toString());

                    if (!mainFolder.exists()) { /** 요청 폴더가 존재하지 않을 경우에만 생성 */
                        try {
                            FileUtils.forceMkdir(mainFolder);
                        } catch (IOException e) {
                            logger.error("{}- {}", e.getClass().getCanonicalName(), e.getMessage(), e);
                            throw new KPFException(KPF_RESULT.ERROR2005, "폴더 생성 실패");
                        }
                    }

                    FileUtils.listFilesAndDirs(mainFolder, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE).stream()
                            .filter(data -> !requestObject.getContentsCode().equals(data.getName()))
                            .map(data -> {
                                if (data.isDirectory()) {
                                    return ContentsFolderInfo.builder()
                                            .folderName(data.getName())
                                            .folderPath(data.getPath().replaceAll(Matcher.quoteReplacement("\\"), "/").replaceAll(new StringBuilder(appConfig.getUploadFile().getContentsContextPath())
                                                    .append("/")
                                                    .append(object.getContentsCode()).toString(), ""))
                                            .subFolderList(new ArrayList<>())
                                            .build();
                                }
                                return null;
                            }).filter(data -> data != null)
                            .forEach(data -> {
                                if (data.getFolderPath().split("/").length == 2) {
                                    folderList.add(data);
                                } else if (data.getFolderPath().split("/").length == 3) {
                                    folderList.stream().forEach(depth1 -> {
                                        if (data.getFolderPath().contains(depth1.getFolderPath())) {
                                            depth1.getSubFolderList().add(data);
                                        }
                                    });
                                } else if (data.getFolderPath().split("/").length == 4) {
                                    folderList.stream().forEach(depth1 -> {
                                        depth1.getSubFolderList().stream().forEach(depth2 -> {
                                            if (data.getFolderPath().contains(depth2.getFolderPath())) {
                                                depth2.getSubFolderList().add(data);
                                            }
                                        });
                                    });
                                } else if (data.getFolderPath().split("/").length == 5) {
                                    folderList.stream().forEach(depth1 -> {
                                        depth1.getSubFolderList().stream().forEach(depth2 -> {
                                            depth2.getSubFolderList().stream().forEach(depth3 -> {
                                                if (data.getFolderPath().contains(depth3.getFolderPath())) {
                                                    depth3.getSubFolderList().add(data);
                                                }
                                            });
                                        });
                                    });
                                }
                            });

                    result.setSubFolderList(folderList);

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
     * 콘텐츠 폴더 관련 파일리스트 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getFileList(ContentsFileManagementViewRequestVO requestObject) {
        if(!Optional.ofNullable(requestObject.getContentsCode()).isPresent() || !Optional.ofNullable(requestObject.getFolderPath()).isPresent()) {
            throw new KPFException(KPF_RESULT.ERROR9001, "콘텐츠 코드 누락.");
        }
        return (Page<T>) Optional.ofNullable(requestObject)
                .filter(object -> StringUtils.isNotEmpty(object.getContentsCode()))
                .map(object -> {
                    ContentsFolderInfo resultVO = ContentsFolderInfo.builder()
                                                                        .folderName(requestObject.getFolderPath().split("/")[requestObject.getFolderPath().split("/").length - 1])
                                                                        .folderPath(requestObject.getFolderPath())
                                                                        .build();

                    File filePath = new File(new StringBuilder(appConfig.getUploadFile().getContentsContextPath())
                            .append("/")
                            .append(object.getContentsCode()).append(object.getFolderPath()).toString());

                    if (!filePath.exists()) { /** 요청 폴더가 존재하지 않을 경우에만 생성 */
                        try {
                            FileUtils.forceMkdir(filePath);
                        } catch (IOException e) {
                            logger.error("{}- {}", e.getClass().getCanonicalName(), e.getMessage(), e);
                            throw new KPFException(KPF_RESULT.ERROR2005, "폴더 생성 실패");
                        }
                    }

                    resultVO.setContentsFileInfoList(FileUtils.listFiles(filePath, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE).stream()
                            .filter(data -> String.valueOf(data.getParent()).equals(String.valueOf(filePath)))
                            .map(data -> {
                                try {
                                    return ContentsFileInfo.builder()
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
                            .count(resultVO.getContentsFileInfoList().size())
                            .offset(requestObject.getPageNum())
                            .limit(requestObject.getPageSize())
                            .build();
                    Pageable pageableToApply = summary.ensureValidPageable(requestObject.getPageable());

                    return CSPageImpl.of(Arrays.asList(resultVO), pageableToApply, resultVO.getContentsFileInfoList().size());
                })
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 콘텐츠 정보 생성
     *
     * @param contentsApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public ContentsApiResponseVO createContentsInfo(ContentsApiRequestVO contentsApiRequestVO) {
        ContentsMaster entity = ContentsMaster.builder().build();
        copyNonNullObject(contentsApiRequestVO, entity);
        if (contentsMasterRepository.findOne(Example.of(ContentsMaster.builder()
                        .contentsName(contentsApiRequestVO.getContentsName())
                        .build()))
                .isPresent()) { /** 기존 등록된 콘텐츠 정보 확인 */
            throw new KPFException(KPF_RESULT.ERROR2002, "동일 콘텐츠 존재");
        } else {
            ContentsApiResponseVO result = ContentsApiResponseVO.builder().build();
            entity.setContentsCode(commonContentsRepository.generateContentsCode(PREFIX_CONTENTS));
            /** 콘텐츠 정보 생성 */
            BeanUtils.copyProperties(contentsMasterRepository.saveAndFlush(entity), result);
            /** 콘텐츠 연결 챕터 목록 생성 */
            if(contentsApiRequestVO.getChapterList() != null && contentsApiRequestVO.getChapterList().size() > 0) {
                contentsApiRequestVO.getChapterList().stream()
                        .map(data -> {
                            ContentsChapter imsiChapter = ContentsChapter.builder()
                                    .contentsCode(entity.getContentsCode())
                                    .chapterCode(data.getValue())
                                    .build();
                            return contentsChapterRepository.findOne(Example.of(imsiChapter))
                                    .map(chap -> {
                                        chap.setSortNo(data.getSortNo());
                                        return contentsChapterRepository.save(chap);
                                    })
                                    .orElseGet(() -> { /** 연결된 챕터 정보가 존재하지 않으면... */
                                        imsiChapter.setSortNo(data.getSortNo());
                                        /** 관련 챕터 정보가 존재하는지 확인 */
                                        chapterMasterRepository.findOne(Example.of(ChapterMaster.builder()
                                                        .chapterCode(data.getValue())
                                                        .build()))
                                                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2041, "관련 챕터 정보가 존재하지 않습니다. (" + data.getValue() + ")"));
                                        /** 생성된 콘텐츠와 해당 챕터 연결 */
                                        return contentsChapterRepository.save(imsiChapter);
                                    });
                        }).collect(Collectors.toList());
            }
            return result;
        }
    }

    /**
     * 콘텐츠 정보 수정
     *
     * @param contentsApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public ContentsApiResponseVO updateContentsInfo(ContentsApiRequestVO contentsApiRequestVO) {
        return contentsMasterRepository.findOne(Example.of(ContentsMaster.builder()
                        .contentsCode(contentsApiRequestVO.getContentsCode())
                        .build()))
                .map(contentsMaster -> {
                    copyNonNullObject(contentsApiRequestVO, contentsMaster);

                    ContentsApiResponseVO result = ContentsApiResponseVO.builder().build();
                    BeanUtils.copyProperties(contentsMasterRepository.saveAndFlush(contentsMaster), result);
                    if(contentsApiRequestVO.getChapterList() != null && contentsApiRequestVO.getChapterList().size() == 0) {
                        /** 콘텐츠 연결 챕터 기존 목록 초기화 */
                        contentsChapterRepository.deleteAll(
                                contentsChapterRepository.findAll(Example.of(ContentsChapter.builder()
                                        .contentsCode(contentsMaster.getContentsCode())
                                        .build())));
                    } else if(contentsApiRequestVO.getChapterList() != null && contentsApiRequestVO.getChapterList().size() > 0) {
                        /** 콘텐츠 연결 챕터 기존 목록 초기화 */
                        contentsChapterRepository.deleteAll(
                                contentsChapterRepository.findAll(Example.of(ContentsChapter.builder()
                                        .contentsCode(contentsMaster.getContentsCode())
                                        .build())));
                        /** 콘텐츠 연결 챕터 목록 새로 갱신 */
                        contentsApiRequestVO.getChapterList().stream()
                                .map(data -> {
                                    ContentsChapter imsiChapter = ContentsChapter.builder()
                                            .contentsCode(contentsMaster.getContentsCode())
                                            .chapterCode(data.getValue())
                                            .build();
                                    return contentsChapterRepository.findOne(Example.of(imsiChapter))
                                            .map(chap -> {
                                                chap.setSortNo(data.getSortNo());
                                                return contentsChapterRepository.save(chap);
                                            })
                                            .orElseGet(() -> { /** 연결된 챕터 정보가 존재하지 않으면... */
                                                imsiChapter.setSortNo(data.getSortNo());
                                                /** 관련 챕터 정보가 존재하는지 확인 */
                                                chapterMasterRepository.findOne(Example.of(ChapterMaster.builder()
                                                                .chapterCode(data.getValue())
                                                                .build()))
                                                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2041, "관련 챕터 정보가 존재하지 않습니다. (" + data.getValue() + ")"));
                                                /** 생성된 콘텐츠와 해당 챕터 연결 */
                                                return contentsChapterRepository.save(imsiChapter);
                                            });
                                }).collect(Collectors.toList());
                    }

                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2003, "해당 콘텐츠 미존재"));
    }

    /**
     * 콘텐츠 썸네일 파일 업로드
     *
     * @param contentsCode
     * @param thumbnailFile
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport thumbnailFileUpload(String contentsCode, MultipartFile thumbnailFile) {
        /** 콘텐츠 존재 여부 확인 */
        ContentsMaster contentsMaster = contentsMasterRepository.findOne(Example.of(ContentsMaster.builder()
                        .contentsCode(contentsCode)
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2003, "해당 콘텐츠 미존재"));
        /** 파일 저장 및 파일 경로 셋팅 */
        Optional.ofNullable(thumbnailFile)
                .ifPresent(file -> {
                    Path directoryPath = Paths.get(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getThumbnailFolder()).toString());
                    try {
                        Files.createDirectories(directoryPath);
                        try {
                            String imageSequence = new StringBuilder("_").append(new SimpleDateFormat("yyMMddHHmmss").format(new Date())).toString();

                            String thumbnailFilePath = new StringBuilder(appConfig.getUploadFile().getUploadContextPath())
                                    .append(appConfig.getUploadFile().getThumbnailFolder())
                                    .append("/")
                                    .append(contentsCode + THUMB_IMG_TAG + imageSequence + "." + StringUtils.substringAfter(file.getOriginalFilename(), ".")).toString();
                            file.transferTo(new File(thumbnailFilePath));
                            contentsMaster.setThumbnailFilePath(thumbnailFilePath.replace(appConfig.getUploadFile().getUploadContextPath(), ""));
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
     * 콘텐츠 폴더 관리
     *
     * @param contentsCode
     * @param folderList
     * @return
     */
    public CSResponseVOSupport folderManagement(String contentsCode, List<ContentsFileManagementApiRequestVO> folderList) {
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
                File beforeFolder = FileUtils.getFile(new StringBuilder(appConfig.getUploadFile().getContentsContextPath())
                        .append("/")
                        .append(contentsCode)
                        .append(beforeFolderPath).toString());
                /** 변경 후 폴더 경로 */
                File afterFolder = FileUtils.getFile(new StringBuilder(appConfig.getUploadFile().getContentsContextPath())
                        .append("/")
                        .append(contentsCode)
                        .append(renameFolderPath).toString());
                if(!beforeFolder.exists()) { /** 변경하고자 하는 폴더가 존재하지 않을 경우 */
                    throw new KPFException(KPF_RESULT.ERROR2008, "존재하지 않는 폴더");
                }
                if(afterFolder.exists()) { /** 변경하고자 하는 폴더가 존재할 경우 */
                    throw new KPFException(KPF_RESULT.ERROR2009, "이미 존재하는 폴더");
                }

                StringBuilder imsiFolderPath = new StringBuilder(appConfig.getUploadFile().getContentsContextPath())
                        .append("/")
                        .append(contentsCode);

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
                File beforeFolder = FileUtils.getFile(new StringBuilder(appConfig.getUploadFile().getContentsContextPath())
                        .append("/")
                        .append(contentsCode)
                        .append(beforeFolderPath).toString());
                /** 이동 후 상위 폴더 경로 */
                File afterFolder = FileUtils.getFile(new StringBuilder(appConfig.getUploadFile().getContentsContextPath())
                        .append("/")
                        .append(contentsCode)
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
                File folder = FileUtils.getFile(new StringBuilder(appConfig.getUploadFile().getContentsContextPath())
                        .append("/")
                        .append(contentsCode)
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
                File folder = FileUtils.getFile(new StringBuilder(appConfig.getUploadFile().getContentsContextPath())
                        .append("/")
                        .append(contentsCode)
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
                File folder = FileUtils.getFile(new StringBuilder(appConfig.getUploadFile().getContentsContextPath())
                        .append("/")
                        .append(contentsCode)
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
     * 콘텐츠 파일 업로드
     *
     * @param contentsCode
     * @param filePath
     * @param contentsFileList
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport fileUpload(String contentsCode, String filePath, MultipartFile[] contentsFileList) {
        /** 콘텐츠 존재 여부 확인 */
        contentsMasterRepository.findOne(Example.of(ContentsMaster.builder()
                        .contentsCode(contentsCode)
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2003, "해당 콘텐츠 미존재"));
        /** 파일 저장 */
        Optional.ofNullable(contentsFileList)
                .filter(fileList -> fileList.length > 0)
                .ifPresent(fileList -> {
                    Arrays.stream(fileList).forEach(file -> {
                        Path directoryPath = Paths.get(new StringBuilder(appConfig.getUploadFile().getContentsContextPath())
                                .append("/")
                                .append(contentsCode)
                                .append(filePath).toString());
                        try {
                            Files.createDirectories(directoryPath);
                            try {
                                String uploadFilePath = new StringBuilder(appConfig.getUploadFile().getContentsContextPath())
                                        .append("/")
                                        .append(contentsCode)
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
     * @param contentsCode
     * @param filePath
     * @param zipFileList
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport zipFileUpload(String contentsCode, String filePath, MultipartFile[] zipFileList) {
        /** 콘텐츠 존재 여부 확인 */
        contentsMasterRepository.findOne(Example.of(ContentsMaster.builder()
                        .contentsCode(contentsCode)
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2003, "해당 콘텐츠 미존재"));
        /** 압축 해제 및 파일 저장 */
        Optional.ofNullable(zipFileList)
                .filter(fileList -> fileList.length > 0)
                .ifPresent(fileList -> {
                    Arrays.stream(fileList).forEach(file -> {
                        Path directoryPath = Paths.get(new StringBuilder(appConfig.getUploadFile().getContentsContextPath())
                                .append("/")
                                .append(contentsCode)
                                .append(filePath).toString());
                        try {
                            Files.createDirectories(directoryPath);
                            try {
                                /** 임시 디렉토리 생성 */
                                File tempDir = Files.createTempDirectory("temp").toFile();

                                /** 업로드된 파일 저장 */
                                File uploadedFile = new File(tempDir.getAbsolutePath() + File.separator + file.getOriginalFilename());
                                file.transferTo(uploadedFile);

                                /** 압축 해제 */
                                unzip(uploadedFile.getAbsolutePath(), appConfig.getUploadFile().getContentsContextPath() + File.separator + contentsCode + filePath);

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
     엑셀 다운로드
     */
    public <T> List<ContentsExcelVO> getExcel(ContentsViewRequestVO requestObject) {
        List<ContentsExcelVO> contentsExcelVOList = (List<ContentsExcelVO>) commonContentsRepository.findEntityListExcel(requestObject);
        return contentsExcelVOList;
    }

}
