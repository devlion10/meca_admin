package kr.or.kpf.lms.biz.education.application.service;

import kr.or.kpf.lms.biz.business.instructor.identify.vo.request.FormeBizlecinfoApiRequestVO;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.request.JournalismschoolApiRequestVO;
import kr.or.kpf.lms.biz.education.application.vo.ApplicationUploadExcelVO;
import kr.or.kpf.lms.biz.education.application.vo.request.EducationApplicationApiRequestVO;
import kr.or.kpf.lms.biz.education.application.vo.request.EducationApplicationViewRequestVO;
import kr.or.kpf.lms.biz.education.application.vo.request.EducationStateApiRequestVO;
import kr.or.kpf.lms.biz.education.application.vo.response.EducationApplicationApiResponseVO;
import kr.or.kpf.lms.biz.education.application.vo.response.EducationApplicationExcelVO;
import kr.or.kpf.lms.biz.education.application.vo.response.EducationCompleteExcelVO;
import kr.or.kpf.lms.biz.user.history.vo.JournalismschoolViewRequestVO;
import kr.or.kpf.lms.biz.user.webuser.service.WebUserService;
import kr.or.kpf.lms.common.converter.DateToStringConverter;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.common.support.Code;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonEducationRepository;
import kr.or.kpf.lms.repository.contents.ChapterApplicationSectionRepostiroy;
import kr.or.kpf.lms.repository.contents.ContentsApplicationChapterRepository;
import kr.or.kpf.lms.repository.education.*;
import kr.or.kpf.lms.repository.entity.BizInstructorDistCrtrAmtItem;
import kr.or.kpf.lms.repository.entity.contents.ChapterApplicationSection;
import kr.or.kpf.lms.repository.entity.contents.ContentsApplicationChapter;
import kr.or.kpf.lms.repository.entity.education.*;
import kr.or.kpf.lms.repository.entity.user.LmsUser;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 교육신청 관련 Service
 */
@Service
@RequiredArgsConstructor
public class EducationApplicationService extends CSServiceSupport {

    private static final String APPLICATION_IMG_TAG = "_APPLICATION";
    private static final String PREFIX_APPLICATION = "APLY";

    private final CommonEducationRepository commonEducationRepository;

    private final EducationPlanRepository educationPlanRepository;
    private final CurriculumMasterRepository curriculumMasterRepository;

    private final CurriculumQuestionRepository curriculumQuestionRepository;
    private final CurriculumApplicationMasterRepository curriculumApplicationMasterRepository;
    private final CurriculumApplicationContentsRepository curriculumApplicationContentsRepository;
    private final ContentsApplicationChapterRepository contentsApplicationChapterRepository;
    private final ChapterApplicationSectionRepostiroy chapterApplicationSectionRepository;
    private final CurriculumApplicationExamRepository curriculumApplicationExamRepository;
    private final ExamApplicationQuestionRepository examApplicationQuestionRepository;
    private final CurriculumApplicationEvaluateRepository curriculumApplicationEvaluateRepository;

    private final CurriculumApplicationLectureRepository curriculumApplicationLectureRepository;

    private final WebUserService webUserService;
    /**
     * 교육 관리 > 교육 운영 관리 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(EducationApplicationViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonEducationRepository.findEntityList(requestObject))
                            .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 강제 교육 신청
     *
     * @param educationApplicationApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public EducationApplicationApiResponseVO createEducationApplication(EducationApplicationApiRequestVO educationApplicationApiRequestVO) {
        CurriculumApplicationMaster curriculumApplicationMaster = CurriculumApplicationMaster.builder().build();
        copyNonNullObject(educationApplicationApiRequestVO, curriculumApplicationMaster);

        EducationPlan educationPlan = educationPlanRepository.findOne(Example.of(EducationPlan.builder()
                        .educationPlanCode(educationApplicationApiRequestVO.getEducationPlanCode())
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3064, "존재하지 않는 교육 과정 일정"));

        /** 신청 인원이 허용 인원보다 크거나 같은 경우 */
        if (educationPlan.getNumberOfPeople() != null && educationPlan.getNumberOfPeople() > 0
                && educationPlan.getNumberOfPeople() <= curriculumApplicationMasterRepository.findAll(Example.of(CurriculumApplicationMaster.builder()
                .adminApprovalState(Code.ADM_APL_STATE.APPROVAL.enumCode)
                .educationPlanCode(educationPlan.getEducationPlanCode())
                .build())).size()) {
            throw new RuntimeException("정원을 초과하였습니다.");
        }

        curriculumApplicationMaster.setUserId(educationApplicationApiRequestVO.getUserId());
        curriculumApplicationMaster.setIsComplete("W");
        curriculumApplicationMaster.setSetEducationType(educationApplicationApiRequestVO.getSetEducationType());
        /** 신청 승인 처리 */
        curriculumApplicationMaster.setAdminApprovalState(Code.ADM_APL_STATE.APPROVAL.enumCode);
        /** 교육 진행 중 처리 */
        curriculumApplicationMaster.setEducationState(Code.EDU_STATE.PROCEEDING.enumCode);
        /** 교육 과정 코드 */
        curriculumApplicationMaster.setCurriculumCode(educationApplicationApiRequestVO.getCurriculumCode());

        /** 교육 상시 여부에 따른 교육 기간 정의 */
        if(Code.OPER_TYPE.CARDINAL.equals(Code.OPER_TYPE.enumOfCode(educationPlan.getOperationType()))) {
            /** 교육 시작 일시 (교육 계획에 지정된 교육 시작일자로 지정) */
            curriculumApplicationMaster.setOperationBeginDateTime(educationPlan.getOperationBeginDateTime());
            /** 교육 종료 일시 (교육 계획에 지정된 교육 종료일자로 지정) */
            curriculumApplicationMaster.setOperationEndDateTime(educationPlan.getOperationEndDateTime());
        } else if(Code.OPER_TYPE.ALWAYS.equals(Code.OPER_TYPE.enumOfCode(educationPlan.getOperationType()))) {
            /** 교육 시작 일시 (신청일 기준으로 시작일자 지정) */
            curriculumApplicationMaster.setOperationBeginDateTime(new DateToStringConverter().convertToEntityAttribute(new Date()));
            /** 교육 종료 일시 (신청일 기준으로 교육 유효기간으로 종료일자 지정) */
            curriculumApplicationMaster.setOperationEndDateTime(new DateToStringConverter().convertToEntityAttribute(new DateTime().plusDays(educationPlan.getAlwaysEducationTerm()).toDate()));
        }
        /** 교육 점수 '0'으로 초기화 */
        curriculumApplicationMaster.setProgressRate(0.0); curriculumApplicationMaster.setProgressScore(0); curriculumApplicationMaster.setExamScore(0); curriculumApplicationMaster.setAssignmentScore(0);

        if(curriculumApplicationMasterRepository.findByEducationPlanCodeAndUserId(curriculumApplicationMaster.getEducationPlanCode(), curriculumApplicationMaster.getUserId()) != null) { /** 기존에 신청한 내역이 존재할 경우 */
            throw new KPFException(KPF_RESULT.ERROR3023, "이미 등록되어 있는 교육 과정 신청 내역 존재");
        } else { /** 기존에 신청한 내역이 존재하지 않을 경우 */
            CurriculumApplicationMaster entity = CurriculumApplicationMaster.builder().build();
            BeanUtils.copyProperties(curriculumApplicationMasterRepository.saveAndFlush(curriculumApplicationMaster), entity);
            return educationApplication(entity);
        }
    }

    /**
     * 교육 신청 관련 데이터 생성
     *
     * @param entity
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public EducationApplicationApiResponseVO educationApplication(CurriculumApplicationMaster entity) {
        return this.educationApplication(entity, false);
    }

    /**
     * 교육 신청 관련 데이터 생성
     *
     * @param entity
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public EducationApplicationApiResponseVO educationApplication(CurriculumApplicationMaster entity, Boolean isExamReset) {
        EducationApplicationApiResponseVO result = EducationApplicationApiResponseVO.builder().build();
        EducationPlan educationPlan = educationPlanRepository.findOne(Example.of(EducationPlan.builder()
                        .educationPlanCode(entity.getEducationPlanCode())
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3064, "존재하지 않는 교육 과정 일정"));

        /** 교육 신청 일련번호 획득 */
        String applicationNo = Optional.ofNullable(entity.getApplicationNo())
                .map(data -> {
                    /** 교육 신청 승인 취소 후 재승인일 수 있기 때문에, 기존 신청 내역 삭제 */
                    if (educationPlan.getCurriculumMaster().getCurriculumContentsList() != null && educationPlan.getCurriculumMaster().getCurriculumContentsList().size() > 0) {
                        if(!isExamReset) { /** 시험만 초기화가 아닌 전체 초기화일 경우 */
                            /** 개인 별 신청 콘텐츠 내역 삭제 */
                            curriculumApplicationContentsRepository.deleteAll(curriculumApplicationContentsRepository.findAll(Example.of(CurriculumApplicationContents.builder()
                                    .applicationNo(data)
                                    .userId(entity.getUserId())
                                    .build())));

                            /** 개인 별 신청 콘텐츠 챕터 목록 삭제 */
                            contentsApplicationChapterRepository.deleteAll(contentsApplicationChapterRepository.findAll(Example.of(ContentsApplicationChapter.builder()
                                    .applicationNo(data)
                                    .userId(entity.getUserId())
                                    .build())));

                            /** 개인 별 신청 챕터 목록에 대한 차시(절) 목록 삭제 */
                            chapterApplicationSectionRepository.deleteAll(chapterApplicationSectionRepository.findAll(Example.of(ChapterApplicationSection.builder()
                                    .applicationNo(data)
                                    .userId(entity.getUserId())
                                    .build())));
                        }
                        /** 개인 별 신청 시험 내역 삭제 */
                        curriculumApplicationExamRepository.deleteAll(curriculumApplicationExamRepository.findAll(Example.of(CurriculumApplicationExam.builder()
                                .applicationNo(data)
                                .userId(entity.getUserId())
                                .build())));
                        /** 개인 별 신청 시험 문제 내역 삭제 */
                        examApplicationQuestionRepository.deleteAll(examApplicationQuestionRepository.findAll(Example.of(ExamApplicationQuestion.builder()
                                .applicationNo(data)
                                .userId(entity.getUserId())
                                .build())));
                    } else {
                        /** 개인 별 신청 강의 목록 삭제 */
                        curriculumApplicationLectureRepository.deleteAll(curriculumApplicationLectureRepository.findAll(Example.of(CurriculumApplicationLecture.builder()
                                .applicationNo(data)
                                .userId(entity.getUserId())
                                .build())));
                    }
                    /** 개인 별 설문(평가) 내역이 존재할 경우 삭제 */
                    Optional.ofNullable(curriculumApplicationEvaluateRepository.findAll(Example.of(CurriculumApplicationEvaluate.builder()
                                    .applicationNo(data)
                                    .curriculumCode(educationPlan.getCurriculumCode())
                                    .userId(entity.getUserId())
                                    .build())))
                            .filter(dataList -> dataList.size() > 0)
                            .ifPresent(curriculumApplicationEvaluateRepository::deleteAll);
                    return data;
                }).orElseGet(() -> entity.getApplicationNo());

        /** 교육과정에 콘텐츠 정보가 연결되어 있을 경우에만... 즉, 이러닝 교육일 경우에만 해당 */
        if(educationPlan.getCurriculumMaster().getCurriculumContentsList() != null && educationPlan.getCurriculumMaster().getCurriculumContentsList().size() > 0) {
            /** 신청한 교육 과정에 해당하는 나만의 교육 콘텐츠 목록 생성 */
            curriculumApplicationContentsRepository.saveAllAndFlush(educationPlan.getCurriculumMaster().getCurriculumContentsList().stream()
                    .filter(subData -> subData != null && subData.getContentsMaster() != null)
                    .map(subData -> {
                        /** 신청한 콘텐츠에 해당하는 나만의 챕터 목록 생성 */
                        contentsApplicationChapterRepository.saveAllAndFlush(subData.getContentsMaster().getContentsChapterList().stream()
                                .filter(imsiChapterData -> imsiChapterData != null && imsiChapterData.getChapterMaster() != null)
                                .map(imsiChapterData -> {
                                    ContentsApplicationChapter contentsApplicationChapter = ContentsApplicationChapter.builder().build();
                                    BeanUtils.copyProperties(imsiChapterData, contentsApplicationChapter);
                                    BeanUtils.copyProperties(imsiChapterData.getChapterMaster(), contentsApplicationChapter);
                                    contentsApplicationChapter.setCurriculumCode(educationPlan.getCurriculumCode());
                                    contentsApplicationChapter.setApplicationNo(applicationNo);
                                    contentsApplicationChapter.setUserId(entity.getUserId());
                                    /** 나만의 챕터 목록에 해당하는 섹션(절)정보 목록 생성 */
                                    chapterApplicationSectionRepository.saveAllAndFlush(imsiChapterData.getChapterMaster().getSectionMasterList().stream()
                                            .filter(imsiSectionData -> imsiSectionData != null)
                                            .map(imsiSectionData -> {
                                                ChapterApplicationSection chapterApplicationSection = ChapterApplicationSection.builder().build();
                                                BeanUtils.copyProperties(imsiSectionData, chapterApplicationSection);
                                                chapterApplicationSection.setCurriculumCode(educationPlan.getCurriculumCode());
                                                chapterApplicationSection.setContentsCode(imsiChapterData.getContentsCode());
                                                chapterApplicationSection.setApplicationNo(applicationNo);
                                                chapterApplicationSection.setUserId(entity.getUserId());
                                                chapterApplicationSection.setProgressRate(0.0);
                                                chapterApplicationSection.setIsComplete(false);
                                                return chapterApplicationSection;
                                            }).collect(Collectors.toList()));

                                    return contentsApplicationChapter;
                                }).collect(Collectors.toList()));

                        CurriculumApplicationContents curriculumApplicationContents = CurriculumApplicationContents.builder().build();
                        BeanUtils.copyProperties(subData, curriculumApplicationContents);
                        BeanUtils.copyProperties(subData.getContentsMaster(), curriculumApplicationContents);
                        curriculumApplicationContents.setCurriculumCode(educationPlan.getCurriculumMaster().getCurriculumCode());
                        curriculumApplicationContents.setApplicationNo(applicationNo);
                        curriculumApplicationContents.setUserId(entity.getUserId());
                        return curriculumApplicationContents;
                    }).collect(Collectors.toList()));

            /** 신청한 교육 과정에 해당하는 나만의 시험 목록 생성 */
            curriculumApplicationExamRepository.saveAllAndFlush(educationPlan.getCurriculumMaster().getCurriculumExamList().stream()
                    .filter(subData -> subData != null && subData.getExamMaster() != null)
                    .map(subData -> {
                        CurriculumApplicationExam curriculumApplicationExam = CurriculumApplicationExam.builder().build();
                        BeanUtils.copyProperties(subData, curriculumApplicationExam);
                        BeanUtils.copyProperties(subData.getExamMaster(), curriculumApplicationExam);
                        curriculumApplicationExam.setCurriculumCode(educationPlan.getCurriculumMaster().getCurriculumCode());
                        curriculumApplicationExam.setApplicationNo(applicationNo);
                        curriculumApplicationExam.setUserId(entity.getUserId());
                        return curriculumApplicationExam;
                    }).collect(Collectors.toList()));

            /** 신청한 교육 과정에 해당하는 나만의 시험 문제 생성 */
            educationPlan.getCurriculumMaster().getCurriculumExamList().forEach(data -> {
                List<ExamApplicationQuestion> examApplicationQuestionList = new ArrayList<>();
                /** 난이도 상 */
                Optional.ofNullable(data.getExamMaster().getGradeFirstSelectQuestionCount())
                        .filter(questionCount -> questionCount > 0)
                        .map(questionCount -> difficultyLevelExamList(data.getCurriculumCode(), Code.QUE_LEVEL.HIGH, questionCount).stream()
                                .map(highQuestion -> ExamApplicationQuestion.builder()
                                        .applicationNo(applicationNo)
                                        .curriculumCode(data.getCurriculumCode())
                                        .examSerialNo(data.getExamSerialNo())
                                        .questionSerialNo(highQuestion.getQuestionSerialNo())
                                        .userId(entity.getUserId())
                                        .isComplete(false)
                                        .build())
                                .collect(Collectors.toList()))
                        .ifPresent(examApplicationQuestionList::addAll);
                /** 난이도 중 */
                Optional.ofNullable(data.getExamMaster().getGradeSecondSelectQuestionCount())
                        .filter(questionCount -> questionCount > 0)
                        .map(questionCount -> difficultyLevelExamList(data.getCurriculumCode(), Code.QUE_LEVEL.MEDIUM, questionCount).stream()
                                .map(mediumQuestion -> ExamApplicationQuestion.builder()
                                        .applicationNo(applicationNo)
                                        .curriculumCode(data.getCurriculumCode())
                                        .examSerialNo(data.getExamSerialNo())
                                        .questionSerialNo(mediumQuestion.getQuestionSerialNo())
                                        .userId(entity.getUserId())
                                        .isComplete(false)
                                        .build())
                                .collect(Collectors.toList()))
                        .ifPresent(examApplicationQuestionList::addAll);
                /** 난이도 하 */
                Optional.ofNullable(data.getExamMaster().getGradeThirdSelectQuestionCount())
                        .filter(questionCount -> questionCount > 0)
                        .map(questionCount -> difficultyLevelExamList(data.getCurriculumCode(), Code.QUE_LEVEL.LOW, questionCount).stream()
                                .map(lowQuestion -> ExamApplicationQuestion.builder()
                                        .applicationNo(applicationNo)
                                        .curriculumCode(data.getCurriculumCode())
                                        .examSerialNo(data.getExamSerialNo())
                                        .questionSerialNo(lowQuestion.getQuestionSerialNo())
                                        .userId(entity.getUserId())
                                        .isComplete(false)
                                        .build())
                                .collect(Collectors.toList()))
                        .ifPresent(examApplicationQuestionList::addAll);

                AtomicInteger sortNo = new AtomicInteger(0);
                examApplicationQuestionRepository.saveAllAndFlush(examApplicationQuestionList.parallelStream().collect(Collectors.toList())
                        .stream().peek(myExamInfo -> myExamInfo.setSortNo(sortNo.incrementAndGet()))
                        .collect(Collectors.toList()));
            });
        } else {    /** 이러닝 교육이 아닌 교육일 경우.... 집합 교육 or 화상 교육 */
            CurriculumApplicationLecture curriculumApplicationLecture = CurriculumApplicationLecture.builder()
                    .applicationNo(applicationNo)
                    .curriculumCode(educationPlan.getCurriculumCode())
                    .userId(entity.getUserId())
                    .build();

            educationPlan.getLectureMasterList().forEach(lectureMaster -> {
                curriculumApplicationLecture.setLectureCode(lectureMaster.getLectureCode());
                curriculumApplicationLecture.setIsAttend(false);
                curriculumApplicationLectureRepository.saveAndFlush(curriculumApplicationLecture);
            });
        }

        BeanUtils.copyProperties(curriculumApplicationMasterRepository.saveAndFlush(entity), result);
        return result;
    }

    /**
     * 난이도 별 시험 문제 추출
     *
     * @param curriculumCode
     * @param level
     * @param count
     * @return
     */
    public List<CurriculumQuestion> difficultyLevelExamList(String curriculumCode, Code.QUE_LEVEL level, Integer count) {
        return curriculumQuestionRepository.findAll(Example.of(CurriculumQuestion.builder()
                        .curriculumCode(curriculumCode)
                        .build())).parallelStream()
                .filter(mappingInfo -> level.equals(Code.QUE_LEVEL.enumOfCode(mappingInfo.getExamQuestionMaster().getQuestionLevel())))
                .distinct()
                .limit(count)
                .collect(Collectors.toList());
    }

    /**
     * 강제 교육 신청 By 엑셀 파일
     *
     * @param curriculumCode
     * @param excelFile
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport createEducationApplicationByFile(String curriculumCode,String educationPlanCode, MultipartFile excelFile) {
        Workbook workBook = null;
        Sheet workSheet = null;

        try {
            workBook = new XSSFWorkbook(excelFile.getInputStream());
            workSheet = workBook.getSheetAt(0);
            List<ApplicationUploadExcelVO> applicationUploadExcelList = new ArrayList<>();

            for(int index = 1; index < workSheet.getPhysicalNumberOfRows(); ++index) { /** 엑셀에서 데이터 추출하여 리스트 객체에 파싱 */
                ApplicationUploadExcelVO data = new ApplicationUploadExcelVO();
                Row row = workSheet.getRow(index);
                data.setUserId(row.getCell(0).getCellType() == CellType.NUMERIC ? String.format("%.0f",row.getCell(0).getNumericCellValue()) : row.getCell(0).getStringCellValue());
                applicationUploadExcelList.add(data);
            }

            applicationUploadExcelList.stream().forEach(data -> { /** 파싱한 데이터 리스트를 건별로 교육 신청 함수 호출. */
                try{
                    LmsUser user = webUserService.getUserInfoById(data.getUserId());
                    EducationApplicationApiRequestVO requestObject = EducationApplicationApiRequestVO.builder().build();
                    BeanUtils.copyProperties(data, requestObject);
                    requestObject.setCurriculumCode(curriculumCode);
                    requestObject.setEducationPlanCode(educationPlanCode);
                    requestObject.setAdminApprovalState("2");
                    requestObject.setIsComplete("W");
                    this.createEducationApplication(requestObject);
                } catch (Exception e){
                    logger.error("{}- {}", e.getClass().getCanonicalName(), e.getMessage(), e);
                    throw new KPFException(KPF_RESULT.ERROR9999, "엑셀에 작성한 아이디 오류");
                }
            });

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
     * 교육 방식 변경
     *
     * @param educationApplicationApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public EducationApplicationApiResponseVO updateEducationApplicationEducationType(EducationApplicationApiRequestVO educationApplicationApiRequestVO) {
        return Optional.ofNullable(educationApplicationApiRequestVO)
                .filter(object -> educationApplicationApiRequestVO.getApplicationList().size() > 0)
                .map(object -> {
                    EducationApplicationApiResponseVO result = EducationApplicationApiResponseVO.builder().build();
                    result.setApplicationList(object.getApplicationList().stream()
                            .map(info -> {
                                EducationApplicationApiResponseVO educationApplicationApiResponseVO = EducationApplicationApiResponseVO.builder().build();
                                /** 교육 신청 내역 조회 */
                                CurriculumApplicationMaster entity = curriculumApplicationMasterRepository.findOne(Example.of(CurriculumApplicationMaster.builder()
                                        .applicationNo(info.getApplicationNo())
                                        .educationPlanCode(info.getEducationPlanCode())
                                        .curriculumCode(info.getCurriculumCode())
                                        .userId(info.getUserId())
                                        .build())).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3025, "해당 교육 과정 신청 이력 미존재."));
                                /** 숙박 여부 변경 */
                                entity.setSetEducationType(info.getSetEducationType());
                                BeanUtils.copyProperties(curriculumApplicationMasterRepository.saveAndFlush(entity), educationApplicationApiResponseVO);
                                return educationApplicationApiResponseVO;
                            }).collect(Collectors.toList()));
                    return result;
                })
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9001, "관리자 승인 여부 필수."));
    }

    /**
     * 교육 신청 승인 여부 변경
     *
     * @param educationApplicationApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public EducationApplicationApiResponseVO updateEducationApplicationApprovalState(EducationApplicationApiRequestVO educationApplicationApiRequestVO) {
        return Optional.ofNullable(educationApplicationApiRequestVO)
                .filter(object -> educationApplicationApiRequestVO.getApplicationList().size() > 0)
                .map(object -> {
                    EducationApplicationApiResponseVO result = EducationApplicationApiResponseVO.builder().build();
                    result.setApplicationList(object.getApplicationList().stream()
                            .map(info -> {
                                EducationApplicationApiResponseVO educationApplicationApiResponseVO = EducationApplicationApiResponseVO.builder().build();
                                /** 교육 신청 내역 조회 */
                                CurriculumApplicationMaster entity = curriculumApplicationMasterRepository.findOne(Example.of(CurriculumApplicationMaster.builder()
                                        .applicationNo(info.getApplicationNo())
                                        .educationPlanCode(info.getEducationPlanCode())
                                        .curriculumCode(info.getCurriculumCode())
                                        .userId(info.getUserId())
                                        .build())).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3025, "해당 교육 과정 신청 이력 미존재."));
                                /** 교육 신청 내역 대기 or 승인 취소 or 사용자 취소 / 승인 처리 */
                                switch(Code.ADM_APL_STATE.enumOfCode(info.getAdminApprovalState())) {
                                    case WAIT: case REFUSE: case CANCEL:
                                        entity.setAdminApprovalState(info.getAdminApprovalState());
                                        entity.setEducationState(Code.EDU_STATE.WAIT.enumCode);
                                        /** 교육 점수 '0'으로 초기화 */
                                        entity.setProgressRate(0.0); entity.setProgressScore(0); entity.setExamScore(0); entity.setAssignmentScore(0);
                                        /** 수료 여부 초기화 */
                                        entity.setIsComplete("W"); entity.setCompleteDateTime(null);
                                        BeanUtils.copyProperties(curriculumApplicationMasterRepository.saveAndFlush(entity), educationApplicationApiResponseVO);
                                        return educationApplicationApiResponseVO;
                                    case APPROVAL:
                                        EducationPlan educationPlan = educationPlanRepository.findOne(Example.of(EducationPlan.builder()
                                                        .educationPlanCode(entity.getEducationPlanCode())
                                                        .build()))
                                                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3064, "존재하지 않는 교육 과정 일정"));

                                        /** 승인 인원이 허용 인원보다 크거나 같은 경우 */
                                        if (educationPlan.getNumberOfPeople() != null && educationPlan.getNumberOfPeople() > 0
                                                && educationPlan.getNumberOfPeople() <= curriculumApplicationMasterRepository.findAll(Example.of(CurriculumApplicationMaster.builder()
                                                .adminApprovalState(Code.ADM_APL_STATE.APPROVAL.enumCode)
                                                .educationPlanCode(educationPlan.getEducationPlanCode())
                                                .build())).size()) {
                                            throw new RuntimeException("정원을 초과하였습니다.");
                                        }

                                        entity.setAdminApprovalState(info.getAdminApprovalState());
                                        /** 교육 진행 중 처리 */
                                        entity.setEducationState(Code.EDU_STATE.PROCEEDING.enumCode);
                                        /** 교육 상시 여부에 따른 교육 기간 정의 */
                                        if(Code.OPER_TYPE.CARDINAL.equals(Code.OPER_TYPE.enumOfCode(educationPlan.getOperationType()))) {
                                            /** 교육 시작 일시 (교육 계획에 지정된 교육 시작일자로 지정) */
                                            entity.setOperationBeginDateTime(educationPlan.getOperationBeginDateTime());
                                            /** 교육 종료 일시 (교육 계획에 지정된 교육 종료일자로 지정) */
                                            entity.setOperationEndDateTime(educationPlan.getOperationEndDateTime());
                                        } else if(Code.OPER_TYPE.ALWAYS.equals(Code.OPER_TYPE.enumOfCode(educationPlan.getOperationType()))) {
                                            /** 교육 시작 일시 (신청일 기준으로 시작일자 지정) */
                                            entity.setOperationBeginDateTime(new DateToStringConverter().convertToEntityAttribute(new Date()));
                                            /** 교육 종료 일시 (신청일 기준으로 교육 유효기간으로 종료일자 지정) */
                                            entity.setOperationEndDateTime(new DateToStringConverter().convertToEntityAttribute(new DateTime().plusDays(educationPlan.getAlwaysEducationTerm()).toDate()));
                                        }
                                        return educationApplication(curriculumApplicationMasterRepository.saveAndFlush(entity));
                                    default:
                                        throw new KPFException(KPF_RESULT.ERROR9998, "유효하지 않은 교육 신청 내역 코드.");
                                }
                            }).collect(Collectors.toList()));
                    return result;
                })
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9001, "관리자 승인 여부 필수."));
    }

    /**
     * 숙박 여부 변경
     *
     * @param educationApplicationApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public EducationApplicationApiResponseVO updateAccommodationState(EducationApplicationApiRequestVO educationApplicationApiRequestVO) {
        return Optional.ofNullable(educationApplicationApiRequestVO)
                .filter(object -> educationApplicationApiRequestVO.getApplicationList().size() > 0)
                .map(object -> {
                    EducationApplicationApiResponseVO result = EducationApplicationApiResponseVO.builder().build();
                    result.setApplicationList(object.getApplicationList().stream()
                            .map(info -> {
                                EducationApplicationApiResponseVO educationApplicationApiResponseVO = EducationApplicationApiResponseVO.builder().build();

                                /** 교육 신청 내역 조회 */
                                CurriculumApplicationMaster entity = curriculumApplicationMasterRepository.findOne(Example.of(CurriculumApplicationMaster.builder()
                                        .applicationNo(info.getApplicationNo())
                                        .educationPlanCode(info.getEducationPlanCode())
                                        .curriculumCode(info.getCurriculumCode())
                                        .userId(info.getUserId())
                                        .build())).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3025, "해당 교육 과정 신청 이력 미존재."));

                                /** 숙박 여부 변경 */
                                entity.setIsAccommodation(info.getIsAccommodation());
                                BeanUtils.copyProperties(curriculumApplicationMasterRepository.saveAndFlush(entity), educationApplicationApiResponseVO);

                                return educationApplicationApiResponseVO;
                            }).collect(Collectors.toList()));
                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9001, "교육 과정 신청 내역이 존재하지 않습니다."));
    }

    /**
     * 수강 교육 과정 상태 변경 처리(수료, 미수료)
     *
     * @param educationApplicationApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public EducationApplicationApiResponseVO updateEducationState(EducationApplicationApiRequestVO educationApplicationApiRequestVO) {
        return Optional.ofNullable(educationApplicationApiRequestVO)
                .filter(object -> object.getApplicationList().size() > 0)
                .map(object -> {
                    EducationApplicationApiResponseVO result = EducationApplicationApiResponseVO.builder().build();
                    result.setApplicationList(object.getApplicationList().stream()
                            .map(info -> {
                                EducationApplicationApiResponseVO educationApplicationApiResponseVO = EducationApplicationApiResponseVO.builder().build();
                                /** 교육 신청 내역 조회 */
                                CurriculumApplicationMaster entity = curriculumApplicationMasterRepository.findOne(Example.of(CurriculumApplicationMaster.builder()
                                        .applicationNo(info.getApplicationNo())
                                        .educationPlanCode(info.getEducationPlanCode())
                                        .curriculumCode(info.getCurriculumCode())
                                        .userId(info.getUserId())
                                        .build())).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3025, "해당 교육 과정 신청 이력 미존재."));
                                /** 미수료로 변경하는 것이라면... 교육 진행 중이던 과정들을 어떻게 처리할 것인지... */
                                if(info.getIsComplete().equals("Y")) {
                                    /** 진도점수 셋팅 */
                                    if(info.getProgressScore() != null) entity.setProgressScore(info.getProgressScore());
                                    /** 시험점수 셋팅 */
                                    if(info.getExamScore() != null) entity.setExamScore(info.getExamScore());
                                    /** 수료 여부 셋팅 */
                                    entity.setIsComplete(info.getIsComplete());
                                    /** 수료 일자 셋팅 */
                                    entity.setCompleteDateTime(new DateToStringConverter().convertToEntityAttribute(new Date()));
                                    /** 교육 완료 상태로 변경 */
                                    entity.setEducationState(Code.EDU_STATE.END.enumCode);
                                    /** 연수 이수 번호 생성 */
                                    if(!StringUtils.isEmpty(entity.getEducationPlan().getProgramNumber()) && entity.getEducationPlan().getCurriculumMaster().getEducationTarget().equals("3")) {
                                        entity.setProgramCompleteNumber(String.format("%07d", Integer.parseInt(curriculumApplicationMasterRepository.findAll(Example.of(CurriculumApplicationMaster.builder()
                                                        .educationPlanCode(info.getEducationPlanCode())
                                                        .build())).stream()
                                                .filter(curriculumApplicationMaster -> curriculumApplicationMaster.getProgramCompleteNumber() != null)
                                                .max(Comparator.comparing(CurriculumApplicationMaster::getProgramCompleteNumber))
                                                .map(CurriculumApplicationMaster::getProgramCompleteNumber)
                                                .orElse(entity.getEducationPlan().getProgramNumber() + "000")) + 1));
                                    }
                                } else {
                                    /** 진도점수 셋팅 */
                                    if(info.getProgressScore() != null) entity.setProgressScore(info.getProgressScore());
                                    /** 시험점수 셋팅 */
                                    if(info.getExamScore() != null) entity.setExamScore(info.getExamScore());
                                    /** 수료 여부 셋팅 */
                                    entity.setIsComplete(info.getIsComplete());
                                    /** 수료 일자 초기화 */
                                    entity.setCompleteDateTime(null);
                                    /** 교육 진행 중 상태로 변경 */
                                    entity.setEducationState(Code.EDU_STATE.PROCEEDING.enumCode);
                                    /** 연수 이수 번호 초기화 */
                                    entity.setProgramCompleteNumber(null);
                                }
                                BeanUtils.copyProperties(curriculumApplicationMasterRepository.saveAndFlush(entity), educationApplicationApiResponseVO);
                                return educationApplicationApiResponseVO;

                            }).collect(Collectors.toList()));
                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3026, "교육 과정 수료 처리에 실패."));
    }

    /**
     * 시험 응시 내역 초기화
     *
     * @param educationApplicationApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public EducationApplicationApiResponseVO updateExamReset(EducationApplicationApiRequestVO educationApplicationApiRequestVO) {
        return Optional.ofNullable(educationApplicationApiRequestVO)
                .filter(object -> educationApplicationApiRequestVO.getApplicationList().size() > 0)
                .map(object -> {
                    EducationApplicationApiResponseVO result = EducationApplicationApiResponseVO.builder().build();
                    result.setApplicationList(object.getApplicationList().stream()
                            .map(info -> {
                                EducationApplicationApiResponseVO educationApplicationApiResponseVO = EducationApplicationApiResponseVO.builder().build();
                                /** 교육 신청 내역 조회 */
                                CurriculumApplicationMaster entity = curriculumApplicationMasterRepository.findOne(Example.of(CurriculumApplicationMaster.builder()
                                        .applicationNo(info.getApplicationNo())
                                        .educationPlanCode(info.getEducationPlanCode())
                                        .curriculumCode(info.getCurriculumCode())
                                        .userId(info.getUserId())
                                        .build())).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3025, "해당 교육 과정 신청 이력 미존재."));
                                /** 연결된 시험 정보 초기화 */
                                educationApplication(entity, true);
                                BeanUtils.copyProperties(curriculumApplicationMasterRepository.saveAndFlush(entity), educationApplicationApiResponseVO);
                                return educationApplicationApiResponseVO;

                            }).collect(Collectors.toList()));
                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3028, "시험 응시 내역 초기화 실패."));
    }

    /**
     * 수강 교육 콘텐츠 완료 / 미완료 처리
     *
     * @param educationApplicationApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public EducationApplicationApiResponseVO updateContentsComplete(EducationApplicationApiRequestVO educationApplicationApiRequestVO) {
        return Optional.ofNullable(educationApplicationApiRequestVO)
                .map(object -> {
                    EducationApplicationApiResponseVO result = EducationApplicationApiResponseVO.builder().build();
                    /** 수강 교육 콘텐츠 조회 */
                    CurriculumApplicationContents entity = curriculumApplicationContentsRepository.findOne(Example.of(CurriculumApplicationContents.builder()
                            .applicationNo(educationApplicationApiRequestVO.getApplicationNo())
                            .curriculumCode(educationApplicationApiRequestVO.getCurriculumCode())
                            .contentsCode(Optional.ofNullable(educationApplicationApiRequestVO.getContentsCode()).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9001, "콘텐츠 코드 누락.")))
                            .userId(educationApplicationApiRequestVO.getUserId())
                            .build())).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3025, "수강 교육 과정 콘텐츠 챕터 미존재."));

                    /** 교육 과정 유형 조회 */
                    String educationType = curriculumMasterRepository.findOne(Example.of(CurriculumMaster.builder().curriculumCode(educationApplicationApiRequestVO.getCurriculumCode()).build()))
                            .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3004, "존재하지 않는 교육과정")).getEducationType();

                    if(Code.EDU_TYPE.E_LEARNING.equals(Code.EDU_TYPE.enumOfCode(educationType))) {
                        if(entity.getContentsApplicationChapterList() != null && entity.getContentsApplicationChapterList().size() > 0) {
                            throw new KPFException(KPF_RESULT.ERROR3027, "수강 대상 챕터 정보가 존재하는 콘텐츠는 진도 처리 불가.");
                        }
                    }

                    if(educationApplicationApiRequestVO.getIsComplete().equals("Y")) {
                        entity.setIsComplete(true);
                        /** 해당 콘텐츠 참여점수를 만점 점수 및 출석으로 업데이트 */
                        entity.setAttendScore(entity.getProgressScore());
                        entity.setCompleteDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    } else {
                        entity.setIsComplete(false);
                        /** 해당 콘텐츠 참여 점수 초기화 */
                        entity.setAttendScore(0);
                        entity.setCompleteDateTime(null);
                    }

                    BeanUtils.copyProperties(curriculumApplicationContentsRepository.saveAndFlush(entity), result);
                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3024, "수강 교육 과정 콘텐츠 정보 변경 실패."));
    }

    /**
     * 수강 교육 챕터 완료 / 미완료 처리
     *
     * @param educationApplicationApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public EducationApplicationApiResponseVO updateChapterComplete(EducationApplicationApiRequestVO educationApplicationApiRequestVO) {
        return Optional.ofNullable(educationApplicationApiRequestVO)
                .map(object -> {
                    EducationApplicationApiResponseVO result = EducationApplicationApiResponseVO.builder().build();
                    /** 수강 교육 콘텐츠의 챕터 조회 */
                    ContentsApplicationChapter entity = contentsApplicationChapterRepository.findOne(Example.of(ContentsApplicationChapter.builder()
                            .applicationNo(educationApplicationApiRequestVO.getApplicationNo())
                            .curriculumCode(educationApplicationApiRequestVO.getCurriculumCode())
                            .contentsCode(Optional.ofNullable(educationApplicationApiRequestVO.getContentsCode()).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9001, "콘텐츠 코드 누락.")))
                            .chapterCode(Optional.ofNullable(educationApplicationApiRequestVO.getChapterCode()).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9001, "챕터 코드 누락.")))
                            .userId(educationApplicationApiRequestVO.getUserId())
                            .build())).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3025, "수강 교육 과정 콘텐츠 챕터 미존재."));

                    if(educationApplicationApiRequestVO.getIsComplete().equals("Y")) {
                        entity.setIsComplete(true);
                        entity.setCompleteDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    } else {
                        entity.setIsComplete(false);
                    }
                    BeanUtils.copyProperties(contentsApplicationChapterRepository.saveAndFlush(entity), result);
                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3024, "수강 교육 과정 콘텐츠 챕터 정보 변경 실패."));
    }

    /**
     * 수강 교육 절 완료 / 미완료 처리
     *
     * @param educationStateApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport submitSectionProgress(List<EducationStateApiRequestVO> educationStateApiRequestVO) {
        for (EducationStateApiRequestVO dto : educationStateApiRequestVO) {
            ChapterApplicationSection chapterApplicationSection = chapterApplicationSectionRepository.findOne(Example.of(ChapterApplicationSection.builder()
                    .applicationNo(dto.getApplicationNo())
                    .chapterCode(dto.getChapterCode())
                    .sectionCode(dto.getSectionCode())
                    .build())).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3025, "존재하지 않는 교육 신청 절 정보"));
            /** 절 진도율 */
            chapterApplicationSection.setProgressRate(dto.getSectionProgressRate());

            if(org.apache.commons.lang3.StringUtils.isEmpty(chapterApplicationSection.getBeginDateTime())) {
                /** 절 시작 일시 */
                chapterApplicationSection.setBeginDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            }

            if(dto.getSectionProgressRate().equals(Double.parseDouble("100"))) {
                /** 절 완료 처리 */
                chapterApplicationSection.setIsComplete(true);
                /** 절 완료 일시 */
                chapterApplicationSection.setCompleteDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            }

            chapterApplicationSectionRepository.saveAndFlush(chapterApplicationSection);

            /** 교육 과정 중 콘텐츠 정보 관련 업데이트 */
            curriculumApplicationMasterRepository.findOne(Example.of(CurriculumApplicationMaster.builder()
                    .applicationNo(dto.getApplicationNo())
                    .build())).ifPresentOrElse(curriculumApplicationMaster -> {
                Map<String, String> progressTotalMap = curriculumApplicationMaster.getCurriculumApplicationContentsList().stream()
                        .map(curriculumApplicationContents -> {
                            Map<String, Long> sectionTotalMap = curriculumApplicationContents.getContentsApplicationChapterList().stream()
                                    .map(contentsApplicationChapter -> {
                                        /** 차시의 시작 일자가 비어있을 경우 최초 진입으로 판단하여 시작일시 셋팅 */
                                        if(org.apache.commons.lang3.StringUtils.isEmpty(contentsApplicationChapter.getChapterBeginDateTime())
                                                && contentsApplicationChapter.getChapterCode().equals(dto.getChapterCode())) {
                                            contentsApplicationChapter.setChapterBeginDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                            contentsApplicationChapterRepository.saveAndFlush(contentsApplicationChapter);
                                        }
                                        /** 차시의 절 갯수 */
                                        long sectionCount = contentsApplicationChapter.getChapterApplicationSectionList().size();
                                        /** 차시의 완료된 절 갯수 */
                                        long completeSectionCount = contentsApplicationChapter.getChapterApplicationSectionList().stream().filter(ChapterApplicationSection::getIsComplete).count();
                                        /** 차시의 절을 모두 완료하였을 경우 차시 완료 처리 */
                                        if (!contentsApplicationChapter.getIsComplete() && (sectionCount == completeSectionCount)) {
                                            contentsApplicationChapter.setIsComplete(true);
                                            contentsApplicationChapter.setCompleteDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                            contentsApplicationChapterRepository.saveAndFlush(contentsApplicationChapter);
                                        }
                                        Map<String, Long> sectionMap = new HashMap<>();
                                        sectionMap.put("sectionCount", sectionCount);
                                        sectionMap.put("completeSectionCount", completeSectionCount);
                                        return sectionMap;
                                    }).collect(Collectors.toList()).stream()
                                    .reduce((before, current) -> {
                                        long sectionCount = before.get("sectionCount") + current.get("sectionCount");
                                        long completeSectionCount = before.get("completeSectionCount") + current.get("completeSectionCount");
                                        Map<String, Long> sectionMap = new HashMap<>();
                                        sectionMap.put("sectionCount", sectionCount);
                                        sectionMap.put("completeSectionCount", completeSectionCount);
                                        return sectionMap;
                                    }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3024, "수강 점수 계산 오류"));

                            Long progressScore =  Math.round(100 / Double.parseDouble(sectionTotalMap.get("sectionCount").toString()) *  sectionTotalMap.get("completeSectionCount"));
                            Double progressRate =  Double.parseDouble(sectionTotalMap.get("completeSectionCount").toString()) / Double.parseDouble(sectionTotalMap.get("sectionCount").toString()) * 100;

                            curriculumApplicationContents.setProgressScore(Math.toIntExact(progressScore));
                            if(progressScore == 100) {
                                curriculumApplicationContents.setIsComplete(true);
                            }
                            curriculumApplicationContentsRepository.saveAndFlush(curriculumApplicationContents);

                            Map<String, String> progressMap = new HashMap<>();
                            progressMap.put("progressScore", progressScore.toString());
                            progressMap.put("progressRate", progressRate.toString());
                            return progressMap;
                        }).findFirst().orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3024, "수강 점수 계산 오류"));

                curriculumApplicationMaster.setProgressRate(Double.valueOf(progressTotalMap.get("progressRate")));
                curriculumApplicationMaster.setProgressScore(Integer.valueOf(progressTotalMap.get("progressScore")));

                /** 교육 수료 조건 여부 확인 및 수료 처리 */
                isComplete(curriculumApplicationMaster);
                curriculumApplicationMasterRepository.saveAndFlush(curriculumApplicationMaster);
            }, () -> {
                throw new KPFException(KPF_RESULT.ERROR3025, "존재하지 않는 교육 과정 신청 정보");
            });
        }

        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     * 화상 & 집합 교육 강의 완료 / 미완료 처리
     *
     * @param educationApplicationApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public EducationApplicationApiResponseVO updateLectureComplete(EducationApplicationApiRequestVO educationApplicationApiRequestVO) {
        EducationApplicationApiResponseVO response = Optional.ofNullable(educationApplicationApiRequestVO)
            .filter(object -> educationApplicationApiRequestVO.getApplicationList().size() > 0)
            .map(object -> {
                EducationApplicationApiResponseVO result = EducationApplicationApiResponseVO.builder().build();
                result.setApplicationList(object.getApplicationList().stream()
                    .map(info -> {
                        EducationApplicationApiResponseVO educationApplicationApiResponseVO = EducationApplicationApiResponseVO.builder().build();
                        /** 수강 중인 강의 정보 획득 */
                        curriculumApplicationLectureRepository.findOne(Example.of(CurriculumApplicationLecture.builder()
                                .lectureCode(info.getLectureCode())
                                .userId(info.getUserId())
                                .build())).ifPresent(curriculumApplicationLecture -> {
                            curriculumApplicationLecture.setIsAttend(info.getIsAttend());
                            BeanUtils.copyProperties(curriculumApplicationLectureRepository.saveAndFlush(curriculumApplicationLecture), educationApplicationApiResponseVO);
                        });
                        return educationApplicationApiResponseVO;
                    }).collect(Collectors.toList()));

                return result;
            }).orElseThrow(() -> new RuntimeException("강의 수강 상태 변경에 실패하였습니다."));

        Optional.of(educationApplicationApiRequestVO)
            .filter(object -> educationApplicationApiRequestVO.getApplicationList().size() > 0)
            .ifPresent(object -> {
                object.getApplicationList().stream().map(subData -> {
                    Map<String, String> filterMap = new HashMap<>();

                    filterMap.put("applicationNo", subData.getApplicationNo());
                    filterMap.put("userId", subData.getUserId());

                    return filterMap;
                }).distinct().forEach(filter -> {

                    curriculumApplicationMasterRepository.findOne(Example.of(CurriculumApplicationMaster.builder()
                            .applicationNo(filter.get("applicationNo"))
                            .userId(filter.get("userId"))
                            .build())).ifPresent(curriculumApplicationMaster -> {
                        List<CurriculumApplicationLecture> curriculumApplicationLectureList = curriculumApplicationLectureRepository.findAll(Example.of(CurriculumApplicationLecture.builder()
                                .applicationNo(curriculumApplicationMaster.getApplicationNo())
                                .userId(curriculumApplicationMaster.getUserId())
                                .build()));

                        long totalLectureCount = curriculumApplicationLectureList.size();
                        long attendLectureCount = curriculumApplicationLectureList.stream().filter(CurriculumApplicationLecture::getIsAttend).count();

                        curriculumApplicationMaster.setProgressScore(
                                (int) Math.round(100 / Double.parseDouble(Long.toString(totalLectureCount)) * Double.parseDouble(Long.toString(attendLectureCount))));
                        curriculumApplicationMaster.setProgressRate(
                                Double.parseDouble(Long.toString(attendLectureCount)) / Double.parseDouble(Long.toString(totalLectureCount)) * 100);

                        /** 교육 수료 조건 여부 확인 및 수료 처리 */
                        isComplete(curriculumApplicationMaster);
                        curriculumApplicationMasterRepository.saveAndFlush(curriculumApplicationMaster);
                    });
                });
            });

        return response;
    }

    /**
     * 교육 수료 조건 여부 확인 및 수료 처리(교육 완료 처리)
     *
     * @param curriculumApplicationMaster
     */
    @Transactional(rollbackOn = {Exception.class})
    void isComplete(CurriculumApplicationMaster curriculumApplicationMaster) {
        if(!curriculumApplicationMaster.getIsComplete().equals("Y")) { /** 수료 처리중, 미수료 상태일 경우 */
            Double progressScoreOfEnd = curriculumApplicationMaster.getEducationPlan().getCurriculumMaster().getProgressScoreOfEnd();
            Double examScoreOfEnd = curriculumApplicationMaster.getEducationPlan().getCurriculumMaster().getExamScoreOfEnd();

            if (Double.parseDouble(curriculumApplicationMaster.getProgressScore().toString()) >= progressScoreOfEnd &&
                    Double.parseDouble(curriculumApplicationMaster.getExamScore().toString()) >= examScoreOfEnd) {
                curriculumApplicationMaster.setIsComplete("Y");
                curriculumApplicationMaster.setCompleteDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                curriculumApplicationMaster.setEducationState(Code.EDU_STATE.END.enumCode);
                curriculumApplicationMasterRepository.saveAndFlush(curriculumApplicationMaster);
            }
        }
    }

    /**
     엑셀 다운로드
     */
    public <T> List<EducationApplicationExcelVO> getExcelApply(EducationApplicationViewRequestVO requestObject) {
        requestObject.setIsApply(true);
        List<EducationApplicationExcelVO> educationApplicationExcelVOList = (List<EducationApplicationExcelVO>) commonEducationRepository.excelDownload(requestObject);
        return educationApplicationExcelVOList;
    }

    /**
     엑셀 다운로드
     */
    public <T> List<EducationCompleteExcelVO> getExcelComplete(EducationApplicationViewRequestVO requestObject) {
        requestObject.setIsApply(false);
        requestObject.setAdminApprovalState("2");
        List<EducationCompleteExcelVO> educationCompleteExcelVOList = (List<EducationCompleteExcelVO>) commonEducationRepository.excelDownload(requestObject);
        return educationCompleteExcelVOList;
    }


    /**
     * 언론인교육센터 목록
      */
    public <T> Page<T> getJournalismschoolList(JournalismschoolApiRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonEducationRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));

    }
    /**
     * 언론인교육센터 목록(페이지)
     */
    public <T> Page<T> getJournalismschoolList(JournalismschoolViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonEducationRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));

    }

}
