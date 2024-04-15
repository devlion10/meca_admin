package kr.or.kpf.lms.biz.education.question.service;

import kr.or.kpf.lms.biz.education.question.vo.request.ExamQuestionApiRequestVO;
import kr.or.kpf.lms.biz.education.question.vo.request.ExamQuestionApiRequestVOS;
import kr.or.kpf.lms.biz.education.question.vo.request.ExamQuestionViewRequestVO;
import kr.or.kpf.lms.biz.education.question.vo.response.ExamQuestionApiResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonEducationRepository;
import kr.or.kpf.lms.repository.education.*;
import kr.or.kpf.lms.repository.entity.education.CurriculumMaster;
import kr.or.kpf.lms.repository.entity.education.CurriculumQuestion;
import kr.or.kpf.lms.repository.entity.education.ExamQuestionItem;
import kr.or.kpf.lms.repository.entity.education.ExamQuestionMaster;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 교육 관리 > 교육 과정 관리 API 관련 Service
 */
@Service
@RequiredArgsConstructor
public class ExamQuestionService extends CSServiceSupport {

    private static final String PREFIX_QUESTION = "QUES";
    private static final String PREFIX_QUESTION_ITEM = "ITEM";

    private static final String QUES_ATTACH_FILE_TAG = "_QUES";
    private static final String QUES_ITEM_ATTACH_FILE_TAG = "_ITEM";

    /** 교육 관리 공통 */
    private final CommonEducationRepository commonEducationRepository;
    /** 시험 관리 */
    private final ExamMasterRepository examMasterRepository;
    /** 시험 문제 관리 */
    private final ExamQuestionMasterRepository examQuestionMasterRepository;
    /** 시험 문제 문항 */
    private final ExamQuestionItemRepository examQuestionItemRepository;
    /** 시험 문제 목록 */
    private final CurriculumQuestionRepository curriculumQuestionRepository;
    /** 교육과정 관리 */
    private final CurriculumMasterRepository curriculumMasterRepository;

    /**
     * 시험 문제 정보 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(ExamQuestionViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonEducationRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 특정 교육 과정 조회
     *
     * @param curriculumCode
     * @return
     */
    public CurriculumMaster getCurriculumInfo(String curriculumCode) {
        return curriculumMasterRepository.findOne(Example.of(CurriculumMaster.builder().curriculumCode(curriculumCode).build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3004, "존재하지 않는 교육과정"));
    }

    /**
     * 시험 문제 정보 생성
     *
     * @param examQuestionApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public ExamQuestionApiResponseVO createQuestionInfo(ExamQuestionApiRequestVO examQuestionApiRequestVO, MultipartFile questionFile, List<MultipartFile> itemFiles, List<String> fileSortNo) {
        ExamQuestionMaster entity = ExamQuestionMaster.builder().build();
        copyNonNullObject(examQuestionApiRequestVO, entity);

        if(examQuestionApiRequestVO.getExamQuestionItemList() == null || examQuestionApiRequestVO.getExamQuestionItemList().size() == 0) {
            throw new KPFException(KPF_RESULT.ERROR3085, "최초 시험 문제 생성 시 문항 정보 필수");
        }

        ExamQuestionApiResponseVO result = ExamQuestionApiResponseVO.builder().build();
        String questionSerialNo = commonEducationRepository.generateQuestionSerialNo(PREFIX_QUESTION);
        entity.setQuestionSerialNo(questionSerialNo);

        /** 시험 문제 문항 생성 */
        List<ExamQuestionItem> questionItemList = examQuestionApiRequestVO.getExamQuestionItemList().stream()
                .map(item -> {
                    ExamQuestionItem examQuestionItem = ExamQuestionItem.builder().build();
                    copyNonNullObject(item, examQuestionItem);
                    examQuestionItem.setQuestionSerialNo(questionSerialNo);
                    examQuestionItem.setQuestionItemSerialNo(commonEducationRepository.generateQuestionItemSerialNo(PREFIX_QUESTION_ITEM));
                    examQuestionItemRepository.saveAndFlush(examQuestionItem);
                    Optional.ofNullable(fileSortNo).ifPresent(number -> number.stream().filter(sortNo -> Integer.parseInt(sortNo) == item.getQuestionSortNo())
                        .forEach(sortNo -> {
                            Integer index = fileSortNo.indexOf(sortNo);
                            /** 시험 문제 문항 파일 추가 */
                            examQuestionItemFileUpload(questionSerialNo, examQuestionItem.getQuestionItemSerialNo(), itemFiles.get(index));
                        }));
                    return examQuestionItem;
                }).collect(Collectors.toList());
        /** 시험 문제 문항 갯수 */
        entity.setQuestionItemCount(questionItemList.size());
        /** 시험 문제 정보 생성 */
        BeanUtils.copyProperties(examQuestionMasterRepository.saveAndFlush(entity), result);
        /** 시험 문제 파일 추가 */
        Optional.ofNullable(questionFile).ifPresent(file -> examQuestionFileUpload(questionSerialNo, file));
        /** 교육 과정과 연결 */
        curriculumQuestionRepository.saveAndFlush(CurriculumQuestion.builder()
                        .curriculumCode(examQuestionApiRequestVO.getCurriculumCode())
                        .questionSerialNo(entity.getQuestionSerialNo())
                        .build());

        return result;
    }

    /**
     * 시험 문제 정보 업데이트
     *
     * @param examQuestionApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public ExamQuestionApiResponseVO updateQuestionInfo(ExamQuestionApiRequestVO examQuestionApiRequestVO, MultipartFile questionFile, List<MultipartFile> itemFiles, List<String> fileSortNo) {
        return examQuestionMasterRepository.findOne(Example.of(ExamQuestionMaster.builder()
                                                                .questionSerialNo(examQuestionApiRequestVO.getQuestionSerialNo())
                                                                .build()))
                .map(questionMaster -> {

                    copyNonNullObject(examQuestionApiRequestVO, questionMaster);
                    ExamQuestionApiResponseVO result = ExamQuestionApiResponseVO.builder().build();

                    /** 교육과정 시험 문제 문항 정보가 존재할 경우 */
                    questionMaster.setQuestionItemCount(Optional.ofNullable(examQuestionApiRequestVO.getExamQuestionItemList())
                            .filter(list -> list.size() > 0)
                            .map(list -> {
                                /** 기존 시험 문제 문항 정보 */
                                List<ExamQuestionItem> existExamQuestionItemList = examQuestionItemRepository.findAll(Example.of(ExamQuestionItem.builder()
                                        .questionSerialNo(examQuestionApiRequestVO.getQuestionSerialNo())
                                        .build()));
                                List<String> existExamQuestionItemSerialNoList = existExamQuestionItemList.stream()
                                        .map(data -> data.getQuestionItemSerialNo())
                                        .collect(Collectors.toList());

                                /** 요청 시험 문제 문항 정보 */
                                List<String> requestExamQuestionItemSerialNoList = list.stream().map(data -> data.getQuestionItemSerialNo()).collect(Collectors.toList());

                                List<ExamQuestionItem> questionItemList = list.stream()
                                        .map(item -> {
                                            if(existExamQuestionItemSerialNoList.contains(item.getQuestionItemSerialNo())) { /** 기존 시험 문제 문항 정보인 경우... 업데이트 */
                                                return existExamQuestionItemList.stream().filter(existData -> existData.getQuestionItemSerialNo().equals(item.getQuestionItemSerialNo()))
                                                        .map(existData -> {
                                                            copyNonNullObject(item, existData);
                                                            existData.setQuestionSerialNo(examQuestionApiRequestVO.getQuestionSerialNo());
                                                            Optional.ofNullable(fileSortNo).ifPresent(number -> number.stream().filter(sortNo -> Integer.parseInt(sortNo) == item.getQuestionSortNo())
                                                                    .forEach(sortNo -> {
                                                                        Integer index = fileSortNo.indexOf(sortNo);
                                                                        /** 시험 문제 문항 파일 추가 */
                                                                        examQuestionItemFileUpload(existData.getQuestionSerialNo(), existData.getQuestionItemSerialNo(), itemFiles.get(index));
                                                                    }));
                                                            return existData;
                                                        }).findFirst().orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3083, "기존 시험 문제 문항 정보 변경 중 오류 발생"));
                                            } else { /** 기존 시험 문제 문항 정보가 아닌 경우... 신규 생성 */
                                                ExamQuestionItem examQuestionItem = ExamQuestionItem.builder().build();
                                                copyNonNullObject(item, examQuestionItem);
                                                examQuestionItem.setQuestionSerialNo(examQuestionApiRequestVO.getQuestionSerialNo());
                                                examQuestionItem.setQuestionItemSerialNo(commonEducationRepository.generateQuestionItemSerialNo(PREFIX_QUESTION_ITEM));
                                                examQuestionItemRepository.saveAndFlush(examQuestionItem);
                                                Optional.ofNullable(fileSortNo).ifPresent(number -> number.stream().filter(sortNo -> Integer.parseInt(sortNo) == item.getQuestionSortNo())
                                                        .forEach(sortNo -> {
                                                            Integer index = fileSortNo.indexOf(sortNo);
                                                            /** 시험 문제 문항 파일 추가 */
                                                            examQuestionItemFileUpload(examQuestionItem.getQuestionSerialNo(), examQuestionItem.getQuestionItemSerialNo(), itemFiles.get(index));
                                                        }));
                                                return examQuestionItem;
                                            }
                                        }).collect(Collectors.toList());

                                /** 요청하지 않은 시험 문제 문항 삭제 */
                                examQuestionItemRepository.deleteAll(existExamQuestionItemList.stream()
                                        .filter(data -> !requestExamQuestionItemSerialNoList.contains(data.getQuestionItemSerialNo()))
                                        .collect(Collectors.toList()));

                                return questionItemList.size();
                            }).orElse(0));

                    /** 시험 문제 정보 업데이트 */
                    BeanUtils.copyProperties(examQuestionMasterRepository.saveAndFlush(questionMaster), result);
                    /** 시험 문제 파일 추가 */
                    Optional.ofNullable(questionFile).ifPresent(file -> examQuestionFileUpload(examQuestionApiRequestVO.getQuestionSerialNo(), file));

                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3083, "해당 시험 문제 정보 미존재."));
    }

    /**
     * 시험 문제 정보 삭제
     *
     * @param examQuestions
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport deleteQuestionInfo(ExamQuestionApiRequestVOS examQuestions) {
        examQuestions.getExamQuestions().stream().forEach(data -> {
            ExamQuestionMaster deleteExamQuestion = examQuestionMasterRepository.findOne(Example.of(ExamQuestionMaster.builder()
                                                                                                        .questionSerialNo(data.getQuestionSerialNo())
                                                                                                        .build()))
                                                            .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3087, "삭제 대상 시험 문제 미존재."));
            /** 시험 문제 문항 정보 삭제 */
            examQuestionItemRepository.deleteAll(deleteExamQuestion.getQuestionItemList());
            /** 시험 문제 정보 삭제 */
            examQuestionMasterRepository.delete(deleteExamQuestion);
        });

        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     * 시험 문제 정보 생성 By 엑셀 파일
     *
     * @param excelFile
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport createQuestionInfoByFile(MultipartFile excelFile) {
        Workbook workBook = null;
        Sheet workSheet = null;
        try {

        } catch (Exception e) {
            logger.error("{}- {}", e.getClass().getCanonicalName(), e.getMessage(), e);
            throw new KPFException(KPF_RESULT.ERROR9999, "지원하지 않는 엑셀 형식입니다.");
        } finally {
            if(workBook != null) {
                try {
                    workBook.close();
                } catch (IOException e) {
                    throw new KPFException(KPF_RESULT.ERROR9999, "엑셀파일 처리 중 오류 발생.");
                }
            }
        }

        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     * 시험 문제 첨부파일 업로드
     *
     * @param questionSerialNo
     * @param questionFile
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport examQuestionFileUpload(String questionSerialNo, MultipartFile questionFile) {
        /** 시험 문제 정보 존재 여부 확인 */
        ExamQuestionMaster examQuestionMaster = examQuestionMasterRepository.findOne(Example.of(ExamQuestionMaster.builder()
                        .questionSerialNo(questionSerialNo)
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3087, "해당 시험 문제 정보 미존재"));

        /** 파일 저장 및 파일 경로 셋팅 */
        Optional.ofNullable(questionFile)
                .ifPresent(file -> {
                    Path directoryPath = Paths.get(new StringBuilder(appConfig.getUploadFile().getExamFolder()).toString());
                    try {
                        Files.createDirectories(directoryPath);
                        try {
                            String fileSequence = new StringBuilder("_").append(new SimpleDateFormat("yyMMddHHmmss").format(new Date())).toString();

                            String attachFilepath = new StringBuilder(appConfig.getUploadFile().getUploadContextPath())
                                    .append(appConfig.getUploadFile().getExamFolder())
                                    .append("/")
                                    .append(questionSerialNo + QUES_ATTACH_FILE_TAG + fileSequence + "." + StringUtils.substringAfter(file.getOriginalFilename(), ".")).toString();
                            file.transferTo(new File(attachFilepath));
                            examQuestionMaster.setQuestionFilePath(attachFilepath.replace(appConfig.getUploadFile().getUploadContextPath(), ""));
                            examQuestionMaster.setQuestionFileSize(file.getSize());
                            examQuestionMaster.setQuestionFileOrigin(file.getOriginalFilename());
                            examQuestionMasterRepository.saveAndFlush(examQuestionMaster);
                        } catch (IOException e) {
                            throw new KPFException(KPF_RESULT.ERROR9004, "파일 업로드 실패");
                        }
                    } catch (IOException e2) {
                        throw new KPFException(KPF_RESULT.ERROR9004, "파일 경로 확인 또는, 생성 실패");
                    }
                });

        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     * 시험 문제 문항 첨부파일 업로드
     *
     * @param questionSerialNo
     * @param questionFile
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport examQuestionItemFileUpload(String questionSerialNo, String questionItemSerialNo, MultipartFile questionFile) {
        /** 시험 문제 정보 존재 여부 확인 */
        ExamQuestionItem examQuestionItem = examQuestionItemRepository.findOne(Example.of(ExamQuestionItem.builder()
                        .questionSerialNo(questionSerialNo)
                        .questionItemSerialNo(questionItemSerialNo)
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3091, "해당 시험 문제 문항 정보 미존재"));

        /** 파일 저장 및 파일 경로 셋팅 */
        Optional.ofNullable(questionFile)
                .ifPresent(file -> {
                    Path directoryPath = Paths.get(new StringBuilder(appConfig.getUploadFile().getExamFolder()).toString());
                    try {
                        Files.createDirectories(directoryPath);
                        try {
                            String fileSequence = new StringBuilder("_").append(new SimpleDateFormat("yyMMddHHmmss").format(new Date())).toString();

                            String attachFilepath = new StringBuilder(appConfig.getUploadFile().getUploadContextPath())
                                    .append(appConfig.getUploadFile().getExamFolder())
                                    .append("/")
                                    .append(questionSerialNo + "_" + questionItemSerialNo + QUES_ITEM_ATTACH_FILE_TAG + fileSequence + "." + StringUtils.substringAfter(file.getOriginalFilename(), ".")).toString();
                            file.transferTo(new File(attachFilepath));
                            examQuestionItem.setQuestionItemFilePath(attachFilepath.replace(appConfig.getUploadFile().getUploadContextPath(), ""));
                            examQuestionItem.setQuestionItemFileSize(file.getSize());
                            examQuestionItem.setQuestionItemFileOrigin(file.getOriginalFilename());
                            examQuestionItemRepository.saveAndFlush(examQuestionItem);
                        } catch (IOException e) {
                            throw new KPFException(KPF_RESULT.ERROR9004, "파일 업로드 실패");
                        }
                    } catch (IOException e2) {
                        throw new KPFException(KPF_RESULT.ERROR9004, "파일 경로 확인 또는, 생성 실패");
                    }
                });

        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }
}
