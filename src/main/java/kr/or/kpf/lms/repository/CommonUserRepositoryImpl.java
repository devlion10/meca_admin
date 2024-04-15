package kr.or.kpf.lms.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.or.kpf.lms.biz.user.adminuser.vo.request.AdminUserViewRequestVO;
import kr.or.kpf.lms.biz.user.adminuser.vo.response.AdminUserExcelVO;
import kr.or.kpf.lms.biz.user.authority.menu.vo.request.AuthorityMenuViewRequestVO;
import kr.or.kpf.lms.biz.user.authority.vo.request.AuthorityViewRequestVO;
import kr.or.kpf.lms.biz.user.history.vo.InstructorLctrViewRequestVO;
import kr.or.kpf.lms.biz.user.instructor.history.vo.request.InstructorHistoryViewRequestVO;
import kr.or.kpf.lms.biz.user.instructor.qualification.vo.request.InstructorQualificationViewRequestVO;
import kr.or.kpf.lms.biz.user.instructor.vo.request.InstructorCareerRequestVO;
import kr.or.kpf.lms.biz.user.instructor.vo.request.InstructorCustomViewRequestVO;
import kr.or.kpf.lms.biz.user.instructor.vo.request.InstructorLctrApiRequestVO;
import kr.or.kpf.lms.biz.user.instructor.vo.request.InstructorViewRequestVO;
import kr.or.kpf.lms.biz.user.instructor.vo.response.InstructorCareerResponseVO;
import kr.or.kpf.lms.biz.user.instructor.vo.response.InstructorExcelVO;
import kr.or.kpf.lms.biz.user.organization.vo.request.OrganizationMediaViewRequestVO;
import kr.or.kpf.lms.biz.user.organization.vo.request.OrganizationViewRequestVO;
import kr.or.kpf.lms.biz.user.organization.vo.response.OrganizationExcelVO;
import kr.or.kpf.lms.biz.user.organization.vo.response.OrganizationMediaExcelVO;
import kr.or.kpf.lms.biz.user.webauthority.vo.request.WebAuthorityViewRequestVO;
import kr.or.kpf.lms.biz.user.webauthority.vo.response.WebAuthorityCustomApiResponseVO;
import kr.or.kpf.lms.biz.user.webauthority.vo.response.WebAuthorityExcelVO;
import kr.or.kpf.lms.biz.user.webuser.vo.request.WebUserViewRequestVO;
import kr.or.kpf.lms.biz.user.webuser.vo.response.WebUserExcelVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSEntitySupport;
import kr.or.kpf.lms.common.support.CSRepositorySupport;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import kr.or.kpf.lms.common.support.Code;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.ResponseSummary;
import kr.or.kpf.lms.repository.entity.*;
import kr.or.kpf.lms.repository.entity.education.EducationPlan;
import kr.or.kpf.lms.repository.entity.education.LectureLecturer;
import kr.or.kpf.lms.repository.entity.education.LectureMaster;
import kr.or.kpf.lms.repository.entity.homepage.ClassSubject;
import kr.or.kpf.lms.repository.entity.role.AdminRoleMenu;
import kr.or.kpf.lms.repository.entity.role.IndividualAuthorityHistory;
import kr.or.kpf.lms.repository.entity.role.OrganizationAuthorityHistory;
import kr.or.kpf.lms.repository.entity.user.InstructorInfo;
import kr.or.kpf.lms.repository.entity.user.LmsUser;
import kr.or.kpf.lms.repository.role.AdminRoleMenuRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.*;
import java.util.stream.Collectors;

import static kr.or.kpf.lms.repository.entity.QBizInstructorAsgnm.bizInstructorAsgnm;
import static kr.or.kpf.lms.repository.entity.QBizInstructorIdentifyDtl.bizInstructorIdentifyDtl;
import static kr.or.kpf.lms.repository.entity.QBizOrganizationAplyDtl.bizOrganizationAplyDtl;
import static kr.or.kpf.lms.repository.entity.QBizPbancMaster.bizPbancMaster;
import static kr.or.kpf.lms.repository.entity.education.QCurriculumMaster.curriculumMaster;
import static kr.or.kpf.lms.repository.entity.education.QEducationPlan.educationPlan;
import static kr.or.kpf.lms.repository.entity.education.QLectureLecturer.lectureLecturer;
import static kr.or.kpf.lms.repository.entity.education.QLectureMaster.lectureMaster;
import static kr.or.kpf.lms.repository.entity.role.QAdminRoleGroup.adminRoleGroup;
import static kr.or.kpf.lms.repository.entity.role.QAdminRoleMenu.adminRoleMenu;
import static kr.or.kpf.lms.repository.entity.role.QIndividualAuthorityHistory.individualAuthorityHistory;
import static kr.or.kpf.lms.repository.entity.role.QOrganizationAuthorityHistory.organizationAuthorityHistory;
import static kr.or.kpf.lms.repository.entity.system.QAdminMenu.adminMenu;
import static kr.or.kpf.lms.repository.entity.user.QAdminUser.adminUser;
import static kr.or.kpf.lms.repository.entity.user.QFormeEdu2020LctrHistory.formeEdu2020LctrHistory;
import static kr.or.kpf.lms.repository.entity.user.QLmsUser.lmsUser;
import static kr.or.kpf.lms.repository.entity.user.QInstructorInfo.instructorInfo;
import static kr.or.kpf.lms.repository.entity.user.QInstructorHist.instructorHist;
import static kr.or.kpf.lms.repository.entity.user.QInstructorQlfc.instructorQlfc;
import static kr.or.kpf.lms.repository.entity.user.QOrganizationInfo.organizationInfo;
import static kr.or.kpf.lms.repository.entity.QBizOrganizationAply.bizOrganizationAply;
import static kr.or.kpf.lms.repository.entity.QBizInstructorAply.bizInstructorAply;
import static kr.or.kpf.lms.repository.entity.QBizInstructorIdentify.bizInstructorIdentify;
import static kr.or.kpf.lms.repository.entity.user.QOrganizationInfoMedia.organizationInfoMedia;
import static org.springframework.util.StringUtils.hasText;

/**
 * 유저 공통 Repository 구현체
 */
@Repository
public class CommonUserRepositoryImpl extends CSRepositorySupport implements CommonUserRepository {

    private final JPAQueryFactory jpaQueryFactory;
    public CommonUserRepositoryImpl(JPAQueryFactory jpaQueryFactory){ this.jpaQueryFactory = jpaQueryFactory; }

    @Autowired private AdminRoleMenuRepository adminRoleMenuRepository;

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
     * 엑셀 조회
     */
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
        if(requestObject instanceof AuthorityViewRequestVO) { /** 어드민 권한 관리 */
            return jpaQueryFactory.select(adminRoleGroup.count())
                    .from(adminRoleGroup)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof AuthorityMenuViewRequestVO) { /** 어드민 권한 관리 */
            return jpaQueryFactory.select(adminRoleMenu.count())
                    .from(adminRoleMenu)
                    .leftJoin(adminMenu).on(adminMenu.sequenceNo.eq(adminRoleMenu.menuSequenceNo))
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof WebAuthorityViewRequestVO) { /** 웹 회원 권한 관리 */
            ((WebAuthorityViewRequestVO) requestObject).setTargetTable(0);
            Long count = Long.valueOf(0);
            count = count + jpaQueryFactory.select(organizationAuthorityHistory.count())
                    .from(organizationAuthorityHistory)
                    .innerJoin(lmsUser).on(organizationAuthorityHistory.userId.eq(lmsUser.userId))
                    .where(getQuery(requestObject))
                    .fetchOne();

            ((WebAuthorityViewRequestVO) requestObject).setTargetTable(1);
            count = count + jpaQueryFactory.select(individualAuthorityHistory.count())
                    .from(individualAuthorityHistory)
                    .innerJoin(lmsUser).on(individualAuthorityHistory.userId.eq(lmsUser.userId))
                    .where(getQuery(requestObject))
                    .fetchOne();

            return count;
        } else if(requestObject instanceof WebUserViewRequestVO) { /** 웹 회원 관리 */
            return jpaQueryFactory.select(lmsUser.count())
                    .from(lmsUser)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof InstructorViewRequestVO) { /** 강사 관리 */
            return jpaQueryFactory.select(instructorInfo.count())
                    .from(instructorInfo)
                    .leftJoin(lmsUser).on(lmsUser.userId.eq(instructorInfo.userId))
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof InstructorCustomViewRequestVO) { /** 강사 관리 엑셀 */
            return jpaQueryFactory.select(instructorInfo.count())
                    .from(instructorInfo)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof InstructorHistoryViewRequestVO) { /** 강사 관리 주요 이력 */
            return jpaQueryFactory.select(instructorHist.count())
                    .from(instructorHist)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof InstructorQualificationViewRequestVO) { /** 강사 관리 주요 이력 */
            return jpaQueryFactory.select(instructorQlfc.count())
                    .from(instructorQlfc)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof OrganizationViewRequestVO) { /** 기관 정보 */
            return jpaQueryFactory.select(organizationInfo.count())
                    .from(organizationInfo)
                    .where(getQuery(requestObject))
                    .fetchOne();
        }  else if(requestObject instanceof OrganizationMediaViewRequestVO) { /** 매체 정보 */
            return jpaQueryFactory.select(organizationInfoMedia.count())
                    .from(organizationInfoMedia)
                    .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(organizationInfoMedia.organizationCode))
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof AdminUserViewRequestVO) { /** 어드민 계정 관리 */
            return jpaQueryFactory.select(adminUser.count())
                    .from(adminUser)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof InstructorCareerRequestVO) { /** 강의 이력 */
            if (((InstructorCareerRequestVO) requestObject).getInstrCtgr() != null) {
                if (((InstructorCareerRequestVO) requestObject).getInstrCtgr().contains(Code.INSTR_CTGR.INSTR.enumCode)) {
                    List<Long> countDto = new ArrayList<>();
                    ((InstructorCareerRequestVO) requestObject).setCategory("instr");
                    List<Long> countInstr = jpaQueryFactory.select(bizInstructorAsgnm.count())
                            .from(bizInstructorAsgnm)
                            .innerJoin(bizInstructorIdentifyDtl).on(bizInstructorIdentifyDtl.bizOrgAplyDtlNo.eq(bizInstructorAsgnm.bizOrgAplyDtlNo))
                            .innerJoin(bizInstructorIdentify).on(bizInstructorIdentify.bizInstrIdntyNo.eq(bizInstructorIdentifyDtl.bizInstrIdntyNo))
                            .innerJoin(bizOrganizationAplyDtl).on(bizOrganizationAplyDtl.bizOrgAplyDtlNo.eq(bizInstructorAsgnm.bizOrgAplyDtlNo))
                            .innerJoin(bizOrganizationAply).on(bizOrganizationAply.bizOrgAplyNo.eq(bizOrganizationAplyDtl.bizOrgAplyNo))
                            .innerJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                            .where(getQuery(requestObject))
                            .fetch();
                    countDto.addAll(countInstr);
                    ((InstructorCareerRequestVO) requestObject).setCategory("reader");
                    List<Long> countReader = jpaQueryFactory.select(lectureMaster.count())
                            .from(lectureMaster)
                            .innerJoin(educationPlan).on(educationPlan.educationPlanCode.eq(lectureMaster.educationPlanCode))
                            .innerJoin(curriculumMaster).on(curriculumMaster.curriculumCode.eq(educationPlan.curriculumCode))
                            .where(getQuery(requestObject))
                            .groupBy(lectureMaster.educationPlanCode)
                            .fetch();
                    countDto.addAll(countReader);

                    Long result = Long.valueOf(0);
                    for(Long count : countDto){
                        result=result + 1;
                    }
                    return result;
                } else {
                    List<Long> countDto = jpaQueryFactory.select(lectureMaster.count())
                            .from(lectureMaster)
                            .innerJoin(educationPlan).on(educationPlan.educationPlanCode.eq(lectureMaster.educationPlanCode))
                            .innerJoin(curriculumMaster).on(curriculumMaster.curriculumCode.eq(educationPlan.curriculumCode))
                            .where(getQuery(requestObject))
                            .groupBy(lectureMaster.educationPlanCode)
                            .fetch();

                    Long result = Long.valueOf(0);
                    for(Long count : countDto){
                        result=result + 1;
                    }
                    return result;
                }
            } else return NumberUtils.LONG_ZERO;
        } else if(requestObject instanceof InstructorLctrApiRequestVO) { /** 강의 이력(언론인 교육센터) */
            return jpaQueryFactory.select(formeEdu2020LctrHistory.count())
                    .from(formeEdu2020LctrHistory)
                    .where(getQuery(requestObject))
                    .fetchOne();
        }else if(requestObject instanceof InstructorLctrViewRequestVO) { /** 강의 이력(언론인 교육센터) */
            return jpaQueryFactory.select(formeEdu2020LctrHistory.count())
                    .from(formeEdu2020LctrHistory)
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
        if(requestObject instanceof AuthorityViewRequestVO) { /** 어드민 권한 관리 */
            return jpaQueryFactory.selectFrom(adminRoleGroup)
                    .where(getQuery(requestObject))
                    .orderBy(adminRoleGroup.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch().stream()
                    .peek(auth -> {
                        List<AdminRoleMenu> adminRoleMenus = adminRoleMenuRepository.findAll(Example.of(AdminRoleMenu.builder()
                                .roleGroupCode(auth.getRoleGroupCode())
                                .build()));

                        if (adminRoleMenus != null && !adminRoleMenus.isEmpty() && adminRoleMenus.size() > 0)
                            auth.setAdminRoleMenus(adminRoleMenus);
                    }).collect(Collectors.toList());
        } else if(requestObject instanceof AuthorityMenuViewRequestVO) { /** 어드민 권한 관리 */
            return jpaQueryFactory.selectFrom(adminRoleMenu)
                    .leftJoin(adminMenu).on(adminMenu.sequenceNo.eq(adminRoleMenu.menuSequenceNo))
                    .where(getQuery(requestObject))
                    .orderBy(adminMenu.depth.asc(), adminMenu.sort.asc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        } else if(requestObject instanceof WebAuthorityViewRequestVO) { /** 웹 회원 권한 관리 */
            List<WebAuthorityCustomApiResponseVO> dtos = new ArrayList<>();

            long offset = requestObject.getPageable().getOffset();
            long pageSize = requestObject.getPageable().getPageSize();

            ((WebAuthorityViewRequestVO) requestObject).setTargetTable(0);
            List<WebAuthorityCustomApiResponseVO> organizations = jpaQueryFactory.select(Projections.fields(WebAuthorityCustomApiResponseVO.class,
                            lmsUser.roleGroup,
                            lmsUser.userName,
                            lmsUser.phone,
                            organizationAuthorityHistory.sequenceNo,
                            organizationAuthorityHistory.userId,
                            organizationAuthorityHistory.createDateTime,
                            organizationAuthorityHistory.businessAuthority,
                            organizationAuthorityHistory.businessAuthorityApprovalState
                    ))
                    .from(organizationAuthorityHistory)
                    .innerJoin(lmsUser).on(lmsUser.userId.eq(organizationAuthorityHistory.userId))
                    .where(getQuery(requestObject))
                    .fetch();

            if (organizations.size() > 0 && organizations != null) {
                for (WebAuthorityCustomApiResponseVO organization : organizations) {
                    organization.setOrganizationAuthorityHistory(jpaQueryFactory.selectFrom(organizationAuthorityHistory)
                            .where(organizationAuthorityHistory.sequenceNo.eq(organization.getSequenceNo()))
                            .fetchOne());

                    organization.setLmsUser(jpaQueryFactory.selectFrom(lmsUser)
                            .where(lmsUser.userId.eq(organization.getUserId()))
                            .fetchOne());
                }
                dtos.addAll(organizations);
            }

            ((WebAuthorityViewRequestVO) requestObject).setTargetTable(1);
            List<WebAuthorityCustomApiResponseVO> inviduals = jpaQueryFactory.select(Projections.fields(WebAuthorityCustomApiResponseVO.class,
                            lmsUser.roleGroup,
                            lmsUser.userName,
                            lmsUser.phone,
                            individualAuthorityHistory.sequenceNo,
                            individualAuthorityHistory.userId,
                            individualAuthorityHistory.createDateTime,
                            individualAuthorityHistory.businessAuthority,
                            individualAuthorityHistory.businessAuthorityApprovalState
                    ))
                    .from(individualAuthorityHistory)
                    .innerJoin(lmsUser).on(lmsUser.userId.eq(individualAuthorityHistory.userId))
                    .where(getQuery(requestObject))
                    .fetch();

            if (inviduals.size() > 0 && inviduals != null) {
                for (WebAuthorityCustomApiResponseVO invidual : inviduals) {
                    invidual.setIndividualAuthorityHistory(jpaQueryFactory.selectFrom(individualAuthorityHistory)
                            .where(individualAuthorityHistory.sequenceNo.eq(invidual.getSequenceNo()))
                            .fetchOne());

                    invidual.setLmsUser(jpaQueryFactory.selectFrom(lmsUser)
                            .where(lmsUser.userId.eq(invidual.getUserId()))
                            .fetchOne());
                }
                dtos.addAll(inviduals);
            }

            dtos.sort(Comparator.comparing(WebAuthorityCustomApiResponseVO::getCreateDateTime).reversed());
            if (offset+pageSize > dtos.size()) {
                return dtos.subList((int) offset, (int) (dtos.size()));
            } else {
                return dtos.subList((int) offset, (int) (offset + pageSize));
            }

        } else if(requestObject instanceof WebUserViewRequestVO) { /** 웹 회원 관리 */
            return jpaQueryFactory.selectFrom(lmsUser)
                    .where(getQuery(requestObject))
                    .orderBy(lmsUser.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        } else if(requestObject instanceof InstructorViewRequestVO) { /** 강사 관리 */
            List<InstructorInfo> entities = jpaQueryFactory.selectFrom(instructorInfo)
                    .leftJoin(lmsUser).on(lmsUser.userId.eq(instructorInfo.userId))
                    .where(getQuery(requestObject))
                    .orderBy(instructorInfo.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();

            if (entities != null && entities.size() > 0) {
                for (InstructorInfo entity : entities) {
                    if (entity.getUserId() != null) {
                        LmsUser lmsUserEntity = jpaQueryFactory.selectFrom(lmsUser)
                                .where(lmsUser.userId.eq(entity.getUserId()))
                                .fetchOne();

                        if (lmsUserEntity != null && !lmsUserEntity.equals(null)) {
                            entity.setLmsUser(lmsUserEntity);

                            List<BizInstructorAply> bizInstructorAplyEntities = jpaQueryFactory.selectFrom(bizInstructorAply)
                                    .where(bizInstructorAply.bizInstrAplyStts.eq(3), bizInstructorAply.bizInstrAplyInstrId.eq(lmsUserEntity.getUserId()))
                                    .orderBy(bizInstructorAply.createDateTime.desc())
                                    .fetch();

                            if (bizInstructorAplyEntities != null && bizInstructorAplyEntities.size() > 0) {
                                entity.setBizInstructorAplies(bizInstructorAplyEntities);
                                for (BizInstructorAply bizInstructorAplyEntity : bizInstructorAplyEntities) {
                                    BizOrganizationAply bizOrganizationAplyEntity = jpaQueryFactory.selectFrom(bizOrganizationAply)
                                            .where(bizOrganizationAply.bizOrgAplyNo.eq(bizInstructorAplyEntity.getBizOrgAplyNo()))
                                            .fetchOne();

                                    if (bizOrganizationAplyEntity != null && !bizOrganizationAplyEntity.equals(null)) {
                                        bizInstructorAplyEntity.setBizOrganizationAply(bizOrganizationAplyEntity);

                                        /** 강의확인서 dtl에 사업 신청 dtl 객체 추가 */
                                        bizOrganizationAplyEntity.setBizInstructorIdentifies(jpaQueryFactory.selectFrom(bizInstructorIdentify)
                                            .where(bizInstructorIdentify.bizOrgAplyNo.eq(bizOrganizationAplyEntity.getBizOrgAplyNo()),
                                                    bizInstructorIdentify.registUserId.eq(bizInstructorAplyEntity.getBizInstrAplyInstrId()))
                                            .fetch());
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return entities;

        } else if(requestObject instanceof InstructorHistoryViewRequestVO) { /** 강사 관리 주요 이력 */
            return jpaQueryFactory.selectFrom(instructorHist)
                    .where(getQuery(requestObject))
                    .orderBy(instructorHist.sequenceNo.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        } else if(requestObject instanceof InstructorQualificationViewRequestVO) { /** 강사 관리 자격증 */
            return jpaQueryFactory.selectFrom(instructorQlfc)
                    .where(getQuery(requestObject))
                    .orderBy(instructorQlfc.sequenceNo.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        } else if(requestObject instanceof OrganizationViewRequestVO) { /** 기관 정보 */
            return jpaQueryFactory.selectFrom(organizationInfo)
                    .where(getQuery(requestObject))
                    .orderBy(organizationInfo.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        } else if(requestObject instanceof OrganizationMediaViewRequestVO) { /** 매체 정보 */
            return jpaQueryFactory.selectFrom(organizationInfoMedia)
                    .where(getQuery(requestObject))
                    .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(organizationInfoMedia.organizationCode))
                    .orderBy(organizationInfoMedia.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        } else if(requestObject instanceof AdminUserViewRequestVO) { /** 어드민 계정 관리 */
            return jpaQueryFactory.selectFrom(adminUser)
                    .where(getQuery(requestObject))
                    .orderBy(adminUser.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        } else if(requestObject instanceof InstructorCareerRequestVO) { /** 강의 이력 */
            if (((InstructorCareerRequestVO) requestObject).getInstrCtgr() != null) {
                List<InstructorCareerResponseVO> dtos = new ArrayList<>();
                if (((InstructorCareerRequestVO) requestObject).getInstrCtgr().contains(Code.INSTR_CTGR.INSTR.enumCode)) {
                    ((InstructorCareerRequestVO) requestObject).setCategory("instr");
                    List<InstructorCareerResponseVO> instrs = jpaQueryFactory.select(Projections.fields(InstructorCareerResponseVO.class,
                                    bizInstructorAsgnm.bizInstrAsgnmNo,
                                    bizInstructorAsgnm.bizInstrNo,
                                    bizInstructorAsgnm.bizOrgAplyNo,
                                    bizInstructorAsgnm.bizOrgAplyDtlNo,
                                    bizInstructorAsgnm.bizInstrAplyNo,
                                    bizPbancMaster.bizPbancCtgr.as("category"),
                                    bizPbancMaster.bizPbancNm.as("title"),
                                    bizOrganizationAply.orgCd.as("orgCd"),
                                    bizOrganizationAply.bizOrgAplyLsnPlanBgng.as("periodBgng"),
                                    bizOrganizationAply.bizOrgAplyLsnPlanEnd.as("periodEnd")
                            ))
                            .from(bizInstructorAsgnm)
                            .innerJoin(bizInstructorIdentifyDtl).on(bizInstructorIdentifyDtl.bizOrgAplyDtlNo.eq(bizInstructorAsgnm.bizOrgAplyDtlNo))
                            .innerJoin(bizInstructorIdentify).on(bizInstructorIdentify.bizInstrIdntyNo.eq(bizInstructorIdentifyDtl.bizInstrIdntyNo))
                            .innerJoin(bizOrganizationAplyDtl).on(bizOrganizationAplyDtl.bizOrgAplyDtlNo.eq(bizInstructorAsgnm.bizOrgAplyDtlNo))
                            .innerJoin(bizOrganizationAply).on(bizOrganizationAply.bizOrgAplyNo.eq(bizOrganizationAplyDtl.bizOrgAplyNo))
                            .innerJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                            .where(getQuery(requestObject))
                            .offset(requestObject.getPageable().getOffset())
                            .limit(requestObject.getPageable().getPageSize())
                            .fetch().stream().peek(data -> {
                                String category = "강의";
                                switch (data.getCategory()) {
                                    case 0:
                                        data.setCategoryNm(category+"(미디어교육)");
                                        break;
                                    case 1:
                                        data.setCategoryNm(category+"(언론인연수)");
                                        break;
                                    case 2:
                                        data.setCategoryNm(category+"(미디어지원)");
                                        break;
                                    default:
                                        data.setCategoryNm(category+"(구분)");
                                        break;
                                }

                                /** 사업명 기관명 추가 */
                                String orgNm = jpaQueryFactory.select(organizationInfo.organizationName)
                                        .from(organizationInfo)
                                        .where(organizationInfo.organizationCode.eq(data.getOrgCd()))
                                        .fetchOne();
                                data.setTitle(data.getTitle()+"("+orgNm+")");

                                /** 진행일시, 금액 */
                                List<BizInstructorIdentify> bizInstructorIdentifies = jpaQueryFactory.selectFrom(bizInstructorIdentify)
                                        .where(bizInstructorIdentify.bizOrgAplyNo.eq(data.getBizOrgAplyNo()), bizInstructorIdentify.registUserId.eq(((InstructorCareerRequestVO) requestObject).getUserId()))
                                        .fetch();

                                Integer total = 0;
                                List<String> dateTime = new ArrayList<>();
                                if (bizInstructorIdentifies != null && bizInstructorIdentifies.size() > 0) {
                                    for (BizInstructorIdentify identify : bizInstructorIdentifies) {
                                        /** 진행일시 */
                                        List<BizInstructorIdentifyDtl> bizInstructorIdentifyDtls = identify.getBizInstructorIdentifyDtls();
                                        if (bizInstructorIdentifyDtls != null && bizInstructorIdentifyDtls.size() > 0) {
                                            for (BizInstructorIdentifyDtl identifyDtl : bizInstructorIdentifyDtls) {
                                                BizOrganizationAplyDtl organizationAplyDtl = jpaQueryFactory.selectFrom(bizOrganizationAplyDtl)
                                                        .where(bizOrganizationAplyDtl.bizOrgAplyDtlNo.eq(identifyDtl.getBizOrgAplyDtlNo()))
                                                        .fetchOne();

                                                String date = new StringBuilder(organizationAplyDtl.getBizOrgAplyLsnDtlYmd())
                                                        .append(" ")
                                                        .append(organizationAplyDtl.getBizOrgAplyLsnDtlBgngTm())
                                                        .append(" ~ ")
                                                        .append(organizationAplyDtl.getBizOrgAplyLsnDtlEndTm()).toString();
                                                dateTime.add(date);
                                            }
                                        }

                                        /** 금액 */
                                        total += Integer.parseInt(identify.getBizInstrIdntyAmt());
                                    }
                                }
                                data.setDate(dateTime);
                                data.setPay(total);

                            }).collect(Collectors.toList());
                    dtos.addAll(instrs);

                    ((InstructorCareerRequestVO) requestObject).setCategory("reader");
                    List<InstructorCareerResponseVO> readers = jpaQueryFactory.select(Projections.fields(InstructorCareerResponseVO.class,
                                    lectureMaster.lectureCode,
                                    lectureMaster.educationPlanCode,
                                    curriculumMaster.categoryCode.as("categoryNm"),
                                    curriculumMaster.curriculumName.as("title"),
                                    educationPlan.operationBeginDateTime.as("periodBgng"),
                                    educationPlan.operationEndDateTime.as("periodEnd")
                            ))
                            .from(lectureMaster)
                            .innerJoin(educationPlan).on(educationPlan.educationPlanCode.eq(lectureMaster.educationPlanCode))
                            .innerJoin(curriculumMaster).on(curriculumMaster.curriculumCode.eq(educationPlan.curriculumCode))
                            .where(getQuery(requestObject))
                            .groupBy(lectureMaster.educationPlanCode)
                            .offset(requestObject.getPageable().getOffset())
                            .limit(requestObject.getPageable().getPageSize())
                            .fetch().stream().peek(data -> {
                                String category = "교육";
                                switch (data.getCategoryNm()) {
                                    case "1":
                                        data.setCategoryNm(category+"(언론인연수)");
                                        break;
                                    case "2":
                                        data.setCategoryNm(category+"(미디어교육)");
                                        break;
                                    default:
                                        data.setCategoryNm(category+"(공통)");
                                        break;
                                }

                                /** 진행일시, 금액 */
                                Integer total = 0;
                                List<String> dateTime = new ArrayList<>();
                                List<LectureLecturer> lectureLecturerList = jpaQueryFactory.selectFrom(lectureMaster)
                                        .where(lectureMaster.lectureCode.eq(data.getLectureCode()))
                                        .fetchOne().getLectureLecturerList();

                                if (lectureLecturerList != null && lectureLecturerList.size() > 0) {
                                    for (LectureLecturer lectureLecturer : lectureLecturerList) {
                                        if (lectureLecturer.getLecturer().equals(((InstructorCareerRequestVO) requestObject).getInstrSerialNo().toString())) {
                                            /** 진행일시 */
                                            if (data.getLectureCode().equals(lectureLecturer.getLectureCode())) {
                                                String date = new StringBuilder(data.getPeriodBgng())
                                                        .append(" ~ ")
                                                        .append(data.getPeriodEnd()).toString();
                                                dateTime.add(date);
                                            }
                                            /** 총액 */
                                            total += Integer.parseInt(lectureLecturer.getLecturerGratuity());
                                        }
                                    }
                                }
                                data.setDate(dateTime);
                                data.setPay(total);

                            }).collect(Collectors.toList());
                    dtos.addAll(readers);

                } else {
                    dtos = jpaQueryFactory.select(Projections.fields(InstructorCareerResponseVO.class,
                                    lectureMaster.lectureCode,
                                    lectureMaster.educationPlanCode,
                                    curriculumMaster.categoryCode.as("categoryNm"),
                                    curriculumMaster.curriculumName.as("title"),
                                    educationPlan.operationBeginDateTime.as("periodBgng"),
                                    educationPlan.operationEndDateTime.as("periodEnd")
                            ))
                            .from(lectureMaster)
                            .innerJoin(educationPlan).on(educationPlan.educationPlanCode.eq(lectureMaster.educationPlanCode))
                            .innerJoin(curriculumMaster).on(curriculumMaster.curriculumCode.eq(educationPlan.curriculumCode))
                            .where(getQuery(requestObject))
                            .groupBy(lectureMaster.educationPlanCode)
                            .offset(requestObject.getPageable().getOffset())
                            .limit(requestObject.getPageable().getPageSize())
                            .fetch().stream().peek(data -> {
                                String category = "교육";
                                switch (data.getCategoryNm()) {
                                    case "1":
                                        data.setCategoryNm(category+"(언론인연수)");
                                        break;
                                    case "2":
                                        data.setCategoryNm(category+"(미디어교육)");
                                        break;
                                    default:
                                        data.setCategoryNm(category+"(공통)");
                                        break;
                                }

                                /** 진행일시, 금액 */
                                Integer total = 0;
                                List<String> dateTime = new ArrayList<>();
                                List<LectureLecturer> lectureLecturerList = jpaQueryFactory.selectFrom(lectureMaster)
                                        .where(lectureMaster.lectureCode.eq(data.getLectureCode()))
                                        .fetchOne().getLectureLecturerList();

                                if (lectureLecturerList != null && lectureLecturerList.size() > 0) {
                                    for (LectureLecturer lectureLecturer : lectureLecturerList) {
                                        if (lectureLecturer.getLecturer().equals(((InstructorCareerRequestVO) requestObject).getInstrSerialNo().toString())) {
                                            /** 진행일시 */
                                            if (data.getLectureCode().equals(lectureLecturer.getLectureCode())) {
                                                String date = new StringBuilder(data.getPeriodBgng())
                                                        .append(" ~ ")
                                                        .append(data.getPeriodEnd()).toString();
                                                dateTime.add(date);
                                            }

                                            /** 총액 */
                                            total += Integer.parseInt(lectureLecturer.getLecturerGratuity());
                                        }
                                    }
                                }
                                data.setDate(dateTime);
                                data.setPay(total);

                            }).collect(Collectors.toList());
                }
                dtos.sort(Comparator.comparing(InstructorCareerResponseVO::getPeriodBgng).reversed());
                return dtos;
            } else return new ArrayList<>();

        } else if(requestObject instanceof InstructorLctrApiRequestVO) { /** 강의 이력(언론인 교육센터) */
            return jpaQueryFactory.selectFrom(formeEdu2020LctrHistory)
                    .where(getQuery(requestObject))
                    .orderBy(formeEdu2020LctrHistory.lctrNo.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        }else if(requestObject instanceof InstructorLctrViewRequestVO) { /** 강의 이력(언론인 교육센터) */
            return jpaQueryFactory.selectFrom(formeEdu2020LctrHistory)
                    .where(getQuery(requestObject))
                    .orderBy(formeEdu2020LctrHistory.lctrNo.desc())
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
        if(requestObject instanceof WebUserViewRequestVO) { /** 웹 회원 관리 */
            List<WebUserExcelVO> dtos = jpaQueryFactory.select(Projections.fields(WebUserExcelVO.class,
                            lmsUser.userId,
                            lmsUser.userName,
                            lmsUser.phone,
                            lmsUser.email,
                            lmsUser.roleGroup,
                            lmsUser.state,
                            lmsUser.lastLoginDateTime,
                            lmsUser.createDateTime,
                            organizationInfo.organizationName,
                            organizationInfo.organizationAddress1,
                            lmsUser.department,
                            lmsUser.rank,
                            lmsUser.position,
                            lmsUser.userZipCode,
                            lmsUser.userAddress1,
                            lmsUser.userAddress2,
                            lmsUser.tutorYn,
                            lmsUser.personalNiceNo
                ))
            .from(lmsUser)
            .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(lmsUser.organizationCode))
            .where(getQuery(requestObject))
            .orderBy(lmsUser.createDateTime.asc())
            .fetch();
            for(WebUserExcelVO dto:dtos){
                if(dto.getState()==null){
                    dto.setStateString("-");
                }else if(dto.getState().equals("1")){
                    dto.setStateString("정상");
                }else if(dto.getState().equals("2")){
                    dto.setStateString("휴면");
                }else if(dto.getState().equals("3")){
                    dto.setStateString("잠김");
                }else if(dto.getState().equals("4")){
                    dto.setStateString("탈퇴");
                }else{
                    dto.setStateString(dto.getState());
                }

                if(dto.getTutorYn()==null){
                    dto.setTutorYn("-");
                }else if(dto.getTutorYn().equals("Y")){
                    dto.setTutorYn("튜터");
                }else{
                    dto.setTutorYn("");
                }
                if(dto.getRoleGroup()==null){
                    dto.setRoleGroupString("-");
                }else if(dto.getRoleGroup().equals( "GENERAL")){
                    dto.setRoleGroupString("일반");
                }else if(dto.getRoleGroup().equals("TEACHER")){
                    dto.setRoleGroupString("일반(교원)");
                }else if(dto.getRoleGroup().equals("STUDENT")){
                    dto.setRoleGroupString("일반(학생)");
                }else if(dto.getRoleGroup().equals("PARENTS")){
                    dto.setRoleGroupString("일반(학부모)");
                }else if(dto.getRoleGroup().equals("JOURNALIST")){
                    dto.setRoleGroupString("언론인");
                }else{
                    dto.setRoleGroupString(dto.getRoleGroup());
                }
                if(dto.getOrganizationAddress1()!=null){
                    List<String> addressList = List.of(dto.getOrganizationAddress1().split(" "));
                    if(addressList.size()>1){
                        dto.setOrganizationRegion(dto.getOrganizationAddress1().split(" ")[1]);
                    }
                }
            }
            return dtos;
        }else if(requestObject instanceof WebAuthorityViewRequestVO) { /** 웹 회원 권한 관리 */
            List<WebAuthorityExcelVO> dtos = new ArrayList<>();

            ((WebAuthorityViewRequestVO) requestObject).setTargetTable(0);
            List<WebAuthorityExcelVO> organizations = jpaQueryFactory.select(Projections.fields(WebAuthorityExcelVO.class,
                            organizationAuthorityHistory.userId,
                            lmsUser.roleGroup,
                            lmsUser.userName,
                            lmsUser.phone,
                            lmsUser.email,
                            organizationAuthorityHistory.businessAuthority,
                            organizationAuthorityHistory.businessAuthorityApprovalState,
                            organizationAuthorityHistory.organizationName,
                            organizationAuthorityHistory.createDateTime,
                            organizationAuthorityHistory.updateDateTime
                    ))
                    .from(organizationAuthorityHistory)
                    .innerJoin(lmsUser).on(lmsUser.userId.eq(organizationAuthorityHistory.userId))
                    .where(getQuery(requestObject))
                    .orderBy(organizationAuthorityHistory.createDateTime.asc())
                    .fetch();

            dtos.addAll(organizations);
            ((WebAuthorityViewRequestVO) requestObject).setTargetTable(1);
            List<WebAuthorityExcelVO> inviduals = jpaQueryFactory.select(Projections.fields(WebAuthorityExcelVO.class,
                            individualAuthorityHistory.userId,
                            lmsUser.roleGroup,
                            lmsUser.userName,
                            lmsUser.phone,
                            lmsUser.email,

                            individualAuthorityHistory.businessAuthority,
                            individualAuthorityHistory.businessAuthorityApprovalState,
                            individualAuthorityHistory.createDateTime,
                            individualAuthorityHistory.updateDateTime
                    ))
                    .from(individualAuthorityHistory)
                    .innerJoin(lmsUser).on(lmsUser.userId.eq(individualAuthorityHistory.userId))
                    .where(getQuery(requestObject))
                    .orderBy(individualAuthorityHistory.createDateTime.asc())
                    .fetch();

            dtos.addAll(inviduals);
            for(WebAuthorityExcelVO dto:dtos){
                if(dto.getBusinessAuthority() == null){
                    dto.setType("-");
                }else if(dto.getBusinessAuthority().equals("SCHOOL")){
                    dto.setType("학교");
                }else if(dto.getBusinessAuthority().equals("AGENCY")){
                    dto.setType("기관");
                }else if(dto.getBusinessAuthority().equals("INSTR")){
                    dto.setType("미디어강사");
                }else{
                    dto.setType(dto.getBusinessAuthority());
                }
                if(dto.getBusinessAuthorityApprovalState()==null){
                    dto.setState("-");
                }else if(dto.getBusinessAuthorityApprovalState().equals("1")){
                    dto.setState("권한신청");
                }else if(dto.getBusinessAuthorityApprovalState().equals("2")){
                    dto.setState("승인");
                }else if(dto.getBusinessAuthorityApprovalState().equals("3")){
                    dto.setState("해제신청");
                }else if(dto.getBusinessAuthorityApprovalState().equals("4")){
                    dto.setState("해제");
                }else if(dto.getBusinessAuthorityApprovalState().equals("5")){
                    dto.setState("반려");
                }else{
                    dto.setState(dto.getBusinessAuthorityApprovalState());
                }
            }
            return dtos;

        }else if(requestObject instanceof InstructorCustomViewRequestVO) { /** 강사 관리 */
            List<InstructorExcelVO> dtos =  jpaQueryFactory.select(Projections.fields(InstructorExcelVO.class,
                            instructorInfo.userId,
                            instructorInfo.instrCtgr,
                            instructorInfo.instrNm,
                            instructorInfo.instrBrdt,
                            instructorInfo.instrTel,
                            instructorInfo.instrEml,
                            instructorInfo.instrStts,
                            instructorInfo.instrLctRgn1,
                            instructorInfo.instrLctRgn2,
                            instructorInfo.instrZipCd,
                            instructorInfo.instrAddr1,
                            instructorInfo.instrAddr2,
                            instructorInfo.instrAcbgSchlNm,
                            instructorInfo.instrAcbgMjr,
                            instructorInfo.instrAcbgDgr,
                            instructorInfo.instrBank,
                            instructorInfo.instrActno,
                            instructorInfo.orgName,
                            instructorInfo.department
                    ))
                    .from(instructorInfo)
                    .where(getQuery(requestObject))
                    .orderBy(instructorInfo.createDateTime.asc())
                    .fetch();

            for(InstructorExcelVO dto:dtos){
                if (dto.getInstrCtgr() != null) {
                    if (dto.getInstrCtgr().equals(Code.INSTR_CTGR.INSTR.enumCode))
                        dto.setInstrCtgr(Code.INSTR_CTGR.INSTR.codeName);
                    else if (dto.getInstrCtgr().equals(Code.INSTR_CTGR.LECTURER.enumCode))
                        dto.setInstrCtgr(Code.INSTR_CTGR.LECTURER.codeName);
                    else if (dto.getInstrCtgr().equals(Code.INSTR_CTGR.READER.enumCode))
                        dto.setInstrCtgr(Code.INSTR_CTGR.READER.codeName);
                    else
                        dto.setInstrCtgr("미지정");
                }
                if(dto.getInstrStts()== null){
                    dto.setInstrSttsString("-");
                }else if(dto.getInstrStts()==1){
                    dto.setInstrSttsString("사용");
                }else{
                    dto.setInstrSttsString("미사용");
                }

                if (dto.getUserId() != null) {
                    LmsUser user = jpaQueryFactory.selectFrom(lmsUser)
                            .where(lmsUser.userId.eq(dto.getUserId()))
                            .orderBy(lmsUser.createDateTime.desc())
                            .fetchOne();

                    if (user != null) {
                        if (user.getTutorYn() != null && user.getTutorYn().equals("Y")) {
                            dto.setTutorYn("튜터");
                        } else {
                            dto.setTutorYn("");
                        }

                        if (user.getPersonalNiceNo() != null) {
                            dto.setPersonalNiceNo(user.getPersonalNiceNo());
                        } else {
                            dto.setPersonalNiceNo("");
                        }
                    }
                }
            }
            return dtos;
        }else if(requestObject instanceof OrganizationViewRequestVO) { /** 기관 정보 */
            List<OrganizationExcelVO> dtos = jpaQueryFactory.select(Projections.fields(OrganizationExcelVO.class,
                            organizationInfo.organizationName,
                            organizationInfo.organizationType,
                            organizationInfo.bizLicenseNumber,
                            organizationInfo.organizationZipCode,
                            organizationInfo.organizationAddress1,
                            organizationInfo.organizationAddress2,
                            organizationInfo.organizationTelNumber,
                            organizationInfo.organizationFaxNumber,
                            organizationInfo.organizationHomepage,
                            organizationInfo.isUsable,
                            organizationInfo.createDateTime,
                            organizationInfo.updateDateTime
                    ))
                    .from(organizationInfo)
                    .where(getQuery(requestObject))
                    .orderBy(organizationInfo.createDateTime.asc())
                    .fetch();
            for(OrganizationExcelVO dto:dtos){
                if(dto.getIsUsable()){
                    dto.setIsUsableString("사용");
                }else {
                    dto.setIsUsableString("미사용");
                }
                if(dto.getOrganizationType() == null){
                    dto.setOrganizationTypeString("-");
                }else if(dto.getOrganizationType().equals("1")){
                    dto.setOrganizationTypeString("매체사");
                }else if(dto.getOrganizationType().equals("2")){
                    dto.setOrganizationTypeString("기관");
                }else if(dto.getOrganizationType().equals("3")){
                    dto.setOrganizationTypeString("학교");
                }else{
                    dto.setOrganizationTypeString(dto.getOrganizationType());
                }
            }
            return dtos;
        } else if(requestObject instanceof OrganizationMediaViewRequestVO) { /** 매체 정보 */
            List<OrganizationMediaExcelVO> dtos = jpaQueryFactory.select(Projections.fields(OrganizationMediaExcelVO.class,
                            organizationInfo.organizationName,
                            organizationInfoMedia.mediaName,
                            organizationInfoMedia.mediaClsName1,
                            organizationInfoMedia.mediaClsName2,
                            organizationInfoMedia.mediaArea,
                            organizationInfo.bizLicenseNumber,
                            organizationInfo.organizationZipCode,
                            organizationInfo.organizationAddress1,
                            organizationInfo.organizationAddress2,
                            organizationInfo.organizationTelNumber,
                            organizationInfo.organizationFaxNumber,
                            organizationInfo.organizationHomepage,
                            organizationInfo.isUsable
                    ))
                    .from(organizationInfoMedia)
                    .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(organizationInfoMedia.organizationCode))
                    .where(getQuery(requestObject))
                    .orderBy(organizationInfoMedia.createDateTime.asc())
                    .fetch();
            for(OrganizationMediaExcelVO dto:dtos){
                if(dto.getIsUsable()){
                    dto.setIsUsableString("사용");
                }else {
                    dto.setIsUsableString("미사용");
                }
            }
            return dtos;
        } else if(requestObject instanceof AdminUserViewRequestVO) { /** 어드민 계정 관리 */
            List<AdminUserExcelVO> dtos = jpaQueryFactory.select(Projections.fields(AdminUserExcelVO.class,
                            adminUser.userId,
                            adminUser.userName,
                            adminUser.roleGroup,
                            adminUser.phone,
                            adminUser.email,
                            adminUser.availableIp,
                            adminUser.state,
                            adminUser.isLock,
                            adminUser.passwordFailureCount,
                            adminUser.department,
                            adminUser.rank,
                            adminUser.position,
                            adminUser.createDateTime,
                            adminUser.updateDateTime
                    ))
                    .from(adminUser)
                    .where(getQuery(requestObject))
                    .orderBy(adminUser.userSerialNo.asc())
                    .fetch();
            for(AdminUserExcelVO dto:dtos){
                if(dto.getIsLock()){
                    dto.setIsLockString("잠김");
                }else{
                    dto.setIsLockString("잠기지 않음");
                }
                if(dto.getState() == null){
                    dto.setStateString("");
                }else if(dto.getState().equals("1")){
                    dto.setStateString("사용");
                }else{
                    dto.setStateString("사용안함");
                }
            }
            return dtos;
        } else{
            return null;
        }
    }

    /**
     * where 절
     */
    private <T extends CSViewVOSupport> Predicate[] getQuery(T requestObject) {
        if(requestObject instanceof AuthorityViewRequestVO) { /** 어드민 권한 관리 */
            return new Predicate[]{ condition(((AuthorityViewRequestVO) requestObject).getRoleGroupName(), adminRoleGroup.roleGroupName::contains),
                    condition(((AuthorityViewRequestVO) requestObject).getRoleGroupCode(), adminRoleGroup.roleGroupCode::eq)};
        } else if(requestObject instanceof AuthorityMenuViewRequestVO) { /** 어드민 권한 메뉴 관리 */
            return new Predicate[]{ condition(((AuthorityMenuViewRequestVO) requestObject).getRoleGroupCode(), adminRoleMenu.roleGroupCode::contains),
                    condition(((AuthorityMenuViewRequestVO) requestObject).getMenuSequenceNo(), adminRoleMenu.menuSequenceNo::eq)};
        } else if(requestObject instanceof WebAuthorityViewRequestVO) { /** 웹 회원 권한 관리 */
            String[] arrBusinessAuthoritys = null;
            if (((WebAuthorityViewRequestVO) requestObject).getBusinessAuthoritys() != null && !((WebAuthorityViewRequestVO) requestObject).getBusinessAuthoritys().isEmpty()) {
                arrBusinessAuthoritys = ((WebAuthorityViewRequestVO) requestObject).getBusinessAuthoritys().split(",");
            }

            if (((WebAuthorityViewRequestVO) requestObject).getApplyBgngDate() != null || ((WebAuthorityViewRequestVO) requestObject).getApplyEndDate() != null
                    || ((WebAuthorityViewRequestVO) requestObject).getApprvBgngDate() != null || ((WebAuthorityViewRequestVO) requestObject).getApprvEndDate() != null) {
                if (((WebAuthorityViewRequestVO) requestObject).getTargetTable() == 0) {

                    return new Predicate[]{condition(((WebAuthorityViewRequestVO) requestObject).getSequenceNo(), organizationAuthorityHistory.sequenceNo::eq),
                            condition(((WebAuthorityViewRequestVO) requestObject).getUserName(), lmsUser.userName::contains),
                            betweenDate(new OrganizationAuthorityHistory(), ((WebAuthorityViewRequestVO) requestObject).getApplyBgngDate(), ((WebAuthorityViewRequestVO) requestObject).getApplyEndDate(), "Apply"),
                            betweenDate(new OrganizationAuthorityHistory(), ((WebAuthorityViewRequestVO) requestObject).getApprvBgngDate(), ((WebAuthorityViewRequestVO) requestObject).getApprvEndDate(), "Apprv"),

                            condition(arrBusinessAuthoritys, organizationAuthorityHistory.businessAuthority::in),
                            condition(((WebAuthorityViewRequestVO) requestObject).getUserId(), organizationAuthorityHistory.userId::contains),
                            condition(((WebAuthorityViewRequestVO) requestObject).getBusinessAuthorityApprovalState(), organizationAuthorityHistory.businessAuthorityApprovalState::eq)
                    };
                } else {
                    return new Predicate[]{condition(((WebAuthorityViewRequestVO) requestObject).getSequenceNo(), individualAuthorityHistory.sequenceNo::eq),
                            condition(((WebAuthorityViewRequestVO) requestObject).getUserName(), lmsUser.userName::contains),
                            betweenDate(new IndividualAuthorityHistory(), ((WebAuthorityViewRequestVO) requestObject).getApplyBgngDate(), ((WebAuthorityViewRequestVO) requestObject).getApplyEndDate(), "Apply"),
                            betweenDate(new IndividualAuthorityHistory(), ((WebAuthorityViewRequestVO) requestObject).getApprvBgngDate(), ((WebAuthorityViewRequestVO) requestObject).getApprvEndDate(), "Apprv"),

                            condition(arrBusinessAuthoritys, individualAuthorityHistory.businessAuthority::in),
                            condition(((WebAuthorityViewRequestVO) requestObject).getUserId(), individualAuthorityHistory.userId::contains),
                            condition(((WebAuthorityViewRequestVO) requestObject).getBusinessAuthorityApprovalState(), individualAuthorityHistory.businessAuthorityApprovalState::eq)
                    };
                }
            } else {
                if (((WebAuthorityViewRequestVO) requestObject).getTargetTable() == 0) {
                    return new Predicate[]{condition(((WebAuthorityViewRequestVO) requestObject).getSequenceNo(), organizationAuthorityHistory.sequenceNo::eq),
                            condition(((WebAuthorityViewRequestVO) requestObject).getUserName(), lmsUser.userName::contains),
                            condition(arrBusinessAuthoritys, organizationAuthorityHistory.businessAuthority::in),
                            condition(((WebAuthorityViewRequestVO) requestObject).getUserId(), organizationAuthorityHistory.userId::contains),
                            condition(((WebAuthorityViewRequestVO) requestObject).getBusinessAuthorityApprovalState(), organizationAuthorityHistory.businessAuthorityApprovalState::eq)
                    };
                } else {
                    return new Predicate[]{condition(((WebAuthorityViewRequestVO) requestObject).getSequenceNo(), individualAuthorityHistory.sequenceNo::eq),
                            condition(((WebAuthorityViewRequestVO) requestObject).getUserName(), lmsUser.userName::contains),
                            condition(arrBusinessAuthoritys, individualAuthorityHistory.businessAuthority::in),
                            condition(((WebAuthorityViewRequestVO) requestObject).getUserId(), individualAuthorityHistory.userId::contains),
                            condition(((WebAuthorityViewRequestVO) requestObject).getBusinessAuthorityApprovalState(), individualAuthorityHistory.businessAuthorityApprovalState::eq)
                    };
                }
            }

        } else if(requestObject instanceof WebUserViewRequestVO) { /** 웹 회원 관리 */
            if (((WebUserViewRequestVO) requestObject).getManager() != null) {
                String[] roleGroup = {Code.BIZ_AUTH.SCHOOL.enumCode, Code.BIZ_AUTH.AGENCY.enumCode};
                return new Predicate[]{ condition(roleGroup, lmsUser.businessAuthority::in),
                        condition(((WebUserViewRequestVO) requestObject).getOrganizationCode(), lmsUser.organizationCode::eq),
                        searchContainText(requestObject, ((WebUserViewRequestVO) requestObject).getContainTextType(), ((WebUserViewRequestVO) requestObject).getContainText())};
            } else {
                List<String> arrRoleGroup = new ArrayList<>();
                if (((WebUserViewRequestVO) requestObject).getRoleGroups() != null) {
                    String[] paramRoleGroup = ((WebUserViewRequestVO) requestObject).getRoleGroups().split(",");
                    for (String roleGroup : paramRoleGroup) {
                        arrRoleGroup.add(roleGroup);
                    }
                } else {
                    arrRoleGroup = null;
                }

                List<String> arrBusinessAuthority = new ArrayList<>();
                if (((WebUserViewRequestVO) requestObject).getBusinessAuthoritys() != null) {
                    String[] paramBusinessAuthority = ((WebUserViewRequestVO) requestObject).getBusinessAuthoritys().split(",");
                    for (String businessAuthority : paramBusinessAuthority) {
                        arrBusinessAuthority.add(businessAuthority);
                    }
                } else {
                    arrBusinessAuthority = null;
                }

                return new Predicate[]{condition(((WebUserViewRequestVO) requestObject).getUserSerialNo(), lmsUser.userSerialNo::eq),
                        condition(((WebUserViewRequestVO) requestObject).getUserId(), lmsUser.userId::contains),
                        condition(((WebUserViewRequestVO) requestObject).getUserName(), lmsUser.userName::contains),

                        condition(arrRoleGroup, lmsUser.roleGroup::in),
                        condition(arrBusinessAuthority, lmsUser.businessAuthority::in),
                        condition(((WebUserViewRequestVO) requestObject).getTutorYn(), lmsUser.tutorYn::eq),

                        condition(((WebUserViewRequestVO) requestObject).getOrganizationName(), organizationInfo.organizationName::contains),
                        condition(Code.USER_STATE.valueOfEnum(((WebUserViewRequestVO) requestObject).getState()), lmsUser.state::eq),
                        condition(((WebUserViewRequestVO) requestObject).getWithDrawDate(), lmsUser.withDrawDate::eq),
                        condition(((WebUserViewRequestVO) requestObject).getDormancyDate(), lmsUser.dormancyDate::eq),
                        betweenTime(requestObject),
                        searchContainText(requestObject, ((WebUserViewRequestVO) requestObject).getContainTextType(), ((WebUserViewRequestVO) requestObject).getContainText())};
            }
        } else if(requestObject instanceof InstructorViewRequestVO) { /** 강사 관리 */
            return new Predicate[]{ condition(((InstructorViewRequestVO) requestObject).getInstrSerialNo(), instructorInfo.instrSerialNo::eq),
                    condition(((InstructorViewRequestVO) requestObject).getUserId(), lmsUser.userId::contains),
                    condition(((InstructorViewRequestVO) requestObject).getOrgName(), instructorInfo.orgName::contains),
                    condition(Code.USER_STATE.valueOfEnum(((InstructorViewRequestVO) requestObject).getState()), lmsUser.state::eq),
                    condition(Code.INSTR_CTGR.valueOfEnum(((InstructorViewRequestVO) requestObject).getInstrCtgr()), instructorInfo.instrCtgr::contains),
                    condition(((InstructorViewRequestVO) requestObject).getInstrNm(), instructorInfo.instrNm::contains),
                    condition(((InstructorViewRequestVO) requestObject).getInstrStts(), instructorInfo.instrStts::eq),
                    betweenTime(requestObject) };
        } else if(requestObject instanceof InstructorCustomViewRequestVO) { /** 강사 관리 */
            return new Predicate[]{ condition(((InstructorCustomViewRequestVO) requestObject).getUserId(), instructorInfo.userId::contains),
                    condition(Code.INSTR_CTGR.valueOfEnum(((InstructorCustomViewRequestVO) requestObject).getInstrCtgr()), instructorInfo.instrCtgr::contains),
                    condition(((InstructorCustomViewRequestVO) requestObject).getInstrNm(), instructorInfo.instrNm::contains),
                    condition(((InstructorCustomViewRequestVO) requestObject).getInstrStts(), instructorInfo.instrStts::eq),
                    betweenTime(requestObject) };
        } else if(requestObject instanceof InstructorHistoryViewRequestVO) { /** 강사 관리 - 주요 이력 */
            return new Predicate[]{ condition(((InstructorHistoryViewRequestVO) requestObject).getSequenceNo(), instructorHist.sequenceNo::eq), };
        } else if(requestObject instanceof InstructorQualificationViewRequestVO) { /** 강사 관리 - 자격증 */
            return new Predicate[]{ condition(((InstructorQualificationViewRequestVO) requestObject).getSequenceNo(), instructorQlfc.sequenceNo::eq), };
        } else if(requestObject instanceof OrganizationViewRequestVO) { /** 기관 정보 */
            return new Predicate[] { condition(((OrganizationViewRequestVO) requestObject).getOrganizationCode(), organizationInfo.organizationCode::eq),
                    condition(((OrganizationViewRequestVO) requestObject).getOrganizationType(), organizationInfo.organizationType::eq),
                    condition(((OrganizationViewRequestVO) requestObject).getNotOrganizationType(), organizationInfo.organizationType::ne),
                    condition(((OrganizationViewRequestVO) requestObject).getIsUsable(), organizationInfo.isUsable::eq),
                    condition(((OrganizationViewRequestVO) requestObject).getOrganizationName(), organizationInfo.organizationName::contains),
                    condition(((OrganizationViewRequestVO) requestObject).getOrganizationAddress1(), organizationInfo.organizationAddress1::contains),
                    condition(((OrganizationViewRequestVO) requestObject).getBizLicenseNumber(), organizationInfo.bizLicenseNumber::contains)};
        } else if(requestObject instanceof OrganizationMediaViewRequestVO) { /** 기관 정보 미디어 */
            return new Predicate[] { condition(((OrganizationMediaViewRequestVO) requestObject).getOrganizationCode(), organizationInfoMedia.organizationCode::eq),
                    condition(((OrganizationMediaViewRequestVO) requestObject).getMediaClsName1(), organizationInfoMedia.mediaClsName1::eq),
                    condition(((OrganizationMediaViewRequestVO) requestObject).getIsUsable(), organizationInfoMedia.isUsable::eq),
                    condition(((OrganizationMediaViewRequestVO) requestObject).getMediaName(), organizationInfoMedia.mediaName::contains),
                    condition(((OrganizationMediaViewRequestVO) requestObject).getOrganizationName(), organizationInfo.organizationName::contains),
                    condition(((OrganizationMediaViewRequestVO) requestObject).getBizLicenseNumber(), organizationInfo.bizLicenseNumber::contains)};
        } else if(requestObject instanceof AdminUserViewRequestVO) { /** 어드민 계정 관리 */
            if (((AdminUserViewRequestVO) requestObject).getManager() != null) {
                return new Predicate[]{ searchContainText(requestObject, ((AdminUserViewRequestVO) requestObject).getContainTextType(), ((AdminUserViewRequestVO) requestObject).getContainText())};
            } else {
                String[] roleGroups = null;
                if (((AdminUserViewRequestVO) requestObject).getRoleGroups() != null && !((AdminUserViewRequestVO) requestObject).getRoleGroups().isEmpty()) {
                    roleGroups = ((AdminUserViewRequestVO) requestObject).getRoleGroups().split(",");
                }

                return new Predicate[]{condition(((AdminUserViewRequestVO) requestObject).getUserSerialNo(), adminUser.userSerialNo::eq),
                        condition(roleGroups, adminUser.roleGroup::in),
                        condition(((AdminUserViewRequestVO) requestObject).getUserId(), adminUser.userId::eq),
                        condition(((AdminUserViewRequestVO) requestObject).getIsLock(), adminUser.isLock::eq),
                        condition(((AdminUserViewRequestVO) requestObject).getState(), adminUser.state::eq),
                        searchContainText(requestObject, ((AdminUserViewRequestVO) requestObject).getContainTextType(), ((AdminUserViewRequestVO) requestObject).getContainText())};
            }
        } else if(requestObject instanceof InstructorCareerRequestVO) { /** 강의 이력 */
            if (((InstructorCareerRequestVO) requestObject).getInstrCtgr() != null) {
                if (((InstructorCareerRequestVO) requestObject).getInstrCtgr().contains(Code.INSTR_CTGR.INSTR.enumCode)) {
                    if (((InstructorCareerRequestVO) requestObject).getCategory() != null) {
                        if(((InstructorCareerRequestVO) requestObject).getCategory().equals("instr")) {
                            List<BizInstructorAsgnm> subBizInstructorAsgnms = jpaQueryFactory.selectFrom(bizInstructorAsgnm)
                                    .innerJoin(bizInstructorAply).on(bizInstructorAply.bizInstrAplyNo.eq(bizInstructorAsgnm.bizInstrAplyNo))
                                    .innerJoin(bizOrganizationAplyDtl).on(bizOrganizationAplyDtl.bizOrgAplyDtlNo.eq(bizInstructorAsgnm.bizOrgAplyDtlNo))
                                    .where(bizInstructorAply.bizInstrAplyInstrId.eq(((InstructorCareerRequestVO) requestObject).getUserId()))
                                    .fetch();

                            /** 그룹화 */
                            Map<String, List<BizInstructorAsgnm>> groupBizInstructorAsgnms = subBizInstructorAsgnms.stream()
                                    .collect(Collectors.groupingBy(BizInstructorAsgnm::getBizInstrAplyNo));
                            List<String> instructorAsgnmNos = new ArrayList<>();
                            groupBizInstructorAsgnms.forEach((key, value) -> {
                                instructorAsgnmNos.add(value.get(0).getBizInstrAsgnmNo());
                            });

                            return new Predicate[] {
                                    condition(instructorAsgnmNos, bizInstructorAsgnm.bizInstrAsgnmNo::in),
                                    condition(4, bizInstructorIdentify.bizInstrIdntyStts::eq)};
                        } else {
                            List<LectureLecturer> subLecturers = jpaQueryFactory.selectFrom(lectureLecturer)
                                    .where(lectureLecturer.lecturer.eq(((InstructorCareerRequestVO) requestObject).getInstrSerialNo().toString()))
                                    .fetch();
                            /** 그룹화 */
                            Map<String, List<LectureLecturer>> groupLectureLecturers = subLecturers.stream()
                                    .collect(Collectors.groupingBy(LectureLecturer::getEduPlanCode));
                            List<String> instructorEduNos = new ArrayList<>();
                            groupLectureLecturers.forEach((key, value) -> {
                                instructorEduNos.add(value.get(0).getEduPlanCode());
                            });

                            return new Predicate[] {
                                    condition(instructorEduNos, educationPlan.educationPlanCode::in)};
                        }
                    } else {
                        return new Predicate[] {
                                condition(String.valueOf(((InstructorCareerRequestVO) requestObject).getYear()), educationPlan.yearOfEducationPlan::eq)};
                    }
                } else {
                    List<LectureLecturer> subLecturers = jpaQueryFactory.selectFrom(lectureLecturer)
                            .where(lectureLecturer.lecturer.eq(((InstructorCareerRequestVO) requestObject).getInstrSerialNo().toString()))
                            .fetch();
                    /** 그룹화 */
                    Map<String, List<LectureLecturer>> groupLectureLecturers = subLecturers.stream()
                            .collect(Collectors.groupingBy(LectureLecturer::getEduPlanCode));
                    List<String> instructorEduNos = new ArrayList<>();
                    groupLectureLecturers.forEach((key, value) -> {
                        instructorEduNos.add(value.get(0).getEduPlanCode());
                    });

                    return new Predicate[] {
                            condition(instructorEduNos, educationPlan.educationPlanCode::in)};
                }
            } else return null;
        } else if(requestObject instanceof InstructorLctrApiRequestVO) { /** 강의 이력(언론인 교육센터) */
            return new Predicate[] {
                    condition(((InstructorLctrApiRequestVO) requestObject).getLectrNo(), formeEdu2020LctrHistory.lectrNo::eq)
            };
        } else if(requestObject instanceof InstructorLctrViewRequestVO) { /** 강의 이력(언론인 교육센터) */
            return new Predicate[] {
                    condition(((InstructorLctrViewRequestVO) requestObject).getLectrNm(), formeEdu2020LctrHistory.lectrNm::contains),
                    condition(((InstructorLctrViewRequestVO) requestObject).getLctrNm(), formeEdu2020LctrHistory.lctrNm::contains),
                    condition(((InstructorLctrViewRequestVO) requestObject).getCrsCls1Nm(), formeEdu2020LctrHistory.crsCls1Nm::contains),
                    condition(((InstructorLctrViewRequestVO) requestObject).getCrsCls2Nm(), formeEdu2020LctrHistory.crsCls2Nm::contains),
                    condition(((InstructorLctrViewRequestVO) requestObject).getCrsNm(), formeEdu2020LctrHistory.crsNm::contains),
                    condition(((InstructorLctrViewRequestVO) requestObject).getCrsNo(), formeEdu2020LctrHistory.crsNo::eq),
                    condition(((InstructorLctrViewRequestVO) requestObject).getStartDate(), formeEdu2020LctrHistory.lctrDt::goe),
                    condition(((InstructorLctrViewRequestVO) requestObject).getEndDate(), formeEdu2020LctrHistory.lctrDt::loe),

            };
        } else {
            return new Predicate[]{};
        }
    }

    /** 조회 타입 별 포함 단어 조회 */
    private <T extends CSViewVOSupport> BooleanBuilder searchContainText(T requestObject, String containTextType, String containsText) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (hasText(containsText)) { /** 검색어가 존재할 경우 */
            if(requestObject instanceof AuthorityViewRequestVO) { /** 어드민 권한 관리 */

            } else if(requestObject instanceof WebAuthorityViewRequestVO) { /** 웹 회원 권한 관리 */

            } else if(requestObject instanceof WebUserViewRequestVO) { /** 웹 회원 관리 */
                if (((WebUserViewRequestVO) requestObject).getManager() != null) {
                    if(containTextType.equals("1")) { /** 이름 */
                        booleanBuilder.or(lmsUser.userName.contains(containsText));
                    } else if(containTextType.equals("2")) { /** 아이디 */
                        booleanBuilder.or(lmsUser.userId.contains(containsText));
                    } else if(containTextType.equals("3")) { /** 소속 */
                        booleanBuilder.or(lmsUser.organizationInfo.organizationName.contains(containsText));
                    } else { /** 전체(이름+아이디+핸드폰번호) */
                        booleanBuilder.or(lmsUser.userName.contains(containsText));
                        booleanBuilder.or(lmsUser.userId.contains(containsText));
                        booleanBuilder.or(lmsUser.organizationInfo.organizationName.contains(containsText));
                    }
                } else {

                }
            } else if(requestObject instanceof OrganizationViewRequestVO) { /** 기관 정보 */

            } else if(requestObject instanceof AdminUserViewRequestVO) { /** 어드민 계정 관리 */
                if (((AdminUserViewRequestVO) requestObject).getManager() != null) {
                    if(containTextType.equals("1")) { /** 이름 */
                        booleanBuilder.or(adminUser.userName.contains(containsText));
                    } else if(containTextType.equals("2")) { /** 아이디 */
                        booleanBuilder.or(adminUser.userId.contains(containsText));
                    } else if(containTextType.equals("3")) { /** 소속 */
                        booleanBuilder.or(adminUser.department.contains(containsText));
                    } else { /** 전체(이름+아이디+소속) */
                        booleanBuilder.or(adminUser.userName.contains(containsText));
                        booleanBuilder.or(adminUser.userId.contains(containsText));
                        booleanBuilder.or(adminUser.department.contains(containsText));
                    }
                } else {
                    if(containTextType.equals("1")) { /** 이름 */
                        booleanBuilder.or(adminUser.userName.contains(containsText));
                    } else if(containTextType.equals("2")) { /** 아이디 */
                        booleanBuilder.or(adminUser.userId.contains(containsText));
                    } else if(containTextType.equals("3")) { /** 핸드폰번호 */
                        booleanBuilder.or(adminUser.phone.contains(containsText));
                    } else { /** 전체(이름+아이디+핸드폰번호) */
                        booleanBuilder.or(adminUser.userName.contains(containsText));
                        booleanBuilder.or(adminUser.userId.contains(containsText));
                        booleanBuilder.or(adminUser.phone.contains(containsText));
                    }
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
            if(requestObject instanceof WebAuthorityViewRequestVO) { /** 웹 회원 권한 관리 */
                return null;
            } else if(requestObject instanceof WebUserViewRequestVO) { /** 웹 회원 관리 */
                if(!StringUtils.isEmpty(((WebUserViewRequestVO) requestObject).getJoinDate())) {
                    return lmsUser.createDateTime.between(startDateTime, endDateTime);
                } else if(!StringUtils.isEmpty(((WebUserViewRequestVO) requestObject).getLastLoginDate())) {
                    return lmsUser.lastLoginDateTime.between(startDateTime, endDateTime);
                } else {
                    return null;
                }
            } else if(requestObject instanceof InstructorViewRequestVO) { /** 강사 관리 */
                return null;
            } else if(requestObject instanceof OrganizationViewRequestVO) { /** 기관 정보 */
                return null;
            } else if(requestObject instanceof AdminUserViewRequestVO) { /** 어드민 계정 관리 */
                return null;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 기관 일련 번호 생성
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    public String generateOrganizationCode(String prefixCode) {
        return jpaQueryFactory.selectFrom(organizationInfo)
                .where(organizationInfo.organizationCode.like(prefixCode+"%"))
                .orderBy(organizationInfo.organizationCode.desc())
                .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                        .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getOrganizationCode().replace(prefixCode, "")) + 1), 7, "0"))
                        .toString())
                .orElse(new StringBuilder(prefixCode).append("0000000").toString());
    }

    /**
     * 기관 일련 번호 생성
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    public String generateMediaCode(String prefixCode) {
        return jpaQueryFactory.selectFrom(organizationInfoMedia)
                .where(organizationInfoMedia.mediaCode.like(prefixCode+"%"))
                .orderBy(organizationInfoMedia.mediaCode.desc())
                .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                        .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getMediaCode().replace(prefixCode, "")) + 1), 8, "0"))
                        .toString())
                .orElse(new StringBuilder(prefixCode).append("0000000").toString());
    }

    /** 시간 대 검색 */
    private <T extends CSEntitySupport> BooleanExpression betweenDate(T value, String startDate, String endDate, String type) {

        if((!StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate))
                || (StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate))) {
            throw new KPFException(KPF_RESULT.ERROR9001, "조회 시작 일자 & 조회 종료 일자는 한가지만 존재할 수 없습니다.");
        } else if(!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
            if (value instanceof OrganizationAuthorityHistory) {
                if (type.equals("Apply")) {
                    DateExpression<Date> expression = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", organizationAuthorityHistory.createDateTime, "%Y-%m-%d");
                    DateExpression<Date> expressionStart = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", startDate, "%Y-%m-%d");
                    DateExpression<Date> expressionEnd = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", endDate, "%Y-%m-%d");
                    return expression.between(expressionStart, expressionEnd);
                }else if (type.equals("Apprv")){
                    DateExpression<Date> expression = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", organizationAuthorityHistory.updateDateTime, "%Y-%m-%d");
                    DateExpression<Date> expressionStart = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", startDate, "%Y-%m-%d");
                    DateExpression<Date> expressionEnd = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", endDate, "%Y-%m-%d");
                    return expression.between(expressionStart, expressionEnd);
                }
            } else if (value instanceof IndividualAuthorityHistory) {
                if (type.equals("Apply")) {
                    DateExpression<Date> expression = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", individualAuthorityHistory.createDateTime, "%Y-%m-%d");
                    DateExpression<Date> expressionStart = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", startDate, "%Y-%m-%d");
                    DateExpression<Date> expressionEnd = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", endDate, "%Y-%m-%d");
                    return expression.between(expressionStart, expressionEnd);
                }else if (type.equals("Apprv")){
                    DateExpression<Date> expression = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", individualAuthorityHistory.updateDateTime, "%Y-%m-%d");
                    DateExpression<Date> expressionStart = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", startDate, "%Y-%m-%d");
                    DateExpression<Date> expressionEnd = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", endDate, "%Y-%m-%d");
                    return expression.between(expressionStart, expressionEnd);
                }
            }
        } else {
            return null;
        }
        return null;
    }

    /** 날짜 비교 */
    private <T extends CSEntitySupport> BooleanExpression checkPeriod(T value, String date, String type) {
        DateExpression<Date> expressionCheck = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", date, "%Y-%m-%d");

        if (value instanceof OrganizationAuthorityHistory) {
            if (type.equals("goeApply")) {
                DateExpression<Date> expression = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", organizationAuthorityHistory.createDateTime, "%Y-%m-%d");
                return expression.goe(expressionCheck);

            } else if (type.equals("loeApply")) {
                DateExpression<Date> expression = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", organizationAuthorityHistory.createDateTime, "%Y-%m-%d");
                return expression.loe(expressionCheck);

            } else if (type.equals("goeApprv")) {
                DateExpression<Date> expression = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", organizationAuthorityHistory.updateDateTime, "%Y-%m-%d");
                return expression.goe(expressionCheck);

            } else if (type.equals("loeApprv")) {
                DateExpression<Date> expression = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", organizationAuthorityHistory.updateDateTime, "%Y-%m-%d");
                return expression.loe(expressionCheck);

            } else return null;
        } else if (value instanceof IndividualAuthorityHistory) {
            if (type.equals("goeApply")) {
                DateExpression<Date> expression = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", individualAuthorityHistory.createDateTime, "%Y-%m-%d");
                return expression.goe(expressionCheck);

            } else if (type.equals("loeApply")) {
                DateExpression<Date> expression = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", individualAuthorityHistory.createDateTime, "%Y-%m-%d");
                return expression.loe(expressionCheck);

            } else if (type.equals("goeApprv")) {
                DateExpression<Date> expression = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", individualAuthorityHistory.updateDateTime, "%Y-%m-%d");
                return expression.goe(expressionCheck);

            } else if (type.equals("loeApprv")) {
                DateExpression<Date> expression = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", individualAuthorityHistory.updateDateTime, "%Y-%m-%d");
                return expression.loe(expressionCheck);

            } else return null;
        } else return null;
    }
}