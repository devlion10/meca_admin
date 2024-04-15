package kr.or.kpf.lms.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.business.apply.vo.request.BizAplyViewRequestVO;
import kr.or.kpf.lms.biz.business.apply.vo.response.BizAplyExcelVO;
import kr.or.kpf.lms.biz.business.instructor.clcln.ddln.vo.request.BizInstructorClclnDdlnViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.clcln.ddln.vo.response.BizInstructorClclnDdlnExcelVO;
import kr.or.kpf.lms.biz.business.instructor.dist.vo.request.BizInstructorDistViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.dist.vo.response.BizInstructorDistExcelVO;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.request.BizInstructorIdentifyViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.response.BizInstructorIdentifyExcelVO;
import kr.or.kpf.lms.biz.business.instructor.vo.request.BizInstructorViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.vo.response.BizInstructorExcelVO;
import kr.or.kpf.lms.biz.business.instructor.vo.response.BizInstructorTestExcelVO;
import kr.or.kpf.lms.biz.business.organization.aply.vo.request.BizOrganizationAplyCustomViewRequestVO;
import kr.or.kpf.lms.biz.business.organization.aply.vo.request.BizOrganizationAplyViewRequestVO;
import kr.or.kpf.lms.biz.business.organization.aply.vo.response.BizOrganizationAplyDistExcelVO;
import kr.or.kpf.lms.biz.business.organization.aply.vo.response.BizOrganizationAplyExcelVO;
import kr.or.kpf.lms.biz.business.organization.rpt.vo.request.BizOrganizationRsltRptViewRequestVO;
import kr.or.kpf.lms.biz.business.organization.rpt.vo.response.BizOrganizationRsltRptExcelVO;
import kr.or.kpf.lms.biz.business.pbanc.master.vo.request.BizPbancViewRequestVO;
import kr.or.kpf.lms.biz.business.pbanc.master.vo.response.BizPbancCustomApiResponseVO;
import kr.or.kpf.lms.biz.business.survey.qitem.vo.request.BizSurveyQitemViewRequestVO;
import kr.or.kpf.lms.biz.business.survey.qitem.vo.response.BizSurveyQitemExcelVO;
import kr.or.kpf.lms.biz.business.survey.vo.request.BizSurveyViewRequestVO;
import kr.or.kpf.lms.biz.business.survey.vo.response.BizSurveyExcelVO;
import kr.or.kpf.lms.biz.homepage.archive.guide.classsubject.vo.request.ClassSubjectViewRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.request.ArchiveClassGuideExcelRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.request.ArchiveClassGuideViewRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.response.ArchiveClassGuideExcelResponseVO;
import kr.or.kpf.lms.biz.homepage.banner.vo.request.BannerViewRequestVO;
import kr.or.kpf.lms.biz.homepage.eduplace.vo.request.EduPlaceViewRequestVO;
import kr.or.kpf.lms.biz.homepage.event.vo.request.EventViewRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.data.vo.request.ArchiveViewRequestVO;
import kr.or.kpf.lms.biz.homepage.myqna.vo.request.MyQnaViewRequestVO;
import kr.or.kpf.lms.biz.homepage.notice.vo.request.NoticeViewRequestVO;
import kr.or.kpf.lms.biz.homepage.page.vo.request.PageViewRequestVO;
import kr.or.kpf.lms.biz.homepage.popup.vo.request.PopupViewRequestVO;
import kr.or.kpf.lms.biz.homepage.press.vo.request.PressViewRequestVO;
import kr.or.kpf.lms.biz.homepage.review.vo.request.ReviewViewRequestVO;
import kr.or.kpf.lms.biz.homepage.sms.vo.request.SmsViewRequestVO;
import kr.or.kpf.lms.biz.homepage.suggestion.vo.request.SuggestionViewRequestVO;
import kr.or.kpf.lms.biz.homepage.topqna.vo.request.TopQnaViewRequestVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSEntitySupport;
import kr.or.kpf.lms.common.support.CSRepositorySupport;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import kr.or.kpf.lms.common.support.Code;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.ResponseSummary;
import kr.or.kpf.lms.repository.entity.*;
import kr.or.kpf.lms.repository.entity.homepage.*;
import kr.or.kpf.lms.repository.entity.user.AdminUser;
import kr.or.kpf.lms.repository.entity.user.LmsUser;
import kr.or.kpf.lms.repository.user.AdminUserRepository;
import kr.or.kpf.lms.repository.user.LmsUserRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kr.or.kpf.lms.repository.entity.QBizAply.bizAply;
import static kr.or.kpf.lms.repository.entity.QBizInstructor.bizInstructor;
import static kr.or.kpf.lms.repository.entity.QBizInstructorAply.bizInstructorAply;
import static kr.or.kpf.lms.repository.entity.QBizInstructorAsgnm.bizInstructorAsgnm;
import static kr.or.kpf.lms.repository.entity.QBizInstructorClclnDdln.bizInstructorClclnDdln;
import static kr.or.kpf.lms.repository.entity.QBizInstructorDist.bizInstructorDist;
import static kr.or.kpf.lms.repository.entity.QBizInstructorIdentify.bizInstructorIdentify;
import static kr.or.kpf.lms.repository.entity.QBizInstructorIdentifyDtl.bizInstructorIdentifyDtl;
import static kr.or.kpf.lms.repository.entity.QBizInstructorPbanc.bizInstructorPbanc;
import static kr.or.kpf.lms.repository.entity.QBizOrganizationAply.bizOrganizationAply;
import static kr.or.kpf.lms.repository.entity.QBizOrganizationAplyDtl.bizOrganizationAplyDtl;
import static kr.or.kpf.lms.repository.entity.QBizOrganizationRsltRpt.bizOrganizationRsltRpt;
import static kr.or.kpf.lms.repository.entity.QBizPbancMaster.bizPbancMaster;
import static kr.or.kpf.lms.repository.entity.QBizPbancResult.bizPbancResult;
import static kr.or.kpf.lms.repository.entity.QBizSurvey.bizSurvey;
import static kr.or.kpf.lms.repository.entity.QBizSurveyMaster.bizSurveyMaster;
import static kr.or.kpf.lms.repository.entity.QBizSurveyQitem.bizSurveyQitem;
import static kr.or.kpf.lms.repository.entity.QFileMaster.fileMaster;
import static kr.or.kpf.lms.repository.entity.homepage.QClassGuide.classGuide;
import static kr.or.kpf.lms.repository.entity.homepage.QClassGuideSubject.classGuideSubject;
import static kr.or.kpf.lms.repository.entity.homepage.QClassSubject.classSubject;
import static kr.or.kpf.lms.repository.entity.homepage.QClassGuideFile.classGuideFile;
import static kr.or.kpf.lms.repository.entity.homepage.QDocuments.documents;
import static kr.or.kpf.lms.repository.entity.homepage.QEducationSuggestion.educationSuggestion;
import static kr.or.kpf.lms.repository.entity.homepage.QEducationReview.educationReview;
import static kr.or.kpf.lms.repository.entity.homepage.QEventInfo.eventInfo;
import static kr.or.kpf.lms.repository.entity.homepage.QHomeBanner.homeBanner;
import static kr.or.kpf.lms.repository.entity.homepage.QHomePopup.homePopup;
import static kr.or.kpf.lms.repository.entity.homepage.QPressRelease.pressRelease;
import static kr.or.kpf.lms.repository.entity.homepage.QEduPlaceAply.eduPlaceAply;
import static kr.or.kpf.lms.repository.entity.homepage.QLmsData.lmsData;
import static kr.or.kpf.lms.repository.entity.homepage.QMyQna.myQna;
import static kr.or.kpf.lms.repository.entity.homepage.QNotice.notice;
import static kr.or.kpf.lms.repository.entity.homepage.QSmsSendMaster.smsSendMaster;
import static kr.or.kpf.lms.repository.entity.homepage.QTopQna.topQna;
import static kr.or.kpf.lms.repository.entity.user.QLmsUser.lmsUser;
import static kr.or.kpf.lms.repository.entity.user.QOrganizationInfo.organizationInfo;
import static org.springframework.util.StringUtils.delimitedListToStringArray;
import static org.springframework.util.StringUtils.hasText;

/**
 * 홈페이지 관리 공통 Repository 구현체
 */
@Repository
public class CommonHomepageRepositoryImpl extends CSRepositorySupport implements CommonHomepageRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CommonHomepageRepositoryImpl(JPAQueryFactory jpaQueryFactory){ this.jpaQueryFactory = jpaQueryFactory; }
    @Autowired private LmsUserRepository lmsUserRepository;
    @Autowired private AdminUserRepository adminUserRepository;

    /**
     * 공통 리스트 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    @Override
    public <T extends CSViewVOSupport> CSPageImpl <?> findEntityList(T requestObject) {
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

    /** Excel 조회 */
    @Override
    public <T extends CSViewVOSupport> List<?> findEntityListExcel(T requestObject) {
        return getEntityListExcel(requestObject);
    }

    /**
     * Entity 총 갯수
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    private <T extends CSViewVOSupport> Long getEntityCount(T requestObject) {
        if(requestObject instanceof NoticeViewRequestVO) { /** 공지사항 */
            return jpaQueryFactory.select(notice.count())
                    .from(notice)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof TopQnaViewRequestVO) { /** 자주묻는 질문 */
            return jpaQueryFactory.select(topQna.count())
                    .from(topQna)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof MyQnaViewRequestVO) { /** 1:1 문의 */
            return jpaQueryFactory.select(myQna.count())
                    .from(myQna)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof SuggestionViewRequestVO) { /** 교육 주제 제안 */
            return jpaQueryFactory.select(educationSuggestion.count())
                    .from(educationSuggestion)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof ReviewViewRequestVO) { /** 교육 후기 */
            return jpaQueryFactory.select(educationReview.count())
                    .from(educationReview)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof ArchiveClassGuideViewRequestVO) { /** 수업지도안 */
            return jpaQueryFactory.select(classGuide.count())
                    .from(classGuide)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof ClassSubjectViewRequestVO) { /** 수업지도안 교과 */
            return jpaQueryFactory.select(classSubject.count())
                    .from(classSubject)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof PageViewRequestVO) { /** 페이지 */
            return jpaQueryFactory.select(documents.count())
                    .from(documents)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof EventViewRequestVO) { /** 이벤트/설문 */
            return jpaQueryFactory.select(eventInfo.count())
                    .from(eventInfo)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof PressViewRequestVO) { /** 행사소개 */
            return jpaQueryFactory.select(pressRelease.count())
                    .from(pressRelease)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof EduPlaceViewRequestVO) { /** 교육장 신청 이력 */
            return jpaQueryFactory.select(eduPlaceAply.count())
                    .from(eduPlaceAply)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof BannerViewRequestVO) { /** 배너 */
            return jpaQueryFactory.select(homeBanner.count())
                    .from(homeBanner)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof PopupViewRequestVO) { /** 팝업 */
            return jpaQueryFactory.select(homePopup.count())
                    .from(homePopup)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof ArchiveViewRequestVO) { /** 자료실 */
            return jpaQueryFactory.select(lmsData.count())
                    .from(lmsData)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof SmsViewRequestVO) { /** SMS 관리 */
            return jpaQueryFactory.select(smsSendMaster.count())
                    .from(smsSendMaster)
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
        if(requestObject instanceof NoticeViewRequestVO) { /** 공지사항 */
            List<Notice> entities = jpaQueryFactory.selectFrom(notice)
                .where(getQuery(requestObject))
                .orderBy(notice.isTop.desc(), notice.createDateTime.desc())
                .offset(requestObject.getPageable().getOffset())
                .limit(requestObject.getPageable().getPageSize()).fetch().stream()
                .peek(data -> data.setIsNew(new DateTime().minusDays(15).compareTo(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
                .parseDateTime(data.getCreateDateTime())) < 0)).collect(Collectors.toList());

            if (!entities.isEmpty() && entities.size() > 0) {
                for (Notice entity : entities) {
                    entity.setFileMasters(jpaQueryFactory.selectFrom(fileMaster)
                            .where(fileMaster.atchFileSn.eq(entity.getNoticeSerialNo()))
                            .fetch());
                }
            }
            return entities;

        } else if(requestObject instanceof TopQnaViewRequestVO) { /** 자주묻는 질문 */
            return jpaQueryFactory.selectFrom(topQna)
                    .where(getQuery(requestObject))
                    .orderBy(topQna.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        } else if(requestObject instanceof MyQnaViewRequestVO) { /** 1:1 문의 */
            return jpaQueryFactory.selectFrom(myQna)
                    .where(getQuery(requestObject))
                    .orderBy(myQna.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        } else if(requestObject instanceof SuggestionViewRequestVO) { /** 교육 주제 제안 */
            return jpaQueryFactory.selectFrom(educationSuggestion)
                    .where(getQuery(requestObject))
                    .orderBy(educationSuggestion.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch().stream().peek(data -> {
                        if (lmsUserRepository.findOne(Example.of(LmsUser.builder().userId(data.getRegistUserId()).build())).isPresent()) {
                            data.setSuggestUser(lmsUserRepository.findOne(Example.of(LmsUser.builder().userId(data.getRegistUserId()).build())).get().getUserName());
                        } else {
                            if (adminUserRepository.findOne(Example.of(AdminUser.builder().userId(data.getRegistUserId()).build())).isPresent()) {
                                data.setSuggestUser(adminUserRepository.findOne(Example.of(AdminUser.builder().userId(data.getRegistUserId()).build())).get().getUserName());
                            } else {
                                data.setSuggestUser("-");
                            }
                        }

                        if (data.getCommentUserId() != null) {
                            if (lmsUserRepository.findOne(Example.of(LmsUser.builder().userId(data.getCommentUserId()).build())).isPresent()) {
                                data.setCommentUser(lmsUserRepository.findOne(Example.of(LmsUser.builder().userId(data.getCommentUserId()).build())).get().getUserName());
                            } else {
                                if (adminUserRepository.findOne(Example.of(AdminUser.builder().userId(data.getCommentUserId()).build())).isPresent()) {
                                    data.setCommentUser(adminUserRepository.findOne(Example.of(AdminUser.builder().userId(data.getCommentUserId()).build())).get().getUserName());
                                } else {
                                    data.setCommentUser("-");
                                }
                            }
                        }
                    }).collect(Collectors.toList());
        } else if(requestObject instanceof ReviewViewRequestVO) { /** 교육 후기 */
            return jpaQueryFactory.selectFrom(educationReview)
                    .where(getQuery(requestObject))
                    .orderBy(educationReview.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();

        } else if(requestObject instanceof ArchiveClassGuideViewRequestVO) { /** 수업 지도안 */
            List<ClassGuide> entities = jpaQueryFactory.selectFrom(classGuide)
                    .where(getQuery(requestObject))
                    .orderBy(classGuide.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();

            if (!entities.isEmpty() && entities.size() > 0) {
                for (ClassGuide entity : entities) {
                    List<ClassGuideFile> guideFileList = jpaQueryFactory.selectFrom(classGuideFile)
                            .where(classGuideFile.classGuideCode.eq(entity.getClassGuideCode()), classGuideFile.fileType.eq(Code.GUI_FILE_TYPE.TEACH.enumCode))
                            .fetch();
                    if (guideFileList.size() > 0 && !guideFileList.isEmpty())
                        entity.setGuideFileList(guideFileList);

                    List<ClassGuideFile> activityFileList = jpaQueryFactory.selectFrom(classGuideFile)
                            .where(classGuideFile.classGuideCode.eq(entity.getClassGuideCode()), classGuideFile.fileType.eq(Code.GUI_FILE_TYPE.ACTIVITY.enumCode))
                            .fetch();
                    if (activityFileList.size() > 0 && !activityFileList.isEmpty())
                        entity.setActivityFileList(activityFileList);

                    List<ClassGuideFile> answerFileList = jpaQueryFactory.selectFrom(classGuideFile)
                            .where(classGuideFile.classGuideCode.eq(entity.getClassGuideCode()), classGuideFile.fileType.eq(Code.GUI_FILE_TYPE.ANSWER.enumCode))
                            .fetch();
                    if (answerFileList.size() > 0 && !answerFileList.isEmpty())
                        entity.setAnswerFileList(answerFileList);

                    List<ClassGuideFile> nieFileList = jpaQueryFactory.selectFrom(classGuideFile)
                            .where(classGuideFile.classGuideCode.eq(entity.getClassGuideCode()), classGuideFile.fileType.eq(Code.GUI_FILE_TYPE.NIE.enumCode))
                            .fetch();
                    if (nieFileList.size() > 0 && !nieFileList.isEmpty())
                        entity.setNieFileList(nieFileList);

                    LmsUser user = jpaQueryFactory.selectFrom(lmsUser)
                            .where(lmsUser.userId.eq(entity.getRegistUserId()))
                            .fetchOne();
                    if (user != null)
                        entity.setUserName(user.getUserName());
                    else
                        entity.setUserName("(사용자)");

                    if(entity.getReferenceSubject() != null) {
                        List<ClassSubject> subjects = new ArrayList<>();
                        String[] list = entity.getReferenceSubject().split(",");

                        for (String str : list) {
                            subjects.add(jpaQueryFactory.selectFrom(classSubject)
                                    .where(classSubject.individualCode.eq(str))
                                    .fetchOne());
                        }

                        if (subjects.size() > 0 && !subjects.isEmpty()) {
                            int n = 0;
                            for (ClassSubject subject : subjects) {
                                List<String> subjectStrList = new ArrayList<>();
                                String code = subject.getIndividualCode();

                                for (int i=0; i < subject.getDepth(); i++) {
                                    ClassSubject subjectSub = jpaQueryFactory.selectFrom(classSubject)
                                            .where(classSubject.individualCode.eq(code))
                                            .orderBy(classSubject.individualCode.desc())
                                            .fetchOne();

                                    if (subjectSub != null && !subjectSub.equals(null)) {
                                        code = subjectSub.getUpIndividualCode();
                                        subjectStrList.add(subjectSub.getContent());
                                    }
                                }

                                if (subjectStrList.size() > 0 && !subjectStrList.isEmpty()) {
                                    for (int i = subjectStrList.size() - 1; i >= 0; i--) {
                                        if (i == subjectStrList.size()-1) {
                                            code = new StringBuilder("").append(subjectStrList.get(i)).toString();
                                        } else {
                                            code = new StringBuilder(code).append(">").append(subjectStrList.get(i)).toString();
                                        }
                                    }
                                }
                                subjects.get(n).setCodeInfo(code);
                                n++;
                            }
                            entity.setClassSubjectList(subjects);
                        }
                    }
                }
            }
            return entities;

        } else if(requestObject instanceof ClassSubjectViewRequestVO) { /** 수업지도안 교과 */
            return jpaQueryFactory.selectFrom(classSubject)
                    .where(getQuery(requestObject))
                    .orderBy(classSubject.order.asc())
                    .fetch();
        } else if(requestObject instanceof PageViewRequestVO) { /** 페이지 */
            return jpaQueryFactory.selectFrom(documents)
                    .where(getQuery(requestObject))
                    .orderBy(documents.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        } else if(requestObject instanceof EventViewRequestVO) { /** 이벤트/설문 */
            return jpaQueryFactory.selectFrom(eventInfo)
                    .where(getQuery(requestObject))
                    .orderBy(eventInfo.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        } else if(requestObject instanceof PressViewRequestVO) { /** 행사소개 */
            return jpaQueryFactory.selectFrom(pressRelease)
                    .where(getQuery(requestObject))
                    .orderBy(pressRelease.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        } else if(requestObject instanceof EduPlaceViewRequestVO) { /** 교육장 장소 신청 */
            return jpaQueryFactory.selectFrom(eduPlaceAply)
                    .where(getQuery(requestObject))
                    .orderBy(eduPlaceAply.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        } else if(requestObject instanceof BannerViewRequestVO) { /** 배너 */
            return jpaQueryFactory.selectFrom(homeBanner)
                    .where(getQuery(requestObject))
                    .orderBy(homeBanner.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        } else if(requestObject instanceof PopupViewRequestVO) { /** 팝업 */
            return jpaQueryFactory.selectFrom(homePopup)
                    .where(getQuery(requestObject))
                    .orderBy(homePopup.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        } else if(requestObject instanceof ArchiveViewRequestVO) { /** 자료실 */
            return jpaQueryFactory.selectFrom(lmsData)
                    .where(getQuery(requestObject))
                    .orderBy(lmsData.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        } else if(requestObject instanceof SmsViewRequestVO) { /** SMS 관리 */
            return jpaQueryFactory.selectFrom(smsSendMaster)
                    .where(getQuery(requestObject))
                    .orderBy(smsSendMaster.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Entity 리스트
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    private <T extends CSViewVOSupport> List<?> getEntityListExcel(T requestObject) {
        if(requestObject instanceof ArchiveClassGuideExcelRequestVO) { /** 사업 공고 */
            List<ArchiveClassGuideExcelResponseVO> dtos = jpaQueryFactory.select(Projections.fields(ArchiveClassGuideExcelResponseVO.class,
                            classGuide.classGuideCode,
                            classGuideFile.sequenceNo,
                            classGuide.classGuideType,
                            classGuide.target,
                            classGuide.title,
                            classGuide.eNIEYn,
                            classGuide.registUserId,
                            classGuide.createDateTime,
                            classGuideFile.originalFileName,
                            classGuide.viewCount
                    ))
                    .from(classGuideFile)
                    .leftJoin(classGuide).on(classGuide.classGuideCode.eq(classGuideFile.classGuideCode))
                    .where(getQuery(requestObject))
                    .orderBy(classGuideFile.createDateTime.asc())
                    .fetch().stream().peek(dto -> {
                        if (dto.getClassGuideType() != null && !dto.getClassGuideType().isEmpty()) {
                            if (dto.getClassGuideType().equals("1")) {
                                dto.setClassGuideType("수업지도안(교사)");
                            } else if (dto.getClassGuideType().equals("2")) {
                                dto.setClassGuideType("수업지도안(학부모)");
                            } else if (dto.getClassGuideType().equals("3")) {
                                dto.setClassGuideType("수업지도안(기타)");
                            }
                        }

                        if (dto.getRegistUserId() != null && !dto.getRegistUserId().isEmpty()) {
                            String userName = jpaQueryFactory.select(lmsUser.userName)
                                    .from(lmsUser)
                                    .where(lmsUser.userId.eq(dto.getRegistUserId()))
                                    .fetchOne();

                            if (userName != null && !userName.isEmpty()) {
                                dto.setUserName(userName);
                            } else {
                                dto.setUserName("(사용자)");
                            }
                        }

                        BigInteger downloadCount = jpaQueryFactory.select(classGuideFile.viewCount)
                                .from(classGuideFile)
                                .where(classGuideFile.sequenceNo.eq(dto.getSequenceNo()))
                                .fetchOne();
                        dto.setDownloadCount(downloadCount);

                    }).collect(Collectors.toList());
            return dtos;

        } else {
            return null;
        }
    }

    /**
     * where 절
     */
    private <T extends CSViewVOSupport> Predicate[] getQuery(T requestObject) {
        if(requestObject instanceof NoticeViewRequestVO) {/** 공지 사항 */
            return new Predicate[] { betweenTime(requestObject),
                    condition(((NoticeViewRequestVO) requestObject).getNoticeSerialNo(), notice.noticeSerialNo::eq),
                    condition(((NoticeViewRequestVO) requestObject).getNoticeType(), notice.noticeType::eq),
                    searchContainText(requestObject, ((NoticeViewRequestVO) requestObject).getContainText())};
        } else if(requestObject instanceof TopQnaViewRequestVO) { /** 자주묻는 질문 */
            return new Predicate[] { betweenTime(requestObject),
                    condition(((TopQnaViewRequestVO) requestObject).getSequenceNo(), topQna.sequenceNo::eq),
                    searchContainText(requestObject, ((TopQnaViewRequestVO) requestObject).getContainText())};
        } else if(requestObject instanceof MyQnaViewRequestVO) { /** 1:1 문의 */
            return new Predicate[] { betweenTime(requestObject),
                    condition(((MyQnaViewRequestVO) requestObject).getSequenceNo(), myQna.sequenceNo::eq),
                    condition(((MyQnaViewRequestVO) requestObject).getUserId(), myQna.registUserId::eq),
                    condition(((MyQnaViewRequestVO) requestObject).getUserName(), myQna.lmsUser.userName::contains),
                    searchContainText(requestObject, ((MyQnaViewRequestVO) requestObject).getContainText())};
        } else if(requestObject instanceof SuggestionViewRequestVO) { /** 교육 주제 제안 */
            return new Predicate[] { betweenTime(requestObject),
                    condition(((SuggestionViewRequestVO) requestObject).getSuggestionType(), educationSuggestion.suggestionType::eq)};
        } else if(requestObject instanceof ReviewViewRequestVO) { /** 교육 후기 */
            String[] types = null;
            if (((ReviewViewRequestVO) requestObject).getTypes() != null && !((ReviewViewRequestVO) requestObject).getTypes().isEmpty()) {
                types = ((ReviewViewRequestVO) requestObject).getTypes().split(",");
            }
            return new Predicate[] { betweenTime(requestObject),
                    condition(((ReviewViewRequestVO) requestObject).getSequenceNo(), educationReview.sequenceNo::eq),
                    condition(((ReviewViewRequestVO) requestObject).getCategory(), educationReview.category::eq),
                    condition(types, educationReview.type::in),
                    condition(((ReviewViewRequestVO) requestObject).getUserId(), educationReview.adminUser.userId::contains),
                    condition(((ReviewViewRequestVO) requestObject).getUserName(), educationReview.adminUser.userName::contains),
                    searchContainText(requestObject, ((ReviewViewRequestVO) requestObject).getContainText())};
        } else if(requestObject instanceof ArchiveClassGuideViewRequestVO) { /** 수업지도안 */
            String[] targets = null;
            if (((ArchiveClassGuideViewRequestVO) requestObject).getTargets() != null && !((ArchiveClassGuideViewRequestVO) requestObject).getTargets().isEmpty()) {
                targets = ((ArchiveClassGuideViewRequestVO) requestObject).getTargets().split(",");
            }

            String[] targetGrades = null;
            if (((ArchiveClassGuideViewRequestVO) requestObject).getTargetGrades() != null && !((ArchiveClassGuideViewRequestVO) requestObject).getTargetGrades().isEmpty()) {
                targetGrades = ((ArchiveClassGuideViewRequestVO) requestObject).getTargetGrades().split(",");
            }

            String[] years = null;
            if (((ArchiveClassGuideViewRequestVO) requestObject).getYears() != null && !((ArchiveClassGuideViewRequestVO) requestObject).getYears().isEmpty()) {
                years = ((ArchiveClassGuideViewRequestVO) requestObject).getYears().split(",");
            }

            String[] subjects = null;
            String[] classGuides = null;
            if (((ArchiveClassGuideViewRequestVO) requestObject).getSubjects() != null && !((ArchiveClassGuideViewRequestVO) requestObject).getSubjects().isEmpty()) {
                subjects = ((ArchiveClassGuideViewRequestVO) requestObject).getSubjects().split(",");

                List<String> classSubjectCodes = new ArrayList<>();
                for (int i=0; i<subjects.length; i++) {
                    classSubjectCodes.addAll(jpaQueryFactory.select(classSubject.individualCode)
                            .from(classSubject)
                            .where(classSubject.content.contains(subjects[i]))
                            .fetch());
                }

                String[] classSubjects = classSubjectCodes.toArray(new String[classSubjectCodes.size()]);
                List<String> classGuideCodes = jpaQueryFactory.select(classGuideSubject.classGuideCode)
                        .from(classGuideSubject)
                        .where(classGuideSubject.individualCode.in(classSubjects))
                        .fetch().stream().distinct().collect(Collectors.toList());
                classGuides = classGuideCodes.toArray(new String[classGuideCodes.size()]);
            }

            return new Predicate[] { betweenTime(requestObject),
                    condition(((ArchiveClassGuideViewRequestVO) requestObject).getClassGuideCode(), classGuide.classGuideCode::eq),
                    condition(((ArchiveClassGuideViewRequestVO) requestObject).getClassGuideType(), classGuide.classGuideType::eq),
                    condition(targets, classGuide.target::in),
                    condition(targetGrades, classGuide.targetGrade::in),
                    condition(years, classGuide.createDateTime.substring(0,4)::in),/**  */
                    condition(classGuides, classGuide.classGuideCode::in),/** 수업지도안의 관련교생성 일자의 년도와 비교과 코드의 내용과 비교 */
                    searchContainText(requestObject, ((ArchiveClassGuideViewRequestVO) requestObject).getContainText())};
        } else if(requestObject instanceof ArchiveClassGuideExcelRequestVO) { /** 수업지도안 엑셀 */
            String[] targets = null;
            if (((ArchiveClassGuideExcelRequestVO) requestObject).getTargets() != null && !((ArchiveClassGuideExcelRequestVO) requestObject).getTargets().isEmpty()) {
                targets = ((ArchiveClassGuideExcelRequestVO) requestObject).getTargets().split(",");
            }

            String[] targetGrades = null;
            if (((ArchiveClassGuideExcelRequestVO) requestObject).getTargetGrades() != null && !((ArchiveClassGuideExcelRequestVO) requestObject).getTargetGrades().isEmpty()) {
                targetGrades = ((ArchiveClassGuideExcelRequestVO) requestObject).getTargetGrades().split(",");
            }

            String[] years = null;
            if (((ArchiveClassGuideExcelRequestVO) requestObject).getYears() != null && !((ArchiveClassGuideExcelRequestVO) requestObject).getYears().isEmpty()) {
                years = ((ArchiveClassGuideExcelRequestVO) requestObject).getYears().split(",");
            }

            String[] subjects = null;
            String[] classGuides = null;
            if (((ArchiveClassGuideExcelRequestVO) requestObject).getSubjects() != null && !((ArchiveClassGuideExcelRequestVO) requestObject).getSubjects().isEmpty()) {
                subjects = ((ArchiveClassGuideExcelRequestVO) requestObject).getSubjects().split(",");

                List<String> classSubjectCodes = new ArrayList<>();
                for (int i=0; i<subjects.length; i++) {
                    classSubjectCodes.addAll(jpaQueryFactory.select(classSubject.individualCode)
                            .from(classSubject)
                            .where(classSubject.content.contains(subjects[i]))
                            .fetch());
                }
                String[] classSubjects = classSubjectCodes.toArray(new String[classSubjectCodes.size()]);
                List<String> classGuideCodes = jpaQueryFactory.select(classGuideSubject.classGuideCode)
                        .from(classGuideSubject)
                        .where(classGuideSubject.individualCode.in(classSubjects))
                        .fetch().stream().distinct().collect(Collectors.toList());
                classGuides = classGuideCodes.toArray(new String[classGuideCodes.size()]);
            }

            return new Predicate[] { betweenTime(requestObject),
                    condition(((ArchiveClassGuideExcelRequestVO) requestObject).getClassGuideCode(), classGuide.classGuideCode::eq),
                    condition(((ArchiveClassGuideExcelRequestVO) requestObject).getClassGuideType(), classGuide.classGuideType::eq),
                    condition(targets, classGuide.target::in),
                    condition(targetGrades, classGuide.targetGrade::in),
                    condition(years, classGuide.createDateTime.substring(0,4)::in),/** 생성 일자의 년도와 비교 */
                    condition(classGuides, classGuide.classGuideCode::in),/** 수업지도안의 관련교과 코드의 내용과 비교 */
                    searchContainText(requestObject, ((ArchiveClassGuideExcelRequestVO) requestObject).getContainText())};
        } else if(requestObject instanceof ClassSubjectViewRequestVO) { /** 수업지도안 - 교과 */
            return new Predicate[] { betweenTime(requestObject),
                    condition(((ClassSubjectViewRequestVO) requestObject).getContent(), classSubject.content::eq),
                    condition(((ClassSubjectViewRequestVO) requestObject).getRegistUserId(), classSubject.registUserId::eq),
                    searchContainText(requestObject, ((ClassSubjectViewRequestVO) requestObject).getContainText())};
        } else if(requestObject instanceof PageViewRequestVO) { /** 페이지 */
            return new Predicate[] { betweenTime(requestObject),
                    condition(((PageViewRequestVO) requestObject).getSequenceNo(), documents.sequenceNo::eq),
                    condition(((PageViewRequestVO) requestObject).getDocumentType(), documents.documentType::eq)};
        } else if(requestObject instanceof EventViewRequestVO) { /** 이벤트/설문 */
                return new Predicate[] { betweenTime(requestObject),
                        condition(((EventViewRequestVO) requestObject).getSequenceNo(), eventInfo.sequenceNo::eq),
                        condition(((EventViewRequestVO) requestObject).getStatus(), eventInfo.status::eq),
                        condition(((EventViewRequestVO) requestObject).getType(), eventInfo.type::eq),
                        checkDate(new EventInfo(), ((EventViewRequestVO) requestObject).getStartDt(), "goe"),
                        checkDate(new EventInfo(), ((EventViewRequestVO) requestObject).getEndDt(), "loe"),
                        searchContainText(requestObject, ((EventViewRequestVO) requestObject).getContainText())};
        } else if(requestObject instanceof PressViewRequestVO) { /** 행사소개 */
            return new Predicate[] { betweenTime(requestObject),
                    condition(((PressViewRequestVO) requestObject).getSequenceNo(), pressRelease.sequenceNo::eq),
                    searchContainText(requestObject, ((PressViewRequestVO) requestObject).getContainText())};
        } else if(requestObject instanceof EduPlaceViewRequestVO) { /** 교육장 신청 이력 */
            return new Predicate[] { betweenTime(requestObject),
                    condition(((EduPlaceViewRequestVO) requestObject).getSequenceNo(), eduPlaceAply.sequenceNo::eq),
                    condition(((EduPlaceViewRequestVO) requestObject).getAplyUserNm(), eduPlaceAply.aplyUserNm::eq),
                    condition(((EduPlaceViewRequestVO) requestObject).getAplyPhone(), eduPlaceAply.aplyPhone::eq),
                    condition(((EduPlaceViewRequestVO) requestObject).getUserId(), eduPlaceAply.registUserId::eq),
                    searchContainText(requestObject, ((EduPlaceViewRequestVO) requestObject).getContainText())};
        } else if(requestObject instanceof BannerViewRequestVO) { /** 배너 */
            return new Predicate[] { betweenTime(requestObject),
                    condition(((BannerViewRequestVO) requestObject).getBannerSn(), homeBanner.bannerSn::eq),
                    condition(((BannerViewRequestVO) requestObject).getStatus(), homeBanner.bannerStts::eq),
                    searchContainText(requestObject, ((BannerViewRequestVO) requestObject).getContainText())};
        } else if(requestObject instanceof PopupViewRequestVO) { /** 팝업 */
            return new Predicate[] { betweenTime(requestObject),
                    condition(((PopupViewRequestVO) requestObject).getPopupSn(), homePopup.popupSn::eq),
                    condition(((PopupViewRequestVO) requestObject).getStatus(), homePopup.popupStts::eq),
                    condition(((PopupViewRequestVO) requestObject).getContainText(), homePopup.title::contains),

                    checkDate(new HomePopup(), ((PopupViewRequestVO) requestObject).getStartDt(), "goe"),
                    checkDate(new HomePopup(), ((PopupViewRequestVO) requestObject).getEndDt(), "loe"),

                    searchContainText(requestObject, ((PopupViewRequestVO) requestObject).getContainText())};
        } else if(requestObject instanceof ArchiveViewRequestVO) { /** 자료실 */
            String[] types = null;
            if (((ArchiveViewRequestVO) requestObject).getTypes() != null && !((ArchiveViewRequestVO) requestObject).getTypes().isEmpty()) {
                types = ((ArchiveViewRequestVO) requestObject).getTypes().split(",");
            }

            return new Predicate[] { betweenTime(requestObject),
                    condition(((ArchiveViewRequestVO) requestObject).getSequenceNo(), lmsData.sequenceNo::eq),
                    condition(((ArchiveViewRequestVO) requestObject).getTeamCategory(), lmsData.teamCategory::eq),
                    condition(((ArchiveViewRequestVO) requestObject).getMaterialCategory(), lmsData.materialCategory::eq),
                    condition(types, lmsData.materialType::in),
                    searchContainText(requestObject, ((ArchiveViewRequestVO) requestObject).getContainText())};
        } else if (requestObject instanceof SmsViewRequestVO) { /** SMS 관리 */
            return new Predicate[] { betweenTime(requestObject),
                    condition(((SmsViewRequestVO) requestObject).getContent(), smsSendMaster.content::contains),
                    condition(((SmsViewRequestVO) requestObject).getSender(), smsSendMaster.sender::eq)};
        } else {
                return new Predicate[] {};
        }
    }

    /** 조회 타입 별 포함 단어 조회 */
    private <T extends CSViewVOSupport> BooleanBuilder searchContainText(T requestObject, String containsText) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (hasText(containsText)) { /** 검색어가 존재할 경우 */
            if(requestObject instanceof NoticeViewRequestVO) {/** 공지사항 포함 단어 조회 */
                switch (Code.CON_TEXT_TYPE.enumOfCode(((NoticeViewRequestVO) requestObject).getContainTextType())) {
                    case ALL:   /** 제목 + 내용 */
                        booleanBuilder.or(notice.title.contains(containsText));
                        booleanBuilder.or(notice.contents.contains(containsText));
                        break;
                    case TITLE: /** 제목 */
                        booleanBuilder.or(notice.title.contains(containsText));
                        break;
                    case CONTENTS:  /** 내용 */
                        booleanBuilder.or(notice.contents.contains(containsText));
                        break;
                }
            } else if(requestObject instanceof TopQnaViewRequestVO) { /** 자주묻는 질문 */
                switch (Code.CON_TEXT_TYPE.enumOfCode(((TopQnaViewRequestVO) requestObject).getContainTextType())) {
                    case ALL:   /** 질문 + 답변 */
                        booleanBuilder.or(topQna.question.contains(containsText));
                        booleanBuilder.or(topQna.answer.contains(containsText));
                        break;
                    case TITLE: /** 질문 */
                        booleanBuilder.or(topQna.question.contains(containsText));
                        break;
                    case CONTENTS:  /** 답변 */
                        booleanBuilder.or(topQna.answer.contains(containsText));
                        break;
                }
            } else if(requestObject instanceof MyQnaViewRequestVO) { /** 1:1 문의 */
                switch (Code.CON_TEXT_TYPE.enumOfCode(((MyQnaViewRequestVO) requestObject).getContainTextType())) {
                    case ALL:   /** 제목 + 내용 */
                        booleanBuilder.or(myQna.reqTitle.contains(containsText));
                        booleanBuilder.or(myQna.reqContents.contains(containsText));
                        break;
                    case TITLE: /** 제목 */
                        booleanBuilder.or(myQna.reqTitle.contains(containsText));
                        break;
                    case CONTENTS:  /** 내용 */
                        booleanBuilder.or(myQna.reqContents.contains(containsText));
                        break;
                }
            } else if(requestObject instanceof ReviewViewRequestVO) { /** 교육 후기 */
                switch (Code.CON_TEXT_TYPE.enumOfCode(((ReviewViewRequestVO) requestObject).getContainTextType())) {
                    case ALL:   /** 제목 + 내용 */
                        booleanBuilder.or(educationReview.title.contains(containsText));
                        booleanBuilder.or(educationReview.contents.contains(containsText));
                        break;
                    case TITLE: /** 제목 */
                        booleanBuilder.or(educationReview.title.contains(containsText));
                        break;
                    case CONTENTS:  /** 내용 */
                        booleanBuilder.or(educationReview.contents.contains(containsText));
                        break;
                }
            } else if(requestObject instanceof ArchiveClassGuideViewRequestVO) { /** 수업지도안 관리 */
                switch (Code.CON_TEXT_TYPE.enumOfCode(((ArchiveClassGuideViewRequestVO) requestObject).getContainTextType())) {
                    case ALL:   /** 제목 + 내용 (학습영역) */
                        booleanBuilder.or(classGuide.title.contains(containsText));
                        booleanBuilder.or(classGuide.learningArea.contains(containsText));
                        break;
                    case TITLE: /** 제목 */
                        booleanBuilder.or(classGuide.title.contains(containsText));
                        break;
                    case CONTENTS:  /** 내용 (학습영역) */
                        booleanBuilder.or(classGuide.learningArea.contains(containsText));
                        break;
                }
            }
            else if(requestObject instanceof ArchiveClassGuideExcelRequestVO) { /** 수업지도안 관리 엑셀 */
                switch (Code.CON_TEXT_TYPE.enumOfCode(((ArchiveClassGuideExcelRequestVO) requestObject).getContainTextType())) {
                    case ALL:   /** 제목 + 내용 (학습영역) */
                        booleanBuilder.or(classGuide.title.contains(containsText));
                        booleanBuilder.or(classGuide.learningArea.contains(containsText));
                        break;
                    case TITLE: /** 제목 */
                        booleanBuilder.or(classGuide.title.contains(containsText));
                        break;
                    case CONTENTS:  /** 내용 (학습영역) */
                        booleanBuilder.or(classGuide.learningArea.contains(containsText));
                        break;
                }
            }else if(requestObject instanceof EventViewRequestVO) { /** 이벤트/설문 관리 */
                switch (Code.CON_TEXT_TYPE.enumOfCode(((EventViewRequestVO) requestObject).getContainTextType())) {
                    case ALL:   /** 제목 + 내용 */
                        booleanBuilder.or(eventInfo.title.contains(containsText));
                        booleanBuilder.or(eventInfo.contents.contains(containsText));
                        break;
                    case TITLE: /** 제목 */
                        booleanBuilder.or(eventInfo.title.contains(containsText));
                        break;
                    case CONTENTS:  /** 내용 */
                        booleanBuilder.or(eventInfo.contents.contains(containsText));
                        break;
                }
            } else if(requestObject instanceof PressViewRequestVO) { /** 행사소개 관리 */
                switch (Code.CON_TEXT_TYPE.enumOfCode(((PressViewRequestVO) requestObject).getContainTextType())) {
                    case ALL:   /** 제목 + 내용 */
                        booleanBuilder.or(pressRelease.title.contains(containsText));
                        booleanBuilder.or(pressRelease.contents.contains(containsText));
                        break;
                    case TITLE: /** 제목 */
                        booleanBuilder.or(pressRelease.title.contains(containsText));
                        break;
                    case CONTENTS:  /** 내용 */
                        booleanBuilder.or(pressRelease.contents.contains(containsText));
                        break;
                }
            } else if(requestObject instanceof EduPlaceViewRequestVO) { /** 교육장 신청 관리 */
                switch (Code.CON_TEXT_TYPE.enumOfCode(((EduPlaceViewRequestVO) requestObject).getContainTextType())) {
                    case ALL:   /** 제목 + 내용 */
                        booleanBuilder.or(eduPlaceAply.title.contains(containsText));
                        booleanBuilder.or(eduPlaceAply.contents.contains(containsText));
                        break;
                    case TITLE: /** 제목 */
                        booleanBuilder.or(eduPlaceAply.title.contains(containsText));
                        break;
                    case CONTENTS:  /** 내용 */
                        booleanBuilder.or(eduPlaceAply.contents.contains(containsText));
                        break;
                }
            } else if(requestObject instanceof BannerViewRequestVO) { /** 배너 관리 */
                switch (Code.CON_TEXT_TYPE.enumOfCode(((BannerViewRequestVO) requestObject).getContainTextType())) {
                    case ALL:   /** 제목 + 내용 */
                        booleanBuilder.or(homeBanner.title.contains(containsText));
                        break;
                    case TITLE: /** 제목 */
                        booleanBuilder.or(homeBanner.title.contains(containsText));
                        break;
                }
            } else if(requestObject instanceof PopupViewRequestVO) { /** 팝업 관리 */
                switch (Code.CON_TEXT_TYPE.enumOfCode(((PopupViewRequestVO) requestObject).getContainTextType())) {
                    case ALL:   /** 제목 + 내용 */
                        booleanBuilder.or(homePopup.title.contains(containsText));
                        booleanBuilder.or(homePopup.contents.contains(containsText));
                        break;
                    case TITLE: /** 제목 */
                        booleanBuilder.or(homePopup.title.contains(containsText));
                        break;
                    case CONTENTS:  /** 내용 */
                        booleanBuilder.or(homePopup.contents.contains(containsText));
                        break;
                }
            } else if(requestObject instanceof ArchiveViewRequestVO) { /** 자료실 관리 */
                switch (Code.LMS_DATA_CON_TEXT_TYPE.enumOfCode(((ArchiveViewRequestVO) requestObject).getContainTextType())) {
                    case ALL:   /** 제목 + 내용 + 저자 */
                        booleanBuilder.or(lmsData.title.contains(containsText));
                        booleanBuilder.or(lmsData.contents.contains(containsText));
                        booleanBuilder.or(lmsData.author.contains(containsText));
                        break;
                    case TITLE: /** 제목 */
                        booleanBuilder.or(lmsData.title.contains(containsText));
                        break;
                    case CONTENTS:  /** 내용 */
                        booleanBuilder.or(lmsData.contents.contains(containsText));
                        break;
                    case AUTHOR:  /** 저자 */
                        booleanBuilder.or(lmsData.author.contains(containsText));
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

        if((!StringUtils.isEmpty(startDateTime) && StringUtils.isEmpty(endDateTime))
                || (StringUtils.isEmpty(startDateTime) && !StringUtils.isEmpty(endDateTime))) {
            throw new KPFException(KPF_RESULT.ERROR9001, "조회 시작 일자 & 조회 종료 일자는 한가지만 존재할 수 없습니다.");
        } else if(!StringUtils.isEmpty(startDateTime) && !StringUtils.isEmpty(endDateTime)) {
            if(requestObject instanceof NoticeViewRequestVO) {/** 공지 사항 */
                return notice.createDateTime.between(startDateTime, endDateTime);
            } else if(requestObject instanceof TopQnaViewRequestVO) { /** 자주묻는 질문 */
                return topQna.createDateTime.between(startDateTime, endDateTime);
            } else if(requestObject instanceof MyQnaViewRequestVO) { /** 1:1 문의 */
                return myQna.createDateTime.between(startDateTime, endDateTime);
            } else if(requestObject instanceof ReviewViewRequestVO) { /** 교육 후기 */
                return educationReview.createDateTime.between(startDateTime, endDateTime);
            } else if(requestObject instanceof ArchiveClassGuideViewRequestVO) { /** 수업지도안 */
                return classGuide.createDateTime.between(startDateTime, endDateTime);
            } else if(requestObject instanceof ClassSubjectViewRequestVO) { /** 수업지도안 교과 */
                return classSubject.createDateTime.between(startDateTime, endDateTime);
            } else if(requestObject instanceof PageViewRequestVO) { /** 페이지 */
                return documents.createDateTime.between(startDateTime, endDateTime);
            } else if(requestObject instanceof EventViewRequestVO) { /** 이벤트/설문 */
                return eventInfo.createDateTime.between(startDateTime, endDateTime);
            } else if(requestObject instanceof PressViewRequestVO) { /** 행사소개 */
                return pressRelease.createDateTime.between(startDateTime, endDateTime);
            } else if(requestObject instanceof EduPlaceViewRequestVO) { /** 교육장 신청 이력 */
                return eduPlaceAply.createDateTime.between(startDateTime, endDateTime);
            } else if(requestObject instanceof BannerViewRequestVO) { /** 배너 */
                return homeBanner.createDateTime.between(startDateTime, endDateTime);
            } else if(requestObject instanceof PopupViewRequestVO) { /** 쿠폰 */
                return homePopup.createDateTime.between(startDateTime, endDateTime);
            } else if(requestObject instanceof ArchiveViewRequestVO) { /** 자료실 */
                return lmsData.createDateTime.between(startDateTime, endDateTime);
            } else if(requestObject instanceof SmsViewRequestVO) { /** SMS 관리 */
                return smsSendMaster.sendDateTime.between(startDateTime, endDateTime);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /** 시간 대 검색 */
    private <T extends CSEntitySupport> BooleanExpression checkDate(T value, String date, String type) {

        if (date != null && date.length() > 0) {
            DateExpression<Date> expressionCheck = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", date, "%Y-%m-%d");
            if (value instanceof EventInfo) {
                if (type.equals("goe")) {/** expression >= expressionCheck */
                    DateExpression<Date> expression = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", eventInfo.startDt, "%Y-%m-%d");
                    return expression.goe(expressionCheck);
                } else if (type.equals("loe")) {/** expression <= expressionCheck */
                    DateExpression<Date> expression = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", eventInfo.endDt, "%Y-%m-%d");
                    return expression.loe(expressionCheck);
                }
            } else if (value instanceof HomePopup) {
                if (type.equals("goe")) {/** expression >= expressionCheck */
                    DateExpression<Date> expression = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", homePopup.popupStartYmd, "%Y-%m-%d");
                    return expression.goe(expressionCheck);
                } else if (type.equals("loe")) {/** expression <= expressionCheck */
                    DateExpression<Date> expression = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", homePopup.popupEndYmd, "%Y-%m-%d");
                    return expression.loe(expressionCheck);
                }
            }
        }
        return null;
    }

    /** 자동 순서 생성(과목) */
    @Override
    public Integer generateOrderAutoIncrease(String upIndividualCode) {
        Optional<ClassSubject> order = jpaQueryFactory.selectFrom(classSubject)
                .where(classSubject.upIndividualCode.eq(upIndividualCode))
                .orderBy(classSubject.order.desc())
                .fetch().stream().findFirst();

        if (!order.isEmpty()){
            return order.get().getOrder() + 1;
        }
        return 1;
    }

    /**
     * 일련 번호 생성
     */
    @Override
    public String generateCode(String prefixCode) {
        if (prefixCode.equals("NOTI")) {/** 공지사항 */
            return jpaQueryFactory.selectFrom(notice)
                    .where(notice.noticeSerialNo.like(prefixCode+"%"))
                    .orderBy(notice.noticeSerialNo.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getNoticeSerialNo().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        } else if (prefixCode.equals("GUI")) {/** 수업지도안 */
            return jpaQueryFactory.selectFrom(classGuide)
                    .orderBy(classGuide.classGuideCode.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getClassGuideCode().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        } else if (prefixCode.equals("SBJT")) {/** 수업지도안 교과 */
            return jpaQueryFactory.selectFrom(classSubject)
                    .where(classSubject.individualCode.like(prefixCode+"%"))
                    .orderBy(classSubject.individualCode.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getIndividualCode().replace(prefixCode, "")) + 1), 5, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("00000").toString());
        } else if (prefixCode.equals("HBN")) {/** 배너 */
            return jpaQueryFactory.selectFrom(homeBanner)
                    .where(homeBanner.bannerSn.like(prefixCode+"%"))
                    .orderBy(homeBanner.bannerSn.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getBannerSn().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("00000").toString());
        } else if (prefixCode.equals("HPU")) {/** 팝업 */
            return jpaQueryFactory.selectFrom(homePopup)
                    .where(homePopup.popupSn.like(prefixCode+"%"))
                    .orderBy(homePopup.popupSn.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getPopupSn().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("00000").toString());
        } else {
            return null;
        }
    }

}
