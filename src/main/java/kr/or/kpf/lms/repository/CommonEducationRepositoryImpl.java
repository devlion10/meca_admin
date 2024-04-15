package kr.or.kpf.lms.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.request.JournalismschoolApiRequestVO;
import kr.or.kpf.lms.biz.education.application.vo.request.EducationApplicationViewRequestVO;
import kr.or.kpf.lms.biz.education.application.vo.response.EducationApplicationExcelVO;
import kr.or.kpf.lms.biz.education.application.vo.response.EducationCompleteExcelVO;
import kr.or.kpf.lms.biz.education.curriculum.vo.CurriculumExcelVO;
import kr.or.kpf.lms.biz.education.curriculum.vo.request.CurriculumViewRequestVO;
import kr.or.kpf.lms.biz.education.exam.vo.request.ExamViewRequestVO;
import kr.or.kpf.lms.biz.education.lecture.vo.request.LectureViewRequestVO;
import kr.or.kpf.lms.biz.education.question.vo.request.ExamQuestionViewRequestVO;
import kr.or.kpf.lms.biz.education.reference.vo.request.ReferenceRoomViewRequestVO;
import kr.or.kpf.lms.biz.education.reference.vo.response.ReferenceRoomExcelVO;
import kr.or.kpf.lms.biz.education.schedule.vo.request.ScheduleViewRequestVO;
import kr.or.kpf.lms.biz.education.schedule.vo.response.ScheduleExcelVO;
import kr.or.kpf.lms.biz.user.history.vo.JournalismschoolViewRequestVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSRepositorySupport;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import kr.or.kpf.lms.common.support.Code;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.ResponseSummary;
import kr.or.kpf.lms.repository.contents.ContentsApplicationChapterRepository;
import kr.or.kpf.lms.repository.education.CurriculumApplicationMasterRepository;
import kr.or.kpf.lms.repository.entity.FileMaster;
import kr.or.kpf.lms.repository.entity.contents.ContentsApplicationChapter;
import kr.or.kpf.lms.repository.entity.education.CurriculumApplicationMaster;
import kr.or.kpf.lms.repository.entity.education.EducationPlan;
import kr.or.kpf.lms.repository.entity.user.AdminUser;
import kr.or.kpf.lms.repository.entity.user.LmsUser;
import kr.or.kpf.lms.repository.entity.user.OrganizationInfo;
import kr.or.kpf.lms.repository.entity.user.OrganizationInfoMedia;
import kr.or.kpf.lms.repository.user.AdminUserRepository;
import kr.or.kpf.lms.repository.user.OrganizationInfoRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static kr.or.kpf.lms.repository.entity.QBizPbancMaster.bizPbancMaster;
import static kr.or.kpf.lms.repository.entity.QformeEdu2020History.formeEdu2020History;
import static kr.or.kpf.lms.repository.entity.education.QCurriculumApplicationMaster.curriculumApplicationMaster;
import static kr.or.kpf.lms.repository.entity.education.QCurriculumExam.curriculumExam;
import static kr.or.kpf.lms.repository.entity.education.QCurriculumMaster.curriculumMaster;
import static kr.or.kpf.lms.repository.entity.education.QCurriculumQuestion.curriculumQuestion;
import static kr.or.kpf.lms.repository.entity.education.QCurriculumReferenceRoom.curriculumReferenceRoom;
import static kr.or.kpf.lms.repository.entity.education.QEducationPlan.educationPlan;
import static kr.or.kpf.lms.repository.entity.education.QExamMaster.examMaster;
import static kr.or.kpf.lms.repository.entity.education.QExamQuestionItem.examQuestionItem;
import static kr.or.kpf.lms.repository.entity.education.QExamQuestionMaster.examQuestionMaster;
import static kr.or.kpf.lms.repository.entity.education.QLectureMaster.lectureMaster;
import static kr.or.kpf.lms.repository.entity.homepage.QNotice.notice;
import static kr.or.kpf.lms.repository.entity.user.QAdminUser.adminUser;
import static kr.or.kpf.lms.repository.entity.user.QLmsUser.lmsUser;
import static kr.or.kpf.lms.repository.entity.user.QOrganizationInfo.organizationInfo;
import static kr.or.kpf.lms.repository.entity.user.QOrganizationInfoMedia.organizationInfoMedia;
import static org.springframework.util.StringUtils.hasText;

/**
 * 교육 신청 공통 Repository 구현체
 */
@Repository
public class CommonEducationRepositoryImpl extends CSRepositorySupport implements CommonEducationRepository {

    @Autowired private ContentsApplicationChapterRepository contentsApplicationChapterRepository;
    @Autowired private CurriculumApplicationMasterRepository curriculumApplicationMasterRepository;
    @Autowired private FileMasterRepository fileMasterRepository;
    @Autowired private AdminUserRepository adminUserRepository;
    @Autowired private OrganizationInfoRepository organizationInfoRepository;

    private final JPAQueryFactory jpaQueryFactory;
    public CommonEducationRepositoryImpl(JPAQueryFactory jpaQueryFactory){ this.jpaQueryFactory = jpaQueryFactory; }

    /**
     * 공통 리스트 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    @Override
    public <T extends CSViewVOSupport> CSPageImpl<?> findEntityList(T requestObject) {
        /** Entity 총 갯수 */
        Long count = getEntityCount(requestObject);
        /** Entity 리스트 */
        List<?> content = getEntityList(requestObject);

        ResponseSummary summary = ResponseSummary.builder()
                .count(content.size())
                .offset(requestObject.getPageNum())
                .limit(requestObject.getPageSize())
                .build();
        Pageable pageableToApply = summary.ensureValidPageable(requestObject.getPageable());

        return CSPageImpl.of(content, pageableToApply, count);
    }

    /**
     * Entity 총 갯수
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    private <T extends CSViewVOSupport> Long getEntityCount(T requestObject) {
        if(requestObject instanceof CurriculumViewRequestVO) { /** 교육 과정 관리 */
            return jpaQueryFactory.select(curriculumMaster.count())
                    .from(curriculumMaster)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof JournalismschoolViewRequestVO) { /** 언론인교육센터 과거 이관데이터 */
            return jpaQueryFactory.select(formeEdu2020History.count())
                    .from(formeEdu2020History)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof JournalismschoolApiRequestVO) { /** 언론인교육센터 과거 이관데이터 */
            return jpaQueryFactory.select(formeEdu2020History.count())
                    .from(formeEdu2020History)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof ExamViewRequestVO) { /** 시험 관리 */
            return jpaQueryFactory.select(curriculumExam.count())
                    .from(curriculumExam)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof ExamQuestionViewRequestVO) { /** 시험 문제 관리 */
            return jpaQueryFactory.select(curriculumQuestion.count())
                    .from(curriculumQuestion)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof EducationApplicationViewRequestVO) { /** 교육 운영 관리 */
            return jpaQueryFactory.select(curriculumApplicationMaster.count())
                    .from(curriculumApplicationMaster)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof ScheduleViewRequestVO) { /** 교육 과정 개설 관리 */
            return jpaQueryFactory.select(educationPlan.count())
                    .from(educationPlan)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof LectureViewRequestVO) { /** 일반 교육 강의 관리 */
            return jpaQueryFactory.select(lectureMaster.count())
                    .from(lectureMaster)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if (requestObject instanceof ReferenceRoomViewRequestVO) { /** 자료실 관리 */
            return jpaQueryFactory.select(curriculumReferenceRoom.count())
                    .from(curriculumReferenceRoom)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else {
            return NumberUtils.LONG_ZERO;
        }
    }

    /**
     * Entity 리스트
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    private <T extends CSViewVOSupport> List<?> getEntityList(T requestObject) {
        if(requestObject instanceof CurriculumViewRequestVO) { /** 교육 과정 관리 */
            return jpaQueryFactory.selectFrom(curriculumMaster)
                    .where(getQuery(requestObject))
                    .orderBy(curriculumMaster.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        } else if(requestObject instanceof JournalismschoolViewRequestVO) { /** 언론인교육센터 과거 이관데이터 */
            return  jpaQueryFactory.selectFrom(formeEdu2020History)
                    .where(getQuery(requestObject))
                    .orderBy(formeEdu2020History.crsNo.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        } else if(requestObject instanceof JournalismschoolApiRequestVO) { /** 언론인교육센터 과거 이관데이터 */
            return  jpaQueryFactory.selectFrom(formeEdu2020History)
                    .where(getQuery(requestObject))
                    .orderBy(formeEdu2020History.crsRqstSeq.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        } else if(requestObject instanceof ExamViewRequestVO) { /** 시험 관리 */
            return jpaQueryFactory.selectFrom(curriculumExam)
                    .where(getQuery(requestObject))
                    .orderBy(curriculumExam.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        } else if(requestObject instanceof ExamQuestionViewRequestVO) { /** 시험 문제 관리 */
            return jpaQueryFactory.selectFrom(curriculumQuestion)
                    .where(getQuery(requestObject))
                    .orderBy(curriculumQuestion.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        } else if(requestObject instanceof EducationApplicationViewRequestVO) { /** 교육 운영 관리 */
            return jpaQueryFactory.selectFrom(curriculumApplicationMaster)
                    .where(getQuery(requestObject))
                    .orderBy(curriculumApplicationMaster.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch().stream().map(data -> {
                        data.getCurriculumApplicationContentsList().stream().forEach(subData -> {
                            subData.setContentsApplicationChapterList(contentsApplicationChapterRepository.findAll(Example.of(ContentsApplicationChapter.builder()
                                            .applicationNo(subData.getApplicationNo())
                                            .curriculumCode(subData.getCurriculumCode())
                                            .contentsCode(subData.getContentsCode())
                                            .build())));
                                });
                        return data;
                    }).collect(Collectors.toList());
        } else if(requestObject instanceof ScheduleViewRequestVO) { /** 교육 과정 개설 관리 */
            return jpaQueryFactory.selectFrom(educationPlan)
                    .where(getQuery(requestObject))
                    .orderBy(educationPlan.isTop.desc(), educationPlan.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch().stream()
                    .peek(plan -> {
                        /** 일자 기준으로 교육 신청 가능 상태 셋팅 */
                        plan.setAvailableApplicationType(Optional.ofNullable(plan.getAvailableApplicationType())
                                .orElseGet(() -> {
                                    try {
                                        if(StringUtils.isEmpty(plan.getApplyBeginDateTime())) {
                                            if (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(plan.getOperationEndDateTime()).after(new Date())) {
                                                return "3"; /** 신청 가능 */
                                            } else {
                                                return "1"; /** 신청 불가 */
                                            }
                                        } else if (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(plan.getApplyBeginDateTime()).before(new Date())
                                                && new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(plan.getApplyEndDateTime()).after(new Date()))
                                            return "3"; /** 신청 가능 */
                                        else if (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(plan.getApplyEndDateTime()).before(new Date()))
                                            return "2"; /** 신청 마감 */
                                        else return "1"; /** 신청 불가 */
                                    } catch (Exception e) {
                                        throw new KPFException(KPF_RESULT.ERROR3022, "교육 과정 조회 중 날짜 파싱 오류.");
                                    }
                                }));

                        List<CurriculumApplicationMaster> curriculumApplicationMasters = curriculumApplicationMasterRepository.findByEducationPlanCodeAndAdminApprovalStateIn(plan.getEducationPlanCode(), List.of(Code.ADM_APL_STATE.WAIT.enumCode, Code.ADM_APL_STATE.APPROVAL.enumCode));
                        /** 해당 교육 현재 신청 인원 셋팅(승인 + 대기) */
                        plan.setCurrentNumberOfPeople(curriculumApplicationMasters.size());
                        /** 해당 교육 현재 신청 인원 셋팅(승인만...) */
                        plan.setCurrentApprovalNumberOfPeople((int) curriculumApplicationMasters.stream().filter(data -> Code.ADM_APL_STATE.APPROVAL.enumCode.equals(data.getAdminApprovalState())).count());
                        /** 해당 교육 현재 수료 인원 셋팅 */
                        plan.setCurrentCompleteNumberOfPeople((int) curriculumApplicationMasters.stream().filter(data -> data.getIsComplete().equals("Y")).count());

                        if(plan.getNumberOfPeople() != null && plan.getNumberOfPeople() > 0 && plan.getNumberOfPeople() <= plan.getCurrentNumberOfPeople()) {
                            /** 교육 일자 기준으로 신청 가능 이였을 경우 신청 마감으로 재변경 */
                            if(plan.getAvailableApplicationType().equals("3")) {
                                plan.setAvailableApplicationType("2");
                            }
                        }

                        plan.setFileMasters(fileMasterRepository.findAll(Example.of(FileMaster.builder()
                                .atchFileSn(plan.getEducationPlanCode())
                                .build())));

                        /*AdminUser user = jpaQueryFactory.selectFrom(adminUser)
                                .where(adminUser.userId.eq(plan.getRegistUserId()))
                                .fetchOne();*/
                        String adminName = jpaQueryFactory.select(adminUser.userName)
                                .from(adminUser)
                                .where(adminUser.userId.eq(plan.getEduPlanPic()))
                                .fetchOne();
                        if (adminName != null)
                            plan.setAdminName(adminName);

                    }).sorted(Comparator.comparing(EducationPlan::getIsTop).thenComparing(EducationPlan::getOperationBeginDateTime).reversed()).collect(Collectors.toList());
        } else if(requestObject instanceof LectureViewRequestVO) { /** 일반 교육 강의 관리 */
            return jpaQueryFactory.selectFrom(lectureMaster)
                    .where(getQuery(requestObject))
                    .orderBy(lectureMaster.createDateTime.desc(), lectureMaster.operationBeginDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        } else if (requestObject instanceof ReferenceRoomViewRequestVO) { /** 자료실 관리 */
            return jpaQueryFactory.selectFrom(curriculumReferenceRoom)
                    .where(getQuery(requestObject))
                    .orderBy(curriculumReferenceRoom.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 공통 엑셀 다운로드
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    @Override
    public <T extends CSViewVOSupport> List<?> excelDownload(T requestObject) {
        if(requestObject instanceof CurriculumViewRequestVO) { /** 교육 과정 관리 */
        List<CurriculumExcelVO> dtos = jpaQueryFactory.select(Projections.fields(CurriculumExcelVO.class,
                        curriculumMaster.educationType,
                        curriculumMaster.categoryCode,
                        curriculumMaster.curriculumName,
                        curriculumMaster.createDateTime,
                        curriculumMaster.isUsable,
                        curriculumMaster.educationPerHour,
                        curriculumMaster.educationPerMinute,
                        curriculumMaster.managerName,
                        curriculumMaster.managerDepartment,
                        curriculumMaster.educationTarget
                ))
                .from(curriculumMaster)
                .where(getQuery(requestObject))
                .orderBy(curriculumMaster.createDateTime.desc())
                .fetch();
        for(CurriculumExcelVO dto : dtos){
            if(dto.getEducationPerMinute()!=null && dto.getEducationPerMinute()>0){
                dto.setEduHr(dto.getEducationPerHour()+"시간 "+dto.getEducationPerMinute()+"분");
            }else{
                dto.setEduHr(dto.getEducationPerHour()+"시간");
            }
            if(dto.getIsUsable()){
                dto.setIsUsableString("사용");
            }else{
                dto.setIsUsableString("미사용");
            }
            if(dto.getCategoryCode()==null){
                dto.setCategoryCode("-");
            }else if(dto.getCategoryCode().equals("1")){
                dto.setCategoryCode("언론인연수");
            }else if(dto.getCategoryCode().equals("2")){
                dto.setCategoryCode("미디어교육");
            }else if(dto.getCategoryCode().equals("3")){
                dto.setCategoryCode("공통");
            }
            if(dto.getEducationType()==null){
                dto.setEducationType("-");
            }else if(dto.getEducationType().equals(Code.EDU_TYPE.VIDEO.enumCode)){
                dto.setEducationType(Code.EDU_TYPE.VIDEO.codeName);
            }else if(dto.getEducationType().equals(Code.EDU_TYPE.CONVOCATION.enumCode)){
                dto.setEducationType(Code.EDU_TYPE.CONVOCATION.codeName);
            }else if(dto.getEducationType().equals(Code.EDU_TYPE.E_LEARNING.enumCode)){
                dto.setEducationType(Code.EDU_TYPE.E_LEARNING.codeName);
            }else {
                dto.setEducationType("-");
            }
            if(dto.getEducationTarget()==null){
                dto.setEducationTarget("-");
            }else if(dto.getEducationTarget().equals("1")){
                dto.setEducationTarget("일반인(교원+학생+학부모)");
            }else if(dto.getEducationTarget().equals("2")){
                dto.setEducationTarget("언론인");
            }else if(dto.getEducationTarget().equals("3")){
                dto.setEducationTarget("교원");
            }else if(dto.getEducationTarget().equals("4")){
                dto.setEducationTarget("학생");
            }else if(dto.getEducationTarget().equals("5")){
                dto.setEducationTarget("학부모");
            }else if(dto.getEducationTarget().equals("6")){
                dto.setEducationTarget("모든 대상");
            }
        }
            return dtos;
        } else if(requestObject instanceof ScheduleViewRequestVO){ /** 교육 과정 개설 관리 */
            List<ScheduleExcelVO> dtos= jpaQueryFactory.select(Projections.fields(ScheduleExcelVO.class,
                            educationPlan.operationType,
                            educationPlan.yearOfEducationPlan,
                            educationPlan.yearOfEducationPlanStep,
                            educationPlan.province,
                            educationPlan.alwaysEducationTerm,
                            educationPlan.isUsable,
                            educationPlan.applyBeginDateTime,
                            educationPlan.applyEndDateTime,
                            educationPlan.cancelBeginDateTime,
                            educationPlan.cancelEndDateTime,
                            educationPlan.operationBeginDateTime,
                            educationPlan.operationEndDateTime,
                            educationPlan.educationPlace,
                            educationPlan.numberOfPeople,
                            educationPlan.isAccommodation,
                            educationPlan.isReview,
                            educationPlan.availableReviewTerm,
                            educationPlan.isVideoLecture,
                            educationPlan.videoLecturePath,
                            educationPlan.createDateTime,
                            educationPlan.updateDateTime,
                            curriculumMaster.categoryCode,
                            curriculumMaster.educationType,
                            curriculumMaster.curriculumName
                    ))
                    .from(educationPlan)
                    .where(getQuery(requestObject))
                    .leftJoin(curriculumMaster).on(curriculumMaster.curriculumCode.eq(educationPlan.curriculumCode))
                    .orderBy(educationPlan.createDateTime.asc())
                    .fetch();
            for(ScheduleExcelVO dto : dtos){
                if(dto.getCategoryCode()==null){
                    dto.setCategoryCode("-");
                }else if(dto.getCategoryCode().equals("1")){
                    dto.setCategoryCode("언론인연수");
                }else if(dto.getCategoryCode().equals("2")){
                    dto.setCategoryCode("미디어교육");
                }else{
                    dto.setCategoryCode("공개교육");
                }
                if(dto.getEducationType() == null){
                    dto.setEducationType("-");
                }else if(dto.getEducationType().equals("1")){
                    dto.setEducationType("화상 교육");
                }else if(dto.getEducationType().equals("2")){
                    dto.setEducationType("집합 교육");
                }else{
                    dto.setEducationType("이러닝 교육");
                }
                if(dto.getOperationType()==null){
                    dto.setOperationType("-");
                }else if(dto.getOperationType().equals("1")){
                    dto.setOperationType("기수 운영");
                }else{
                    dto.setOperationType("상시 운영");
                }
                if(dto.getIsUsable()){
                    dto.setState("사용");
                }else{
                    dto.setState("사용안함");
                }
                if(dto.getIsAccommodation()){
                    dto.setIsAccommodationString("제공");
                }else{
                    dto.setIsAccommodationString("제공안함");
                }
                if(dto.getIsReview()){
                    dto.setIsReviewString("제공");
                }else{
                    dto.setIsReviewString("제공안함");
                }
                if(dto.getIsVideoLecture()){
                    dto.setIsVideoLectureString("제공");
                }else{
                    dto.setIsVideoLectureString("제공안함");
                }
            }
            return dtos;
        } else if(requestObject instanceof EducationApplicationViewRequestVO){ /** 교육 운영 관리 */
            if(((EducationApplicationViewRequestVO) requestObject).getIsApply()){ /** 신청관리 */
                List<EducationApplicationExcelVO> dtos = jpaQueryFactory.select(Projections.fields(EducationApplicationExcelVO.class,
                                educationPlan.operationType,
                                curriculumMaster.categoryCode,
                                curriculumMaster.curriculumName,
                                curriculumMaster.educationType,
                                curriculumApplicationMaster.setEducationType,
                                curriculumApplicationMaster.adminApprovalState,
                                curriculumApplicationMaster.userId,
                                educationPlan.applyBeginDateTime,
                                educationPlan.applyEndDateTime,
                                curriculumMaster.curriculumName,
                                curriculumApplicationMaster.createDateTime,
                                lmsUser.phone,
                                lmsUser.email,
                                lmsUser.userName,
                                lmsUser.gender,
                                lmsUser.department,
                                lmsUser.rank,
                                lmsUser.position,
                                lmsUser.birthDay,
                                lmsUser.personalNiceNo,
                                educationPlan.yearOfEducationPlan,
                                educationPlan.yearOfEducationPlanStep
                        ))
                        .from(curriculumApplicationMaster)
                        .where(getQuery(requestObject))
                        .leftJoin(curriculumMaster).on(curriculumMaster.curriculumCode.eq(curriculumApplicationMaster.curriculumCode))
                        .leftJoin(educationPlan).on(educationPlan.educationPlanCode.eq(curriculumApplicationMaster.educationPlanCode))
                        .leftJoin(lmsUser).on(lmsUser.userId.eq(curriculumApplicationMaster.userId))
                        .orderBy(curriculumApplicationMaster.createDateTime.asc())
                        .fetch();
                for(EducationApplicationExcelVO dto:dtos){
                    if(dto.getOperationType() == null){
                        dto.setOperationType("-");
                    }else if(dto.getOperationType().equals("1")){
                        dto.setOperationType("기수운영");
                    }else if(dto.getOperationType().equals("2")){
                        dto.setOperationType("상시운영");
                    }
                    if(dto.getEducationType() == null){
                        dto.setEducationType("-");
                    }else if(dto.getEducationType().equals("1")){
                        dto.setEducationType("화상");
                    }else if(dto.getEducationType().equals("2")){
                        dto.setEducationType("집합");
                    }else if(dto.getEducationType().equals("3")){
                        dto.setEducationType("이러닝");
                    }else if(dto.getEducationType().equals("4")){
                        dto.setEducationType("병행(화상+집합)");
                    }
                    if(dto.getSetEducationType() == null){
                        if(dto.getEducationType().equals("1")){
                            dto.setSetEducationType("화상");
                        }else if(dto.getEducationType().equals("2")){
                            dto.setSetEducationType("집합");
                        }else if(dto.getEducationType().equals("3")) {
                            dto.setSetEducationType("이러닝");
                        } else dto.setSetEducationType("-");
                    }else if(dto.getSetEducationType().equals("1")){
                        dto.setSetEducationType("화상");
                    }else if(dto.getSetEducationType().equals("2")){
                        dto.setSetEducationType("집합");
                    }else if(dto.getSetEducationType().equals("3")){
                        dto.setSetEducationType("이러닝");
                    } else dto.setSetEducationType("-");
                    if(dto.getAdminApprovalState() == null){
                        dto.setAdminApprovalState("-");
                    }else if(dto.getAdminApprovalState().equals(Code.ADM_APL_STATE.WAIT.enumCode)){
                        dto.setAdminApprovalState("승인대기");
                    }else if(dto.getAdminApprovalState().equals(Code.ADM_APL_STATE.APPROVAL.enumCode)){
                        dto.setAdminApprovalState("승인");
                    }else if(dto.getAdminApprovalState().equals(Code.ADM_APL_STATE.REFUSE.enumCode)){
                        dto.setAdminApprovalState("승인거절(취소)");
                    }else if(dto.getAdminApprovalState().equals(Code.ADM_APL_STATE.CANCEL.enumCode)){
                        dto.setAdminApprovalState("신청취소");
                    }
                    if (dto.getGender() == null) {
                        dto.setGenderString("미지정");
                    } else if (dto.getGender().equals("1")) {
                        dto.setGenderString("남성");
                    } else if (dto.getGender().equals("2")) {
                        dto.setGenderString("여성");
                    } else {
                        dto.setGenderString("미지정");
                    }
                    LmsUser user = jpaQueryFactory.selectFrom(lmsUser)
                            .where(lmsUser.userId.eq(dto.getUserId()))
                            .fetchOne();
                    if (user.getOrganizationCode() != null && !user.getOrganizationCode().equals("")) {
                        OrganizationInfo orgInfo = jpaQueryFactory.selectFrom(organizationInfo)
                                .where(organizationInfo.organizationCode.eq(user.getOrganizationCode()))
                                .fetchOne();
                        if (orgInfo != null) {
                            dto.setOrganizationName(orgInfo.getOrganizationName());
                        }
                    }
                    if (user.getMediaCode() != null && !user.getMediaCode().equals("")) {
                        OrganizationInfoMedia mediaInfo = jpaQueryFactory.selectFrom(organizationInfoMedia)
                                .where(organizationInfoMedia.mediaCode.eq(user.getMediaCode()))
                                .fetchOne();
                        if (mediaInfo != null) {
                            dto.setMediaName(mediaInfo.getMediaName());
                        }
                    }
                }
                return dtos;
            }else { /** 수료관리 */
                List<EducationCompleteExcelVO> dtos = jpaQueryFactory.select(Projections.fields(EducationCompleteExcelVO.class,
                                educationPlan.operationType,
                                curriculumMaster.categoryCode,
                                curriculumMaster.curriculumName,
                                curriculumApplicationMaster.adminApprovalState,
                                curriculumApplicationMaster.userId,
                                curriculumApplicationMaster.isComplete,
                                educationPlan.operationBeginDateTime,
                                educationPlan.operationEndDateTime,
                                curriculumMaster.curriculumName,
                                lmsUser.phone,
                                lmsUser.email,
                                lmsUser.userName,
                                lmsUser.birthDay,
                                lmsUser.personalNiceNo,
                                lmsUser.organizationCode,
                                lmsUser.department,
                                lmsUser.rank,
                                lmsUser.position,
                                educationPlan.province,
                                educationPlan.eduPlanPic,
                                educationPlan.yearOfEducationPlan,
                                educationPlan.yearOfEducationPlanStep,
                                curriculumApplicationMaster.assignmentScore,
                                curriculumApplicationMaster.examScore,
                                curriculumApplicationMaster.progressScore,
                                curriculumApplicationMaster.progressRate,
                                curriculumApplicationMaster.progressScore,
                                curriculumApplicationMaster.educationState,
                                educationPlan.createDateTime,
                                curriculumApplicationMaster.programCompleteNumber
                        ))
                        .from(curriculumApplicationMaster)
                        .where(getQuery(requestObject))
                        .leftJoin(curriculumMaster).on(curriculumMaster.curriculumCode.eq(curriculumApplicationMaster.curriculumCode))
                        .leftJoin(educationPlan).on(educationPlan.educationPlanCode.eq(curriculumApplicationMaster.educationPlanCode))
                        .leftJoin(lmsUser).on(lmsUser.userId.eq(curriculumApplicationMaster.userId))
                        .orderBy(curriculumApplicationMaster.createDateTime.asc())
                        .fetch();

                for (EducationCompleteExcelVO dto : dtos) {
                    if (dto.getOperationType() == null) {
                        dto.setOperationType("-");
                    } else if (dto.getOperationType().equals("1")) {
                        dto.setOperationType("기수운영");
                    } else if (dto.getOperationType().equals("2")) {
                        dto.setOperationType("상시운영");
                    }
                    if (dto.getIsComplete().equals("Y")) {
                        dto.setState("수료");
                    } else if (dto.getIsComplete().equals("N")) {
                        dto.setState("미수료");
                    } else {
                        dto.setState("처리중");
                    }
                    if (dto.getEducationState() == null) {
                        dto.setEducationState("-");
                    } else if (dto.getEducationState().equals("1")) {
                        dto.setEducationState("신청 중");
                    } else if (dto.getEducationState().equals("2")) {
                        dto.setEducationState("교육 중");
                    } else if (dto.getEducationState().equals("3")) {
                        dto.setEducationState("교육 완료");
                    }
                    if (dto.getProvince() != null) {
                        if (dto.getProvince().equals(Code.PROVINCE_CD.HEAD.enumCode))
                            dto.setProvinceName("본사");
                        else if (dto.getProvince().equals(Code.PROVINCE_CD.SEDA.enumCode))
                            dto.setProvinceName("세종대전지사");
                        else if (dto.getProvince().equals(Code.PROVINCE_CD.BU.enumCode))
                            dto.setProvinceName("부산지사");
                        else if (dto.getProvince().equals(Code.PROVINCE_CD.GW.enumCode))
                            dto.setProvinceName("광주지사");
                        else if (dto.getProvince().equals(Code.PROVINCE_CD.DA.enumCode))
                            dto.setProvinceName("대구지사");
                    } else {
                        dto.setProvinceName("");
                    }

                    String adminName = jpaQueryFactory.select(adminUser.userName)
                            .from(adminUser)
                            .where(adminUser.userId.eq(dto.getEduPlanPic()))
                            .fetchOne();
                    if (adminName != null)
                        dto.setAdminName(adminName);

                    if (dto.getOrganizationCode() != null && !dto.getOrganizationCode().equals("")) {
                        OrganizationInfo orgInfo = organizationInfoRepository.findOne(Example.of(OrganizationInfo.builder()
                                .organizationCode(dto.getOrganizationCode())
                                .build())).get();
                        if (orgInfo != null) {
                            if (orgInfo.getOrganizationAddress1() != null && !orgInfo.getOrganizationAddress1().isEmpty()) {
                                List<String> addressList = List.of(orgInfo.getOrganizationAddress1().split(" "));
                                if (addressList.size() > 1) {
                                    String region = addressList.get(0);
                                    dto.setRegion(region.substring(0, 2));
                                }
                            }
                        }
                    }

                    if (dto.getProgramCompleteNumber() != null) {
                        String str = new StringBuilder("미교원-").append(dto.getCreateDateTime().substring(0, 4)).append("-직무-").append(dto.getProgramCompleteNumber()).toString();
                        dto.setCompleteNumber(str);
                    }
                    LmsUser user = jpaQueryFactory.selectFrom(lmsUser)
                            .where(lmsUser.userId.eq(dto.getUserId()))
                            .fetchOne();
                    if (user.getOrganizationCode() != null && !user.getOrganizationCode().equals("")) {
                        OrganizationInfo orgInfo = jpaQueryFactory.selectFrom(organizationInfo)
                                .where(organizationInfo.organizationCode.eq(user.getOrganizationCode()))
                                .fetchOne();
                        if (orgInfo != null) {
                            dto.setOrganizationName(orgInfo.getOrganizationName());
                        }
                    }
                    if (user.getMediaCode() != null && !user.getMediaCode().equals("")) {
                        OrganizationInfoMedia mediaInfo = jpaQueryFactory.selectFrom(organizationInfoMedia)
                                .where(organizationInfoMedia.mediaCode.eq(user.getMediaCode()))
                                .fetchOne();
                        if (mediaInfo != null) {
                            dto.setMediaName(mediaInfo.getMediaName());
                        }
                    }
                }
                return dtos;
            }
        } else if (requestObject instanceof ReferenceRoomViewRequestVO) {
            List<ReferenceRoomExcelVO> dtos = jpaQueryFactory.select(Projections.fields(ReferenceRoomExcelVO.class,
                            curriculumReferenceRoom.title,
                            curriculumReferenceRoom.contents,
                            curriculumReferenceRoom.filePath,
                            curriculumReferenceRoom.fileSize,
                            curriculumReferenceRoom.adminUser.userName
                    ))
                    .from(curriculumReferenceRoom)
                    .where(getQuery(requestObject))
                    .orderBy(curriculumReferenceRoom.createDateTime.asc())
                    .fetch();

            if (!dtos.isEmpty() && dtos.size() > 0) {
                for (ReferenceRoomExcelVO dto : dtos) {
                    if (dto.getFilePath() != null && !dto.getFilePath().equals(null)) {
                        List<String> fileNames = List.of(dto.getFilePath().split("/"));
                        String fileName = fileNames.get(fileNames.size()-1);
                        dto.setFileName(fileName);
                    }
                }
            }
            return dtos;

        } else {
            return new ArrayList<>();
        }
    }

    /**
     * where 절
     */
    private <T extends CSViewVOSupport> Predicate[] getQuery(T requestObject) {
        if(requestObject instanceof CurriculumViewRequestVO) { /** 교육 과정 관리 */
            return new Predicate[]{ condition(((CurriculumViewRequestVO) requestObject).getEducationType(), curriculumMaster.educationType::eq),
                    condition(((CurriculumViewRequestVO) requestObject).getCategoryCode(), curriculumMaster.categoryCode::eq),
                    condition(((CurriculumViewRequestVO) requestObject).getCurriculumAreaCode(), curriculumMaster.curriculumAreaCode::eq),
                    condition(((CurriculumViewRequestVO) requestObject).getCurriculumAreaCodeDetail(), curriculumMaster.curriculumAreaCodeDetail::eq),
                    condition(((CurriculumViewRequestVO) requestObject).getCurriculumName(), curriculumMaster.curriculumName::contains),
                    condition(((CurriculumViewRequestVO) requestObject).getIsUsable(), curriculumMaster.isUsable::eq),
                    condition(((CurriculumViewRequestVO) requestObject).getCurriculumCode(), curriculumMaster.curriculumCode::eq),
                    betweenTime(requestObject)};
        } else if(requestObject instanceof JournalismschoolViewRequestVO) { /** 언론인교육센터 과거 이관데이터 */
            return new Predicate[]{
                    condition(((JournalismschoolViewRequestVO) requestObject).getUserNm(), formeEdu2020History.userNm::contains),
                    condition(((JournalismschoolViewRequestVO) requestObject).getCrsNm(), formeEdu2020History.crsNm::contains),
                    condition(((JournalismschoolViewRequestVO) requestObject).getCrsCls1Nm(), formeEdu2020History.crsCls1Nm::contains),
                    condition(((JournalismschoolViewRequestVO) requestObject).getCrsCls2Nm(), formeEdu2020History.crsCls2Nm::contains),
                    condition(((JournalismschoolViewRequestVO) requestObject).getUserId(), formeEdu2020History.userId::contains),
                    condition(((JournalismschoolViewRequestVO) requestObject).getMdiaNm(), formeEdu2020History.mdiaNm::contains),
                    condition(((JournalismschoolViewRequestVO) requestObject).getMdiaDNm(), formeEdu2020History.mdiaDNm::contains),
                    condition(((JournalismschoolViewRequestVO) requestObject).getStartDate(), formeEdu2020History.cmpltDt2::goe),
                    condition(((JournalismschoolViewRequestVO) requestObject).getEndDate(), formeEdu2020History.cmpltDt2::loe),
            };
        }else if(requestObject instanceof JournalismschoolApiRequestVO) { /** 언론인교육센터 과거 이관데이터 */
            return new Predicate[]{condition(((JournalismschoolApiRequestVO) requestObject).getUserId(), formeEdu2020History.memId::eq),
            };
        } else if(requestObject instanceof ExamViewRequestVO) { /** 시험 관리 */
            return new Predicate[]{condition(((ExamViewRequestVO) requestObject).getCurriculumCode(), curriculumExam.curriculumCode::eq),
                    condition(((ExamViewRequestVO) requestObject).getExamName(), curriculumExam.examMaster.examName::contains),
                    betweenTime(requestObject)};
        } else if(requestObject instanceof ExamQuestionViewRequestVO) { /** 시험 문제 관리 */
            return new Predicate[]{condition(((ExamQuestionViewRequestVO) requestObject).getCurriculumCode(), curriculumQuestion.curriculumCode::eq),
                    condition(((ExamQuestionViewRequestVO) requestObject).getQuestionSerialNo(), curriculumQuestion.questionSerialNo::eq),
                    condition(((ExamQuestionViewRequestVO) requestObject).getQuestionLevel(), curriculumQuestion.examQuestionMaster.questionLevel::eq),
                    condition(((ExamQuestionViewRequestVO) requestObject).getIsUsable(), curriculumQuestion.examQuestionMaster.isUsable::eq),
                    condition(((ExamQuestionViewRequestVO) requestObject).getQuestionType(), curriculumQuestion.examQuestionMaster.questionType::eq),
                    searchContainText(requestObject, ((ExamQuestionViewRequestVO) requestObject).getContainTextType(), ((ExamQuestionViewRequestVO) requestObject).getContainText()),
                    betweenTime(requestObject)};
        } else if(requestObject instanceof EducationApplicationViewRequestVO) { /** 교육 운영 관리 */
            if (((EducationApplicationViewRequestVO) requestObject).getEducationType() != null) {
                if (((EducationApplicationViewRequestVO) requestObject).getEducationType().equals("0")) {
                    return new Predicate[] {betweenTime(requestObject),
                            searchContainText(requestObject, ((EducationApplicationViewRequestVO) requestObject).getContainTextType(), ((EducationApplicationViewRequestVO) requestObject).getContainText()),
                            condition(((EducationApplicationViewRequestVO) requestObject).getCurriculumName(), curriculumApplicationMaster.educationPlan.curriculumMaster.curriculumName::contains),
                            condition(((EducationApplicationViewRequestVO) requestObject).getCategoryCode(), curriculumApplicationMaster.educationPlan.curriculumMaster.categoryCode::eq),
                            condition(Code.EDU_TYPE.E_LEARNING.enumCode, curriculumApplicationMaster.educationPlan.curriculumMaster.educationType::ne),
                            condition(((EducationApplicationViewRequestVO) requestObject).getOperationType(), curriculumApplicationMaster.educationPlan.operationType::eq),
                            condition(((EducationApplicationViewRequestVO) requestObject).getProvince(), curriculumApplicationMaster.educationPlan.province::eq),
                            condition(((EducationApplicationViewRequestVO) requestObject).getEducationPlanCode(), curriculumApplicationMaster.educationPlanCode::eq),
                            condition(((EducationApplicationViewRequestVO) requestObject).getApplicationNo(), curriculumApplicationMaster.applicationNo::eq),
                            condition(((EducationApplicationViewRequestVO) requestObject).getYearOfEducationPlan(), curriculumApplicationMaster.educationPlan.yearOfEducationPlan::eq),
                            condition(((EducationApplicationViewRequestVO) requestObject).getYearOfEducationPlanStep(), curriculumApplicationMaster.educationPlan.yearOfEducationPlanStep::eq),
                            condition(((EducationApplicationViewRequestVO) requestObject).getAdminApprovalState(), curriculumApplicationMaster.adminApprovalState::eq),
                            condition(((EducationApplicationViewRequestVO) requestObject).getIsComplete(), curriculumApplicationMaster.isComplete::eq),
                            condition(((EducationApplicationViewRequestVO) requestObject).getUserId(), curriculumApplicationMaster.userId::eq),
                            condition(((EducationApplicationViewRequestVO) requestObject).getUserName(), curriculumApplicationMaster.lmsUser.userName::contains)};
                } else {
                    return new Predicate[] {betweenTime(requestObject),
                            searchContainText(requestObject, ((EducationApplicationViewRequestVO) requestObject).getContainTextType(), ((EducationApplicationViewRequestVO) requestObject).getContainText()),
                            condition(((EducationApplicationViewRequestVO) requestObject).getCurriculumName(), curriculumApplicationMaster.educationPlan.curriculumMaster.curriculumName::contains),
                            condition(((EducationApplicationViewRequestVO) requestObject).getCategoryCode(), curriculumApplicationMaster.educationPlan.curriculumMaster.categoryCode::eq),
                            condition(((EducationApplicationViewRequestVO) requestObject).getEducationType(), curriculumApplicationMaster.educationPlan.curriculumMaster.educationType::eq),
                            condition(((EducationApplicationViewRequestVO) requestObject).getOperationType(), curriculumApplicationMaster.educationPlan.operationType::eq),
                            condition(((EducationApplicationViewRequestVO) requestObject).getProvince(), curriculumApplicationMaster.educationPlan.province::eq),
                            condition(((EducationApplicationViewRequestVO) requestObject).getEducationPlanCode(), curriculumApplicationMaster.educationPlanCode::eq),
                            condition(((EducationApplicationViewRequestVO) requestObject).getApplicationNo(), curriculumApplicationMaster.applicationNo::eq),
                            condition(((EducationApplicationViewRequestVO) requestObject).getYearOfEducationPlan(), curriculumApplicationMaster.educationPlan.yearOfEducationPlan::eq),
                            condition(((EducationApplicationViewRequestVO) requestObject).getYearOfEducationPlanStep(), curriculumApplicationMaster.educationPlan.yearOfEducationPlanStep::eq),
                            condition(((EducationApplicationViewRequestVO) requestObject).getAdminApprovalState(), curriculumApplicationMaster.adminApprovalState::eq),
                            condition(((EducationApplicationViewRequestVO) requestObject).getIsComplete(), curriculumApplicationMaster.isComplete::eq),
                            condition(((EducationApplicationViewRequestVO) requestObject).getUserId(), curriculumApplicationMaster.userId::eq),
                            condition(((EducationApplicationViewRequestVO) requestObject).getUserName(), curriculumApplicationMaster.lmsUser.userName::contains)};
                }
            } else {
                return new Predicate[] {betweenTime(requestObject),
                        searchContainText(requestObject, ((EducationApplicationViewRequestVO) requestObject).getContainTextType(), ((EducationApplicationViewRequestVO) requestObject).getContainText()),
                        condition(((EducationApplicationViewRequestVO) requestObject).getCurriculumName(), curriculumApplicationMaster.educationPlan.curriculumMaster.curriculumName::contains),
                        condition(((EducationApplicationViewRequestVO) requestObject).getCategoryCode(), curriculumApplicationMaster.educationPlan.curriculumMaster.categoryCode::eq),
                        condition(((EducationApplicationViewRequestVO) requestObject).getOperationType(), curriculumApplicationMaster.educationPlan.operationType::eq),
                        condition(((EducationApplicationViewRequestVO) requestObject).getProvince(), curriculumApplicationMaster.educationPlan.province::eq),
                        condition(((EducationApplicationViewRequestVO) requestObject).getEducationPlanCode(), curriculumApplicationMaster.educationPlanCode::eq),
                        condition(((EducationApplicationViewRequestVO) requestObject).getApplicationNo(), curriculumApplicationMaster.applicationNo::eq),
                        condition(((EducationApplicationViewRequestVO) requestObject).getYearOfEducationPlan(), curriculumApplicationMaster.educationPlan.yearOfEducationPlan::eq),
                        condition(((EducationApplicationViewRequestVO) requestObject).getYearOfEducationPlanStep(), curriculumApplicationMaster.educationPlan.yearOfEducationPlanStep::eq),
                        condition(((EducationApplicationViewRequestVO) requestObject).getAdminApprovalState(), curriculumApplicationMaster.adminApprovalState::eq),
                        condition(((EducationApplicationViewRequestVO) requestObject).getIsComplete(), curriculumApplicationMaster.isComplete::eq),
                        condition(((EducationApplicationViewRequestVO) requestObject).getUserId(), curriculumApplicationMaster.userId::eq),
                        condition(((EducationApplicationViewRequestVO) requestObject).getUserName(), curriculumApplicationMaster.lmsUser.userName::contains)};
            }
        } else if(requestObject instanceof ScheduleViewRequestVO) { /** 교육 과정 개설 관리 */
            if (((ScheduleViewRequestVO) requestObject).getEducationType() != null) {
                if (((ScheduleViewRequestVO) requestObject).getEducationType().equals(Code.EDU_TYPE.E_LEARNING.enumCode)) {
                    return new Predicate[] {betweenTime(requestObject),
                            availableApplicationType((ScheduleViewRequestVO) requestObject),
                            condition(((ScheduleViewRequestVO) requestObject).getCategoryCode(), educationPlan.curriculumMaster.categoryCode::eq),
                            condition(((ScheduleViewRequestVO) requestObject).getEducationPlanCode(), educationPlan.educationPlanCode::eq),
                            condition(((ScheduleViewRequestVO) requestObject).getCurriculumName(), educationPlan.curriculumMaster.curriculumName::contains),
                            condition(((ScheduleViewRequestVO) requestObject).getEducationType(), educationPlan.curriculumMaster.educationType::eq),
                            condition(((ScheduleViewRequestVO) requestObject).getProvince(), educationPlan.province::eq),
                            condition(((ScheduleViewRequestVO) requestObject).getOperationType(), educationPlan.operationType::eq),
                            condition(((ScheduleViewRequestVO) requestObject).getYearOfEducationPlan(), educationPlan.yearOfEducationPlan::eq),
                            condition(((ScheduleViewRequestVO) requestObject).getYearOfEducationPlanStep(), educationPlan.yearOfEducationPlanStep::eq)};
                } else {
                    String[] eduTypes = {Code.EDU_TYPE.VIDEO.enumCode, Code.EDU_TYPE.CONVOCATION.enumCode, Code.EDU_TYPE.LECTURE.enumCode};
                    return new Predicate[] {betweenTime(requestObject),
                            availableApplicationType((ScheduleViewRequestVO) requestObject),
                            condition(((ScheduleViewRequestVO) requestObject).getCategoryCode(), educationPlan.curriculumMaster.categoryCode::eq),
                            condition(((ScheduleViewRequestVO) requestObject).getEducationPlanCode(), educationPlan.educationPlanCode::eq),
                            condition(((ScheduleViewRequestVO) requestObject).getCurriculumName(), educationPlan.curriculumMaster.curriculumName::contains),
                            condition(eduTypes, educationPlan.curriculumMaster.educationType::in),
                            condition(((ScheduleViewRequestVO) requestObject).getEduType(), educationPlan.curriculumMaster.educationType::eq),
                            condition(((ScheduleViewRequestVO) requestObject).getProvince(), educationPlan.province::eq),
                            condition(((ScheduleViewRequestVO) requestObject).getOperationType(), educationPlan.operationType::eq),
                            condition(((ScheduleViewRequestVO) requestObject).getYearOfEducationPlan(), educationPlan.yearOfEducationPlan::eq),
                            condition(((ScheduleViewRequestVO) requestObject).getYearOfEducationPlanStep(), educationPlan.yearOfEducationPlanStep::eq)};
                }
            } else {
                return new Predicate[] {betweenTime(requestObject),
                        availableApplicationType((ScheduleViewRequestVO) requestObject),
                        condition(((ScheduleViewRequestVO) requestObject).getCategoryCode(), educationPlan.curriculumMaster.categoryCode::eq),
                        condition(((ScheduleViewRequestVO) requestObject).getEducationPlanCode(), educationPlan.educationPlanCode::eq),
                        condition(((ScheduleViewRequestVO) requestObject).getCurriculumName(), educationPlan.curriculumMaster.curriculumName::contains),
                        condition(((ScheduleViewRequestVO) requestObject).getProvince(), educationPlan.province::eq),
                        condition(((ScheduleViewRequestVO) requestObject).getOperationType(), educationPlan.operationType::eq),
                        condition(((ScheduleViewRequestVO) requestObject).getYearOfEducationPlan(), educationPlan.yearOfEducationPlan::eq),
                        condition(((ScheduleViewRequestVO) requestObject).getYearOfEducationPlanStep(), educationPlan.yearOfEducationPlanStep::eq)};
            }
        } else if(requestObject instanceof LectureViewRequestVO) { /** 일반 교육 강의 관리 */
            return new Predicate[] { betweenTime(requestObject),
                    condition(((LectureViewRequestVO) requestObject).getEducationPlanCode(), lectureMaster.educationPlanCode::eq)};
        } else if (requestObject instanceof ReferenceRoomViewRequestVO) { /** 자료실 관리 */
            return new Predicate[] { betweenTime(requestObject),
                    searchContainText(requestObject, ((ReferenceRoomViewRequestVO) requestObject).getContainTextType(), ((ReferenceRoomViewRequestVO) requestObject).getContainText()),
                    condition(((ReferenceRoomViewRequestVO) requestObject).getCurriculumCode(), curriculumReferenceRoom.curriculumCode::eq),
                    condition(((ReferenceRoomViewRequestVO) requestObject).getSequenceNo(), curriculumReferenceRoom.sequenceNo::eq),
                    condition(((ReferenceRoomViewRequestVO) requestObject).getTitle(), curriculumReferenceRoom.title::contains),
                    condition(((ReferenceRoomViewRequestVO) requestObject).getUserName(), curriculumReferenceRoom.adminUser.userName::contains)};
        } else {
            return new Predicate[] {};
        }
    }

    private <T extends CSViewVOSupport> BooleanExpression availableApplicationType(ScheduleViewRequestVO requestObject) {
        LocalDate ld = LocalDate.now();
        Date d = java.sql.Date.valueOf(ld);
        DateExpression<Date> expressionNow = Expressions.dateTemplate(Date.class, "{0}", d);
        if(requestObject.getAvailableApplicationType() != null){
            if(requestObject.getAvailableApplicationType().equals("1")){
                DateExpression<Date> expression = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", educationPlan.applyBeginDateTime, "%Y-%m-%d");
                return expression.gt(expressionNow);
            }else if(requestObject.getAvailableApplicationType().equals("2")){
                DateExpression<Date> expression = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", educationPlan.applyEndDateTime, "%Y-%m-%d");
                return expression.lt(expressionNow);
            }else if(requestObject.getAvailableApplicationType().equals("3")){
                DateExpression<Date> expression = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", educationPlan.applyBeginDateTime, "%Y-%m-%d");
                DateExpression<Date> expression2 = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", educationPlan.applyEndDateTime, "%Y-%m-%d");
                return expression.loe(expressionNow).and(expression2.goe(expressionNow)).or(expression2.isNull());
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    /** 조회 타입 별 포함 단어 조회 */
    private <T extends CSViewVOSupport> BooleanBuilder searchContainText(T requestObject, String containTextType, String containsText) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (hasText(containsText)) { /** 검색어가 존재할 경우 */
            if(requestObject instanceof CurriculumViewRequestVO) { /** 교육 과정 관리 */

            } else if(requestObject instanceof ExamViewRequestVO) { /** 시험 관리 */

            } else if(requestObject instanceof ExamQuestionViewRequestVO) { /** 시험 문제 관리 */
                switch (Code.CON_TEXT_TYPE.enumOfCode(((ExamQuestionViewRequestVO) requestObject).getContainTextType())) {
                    case ALL:   /** 문제 제목 + 문제 내용 */
                        booleanBuilder.or(curriculumQuestion.examQuestionMaster.questionTitle.contains(containsText));
                        booleanBuilder.or(curriculumQuestion.examQuestionMaster.questionContents.contains(containsText));
                        break;
                    case TITLE: /** 문제 제목 */
                        booleanBuilder.or(curriculumQuestion.examQuestionMaster.questionTitle.contains(containsText));
                        break;
                    case CONTENTS:  /** 문제 내용 */
                        booleanBuilder.or(curriculumQuestion.examQuestionMaster.questionContents.contains(containsText));
                        break;
                }
            } else if(requestObject instanceof EducationApplicationViewRequestVO) { /** 교육 운영 관리 */
                switch (((EducationApplicationViewRequestVO) requestObject).getContainTextType()) {
                    case "1":   /** 이름 + 아이디 */
                        booleanBuilder.or(curriculumApplicationMaster.lmsUser.userName.contains(containsText));
                        booleanBuilder.or(curriculumApplicationMaster.lmsUser.userId.contains(containsText));
                        break;
                    case "2": /** 이름 */
                        booleanBuilder.or(curriculumApplicationMaster.lmsUser.userName.contains(containsText));
                        break;
                    case "3":  /** 아이디 */
                        booleanBuilder.or(curriculumApplicationMaster.lmsUser.userId.contains(containsText));
                        break;
                }
            } else if(requestObject instanceof ReferenceRoomViewRequestVO) { /** 자료실 관리 */
                switch (((ReferenceRoomViewRequestVO) requestObject).getContainTextType()) {
                    case "전체":
                        booleanBuilder.or(curriculumReferenceRoom.title.contains(containsText));
                        booleanBuilder.or(curriculumReferenceRoom.adminUser.userName.contains(containsText));
                        break;
                    case "제목":
                        booleanBuilder.or(curriculumReferenceRoom.title.contains(containsText));
                        break;
                    case "작성자":
                        booleanBuilder.or(curriculumReferenceRoom.adminUser.userName.contains(containsText));
                        break;
                }
            }
        }
        return booleanBuilder;
    }
    /** 시간 대 검색 */
    private <T extends CSViewVOSupport> BooleanExpression betweenTime(T requestObject) {
        String startDateTime = Optional.ofNullable(requestObject.getStartDate())
                .map(date -> new StringBuilder(date).append(" 00:00:00").toString())
                .orElse("");
        String endDateTime = Optional.ofNullable(requestObject.getEndDate())
                .map(date -> new StringBuilder(date).append(" 23:59:59").toString())
                .orElse("");

        if(requestObject instanceof EducationApplicationViewRequestVO) { /** 교육 운영 관리 */
            String completeBeginDate = Optional.ofNullable(((EducationApplicationViewRequestVO) requestObject).getCompleteBeginDate())
                    .map(date -> new StringBuilder(date).append(" 00:00:00").toString())
                    .orElse("");
            String completeEndDate = Optional.ofNullable(((EducationApplicationViewRequestVO) requestObject).getCompleteEndDate())
                    .map(date -> new StringBuilder(date).append(" 23:59:59").toString())
                    .orElse("");
            if (!StringUtils.isEmpty(completeBeginDate) && !StringUtils.isEmpty(completeEndDate)) {
                BooleanExpression createDateTimeObject = null;
                if(!StringUtils.isEmpty(startDateTime) && !StringUtils.isEmpty(endDateTime)) {
                    createDateTimeObject =  curriculumApplicationMaster.createDateTime.between(startDateTime, endDateTime);
                }
                return curriculumApplicationMaster.completeDateTime.between(completeBeginDate, completeEndDate).and(createDateTimeObject);
            }
        }

        if((!StringUtils.isEmpty(startDateTime) && StringUtils.isEmpty(endDateTime))
                || (StringUtils.isEmpty(startDateTime) && !StringUtils.isEmpty(endDateTime))) {
            throw new KPFException(KPF_RESULT.ERROR9001, "조회 시작 일자 & 조회 종료 일자는 한가지만 존재할 수 없습니다.");
        } else if(!StringUtils.isEmpty(startDateTime) && !StringUtils.isEmpty(endDateTime)) {
            if(requestObject instanceof CurriculumViewRequestVO) { /** 교육 과정 관리 */
                return curriculumMaster.createDateTime.between(startDateTime, endDateTime);
            } else if(requestObject instanceof ExamViewRequestVO) { /** 시험 관리 */
                return curriculumExam.createDateTime.between(startDateTime, endDateTime);
            } else if(requestObject instanceof ExamQuestionViewRequestVO) { /** 시험 문제 관리 */
                return curriculumQuestion.createDateTime.between(startDateTime, endDateTime);
            } else if(requestObject instanceof EducationApplicationViewRequestVO) { /** 교육 운영 관리 */
                return curriculumApplicationMaster.createDateTime.between(startDateTime, endDateTime);
            } else if(requestObject instanceof ScheduleViewRequestVO) { /** 교육 과정 개설 관리 */
                return educationPlan.operationBeginDateTime.between(startDateTime, endDateTime);
            } else if (requestObject instanceof ReferenceRoomViewRequestVO) { /** 자료실 관리 */
                return curriculumReferenceRoom.createDateTime.between(startDateTime, endDateTime);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 교육 과정 일련 번호 생성
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    public String generateCurriculumCode(String prefixCode) {
        return jpaQueryFactory.selectFrom(curriculumMaster)
                .where(curriculumMaster.curriculumCode.like(prefixCode+"%"))
                .orderBy(curriculumMaster.curriculumCode.desc())
                .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                        .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getCurriculumCode().replace(prefixCode, "")) + 1), 7, "0"))
                        .toString())
                .orElse(new StringBuilder(prefixCode).append("0000000").toString());
    }

    /**
     * 시험 일련 번호 생성
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    public String generateExamSerialNo(String prefixCode) {
        return jpaQueryFactory.selectFrom(examMaster)
                .where(examMaster.examSerialNo.like(prefixCode+"%"))
                .orderBy(examMaster.examSerialNo.desc())
                .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                        .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getExamSerialNo().replace(prefixCode, "")) + 1), 7, "0"))
                        .toString())
                .orElse(new StringBuilder(prefixCode).append("0000000").toString());
    }

    /**
     * 시험 문제 일련 번호 생성
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    public String generateQuestionSerialNo(String prefixCode) {
        return jpaQueryFactory.selectFrom(examQuestionMaster)
                .where(examQuestionMaster.questionSerialNo.like(prefixCode+"%"))
                .orderBy(examQuestionMaster.questionSerialNo.desc())
                .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                        .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getQuestionSerialNo().replace(prefixCode, "")) + 1), 7, "0"))
                        .toString())
                .orElse(new StringBuilder(prefixCode).append("0000000").toString());
    }

    /**
     * 시험 문제 문항 일련 번호 생성
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    public String generateQuestionItemSerialNo(String prefixCode) {
        return jpaQueryFactory.selectFrom(examQuestionItem)
                .where(examQuestionItem.questionItemSerialNo.like(prefixCode+"%"))
                .orderBy(examQuestionItem.questionItemSerialNo.desc())
                .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                        .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getQuestionItemSerialNo().replace(prefixCode, "")) + 1), 7, "0"))
                        .toString())
                .orElse(new StringBuilder(prefixCode).append("0000000").toString());
    }

    /**
     * 교육신청 일련 번호 생성
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    public String generateApplicationNo(String prefixCode) {
        try {
            return jpaQueryFactory.selectFrom(curriculumApplicationMaster)
                    .where(curriculumApplicationMaster.applicationNo.like(prefixCode+"%"))
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .orderBy(curriculumApplicationMaster.applicationNo.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getApplicationNo().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        } catch (CannotAcquireLockException e1) {
            logger.error("{}- {}", e1.getClass().getCanonicalName(), e1.getMessage(), e1);
            throw new RuntimeException("이전 신청을 처리 중입니다. 다시 시도해주세요.");
        } catch (Exception e2) {
            logger.error("{}- {}", e2.getClass().getCanonicalName(), e2.getMessage(), e2);
            throw new RuntimeException("이전 신청을 처리 중입니다. 다시 시도해주세요.");
        }
    }

    /**
     * 교육 계획 일련 번호 생성
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    public String generateEducationPlanCode(String prefixCode) {
        return jpaQueryFactory.selectFrom(educationPlan)
                .where(educationPlan.educationPlanCode.like(prefixCode+"%"))
                .orderBy(educationPlan.educationPlanCode.desc())
                .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                        .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getEducationPlanCode().replace(prefixCode, "")) + 1), 7, "0"))
                        .toString())
                .orElse(new StringBuilder(prefixCode).append("0000000").toString());
    }

    /**
     * 일반 교육 강의 코드 생성
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    public String generateLectureCode(String prefixCode) {
        return jpaQueryFactory.selectFrom(lectureMaster)
                .where(lectureMaster.lectureCode.like(prefixCode+"%"))
                .orderBy(lectureMaster.lectureCode.desc())
                .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                        .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getLectureCode().replace(prefixCode, "")) + 1), 7, "0"))
                        .toString())
                .orElse(new StringBuilder(prefixCode).append("0000000").toString());
    }
}