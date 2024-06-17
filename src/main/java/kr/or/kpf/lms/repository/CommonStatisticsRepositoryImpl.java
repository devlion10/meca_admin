package kr.or.kpf.lms.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.or.kpf.lms.biz.statistics.adminuser.vo.StatisticsAdminUserExcelVO;
import kr.or.kpf.lms.biz.statistics.adminuser.vo.request.StatisticsAdminUserViewRequestVO;
import kr.or.kpf.lms.biz.statistics.education.vo.request.EducationStateRequestVO;
import kr.or.kpf.lms.biz.statistics.evaluation.vo.request.EvaluateCurriculumStatisticsRequestVO;
import kr.or.kpf.lms.biz.statistics.evaluation.vo.request.EvaluateQuestionStatisticsRequestVO;
import kr.or.kpf.lms.biz.statistics.evaluation.vo.response.EvaluateCurriculumStatisticsViewResponseVO;
import kr.or.kpf.lms.biz.statistics.evaluation.vo.response.EvaluateQuestionStatisticsViewResponseVO;
import kr.or.kpf.lms.biz.statistics.privacy.vo.request.PrivacyRequestVO;
import kr.or.kpf.lms.biz.statistics.privacy.vo.response.PrivacyResponseVO;
import kr.or.kpf.lms.biz.statistics.report.vo.request.ReportEducationRequestVO;
import kr.or.kpf.lms.biz.statistics.report.vo.request.ReportScheduleRequestVO;
import kr.or.kpf.lms.biz.statistics.report.vo.request.ReportUserRequestVO;
import kr.or.kpf.lms.biz.statistics.report.vo.response.ReportEducationResponseVO;
import kr.or.kpf.lms.biz.statistics.report.vo.response.ReportScheduleResponseVO;
import kr.or.kpf.lms.biz.statistics.report.vo.response.ReportUserResponseVO;
import kr.or.kpf.lms.biz.statistics.survey.vo.request.SurveyApplyStateRequestVO;
import kr.or.kpf.lms.biz.statistics.survey.vo.request.SurveyQuestionStateRequestVO;
import kr.or.kpf.lms.biz.statistics.survey.vo.response.SurveyApplyStateResponseVO;
import kr.or.kpf.lms.biz.statistics.survey.vo.response.SurveyQuestionStateResponseVO;
import kr.or.kpf.lms.biz.statistics.webuser.vo.request.UserStateRequestVO;
import kr.or.kpf.lms.biz.user.webauthority.vo.request.WebAuthorityViewRequestVO;
import kr.or.kpf.lms.biz.user.webauthority.vo.response.WebAuthorityCustomApiResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSRepositorySupport;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.ResponseSummary;
import kr.or.kpf.lms.repository.entity.BizSurvey;
import kr.or.kpf.lms.repository.entity.BizSurveyAns;
import kr.or.kpf.lms.repository.entity.BizSurveyMaster;
import kr.or.kpf.lms.repository.entity.BizSurveyQitem;
import kr.or.kpf.lms.repository.entity.education.LectureLecturer;
import kr.or.kpf.lms.repository.entity.education.LectureMaster;
import kr.or.kpf.lms.repository.entity.system.AdminMenu;
import kr.or.kpf.lms.repository.entity.user.AdminUser;
import kr.or.kpf.lms.repository.entity.user.InstructorInfo;
import kr.or.kpf.lms.repository.entity.user.LmsUser;
import kr.or.kpf.lms.repository.system.AdminMenuRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static kr.or.kpf.lms.repository.entity.QBizInstructorAply.bizInstructorAply;
import static kr.or.kpf.lms.repository.entity.QBizOrganizationAply.bizOrganizationAply;
import static kr.or.kpf.lms.repository.entity.QBizPbancMaster.bizPbancMaster;
import static kr.or.kpf.lms.repository.entity.QBizSurvey.bizSurvey;
import static kr.or.kpf.lms.repository.entity.QBizSurveyAns.bizSurveyAns;
import static kr.or.kpf.lms.repository.entity.QBizSurveyMaster.bizSurveyMaster;
import static kr.or.kpf.lms.repository.entity.QBizSurveyQitem.bizSurveyQitem;
import static kr.or.kpf.lms.repository.entity.QBizSurveyQitemItem.bizSurveyQitemItem;
import static kr.or.kpf.lms.repository.entity.education.QCurriculumApplicationEvaluate.curriculumApplicationEvaluate;
import static kr.or.kpf.lms.repository.entity.education.QCurriculumApplicationEvaluateComment.curriculumApplicationEvaluateComment;
import static kr.or.kpf.lms.repository.entity.education.QCurriculumApplicationMaster.curriculumApplicationMaster;
import static kr.or.kpf.lms.repository.entity.education.QCurriculumMaster.curriculumMaster;
import static kr.or.kpf.lms.repository.entity.education.QEducationPlan.educationPlan;
import static kr.or.kpf.lms.repository.entity.education.QLectureLecturer.lectureLecturer;
import static kr.or.kpf.lms.repository.entity.education.QLectureMaster.lectureMaster;
import static kr.or.kpf.lms.repository.entity.role.QIndividualAuthorityHistory.individualAuthorityHistory;
import static kr.or.kpf.lms.repository.entity.role.QOrganizationAuthorityHistory.organizationAuthorityHistory;
import static kr.or.kpf.lms.repository.entity.statistics.QAdminAccessHistory.adminAccessHistory;
import static kr.or.kpf.lms.repository.entity.statistics.QWebUserStateHistorySummary.webUserStateHistorySummary;
import static kr.or.kpf.lms.repository.entity.contents.QEvaluateMaster.evaluateMaster;
import static kr.or.kpf.lms.repository.entity.user.QAdminUser.adminUser;
import static kr.or.kpf.lms.repository.entity.user.QInstructorInfo.instructorInfo;
import static kr.or.kpf.lms.repository.entity.user.QLmsUser.lmsUser;
import static kr.or.kpf.lms.repository.entity.user.QOrganizationInfo.organizationInfo;
import static kr.or.kpf.lms.repository.entity.user.QOrganizationInfoMedia.organizationInfoMedia;
import static org.springframework.util.StringUtils.hasText;

/**
 * 통계 관리 공통 Repository 구현체
 */
@Repository
public class CommonStatisticsRepositoryImpl extends CSRepositorySupport implements CommonStatisticsRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired private AdminMenuRepository adminMenuRepository;

    public CommonStatisticsRepositoryImpl(JPAQueryFactory jpaQueryFactory){ this.jpaQueryFactory = jpaQueryFactory; }

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
        if(requestObject instanceof UserStateRequestVO) { /** 이용 통계 관리 */
            return jpaQueryFactory.select(webUserStateHistorySummary.count())
                    .from(webUserStateHistorySummary)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if (requestObject instanceof EvaluateQuestionStatisticsRequestVO) { /** 강의평가 통계 - 설문지별 */
            return jpaQueryFactory.select(evaluateMaster.count())
                    .from(evaluateMaster)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if (requestObject instanceof EvaluateCurriculumStatisticsRequestVO) { /** 강의평가 통계 - 강좌(과정)별 */
            List<Long> countDto = jpaQueryFactory.select(curriculumApplicationEvaluate.count())
                    .from(curriculumApplicationEvaluate)
                    .innerJoin(curriculumMaster).on(curriculumMaster.curriculumCode.eq(curriculumApplicationEvaluate.curriculumCode))
                    .where(getQuery(requestObject))
                    .groupBy(curriculumApplicationEvaluate.curriculumCode)
                    .fetch();

            Long result = Long.valueOf(0);
            for(Long count : countDto){
                result=result + 1;
            }
            return result;
        } else if (requestObject instanceof PrivacyRequestVO) { /** 개인정보 수정 이력 관리 */
            return jpaQueryFactory.select(adminAccessHistory.count())
                    .from(adminAccessHistory)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if (requestObject instanceof StatisticsAdminUserViewRequestVO) { /** 관리자 접속 이력 관리 */
            return jpaQueryFactory.select(adminAccessHistory.count())
                    .from(adminAccessHistory)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if (requestObject instanceof ReportEducationRequestVO) { /** 통계 관리 > 경영평가 보고서 - 교육별 */
            return jpaQueryFactory.select(educationPlan.count())
                    .from(educationPlan)
                    .leftJoin(curriculumMaster).on(curriculumMaster.curriculumCode.eq(educationPlan.curriculumCode))
                    .leftJoin(adminUser).on(adminUser.userId.eq(educationPlan.eduPlanPic))
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if (requestObject instanceof ReportScheduleRequestVO) { /** 통계 관리 > 경영평가 보고서 - 과정별 */
            return jpaQueryFactory.select(lectureMaster.count())
                    .from(lectureMaster)
                    .leftJoin(educationPlan).on(educationPlan.educationPlanCode.eq(lectureMaster.educationPlanCode))
                    .leftJoin(curriculumMaster).on(curriculumMaster.curriculumCode.eq(educationPlan.curriculumCode))
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if (requestObject instanceof ReportUserRequestVO) { /** 통계 관리 > 경영평가 보고서 - 신청자별 */
            return jpaQueryFactory.select(curriculumApplicationMaster.count())
                    .from(curriculumApplicationMaster)
                    .leftJoin(educationPlan).on(educationPlan.educationPlanCode.eq(curriculumApplicationMaster.educationPlanCode))
                    .leftJoin(curriculumMaster).on(curriculumMaster.curriculumCode.eq(curriculumApplicationMaster.curriculumCode))
                    .leftJoin(lmsUser).on(lmsUser.userId.eq(curriculumApplicationMaster.userId))
                    .leftJoin(organizationInfoMedia).on(organizationInfoMedia.mediaCode.eq(lmsUser.mediaCode))
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
        if(requestObject instanceof UserStateRequestVO) { /** 이용 통계 관리 */
            return jpaQueryFactory.selectFrom(webUserStateHistorySummary)
                    .where(getQuery(requestObject))
                    .orderBy(webUserStateHistorySummary.summaryDate.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        } else if (requestObject instanceof EvaluateQuestionStatisticsRequestVO) { /** 강의평가 통계 - 설문지별 */
            return jpaQueryFactory.select(Projections.fields(EvaluateQuestionStatisticsViewResponseVO.class,
                            evaluateMaster.evaluateSerialNo,
                            evaluateMaster.evaluateTitle,
                            evaluateMaster.evaluateType,
                            evaluateMaster.isUsableOtherComments))
                    .from(evaluateMaster)
                    .where(getQuery(requestObject))
                    .orderBy(evaluateMaster.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch().stream().peek(data -> {
                        data.setCountAnswer(jpaQueryFactory.select(curriculumApplicationEvaluate.count())
                                .from(curriculumApplicationEvaluate)
                                .where(curriculumApplicationEvaluate.evaluateSerialNo.eq(data.getEvaluateSerialNo()))
                                .fetchOne());
                    }).collect(Collectors.toList());
        } else if (requestObject instanceof EvaluateCurriculumStatisticsRequestVO) { /** 강의평가 통계 - 강좌(과정)별 */
            return jpaQueryFactory.select(Projections.fields(EvaluateCurriculumStatisticsViewResponseVO.class,
                            curriculumApplicationEvaluate.evaluateSerialNo,
                            curriculumMaster.curriculumCode,
                            curriculumMaster.curriculumName,
                            curriculumMaster.educationType,
                            curriculumMaster.categoryCode))
                    .from(curriculumApplicationEvaluate)
                    .innerJoin(curriculumMaster).on(curriculumMaster.curriculumCode.eq(curriculumApplicationEvaluate.curriculumCode))
                    .where(getQuery(requestObject))
                    .groupBy(curriculumApplicationEvaluate.curriculumCode)
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .orderBy(curriculumApplicationEvaluate.createDateTime.desc())
                    .fetch().stream().peek(data -> {
                        data.setCountEnd(jpaQueryFactory.select(curriculumApplicationMaster.count())
                                .from(curriculumApplicationMaster)
                                .where(curriculumApplicationMaster.isComplete.eq("Y"),
                                        curriculumApplicationMaster.curriculumCode.eq(data.getCurriculumCode()))
                                .fetchOne());

                        data.setCountAnswer(jpaQueryFactory.select(curriculumApplicationEvaluate)
                                .from(curriculumApplicationEvaluate)
                                .where(curriculumApplicationEvaluate.curriculumCode.eq(data.getCurriculumCode()))
                                .groupBy(curriculumApplicationEvaluate.applicationNo)
                                .fetch().stream().count());

                        data.setRateAnswer(String.format("%.2f", data.getCountAnswer().doubleValue() / data.getCountEnd().doubleValue() * 100));
                    }).collect(Collectors.toList());
        } else if (requestObject instanceof PrivacyRequestVO) { /** 개인정보 수정 이력 관리 */
            return jpaQueryFactory.select(Projections.fields(PrivacyResponseVO.class,
                            adminAccessHistory.adminUser.roleGroup,
                            adminAccessHistory.adminUser.userName,
                            adminAccessHistory.adminUser.userId,
                            adminAccessHistory.uri,
                            adminAccessHistory.remoteIp,
                            adminAccessHistory.httpMethod,
                            adminAccessHistory.createDateTime,
                            adminAccessHistory.queryParameter
                            ))
                    .from(adminAccessHistory)
                    .where(getQuery(requestObject))
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .orderBy(adminAccessHistory.createDateTime.desc())
                    .fetch().stream().peek(data -> {
                        data.setMenuName(adminMenuRepository.findAll().stream()
                                .sorted(Comparator.comparing(AdminMenu::getSequenceNo).reversed().thenComparing(Comparator.comparing(AdminMenu::getDepth).reversed()).thenComparing(Comparator.comparing(AdminMenu::getSort).reversed()))
                                .filter(adminMenu -> adminMenu.getUri() != null && data.getUri().contains(adminMenu.getUri()))
                                .findFirst().map(AdminMenu::getMenuName)
                                .orElse("알 수 없는 메뉴"));

                        String uri = data.getUri();
                        String query = data.getQueryParameter();
                        String targetName = "";
                        String targetId = "";

                        if (query.contains("SerialNo\":")) {
                            String[] split1 = query.split("SerialNo\":");
                            String[] split2 = split1[1].split(",");
                            String num = split2[0].replace("\"", "");

                            if (uri.contains("web")) {
                                LmsUser user = jpaQueryFactory.selectFrom(lmsUser)
                                        .where(lmsUser.userSerialNo.eq(Long.parseLong(num)))
                                        .fetchOne();
                                if (user != null) {
                                    targetName = user.getUserName();
                                    targetId = user.getUserId();
                                }
                            } else if (uri.contains("admin")) {
                                AdminUser user = jpaQueryFactory.selectFrom(adminUser)
                                        .where(adminUser.userSerialNo.eq(Long.parseLong(num)))
                                        .fetchOne();
                                if (user != null) {
                                    targetName = user.getUserName();
                                    targetId = user.getUserId();
                                }
                            } else {
                                InstructorInfo user = jpaQueryFactory.selectFrom(instructorInfo)
                                        .where(instructorInfo.instrSerialNo.eq(Long.parseLong(num)))
                                        .fetchOne();
                                if (user != null) {
                                    targetName = user.getInstrNm();
                                    targetId = user.getUserId();
                                }
                            }
                        } else if (!uri.endsWith("/") && !uri.contains("api")) {
                            String userNum = uri.substring(uri.lastIndexOf("/")+1);
                            if (uri.contains("web")) {
                                LmsUser user = jpaQueryFactory.selectFrom(lmsUser)
                                        .where(lmsUser.userSerialNo.eq(Long.parseLong(userNum)))
                                        .fetchOne();
                                if (user != null) {
                                    targetName = user.getUserName();
                                    targetId = user.getUserId();
                                }
                            } else if (uri.contains("admin")) {
                                AdminUser user = jpaQueryFactory.selectFrom(adminUser)
                                        .where(adminUser.userSerialNo.eq(Long.parseLong(userNum)))
                                        .fetchOne();
                                if (user != null) {
                                    targetName = user.getUserName();
                                    targetId = user.getUserId();
                                }
                            } else {
                                InstructorInfo user = jpaQueryFactory.selectFrom(instructorInfo)
                                        .where(instructorInfo.instrSerialNo.eq(Long.parseLong(userNum)))
                                        .fetchOne();
                                if (user != null) {
                                    targetName = user.getInstrNm();
                                    targetId = user.getUserId();
                                }
                            }
                        }

                        data.setTargetName(targetName);
                        data.setTargetId(targetId);
                    }).collect(Collectors.toList());
        } else if (requestObject instanceof StatisticsAdminUserViewRequestVO) { /** 관리자 접속 이력 관리 */
            return jpaQueryFactory.selectFrom(adminAccessHistory)
                    .where(getQuery(requestObject))
                    .orderBy(adminAccessHistory.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch().stream().peek(data -> {
                        data.setMenuFullName(adminMenuRepository.findAll().stream()
                                .sorted(Comparator.comparing(AdminMenu::getSequenceNo).reversed().thenComparing(Comparator.comparing(AdminMenu::getDepth).reversed()).thenComparing(Comparator.comparing(AdminMenu::getSort).reversed()))
                                .filter(adminMenu -> adminMenu.getUri() != null && data.getUri().contains(adminMenu.getUri()))
                                .findFirst().map(AdminMenu::getMenuFullName)
                                .orElse("알 수 없는 메뉴"));
                    }).collect(Collectors.toList());
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
        if(requestObject instanceof UserStateRequestVO) { /** 이용 통계 관리 */
            return jpaQueryFactory.selectFrom(webUserStateHistorySummary)
                    .where(getQuery(requestObject))
                    .orderBy(webUserStateHistorySummary.summaryDate.desc())
                    .fetch();
        } else if(requestObject instanceof EducationStateRequestVO) { /** 학습운영 통계 */
            return jpaQueryFactory.selectFrom(educationPlan)
                    .leftJoin(curriculumMaster).on(curriculumMaster.curriculumCode.eq(educationPlan.curriculumCode))
                    .where(getQuery(requestObject))
                    .orderBy(educationPlan.createDateTime.desc())
                    .fetch();
        } else if (requestObject instanceof EvaluateQuestionStatisticsRequestVO) { /** 강의평가 통계 - 설문지별 */
            return jpaQueryFactory.select(Projections.fields(EvaluateQuestionStatisticsViewResponseVO.class,
                            evaluateMaster.evaluateSerialNo,
                            evaluateMaster.evaluateTitle,
                            evaluateMaster.evaluateType,
                            evaluateMaster.isUsableOtherComments))
                    .from(evaluateMaster)
                    .where(getQuery(requestObject))
                    .orderBy(evaluateMaster.createDateTime.desc())
                    .fetch().stream().peek(data -> {
                        data.setCountAnswer(jpaQueryFactory.select(curriculumApplicationEvaluateComment.count())
                                .from(curriculumApplicationEvaluateComment)
                                .where(curriculumApplicationEvaluateComment.evaluateSerialNo.eq(data.getEvaluateSerialNo()))
                                .fetchOne());
                    }).collect(Collectors.toList());
        } else if (requestObject instanceof EvaluateCurriculumStatisticsRequestVO) { /** 강의평가 통계 - 강좌(과정)별 */
            List<EvaluateCurriculumStatisticsViewResponseVO> dtos = jpaQueryFactory.select(Projections.fields(EvaluateCurriculumStatisticsViewResponseVO.class,
                            curriculumApplicationEvaluate.evaluateSerialNo,
                            curriculumMaster.curriculumCode,
                            curriculumMaster.curriculumName,
                            curriculumMaster.educationType,
                            curriculumMaster.categoryCode))
                    .from(curriculumApplicationEvaluate)
                    .innerJoin(curriculumMaster).on(curriculumMaster.curriculumCode.eq(curriculumApplicationEvaluate.curriculumCode))
                    .where(getQuery(requestObject))
                    .groupBy(curriculumApplicationEvaluate.curriculumCode)
                    .orderBy(curriculumApplicationEvaluate.createDateTime.desc())
                    .fetch().stream().peek(data -> {
                        data.setCountEnd(jpaQueryFactory.select(curriculumApplicationMaster.count())
                                .from(curriculumApplicationMaster)
                                .where(curriculumApplicationMaster.isComplete.eq("Y"))
                                .fetchOne());

                        data.setCountAnswer(jpaQueryFactory.select(curriculumApplicationEvaluate)
                                .from(curriculumApplicationEvaluate)
                                .where(curriculumApplicationEvaluate.curriculumCode.eq(data.getCurriculumCode()))
                                .groupBy(curriculumApplicationEvaluate.applicationNo)
                                .fetch().stream().count());

                        data.setRateAnswer(String.format("%.2f", data.getCountAnswer().doubleValue() / data.getCountEnd().doubleValue() * 100));
                    }).collect(Collectors.toList());
            return dtos;

        } else if (requestObject instanceof SurveyApplyStateRequestVO) { /** 상호평가 통계 - 신청서별 */
            List<SurveyApplyStateResponseVO> dtos = new ArrayList<>();
            ((SurveyApplyStateRequestVO) requestObject).setTargetTable(1);/** 강사(가) 평가 */
            List<SurveyApplyStateResponseVO> instructors = jpaQueryFactory.select(Projections.fields(SurveyApplyStateResponseVO.class,
                            bizSurveyAns.bizSurveyNo,
                            bizSurveyAns.bizSurveyTrgtNo,
                            bizSurvey.bizSurveyCtgr,
                            bizSurvey.bizSurveyNm,
                            bizPbancMaster.bizPbancNm,
                            bizSurvey.bizSurveyStts,
                            bizSurveyAns.registUserId.as("evaluatorId"),
                            lmsUser.userName.as("evaluatorNm"),
                            organizationInfo.organizationName.as("evaluated"),
                            bizSurveyAns.bizSurveyAnsEtc.as("comment"),
                            bizOrganizationAply.createDateTime
                    ))
                    .from(bizSurveyAns)
                    .innerJoin(bizSurvey).on(bizSurvey.bizSurveyNo.eq(bizSurveyAns.bizSurveyNo))
                    .innerJoin(bizOrganizationAply).on(bizOrganizationAply.bizOrgAplyNo.eq(bizSurveyAns.bizSurveyTrgtNo))
                    .innerJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                    .innerJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                    .innerJoin(lmsUser).on(lmsUser.userId.eq(bizSurveyAns.registUserId))
                    .where(getQuery(requestObject))
                    .groupBy(bizSurveyAns.bizSurveyTrgtNo, bizSurveyAns.registUserId)
                    .fetch().stream().peek(instructor -> {
                        instructor.setBizSurveyCtgrNm("강사평가");

                        List<BizSurveyAns> bizSurveyAnss = jpaQueryFactory.selectFrom(bizSurveyAns)
                                .where(bizSurveyAns.bizSurveyTrgtNo.eq(instructor.getBizSurveyTrgtNo()),
                                        bizSurveyAns.registUserId.eq(instructor.getEvaluatorId()))
                                .fetch();

                        double average = 0.0;
                        Integer score = 0;
                        Integer count = 0;
                        String comment = "";
                        String content = "";
                        if (bizSurveyAnss != null && bizSurveyAnss.size() > 0) {
                            for (BizSurveyAns surveyAns : bizSurveyAnss) {
                                if (!surveyAns.getBizSurveyAnsEtc().isEmpty())
                                    comment = comment + '\n' + surveyAns.getBizSurveyAnsEtc();

                                BizSurveyQitem surveyQitem = jpaQueryFactory.selectFrom(bizSurveyQitem)
                                        .where(bizSurveyQitem.bizSurveyQitemNo.eq(surveyAns.getBizSurveyQitemNo()))
                                        .fetchOne();
                                if (surveyQitem.getBizSurveyQitemType() == 0) {
                                    Integer num = Integer.valueOf(surveyAns.getBizSurveyAnsCn());
                                    score = score + num;
                                    count = count + 1;
                                } else if (surveyQitem.getBizSurveyQitemType() != 0) {
                                    content = content + '\n' + '[' + surveyQitem.getBizSurveyQitemName() + ']' + surveyAns.getBizSurveyAnsCn();
                                }
                            }
                        }

                        if (count > 0) {
                            average = score / count;
                            instructor.setScore(average);
                        }
                        instructor.setContent(content);
                        instructor.setComment(comment);
                    }).collect(Collectors.toList());
            dtos.addAll(instructors);

            ((SurveyApplyStateRequestVO) requestObject).setTargetTable(2);/** 기관(이) 평가 */
            List<SurveyApplyStateResponseVO> organizations = jpaQueryFactory.select(Projections.fields(SurveyApplyStateResponseVO.class,
                            bizSurveyAns.bizSurveyNo,
                            bizSurveyAns.bizSurveyTrgtNo,
                            bizSurvey.bizSurveyCtgr,
                            bizSurvey.bizSurveyNm,
                            bizPbancMaster.bizPbancNm,
                            bizSurvey.bizSurveyStts,
                            bizSurveyAns.registUserId.as("evaluatorId"),
                            organizationInfo.organizationName.as("evaluatorNm"),
                            bizInstructorAply.bizInstrAplyInstrNm.as("evaluated"),
                            bizSurveyAns.bizSurveyAnsEtc.as("comment"),
                            bizOrganizationAply.createDateTime
                    ))
                    .from(bizSurveyAns)
                    .innerJoin(bizSurvey).on(bizSurvey.bizSurveyNo.eq(bizSurveyAns.bizSurveyNo))
                    .innerJoin(bizInstructorAply).on(bizInstructorAply.bizInstrAplyNo.eq(bizSurveyAns.bizSurveyTrgtNo))
                    .innerJoin(bizOrganizationAply).on(bizOrganizationAply.bizOrgAplyNo.eq(bizInstructorAply.bizOrgAplyNo))
                    .innerJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                    .innerJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                    .innerJoin(lmsUser).on(lmsUser.userId.eq(bizSurveyAns.registUserId))
                    .where(getQuery(requestObject))
                    .groupBy(bizSurveyAns.bizSurveyTrgtNo)
                    .fetch().stream().peek(organization -> {
                        organization.setBizSurveyCtgrNm("기관평가");

                        List<BizSurveyAns> bizSurveyAnss = jpaQueryFactory.selectFrom(bizSurveyAns)
                                .where(bizSurveyAns.bizSurveyTrgtNo.eq(organization.getBizSurveyTrgtNo()),
                                        bizSurveyAns.registUserId.eq(organization.getEvaluatorId()))
                                .fetch();

                        double average = 0.0;
                        Integer score = 0;
                        Integer count = 0;
                        String comment = "";
                        String content = "";
                        if (bizSurveyAnss != null && bizSurveyAnss.size() > 0) {
                            for (BizSurveyAns surveyAns : bizSurveyAnss) {
                                if (!surveyAns.getBizSurveyAnsEtc().isEmpty())
                                    comment = comment + '\n' + surveyAns.getBizSurveyAnsEtc();

                                BizSurveyQitem surveyQitem = jpaQueryFactory.selectFrom(bizSurveyQitem)
                                        .where(bizSurveyQitem.bizSurveyQitemNo.eq(surveyAns.getBizSurveyQitemNo()))
                                        .fetchOne();
                                if (surveyQitem.getBizSurveyQitemType() == 0) {
                                    Integer num = Integer.valueOf(surveyAns.getBizSurveyAnsCn());
                                    score = score + num;
                                    count = count + 1;
                                } else if (surveyQitem.getBizSurveyQitemEtc() == 1) {
                                    content = content + '\n' + '[' + surveyQitem.getBizSurveyQitemName() + ']' + surveyAns.getBizSurveyAnsCn();
                                }
                            }
                        }

                        if (count > 0) {
                            average = score / count;
                            organization.setScore(average);
                        }
                        organization.setContent(content);
                        organization.setComment(comment);
                    }).collect(Collectors.toList());
            dtos.addAll(organizations);

            dtos.sort(Comparator.comparing(SurveyApplyStateResponseVO::getCreateDateTime).reversed());
            return dtos;

        } else if (requestObject instanceof StatisticsAdminUserViewRequestVO) { /** 관리자 접속 이력 관리 */
            return jpaQueryFactory.select(Projections.fields(StatisticsAdminUserExcelVO.class,
                            adminAccessHistory.adminUser.userId,
                            adminAccessHistory.adminUser.userName,
                            adminAccessHistory.uri,
                            adminAccessHistory.remoteIp))
                    .from(adminAccessHistory)
                    .where(getQuery(requestObject))
                    .orderBy(adminAccessHistory.createDateTime.desc())
                    .fetch().stream().peek(data -> {
                        data.setMenuFullName(adminMenuRepository.findAll().stream()
                                .sorted(Comparator.comparing(AdminMenu::getSequenceNo).reversed().thenComparing(Comparator.comparing(AdminMenu::getDepth).reversed()).thenComparing(Comparator.comparing(AdminMenu::getSort).reversed()))
                                .filter(adminMenu -> adminMenu.getUri() != null && data.getUri().contains(adminMenu.getUri()))
                                .findFirst().map(AdminMenu::getMenuFullName)
                                .orElse("알 수 없는 메뉴"));
                    }).collect(Collectors.toList());
        } else if (requestObject instanceof PrivacyRequestVO) { /** 개인정보 수정 이력 관리 */
            return jpaQueryFactory.select(Projections.fields(PrivacyResponseVO.class,
                            adminAccessHistory.adminUser.roleGroup,
                            adminAccessHistory.adminUser.userName,
                            adminAccessHistory.adminUser.userId,
                            adminAccessHistory.uri,
                            adminAccessHistory.remoteIp,
                            adminAccessHistory.httpMethod,
                            adminAccessHistory.createDateTime,
                            adminAccessHistory.queryParameter
                    ))
                    .from(adminAccessHistory)
                    .where(getQuery(requestObject))
                    .orderBy(adminAccessHistory.createDateTime.desc())
                    .fetch().stream().peek(data -> {
                        data.setMenuName(adminMenuRepository.findAll().stream()
                                .sorted(Comparator.comparing(AdminMenu::getSequenceNo).reversed().thenComparing(Comparator.comparing(AdminMenu::getDepth).reversed()).thenComparing(Comparator.comparing(AdminMenu::getSort).reversed()))
                                .filter(adminMenu -> adminMenu.getUri() != null && data.getUri().contains(adminMenu.getUri()))
                                .findFirst().map(AdminMenu::getMenuName)
                                .orElse("알 수 없는 메뉴"));

                        String uri = data.getUri();
                        String query = data.getQueryParameter();
                        String targetName = "";
                        String targetId = "";

                        if (query.contains("SerialNo\":")) {
                            String[] split1 = query.split("SerialNo\":");
                            String[] split2 = split1[1].split(",");

                            if (uri.contains("web")) {
                                LmsUser user = jpaQueryFactory.selectFrom(lmsUser)
                                        .where(lmsUser.userSerialNo.eq(Long.parseLong(split2[0])))
                                        .fetchOne();
                                if (user != null) {
                                    targetName = user.getUserName();
                                    targetId = user.getUserId();
                                }
                            } else if (uri.contains("admin")) {
                                AdminUser user = jpaQueryFactory.selectFrom(adminUser)
                                        .where(adminUser.userSerialNo.eq(Long.parseLong(split2[0])))
                                        .fetchOne();
                                if (user != null) {
                                    targetName = user.getUserName();
                                    targetId = user.getUserId();
                                }
                            } else {
                                InstructorInfo user = jpaQueryFactory.selectFrom(instructorInfo)
                                        .where(instructorInfo.instrSerialNo.eq(Long.parseLong(split2[0])))
                                        .fetchOne();
                                if (user != null) {
                                    targetName = user.getInstrNm();
                                    targetId = user.getUserId();
                                }
                            }
                        } else if (!uri.endsWith("/") && !uri.contains("api")) {
                            String userNum = uri.substring(uri.lastIndexOf("/")+1);
                            if (uri.contains("web")) {
                                LmsUser user = jpaQueryFactory.selectFrom(lmsUser)
                                        .where(lmsUser.userSerialNo.eq(Long.parseLong(userNum)))
                                        .fetchOne();
                                if (user != null) {
                                    targetName = user.getUserName();
                                    targetId = user.getUserId();
                                }
                            } else if (uri.contains("admin")) {
                                AdminUser user = jpaQueryFactory.selectFrom(adminUser)
                                        .where(adminUser.userSerialNo.eq(Long.parseLong(userNum)))
                                        .fetchOne();
                                if (user != null) {
                                    targetName = user.getUserName();
                                    targetId = user.getUserId();
                                }
                            } else {
                                InstructorInfo user = jpaQueryFactory.selectFrom(instructorInfo)
                                        .where(instructorInfo.instrSerialNo.eq(Long.parseLong(userNum)))
                                        .fetchOne();
                                if (user != null) {
                                    targetName = user.getInstrNm();
                                    targetId = user.getUserId();
                                }
                            }
                        }

                        data.setTargetName(targetName);
                        data.setTargetId(targetId);
                    }).collect(Collectors.toList());
        } else if (requestObject instanceof ReportEducationRequestVO) { /** 통계 관리 > 경영평가 보고서 - 교육별 */
            return jpaQueryFactory.select(Projections.fields(ReportEducationResponseVO.class,
                            curriculumMaster.enforcementType,
                            curriculumMaster.managerDepartment,
                            curriculumMaster.managerName,
                            curriculumMaster.categoryCode,
                            curriculumMaster.educationType,
                            curriculumMaster.curriculumName,
                            educationPlan.province,
                            educationPlan.educationPlanCode,
                            educationPlan.operationBeginDateTime,
                            educationPlan.operationEndDateTime
                    ))
                    .from(educationPlan)
                    .leftJoin(curriculumMaster).on(curriculumMaster.curriculumCode.eq(educationPlan.curriculumCode))
                    .leftJoin(adminUser).on(adminUser.userId.eq(curriculumMaster.managerDepartment))
                    .where(getQuery(requestObject))
                    .orderBy(educationPlan.createDateTime.desc())
                    .fetch().stream().peek(data -> {
                        data.setCountUser(jpaQueryFactory.select(curriculumApplicationMaster.count())
                                .from(curriculumApplicationMaster)
                                .where(curriculumApplicationMaster.educationPlanCode.eq(data.getEducationPlanCode()))
                                .fetchOne());

                        data.setCountEndUser(jpaQueryFactory.select(curriculumApplicationMaster.count())
                                .from(curriculumApplicationMaster)
                                .where(curriculumApplicationMaster.educationPlanCode.eq(data.getEducationPlanCode()),
                                        curriculumApplicationMaster.isComplete.eq("Y"))
                                .fetchOne());

                        data.setCountUserOfParallel(jpaQueryFactory.select(curriculumApplicationMaster.count())
                                .from(curriculumApplicationMaster)
                                .leftJoin(curriculumMaster).on(curriculumMaster.curriculumCode.eq(curriculumApplicationMaster.curriculumCode))
                                .where(curriculumApplicationMaster.educationPlanCode.eq(data.getEducationPlanCode()),
                                        curriculumApplicationMaster.isComplete.eq("Y"),
                                        curriculumApplicationMaster.setEducationType.eq("2"))
                                .fetchOne());
                    }).collect(Collectors.toList());
        } else if (requestObject instanceof ReportScheduleRequestVO) { /** 통계 관리 > 경영평가 보고서 - 과정별 */
            return jpaQueryFactory.select(Projections.fields(ReportScheduleResponseVO.class,
                            curriculumMaster.managerDepartment,
                            curriculumMaster.managerName,
                            curriculumMaster.categoryCode,
                            curriculumMaster.educationType,
                            curriculumMaster.curriculumName,
                            educationPlan.educationPlanCode,
                            educationPlan.province,
                            educationPlan.operationBeginDateTime,
                            educationPlan.operationEndDateTime,
                            lectureMaster.lectureCode,
                            lectureMaster.lectureTitle
                    ))
                    .from(lectureMaster)
                    .leftJoin(educationPlan).on(educationPlan.educationPlanCode.eq(lectureMaster.educationPlanCode))
                    .leftJoin(curriculumMaster).on(curriculumMaster.curriculumCode.eq(educationPlan.curriculumCode))
                    .where(getQuery(requestObject))
                    .orderBy(educationPlan.createDateTime.desc())
                    .fetch().stream().peek(data -> {
                        data.setCountUser(jpaQueryFactory.select(curriculumApplicationMaster.count())
                                .from(curriculumApplicationMaster)
                                .where(curriculumApplicationMaster.educationPlanCode.eq(data.getEducationPlanCode()),
                                        curriculumApplicationMaster.isComplete.eq("Y"))
                                .fetchOne());

                        LectureMaster master = jpaQueryFactory.selectFrom(lectureMaster)
                                .where(lectureMaster.lectureCode.eq(data.getLectureCode()))
                                .fetchOne();

                        if (master.getOperationBeginDateTime().substring(0,10).equals(master.getOperationEndDateTime().substring(0,10))) {
                            data.setLectureDate(master.getOperationBeginDateTime().substring(0,10));
                        } else {
                            data.setLectureDate(master.getOperationBeginDateTime().substring(0,10)+"~"+master.getOperationEndDateTime().substring(0,10));
                        }
                        data.setLectureTime(master.getOperationBeginDateTime().substring(11,16)+"~"+master.getOperationEndDateTime().substring(11,16));

                        /** 수업 시수 구하기 */
                        /** A와 B의 시간을 LocalTime 객체로 생성 */
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        LocalDateTime timeA = LocalDateTime.parse(master.getOperationBeginDateTime().substring(0,16), formatter);
                        LocalDateTime timeB = LocalDateTime.parse(master.getOperationEndDateTime().substring(0,16), formatter);
                        /** 시간 차이 계산 */
                        Duration duration = Duration.between(timeA, timeB);
                        /** 시간 차이를 소수점 형태로 변환하여 출력 */
                        double diffHours = duration.toMinutes() / 60.0;
                        data.setEachTime(diffHours);

                        double totalTime = 0.0;
                        List<LectureMaster> lectureMasterList = jpaQueryFactory.selectFrom(lectureMaster)
                                .where(lectureMaster.educationPlanCode.eq(data.getEducationPlanCode()))
                                .fetch();
                        if (lectureMasterList != null && lectureMasterList.size() > 0) {
                            for (LectureMaster entity : lectureMasterList) {
                                /** A와 B의 시간을 LocalTime 객체로 생성 */
                                LocalDateTime start = LocalDateTime.parse(entity.getOperationBeginDateTime().substring(0,16), formatter);
                                LocalDateTime end = LocalDateTime.parse(entity.getOperationEndDateTime().substring(0,16), formatter);
                                /** 시간 차이 계산 */
                                Duration between = Duration.between(start, end);
                                /** 시간 차이를 소수점 형태로 변환하여 출력 */
                                double diffHour = between.toMinutes() / 60.0;
                                totalTime = totalTime + diffHour;
                            }
                            data.setTotalTime(totalTime);
                        }

                        /** 강사정보 */
                        if (master.getLectureLecturerList() != null && master.getLectureLecturerList().size() > 0) {
                            String lecturerNames = "";
                            String lecturerOrgNames = "";
                            List<LectureLecturer> lectureLecturerList = jpaQueryFactory.selectFrom(lectureLecturer)
                                    .where(lectureLecturer.lectureCode.eq(data.getLectureCode()))
                                    .fetch();
                            if (lectureLecturerList != null && lectureLecturerList.size() > 0) {
                                for (LectureLecturer lecturer : lectureLecturerList) {
                                    lecturerNames = new StringBuilder(lecturerNames)
                                            .append(lecturer.getLecturerInfo().getInstrNm()).append("\n").toString();
                                    lecturerOrgNames = new StringBuilder(lecturerOrgNames)
                                            .append(lecturer.getLecturerInfo().getOrgName()).append("\n").toString();
                                }
                            }
                            data.setInstrNm(lecturerNames);
                            data.setOrgName(lecturerOrgNames);
                        } else {
                            data.setInstrNm(master.getLecturerInfo().getInstrNm());
                            data.setOrgName(master.getLecturerInfo().getOrgName());
                        }

                    }).collect(Collectors.toList());
        } else if (requestObject instanceof ReportUserRequestVO) { /** 통계 관리 > 경영평가 보고서 - 신청자별 */
            return jpaQueryFactory.select(Projections.fields(ReportUserResponseVO.class,
                            curriculumMaster.enforcementType,
                            curriculumMaster.managerDepartment,
                            curriculumMaster.managerName,
                            curriculumMaster.categoryCode,
                            curriculumMaster.educationType,
                            curriculumMaster.curriculumName,
                            curriculumApplicationMaster.setEducationType,
                            educationPlan.educationPlanCode,
                            educationPlan.province,
                            educationPlan.operationBeginDateTime,
                            educationPlan.operationEndDateTime,
                            organizationInfoMedia.mediaName,
                            organizationInfoMedia.mediaClsName1,
                            organizationInfoMedia.mediaClsName2,
                            lmsUser.userName,
                            lmsUser.phone,
                            lmsUser.email,
                            lmsUser.department,
                            lmsUser.rank,
                            lmsUser.birthDay,
                            lmsUser.gender
                    ))
                    .from(curriculumApplicationMaster)
                    .leftJoin(educationPlan).on(educationPlan.educationPlanCode.eq(curriculumApplicationMaster.educationPlanCode))
                    .leftJoin(curriculumMaster).on(curriculumMaster.curriculumCode.eq(curriculumApplicationMaster.curriculumCode))
                    .leftJoin(lmsUser).on(lmsUser.userId.eq(curriculumApplicationMaster.userId))
                    .leftJoin(organizationInfoMedia).on(organizationInfoMedia.mediaCode.eq(lmsUser.mediaCode))
                    .where(getQuery(requestObject))
                    .orderBy(curriculumApplicationMaster.createDateTime.desc())
                    .fetch();
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * where 절
     */
    private <T extends CSViewVOSupport> Predicate[] getQuery(T requestObject) {
        if(requestObject instanceof UserStateRequestVO) { /** 이용 통계 */
            if(!StringUtils.isEmpty(((UserStateRequestVO) requestObject).getSearchYear()) &&
                    StringUtils.isEmpty(((UserStateRequestVO) requestObject).getSearchMonth())){ /** 검색 월이 비어있으면 연 기준으로 검색(연간 집계 데이터) */
                return new Predicate[]{ condition(((UserStateRequestVO) requestObject).getSearchYear(), webUserStateHistorySummary.summaryDate::contains)};
            } else if(!StringUtils.isEmpty(((UserStateRequestVO) requestObject).getSearchYear())) { /** 검색 월이 있으면 월 기준으로 검색(월간 집계 데이터) */
                return new Predicate[]{condition(((UserStateRequestVO) requestObject).getSearchYear() +
                                StringUtils.leftPad(((UserStateRequestVO) requestObject).getSearchMonth(), 2, "0"),
                        webUserStateHistorySummary.summaryDate::contains)};
            } else {
                return new Predicate[]{betweenTime(requestObject)};
            }
        } else if (requestObject instanceof EducationStateRequestVO) { /** 학습운영 통계 */
            return new Predicate[] { betweenTime(requestObject),
                    condition(((EducationStateRequestVO) requestObject).getEducationType(), curriculumMaster.educationType::eq),
                    condition(((EducationStateRequestVO) requestObject).getCategoryCode(), curriculumMaster.categoryCode::eq),
                    condition(((EducationStateRequestVO) requestObject).getEnforcementType(), curriculumMaster.enforcementType::eq) };
        } else if (requestObject instanceof EvaluateQuestionStatisticsRequestVO) { /** 강의평가 통계 - 설문지별 */
            return new Predicate[] { condition(((EvaluateQuestionStatisticsRequestVO) requestObject).getEvaluateTitle(), evaluateMaster.evaluateTitle::contains),
                    condition(((EvaluateQuestionStatisticsRequestVO) requestObject).getEvaluateType(), evaluateMaster.evaluateType::eq)};
        } else if (requestObject instanceof EvaluateCurriculumStatisticsRequestVO) { /** 강의평가 통계 - 강의(과정)별 */
            return new Predicate[] { condition(((EvaluateCurriculumStatisticsRequestVO) requestObject).getEducationType(), curriculumMaster.educationType::eq),
                    condition(((EvaluateCurriculumStatisticsRequestVO) requestObject).getCategoryCode(), curriculumMaster.categoryCode::eq),
                    condition(((EvaluateCurriculumStatisticsRequestVO) requestObject).getCurriculumName(), curriculumMaster.curriculumName::eq)};
        } else if (requestObject instanceof SurveyApplyStateRequestVO) { /** 상호평가 통계 - 신청서별 */
            return new Predicate[] { condition(((SurveyApplyStateRequestVO) requestObject).getTargetTable(), bizSurvey.bizSurveyCtgr::eq) };
        } else if (requestObject instanceof StatisticsAdminUserViewRequestVO) { /** 관리자 접속 이력 */
            return new Predicate[] { betweenTime(requestObject),
                    condition("ANONYMOUS", adminAccessHistory.registUserId::ne),
                    searchContainText(requestObject, ((StatisticsAdminUserViewRequestVO) requestObject).getContainTextType(), ((StatisticsAdminUserViewRequestVO) requestObject).getContainText()) };
        } else if (requestObject instanceof PrivacyRequestVO) { /** 개인정보 수정 이력 */
            return new Predicate[] { betweenTime(requestObject),
                    adminAccessHistory.uri.contains("/user/web-user/").or(adminAccessHistory.uri.contains("/user/admin-user/")).or(adminAccessHistory.uri.contains("/user/instructor/")),
                    condition(((PrivacyRequestVO) requestObject).getTarget(), adminAccessHistory.queryParameter::contains),
                    searchContainText(requestObject, ((PrivacyRequestVO) requestObject).getContainTextType(), ((PrivacyRequestVO) requestObject).getContainText())};
        } else if (requestObject instanceof ReportEducationRequestVO) { /** 통계 관리 > 경영평가 보고서 - 교육별 */
            return new Predicate[] { condition(((ReportEducationRequestVO) requestObject).getYear(), educationPlan.yearOfEducationPlan::eq),
                    betweenTime(requestObject),
                    condition(((ReportEducationRequestVO) requestObject).getProvince(), educationPlan.province::eq),
                    condition(((ReportEducationRequestVO) requestObject).getCategory(), curriculumMaster.categoryCode::eq),
                    condition(((ReportEducationRequestVO) requestObject).getType(), curriculumMaster.educationType::eq),
                    condition(((ReportEducationRequestVO) requestObject).getCName(), curriculumMaster.curriculumName::contains),
                    condition(((ReportEducationRequestVO) requestObject).getMName(), curriculumMaster.managerName::contains) };
        } else if (requestObject instanceof ReportScheduleRequestVO) { /** 통계 관리 > 경영평가 보고서 - 과정별 */
            return new Predicate[] { condition(((ReportScheduleRequestVO) requestObject).getYear(), educationPlan.yearOfEducationPlan::eq),
                    betweenTime(requestObject),
                    condition(((ReportScheduleRequestVO) requestObject).getProvince(), educationPlan.province::eq),
                    condition(((ReportScheduleRequestVO) requestObject).getCategory(), curriculumMaster.categoryCode::eq),
                    condition(((ReportScheduleRequestVO) requestObject).getType(), curriculumMaster.educationType::eq),
                    condition(((ReportScheduleRequestVO) requestObject).getCName(), curriculumMaster.curriculumName::contains),
                    condition(((ReportScheduleRequestVO) requestObject).getMName(), curriculumMaster.managerName::contains) };
        } else if (requestObject instanceof ReportUserRequestVO) { /** 통계 관리 > 경영평가 보고서 - 신청자별 */
            return new Predicate[] { condition(((ReportUserRequestVO) requestObject).getYear(), educationPlan.yearOfEducationPlan::eq),
                    betweenTime(requestObject),
                    condition("Y", curriculumApplicationMaster.isComplete::eq),
                    condition(((ReportUserRequestVO) requestObject).getProvince(), educationPlan.province::eq),
                    condition(((ReportUserRequestVO) requestObject).getCategory(), curriculumMaster.categoryCode::eq),
                    condition(((ReportUserRequestVO) requestObject).getType(), curriculumMaster.educationType::eq),
                    condition(((ReportUserRequestVO) requestObject).getUserName(), lmsUser.userName::eq),
                    condition(((ReportUserRequestVO) requestObject).getOrgName(), organizationInfoMedia.mediaName::contains),
                    condition(((ReportUserRequestVO) requestObject).getCName(), curriculumMaster.curriculumName::contains),
                    condition(((ReportUserRequestVO) requestObject).getMName(), curriculumMaster.managerName::contains) };
        } else {
            return new Predicate[]{};
        }
    }

    /** 조회 타입 별 포함 단어 조회 */
    private <T extends CSViewVOSupport> BooleanBuilder searchContainText(T requestObject, String containTextType, String containsText) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (hasText(containsText)) { /** 검색어가 존재할 경우 */
            if (requestObject instanceof StatisticsAdminUserViewRequestVO) { /** 관리자 접속 이력 */
                switch (((StatisticsAdminUserViewRequestVO) requestObject).getContainTextType()) {
                    case "0":   /** 이름 + 아이디 */
                        booleanBuilder.or(adminAccessHistory.adminUser.userName.eq(containsText));
                        booleanBuilder.or(adminAccessHistory.adminUser.userId.contains(containsText));
                        break;
                    case "1": /** 이름 */
                        booleanBuilder.or(adminAccessHistory.adminUser.userName.contains(containsText));
                        break;
                    case "2":  /** 아이디 */
                        booleanBuilder.or(adminAccessHistory.adminUser.userId.contains(containsText));
                        break;
                }
            } else if (requestObject instanceof PrivacyRequestVO) { /** 개인정보 수정이력 */
                booleanBuilder.or(adminAccessHistory.adminUser.userName.contains(containsText));
                booleanBuilder.or(adminAccessHistory.adminUser.userId.contains(containsText));
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

        if((!StringUtils.isEmpty(startDateTime) && StringUtils.isEmpty(endDateTime))
                || (StringUtils.isEmpty(startDateTime) && !StringUtils.isEmpty(endDateTime))) {
            throw new KPFException(KPF_RESULT.ERROR9001, "조회 시작 일자 & 조회 종료 일자는 한가지만 존재할 수 없습니다.");
        } else if(!StringUtils.isEmpty(startDateTime) && !StringUtils.isEmpty(endDateTime)) {
            if (requestObject instanceof UserStateRequestVO) { /** 이용 통계 */
                return webUserStateHistorySummary.createDateTime.between(startDateTime, endDateTime);
            } else if (requestObject instanceof EducationStateRequestVO) { /** 학습운영 통계 */
                return educationPlan.createDateTime.between(startDateTime, endDateTime);
            } else if (requestObject instanceof StatisticsAdminUserViewRequestVO) { /** 관리자 접속 이력 관리 */
                return adminAccessHistory.createDateTime.between(startDateTime, endDateTime);
            } else if (requestObject instanceof PrivacyRequestVO) { /** 관리자 접속 이력 관리 */
                return adminAccessHistory.createDateTime.between(startDateTime, endDateTime);
            } else if (requestObject instanceof ReportEducationRequestVO) { /** 통계 관리 > 경영평가 보고서 - 교육별 */
                return educationPlan.operationBeginDateTime.between(startDateTime, endDateTime);
            } else if (requestObject instanceof ReportScheduleRequestVO) { /** 통계 관리 > 경영평가 보고서 - 과정별 */
                return educationPlan.operationBeginDateTime.between(startDateTime, endDateTime);
            } else if (requestObject instanceof ReportUserRequestVO) { /** 통계 관리 > 경영평가 보고서 - 신청자별 */
                return educationPlan.operationBeginDateTime.between(startDateTime, endDateTime);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
