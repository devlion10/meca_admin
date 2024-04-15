package kr.or.kpf.lms.biz.education.schedule.service;

import kr.or.kpf.lms.biz.business.instructor.clcln.ddln.vo.response.BizInstructorClclnDdlnApiResponseVO;
import kr.or.kpf.lms.biz.education.schedule.vo.LectureInfo;
import kr.or.kpf.lms.biz.education.schedule.vo.request.ScheduleApiRequestVO;
import kr.or.kpf.lms.biz.education.schedule.vo.request.ScheduleViewRequestVO;
import kr.or.kpf.lms.biz.education.schedule.vo.response.ScheduleApiResponseVO;
import kr.or.kpf.lms.biz.education.schedule.vo.response.ScheduleExcelVO;
import kr.or.kpf.lms.biz.user.webuser.service.WebUserService;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.common.support.Code;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonEducationRepository;
import kr.or.kpf.lms.repository.education.*;
import kr.or.kpf.lms.repository.entity.BizSurveyQitemItem;
import kr.or.kpf.lms.repository.entity.education.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 교육 관리 > 교육 개설 API 관련 Service
 */
@Service
@RequiredArgsConstructor
public class ScheduleService extends CSServiceSupport {
    /** 교육 과정 일정 PREFIX */
    private static final String PREFIX_EDU_PLAN = "PLAN";
    /** 일반 교육 강의 PREFIX */
    private static final String PREFIX_LECTURE = "LEC";
    /** 교육 일정 썸네일 이미지 태그 */
    private static final String THUMB_IMG_TAG = "_THUMB";
    /** 출석 QR 이미지 태그 */
    private static final String QR_IMG_TAG = "_QRCODE";
    /** 관련 첨부파일 태그 */
    private static final String ATCH_FILE_TAG = "_EDU_FILE";

    /** 교육 관리 공통 */
    private final CommonEducationRepository commonEducationRepository;
    /** 교육 일정 관리 */
    private final EducationPlanRepository educationPlanRepository;
    /** 교육 과정 관리 */
    private final CurriculumMasterRepository curriculumMasterRepository;
    /** 일반 교육 강의 */
    private final LectureMasterRepository lectureMasterRepository;
    /** 일반 교육 강의 강사 목록 */
    private final LectureLecturerRepository lectureLecturerRepository;
    /** 교육 신청 현황 */
    private final CurriculumApplicationMasterRepository curriculumApplicationMasterRepository;

    private final WebUserService webUserService;

    /** 상시 운영 */
    private static final String ALWAYS_OPER = "2";
    /** 기수 운영 */
    private static final String OTHER_OPER = "1";

    /**
     * 교육 일정 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(ScheduleViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonEducationRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 교육 일정 생성
     *  
     * @param scheduleApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public ScheduleApiResponseVO createEducationSchedule(ScheduleApiRequestVO scheduleApiRequestVO) {
        EducationPlan entity = EducationPlan.builder().build();
        BeanUtils.copyProperties(scheduleApiRequestVO, entity);

        if (educationPlanRepository.findOne(Example.of(EducationPlan.builder()
                        .curriculumCode(scheduleApiRequestVO.getCurriculumCode())
                        .operationType(scheduleApiRequestVO.getOperationType())
                        .yearOfEducationPlan(scheduleApiRequestVO.getYearOfEducationPlan())
                        .yearOfEducationPlanStep(scheduleApiRequestVO.getYearOfEducationPlanStep())
                        .build()))
                .isPresent()) {
            throw new KPFException(KPF_RESULT.ERROR3062, "동일 교육 일정 존재");
        } else {
            ScheduleApiResponseVO result = ScheduleApiResponseVO.builder().build();

            curriculumMasterRepository.findOne(Example.of(CurriculumMaster.builder().curriculumCode(scheduleApiRequestVO.getCurriculumCode()).build()))
                    .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3004, "교육과정 미존재."));

            /** 복합 데이터 Validation 체크 */
            if(OTHER_OPER.equals(scheduleApiRequestVO.getOperationType()) && scheduleApiRequestVO.getAlwaysEducationTerm() != null) {
                throw new KPFException(KPF_RESULT.ERROR9004, "기수 운영일 경우 상시 운영 관련 데이터 셋팅 할 수 없음.");
            }
            if(ALWAYS_OPER.equals(scheduleApiRequestVO.getOperationType()) && scheduleApiRequestVO.getYearOfEducationPlanStep() != null) {
                throw new KPFException(KPF_RESULT.ERROR9004, "상시 운영일 경우 기수 운영 관련 데이터 셋팅 할 수 없음.");
            }
            if(scheduleApiRequestVO.getIsReview() != null && scheduleApiRequestVO.getIsReview() && scheduleApiRequestVO.getAvailableReviewTerm() == null) {
                throw new KPFException(KPF_RESULT.ERROR9004, "복습 제공을 할 경우 복습 기간 필요.");
            }
            if(scheduleApiRequestVO.getIsVideoLecture() != null && scheduleApiRequestVO.getIsVideoLecture() && StringUtils.isEmpty(scheduleApiRequestVO.getVideoLecturePath())) {
                throw new KPFException(KPF_RESULT.ERROR9004, "병행/화상 강의일 경우 화상 강의 경로 필요.");
            }
            /** 교육 신청 일련 번호 획득 */
            String educationPlanCode = commonEducationRepository.generateEducationPlanCode(PREFIX_EDU_PLAN);
            entity.setEducationPlanCode(educationPlanCode);
            /** 이러닝 교육이 아닌 교육일 경우... 강의 내용 필요... */
            if(!Code.EDU_TYPE.E_LEARNING.equals(Code.EDU_TYPE.enumOfCode(scheduleApiRequestVO.getEducationType()))) {
                if(scheduleApiRequestVO.getLectureInfoList().size() <= 0 || scheduleApiRequestVO.getLectureInfoList() == null) {
                    throw new KPFException(KPF_RESULT.ERROR9004, "강의 내용이 누락.");
                } else {
                    List<LectureMaster> lectureMasterList = scheduleApiRequestVO.getLectureInfoList().stream()
                        .map(data -> {
                            LectureMaster lectureMaster = LectureMaster.builder().build();
                            BeanUtils.copyProperties(data, lectureMaster);
                            lectureMaster.setEducationPlanCode(educationPlanCode);
                            String lectureCode = commonEducationRepository.generateLectureCode(PREFIX_LECTURE);
                            lectureMaster.setLectureCode(lectureCode);
                            lectureMaster.setAttendQRCodePath(appConfig.getWebDomain() + appConfig.getQrCodePath() + "/" + educationPlanCode + "/" + lectureCode);

                            List<LectureLecturer> lectureLecturerList = data.getLectureLecturerList().stream()
                                    .peek(subData -> {
                                        LectureLecturer lectureLecturer = LectureLecturer.builder().build();
                                        BeanUtils.copyProperties(subData, lectureLecturer);
                                        lectureLecturer.setEduPlanCode(educationPlanCode);
                                        lectureLecturer.setLectureCode(lectureCode);
                                        lectureLecturerRepository.saveAndFlush(lectureLecturer);
                                    }).collect(Collectors.toList());

                            return lectureMasterRepository.saveAndFlush(lectureMaster);
                        }).collect(Collectors.toList());

                    lectureMasterList.sort(Comparator.comparing(LectureMaster::getOperationBeginDateTime));

                    /** 교육 운영 타입 기수로 고정 */
                    entity.setOperationType(Code.OPER_TYPE.CARDINAL.enumCode);

                    /** 전체 교육 일정 기간 설정 */
                    entity.setOperationBeginDateTime(lectureMasterList.get(0).getOperationBeginDateTime());
                    entity.setOperationEndDateTime(lectureMasterList.get(lectureMasterList.size() - 1).getOperationEndDateTime());

                    /** 지점 코드 검증 및 셋팅 */
                    entity.setProvince(Code.PROVINCE_CD.enumOfCode(scheduleApiRequestVO.getProvince()).enumCode);

                    /** 복습 가능 여부 */
                    if(scheduleApiRequestVO.getIsReview()) {
                        if(scheduleApiRequestVO.getAvailableReviewTerm() == null) {
                            throw new KPFException(KPF_RESULT.ERROR3066, "복습 가능 기간 누락");
                        }
                    }
                    /** 화상 강의 진행 여부 */
                    if(scheduleApiRequestVO.getEducationType().equals(Code.EDU_TYPE.LECTURE.enumCode)) {
                        if(scheduleApiRequestVO.getVideoLecturePath() == null) {
                            throw new KPFException(KPF_RESULT.ERROR3067, "화상 강의 경로 누락");
                        }
                    }
                }
            }

            BeanUtils.copyProperties(educationPlanRepository.saveAndFlush(entity), result);
            return result;
        }
    }

    /**
     * 교육 일정 업데이트
     *
     * @param scheduleApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public ScheduleApiResponseVO updateEducationSchedule(ScheduleApiRequestVO scheduleApiRequestVO) {
        return educationPlanRepository.findOne(Example.of(EducationPlan.builder()
                        .educationPlanCode(scheduleApiRequestVO.getEducationPlanCode())
                        .build()))
                .map(educationPlan -> {
                    /** 복합 데이터 Validation 체크 */
                    if(OTHER_OPER.equals(scheduleApiRequestVO.getOperationType()) && scheduleApiRequestVO.getAlwaysEducationTerm() != null) {
                        throw new KPFException(KPF_RESULT.ERROR9004, "기수 운영일 경우 상시 운영 관련 데이터 셋팅 할 수 없음.");
                    }
                    if(ALWAYS_OPER.equals(scheduleApiRequestVO.getOperationType()) && scheduleApiRequestVO.getYearOfEducationPlanStep() != null) {
                        throw new KPFException(KPF_RESULT.ERROR9004, "상시 운영일 경우 기수 운영 관련 데이터 셋팅 할 수 없음.");
                    }
                    if(scheduleApiRequestVO.getIsReview() != null && scheduleApiRequestVO.getIsReview() && scheduleApiRequestVO.getAvailableReviewTerm() == null) {
                        throw new KPFException(KPF_RESULT.ERROR9004, "복습 제공을 할 경우 복습 기간 필요.");
                    }
                    if(scheduleApiRequestVO.getIsVideoLecture() != null && scheduleApiRequestVO.getIsVideoLecture() && StringUtils.isEmpty(scheduleApiRequestVO.getVideoLecturePath())) {
                        throw new KPFException(KPF_RESULT.ERROR9004, "병행/화상 강의일 경우 화상 강의 경로 필요.");
                    }

                    /** 이러닝 교육이 아닌 교육일 경우... 강의 내용 필요... */
                    if(!Code.EDU_TYPE.E_LEARNING.equals(Code.EDU_TYPE.enumOfCode(scheduleApiRequestVO.getEducationType()))) {
                        if(scheduleApiRequestVO.getLectureInfoList().size() <= 0 || scheduleApiRequestVO.getLectureInfoList() == null) {
                            throw new KPFException(KPF_RESULT.ERROR9004, "강의 내용이 누락.");
                        } else {

                            String thumbnailFilePath = educationPlan.getThumbnailFilePath();
                            /** 전체 교육 일정 기간 설정 */
                            String originalOperationBeginDateTime = educationPlan.getOperationBeginDateTime();
                            String originalOperationEndDateTime = educationPlan.getOperationEndDateTime();

                            /** 이수 번호를 위한 프로그램 코드 있는 경우, 해당 내용 수정 시에도 그대로 반영 */
                            String programNumber = scheduleApiRequestVO.getProgramNumber();

                            EducationPlan imsiEducationPlan = EducationPlan.builder().build();
                            /** 불필요 데이터 초기화 */
                            BeanUtils.copyProperties(imsiEducationPlan, educationPlan);
                            /** 요청 데이터로 다시 생성 */
                            copyNonNullObject(scheduleApiRequestVO, educationPlan);

                            /** 이수 번호를 위한 프로그램 코드 있는 경우, 해당 내용 수정 시에도 그대로 반영 */
                            educationPlan.setProgramNumber(programNumber);
                            /** 기존 썸네일 파일 경로 재설정 */
                            educationPlan.setThumbnailFilePath(thumbnailFilePath);
                            /** 교육 운영 타입 기수로 고정 */
                            educationPlan.setOperationType(Code.OPER_TYPE.CARDINAL.enumCode);
                            /** 지점 코드 검증 및 셋팅 */
                            educationPlan.setProvince(Code.PROVINCE_CD.enumOfCode(scheduleApiRequestVO.getProvince()).enumCode);

                            /** 전체 교육 일정 기간 설정 */
                            String newOriginalOperationBeginDateTime = scheduleApiRequestVO.getLectureInfoList().get(0).getOperationBeginDateTime();
                            String newOriginalOperationEndDateTime = scheduleApiRequestVO.getLectureInfoList().get(scheduleApiRequestVO.getLectureInfoList().size() - 1).getOperationEndDateTime();
                            educationPlan.setOperationBeginDateTime(newOriginalOperationBeginDateTime);
                            educationPlan.setOperationEndDateTime(newOriginalOperationEndDateTime);

                            /** 복습 가능 여부 */
                            if(scheduleApiRequestVO.getIsReview()) {
                                if(scheduleApiRequestVO.getAvailableReviewTerm() == null) {
                                    throw new KPFException(KPF_RESULT.ERROR3066, "복습 가능 기간 누락");
                                }
                            }
                            /** 화상 강의 진행 여부 */
                            if(scheduleApiRequestVO.getIsVideoLecture()) {
                                if(scheduleApiRequestVO.getVideoLecturePath() == null) {
                                    throw new KPFException(KPF_RESULT.ERROR3067, "화상 강의 경로 누락");
                                }
                            }

                            lectureLecturerRepository.deleteAll(lectureLecturerRepository.findAll(Example.of(LectureLecturer.builder()
                                    .eduPlanCode(educationPlan.getEducationPlanCode())
                                    .build())));
                            List<LectureMaster> lectureMasterList = scheduleApiRequestVO.getLectureInfoList().stream()
                                .map(requestLecture -> {
                                    if (requestLecture.getLectureCode() != null) {
                                        LectureMaster lectureItem = lectureMasterRepository.findById(requestLecture.getLectureCode())
                                                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3064, "차시 정보가 없습니다."));
                                        BeanUtils.copyProperties(requestLecture, lectureItem);

                                        List<LectureLecturer> lectureLecturerList = requestLecture.getLectureLecturerList().stream()
                                            .peek(subData -> {
                                                LectureLecturer lectureLecturer = LectureLecturer.builder().build();
                                                BeanUtils.copyProperties(subData, lectureLecturer);
                                                lectureLecturer.setEduPlanCode(educationPlan.getEducationPlanCode());
                                                BeanUtils.copyProperties(lectureLecturerRepository.saveAndFlush(lectureLecturer), subData);
                                            }).collect(Collectors.toList());
                                        return lectureMasterRepository.saveAndFlush(lectureItem);
                                    } else {
                                        LectureMaster lectureMaster = LectureMaster.builder().build();
                                        BeanUtils.copyProperties(requestLecture, lectureMaster);
                                        lectureMaster.setEducationPlanCode(educationPlan.getEducationPlanCode());
                                        String lectureCode = commonEducationRepository.generateLectureCode(PREFIX_LECTURE);
                                        lectureMaster.setLectureCode(lectureCode);
                                        lectureMaster.setAttendQRCodePath(appConfig.getWebDomain() + appConfig.getQrCodePath() + "/" + educationPlan.getEducationPlanCode() + "/" + lectureCode);

                                        List<LectureLecturer> lectureLecturerList = requestLecture.getLectureLecturerList().stream()
                                                .peek(subData -> {
                                                    LectureLecturer lectureLecturer = LectureLecturer.builder().build();
                                                    BeanUtils.copyProperties(subData, lectureLecturer);
                                                    lectureLecturer.setEduPlanCode(educationPlan.getEducationPlanCode());
                                                    lectureLecturer.setLectureCode(lectureCode);
                                                    BeanUtils.copyProperties(lectureLecturerRepository.saveAndFlush(lectureLecturer), subData);
                                                }).collect(Collectors.toList());
                                        return lectureMasterRepository.saveAndFlush(lectureMaster);
                                    }
                                }).collect(Collectors.toList());

                            List<CurriculumApplicationMaster> applicationList = curriculumApplicationMasterRepository.findAll(Example.of(CurriculumApplicationMaster.builder()
                                    .educationPlanCode(scheduleApiRequestVO.getEducationPlanCode())
                                    .build())).stream().peek(application -> {
                                        application.setOperationBeginDateTime(newOriginalOperationBeginDateTime);
                                        application.setOperationEndDateTime(newOriginalOperationEndDateTime);
                                        curriculumApplicationMasterRepository.saveAndFlush(application);
                                    }).collect(Collectors.toList());
                        }
                    } else {    /** 이러닝 교육 */
                        copyNonNullObject(scheduleApiRequestVO, educationPlan);
                    }

                    ScheduleApiResponseVO result = ScheduleApiResponseVO.builder().build();
                    BeanUtils.copyProperties(educationPlanRepository.saveAndFlush(educationPlan), result);

                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3004, "교육 과정 정보 미존재."));
    }

    /**
     * 교육 일정 썸네일 파일 업로드
     *
     * @param educationPlanCode
     * @param thumbnailFile
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport thumbnailFileUpload(String educationPlanCode, MultipartFile thumbnailFile) {
        /** 교육 일정 존재 여부 확인 */
        EducationPlan educationPlan = educationPlanRepository.findOne(Example.of(EducationPlan.builder()
                        .educationPlanCode(educationPlanCode)
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3064, "해당 교육 일정 미존재"));
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
                                    .append(educationPlanCode + THUMB_IMG_TAG + imageSequence + "." + StringUtils.substringAfter(file.getOriginalFilename(), ".")).toString();
                            file.transferTo(new File(thumbnailFilePath));
                            educationPlan.setThumbnailFilePath(thumbnailFilePath.replace(appConfig.getUploadFile().getUploadContextPath(), ""));
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
     엑셀 다운로드
     */
    public <T> List<ScheduleExcelVO> getExcel(ScheduleViewRequestVO requestObject) {
        List<ScheduleExcelVO> scheduleExcelVOList = (List<ScheduleExcelVO>) commonEducationRepository.excelDownload(requestObject);
        return scheduleExcelVOList;
    }

    /**
     * 교육 일정 업데이트
     *
     * @param educationPlanCode
     * @return
     */
    public ScheduleApiResponseVO thumbnailFileDelete(String educationPlanCode) {
        return educationPlanRepository.findOne(Example.of(EducationPlan.builder()
                        .educationPlanCode(educationPlanCode)
                        .build()))
                .map(educationPlan -> {
                    educationPlan.setThumbnailFilePath(null);
                    ScheduleApiResponseVO result = ScheduleApiResponseVO.builder().build();
                    BeanUtils.copyProperties(educationPlanRepository.saveAndFlush(educationPlan), result);

                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3004, "교육 과정 정보 미존재."));
    }
}
