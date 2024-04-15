package kr.or.kpf.lms.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.or.kpf.lms.biz.business.apply.vo.response.BizAplyExcelVO;
import kr.or.kpf.lms.biz.business.apply.vo.response.BizAplyFreeExcelVO;
import kr.or.kpf.lms.biz.business.instructor.aply.vo.request.BizInstructorAplyApiRequestVO;
import kr.or.kpf.lms.biz.business.instructor.aply.vo.request.BizInstructorAplyViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.clcln.ddln.vo.request.BizInstructorClclnDdlnViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.clcln.ddln.vo.response.BizInstructorClclnDdlnExcelVO;
import kr.or.kpf.lms.biz.business.instructor.dist.crtramt.item.vo.request.BizInstructorDistCrtrAmtItemViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.dist.crtramt.vo.request.BizInstructorDistCrtrAmtViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.dist.vo.request.BizInstructorDistViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.dist.vo.response.BizInstructorDistExcelVO;
import kr.or.kpf.lms.biz.business.instructor.identify.dtl.vo.request.BizInstructorIdentifyDtlExcelRequestVO;
import kr.or.kpf.lms.biz.business.instructor.identify.dtl.vo.request.BizInstructorIdentifyDtlViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.identify.dtl.vo.response.BizInstructorIdentifyDtlExcelVO;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.request.*;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.response.BizInstructorAmtTimeVO;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.response.BizInstructorIdentifyExcelVO;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.response.BizInstructorIdentifyManageApiResponseVO;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.response.BizInstructorIdentifyViewResponseVO;
import kr.or.kpf.lms.biz.business.instructor.question.answer.vo.request.BizInstructorQuestionAnswerViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.question.vo.request.BizInstructorQuestionViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.vo.request.BizInstructorViewRequestVO;
import kr.or.kpf.lms.biz.business.instructor.vo.response.BizInstructorExcelVO;
import kr.or.kpf.lms.biz.business.instructor.vo.response.BizInstructorTestExcelVO;
import kr.or.kpf.lms.biz.business.organization.aply.edithist.vo.request.BizEditHistViewRequestVO;
import kr.or.kpf.lms.biz.business.organization.aply.vo.request.BizOrganizationAplyApiRequestVO;
import kr.or.kpf.lms.biz.business.organization.aply.vo.request.BizOrganizationAplyCustomViewRequestVO;
import kr.or.kpf.lms.biz.business.organization.aply.vo.request.BizOrganizationAplyViewRequestVO;
import kr.or.kpf.lms.biz.business.organization.aply.vo.response.BizOrganizationAplyDistExcelVO;
import kr.or.kpf.lms.biz.business.organization.aply.vo.response.BizOrganizationAplyExcelVO;
import kr.or.kpf.lms.biz.business.apply.vo.request.BizAplyViewRequestVO;
import kr.or.kpf.lms.biz.business.organization.rpt.vo.request.BizOrganizationRsltRptSttsApiRequestVO;
import kr.or.kpf.lms.biz.business.organization.rpt.vo.request.BizOrganizationRsltRptViewRequestVO;
import kr.or.kpf.lms.biz.business.organization.rpt.vo.response.BizOrganizationRsltRptExcelVO;
import kr.or.kpf.lms.biz.business.pbanc.master.vo.request.BizPbancViewRequestVO;
import kr.or.kpf.lms.biz.business.pbanc.master.vo.response.BizPbancCustomApiResponseVO;
import kr.or.kpf.lms.biz.business.pbanc.rslt.vo.request.BizPbancRsltViewRequestVO;
import kr.or.kpf.lms.biz.business.pbanc.rslt.vo.response.BizPbancRsltCustomApiResponseVO;
import kr.or.kpf.lms.biz.business.pbanc.rslt.vo.response.BizPbancRsltOrgsApiResponseVO;
import kr.or.kpf.lms.biz.business.survey.ans.vo.request.BizSurveyAnsViewRequestVO;
import kr.or.kpf.lms.biz.business.survey.qitem.vo.request.BizSurveyQitemViewRequestVO;
import kr.or.kpf.lms.biz.business.survey.qitem.vo.response.BizSurveyQitemExcelVO;
import kr.or.kpf.lms.biz.business.survey.vo.request.BizSurveyViewRequestVO;
import kr.or.kpf.lms.biz.business.survey.vo.response.BizSurveyExcelVO;
import kr.or.kpf.lms.biz.user.history.vo.FormeBizlecinfoViewRequestVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSEntitySupport;
import kr.or.kpf.lms.common.support.CSRepositorySupport;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.ResponseSummary;
import kr.or.kpf.lms.repository.entity.*;
import kr.or.kpf.lms.repository.entity.system.CommonCodeMaster;
import kr.or.kpf.lms.repository.entity.role.OrganizationAuthorityHistory;
import kr.or.kpf.lms.repository.entity.user.LmsUser;
import kr.or.kpf.lms.repository.entity.user.OrganizationInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static kr.or.kpf.lms.repository.entity.QBizAplyDtl.bizAplyDtl;
import static kr.or.kpf.lms.repository.entity.QBizEditHist.bizEditHist;
import static kr.or.kpf.lms.repository.entity.QBizInstructor.bizInstructor;
import static kr.or.kpf.lms.repository.entity.QBizInstructorAply.bizInstructorAply;
import static kr.or.kpf.lms.repository.entity.QBizInstructorAsgnm.bizInstructorAsgnm;
import static kr.or.kpf.lms.repository.entity.QBizInstructorClclnDdln.bizInstructorClclnDdln;
import static kr.or.kpf.lms.repository.entity.QBizInstructorDist.bizInstructorDist;
import static kr.or.kpf.lms.repository.entity.QBizInstructorDistCrtrAmt.bizInstructorDistCrtrAmt;
import static kr.or.kpf.lms.repository.entity.QBizInstructorDistCrtrAmtItem.bizInstructorDistCrtrAmtItem;
import static kr.or.kpf.lms.repository.entity.QBizInstructorIdentify.bizInstructorIdentify;
import static kr.or.kpf.lms.repository.entity.QBizInstructorIdentifyDtl.bizInstructorIdentifyDtl;
import static kr.or.kpf.lms.repository.entity.QBizInstructorPbanc.bizInstructorPbanc;
import static kr.or.kpf.lms.repository.entity.QBizInstructorQuestion.bizInstructorQuestion;
import static kr.or.kpf.lms.repository.entity.QBizInstructorQuestionAnswer.bizInstructorQuestionAnswer;
import static kr.or.kpf.lms.repository.entity.QBizAply.bizAply;
import static kr.or.kpf.lms.repository.entity.QBizOrganizationAply.bizOrganizationAply;
import static kr.or.kpf.lms.repository.entity.QBizOrganizationAplyDtl.bizOrganizationAplyDtl;
import static kr.or.kpf.lms.repository.entity.QBizOrganizationRsltRpt.bizOrganizationRsltRpt;
import static kr.or.kpf.lms.repository.entity.QBizPbancMaster.bizPbancMaster;
import static kr.or.kpf.lms.repository.entity.QBizPbancResult.bizPbancResult;
import static kr.or.kpf.lms.repository.entity.QBizPbancTmpl0.bizPbancTmpl0;
import static kr.or.kpf.lms.repository.entity.QBizPbancTmpl0Item.bizPbancTmpl0Item;
import static kr.or.kpf.lms.repository.entity.QBizPbancTmpl0Trgt.bizPbancTmpl0Trgt;
import static kr.or.kpf.lms.repository.entity.QBizPbancTmpl1.bizPbancTmpl1;
import static kr.or.kpf.lms.repository.entity.QBizPbancTmpl1Trgt.bizPbancTmpl1Trgt;
import static kr.or.kpf.lms.repository.entity.QBizPbancTmpl2.bizPbancTmpl2;
import static kr.or.kpf.lms.repository.entity.QBizPbancTmpl3.bizPbancTmpl3;
import static kr.or.kpf.lms.repository.entity.QBizPbancTmpl4.bizPbancTmpl4;
import static kr.or.kpf.lms.repository.entity.QBizPbancTmpl5.bizPbancTmpl5;
import static kr.or.kpf.lms.repository.entity.QBizSurvey.bizSurvey;
import static kr.or.kpf.lms.repository.entity.QBizSurveyAns.bizSurveyAns;
import static kr.or.kpf.lms.repository.entity.QBizSurveyMaster.bizSurveyMaster;
import static kr.or.kpf.lms.repository.entity.QBizSurveyQitem.bizSurveyQitem;
import static kr.or.kpf.lms.repository.entity.QBizSurveyQitemItem.bizSurveyQitemItem;
import static kr.or.kpf.lms.repository.entity.QFileMaster.fileMaster;
import static kr.or.kpf.lms.repository.entity.QFormeBizlecinfo.formeBizlecinfo;
import static kr.or.kpf.lms.repository.entity.QFormeFomBizapplyTtable.formeFomBizapplyTtable;
import static kr.or.kpf.lms.repository.entity.system.QCommonCodeMaster.commonCodeMaster;
import static kr.or.kpf.lms.repository.entity.user.QLmsUser.lmsUser;
import static kr.or.kpf.lms.repository.entity.user.QOrganizationInfo.organizationInfo;
import static org.springframework.util.StringUtils.hasText;
import static kr.or.kpf.lms.repository.entity.role.QOrganizationAuthorityHistory.organizationAuthorityHistory;

/**
 * 사업 공고 공통 Repository 구현체
 */
@Repository
public class CommonBusinessRepositoryImpl extends CSRepositorySupport implements CommonBusinessRepository {

    private final JPAQueryFactory jpaQueryFactory;
    public CommonBusinessRepositoryImpl(JPAQueryFactory jpaQueryFactory){ this.jpaQueryFactory = jpaQueryFactory; }

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

    @Override
    public <T extends CSViewVOSupport> List<?> findEntityListExcel(T requestObject) {
        return getEntityListExcel(requestObject);
    }

    @Override
    public <T> Object findEntity(T requestObject) {
        return getEntity(requestObject);
    }

    @Override
    public <T> List<?> updateSttsEntityList(T requestObject) {
        if(requestObject instanceof BizOrganizationRsltRptSttsApiRequestVO) { /** 결과보고서 상태 업데이트 */
            long execute = jpaQueryFactory.update(bizOrganizationRsltRpt)
                    .set(bizOrganizationRsltRpt.bizOrgRsltRptStts, ((BizOrganizationRsltRptSttsApiRequestVO) requestObject).getBizOrgRsltRptStts())
                    .where(bizOrganizationRsltRpt.bizOrgRsltRptNo.in(((BizOrganizationRsltRptSttsApiRequestVO) requestObject).getBizOrgRsltRptNos()))
                    .execute();
            if (execute > 0){
                return jpaQueryFactory.selectFrom(bizOrganizationRsltRpt)
                        .where(bizOrganizationRsltRpt.bizOrgRsltRptNo.in(((BizOrganizationRsltRptSttsApiRequestVO) requestObject).getBizOrgRsltRptNos()))
                        .fetch();
            }
        }
        return null;
    }

    @Override
    public <T> Long countEntity(T requestObject) {
        if(requestObject instanceof BizInstructorAplyApiRequestVO) {
            return jpaQueryFactory.select(bizInstructorAply.count())
                    .from(bizInstructorAply)
                    .where(bizInstructorAply.bizInstrAplyStts.eq(1), bizInstructorAply.bizInstrAplyInstrId.eq(((BizInstructorAplyApiRequestVO) requestObject).getLoginUserId()))
                    .fetchOne();
        } else if(requestObject instanceof BizOrganizationAplyApiRequestVO) {
            return jpaQueryFactory.select(bizOrganizationAply.count())
                    .from(bizOrganizationAply)
                    .where(bizOrganizationAply.bizOrgAplyStts.eq(2), bizOrganizationAply.bizPbancNo.eq(((BizOrganizationAplyApiRequestVO) requestObject).getBizPbancNo()))
                    .fetchOne();
        }
        return 0L;
    }

    /**
     * Entity 총 갯수
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    private <T extends CSViewVOSupport> Long getEntityCount(T requestObject) {
        if(requestObject instanceof BizPbancViewRequestVO) { /** 사업 공고 */
            return jpaQueryFactory.select(bizPbancMaster.count())
                    .from(bizPbancMaster)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof BizPbancRsltViewRequestVO) { /** 사업 공고 결과 */
            return jpaQueryFactory.select(bizPbancResult.count())
                    .from(bizPbancResult)
                    .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizPbancResult.bizPbancNo))
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof BizOrganizationAplyViewRequestVO) { /** 사업 공고 신청 */
            return jpaQueryFactory.select(bizOrganizationAply.count())
                    .from(bizOrganizationAply)
                    .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                    .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof BizEditHistViewRequestVO) { /** 사업 공고 신청 변경 이력 */
            return jpaQueryFactory.select(bizEditHist.count())
                    .from(bizEditHist)
                    .leftJoin(bizOrganizationAply).on(bizOrganizationAply.bizOrgAplyNo.eq(bizEditHist.bizEditHistTrgtNo))
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof BizAplyViewRequestVO) { /** 사업 공고 신청 - 언론인/기본형 */
            return jpaQueryFactory.select(bizAply.count())
                    .from(bizAply)
                    .leftJoin(lmsUser).on(lmsUser.userId.eq(bizAply.bizAplyUserID))
                    .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(lmsUser.organizationCode))
                    .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizAply.bizPbancNo))
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof BizInstructorViewRequestVO) { /** 강사 모집 */
            return jpaQueryFactory.select(bizInstructor.count())
                    .from(bizInstructor)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof BizInstructorAplyViewRequestVO) { /** 강사 신청 */
            return jpaQueryFactory.select(bizOrganizationAply.count())
                    .from(bizOrganizationAply)
                    .leftJoin(bizInstructorPbanc).on(bizInstructorPbanc.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof BizOrganizationAplyCustomViewRequestVO) { /** 강사 배정 */
            List<Long> countDto =jpaQueryFactory.select(bizOrganizationAply.count())
                    .from(bizOrganizationAply)
                    .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                    .leftJoin(bizInstructorPbanc).on(bizInstructorPbanc.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                    .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                    .leftJoin(bizInstructorAsgnm).on(bizInstructorAsgnm.bizOrgAplyNo.eq(bizOrganizationAply.bizOrgAplyNo))
                    .leftJoin(bizInstructorAply).on(bizInstructorAply.bizInstrAplyNo.eq(bizInstructorAsgnm.bizInstrAplyNo))
                    .where(getQuery(requestObject))
                    .groupBy(bizOrganizationAply.bizOrgAplyNo)
                    .fetch();
            Long result = Long.valueOf(0);
            for(Long count : countDto){
                result=result + 1;
            }
            return result;
        } else if(requestObject instanceof BizInstructorQuestionViewRequestVO) { /** 강사 지원 문의 */
            return jpaQueryFactory.select(bizInstructorQuestion.count())
                    .from(bizInstructorQuestion)
                    .leftJoin(bizOrganizationAply).on(bizOrganizationAply.bizOrgAplyNo.eq(bizInstructorQuestion.bizOrgAplyNo))
                    .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                    .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof BizInstructorQuestionAnswerViewRequestVO) { /** 강사 지원 문의 답변 */
            return jpaQueryFactory.select(bizInstructorQuestionAnswer.count())
                    .from(bizInstructorQuestionAnswer)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof BizInstructorDistViewRequestVO) { /** 거리 증빙 */
            return jpaQueryFactory.select(bizInstructorDist.count())
                    .from(bizInstructorDist)
                    .leftJoin(bizOrganizationAply).on(bizOrganizationAply.bizOrgAplyNo.eq(bizInstructorDist.bizOrgAplyNo))
                    .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                    .leftJoin(bizInstructorAply).on(bizInstructorAply.bizInstrAplyNo.eq(bizInstructorDist.bizInstrAplyNo))
                    .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof BizInstructorIdentifyViewRequestVO) { /** 강의확인서 */
            return jpaQueryFactory.select(bizInstructorIdentify.count())
                    .from(bizInstructorIdentify)
                    .leftJoin(bizOrganizationAply).on(bizOrganizationAply.bizOrgAplyNo.eq(bizInstructorIdentify.bizOrgAplyNo))
                    .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                    .leftJoin(lmsUser).on(lmsUser.userId.eq(bizInstructorIdentify.registUserId))
                    .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                    .where(getQuery(requestObject))
                    .fetchOne();
        }else if(requestObject instanceof  FormeBizlecinfoViewRequestVO) { /** 포미 강의확인서 View */
            return  jpaQueryFactory.select(formeBizlecinfo.count())
                    .from(formeBizlecinfo)
                    .where(getQuery(requestObject))
                    .fetchOne();
        }else if(requestObject instanceof  FormeBizlecinfoApiRequestVO) { /** 포미 강의확인서 API */
            return  jpaQueryFactory.select(formeBizlecinfo.count())
                    .from(formeBizlecinfo)
                    .where(getQuery(requestObject))
                    .fetchOne();
        }else if(requestObject instanceof  FormeBizlecinfoDetailApiRequestVO) { /** 포미 강의확인서 API */
            return  jpaQueryFactory.select(formeBizlecinfo.count())
                    .from(formeBizlecinfo)
                    .where(getQuery(requestObject))
                    .fetchOne();
        }else if(requestObject instanceof MyInstructorStateViewRequestVO) { /** 강사 참여 현황 - 강의 현황 - 진행 중 */
            List<Long> countDto = jpaQueryFactory.select(bizInstructorAply.count())
                    .from(bizInstructorAply)
                    .innerJoin(bizOrganizationAply).on(bizOrganizationAply.bizOrgAplyNo.eq(bizInstructorAply.bizOrgAplyNo))
                    .innerJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                    .innerJoin(bizInstructorAsgnm).on(bizInstructorAsgnm.bizInstrAplyNo.eq(bizInstructorAply.bizInstrAplyNo))
                    .innerJoin(bizOrganizationAplyDtl).on(bizOrganizationAplyDtl.bizOrgAplyDtlNo.eq(bizInstructorAsgnm.bizOrgAplyDtlNo))
                    .leftJoin(bizInstructorIdentify).on(bizInstructorIdentify.bizOrgAplyNo.eq(bizOrganizationAply.bizOrgAplyNo),
                            bizInstructorIdentify.bizInstrIdntyYm.startsWith(bizOrganizationAplyDtl.bizOrgAplyLsnDtlYmd.substring(0,4)),
                            bizInstructorIdentify.bizInstrIdntyYm.endsWith(bizOrganizationAplyDtl.bizOrgAplyLsnDtlYmd.substring(5,7)),
                            bizInstructorIdentify.registUserId.eq(bizInstructorAply.bizInstrAplyInstrId))
                    .where(getQuery(requestObject))
                    .groupBy(bizInstructorAply.bizInstrAplyNo, bizOrganizationAplyDtl.bizOrgAplyLsnDtlYmd.substring(0, 7))
                    .fetch();
            Long result = Long.valueOf(0);
            for(Long count : countDto){
                result=result + count;
            }
            return result;
        } else if(requestObject instanceof BizInstructorIdentifyCalculateViewRequestVO) { /** 강의확인서 */
            List<Long> resultList;
            if(((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getGroupType() !=null &&
                    ((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getGroupType().equals("orgExcel")){
                /** 정산금액 조회 엑셀 기관별이면 강의확인서 없어도 출력 */
                resultList = jpaQueryFactory.select(bizInstructorAply.count())
                        .from(bizOrganizationAply)
                        .innerJoin(bizInstructorAply).on(bizInstructorAply.bizOrgAplyNo.eq(bizOrganizationAply.bizOrgAplyNo))
                        .innerJoin(bizPbancMaster).on(bizOrganizationAply.bizPbancNo.eq(bizPbancMaster.bizPbancNo))
                        .innerJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                        .where(getQuery(requestObject))
                        .groupBy(bizInstructorAply.bizInstrAplyNo)
                        .fetch();
            } else{
                resultList = jpaQueryFactory.select(bizInstructorAply.count())
                        .from(bizInstructorAply)
                        .innerJoin(bizOrganizationAply).on(bizOrganizationAply.bizOrgAplyNo.eq(bizInstructorAply.bizOrgAplyNo))
                        .innerJoin(bizInstructorIdentify).on(bizInstructorIdentify.bizOrgAplyNo.eq(bizInstructorAply.bizOrgAplyNo).
                                and(bizInstructorIdentify.registUserId.eq(bizInstructorAply.bizInstrAplyInstrId)))
                        .innerJoin(bizPbancMaster).on(bizOrganizationAply.bizPbancNo.eq(bizPbancMaster.bizPbancNo))
                        .innerJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                        .where(getQuery(requestObject))
                        .groupBy(bizInstructorAply.bizInstrAplyNo)
                        .fetch();
            }
            Long result = Long.valueOf(0);
            for(Long count:resultList){
                result = result+1;
            }
            return result;
        } else if(requestObject instanceof BizInstructorIdentifyDtlViewRequestVO) { /** 강의확인서 강의시간표 */
            return jpaQueryFactory.select(bizInstructorIdentifyDtl.count())
                    .from(bizInstructorIdentifyDtl)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof BizInstructorIdentifyManageViewRequestVO) { /** 강의확인서 관리 */
            return jpaQueryFactory.select(bizInstructorAply.count())
                    .from(bizInstructorAply)
                    .leftJoin(lmsUser).on(lmsUser.userId.eq(bizInstructorAply.bizInstrAplyInstrId))
                    .leftJoin(bizOrganizationAply).on(bizOrganizationAply.bizOrgAplyNo.eq(bizInstructorAply.bizOrgAplyNo))
                    .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                    .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                    .where(getQuery(requestObject))
                    .distinct()
                    .fetchOne();
        } else if(requestObject instanceof BizSurveyViewRequestVO) { /** 상호평가 평가지 */
            return jpaQueryFactory.select(bizSurvey.count())
                    .from(bizSurvey)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof BizSurveyQitemViewRequestVO) { /** 상호평가 문항 */
            return jpaQueryFactory.select(bizSurveyQitem.count())
                    .from(bizSurveyQitem)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof BizOrganizationRsltRptViewRequestVO) { /** 결과보고서 */
            return jpaQueryFactory.select(bizOrganizationRsltRpt.count())
                    .from(bizOrganizationRsltRpt)
                    .leftJoin(bizOrganizationAply).on(bizOrganizationAply.bizOrgAplyNo.eq(bizOrganizationRsltRpt.bizOrgAplyNo))
                    .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                    .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationRsltRpt.orgCd))
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof BizSurveyAnsViewRequestVO) { /** 상호평가 답변 */
            return jpaQueryFactory.select(bizSurveyAns.count())
                    .from(bizSurveyAns)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof BizInstructorClclnDdlnViewRequestVO) { /** 정산 마감일 */
            return jpaQueryFactory.select(bizInstructorClclnDdln.count())
                    .from(bizInstructorClclnDdln)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof BizInstructorDistCrtrAmtViewRequestVO) { /** 이동거리 기준단가 */
            return jpaQueryFactory.select(bizInstructorDistCrtrAmt.count())
                    .from(bizInstructorDistCrtrAmt)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof BizInstructorDistCrtrAmtItemViewRequestVO) { /** 이동거리 기준단가 항목 */
            return jpaQueryFactory.select(bizInstructorDistCrtrAmtItem.count())
                    .from(bizInstructorDistCrtrAmtItem)
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
    private <T> Object getEntity(T requestObject) {
        if(requestObject instanceof BizPbancViewRequestVO) { /** 사업 공고 */
            BizPbancCustomApiResponseVO dto = jpaQueryFactory.select(Projections.fields(BizPbancCustomApiResponseVO.class,
                            bizPbancMaster.bizPbancNo,
                            bizPbancMaster.bizPbancType,
                            bizPbancMaster.bizPbancCtgr,
                            bizPbancMaster.bizPbancNm,
                            bizPbancMaster.bizPbancMaxTm,
                            bizPbancMaster.bizPbancYr,
                            bizPbancMaster.bizPbancRnd,
                            bizPbancMaster.bizPbancMaxInst,
                            bizPbancMaster.bizPbancSlctnMeth,
                            bizPbancMaster.bizPbancInstrSlctnMeth,
                            bizPbancMaster.bizPbancStts,
                            bizPbancMaster.bizPbancRcptBgng,
                            bizPbancMaster.bizPbancRcptEnd,
                            bizPbancMaster.bizPbancSprtBgng,
                            bizPbancMaster.bizPbancSprtEnd,
                            bizPbancMaster.bizPbancCn,
                            bizPbancMaster.isTop,
                            bizPbancMaster.createDateTime,
                            bizPbancMaster.registUserId,
                            bizPbancMaster.updateDateTime,
                            bizPbancMaster.modifyUserId,
                            bizPbancResult.bizPbancRsltNo,
                            lmsUser.userName
                    ))
                    .from(bizPbancMaster)
                    .leftJoin(lmsUser).on(lmsUser.userId.eq(bizPbancMaster.registUserId))
                    .leftJoin(bizPbancResult).on(bizPbancResult.bizPbancNo.eq(bizPbancMaster.bizPbancNo))
                    .where(bizPbancMaster.bizPbancNo.eq(((BizPbancViewRequestVO) requestObject).getBizPbancNo()))
                    .fetchOne();
            if (dto != null) {
                dto.setFileMasters(
                        jpaQueryFactory.selectFrom(fileMaster)
                                .where(fileMaster.atchFileSn.eq(dto.getBizPbancNo()))
                                .fetch()
                );
            }
            return dto;

        } else if(requestObject instanceof BizPbancRsltViewRequestVO) { /** 사업 공고 결과 */
            return jpaQueryFactory.select(Projections.fields(BizPbancRsltCustomApiResponseVO.class,
                            bizPbancResult.bizPbancRsltNo,
                            bizPbancResult.bizPbancNo,
                            bizPbancResult.bizPbancRsltCn,
                            bizPbancResult.bizPbancNtcYn,
                            bizPbancResult.bizPbancRsltFile,
                            bizPbancMaster.bizPbancType,
                            bizPbancResult.registUserId,
                            bizPbancMaster.bizPbancCtgr,
                            bizPbancMaster.bizPbancNm,
                            lmsUser.userName
                    ))
                    .from(bizPbancResult)
                    .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizPbancResult.bizPbancNo))
                    .leftJoin(lmsUser).on(lmsUser.userId.eq(bizPbancResult.registUserId))
                    .where(bizPbancResult.bizPbancRsltNo.eq(((BizPbancRsltViewRequestVO) requestObject).getBizPbancRsltNo()))
                    .fetchOne();

        } else if(requestObject instanceof BizInstructorViewRequestVO) { /** 강사 모집 */
            return jpaQueryFactory.selectFrom(bizInstructor)
                    .where(bizInstructor.bizInstrNo.eq(((BizInstructorViewRequestVO) requestObject).getBizInstrNo()))
                    .fetchOne();
        } else if(requestObject instanceof BizInstructorAplyViewRequestVO) { /** 강사 신청 */
            return jpaQueryFactory.selectFrom(bizOrganizationAply)
                    .where(bizInstructorPbanc.bizPbancNo.eq(((BizInstructorAplyViewRequestVO) requestObject).getBizOrgAplyNo()))
                    .fetchOne();
        } else if(requestObject instanceof BizOrganizationAplyCustomViewRequestVO) { /** 강사 배정 */
            return jpaQueryFactory.selectFrom(bizOrganizationAply)
                    .where(bizInstructorPbanc.bizPbancNo.eq(((BizOrganizationAplyCustomViewRequestVO) requestObject).getBizOrgAplyNo()))
                    .fetchOne();
        } else if(requestObject instanceof BizInstructorIdentifyViewRequestVO) { /** 강의확인서 */
            return jpaQueryFactory.selectFrom(bizInstructorIdentify)
                    .where(bizInstructorIdentify.bizInstrIdntyNo.eq(((BizInstructorIdentifyViewRequestVO) requestObject).getBizInstrIdntyNo()))
                    .fetchOne();
        } else if(requestObject instanceof BizInstructorIdentifyCalculateViewRequestVO) { /** 강의확인서 */
            return jpaQueryFactory.selectFrom(bizInstructorIdentify)
                    .where(bizInstructorIdentify.bizInstrIdntyNo.eq(((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getBizInstrIdntyNo()))
                    .fetchOne();
        } else if(requestObject instanceof BizSurveyViewRequestVO) { /** 상호평가 평가지 */
            return jpaQueryFactory.selectFrom(bizSurvey)
                    .where(bizSurvey.bizSurveyNo.eq(((BizSurveyViewRequestVO) requestObject).getBizSurveyNo()))
                    .fetchOne();
        } else if(requestObject instanceof BizOrganizationAplyViewRequestVO) { /** 사업 공고 신청 */
            return jpaQueryFactory.selectFrom(bizOrganizationAply)
                    .where(bizOrganizationAply.bizOrgAplyNo.eq(((BizOrganizationAplyViewRequestVO) requestObject).getBizOrgAplyNo()))
                    .fetchOne();
        } else if(requestObject instanceof BizSurveyAnsViewRequestVO) { /** 상호평가 답변 */
            return jpaQueryFactory.selectFrom(bizSurveyAns)
                    .where(bizSurveyAns.bizSurveyNo.eq(((BizSurveyAnsViewRequestVO) requestObject).getBizSurveyNo()))
                    .fetchOne();
        } else if(requestObject instanceof BizInstructorQuestionViewRequestVO) { /** 강사 지원 문의 */
            return jpaQueryFactory.selectFrom(bizInstructorQuestion)
                    .where(bizInstructorQuestion.bizInstrQstnNo.eq(((BizInstructorQuestionViewRequestVO) requestObject).getBizInstrQstnNo()))
                    .fetchOne();
        } else if(requestObject instanceof BizInstructorQuestionAnswerViewRequestVO) { /** 강사 지원 문의 답변 */
            return jpaQueryFactory.selectFrom(bizInstructorQuestionAnswer)
                    .where(bizInstructorQuestionAnswer.bizInstrQstnAnsNo.eq(((BizInstructorQuestionAnswerViewRequestVO) requestObject).getBizInstrQstnAnsNo()))
                    .fetchOne();
        } else if(requestObject instanceof BizInstructorClclnDdlnViewRequestVO) { /** 강사 지원 문의 답변 */
            return jpaQueryFactory.selectFrom(bizInstructorClclnDdln)
                    .where(bizInstructorClclnDdln.bizInstrClclnDdlnNo.eq(((BizInstructorClclnDdlnViewRequestVO) requestObject).getBizInstrClclnDdlnNo()))
                    .fetchOne();
        }
        return null;
    }

    /**
     * Entity 리스트
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    private <T extends CSViewVOSupport> List<?> getEntityListExcel(T requestObject) {
        if(requestObject instanceof BizPbancViewRequestVO) { /** 사업 공고 */
            List<BizPbancCustomApiResponseVO> dtos = new ArrayList<>();
            dtos = jpaQueryFactory.select(Projections.fields(BizPbancCustomApiResponseVO.class,
                            bizPbancMaster.bizPbancNo,
                            bizPbancMaster.bizPbancType,
                            bizPbancMaster.bizPbancCtgr,
                            bizPbancMaster.bizPbancNm,
                            bizPbancMaster.bizPbancMaxTm,
                            bizPbancMaster.bizPbancYr,
                            bizPbancMaster.bizPbancRnd,
                            bizPbancMaster.bizPbancMaxInst,
                            bizPbancMaster.bizPbancSlctnMeth,
                            bizPbancMaster.bizPbancInstrSlctnMeth,
                            bizPbancMaster.bizPbancStts,
                            bizPbancMaster.bizPbancRcptBgng,
                            bizPbancMaster.bizPbancRcptEnd,
                            bizPbancMaster.bizPbancSprtBgng,
                            bizPbancMaster.bizPbancSprtEnd,
                            bizPbancMaster.bizPbancRsltYmd,
                            bizPbancMaster.bizPbancCn,
                            bizPbancMaster.bizPbancPeprYn,
                            bizPbancMaster.bizPbancPicTel,
                            bizPbancMaster.createDateTime,
                            bizPbancMaster.registUserId,
                            bizPbancMaster.updateDateTime,
                            bizPbancMaster.modifyUserId,
                            bizPbancResult.bizPbancRsltNo,
                            lmsUser.userName
                    ))
                    .from(bizPbancMaster)
                    .leftJoin(lmsUser).on(lmsUser.userId.eq(bizPbancMaster.registUserId))
                    .leftJoin(bizPbancResult).on(bizPbancResult.bizPbancNo.eq(bizPbancMaster.bizPbancNo))
                    .where(getQuery(requestObject))
                    .orderBy(bizPbancMaster.createDateTime.asc())
                    .fetch();

            return dtos;
        } else if (requestObject instanceof BizOrganizationAplyViewRequestVO) { /** 사업 공고 신청*/
            if(((BizOrganizationAplyViewRequestVO) requestObject).getIsDist() ==1){ /** 거리증빙 포함*/
                List<BizOrganizationAplyDistExcelVO> dtos = jpaQueryFactory.select(Projections.fields(BizOrganizationAplyDistExcelVO.class,
                                bizPbancMaster.bizPbancType,
                                bizPbancMaster.bizPbancNm,
                                organizationInfo.organizationName,
                                bizOrganizationAply.bizOrgAplyOperMeth,
                                bizOrganizationAply.bizOrgAplyRgn,
                                bizOrganizationAply.registUserId,
                                bizOrganizationAply.bizOrgAplyPicNm,
                                bizOrganizationAply.bizOrgAplyPicTelno,
                                bizOrganizationAply.bizOrgAplyPicMpno,
                                bizOrganizationAply.bizOrgAplyPicEml,
                                bizOrganizationAply.bizOrgAplyStts,
                                bizOrganizationAply.bizOrgAplySttsCmnt,
                                bizOrganizationAply.bizOrgAplyLsnPlanBgng,
                                bizOrganizationAply.bizOrgAplyLsnPlanEnd,
                                bizOrganizationAply.bizOrgAplyTime,
                                bizOrganizationAply.bizOrgAplyTimeFrst,
                                bizOrganizationAply.bizOrgAplyLsnPlanNope,
                                bizOrganizationAply.bizOrgAplyLsnPlanTrgt,
                                bizOrganizationAply.bizOrgAplyLsnPlanEtcInstr,
                                bizOrganizationAply.bizOrgAplyPeprYn,
                                bizInstructorAply.bizInstrAplyInstrNm,
                                bizInstructorAply.bizInstrAplyInstrId,
                                bizInstructorDist.bizDistBgngNm,
                                bizInstructorDist.bizDistBgngAddr,
                                bizInstructorDist.bizDistEndNm,
                                bizInstructorDist.bizDistEndAddr,
                                bizInstructorDist.bizDistValue,
                                bizInstructorDist.bizDistAmt,
                                bizInstructorDist.bizDistStts,
                                bizOrganizationAply.bizOrgAplyNo,
                                bizInstructorAply.bizInstrAplyNo,
                                bizOrganizationAply.bizOrgAplyChgYn,
                                organizationInfo.organizationType,
                                organizationInfo.organizationAddress1
                        ))
                        .from(bizOrganizationAply)
                        .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                        .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                        .leftJoin(bizInstructorAply).on(bizInstructorAply.bizOrgAplyNo.eq(bizOrganizationAply.bizOrgAplyNo))
                        .leftJoin(bizInstructorDist).on(bizInstructorDist.bizInstrAplyNo.eq(bizInstructorAply.bizInstrAplyNo))
                        .where(getQuery(requestObject))
                        .orderBy(bizOrganizationAply.createDateTime.asc())
                        .fetch();
                for(BizOrganizationAplyDistExcelVO dto:dtos){
                    if(dto.getBizInstrAplyNo()!=null){
                        List<BizInstructorAsgnm> bizInstructorAsgnms = jpaQueryFactory.selectFrom(bizInstructorAsgnm)
                                .where(bizInstructorAsgnm.bizOrgAplyNo.eq(dto.getBizOrgAplyNo()), bizInstructorAsgnm.bizInstrAplyNo.eq(dto.getBizInstrAplyNo()))
                                .fetch();
                        if(bizInstructorAsgnms!=null && bizInstructorAsgnms.size()>0){
                            Double instrTime = Double.valueOf(0);
                            for(BizInstructorAsgnm asgnm: bizInstructorAsgnms){
                                BizOrganizationAplyDtl dtl = jpaQueryFactory.selectFrom(bizOrganizationAplyDtl)
                                        .where(bizOrganizationAplyDtl.bizOrgAplyDtlNo.eq(asgnm.getBizOrgAplyDtlNo()))
                                        .fetchOne();
                                if(dtl!=null && dtl.getBizOrgAplyLsnDtlHr() !=null){
                                    instrTime+=Double.valueOf(dtl.getBizOrgAplyLsnDtlHr());
                                }
                            }
                            dto.setBizInstrAprvTime(instrTime);
                        }
                    }
                    if(dto.getBizOrgAplyChgYn() == 1){
                        dto.setChange("가능");
                    }else{
                        dto.setChange("불가");
                    }
                    if(dto.getBizDistStts()!=null && dto.getBizDistStts() == 1){
                        dto.setDistState("확인완료");
                    }else if(dto.getBizDistStts()!=null && dto.getBizDistStts() == 0){
                        dto.setDistState("확인전");
                    }
                    if(dto.getBizPbancType()==0){
                        dto.setBizPbancTypeString("기본형");
                    }else if(dto.getBizPbancType()==1){
                        dto.setBizPbancTypeString("미디어교육 평생교실");
                    }else if(dto.getBizPbancType()==2){
                        dto.setBizPbancTypeString("미디어교육 운영학교");
                    }else if(dto.getBizPbancType()==3){
                        dto.setBizPbancTypeString("자유학기제 미디어교육지원");
                    }else if(dto.getBizPbancType() ==4){
                        dto.setBizPbancTypeString("팩트체크 교실");
                    }
                    switch (dto.getBizOrgAplyStts()){
                        case 0:
                            dto.setState("임시접수"); break;
                        case 1:
                            dto.setState("접수"); break;
                        case 2:
                            dto.setState("승인"); break;
                        case 3:
                            dto.setState("반려"); break;
                        case 9:
                            dto.setState("가승인"); break;
                        default:
                            dto.setState("-"); break;
                    }
                    if(dto.getBizOrgAplyPeprYn()==1){
                        dto.setPaper("신청");
                    }else{
                        dto.setPaper("미신청");
                    }
                    if(!dto.getOrganizationAddress1().equals(null) || !dto.getOrganizationAddress1().equals("")){
                        List<String> addressList = List.of(dto.getOrganizationAddress1().split(" "));
                        if(addressList.size()>1){
                            dto.setOrganizationLocation(dto.getOrganizationAddress1().split(" ")[1]);
                        }
                    }
                    if (dto.getOrganizationName().contains("유치원")) {
                        dto.setOrganizationTypeName("유치원");
                    } else {
                        if (dto.getOrganizationName().contains("초등학교")){
                            dto.setOrganizationTypeName("초등학교");
                        } else if (dto.getOrganizationName().contains("중학교")){
                            dto.setOrganizationTypeName("중학교");
                        } else if (dto.getOrganizationName().contains("고등학교")){
                            dto.setOrganizationTypeName("고등학교");
                        } else if (dto.getOrganizationName().contains("학교") || dto.getOrganizationType().equals("3")) {
                            dto.setOrganizationTypeName("학교");
                        } else {
                            dto.setOrganizationTypeName("기관");
                        }
                    }
                }
                return dtos;
            }else{ /** 거리증빙 미포함*/
                List<BizOrganizationAplyExcelVO> dtos = jpaQueryFactory.select(Projections.fields(BizOrganizationAplyExcelVO.class,
                                bizOrganizationAply.bizOrgAplyNo,
                                bizPbancMaster.bizPbancType,
                                bizPbancMaster.bizPbancNm,
                                bizOrganizationAply.bizOrgAplyRgn,
                                organizationInfo.organizationName,
                                organizationInfo.organizationType,
                                bizOrganizationAply.bizOrgAplyOperMeth,
                                bizOrganizationAply.registUserId,
                                bizOrganizationAply.bizOrgAplyPicNm,
                                bizOrganizationAply.bizOrgAplyPicJbgd,
                                bizOrganizationAply.bizOrgAplyPicTelno,
                                bizOrganizationAply.bizOrgAplyPicMpno,
                                bizOrganizationAply.bizOrgAplyPicEml,
                                bizOrganizationAply.bizOrgAplyStts,
                                bizOrganizationAply.bizOrgAplySttsCmnt,
                                bizOrganizationAply.bizOrgAplyChgYn,
                                bizOrganizationAply.bizOrgAplyLsnPlanBgng,
                                bizOrganizationAply.bizOrgAplyLsnPlanEnd,
                                bizOrganizationAply.bizOrgAplyTime,
                                bizOrganizationAply.bizOrgAplyTimeFrst,
                                bizOrganizationAply.bizOrgAplyLsnPlanNope,
                                bizOrganizationAply.bizOrgAplyLsnPlanTrgt,
                                organizationInfo.organizationZipCode,
                                organizationInfo.organizationAddress1,
                                organizationInfo.organizationAddress2,
                                bizOrganizationAply.bizOrgAplyLsnPlanDscr1,
                                bizOrganizationAply.bizOrgAplyLsnPlanDscr2,
                                bizOrganizationAply.bizOrgAplyLsnPlanDscr3,
                                bizOrganizationAply.bizOrgAplyLsnPlanEtcInstr,
                                bizOrganizationAply.bizOrgAplyLsnPlanEtc,
                                bizOrganizationAply.bizOrgAplyPeprYn,
                                bizOrganizationAply.createDateTime,
                                bizOrganizationAply.updateDateTime
                        ))
                        .from(bizOrganizationAply)
                        .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                        .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                        .where(getQuery(requestObject))
                        .orderBy(bizOrganizationAply.createDateTime.asc())
                        .fetch();

                for(BizOrganizationAplyExcelVO dto:dtos){
                    if(dto.getBizPbancType()==0){
                        dto.setBizPbancTypeString("기본형");
                    }else if(dto.getBizPbancType()==1){
                        dto.setBizPbancTypeString("미디어교육 평생교실");
                    }else if(dto.getBizPbancType()==2){
                        dto.setBizPbancTypeString("미디어교육 운영학교");
                    }else if(dto.getBizPbancType()==3){
                        dto.setBizPbancTypeString("자유학기제 미디어교육지원");
                    }else if(dto.getBizPbancType() ==4){
                        dto.setBizPbancTypeString("팩트체크 교실");
                    }
                    if(dto.getBizOrgAplyChgYn() == 1){
                        dto.setChange("가능");
                    }else{
                        dto.setChange("불가");
                    }
                    switch (dto.getBizOrgAplyStts()){
                        case 0:
                            dto.setState("임시접수"); break;
                        case 1:
                            dto.setState("접수"); break;
                        case 2:
                            dto.setState("승인"); break;
                        case 3:
                            dto.setState("반려"); break;
                        case 9:
                            dto.setState("가승인"); break;
                        case 7:
                            dto.setState("종료"); break;
                        default:
                            dto.setState("-"); break;
                    }
                    if(dto.getBizOrgAplyPeprYn()==1){
                        dto.setPaper("신청");
                    }else{
                        dto.setPaper("미신청");
                    }
                    if(!dto.getOrganizationAddress1().equals(null) || !dto.getOrganizationAddress1().trim().equals("")){
                        List<String> addressList = List.of(dto.getOrganizationAddress1().split(" "));
                        if(addressList.size()>1){
                            dto.setOrganizationLocation(dto.getOrganizationAddress1().split(" ")[1]);
                        }
                    }
                    if (dto.getOrganizationName().contains("유치원")) {
                        dto.setOrganizationTypeName("유치원");
                    } else {
                        if (dto.getOrganizationName().contains("초등학교")){
                            dto.setOrganizationTypeName("초등학교");
                        } else if (dto.getOrganizationName().contains("중학교")){
                            dto.setOrganizationTypeName("중학교");
                        } else if (dto.getOrganizationName().contains("고등학교")){
                            dto.setOrganizationTypeName("고등학교");
                        } else if (dto.getOrganizationName().contains("학교") || dto.getOrganizationType().equals("3")) {
                            dto.setOrganizationTypeName("학교");
                        } else {
                            dto.setOrganizationTypeName("기관");
                        }
                    }
                    List<BizInstructorAply> bizInstructorAplies = jpaQueryFactory.selectFrom(bizInstructorAply)
                            .where(bizInstructorAply.bizOrgAplyNo.eq(dto.getBizOrgAplyNo()), bizInstructorAply.bizInstrAplyStts.eq(2))
                            .fetch();
                    if (bizInstructorAplies != null && bizInstructorAplies.size() > 0) {
                        String instrNames = " ";
                        for (BizInstructorAply instructorAply : bizInstructorAplies) {
                            instrNames = new StringBuilder(instrNames).append(instructorAply.getBizInstrAplyInstrNm()).append(" ").toString();
                        }
                        dto.setBizOrgAplyInstr(instrNames);
                    }
                }
                return dtos;
            }

        } else if(requestObject instanceof BizAplyViewRequestVO) { /** 사업 공고 신청 - 언론인/기본형 */
            if (((BizAplyViewRequestVO) requestObject).getBizAplyType().equals("free")) {/** 자유형 */
                List<BizAplyFreeExcelVO> dtos =jpaQueryFactory.select(Projections.fields(BizAplyFreeExcelVO.class,
                                bizPbancMaster.bizPbancYr,
                                bizPbancMaster.bizPbancCtgr,
                                bizPbancMaster.bizPbancRnd,
                                bizPbancMaster.bizPbancNm,
                                bizPbancMaster.bizPbancNo,
                                bizAply.sequenceNo,
                                bizAply.bizAplyUserID,
                                bizAply.bizAplyUserNm,
                                bizAply.bizAplyUserDptm,
                                bizAply.bizAplyUserJbgd,
                                organizationInfo.organizationName,
                                bizAply.orgName,
                                bizAply.bizAplyUserTelno,
                                bizAply.bizAplyUserEml,
                                bizAply.bizAplyStts,
                                bizAply.createDateTime,
                                bizAply.updateDateTime
                        ))
                        .from(bizAply)
                        .leftJoin(lmsUser).on(lmsUser.userId.eq(bizAply.bizAplyUserID))
                        .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizAply.orgCd))
                        .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizAply.bizPbancNo))
                        .where(getQuery(requestObject))
                        .orderBy(bizAply.createDateTime.asc())
                        .fetch();
                if(dtos != null && dtos.size()>0){
                    for(BizAplyFreeExcelVO dto:dtos){
                        switch (dto.getBizAplyStts()){
                            case 1:
                                dto.setStts("신청");
                                break;
                            case 5:
                                dto.setStts("가승인");
                                break;
                            case 7:
                                dto.setStts("승인");
                                break;
                            case 9:
                                dto.setStts("종료");
                                break;
                        }
                        switch (dto.getBizPbancCtgr()){
                            case 0:
                                dto.setCtgr("미디어교육");
                                break;
                            case 1:
                                dto.setCtgr("언론인연수");
                                break;
                            case 2:
                                dto.setCtgr("미디어지원");
                                break;
                        }
                        LmsUser user = jpaQueryFactory.selectFrom(lmsUser)
                                .where(lmsUser.userId.eq(dto.getBizAplyUserID()))
                                .fetchOne();
                        if (user != null && user.getOrganizationInfo() != null) {
                            if (user.getMediaInfo() != null) {
                                dto.setOrganizationName(new StringBuilder(user.getOrganizationInfo().getOrganizationName()).append(" / ").append(user.getMediaInfo().getMediaName()).toString());
                            } else dto.setOrganizationName(user.getOrganizationInfo().getOrganizationName());
                        } else {
                            if((dto.getOrganizationName() == null || dto.getOrganizationName().isEmpty()) && dto.getOrgName() != null) dto.setOrganizationName(dto.getOrgName());
                        }

                        List<BizAplyDtl> bizAplyDtls = jpaQueryFactory.selectFrom(bizAplyDtl)
                                .where(bizAplyDtl.sequenceNo.eq(dto.getSequenceNo()))
                                .fetch();
                        if(bizAplyDtls != null && bizAplyDtls.size()>0){
                            for (BizAplyDtl aplyDtl : bizAplyDtls) {
                                BizPbancTmpl5 bizPbancTmpl5s = jpaQueryFactory.selectFrom(bizPbancTmpl5)
                                        .where(bizPbancTmpl5.bizPbancTmpl5No.eq(aplyDtl.getBizPbancTmpl5No()))
                                        .fetchOne();
                                aplyDtl.setBizPbancTmpl5(bizPbancTmpl5s);
                            }
                            dto.setBizAplyDtls(bizAplyDtls);
                        }
                        BizPbancMaster pbancMaster = jpaQueryFactory.selectFrom(bizPbancMaster)
                                .where(bizPbancMaster.bizPbancNo.eq(dto.getBizPbancNo()))
                                .fetchOne();
                        List<BizPbancTmpl5> bizPbancTmpl5s = jpaQueryFactory.selectFrom(bizPbancTmpl5)
                                .where(bizPbancTmpl5.bizPbancNo.eq(dto.getBizPbancNo()))
                                .orderBy(bizPbancTmpl5.bizPbancTmpl5Ordr.asc()).fetch();
                        if(bizPbancTmpl5s != null && bizPbancTmpl5s.size()>0){
                            pbancMaster.setBizPbancTmpl5s(bizPbancTmpl5s);
                            dto.setBizPbancMaster(pbancMaster);
                        }
                        if (user != null) {
                            dto.setLmsUser(user);
                        }
                    }
                }
                return dtos;
            } else if (((BizAplyViewRequestVO) requestObject).getBizAplyType().equals("journalist")) {/** 언론인 */
                return jpaQueryFactory.select(Projections.fields(BizAplyExcelVO.class,
                                bizPbancMaster.bizPbancYr,
                                bizPbancMaster.bizPbancRnd,
                                bizPbancMaster.bizPbancNm,
                                bizAply.bizAplyUserID,
                                lmsUser.userId,
                                lmsUser.userName,
                                lmsUser.phone,
                                lmsUser.email,
                                bizAply.createDateTime
                        ))
                        .from(bizAply)
                        .leftJoin(lmsUser).on(lmsUser.userId.eq(bizAply.bizAplyUserID))
                        .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(lmsUser.organizationCode))
                        .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizAply.bizPbancNo))
                        .where(getQuery(requestObject))
                        .orderBy(bizAply.createDateTime.asc())
                        .fetch();
            } else {/** 기본형 */
                return jpaQueryFactory.select(Projections.fields(BizAplyExcelVO.class,
                                bizPbancMaster.bizPbancYr,
                                bizPbancMaster.bizPbancRnd,
                                bizPbancMaster.bizPbancNm,
                                bizAply.bizAplyUserID,
                                lmsUser.userName,
                                lmsUser.userId,
                                lmsUser.phone,
                                lmsUser.email,
                                organizationInfo.organizationName,
                                bizAply.createDateTime
                        ))
                        .from(bizAply)
                        .leftJoin(lmsUser).on(lmsUser.userId.eq(bizAply.bizAplyUserID))
                        .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(lmsUser.organizationCode))
                        .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizAply.bizPbancNo))
                        .where(getQuery(requestObject))
                        .orderBy(bizAply.createDateTime.asc())
                        .fetch();
            }
        } else if(requestObject instanceof BizOrganizationAplyCustomViewRequestVO) { /** 강사 배정 */
            List<BizInstructorExcelVO> dtos  =  jpaQueryFactory.select(Projections.fields(BizInstructorExcelVO.class,
                            bizPbancMaster.bizPbancYr,
                            bizPbancMaster.bizPbancRnd,
                            bizPbancMaster.bizPbancNm,
                            organizationInfo.organizationName,
                            bizOrganizationAply.bizOrgAplyPicNm,
                            bizOrganizationAply.bizOrgAplyPicMpno,
                            bizOrganizationAply.bizOrgAplyLsnPlanBgng,
                            bizOrganizationAply.bizOrgAplyLsnPlanEnd,
                            bizOrganizationAply.bizOrgAplyNo
                    ))
                    .from(bizOrganizationAply)
                    .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                    .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                    .leftJoin(bizInstructorPbanc).on(bizInstructorPbanc.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                    .leftJoin(bizInstructorAply).on(bizInstructorAply.bizOrgAplyNo.eq(bizOrganizationAply.bizOrgAplyNo))
                    .where(getQuery(requestObject))
                    .groupBy(bizOrganizationAply.bizOrgAplyNo)
                    .orderBy(bizOrganizationAply.createDateTime.asc())
                    .fetch();
            if (dtos != null && dtos.size() > 0) {
                for (BizInstructorExcelVO dto : dtos) {
                    dto.setBizInstr("");
                    List<BizInstructorAsgnm> bizInstructorAsgnmEntities = jpaQueryFactory.selectFrom(bizInstructorAsgnm)
                            .leftJoin(bizInstructorAply).on(bizInstructorAply.bizInstrAplyNo.eq(bizInstructorAsgnm.bizInstrAplyNo))
                            .groupBy(bizInstructorAsgnm.bizInstrAplyNo)
                            .where(bizInstructorAsgnm.bizOrgAplyNo.eq(dto.getBizOrgAplyNo()))
                            .fetch();
                    if (!bizInstructorAsgnmEntities.isEmpty() && bizInstructorAsgnmEntities.size() > 0) {
                        for (BizInstructorAsgnm entity : bizInstructorAsgnmEntities) {
                            if (entity.getBizInstructorAply() != null) {
                                dto.setBizInstr(dto.getBizInstr().concat(entity.getBizInstructorAply().getBizInstrAplyInstrNm() + ", "));
                            }
                        }
                    }
                }
            }
            return dtos;

        } else if(requestObject instanceof BizInstructorDistViewRequestVO){ /** 거리증빙 */
            List<BizInstructorDistExcelVO> dtos = new ArrayList<>();
            dtos = jpaQueryFactory.select(Projections.fields(BizInstructorDistExcelVO.class,
                            bizInstructorDist.bizInstrDistNo,
                            bizPbancMaster.bizPbancNm,
                            organizationInfo.organizationName,
                            bizOrganizationAply.bizOrgAplyLsnPlanBgng,
                            bizOrganizationAply.bizOrgAplyLsnPlanEnd,
                            bizInstructorAply.bizInstrAplyInstrNm,
                            bizInstructorAply.bizInstrAplyInstrId,
                            bizInstructorDist.bizDistBgngNm,
                            bizInstructorDist.bizDistBgngAddr,
                            bizInstructorDist.bizDistEndNm,
                            bizInstructorDist.bizDistEndAddr,
                            bizInstructorDist.bizDistValue,
                            bizInstructorDist.bizDistAmt,
                            bizInstructorDist.bizDistStts
                    ))
                    .from(bizInstructorDist)
                    .leftJoin(bizInstructorAply).on(bizInstructorAply.bizInstrAplyNo.eq(bizInstructorDist.bizInstrAplyNo))
                    .leftJoin(bizOrganizationAply).on(bizOrganizationAply.bizOrgAplyNo.eq(bizInstructorDist.bizOrgAplyNo))
                    .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                    .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                    .where(getQuery(requestObject))
                    .orderBy(bizInstructorDist.createDateTime.asc())
                    .fetch();
            for(BizInstructorDistExcelVO dto:dtos){
                if(dto.getBizDistStts() == 0){
                    dto.setState("확인전");
                }else{
                    dto.setState("확인완료");
                }
            }
            return dtos;
        } else if(requestObject instanceof BizInstructorIdentifyViewRequestVO) { /** 강의확인서 */
            List<BizInstructorIdentifyExcelVO> dtos = jpaQueryFactory.select(Projections.fields(BizInstructorIdentifyExcelVO.class,
                            lmsUser.userName,
                            bizInstructorIdentify.registUserId,
                            bizPbancMaster.bizPbancNm,
                            organizationInfo.organizationName,
                            bizOrganizationAply.bizOrgAplyRgn,
                            bizOrganizationAply.bizOrgAplyPicNm,
                            bizOrganizationAply.bizOrgAplyPicMpno,
                            bizOrganizationAply.bizOrgAplyLsnPlanBgng,
                            bizOrganizationAply.bizOrgAplyLsnPlanEnd,
                            bizInstructorIdentify.bizInstrIdntyYm,
                            bizInstructorIdentify.bizInstrIdntyStts,
                            bizInstructorIdentify.bizInstrIdntyTime,
                            bizInstructorIdentify.createDateTime,
                            bizInstructorIdentify.bizInstrIdntyNo,
                            bizInstructorIdentify.bizInstrIdntyAmt,
                            bizInstructorIdentify.bizInstrIdntyAprvDt,
                            bizInstructorIdentify.bizInstrIdntyPayYm

                    ))
                    .from(bizInstructorIdentify)
                    .leftJoin(bizOrganizationAply).on(bizOrganizationAply.bizOrgAplyNo.eq(bizInstructorIdentify.bizOrgAplyNo))
                    .leftJoin(lmsUser).on(lmsUser.userId.eq(bizInstructorIdentify.registUserId))
                    .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                    .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                    .where(getQuery(requestObject))
                    .orderBy(bizInstructorIdentify.createDateTime.asc())
                    .fetch();
            for(BizInstructorIdentifyExcelVO dto:dtos){
                if(dto.getBizInstrIdntyStts()==1){
                    dto.setState("제출(접수)");
                }else if(dto.getBizInstrIdntyStts()==2){
                    dto.setState("기관 승인");
                }else if(dto.getBizInstrIdntyStts()==3){
                    dto.setState("승인(지출 접수)");
                }else if(dto.getBizInstrIdntyStts()==4){
                    dto.setState("지출 완료");
                }else if(dto.getBizInstrIdntyStts()==9){
                    dto.setState("반려");
                }
                dto.setBizOnlineCount(0);
                dto.setBizInstrIdntyTime(dto.getBizInstrIdntyTime().replace(".0",""));
                List<BizInstructorIdentifyDtl> dtls = jpaQueryFactory.selectFrom(bizInstructorIdentifyDtl)
                        .where(bizInstructorIdentifyDtl.bizInstrIdntyNo.eq(dto.getBizInstrIdntyNo()))
                        .leftJoin(bizOrganizationAplyDtl).on(bizOrganizationAplyDtl.bizOrgAplyDtlNo.eq(bizInstructorIdentifyDtl.bizOrgAplyDtlNo))
                        .fetch();
                for(BizInstructorIdentifyDtl dtl : dtls){
                    if(dtl.getVdoLctYn() == 1){
                        dto.setBizOnlineCount(dto.getBizOnlineCount()+1);
                    }
                }
                if(dto.getBizOnlineCount()>0){
                    dto.setBizOnline("포함("+dto.getBizOnlineCount()+")");
                }else{
                    dto.setBizOnline("미포함");
                }
            }
            return dtos;
        } else if(requestObject instanceof BizInstructorIdentifyDtlExcelRequestVO) { /** 강의확인서 강사 작성 주제 */
            List<BizInstructorIdentifyDtlExcelVO> dtos = jpaQueryFactory.select(Projections.fields(BizInstructorIdentifyDtlExcelVO.class,
                            lmsUser.userName,
                            bizInstructorIdentify.registUserId,
                            bizPbancMaster.bizPbancNm,
                            organizationInfo.organizationName,
                            bizOrganizationAply.bizOrgAplyRgn,
                            bizOrganizationAply.bizOrgAplyLsnPlanBgng,
                            bizOrganizationAply.bizOrgAplyLsnPlanEnd,
                            bizInstructorIdentify.bizInstrIdntyYm,
                            bizInstructorIdentify.bizInstrIdntyStts,
                            bizOrganizationAplyDtl.bizOrgAplyLsnDtlTpic,
                            bizInstructorIdentifyDtl.bizInstrIdntyDtlTpic,
                            bizInstructorIdentifyDtl.bizInstrIdntyDtlNope
                    ))
                    .from(bizInstructorIdentifyDtl)
                    .leftJoin(bizOrganizationAplyDtl).on(bizOrganizationAplyDtl.bizOrgAplyDtlNo.eq(bizInstructorIdentifyDtl.bizOrgAplyDtlNo))
                    .leftJoin(bizInstructorIdentify).on(bizInstructorIdentify.bizInstrIdntyNo.eq(bizInstructorIdentifyDtl.bizInstrIdntyNo))
                    .leftJoin(bizOrganizationAply).on(bizOrganizationAply.bizOrgAplyNo.eq(bizInstructorIdentify.bizOrgAplyNo))
                    .leftJoin(lmsUser).on(lmsUser.userId.eq(bizInstructorIdentify.registUserId))
                    .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                    .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                    .where(getQuery(requestObject))
                    .orderBy(bizInstructorIdentify.createDateTime.asc())
                    .fetch();
            for(BizInstructorIdentifyDtlExcelVO dto:dtos){
                if(dto.getBizInstrIdntyStts()==1){
                    dto.setState("제출(접수)");
                }else if(dto.getBizInstrIdntyStts()==2){
                    dto.setState("기관 승인");
                }else if(dto.getBizInstrIdntyStts()==3){
                    dto.setState("승인(지출 접수)");
                }else if(dto.getBizInstrIdntyStts()==4){
                    dto.setState("지출 완료");
                }else if(dto.getBizInstrIdntyStts()==9){
                    dto.setState("반려");
                }
            }
            return dtos;
        } else if(requestObject instanceof BizOrganizationRsltRptViewRequestVO) { /** 결과보고서 */
            List<BizOrganizationRsltRptExcelVO> dtos = jpaQueryFactory.select(Projections.fields(BizOrganizationRsltRptExcelVO.class,
                            bizPbancMaster.bizPbancNm,
                            organizationInfo.organizationName,
                            lmsUser.userId,
                            lmsUser.userName,
                            lmsUser.phone,
                            bizOrganizationAply.bizOrgAplyLsnPlanBgng,
                            bizOrganizationAply.bizOrgAplyLsnPlanEnd,
                            bizOrganizationRsltRpt.bizOrgRsltRptStts,
                            bizOrganizationAply.bizOrgAplyNo
                    ))
                    .from(bizOrganizationRsltRpt)
                    .leftJoin(lmsUser).on(lmsUser.userId.eq(bizOrganizationRsltRpt.registUserId))
                    .leftJoin(bizOrganizationAply).on(bizOrganizationAply.bizOrgAplyNo.eq(bizOrganizationRsltRpt.bizOrgAplyNo))
                    .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                    .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationRsltRpt.orgCd))
                    .where(getQuery(requestObject))
                    .orderBy(bizOrganizationRsltRpt.createDateTime.asc())
                    .fetch();
            if (dtos != null && dtos.size() > 0){
                for(BizOrganizationRsltRptExcelVO dto : dtos){
                    dto.setBizInstr("");
                    List<BizInstructorAsgnm> bizInstructorAsgnmEntities = jpaQueryFactory.selectFrom(bizInstructorAsgnm)
                            .leftJoin(bizInstructorAply).on(bizInstructorAply.bizInstrAplyNo.eq(bizInstructorAsgnm.bizInstrAplyNo))
                            .where(bizInstructorAsgnm.bizOrgAplyNo.eq(dto.getBizOrgAplyNo()))
                            .fetch();
                    if (!bizInstructorAsgnmEntities.isEmpty() && bizInstructorAsgnmEntities.size() > 0) {
                        for(BizInstructorAsgnm entity : bizInstructorAsgnmEntities){
                            if(entity.getBizInstructorAply()!=null){
                                dto.setBizInstr(dto.getBizInstr().concat(entity.getBizInstructorAply().getBizInstrAplyInstrNm()+", "));
                            }
                        }
                    }
                    if(dto.getBizOrgRsltRptStts()==0){
                        dto.setState("임시저장");
                    }else if(dto.getBizOrgRsltRptStts()==1){
                        dto.setState("접수");
                    }else if(dto.getBizOrgRsltRptStts()==2){
                        dto.setState("승인");
                    }else if(dto.getBizOrgRsltRptStts()==9){
                        dto.setState("반려");
                    }
                }
            }
            return dtos;
        } else if(requestObject instanceof BizInstructorClclnDdlnViewRequestVO) { /** 정산 마감일 */
            List<BizInstructorClclnDdlnExcelVO> dtos = jpaQueryFactory.select(Projections.fields(BizInstructorClclnDdlnExcelVO.class,
                            bizInstructorClclnDdln.bizInstrClclnDdlnValue,
                            bizInstructorClclnDdln.bizInstrClclnDdlnTm,
                            bizInstructorClclnDdln.bizInstrClclnDdlnNo,
                            bizInstructorClclnDdln.bizInstrClclnDdlnYr,
                            bizInstructorClclnDdln.bizInstrClclnDdlnMm
                    ))
                    .from(bizInstructorClclnDdln)
                    .where(getQuery(requestObject))
                    .orderBy(bizInstructorClclnDdln.createDateTime.asc())
                    .fetch();

            if (!dtos.isEmpty() && dtos.size() > 0) {
                for (BizInstructorClclnDdlnExcelVO dto : dtos) {
                    String value = dto.getBizInstrClclnDdlnYr().toString()+dto.getBizInstrClclnDdlnMm().toString();
                    List<BizInstructorIdentify> bizInstructorIdentifyList = jpaQueryFactory.selectFrom(bizInstructorIdentify)
                            .where(bizInstructorIdentify.bizInstrIdntyYm.eq(value))
                            .fetch();
                    dto.setBizInstrIdentifyCount(0);
                    dto.setAmount(0);
                    if(!bizInstructorIdentifyList.isEmpty() && bizInstructorIdentifyList.size()>0){
                        for(BizInstructorIdentify entity : bizInstructorIdentifyList){
                            dto.setBizInstrIdentifyCount(dto.getBizInstrIdentifyCount()+1);
                            if(entity.getBizInstrIdntyAmt()!=null){
                                dto.setAmount(Integer.valueOf(entity.getBizInstrIdntyAmt())+dto.getAmount());

                            }
                        }
                    }

                }
            }

            return dtos;

        }else if(requestObject instanceof BizSurveyViewRequestVO) { /** 상호평가 평가지 */
            List<BizSurveyExcelVO> dtos =jpaQueryFactory.select(Projections.fields(BizSurveyExcelVO.class,
                            bizSurvey.bizSurveyNo,
                            bizSurvey.bizSurveyCtgr,
                            bizSurvey.bizSurveyNm,
                            bizSurvey.bizSurveyCn,
                            bizSurvey.bizSurveyStts,
                            bizSurvey.createDateTime,
                            bizSurvey.updateDateTime
                    ))
                    .from(bizSurvey)
                    .where(getQuery(requestObject))
                    .orderBy(bizSurvey.createDateTime.asc())
                    .fetch();
            if (dtos.size() > 0 && dtos != null) {
                for (BizSurveyExcelVO dto : dtos) {
                    if(dto.getBizSurveyStts()==1){
                        dto.setState("사용");
                    }else{
                        dto.setState("미사용");
                    }
                    if(dto.getBizSurveyCtgr()==1){
                        dto.setCategory("기관평가");
                    }else if(dto.getBizSurveyCtgr()==2){
                        dto.setCategory("강사평가");
                    }
                    dto.setQitem("");
                    List<BizSurveyMaster> bizSurveyMasterEntities = jpaQueryFactory.selectFrom(bizSurveyMaster)
                            .where(bizSurveyMaster.bizSurveyNo.eq(dto.getBizSurveyNo()))
                            .fetch();
                    if(bizSurveyMasterEntities.size() > 0 && bizSurveyMasterEntities != null) {
                        for (BizSurveyMaster bizSurveyMasterEntity : bizSurveyMasterEntities) {
                            BizSurveyQitem bizSurveyQitemList = jpaQueryFactory.selectFrom(bizSurveyQitem)
                                    .where(bizSurveyQitem.bizSurveyQitemNo.eq(bizSurveyMasterEntity.getBizSurveyQitemNo()))
                                    .fetchOne();
                            dto.setQitem(dto.getQitem()+bizSurveyQitemList.getBizSurveyQitemName()+", ");
                        }
                    }
                }
            }
            return dtos;

        } else if(requestObject instanceof BizSurveyQitemViewRequestVO) { /** 상호평가 문항 */
            List<BizSurveyQitemExcelVO> dtos =jpaQueryFactory.select(Projections.fields(BizSurveyQitemExcelVO.class,
                            bizSurveyQitem.bizSurveyQitemNo,
                            bizSurveyQitem.bizSurveyQitemCategory,
                            bizSurveyQitem.bizSurveyQitemType,
                            bizSurveyQitem.bizSurveyQitemName,
                            bizSurveyQitem.bizSurveyQitemContent,
                            bizSurveyQitem.bizSurveyQitemEtc,
                            bizSurveyQitem.bizSurveyQitemStatus,
                            bizSurveyQitem.createDateTime,
                            bizSurveyQitem.updateDateTime
                    ))
                    .from(bizSurveyQitem)
                    .where(getQuery(requestObject))
                    .orderBy(bizSurveyQitem.createDateTime.asc())
                    .fetch();

            for(BizSurveyQitemExcelVO dto:dtos){
                if(dto.getBizSurveyQitemCategory()==1){
                    dto.setCategory("강사평가");
                }else if(dto.getBizSurveyQitemCategory()==2){
                    dto.setCategory("기관평가");
                }
                if(dto.getBizSurveyQitemType()==0){
                    dto.setType("단일선택");
                }else if(dto.getBizSurveyQitemType()==1){
                    dto.setType("다중선택");
                }else if(dto.getBizSurveyQitemType()==2){
                    dto.setType("단답형");
                }else if(dto.getBizSurveyQitemType()==3){
                    dto.setType("서술형");
                }
                if(dto.getBizSurveyQitemEtc()==1){
                    dto.setEtc("있음");
                }else if(dto.getBizSurveyQitemEtc()==0){
                    dto.setEtc("없음");
                }
            }
            return dtos;

        } else if(requestObject instanceof BizInstructorViewRequestVO) { /** 강사 모집 배정 시뮬레이션 */
            List<BizInstructorTestExcelVO> dtos =jpaQueryFactory.select(Projections.fields(BizInstructorTestExcelVO.class,
                            bizInstructor.bizInstrNm,
                            bizPbancMaster.bizPbancNm,
                            bizPbancMaster.bizPbancYr,
                            bizPbancMaster.bizPbancType,
                            organizationInfo.organizationName,
                            bizOrganizationAply.bizOrgAplyRgn,
                            organizationInfo.organizationAddress1,
                            bizOrganizationAply.bizOrgAplyTime,
                            bizOrganizationAply.bizOrgAplyLsnPlanEtc,
                            bizOrganizationAply.bizOrgAplyLsnPlanEtcInstr,
                            bizOrganizationAply.bizOrgAplyPicNm,
                            bizOrganizationAply.bizOrgAplyPicJbgd,
                            bizOrganizationAply.bizOrgAplyPicTelno,
                            bizOrganizationAply.bizOrgAplyPicMpno,
                            bizOrganizationAply.bizOrgAplyPicEml,
                            bizOrganizationAply.bizOrgAplyLsnPlanTrgt,
                            bizOrganizationAply.bizOrgAplyStts,
                            bizOrganizationAply.bizOrgAplyNo
                    ))
                    .from(bizInstructorPbanc)
                    .where(bizInstructorPbanc.bizInstrNo.eq(((BizInstructorViewRequestVO) requestObject).getBizInstrNo()))
                    .rightJoin(bizOrganizationAply).on(bizOrganizationAply.bizPbancNo.eq(bizInstructorPbanc.bizPbancNo))
                    .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                    .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                    .leftJoin(bizInstructor).on(bizInstructor.bizInstrNo.eq(bizInstructorPbanc.bizInstrNo))
                    .orderBy(bizInstructor.bizInstrNo.asc())
                    .fetch();
            if (dtos != null && dtos.size() > 0) {
                List<String> bizOrgAplyNoList = new ArrayList<>();
                for (BizInstructorTestExcelVO dto : dtos) {
                    bizOrgAplyNoList.add(dto.getBizOrgAplyNo());
                    dto.setInstrAply(jpaQueryFactory.selectFrom(bizInstructorAply)
                            .where(bizInstructorAply.bizOrgAplyNo.eq(dto.getBizOrgAplyNo()),
                                    bizInstructorAply.bizInstrAplyStts.ne(0))
                            .fetch());
                    dto.setInstrNumber(dto.getInstrAply().size());

                    List<BizInstructorAsgnm> bizInstructorAsgnms = jpaQueryFactory.selectFrom(bizInstructorAsgnm)
                            .where(bizInstructorAsgnm.bizOrgAplyNo.eq(dto.getBizOrgAplyNo()))
                            .groupBy(bizInstructorAsgnm.bizInstrAplyNo)
                            .orderBy(bizInstructorAply.bizInstrAplyInstrNm.asc())
                            .fetch();
                    if(!bizInstructorAsgnms.isEmpty() && bizInstructorAsgnms.size()>0){
                        List<String> etcInstrList = new ArrayList<>();
                        for(BizInstructorAsgnm asgnm : bizInstructorAsgnms){
                            etcInstrList.add(asgnm.getBizInstructorAply().getBizInstrAplyInstrNm());
                        }
                        if(etcInstrList.size()>0){
                            dto.setBizOrgAplyLsnEtcInstr(etcInstrList.toString().replace("[","").replace("]",""));
                        }
                    }
                    if(!dto.getOrganizationAddress1().equals(null) || !dto.getOrganizationAddress1().trim().equals("")){
                        List<String> addressList = List.of(dto.getOrganizationAddress1().split(" "));
                        if(addressList.size()>1){
                            dto.setOrganizationAddress1(dto.getOrganizationAddress1().split(" ")[1]);
                        }
                    }
                }
                dtos.get(0).setInstrAplyAll(jpaQueryFactory.selectFrom(bizInstructorAply)
                        .where(bizInstructorAply.bizOrgAplyNo.in(bizOrgAplyNoList))
                        .groupBy(bizInstructorAply.bizInstrAplyInstrId)
                        .orderBy(bizInstructorAply.bizInstrAplyInstrNm.asc())
                        .fetch());
            }
            return dtos;

        } else {
            return null;
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
        if(requestObject instanceof BizPbancViewRequestVO) { /** 사업 공고 */
            List<BizPbancCustomApiResponseVO> dtos = new ArrayList<>();
            dtos = jpaQueryFactory.select(Projections.fields(BizPbancCustomApiResponseVO.class,
                            bizPbancMaster.bizPbancNo,
                            bizPbancMaster.bizPbancType,
                            bizPbancMaster.bizPbancCtgr,
                            bizPbancMaster.bizPbancCtgrSub,
                            bizPbancMaster.bizPbancNm,
                            bizPbancMaster.bizPbancMaxTm,
                            bizPbancMaster.bizPbancYr,
                            bizPbancMaster.bizPbancRnd,
                            bizPbancMaster.bizPbancMaxInst,
                            bizPbancMaster.bizPbancSlctnMeth,
                            bizPbancMaster.bizPbancInstrSlctnMeth,
                            bizPbancMaster.bizPbancStts,
                            bizPbancMaster.bizPbancRcptBgng,
                            bizPbancMaster.bizPbancRcptEnd,
                            bizPbancMaster.bizPbancSprtBgng,
                            bizPbancMaster.bizPbancSprtEnd,
                            bizPbancMaster.bizPbancRsltYmd,
                            bizPbancMaster.bizPbancCn,
                            bizPbancMaster.bizPbancPeprYn,
                            bizPbancMaster.bizPbancPicTel,
                            bizPbancMaster.isTop,
                            bizPbancMaster.createDateTime,
                            bizPbancMaster.registUserId,
                            bizPbancMaster.updateDateTime,
                            bizPbancMaster.modifyUserId,
                            bizPbancResult.bizPbancRsltNo,
                            lmsUser.userName
                    ))
                    .from(bizPbancMaster)
                    .leftJoin(lmsUser).on(lmsUser.userId.eq(bizPbancMaster.registUserId))
                    .leftJoin(bizPbancResult).on(bizPbancResult.bizPbancNo.eq(bizPbancMaster.bizPbancNo))
                    .where(getQuery(requestObject))
                    .orderBy(bizPbancMaster.isTop.desc(), bizPbancMaster.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch()
                    .stream()
                    .peek(data -> data.setIsNew(new DateTime().minusDays(15).compareTo(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
                            .parseDateTime(data.getCreateDateTime())) < 0)).collect(Collectors.toList());

            if (dtos != null && dtos.size() > 0){
                for (BizPbancCustomApiResponseVO dto : dtos) {
                    dto.setFileMasters(
                            jpaQueryFactory.selectFrom(fileMaster)
                                    .where(fileMaster.atchFileSn.eq(dto.getBizPbancNo()))
                                    .fetch()
                    );
                    if(dto.getBizPbancType()!=5){
                        dto.setBizOrganizationAprvCount(jpaQueryFactory.select(bizOrganizationAply.count())
                                .from(bizOrganizationAply)
                                .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                                .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                                .where(bizOrganizationAply.bizPbancNo.eq(dto.getBizPbancNo()), bizOrganizationAply.bizOrgAplyStts.in(2, 9))
                                .fetchOne());
                        dto.setBizOrganizationAplyCount(jpaQueryFactory.select(bizOrganizationAply.count())
                                .from(bizOrganizationAply)
                                .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                                .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                                .where(bizOrganizationAply.bizPbancNo.eq(dto.getBizPbancNo()), bizOrganizationAply.bizOrgAplyStts.in(1))
                                .fetchOne());

                    }else if(dto.getBizPbancType()==5){
                        dto.setBizOrganizationAprvCount(jpaQueryFactory.select(bizAply.count()).from(bizAply)
                                .where(bizAply.bizPbancNo.eq(dto.getBizPbancNo()),bizAply.bizAplyStts.in(5,7)).fetchOne());
                        dto.setBizOrganizationAplyCount(jpaQueryFactory.select(bizAply.count()).from(bizAply)
                                .where(bizAply.bizPbancNo.eq(dto.getBizPbancNo()),bizAply.bizAplyStts.eq(1)).fetchOne());

                    }

                    /** Temp 정보 추가 */
                    if (dto.getBizPbancType() == 0) {

                        BizPbancTmpl0 bizPbancTmpl0Entity = jpaQueryFactory.selectFrom(bizPbancTmpl0)
                                .where(bizPbancTmpl0.bizPbancNo.eq(dto.getBizPbancNo()))
                                .fetchOne();

                        if (bizPbancTmpl0Entity != null) {

                            List<BizPbancTmpl0Trgt> tmpl0TrgtEntitys = jpaQueryFactory.selectFrom(bizPbancTmpl0Trgt)
                                    .where(bizPbancTmpl0Trgt.bizPbancTmpl0No.eq(bizPbancTmpl0Entity.getBizPbancTmpl0No()))
                                    .fetch();

                            if (tmpl0TrgtEntitys != null && tmpl0TrgtEntitys.size() > 0) {
                                List<Integer> tmpl0Trgt = new ArrayList<>();
                                for (BizPbancTmpl0Trgt i : tmpl0TrgtEntitys) {
                                    tmpl0Trgt.add(i.getBizPbancTmpl0TrgtCd());
                                }
                                bizPbancTmpl0Entity.setBizPbancTmpl0Trgts(tmpl0Trgt);
                            }

                            List<BizPbancTmpl0Item> tmpl0ItemEntitys = jpaQueryFactory.selectFrom(bizPbancTmpl0Item)
                                    .where(bizPbancTmpl0Item.bizPbancTmpl0No.eq(bizPbancTmpl0Entity.getBizPbancTmpl0No()))
                                    .fetch();

                            if (tmpl0ItemEntitys != null && tmpl0ItemEntitys.size() > 0) {
                                bizPbancTmpl0Entity.setBizPbancTmpl0Items(tmpl0ItemEntitys);
                            }
                            dto.setBizPbancTmpl0(bizPbancTmpl0Entity);
                        }
                        dto.setBizPbancTmpl0(bizPbancTmpl0Entity);

                    } else if (dto.getBizPbancType() == 1) {
                        BizPbancTmpl1 bizPbancTmpl1Entity = jpaQueryFactory.selectFrom(bizPbancTmpl1)
                                .where(bizPbancTmpl1.bizPbancNo.eq(dto.getBizPbancNo()))
                                .fetchOne();

                        if (bizPbancTmpl1Entity != null) {
                            List<BizPbancTmpl1Trgt> tmpl1TrgtEntitys = jpaQueryFactory.selectFrom(bizPbancTmpl1Trgt)
                                    .where(bizPbancTmpl1Trgt.bizPbancTmpl1No.eq(bizPbancTmpl1Entity.getBizPbancTmpl1No()))
                                    .fetch();

                            if (tmpl1TrgtEntitys != null && tmpl1TrgtEntitys.size() > 0) {
                                List<Integer> tmpl1Trgt = new ArrayList<>();
                                for (BizPbancTmpl1Trgt i : tmpl1TrgtEntitys) {
                                    tmpl1Trgt.add(i.getBizPbancTmpl1TrgtCd());
                                }
                                bizPbancTmpl1Entity.setBizPbancTmpl1Trgts(tmpl1Trgt);
                            }

                        }

                        dto.setBizPbancTmpl1(bizPbancTmpl1Entity);
                    } else if (dto.getBizPbancType() == 2) {
                        dto.setBizPbancTmpl2(
                                jpaQueryFactory.selectFrom(bizPbancTmpl2)
                                        .where(bizPbancTmpl2.bizPbancNo.eq(dto.getBizPbancNo()))
                                        .fetchOne()
                        );
                    } else if (dto.getBizPbancType() == 3) {
                        dto.setBizPbancTmpl3(
                                jpaQueryFactory.selectFrom(bizPbancTmpl3)
                                        .where(bizPbancTmpl3.bizPbancNo.eq(dto.getBizPbancNo()))
                                        .fetchOne()
                        );
                    } else if (dto.getBizPbancType() == 4) {
                        dto.setBizPbancTmpl4(
                                jpaQueryFactory.selectFrom(bizPbancTmpl4)
                                        .where(bizPbancTmpl4.bizPbancNo.eq(dto.getBizPbancNo()))
                                        .fetchOne()
                        );
                    } else if( dto.getBizPbancType()==5){
                        dto.setBizPbancTmpl5(jpaQueryFactory.selectFrom(bizPbancTmpl5)
                                .where(bizPbancTmpl5.bizPbancNo.eq(dto.getBizPbancNo()))
                                .orderBy(bizPbancTmpl5.bizPbancTmpl5Ordr.asc())
                                .fetch());
                    }

                    /** 공통 코드에서 카테고리명 호출 */
                    CommonCodeMaster commonCode = jpaQueryFactory.selectFrom(commonCodeMaster)
                            .where(commonCodeMaster.code.eq("BIZ_PBANC_CTGR"))
                            .fetchOne();

                    if (commonCode != null) {
                        List<CommonCodeMaster> bizCtgrCodes = jpaQueryFactory.selectFrom(commonCodeMaster)
                                .where(commonCodeMaster.upIndividualCode.eq(commonCode.getIndividualCode()))
                                .fetch();

                        if (bizCtgrCodes != null && bizCtgrCodes.size() > 0) {
                            for (CommonCodeMaster i : bizCtgrCodes) {
                                if (i.getCode().equals(dto.getBizPbancCtgr().toString())) {
                                    dto.setBizPbancCtgrNm(i.getCodeName());
                                }
                            }
                        }
                    }
                }
            }
            return dtos;

        } else if(requestObject instanceof BizPbancRsltViewRequestVO) { /** 사업 공고 결과 */
            List<BizPbancRsltCustomApiResponseVO> dtos = new ArrayList<>();
            dtos = jpaQueryFactory.select(Projections.fields(BizPbancRsltCustomApiResponseVO.class,
                            bizPbancResult.bizPbancRsltNo,
                            bizPbancResult.bizPbancNo,
                            bizPbancResult.bizPbancRsltCn,
                            bizPbancResult.bizPbancNtcYn,
                            bizPbancResult.bizPbancRsltFile,
                            bizPbancResult.registUserId,
                            bizPbancMaster.bizPbancCtgr,
                            bizPbancMaster.bizPbancType,
                            bizPbancMaster.bizPbancNm,
                            lmsUser.userName
                    ))
                    .from(bizPbancResult)
                    .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizPbancResult.bizPbancNo))
                    .leftJoin(lmsUser).on(lmsUser.userId.eq(bizPbancResult.registUserId))
                    .where(getQuery(requestObject))
                    .orderBy(bizPbancResult.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();

            if (dtos != null && dtos.size() > 0) {
                for (BizPbancRsltCustomApiResponseVO dto : dtos) {
                    dto.setFileMasters(jpaQueryFactory.selectFrom(fileMaster)
                            .where(fileMaster.atchFileSn.eq(dto.getBizPbancRsltNo()))
                            .fetch());

                    dto.setBizOrganizationAplies(jpaQueryFactory.select(Projections.fields(BizPbancRsltOrgsApiResponseVO.class,
                                    bizOrganizationAply.bizOrgAplyLsnPlanBgng,
                                    bizOrganizationAply.bizOrgAplyLsnPlanEnd,
                                    bizOrganizationAply.bizOrgAplyTime,
                                    bizOrganizationAply.bizOrgAplyPicNm,
                                    bizOrganizationAply.registUserId,
                                    organizationInfo.organizationName
                            ))
                            .from(bizOrganizationAply)
                            .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                            .where(bizOrganizationAply.bizPbancNo.eq(dto.getBizPbancNo()), bizOrganizationAply.bizOrgAplyStts.eq(1))
                            .orderBy(bizOrganizationAply.createDateTime.desc())
                            .fetch());

                    dto.setBizOrganizationAprvs(jpaQueryFactory.select(Projections.fields(BizPbancRsltOrgsApiResponseVO.class,
                                    bizOrganizationAply.bizOrgAplyLsnPlanBgng,
                                    bizOrganizationAply.bizOrgAplyLsnPlanEnd,
                                    bizOrganizationAply.bizOrgAplyTime,
                                    bizOrganizationAply.bizOrgAplyPicNm,
                                    bizOrganizationAply.registUserId,
                                    organizationInfo.organizationName
                            ))
                            .from(bizOrganizationAply)
                            .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                            .where(bizOrganizationAply.bizPbancNo.eq(dto.getBizPbancNo()), bizOrganizationAply.bizOrgAplyStts.in(2, 9))
                            .orderBy(bizOrganizationAply.createDateTime.desc())
                            .fetch());

                    /** 공통 코드에서 카테고리명 호출 */
                    CommonCodeMaster commonCode = jpaQueryFactory.selectFrom(commonCodeMaster)
                            .where(commonCodeMaster.code.eq("BIZ_PBANC_CTGR"))
                            .fetchOne();

                    if (commonCode != null) {
                        List<CommonCodeMaster> bizCtgrCodes = jpaQueryFactory.selectFrom(commonCodeMaster)
                                .where(commonCodeMaster.upIndividualCode.eq(commonCode.getIndividualCode()))
                                .fetch();

                        if (bizCtgrCodes != null && bizCtgrCodes.size() > 0) {
                            for (CommonCodeMaster i : bizCtgrCodes) {
                                if (i.getCode().equals(dto.getBizPbancCtgr().toString())) {
                                    dto.setBizPbancCtgrNm(i.getCodeName());
                                }
                            }
                        }
                    }
                }
            }

            return dtos;

        } else if(requestObject instanceof BizOrganizationAplyViewRequestVO) { /** 사업 공고 신청 */
            List<BizOrganizationAply> bizOrganizationAplyList = jpaQueryFactory.selectFrom(bizOrganizationAply)
                    .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                    .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                    .where(getQuery(requestObject))
                    .orderBy(bizOrganizationAply.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();

            if (bizOrganizationAplyList != null && bizOrganizationAplyList.size() > 0){
                for(BizOrganizationAply entity : bizOrganizationAplyList){
                    entity.setOrganizationInfo(
                            jpaQueryFactory.selectFrom(organizationInfo)
                                    .where(organizationInfo.organizationCode.eq(entity.getOrgCd()))
                                    .fetchOne());

                    List<OrganizationAuthorityHistory> organizationAuthorityHistories=jpaQueryFactory.selectFrom(organizationAuthorityHistory)
                            .where(organizationAuthorityHistory.organizationCode.eq(entity.getOrgCd()))
                            .orderBy(organizationAuthorityHistory.createDateTime.desc())
                            .fetch();
                    if(organizationAuthorityHistories !=null && organizationAuthorityHistories.size()>0){
                        entity.setOrganizationAuthorityHistory(organizationAuthorityHistories.get(0));
                    }
                    entity.setBizInstructorAplies(jpaQueryFactory.selectFrom(bizInstructorAply)
                            .where(bizInstructorAply.bizOrgAplyNo.eq(entity.getBizOrgAplyNo()), bizInstructorAply.bizInstrAplyStts.eq(2))
                            .fetch());

                    List<BizOrganizationAplyDtl> bizOrganizationAplyDtls = entity.getBizOrganizationAplyDtls();
                    if (bizOrganizationAplyDtls != null && bizOrganizationAplyDtls.size() > 0) {
                        for (BizOrganizationAplyDtl bizOrganizationAplyDtl : bizOrganizationAplyDtls) {
                            List<BizInstructorIdentifyDtl> bizInstructorIdentifyDtls = jpaQueryFactory.selectFrom(bizInstructorIdentifyDtl)
                                    .where(bizInstructorIdentifyDtl.bizOrgAplyDtlNo.eq(bizOrganizationAplyDtl.getBizOrgAplyDtlNo()))
                                    .fetch();

                            if (bizInstructorIdentifyDtls != null && bizInstructorIdentifyDtls.size() > 0 ) {
                                bizOrganizationAplyDtl.setBizInstructorIdentifyDtls(bizInstructorIdentifyDtls);
                            }

                            List<BizInstructorAsgnm> bizInstructorAsgnms = jpaQueryFactory.selectFrom(bizInstructorAsgnm)
                                    .where(bizInstructorAsgnm.bizOrgAplyDtlNo.eq(bizOrganizationAplyDtl.getBizOrgAplyDtlNo()))
                                    .fetch();

                            if (bizInstructorAsgnms != null && bizInstructorAsgnms.size() > 0 ) {
                                bizOrganizationAplyDtl.setBizInstructorAsgnms(bizInstructorAsgnms);
                            }
                        }
                    }
                }
            }
            return bizOrganizationAplyList;

        } else if(requestObject instanceof BizEditHistViewRequestVO) { /** 사업 공고 신청 변경 이력 */
            return jpaQueryFactory.selectFrom(bizEditHist)
                    .leftJoin(bizOrganizationAply).on(bizOrganizationAply.bizOrgAplyNo.eq(bizEditHist.bizEditHistTrgtNo))
                    .where(getQuery(requestObject))
                    .orderBy(bizEditHist.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();

        } else if(requestObject instanceof BizAplyViewRequestVO) { /** 사업 공고 신청 - 언론인 */
        if(((BizAplyViewRequestVO) requestObject).getBizAplyType().equals("free")){
            List<BizAply> dtos = jpaQueryFactory.selectFrom(bizAply)
                    .leftJoin(lmsUser).on(lmsUser.userId.eq(bizAply.bizAplyUserID))
                    .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(lmsUser.organizationCode))
                    .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizAply.bizPbancNo))
                    .where(getQuery(requestObject))
                    .orderBy(bizAply.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
            if(dtos != null && dtos.size()==1){
                for(BizAply dto : dtos){
                    List<BizAplyDtl> dtls = jpaQueryFactory.selectFrom(bizAplyDtl)
                            .where(bizAplyDtl.sequenceNo.eq(dto.getSequenceNo()))
                            .leftJoin(bizPbancTmpl5).on(bizPbancTmpl5.bizPbancTmpl5No.eq(bizAplyDtl.bizPbancTmpl5No))
                            .orderBy(bizPbancTmpl5.bizPbancTmpl5Ordr.asc()).fetch();
                    if(dtls != null && dtls.size()>0){
                        for(BizAplyDtl dtl:dtls){
                            dtl.setBizPbancTmpl5(jpaQueryFactory.selectFrom(bizPbancTmpl5).where(bizPbancTmpl5.bizPbancTmpl5No.eq(dtl.getBizPbancTmpl5No())).fetchOne());
                        }
                        dto.setBizAplyDtls(dtls);
                    }

                    List<BizPbancTmpl5> bizPbancTmpl5s = jpaQueryFactory.selectFrom(bizPbancTmpl5)
                            .where(bizPbancTmpl5.bizPbancNo.eq(dto.getBizPbancNo()))
                            .orderBy(bizPbancTmpl5.bizPbancTmpl5Ordr.asc()).fetch();
                    if(bizPbancTmpl5s != null && bizPbancTmpl5s.size()>0){
                        BizPbancMaster master = dto.getBizPbancMaster();
                        master.setBizPbancTmpl5s(bizPbancTmpl5s);
                        dto.setBizPbancMaster(master);
                    }
                }
            }
            return  dtos;
        }else{
            return jpaQueryFactory.selectFrom(bizAply)
                    .leftJoin(lmsUser).on(lmsUser.userId.eq(bizAply.bizAplyUserID))
                    .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(lmsUser.organizationCode))
                    .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizAply.bizPbancNo))
                    .where(getQuery(requestObject))
                    .orderBy(bizAply.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        }

        } else if(requestObject instanceof BizInstructorViewRequestVO) { /** 강사 모집 */
            List<BizInstructor> entities = jpaQueryFactory.selectFrom(bizInstructor)
                    .where(getQuery(requestObject))
                    .orderBy(bizInstructor.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();

            if (entities != null && entities.size() > 0){
                for (BizInstructor entity : entities){
                    List<BizInstructorPbanc> bizInstructorPbancs = jpaQueryFactory.select(bizInstructorPbanc)
                            .from(bizInstructorPbanc)
                            .where(bizInstructorPbanc.bizInstrNo.eq(entity.getBizInstrNo()))
                            .fetch();

                    if (!bizInstructorPbancs.isEmpty() || bizInstructorPbancs.size() > 0) {
                        for (BizInstructorPbanc instructorPbanc : bizInstructorPbancs) {
                            instructorPbanc.setBizPbancMaster(jpaQueryFactory.selectFrom(bizPbancMaster)
                                    .where(bizPbancMaster.bizPbancNo.eq(instructorPbanc.getBizPbancNo()))
                                    .fetchOne());

                            instructorPbanc.setAprvOrgCnt(jpaQueryFactory.select(bizOrganizationAply.count())
                                    .from(bizOrganizationAply)
                                    .where(bizOrganizationAply.bizPbancNo.eq(instructorPbanc.getBizPbancNo()), bizOrganizationAply.bizOrgAplyStts.eq(2))
                                    .fetchOne());
                        }
                        entity.setBizInstructorPbancs(bizInstructorPbancs);
                    }
                }
            }
            return entities;

        } else if(requestObject instanceof BizInstructorAplyViewRequestVO) { /** 강사 신청 */
            List<BizOrganizationAply> entities = jpaQueryFactory.selectFrom(bizOrganizationAply)
                    .leftJoin(bizInstructorPbanc).on(bizInstructorPbanc.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                    .where(getQuery(requestObject))
                    .orderBy(bizOrganizationAply.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();

            if (entities != null && entities.size() > 0) {
                for (BizOrganizationAply entity : entities) {
                    BizInstructorPbanc bizInstr = jpaQueryFactory.selectFrom(bizInstructorPbanc)
                            .where(bizInstructorPbanc.bizPbancNo.eq(entity.getBizPbancNo()))
                            .fetchOne();
                    entity.setBizInstrNo(bizInstr.getBizInstrNo());
                    entity.setOrganizationInfo(jpaQueryFactory.selectFrom(organizationInfo)
                            .where(organizationInfo.organizationCode.eq(entity.getOrgCd()))
                            .fetchOne());

                    entity.setBizPbancMaster(jpaQueryFactory.selectFrom(bizPbancMaster)
                            .where(bizPbancMaster.bizPbancNo.eq(entity.getBizPbancNo()))
                            .fetchOne());

                    entity.setBizInstructorAplies(jpaQueryFactory.selectFrom(bizInstructorAply)
                            .where(bizInstructorAply.bizOrgAplyNo.eq(entity.getBizOrgAplyNo()),
                                    condition(((BizInstructorAplyViewRequestVO) requestObject).getBizInstrAplyInstrNm(), bizInstructorAply.bizInstrAplyInstrNm::contains))
                            .fetch());
                }
            }
            return entities;

        } else if(requestObject instanceof BizOrganizationAplyCustomViewRequestVO) { /** 강사 배정 */
            List<BizOrganizationAply> entities = jpaQueryFactory.selectFrom(bizOrganizationAply)
                    .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                    .leftJoin(bizInstructorPbanc).on(bizInstructorPbanc.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                    .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                    .leftJoin(bizInstructorAsgnm).on(bizInstructorAsgnm.bizOrgAplyNo.eq(bizOrganizationAply.bizOrgAplyNo))
                    .leftJoin(bizInstructorAply).on(bizInstructorAply.bizInstrAplyNo.eq(bizInstructorAsgnm.bizInstrAplyNo))
                    .where(getQuery(requestObject))
                    .groupBy(bizOrganizationAply.bizOrgAplyNo)
                    .orderBy(bizPbancMaster.createDateTime.desc(), organizationInfo.organizationName.asc())/** 학교순 정렬 시 */
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();

            if (entities != null && entities.size() > 0) {
                for (BizOrganizationAply entity : entities) {

                    BizInstructorPbanc bizInstr = jpaQueryFactory.selectFrom(bizInstructorPbanc)
                            .where(bizInstructorPbanc.bizPbancNo.eq(entity.getBizPbancNo()))
                            .fetchOne();
                    entity.setBizInstrNo(bizInstr.getBizInstrNo());

                    entity.setOrganizationInfo(jpaQueryFactory.selectFrom(organizationInfo)
                            .where(organizationInfo.organizationCode.eq(entity.getOrgCd()))
                            .fetchOne());

                    entity.setBizInstructorAplies(jpaQueryFactory.selectFrom(bizInstructorAply)
                            .where(bizInstructorAply.bizOrgAplyNo.eq(entity.getBizOrgAplyNo()))
                            .fetch());

                    List<BizInstructorAsgnm> bizInstructorAsgnmEntities = jpaQueryFactory.selectFrom(bizInstructorAsgnm)
                            .where(bizInstructorAsgnm.bizOrgAplyNo.eq(entity.getBizOrgAplyNo()))
                            .fetch();

                    if (!bizInstructorAsgnmEntities.isEmpty() && bizInstructorAsgnmEntities.size() > 0) {
                        entity.setBizInstructorAsgnms(bizInstructorAsgnmEntities);
                    }
                }
            }
            return entities;

        } else if(requestObject instanceof BizInstructorQuestionViewRequestVO) { /** 강사 지원 문의 */
            List<BizInstructorQuestion> entities = jpaQueryFactory.selectFrom(bizInstructorQuestion)
                    .leftJoin(bizOrganizationAply).on(bizOrganizationAply.bizOrgAplyNo.eq(bizInstructorQuestion.bizOrgAplyNo))
                    .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                    .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                    .where(getQuery(requestObject))
                    .orderBy(bizInstructorQuestion.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();

            if (!entities.isEmpty() && entities.size() > 0) {
                for (BizInstructorQuestion entity : entities) {
                    BizOrganizationAply bizOrgAplyEntity = jpaQueryFactory.selectFrom(bizOrganizationAply)
                            .where(bizOrganizationAply.bizOrgAplyNo.eq(entity.getBizOrgAplyNo()))
                            .fetchOne();
                    entity.setBizOrganizationAply(bizOrgAplyEntity);

                    bizOrgAplyEntity.setOrganizationInfo(jpaQueryFactory.selectFrom(organizationInfo)
                            .where(organizationInfo.organizationCode.eq(bizOrgAplyEntity.getOrgCd()))
                            .fetchOne());

                    entity.setBizPbancMaster(jpaQueryFactory.selectFrom(bizPbancMaster)
                            .where(bizPbancMaster.bizPbancNo.eq(bizOrgAplyEntity.getBizPbancNo()))
                            .fetchOne());

                    entity.setLmsUser(jpaQueryFactory.selectFrom(lmsUser)
                            .where(lmsUser.userId.eq(entity.getRegistUserId()))
                            .fetchOne());
                }
            }
            return entities;
        } else if(requestObject instanceof BizInstructorDistViewRequestVO) { /** 거리 증빙 */
            List<BizInstructorDist> entities = jpaQueryFactory.selectFrom(bizInstructorDist)
                    .leftJoin(bizOrganizationAply).on(bizOrganizationAply.bizOrgAplyNo.eq(bizInstructorDist.bizOrgAplyNo))
                    .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                    .leftJoin(bizInstructorAply).on(bizInstructorAply.bizInstrAplyNo.eq(bizInstructorDist.bizInstrAplyNo))
                    .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                    .where(getQuery(requestObject))
                    .orderBy(bizInstructorDist.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch().stream().peek(entity -> {
                        LmsUser lmsUserEntity = jpaQueryFactory.selectFrom(lmsUser)
                                .where(lmsUser.userId.eq(entity.getRegistUserId()))
                                .fetchOne();
                        entity.setInstrNm(lmsUserEntity.getUserName());

                        BizOrganizationAply bizOrganizationAplyEntity = jpaQueryFactory.selectFrom(bizOrganizationAply)
                                .where(bizOrganizationAply.bizOrgAplyNo.eq(entity.getBizOrgAplyNo()))
                                .fetchOne();

                        if (bizOrganizationAplyEntity != null){
                            bizOrganizationAplyEntity.setOrganizationInfo(jpaQueryFactory.selectFrom(organizationInfo)
                                    .where(organizationInfo.organizationCode.eq(bizOrganizationAplyEntity.getOrgCd()))
                                    .fetchOne());

                            entity.setBizOrganizationAply(bizOrganizationAplyEntity);
                            entity.setBizPbancMaster(jpaQueryFactory.selectFrom(bizPbancMaster)
                                    .where(bizPbancMaster.bizPbancNo.eq(bizOrganizationAplyEntity.getBizPbancNo()))
                                    .fetchOne());
                        }
                    }).collect(Collectors.toList());
            return entities;

        } else if(requestObject instanceof BizInstructorIdentifyViewRequestVO) { /** 강의확인서 */
            List<BizInstructorIdentify> entities = jpaQueryFactory.selectFrom(bizInstructorIdentify)
                    .leftJoin(bizOrganizationAply).on(bizOrganizationAply.bizOrgAplyNo.eq(bizInstructorIdentify.bizOrgAplyNo))
                    .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                    .leftJoin(lmsUser).on(lmsUser.userId.eq(bizInstructorIdentify.registUserId))
                    .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                    .where(getQuery(requestObject))
                    .orderBy(bizInstructorIdentify.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();

            if (entities != null && entities.size() > 0){
                for (BizInstructorIdentify entity : entities){
                    if(entity.getBizInstructorIdentifyDtls()!=null && entity.getBizInstructorIdentifyDtls().size()>0){
                        for(BizInstructorIdentifyDtl dtl :entity.getBizInstructorIdentifyDtls() ){
                            dtl.setBizOrganizationAplyDtl(jpaQueryFactory.selectFrom(bizOrganizationAplyDtl)
                                    .where(bizOrganizationAplyDtl.bizOrgAplyDtlNo.eq(dtl.getBizOrgAplyDtlNo()))
                                    .fetchOne());
                        }
                    }

                    LmsUser lmsUserEntity = jpaQueryFactory.selectFrom(lmsUser)
                            .where(lmsUser.userId.eq(entity.getRegistUserId()))
                            .fetchOne();
                    entity.setUserName(lmsUserEntity.getUserName());
                    entity.setUserTel(lmsUserEntity.getPhone());

                    BizOrganizationAply bizOrgAplyEntity = jpaQueryFactory.selectFrom(bizOrganizationAply)
                            .where(bizOrganizationAply.bizOrgAplyNo.eq(entity.getBizOrgAplyNo()))
                            .fetchOne();

                    if (bizOrgAplyEntity != null) {
                        entity.setBizOrganizationAply(bizOrgAplyEntity);

                        entity.setBizPbancMaster(jpaQueryFactory.selectFrom(bizPbancMaster)
                                .where(bizPbancMaster.bizPbancNo.eq(bizOrgAplyEntity.getBizPbancNo()))
                                .fetchOne());

                        OrganizationInfo organizationInfoEntity = jpaQueryFactory.selectFrom(organizationInfo)
                                .where(organizationInfo.organizationCode.eq(bizOrgAplyEntity.getOrgCd()))
                                .fetchOne();

                        if (organizationInfoEntity != null) {
                            entity.setOrganizationName(organizationInfoEntity.getOrganizationName());
                        }
                    }

                    BizInstructorDist bizInstructorDistEntity = jpaQueryFactory.selectFrom(bizInstructorDist)
                            .where(bizInstructorDist.registUserId.eq(entity.getRegistUserId()),
                                    bizInstructorDist.bizOrgAplyNo.eq(entity.getBizOrgAplyNo()))
                            .fetchOne();

                    if (bizInstructorDistEntity != null) {
                        entity.setBizInstructorDist(bizInstructorDistEntity);
                    }
                }
            }

            return entities;

        } else if(requestObject instanceof  FormeBizlecinfoViewRequestVO) { /** 포미 강의확인서 VIEW 페이지 */
            return  jpaQueryFactory.selectFrom(formeBizlecinfo)
                    .where(getQuery(requestObject))
                    .orderBy(formeBizlecinfo.blciId.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();

        }else if(requestObject instanceof  FormeBizlecinfoApiRequestVO) { /** 포미 강의확인서 API */
            return  jpaQueryFactory.selectFrom(formeBizlecinfo)
                    .where(getQuery(requestObject))
                    .orderBy(formeBizlecinfo.blciId.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();

        }else if(requestObject instanceof  FormeBizlecinfoDetailApiRequestVO) { /** 포미 강의확인서 API */
        List<FormeBizlecinfo> dtos = jpaQueryFactory.selectFrom(formeBizlecinfo)
                    .where(getQuery(requestObject))
                    .orderBy(formeBizlecinfo.blciId.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        for(FormeBizlecinfo dto : dtos){
            dto.setFormeFomBizapplyTtables(
                    jpaQueryFactory.selectFrom(formeFomBizapplyTtable)
                            .where(formeFomBizapplyTtable.bainId.eq(dto.getBainId()))
                            .orderBy(formeFomBizapplyTtable.battInning.asc())
                            .fetch());
        }
            return  dtos;

        }else if(requestObject instanceof MyInstructorStateViewRequestVO) { /** 강사 참여 현황 - 강의 현황 - 진행 중 */
            List<BizInstructorIdentifyViewResponseVO> dtos =  jpaQueryFactory.select(Projections.fields(BizInstructorIdentifyViewResponseVO.class,
                            bizInstructorAply.bizInstrAplyNo,
                            bizInstructorAply.bizInstrNo,
                            bizInstructorAply.bizOrgAplyNo,
                            bizInstructorAply.bizInstrAplyInstrNm,
                            bizInstructorAply.bizInstrAplyInstrId,
                            bizInstructorAply.bizInstrAplyCndtOrdr,
                            bizInstructorAply.bizInstrAplyCndtDist,
                            bizInstructorAply.bizInstrAplyCmnt,
                            bizInstructorAply.bizInstrAplyStts,
                            bizInstructorIdentify.registUserId,
                            bizOrganizationAplyDtl.bizOrgAplyLsnDtlYmd.substring(0,7).as("month")

                    ))
                    .from(bizInstructorAply)
                    .innerJoin(bizOrganizationAply).on(bizOrganizationAply.bizOrgAplyNo.eq(bizInstructorAply.bizOrgAplyNo))
                    .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                    .innerJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                    .innerJoin(bizInstructorAsgnm).on(bizInstructorAsgnm.bizInstrAplyNo.eq(bizInstructorAply.bizInstrAplyNo))
                    .innerJoin(bizOrganizationAplyDtl).on(bizOrganizationAplyDtl.bizOrgAplyDtlNo.eq(bizInstructorAsgnm.bizOrgAplyDtlNo))
                    .leftJoin(bizInstructorIdentify).on(bizInstructorIdentify.bizOrgAplyNo.eq(bizOrganizationAply.bizOrgAplyNo),
                            bizInstructorIdentify.bizInstrIdntyYm.startsWith(bizOrganizationAplyDtl.bizOrgAplyLsnDtlYmd.substring(0,4)),
                            bizInstructorIdentify.bizInstrIdntyYm.endsWith(bizOrganizationAplyDtl.bizOrgAplyLsnDtlYmd.substring(5,7)),
                            bizInstructorIdentify.registUserId.eq(bizInstructorAply.bizInstrAplyInstrId))
                    .where(getQuery(requestObject))
                    .groupBy(bizInstructorAply.bizInstrAplyNo, bizOrganizationAplyDtl.bizOrgAplyLsnDtlYmd.substring(0,7))
                    .orderBy(bizInstructorAply.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .distinct()
                    .fetch();

            if (dtos != null && dtos.size() > 0){
                for (BizInstructorIdentifyViewResponseVO dto : dtos){
                    dto.setBizInstructor(jpaQueryFactory.selectFrom(bizInstructor)
                            .where(bizInstructor.bizInstrNo.eq(dto.getBizInstrNo()))
                            .fetchOne());

                    BizOrganizationAply bizOrganizationAplyEntity = jpaQueryFactory.selectFrom(bizOrganizationAply)
                            .where(bizOrganizationAply.bizOrgAplyNo.eq(dto.getBizOrgAplyNo()))
                            .fetchOne();

                    BizInstructorPbanc bizInstrPbanc =jpaQueryFactory.selectFrom(bizInstructorPbanc)
                            .where(bizInstructorPbanc.bizInstrNo.eq(dto.getBizInstrNo()),
                                    bizInstructorPbanc.bizPbancNo.eq(bizOrganizationAplyEntity.getBizPbancNo()))
                            .fetchOne();
                    if(!bizInstrPbanc.equals(null)){
                        dto.setSequenceNo(bizInstrPbanc.getSequenceNo());
                    }
                    bizOrganizationAplyEntity.setOrganizationInfo(jpaQueryFactory.selectFrom(organizationInfo)
                            .where(organizationInfo.organizationCode.eq(bizOrganizationAplyEntity.getOrgCd()))
                            .fetchOne());

                    dto.setBizOrganizationAply(bizOrganizationAplyEntity);
                    dto.setBizPbancMaster(jpaQueryFactory.selectFrom(bizPbancMaster)
                            .where(bizPbancMaster.bizPbancNo.eq(bizOrganizationAplyEntity.getBizPbancNo()))
                            .fetchOne());

                    /** 강사 신청서에 해당하는 강사 배정 리스트 호출 */
                    List<BizInstructorAsgnm> bizInstructorAsgnms = jpaQueryFactory.selectFrom(bizInstructorAsgnm)
                            .where(bizInstructorAsgnm.bizInstrAplyNo.eq(dto.getBizInstrAplyNo()), bizInstructorAsgnm.bizOrgAplyNo.eq(dto.getBizOrgAplyNo()),
                                    bizOrganizationAplyDtl.bizOrgAplyLsnDtlYmd.substring(0, 7).eq(dto.getMonth()))
                            .innerJoin(bizOrganizationAplyDtl).on(bizOrganizationAplyDtl.bizOrgAplyDtlNo.eq(bizInstructorAsgnm.bizOrgAplyDtlNo))
                            .orderBy(bizOrganizationAplyDtl.bizOrgAplyLsnDtlYmd.asc())
                            .groupBy(bizInstructorAsgnm.bizOrgAplyDtlNo)
                            .fetch();

                    BizInstructorDist instructorDist = jpaQueryFactory.selectFrom(bizInstructorDist)
                            .where(bizInstructorDist.registUserId.eq(dto.getBizInstrAplyInstrId()), bizInstructorDist.bizInstrAplyNo.eq(dto.getBizInstrAplyNo()),
                                    bizInstructorDist.bizDistStts.eq(1))
                            .fetchOne();

                    if (instructorDist != null) {
                        dto.setBizInstructorDist(instructorDist);
                    }

                    /** 강사 배정 리스트 있을 시 */
                    if (bizInstructorAsgnms != null && bizInstructorAsgnms.size() > 0){
                        List<BizOrganizationAplyDtl> bizOrganizationAplyDtlList = new ArrayList<>();

                        for (BizInstructorAsgnm asgnm : bizInstructorAsgnms) {
                            BizOrganizationAplyDtl dtl = jpaQueryFactory.selectFrom(bizOrganizationAplyDtl)
                                    .where(bizOrganizationAplyDtl.bizOrgAplyDtlNo.eq(asgnm.getBizOrgAplyDtlNo()),
                                            bizOrganizationAplyDtl.bizOrgAplyLsnDtlYmd.substring(0, 7).eq(dto.getMonth()))
                                    .fetchOne();
                            if(dtl != null){
                                bizOrganizationAplyDtlList.add(dtl);
                            }

                        }
                        dto.setBizOrganizationAplyDtls(bizOrganizationAplyDtlList);
                        BizInstructorIdentify bizInstructorIdentifyEntity = jpaQueryFactory.selectFrom(bizInstructorIdentify)
                                .where(bizInstructorIdentify.bizOrgAplyNo.eq(bizOrganizationAplyEntity.getBizOrgAplyNo()),
                                        bizInstructorIdentify.bizInstrIdntyYm.eq(dto.getMonth().replace("-","")),
                                        bizInstructorIdentify.registUserId.eq(dto.getBizInstrAplyInstrId()))
                                .fetchOne();
                        if(bizInstructorIdentifyEntity!=null){
                            dto.setBizInstrIdnty(bizInstructorIdentifyEntity);
                            dto.setBizInstrIdntyNo(bizInstructorIdentifyEntity.getBizInstrIdntyNo());
                        }
                    }
                }
            }
            return dtos;

        } else if(requestObject instanceof BizInstructorIdentifyCalculateViewRequestVO) { /** 강의확인서 */
            List<BizInstructorAply> aplyEntities = null;
            List<String> identifyDate = new ArrayList<>();
            if(((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getGroupType()!=null
                    &&((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getGroupType().equals("org")){
                /** 정산금액 조회 기관별 */
                aplyEntities = jpaQueryFactory.selectFrom(bizInstructorAply)
                        .from(bizInstructorAply)
                        .innerJoin(bizOrganizationAply).on(bizOrganizationAply.bizOrgAplyNo.eq(bizInstructorAply.bizOrgAplyNo))
                        .innerJoin(bizInstructorIdentify).on(bizInstructorIdentify.bizOrgAplyNo.eq(bizInstructorAply.bizOrgAplyNo)
                                .and(bizInstructorIdentify.registUserId.eq(bizInstructorAply.bizInstrAplyInstrId)))
                        .innerJoin(bizPbancMaster).on(bizOrganizationAply.bizPbancNo.eq(bizPbancMaster.bizPbancNo))
                        .innerJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                        .where(getQuery(requestObject))
                        .groupBy(bizInstructorAply.bizInstrAplyNo)
                        .orderBy(organizationInfo.organizationName.asc(),bizOrganizationAply.bizPbancNo.asc(),bizInstructorAply.bizInstrAplyInstrNm.asc())
                        .offset(requestObject.getPageable().getOffset())
                        .limit(requestObject.getPageable().getPageSize())
                        .fetch();
            }else if(((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getGroupType()!=null
                    &&((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getGroupType().equals("orgExcel")){
                /** 정산금액 조회 엑셀 기관별이면 강의확인서 없어도 출력 */
                aplyEntities = jpaQueryFactory.selectFrom(bizInstructorAply)
                        .from(bizInstructorAply)
                        .innerJoin(bizOrganizationAply).on(bizOrganizationAply.bizOrgAplyNo.eq(bizInstructorAply.bizOrgAplyNo))
                        .leftJoin(bizInstructorIdentify).on(bizInstructorIdentify.bizOrgAplyNo.eq(bizInstructorAply.bizOrgAplyNo)
                                .and(bizInstructorIdentify.registUserId.eq(bizInstructorAply.bizInstrAplyInstrId)))
                        .innerJoin(bizPbancMaster).on(bizOrganizationAply.bizPbancNo.eq(bizPbancMaster.bizPbancNo))
                        .innerJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                        .where(getQuery(requestObject))
                        .groupBy(bizInstructorAply.bizInstrAplyNo)
                        .orderBy(organizationInfo.organizationName.asc(),bizOrganizationAply.bizPbancNo.asc(),bizInstructorAply.bizInstrAplyInstrNm.asc())
                        .offset(requestObject.getPageable().getOffset())
                        .limit(requestObject.getPageable().getPageSize())
                        .fetch();
            }else{
                /** 정산금액 조회 강사별 */
                aplyEntities = jpaQueryFactory.selectFrom(bizInstructorAply)
                        .from(bizInstructorAply)
                        .innerJoin(bizOrganizationAply).on(bizOrganizationAply.bizOrgAplyNo.eq(bizInstructorAply.bizOrgAplyNo))
                        .innerJoin(bizInstructorIdentify).on(bizInstructorIdentify.bizOrgAplyNo.eq(bizInstructorAply.bizOrgAplyNo)
                                .and(bizInstructorIdentify.registUserId.eq(bizInstructorAply.bizInstrAplyInstrId)))
                        .innerJoin(bizPbancMaster).on(bizOrganizationAply.bizPbancNo.eq(bizPbancMaster.bizPbancNo))
                        .innerJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                        .where(getQuery(requestObject))
                        .groupBy(bizInstructorAply.bizInstrAplyNo)
                        .orderBy(bizInstructorAply.bizInstrAplyInstrNm.asc(),bizInstructorAply.bizInstrAplyInstrId.asc(), bizOrganizationAply.bizPbancNo.asc(), organizationInfo.organizationName.asc())
                        .offset(requestObject.getPageable().getOffset())
                        .limit(requestObject.getPageable().getPageSize())
                        .fetch();
            }

            if(aplyEntities !=null && aplyEntities.size()>0) {
                for (BizInstructorAply aply : aplyEntities) {
                    /** 필요한 값만 객체에 담아오게 수정 */
                    /** 수업시간/배정시간에 따른 강사료/현재지급액 등 계산 */
                    List<BizInstructorAmtTimeVO> allAmtTimeSelect = jpaQueryFactory.select(Projections.fields(BizInstructorAmtTimeVO.class,
                                    bizInstructorIdentify.bizInstrIdntyAmt,
                                    bizInstructorIdentify.bizInstrIdntyTime))
                            .from(bizInstructorIdentify)
                            .leftJoin(bizOrganizationAply).on(bizOrganizationAply.bizOrgAplyNo.eq(bizInstructorIdentify.bizOrgAplyNo))
                            .leftJoin(bizPbancMaster).on(bizOrganizationAply.bizPbancNo.eq(bizPbancMaster.bizPbancNo))
                            .leftJoin(lmsUser).on(bizInstructorIdentify.registUserId.eq(lmsUser.userId))
                            .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                            .where(bizInstructorIdentify.registUserId.eq(aply.getBizInstrAplyInstrId()),
                                    bizInstructorIdentify.bizOrgAplyNo.eq(aply.getBizOrgAplyNo()),
                                    bizInstructorIdentify.bizInstrIdntyStts.eq(4))
                            .fetch();

                    Integer allAmt = 0;
                    Integer allTime=jpaQueryFactory.select(bizOrganizationAplyDtl.bizOrgAplyLsnDtlHr.sum())
                            .from(bizOrganizationAplyDtl)
                            .where(bizOrganizationAplyDtl.bizOrgAplyNo.eq(aply.getBizOrgAplyNo()))
                            .innerJoin(bizInstructorAsgnm).on(bizInstructorAsgnm.bizOrgAplyDtlNo.eq(bizOrganizationAplyDtl.bizOrgAplyDtlNo).and(bizInstructorAsgnm.bizInstrAplyNo.eq(aply.getBizInstrAplyNo())))
                            .fetchOne();

                    aply.setBizInstrAsgmnTimeAll(allTime);
                    for(BizInstructorAmtTimeVO identify : allAmtTimeSelect){
                        allAmt+=Integer.parseInt(identify.getBizInstrIdntyAmt());
                        allTime-=(int) Double.parseDouble(identify.getBizInstrIdntyTime());
                    }
                    aply.setBizInstrIdntyAmtAll(allAmt);
                    aply.setBizInstrAsgmnTimeRest(allTime);

                    /** 실제 강의확인서 데이터 이부분이 가장 오래걸릴 것 같은데  BizInstructorAply에 List<BizInstructorIdentify>리스트를 언제나 가져오도록 엔터티 수정하는 것도 좀 고민이 됨 */
                    List<BizInstructorIdentify> entities;
                    if(((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getGroupType()!=null &&((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getGroupType().equals("instr")){
                        entities = jpaQueryFactory.selectFrom(bizInstructorIdentify)
                                .leftJoin(bizOrganizationAply).on(bizOrganizationAply.bizOrgAplyNo.eq(bizInstructorIdentify.bizOrgAplyNo))
                                .leftJoin(bizPbancMaster).on(bizOrganizationAply.bizPbancNo.eq(bizPbancMaster.bizPbancNo))
                                .where(bizInstructorIdentify.registUserId.eq(aply.getBizInstrAplyInstrId()),
                                        bizInstructorIdentify.bizOrgAplyNo.eq(aply.getBizOrgAplyNo()),
                                        bizInstructorIdentify.bizInstrIdntyStts.eq(4),
                                        condition(((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getYear(), bizInstructorIdentify.bizInstrIdntyPayYm.substring(0,4)::eq),
                                        condition(((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getMonth(), bizInstructorIdentify.bizInstrIdntyPayYm.substring(4,6)::eq))
                                .orderBy(bizInstructorIdentify.bizInstrIdntyYm.asc())
                                .fetch();
                    }else{
                        entities =jpaQueryFactory.selectFrom(bizInstructorIdentify)
                                .leftJoin(bizOrganizationAply).on(bizOrganizationAply.bizOrgAplyNo.eq(bizInstructorIdentify.bizOrgAplyNo))
                                .leftJoin(bizPbancMaster).on(bizOrganizationAply.bizPbancNo.eq(bizPbancMaster.bizPbancNo))
                                .where(bizInstructorIdentify.registUserId.eq(aply.getBizInstrAplyInstrId()),
                                        bizInstructorIdentify.bizOrgAplyNo.eq(aply.getBizOrgAplyNo()),
                                        bizInstructorIdentify.bizInstrIdntyStts.eq(4),
                                        condition(((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getYear(), bizInstructorIdentify.bizInstrIdntyPayYm.substring(0,4)::eq))
                                .orderBy(bizInstructorIdentify.bizInstrIdntyYm.asc())
                                .fetch();
                    }

                    aply.setLmsUser(jpaQueryFactory.selectFrom(lmsUser)
                            .where(lmsUser.userId.eq(aply.getBizInstrAplyInstrId()))
                            .fetchOne());
                    BizOrganizationAply bizOrgAplyEntity = jpaQueryFactory.selectFrom(bizOrganizationAply)
                            .where(bizOrganizationAply.bizOrgAplyNo.eq(aply.getBizOrgAplyNo()))
                            .fetchOne();

                    if (bizOrgAplyEntity != null) {
                        OrganizationInfo organizationInfoEntity = jpaQueryFactory.selectFrom(organizationInfo)
                                .where(organizationInfo.organizationCode.eq(bizOrgAplyEntity.getOrgCd()))
                                .fetchOne();
                        if (organizationInfoEntity != null) {
                            bizOrgAplyEntity.setOrganizationInfo(organizationInfoEntity);
                        }
                    }
                    aply.setBizOrganizationAply(bizOrgAplyEntity);

                    BizInstructorDist bizInstructorDistEntity = jpaQueryFactory.selectFrom(bizInstructorDist)
                            .where(bizInstructorDist.bizInstrAplyNo.eq(aply.getBizInstrAplyNo()),
                                    bizInstructorDist.bizOrgAplyNo.eq(aply.getBizOrgAplyNo()),
                                    bizInstructorDist.bizDistStts.eq(1))
                            .orderBy(bizInstructorDist.createDateTime.desc())
                            .fetchFirst();
                    if (bizInstructorDistEntity != null) {
                        aply.setBizInstructorDist(bizInstructorDistEntity);
                    }

                    /** 아래는 필요한 부분 */
                    /** 강의확인서 열을 동적 생성하기 위한 전체 달력 저장 및 정렬 중복제거 기능 */
                    if (entities != null && entities.size() > 0) {
                        for (BizInstructorIdentify entity : entities) {
                            identifyDate.add(entity.getBizInstrIdntyYm());
                            for (BizInstructorIdentifyDtl dtl : entity.getBizInstructorIdentifyDtls()) {
                                dtl.setBizOrganizationAplyDtl(jpaQueryFactory.selectFrom(bizOrganizationAplyDtl)
                                        .where(bizOrganizationAplyDtl.bizOrgAplyDtlNo.eq(dtl.getBizOrgAplyDtlNo()))
                                        .fetchOne());
                            }
                        }
                    }
                    identifyDate=identifyDate.stream().distinct().collect(Collectors.toList());
                    Collections.sort(identifyDate);
                    aply.setBizInstructorIdentifies(entities);
                }
                aplyEntities.get(0).setDtlDate(identifyDate);
            }
            return aplyEntities;

        } else if(requestObject instanceof BizInstructorIdentifyManageViewRequestVO) { /** 강의확인서 */
            List<BizInstructorIdentifyManageApiResponseVO> dtos = new ArrayList<>();
            dtos = jpaQueryFactory.select(Projections.fields(BizInstructorIdentifyManageApiResponseVO.class,
                            bizInstructorAply.bizInstrAplyNo,
                            bizInstructorAply.bizInstrNo,
                            bizInstructorAply.bizOrgAplyNo,
                            bizInstructorAply.bizInstrAplyInstrNm,
                            bizInstructorAply.bizInstrAplyInstrId,
                            bizInstructorAply.bizInstrAplyStts,
                            lmsUser.phone
                    ))
                    .from(bizInstructorAply)
                    .leftJoin(lmsUser).on(lmsUser.userId.eq(bizInstructorAply.bizInstrAplyInstrId))
                    .leftJoin(bizOrganizationAply).on(bizOrganizationAply.bizOrgAplyNo.eq(bizInstructorAply.bizOrgAplyNo))
                    .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationAply.orgCd))
                    .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                    .where(getQuery(requestObject))
                    .orderBy(bizInstructorAply.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .distinct()
                    .fetch();

            if (dtos != null && dtos.size() > 0){
                for (BizInstructorIdentifyManageApiResponseVO dto : dtos){
                    BizInstructorAply bizInstructorAplyEntity = jpaQueryFactory.selectFrom(bizInstructorAply)
                            .where(bizInstructorAply.bizInstrAplyNo.eq(dto.getBizInstrAplyNo()))
                            .fetchOne();

                    dto.setBizInstructorIdentifies(jpaQueryFactory.selectFrom(bizInstructorIdentify)
                            .where(bizInstructorIdentify.bizOrgAplyNo.eq(dto.getBizOrgAplyNo()),
                                    bizInstructorIdentify.registUserId.eq(bizInstructorAplyEntity.getBizInstrAplyInstrId()),
                                    bizInstructorIdentify.bizInstrIdntyStts.in(3,4),
                                    betweenTime(new BizInstructorIdentify(), requestObject.getStartDate(), requestObject.getEndDate()))
                            .fetch());

                    BizOrganizationAply bizOrganizationAplyEntity = jpaQueryFactory.selectFrom(bizOrganizationAply)
                            .where(bizOrganizationAply.bizOrgAplyNo.eq(dto.getBizOrgAplyNo()))
                            .fetchOne();

                    OrganizationInfo organizationInfoEntity = jpaQueryFactory.selectFrom(organizationInfo)
                            .where(organizationInfo.organizationCode.eq(bizOrganizationAplyEntity.getOrgCd()))
                            .fetchOne();

                    bizOrganizationAplyEntity.setOrganizationInfo(organizationInfoEntity);
                    dto.setBizOrganizationAply(bizOrganizationAplyEntity);
                }
            }
            return dtos;

        } else if(requestObject instanceof BizSurveyViewRequestVO) { /** 상호평가 평가지 */
            List<BizSurvey> entities = jpaQueryFactory.selectFrom(bizSurvey)
                    .where(getQuery(requestObject))
                    .orderBy(bizSurvey.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();

            if (entities.size() > 0 && entities != null) {
                for (BizSurvey entity : entities) {
                    entity.setBizSurveyQitems(jpaQueryFactory.selectFrom(bizSurveyQitem)
                            .where(bizSurveyMaster.bizSurveyNo.eq(entity.getBizSurveyNo()))
                            .innerJoin(bizSurveyMaster).on(bizSurveyMaster.bizSurveyQitemNo.eq(bizSurveyQitem.bizSurveyQitemNo))
                            .fetch());
                }
            }
            return entities;

        } else if(requestObject instanceof BizSurveyQitemViewRequestVO) { /** 상호평가 문항 */
            return jpaQueryFactory.selectFrom(bizSurveyQitem)
                    .where(getQuery(requestObject))
                    .orderBy(bizSurveyQitem.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();

        } else if(requestObject instanceof BizOrganizationRsltRptViewRequestVO) { /** 결과보고서 */
            List<BizOrganizationRsltRpt> entities = jpaQueryFactory.selectFrom(bizOrganizationRsltRpt)
                    .leftJoin(bizOrganizationAply).on(bizOrganizationAply.bizOrgAplyNo.eq(bizOrganizationRsltRpt.bizOrgAplyNo))
                    .leftJoin(bizPbancMaster).on(bizPbancMaster.bizPbancNo.eq(bizOrganizationAply.bizPbancNo))
                    .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(bizOrganizationRsltRpt.orgCd))
                    .where(getQuery(requestObject))
                    .orderBy(bizOrganizationRsltRpt.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();

            if (entities != null && entities.size() > 0){
                for(BizOrganizationRsltRpt entity : entities){
                    entity.setOrganizationInfo(jpaQueryFactory.selectFrom(organizationInfo)
                            .where(organizationInfo.organizationCode.eq(entity.getOrgCd())).fetchOne());

                    List<BizInstructorIdentify> bizInstructorIdentifies = jpaQueryFactory.selectFrom(bizInstructorIdentify)
                            .where(bizInstructorIdentify.bizOrgAplyNo.eq(entity.getBizOrgAplyNo()),
                                    bizInstructorIdentify.bizInstrIdntyStts.in(3,4))
                            .orderBy(bizInstructorIdentify.bizInstrIdntyYm.asc())
                            .fetch();

                    if (bizInstructorIdentifies.size() > 0 && bizInstructorIdentifies != null) {
                        for (BizInstructorIdentify bizInstructorIdentify : bizInstructorIdentifies) {
                            LmsUser userName = jpaQueryFactory.selectFrom(lmsUser)
                                    .where(lmsUser.userId.eq(bizInstructorIdentify.getRegistUserId()))
                                    .fetchOne();
                            if(userName!=null){
                                bizInstructorIdentify.setUserName(userName.getUserName());
                            }
                            List<BizInstructorIdentifyDtl> bizInstructorIdentifyDtls = bizInstructorIdentify.getBizInstructorIdentifyDtls();

                            if (bizInstructorIdentifyDtls != null && bizInstructorIdentifyDtls.size() > 0) {
                                for (BizInstructorIdentifyDtl bizInstructorIdentifyDtlEntity : bizInstructorIdentifyDtls) {
                                    bizInstructorIdentifyDtlEntity.setBizOrganizationAplyDtl(jpaQueryFactory.selectFrom(bizOrganizationAplyDtl)
                                            .where(bizOrganizationAplyDtl.bizOrgAplyDtlNo.eq(bizInstructorIdentifyDtlEntity.getBizOrgAplyDtlNo()))
                                            .fetchOne());
                                }
                                bizInstructorIdentify.setBizInstructorIdentifyDtls(bizInstructorIdentifyDtls);
                            }
                        }
                        entity.setBizInstructorIdentifies(bizInstructorIdentifies);
                    }

                    entity.setBizOrganizationAply(jpaQueryFactory.selectFrom(bizOrganizationAply)
                            .where(bizOrganizationAply.bizOrgAplyNo.eq(entity.getBizOrgAplyNo()))
                            .fetchOne());

                    List<BizInstructorAsgnm> asgnms = jpaQueryFactory.selectFrom(bizInstructorAsgnm)
                            .where(bizInstructorAsgnm.bizOrgAplyNo.eq(entity.getBizOrgAplyNo()))
                            .groupBy(bizInstructorAsgnm.bizInstrAplyNo)
                            .fetch();
                    if(!asgnms.equals(null) && asgnms.size()>0){
                        entity.getBizOrganizationAply().setBizInstructorAsgnms(asgnms);
                    }
                }
            }
            return entities;
        } else if(requestObject instanceof BizSurveyAnsViewRequestVO) { /** 상호평가 답변 */
            return jpaQueryFactory.selectFrom(bizSurveyAns)
                    .where(getQuery(requestObject))
                    .orderBy(bizSurveyAns.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();

        } else if(requestObject instanceof BizInstructorClclnDdlnViewRequestVO) { /** 정산 마감일 */
            List<BizInstructorClclnDdln> entities = jpaQueryFactory.selectFrom(bizInstructorClclnDdln)
                    .where(getQuery(requestObject))
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();

            if (!entities.isEmpty() && entities.size() > 0) {
                for (BizInstructorClclnDdln entity : entities) {
                    String value = "0";
                    if (entity.getBizInstrClclnDdlnMm() < 10) {
                        value = entity.getBizInstrClclnDdlnYr().toString()+"0"+entity.getBizInstrClclnDdlnMm().toString();
                    } else {
                        value = entity.getBizInstrClclnDdlnYr().toString()+entity.getBizInstrClclnDdlnMm().toString();
                    }
                    List<BizInstructorIdentify> bizInstructorIdentifies = jpaQueryFactory.selectFrom(bizInstructorIdentify)
                            .where(searchContainTextDate(entity.getBizInstrClclnDdlnMm().toString()), bizInstructorIdentify.bizInstrIdntyStts.eq(3),
                                    checkPeriod(new BizInstructorClclnDdln(), entity.getBizInstrClclnDdlnMm().toString()))
                            .fetch();
                    if (bizInstructorIdentifies.size() > 0 && !bizInstructorIdentifies.isEmpty()) {
                        entity.setBizInstructorIdentifiesSize(bizInstructorIdentifies.size());
                    }
                }
            }

            return entities;

        } else if(requestObject instanceof BizInstructorDistCrtrAmtViewRequestVO) { /** 이동거리 기준단가 */
            return jpaQueryFactory.selectFrom(bizInstructorDistCrtrAmt)
                    .where(getQuery(requestObject))
                    .orderBy(bizInstructorDistCrtrAmt.bizInstrDistCrtrAmtYr.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        } else {
            return null;
        }
    }

    /**
     * where 절
     */
    private <T extends CSViewVOSupport> Predicate[] getQuery(T requestObject) {
        if(requestObject instanceof BizPbancViewRequestVO) { /** 사업 공고 */
            return new Predicate[] { condition(((BizPbancViewRequestVO) requestObject).getBizPbancNo(), bizPbancMaster.bizPbancNo::eq),
                    condition(((BizPbancViewRequestVO) requestObject).getBizPbancType(), bizPbancMaster.bizPbancType::eq),
                    condition(((BizPbancViewRequestVO) requestObject).getBizPbancYr(), bizPbancMaster.bizPbancYr::eq),
                    condition(((BizPbancViewRequestVO) requestObject).getBizPbancRnd(), bizPbancMaster.bizPbancRnd::eq),
                    condition(((BizPbancViewRequestVO) requestObject).getNotBizPbancStts(), bizPbancMaster.bizPbancStts::ne),
                    searchContainText(requestObject, ((BizPbancViewRequestVO) requestObject).getContainTextType(), ((BizPbancViewRequestVO) requestObject).getContainText()) };
        } else if(requestObject instanceof BizPbancRsltViewRequestVO) { /** 사업 공고 결과 */
            return new Predicate[] { betweenTime(new BizPbancResult(), requestObject.getStartDate(), requestObject.getEndDate()),
                    condition(((BizPbancRsltViewRequestVO) requestObject).getBizPbancRsltNo(), bizPbancResult.bizPbancRsltNo::eq),
                    condition(((BizPbancRsltViewRequestVO) requestObject).getBizPbancNo(), bizPbancResult.bizPbancNo::eq),
                    condition(((BizPbancRsltViewRequestVO) requestObject).getBizPbancYr(), bizPbancMaster.bizPbancYr::eq),
                    searchContainText(requestObject, ((BizPbancRsltViewRequestVO) requestObject).getContainTextType(), ((BizPbancRsltViewRequestVO) requestObject).getContainText()) };
        } else if(requestObject instanceof BizOrganizationAplyViewRequestVO) { /** 사업 공고 신청 */
            if (((BizOrganizationAplyViewRequestVO) requestObject).getBizOrgAplyPbancList() != null) {
                if (((BizOrganizationAplyViewRequestVO) requestObject).getBizOrgAplyPbancList() == 0) {
                    return new Predicate[] { condition(((BizOrganizationAplyViewRequestVO) requestObject).getBizOrgAplyNo(), bizOrganizationAply.bizOrgAplyNo::eq),
                            condition(1, bizOrganizationAply.bizOrgAplyStts::eq),
                            condition(((BizOrganizationAplyViewRequestVO) requestObject).getBizPbancYr(), bizPbancMaster.bizPbancYr::eq),
                            condition(((BizOrganizationAplyViewRequestVO) requestObject).getBizPbancType(), bizPbancMaster.bizPbancType::eq),
                            condition(((BizOrganizationAplyViewRequestVO) requestObject).getBizPbancNo(), bizPbancMaster.bizPbancNo::eq),
                            condition(((BizOrganizationAplyViewRequestVO) requestObject).getBizPbancRnd(), bizPbancMaster.bizPbancRnd::eq),
                            searchContainText(requestObject, ((BizOrganizationAplyViewRequestVO) requestObject).getContainTextType(), ((BizOrganizationAplyViewRequestVO) requestObject).getContainText()) };
                } else {
                    Integer[] sttss = {2, 9};
                    return new Predicate[] { condition(((BizOrganizationAplyViewRequestVO) requestObject).getBizOrgAplyNo(), bizOrganizationAply.bizOrgAplyNo::eq),
                            condition(sttss, bizOrganizationAply.bizOrgAplyStts::in),
                            condition(((BizOrganizationAplyViewRequestVO) requestObject).getBizPbancYr(), bizPbancMaster.bizPbancYr::eq),
                            condition(((BizOrganizationAplyViewRequestVO) requestObject).getBizPbancType(), bizPbancMaster.bizPbancType::eq),
                            condition(((BizOrganizationAplyViewRequestVO) requestObject).getBizPbancNo(), bizPbancMaster.bizPbancNo::eq),
                            condition(((BizOrganizationAplyViewRequestVO) requestObject).getBizPbancRnd(), bizPbancMaster.bizPbancRnd::eq),
                            searchContainText(requestObject, ((BizOrganizationAplyViewRequestVO) requestObject).getContainTextType(), ((BizOrganizationAplyViewRequestVO) requestObject).getContainText()) };
                }
            } else if(((BizOrganizationAplyViewRequestVO) requestObject).getIsDist() != null) {
                if (((BizOrganizationAplyViewRequestVO) requestObject).getIsDist() != 0) {
                    return new Predicate[] { condition(((BizOrganizationAplyViewRequestVO) requestObject).getBizOrgAplyNo(), bizOrganizationAply.bizOrgAplyNo::eq),
                            condition(((BizOrganizationAplyViewRequestVO) requestObject).getBizOrgAplyStts(), bizOrganizationAply.bizOrgAplyStts::eq),
                            condition(((BizOrganizationAplyViewRequestVO) requestObject).getBizPbancYr(), bizPbancMaster.bizPbancYr::eq),
                            condition(((BizOrganizationAplyViewRequestVO) requestObject).getBizPbancType(), bizPbancMaster.bizPbancType::eq),
                            condition(((BizOrganizationAplyViewRequestVO) requestObject).getBizPbancNo(), bizPbancMaster.bizPbancNo::eq),
                            condition(((BizOrganizationAplyViewRequestVO) requestObject).getBizPbancRnd(), bizPbancMaster.bizPbancRnd::eq),
                            condition(2, bizInstructorAply.bizInstrAplyStts::eq),
                            searchContainText(requestObject, ((BizOrganizationAplyViewRequestVO) requestObject).getContainTextType(), ((BizOrganizationAplyViewRequestVO) requestObject).getContainText()) };
                } else {
                    return new Predicate[] { condition(((BizOrganizationAplyViewRequestVO) requestObject).getBizOrgAplyNo(), bizOrganizationAply.bizOrgAplyNo::eq),
                            condition(((BizOrganizationAplyViewRequestVO) requestObject).getBizOrgAplyStts(), bizOrganizationAply.bizOrgAplyStts::eq),
                            condition(((BizOrganizationAplyViewRequestVO) requestObject).getBizPbancYr(), bizPbancMaster.bizPbancYr::eq),
                            condition(((BizOrganizationAplyViewRequestVO) requestObject).getBizPbancType(), bizPbancMaster.bizPbancType::eq),
                            condition(((BizOrganizationAplyViewRequestVO) requestObject).getBizPbancNo(), bizPbancMaster.bizPbancNo::eq),
                            condition(((BizOrganizationAplyViewRequestVO) requestObject).getBizPbancRnd(), bizPbancMaster.bizPbancRnd::eq),
                            searchContainText(requestObject, ((BizOrganizationAplyViewRequestVO) requestObject).getContainTextType(), ((BizOrganizationAplyViewRequestVO) requestObject).getContainText()) };
                }
            } else {
                return new Predicate[] { condition(((BizOrganizationAplyViewRequestVO) requestObject).getBizOrgAplyNo(), bizOrganizationAply.bizOrgAplyNo::eq),
                        condition(((BizOrganizationAplyViewRequestVO) requestObject).getBizOrgAplyStts(), bizOrganizationAply.bizOrgAplyStts::eq),
                        condition(((BizOrganizationAplyViewRequestVO) requestObject).getBizPbancYr(), bizPbancMaster.bizPbancYr::eq),
                        condition(((BizOrganizationAplyViewRequestVO) requestObject).getBizPbancType(), bizPbancMaster.bizPbancType::eq),
                        condition(((BizOrganizationAplyViewRequestVO) requestObject).getBizPbancNo(), bizPbancMaster.bizPbancNo::eq),
                        condition(((BizOrganizationAplyViewRequestVO) requestObject).getBizPbancRnd(), bizPbancMaster.bizPbancRnd::eq),
                        searchContainText(requestObject, ((BizOrganizationAplyViewRequestVO) requestObject).getContainTextType(), ((BizOrganizationAplyViewRequestVO) requestObject).getContainText()) };
            }
        } else if(requestObject instanceof BizAplyViewRequestVO) { /** 사업 공고 신청 - 언론인/기본형 */
            if (((BizAplyViewRequestVO) requestObject).getBizAplyType().equals("journalist")) {/**언론인  */
                return new Predicate[] { condition(((BizAplyViewRequestVO) requestObject).getName(), lmsUser.userName::eq),
                        condition(((BizAplyViewRequestVO) requestObject).getBizPbancYr(), bizPbancMaster.bizPbancYr::eq),
                        condition(((BizAplyViewRequestVO) requestObject).getBizPbancType(), bizPbancMaster.bizPbancType::eq),
                        condition(((BizAplyViewRequestVO) requestObject).getBizPbancRnd(), bizPbancMaster.bizPbancRnd::eq),
                        condition(((BizAplyViewRequestVO) requestObject).getBizAplyType(), bizAply.bizAplyType::eq),
                        searchContainText(requestObject, ((BizAplyViewRequestVO) requestObject).getContainTextType(), ((BizAplyViewRequestVO) requestObject).getContainText())};
            } else {/** 기본형 */
                return new Predicate[] {
                        condition(((BizAplyViewRequestVO) requestObject).getSequenceNo(), bizAply.sequenceNo::eq),
                        condition(((BizAplyViewRequestVO) requestObject).getBizPbancYr(), bizPbancMaster.bizPbancYr::eq),
                        condition(((BizAplyViewRequestVO) requestObject).getBizPbancType(), bizPbancMaster.bizPbancType::eq),
                        condition(((BizAplyViewRequestVO) requestObject).getBizPbancRnd(), bizPbancMaster.bizPbancRnd::eq),
                        condition(((BizAplyViewRequestVO) requestObject).getBizAplyType(), bizAply.bizAplyType::eq),
                        condition(((BizAplyViewRequestVO) requestObject).getBizPbancCtgr(), bizPbancMaster.bizPbancCtgr::eq),
                        condition(((BizAplyViewRequestVO) requestObject).getBizPbancName(), bizPbancMaster.bizPbancNm::eq),
                        condition(((BizAplyViewRequestVO) requestObject).getBizPbancNo(), bizPbancMaster.bizPbancNo::eq),
                        condition(((BizAplyViewRequestVO) requestObject).getBizAplyStts(), bizAply.bizAplyStts::eq),
                        searchContainText(requestObject, ((BizAplyViewRequestVO) requestObject).getContainTextType(), ((BizAplyViewRequestVO) requestObject).getContainText())};
            }
        } else if(requestObject instanceof BizEditHistViewRequestVO) { /** 사업 공고 신청 변경 이력 */
            return new Predicate[] { condition(((BizEditHistViewRequestVO) requestObject).getBizEditHistTrgtNo(), bizOrganizationAply.bizOrgAplyNo::eq) };
        } else if(requestObject instanceof BizInstructorViewRequestVO) { /** 강사 모집 */
            return new Predicate[] { condition(((BizInstructorViewRequestVO) requestObject).getBizInstrNo(), bizInstructor.bizInstrNo::eq),
                    condition(((BizInstructorViewRequestVO) requestObject).getBizInstrNm(), bizInstructor.bizInstrNm::eq),
                    condition(((BizInstructorViewRequestVO) requestObject).getBizInstrStts(), bizInstructor.bizInstrStts::eq) };
        } else if(requestObject instanceof BizInstructorAplyViewRequestVO) { /** 강사 신청 */
            return new Predicate[] { betweenTime(new BizOrganizationAply(), requestObject.getStartDate(), requestObject.getEndDate()),
                    condition(bizOrganizationAply.bizPbancNo, bizInstructorPbanc.bizPbancNo::eq),
                    condition(((BizInstructorAplyViewRequestVO) requestObject).getBizOrgAplyNo(), bizOrganizationAply.bizOrgAplyNo::eq),
                    searchContainText(requestObject, ((BizInstructorAplyViewRequestVO) requestObject).getContainTextType(), ((BizInstructorAplyViewRequestVO) requestObject).getContainText()) };
        } else if(requestObject instanceof BizOrganizationAplyCustomViewRequestVO) { /** 강사 배정 */
            Integer[] states = {2, 7, 9};
            return new Predicate[] { condition(bizOrganizationAply.bizPbancNo, bizInstructorPbanc.bizPbancNo::eq),
                    condition(((BizOrganizationAplyCustomViewRequestVO) requestObject).getBizOrgAplyNo(), bizOrganizationAply.bizOrgAplyNo::eq),
                    condition(((BizOrganizationAplyCustomViewRequestVO) requestObject).getBizPbancType(), bizPbancMaster.bizPbancType::eq),
                    condition(((BizOrganizationAplyCustomViewRequestVO) requestObject).getBizPbancYr(), bizPbancMaster.bizPbancYr::eq),
                    condition(((BizOrganizationAplyCustomViewRequestVO) requestObject).getBizPbancRnd(), bizPbancMaster.bizPbancRnd::eq),
                    condition(states, bizOrganizationAply.bizOrgAplyStts::in),
                    searchContainText(requestObject, ((BizOrganizationAplyCustomViewRequestVO) requestObject).getContainTextType(), ((BizOrganizationAplyCustomViewRequestVO) requestObject).getContainText()) };
        } else if(requestObject instanceof BizInstructorQuestionViewRequestVO) { /** 강사 문의 질문 */
            return new Predicate[] { condition(((BizInstructorQuestionViewRequestVO) requestObject).getBizInstrQstnNo(), bizInstructorQuestion.bizInstrQstnNo::eq),
                    searchContainText(requestObject, ((BizInstructorQuestionViewRequestVO) requestObject).getContainTextType(), ((BizInstructorQuestionViewRequestVO) requestObject).getContainText()) };
        } else if(requestObject instanceof BizInstructorDistViewRequestVO) { /** 거리 증빙 */
            return new Predicate[] { condition(((BizInstructorDistViewRequestVO) requestObject).getBizPbancType(), bizPbancMaster.bizPbancType::eq),
                    condition(((BizInstructorDistViewRequestVO) requestObject).getBizPbancYr(), bizPbancMaster.bizPbancYr::eq),
                    condition(((BizInstructorDistViewRequestVO) requestObject).getBizPbancType(), bizPbancMaster.bizPbancType::eq),
                    condition(((BizInstructorDistViewRequestVO) requestObject).getBizPbancRnd(), bizPbancMaster.bizPbancRnd::eq),
                    condition(((BizInstructorDistViewRequestVO) requestObject).getBizInstrDistNo(), bizInstructorDist.bizInstrDistNo::eq),
                    condition(((BizInstructorDistViewRequestVO) requestObject).getBizDistStts(), bizInstructorDist.bizDistStts::eq),
                    searchContainText(requestObject, ((BizInstructorDistViewRequestVO) requestObject).getContainTextType(), ((BizInstructorDistViewRequestVO) requestObject).getContainText()) };
        } else if(requestObject instanceof BizInstructorIdentifyViewRequestVO) { /** 강의확인서 */
            if (((BizInstructorIdentifyViewRequestVO) requestObject).getSttsType() != null) {
                if (((BizInstructorIdentifyViewRequestVO) requestObject).getSttsType() == 1) {
                    Integer[] statuss = {1, 2, 9};
                    return new Predicate[] { betweenTime(new BizInstructorIdentify(), requestObject.getStartDate(), requestObject.getEndDate()),
                            condition(((BizInstructorIdentifyViewRequestVO) requestObject).getBizInstrIdntyNo(), bizInstructorIdentify.bizInstrIdntyNo::eq),
                            condition(((BizInstructorIdentifyViewRequestVO) requestObject).getBizOrgAplyNo(), bizInstructorIdentify.bizOrgAplyNo::eq),
                            condition(statuss, bizInstructorIdentify.bizInstrIdntyStts::in),
                            condition(((BizInstructorIdentifyViewRequestVO) requestObject).getBizInstrIdntyStts(), bizInstructorIdentify.bizInstrIdntyStts::eq),
                            condition(((BizInstructorIdentifyViewRequestVO) requestObject).getRegistUserId(), bizInstructorIdentify.registUserId::eq),
                            condition(((BizInstructorIdentifyViewRequestVO) requestObject).getYear(), bizInstructorIdentify.bizInstrIdntyYm.substring(0,4)::eq),
                            condition(((BizInstructorIdentifyViewRequestVO) requestObject).getMonth(), bizInstructorIdentify.bizInstrIdntyYm.substring(4,6)::eq),
                            condition(((BizInstructorIdentifyViewRequestVO) requestObject).getBizPbancType(), bizPbancMaster.bizPbancType::eq),
                            condition(((BizInstructorIdentifyViewRequestVO) requestObject).getBizPbancRnd(), bizPbancMaster.bizPbancRnd::eq),
                            condition(((BizInstructorIdentifyViewRequestVO) requestObject).getBizOrgAplyRgn(), bizOrganizationAply.bizOrgAplyRgn::eq),
                            searchContainText(requestObject, ((BizInstructorIdentifyViewRequestVO) requestObject).getContainTextType(), ((BizInstructorIdentifyViewRequestVO) requestObject).getContainText()) };
                } else if (((BizInstructorIdentifyViewRequestVO) requestObject).getSttsType() == 2) {
                    Integer[] statuss = {3, 4};
                    return new Predicate[] { betweenTime(new BizInstructorIdentify(), requestObject.getStartDate(), requestObject.getEndDate()),
                            condition(((BizInstructorIdentifyViewRequestVO) requestObject).getBizInstrIdntyNo(), bizInstructorIdentify.bizInstrIdntyNo::eq),
                            condition(((BizInstructorIdentifyViewRequestVO) requestObject).getBizOrgAplyNo(), bizInstructorIdentify.bizOrgAplyNo::eq),
                            condition(statuss, bizInstructorIdentify.bizInstrIdntyStts::in),
                            condition(((BizInstructorIdentifyViewRequestVO) requestObject).getBizInstrIdntyStts(), bizInstructorIdentify.bizInstrIdntyStts::eq),
                            condition(((BizInstructorIdentifyViewRequestVO) requestObject).getRegistUserId(), bizInstructorIdentify.registUserId::eq),
                            condition(((BizInstructorIdentifyViewRequestVO) requestObject).getYear(), bizInstructorIdentify.bizInstrIdntyYm.substring(0,4)::eq),
                            condition(((BizInstructorIdentifyViewRequestVO) requestObject).getMonth(), bizInstructorIdentify.bizInstrIdntyYm.substring(4,6)::eq),
                            condition(((BizInstructorIdentifyViewRequestVO) requestObject).getPayMonth(), bizInstructorIdentify.bizInstrIdntyPayYm.substring(4,6)::eq),
                            condition(((BizInstructorIdentifyViewRequestVO) requestObject).getBizPbancType(), bizPbancMaster.bizPbancType::eq),
                            condition(((BizInstructorIdentifyViewRequestVO) requestObject).getBizPbancRnd(), bizPbancMaster.bizPbancRnd::eq),
                            condition(((BizInstructorIdentifyViewRequestVO) requestObject).getBizOrgAplyRgn(), bizOrganizationAply.bizOrgAplyRgn::eq),
                            searchContainText(requestObject, ((BizInstructorIdentifyViewRequestVO) requestObject).getContainTextType(), ((BizInstructorIdentifyViewRequestVO) requestObject).getContainText()) };
                } else {
                    if (((BizInstructorIdentifyViewRequestVO) requestObject).getMonth() != null) {
                        return new Predicate[] { betweenTime(new BizInstructorIdentify(), requestObject.getStartDate(), requestObject.getEndDate()),
                                condition(((BizInstructorIdentifyViewRequestVO) requestObject).getBizInstrIdntyNo(), bizInstructorIdentify.bizInstrIdntyNo::eq),
                                condition(((BizInstructorIdentifyViewRequestVO) requestObject).getBizOrgAplyNo(), bizInstructorIdentify.bizOrgAplyNo::eq),
                                condition(3, bizInstructorIdentify.bizInstrIdntyStts::eq),
                                searchContainTextDate(((BizInstructorIdentifyViewRequestVO) requestObject).getMonth()),
                                checkPeriod(new BizInstructorClclnDdln(), ((BizInstructorIdentifyViewRequestVO) requestObject).getMonth()),
                                condition(((BizInstructorIdentifyViewRequestVO) requestObject).getRegistUserId(), bizInstructorIdentify.registUserId::eq),
                                condition(((BizInstructorIdentifyViewRequestVO) requestObject).getYear(), bizInstructorIdentify.bizInstrIdntyYm.substring(0,4)::eq),
                                condition(((BizInstructorIdentifyViewRequestVO) requestObject).getBizPbancType(), bizPbancMaster.bizPbancType::eq),
                                condition(((BizInstructorIdentifyViewRequestVO) requestObject).getBizPbancRnd(), bizPbancMaster.bizPbancRnd::eq),
                                condition(((BizInstructorIdentifyViewRequestVO) requestObject).getBizOrgAplyRgn(), bizOrganizationAply.bizOrgAplyRgn::eq),
                                searchContainText(requestObject, ((BizInstructorIdentifyViewRequestVO) requestObject).getContainTextType(), ((BizInstructorIdentifyViewRequestVO) requestObject).getContainText()) };
                    } else {
                        return new Predicate[] { betweenTime(new BizInstructorIdentify(), requestObject.getStartDate(), requestObject.getEndDate()),
                                condition(((BizInstructorIdentifyViewRequestVO) requestObject).getBizInstrIdntyNo(), bizInstructorIdentify.bizInstrIdntyNo::eq),
                                condition(((BizInstructorIdentifyViewRequestVO) requestObject).getBizOrgAplyNo(), bizInstructorIdentify.bizOrgAplyNo::eq),
                                condition(3, bizInstructorIdentify.bizInstrIdntyStts::eq),
                                condition(((BizInstructorIdentifyViewRequestVO) requestObject).getRegistUserId(), bizInstructorIdentify.registUserId::eq),
                                condition(((BizInstructorIdentifyViewRequestVO) requestObject).getYear(), bizInstructorIdentify.bizInstrIdntyYm.substring(0,4)::eq),
                                condition(((BizInstructorIdentifyViewRequestVO) requestObject).getBizPbancType(), bizPbancMaster.bizPbancType::eq),
                                condition(((BizInstructorIdentifyViewRequestVO) requestObject).getBizPbancRnd(), bizPbancMaster.bizPbancRnd::eq),
                                condition(((BizInstructorIdentifyViewRequestVO) requestObject).getBizOrgAplyRgn(), bizOrganizationAply.bizOrgAplyRgn::eq),
                                searchContainText(requestObject, ((BizInstructorIdentifyViewRequestVO) requestObject).getContainTextType(), ((BizInstructorIdentifyViewRequestVO) requestObject).getContainText()) };
                    }
                }
            } else {
                return new Predicate[] { betweenTime(new BizInstructorIdentify(), requestObject.getStartDate(), requestObject.getEndDate()),
                        condition(((BizInstructorIdentifyViewRequestVO) requestObject).getBizInstrIdntyNo(), bizInstructorIdentify.bizInstrIdntyNo::eq),
                        condition(((BizInstructorIdentifyViewRequestVO) requestObject).getBizOrgAplyNo(), bizInstructorIdentify.bizOrgAplyNo::eq),
                        condition(((BizInstructorIdentifyViewRequestVO) requestObject).getBizInstrIdntyStts(), bizInstructorIdentify.bizInstrIdntyStts::eq),
                        condition(((BizInstructorIdentifyViewRequestVO) requestObject).getRegistUserId(), bizInstructorIdentify.registUserId::eq),
                        condition(((BizInstructorIdentifyViewRequestVO) requestObject).getYear(), bizInstructorIdentify.bizInstrIdntyYm.substring(0,4)::eq),
                        condition(((BizInstructorIdentifyViewRequestVO) requestObject).getMonth(), bizInstructorIdentify.bizInstrIdntyYm.substring(4,6)::eq),
                        condition(((BizInstructorIdentifyViewRequestVO) requestObject).getBizPbancType(), bizPbancMaster.bizPbancType::eq),
                        condition(((BizInstructorIdentifyViewRequestVO) requestObject).getBizPbancRnd(), bizPbancMaster.bizPbancRnd::eq),
                        condition(((BizInstructorIdentifyViewRequestVO) requestObject).getBizOrgAplyRgn(), bizOrganizationAply.bizOrgAplyRgn::eq),
                        searchContainText(requestObject, ((BizInstructorIdentifyViewRequestVO) requestObject).getContainTextType(), ((BizInstructorIdentifyViewRequestVO) requestObject).getContainText()) };
            }
        } else if(requestObject instanceof BizInstructorIdentifyDtlExcelRequestVO) { /** 강의확인서 주제 */
            if (((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getSttsType() != null) {
                if (((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getSttsType() == 1) {
                    Integer[] statuss = {1, 2, 9};
                    return new Predicate[] { betweenTime(new BizInstructorIdentify(), requestObject.getStartDate(), requestObject.getEndDate()),
                            condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getBizInstrIdntyNo(), bizInstructorIdentify.bizInstrIdntyNo::eq),
                            condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getBizOrgAplyNo(), bizInstructorIdentify.bizOrgAplyNo::eq),
                            condition(statuss, bizInstructorIdentify.bizInstrIdntyStts::in),
                            condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getBizInstrIdntyStts(), bizInstructorIdentify.bizInstrIdntyStts::eq),
                            condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getRegistUserId(), bizInstructorIdentify.registUserId::eq),
                            condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getYear(), bizInstructorIdentify.bizInstrIdntyYm.substring(0,4)::eq),
                            condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getMonth(), bizInstructorIdentify.bizInstrIdntyYm.substring(4,6)::eq),
                            condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getBizPbancType(), bizPbancMaster.bizPbancType::eq),
                            condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getBizPbancRnd(), bizPbancMaster.bizPbancRnd::eq),
                            condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getBizOrgAplyRgn(), bizOrganizationAply.bizOrgAplyRgn::eq),
                            searchContainText(requestObject, ((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getContainTextType(), ((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getContainText()) };
                } else if (((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getSttsType() == 2) {
                    Integer[] statuss = {3, 4};
                    return new Predicate[] { betweenTime(new BizInstructorIdentify(), requestObject.getStartDate(), requestObject.getEndDate()),
                            condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getBizInstrIdntyNo(), bizInstructorIdentify.bizInstrIdntyNo::eq),
                            condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getBizOrgAplyNo(), bizInstructorIdentify.bizOrgAplyNo::eq),
                            condition(statuss, bizInstructorIdentify.bizInstrIdntyStts::in),
                            condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getBizInstrIdntyStts(), bizInstructorIdentify.bizInstrIdntyStts::eq),
                            condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getRegistUserId(), bizInstructorIdentify.registUserId::eq),
                            condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getYear(), bizInstructorIdentify.bizInstrIdntyYm.substring(0,4)::eq),
                            condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getMonth(), bizInstructorIdentify.bizInstrIdntyYm.substring(4,6)::eq),
                            condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getPayMonth(), bizInstructorIdentify.bizInstrIdntyPayYm.substring(4,6)::eq),
                            condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getBizPbancType(), bizPbancMaster.bizPbancType::eq),
                            condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getBizPbancRnd(), bizPbancMaster.bizPbancRnd::eq),
                            condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getBizOrgAplyRgn(), bizOrganizationAply.bizOrgAplyRgn::eq),
                            searchContainText(requestObject, ((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getContainTextType(), ((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getContainText()) };
                } else {
                    if (((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getMonth() != null) {
                        return new Predicate[] { betweenTime(new BizInstructorIdentify(), requestObject.getStartDate(), requestObject.getEndDate()),
                                condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getBizInstrIdntyNo(), bizInstructorIdentify.bizInstrIdntyNo::eq),
                                condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getBizOrgAplyNo(), bizInstructorIdentify.bizOrgAplyNo::eq),
                                condition(3, bizInstructorIdentify.bizInstrIdntyStts::eq),
                                searchContainTextDate(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getMonth()),
                                checkPeriod(new BizInstructorClclnDdln(), ((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getMonth()),
                                condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getRegistUserId(), bizInstructorIdentify.registUserId::eq),
                                condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getYear(), bizInstructorIdentify.bizInstrIdntyYm.substring(0,4)::eq),
                                condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getBizPbancType(), bizPbancMaster.bizPbancType::eq),
                                condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getBizPbancRnd(), bizPbancMaster.bizPbancRnd::eq),
                                condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getBizOrgAplyRgn(), bizOrganizationAply.bizOrgAplyRgn::eq),
                                searchContainText(requestObject, ((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getContainTextType(), ((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getContainText()) };
                    } else {
                        return new Predicate[] { betweenTime(new BizInstructorIdentify(), requestObject.getStartDate(), requestObject.getEndDate()),
                                condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getBizInstrIdntyNo(), bizInstructorIdentify.bizInstrIdntyNo::eq),
                                condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getBizOrgAplyNo(), bizInstructorIdentify.bizOrgAplyNo::eq),
                                condition(3, bizInstructorIdentify.bizInstrIdntyStts::eq),
                                condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getRegistUserId(), bizInstructorIdentify.registUserId::eq),
                                condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getYear(), bizInstructorIdentify.bizInstrIdntyYm.substring(0,4)::eq),
                                condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getBizPbancType(), bizPbancMaster.bizPbancType::eq),
                                condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getBizPbancRnd(), bizPbancMaster.bizPbancRnd::eq),
                                condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getBizOrgAplyRgn(), bizOrganizationAply.bizOrgAplyRgn::eq),
                                searchContainText(requestObject, ((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getContainTextType(), ((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getContainText()) };
                    }
                }
            } else {
                return new Predicate[] { betweenTime(new BizInstructorIdentify(), requestObject.getStartDate(), requestObject.getEndDate()),
                        condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getBizInstrIdntyNo(), bizInstructorIdentify.bizInstrIdntyNo::eq),
                        condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getBizOrgAplyNo(), bizInstructorIdentify.bizOrgAplyNo::eq),
                        condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getBizInstrIdntyStts(), bizInstructorIdentify.bizInstrIdntyStts::eq),
                        condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getRegistUserId(), bizInstructorIdentify.registUserId::eq),
                        condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getYear(), bizInstructorIdentify.bizInstrIdntyYm.substring(0,4)::eq),
                        condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getMonth(), bizInstructorIdentify.bizInstrIdntyYm.substring(4,6)::eq),
                        condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getBizPbancType(), bizPbancMaster.bizPbancType::eq),
                        condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getBizPbancRnd(), bizPbancMaster.bizPbancRnd::eq),
                        condition(((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getBizOrgAplyRgn(), bizOrganizationAply.bizOrgAplyRgn::eq),
                        searchContainText(requestObject, ((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getContainTextType(), ((BizInstructorIdentifyDtlExcelRequestVO) requestObject).getContainText()) };
            }
        }else if(requestObject instanceof FormeBizlecinfoViewRequestVO) { /** 포미 강의확인서 View 페이지 */
            return new Predicate[]{
                    condition(((FormeBizlecinfoViewRequestVO) requestObject).getBlciUserNm(), formeBizlecinfo.blciUserNm::contains),
                    condition(((FormeBizlecinfoViewRequestVO) requestObject).getBlciUserId(), formeBizlecinfo.blciUserId::contains),
                    condition(((FormeBizlecinfoViewRequestVO) requestObject).getBninTitle(), formeBizlecinfo.bninTitle::contains),
                    condition(((FormeBizlecinfoViewRequestVO) requestObject).getBainInstNm(), formeBizlecinfo.bainInstNm::contains),
                    condition(((FormeBizlecinfoViewRequestVO) requestObject).getBninDegree(), formeBizlecinfo.bninDegree::eq),
                    condition(((FormeBizlecinfoViewRequestVO) requestObject).getYear(), formeBizlecinfo.blciYymm::startsWith),
                    condition(((FormeBizlecinfoViewRequestVO) requestObject).getMonth(), formeBizlecinfo.blciYymm::endsWith),
            };
        }else if(requestObject instanceof FormeBizlecinfoApiRequestVO) { /** 포미 강의확인서 API */
            return new Predicate[]{
                    condition(((FormeBizlecinfoApiRequestVO) requestObject).getBlciId(), formeBizlecinfo.blciId::eq),
                    condition(((FormeBizlecinfoApiRequestVO) requestObject).getBlciUserId(), formeBizlecinfo.blciUserId::eq),
            };
        }else if(requestObject instanceof FormeBizlecinfoDetailApiRequestVO) { /** 포미 강의확인서 API */
            return new Predicate[]{
                    condition(((FormeBizlecinfoDetailApiRequestVO) requestObject).getBlciId(), formeBizlecinfo.blciId::eq),
            };
        }else if(requestObject instanceof BizInstructorIdentifyCalculateViewRequestVO) { /** 강의확인서 정산 */
            if(((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getGroupType() !=null &&
                    ((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getGroupType() .equals("orgExcel")){
                /** 정산금액 조회 엑셀 기관별이면 강의확인서 없어도 출력 */
                Integer[] statuss = {2, 3, 7, 9};
                Integer year = null;
                if(((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getYear()!=null){
                    year = Integer.valueOf(((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getYear());
                }
                return new Predicate[] { betweenTime(new BizInstructorIdentify(), requestObject.getStartDate(), requestObject.getEndDate()),
                        condition(((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getBizInstrAplyStts(), bizInstructorAply.bizInstrAplyStts::eq),
                        condition(statuss, bizOrganizationAply.bizOrgAplyStts::in),
                        condition(((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getRegistUserId(), bizInstructorAply.bizInstrAplyInstrId::eq),
                        condition(year, bizPbancMaster.bizPbancYr::eq),
                        condition(((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getBizPbancType(), bizPbancMaster.bizPbancType::eq),
                        condition(((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getBizPbancRnd(), bizPbancMaster.bizPbancRnd::eq),
                        condition(((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getBizOrgAplyRgn(), bizOrganizationAply.bizOrgAplyRgn::eq),
                        searchContainText(requestObject, ((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getContainTextType(), ((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getContainText()) };
            }else{
                return new Predicate[] { betweenTime(new BizInstructorIdentify(), requestObject.getStartDate(), requestObject.getEndDate()),
                        condition(((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getBizInstrAplyStts(), bizInstructorAply.bizInstrAplyStts::eq),
                        condition(((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getBizInstrIdntyStts(), bizInstructorIdentify.bizInstrIdntyStts::eq),
                        condition(((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getRegistUserId(), bizInstructorAply.bizInstrAplyInstrId::eq),
                        condition(((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getYear(), bizInstructorIdentify.bizInstrIdntyPayYm.substring(0,4)::eq),
                        condition(((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getMonth(), bizInstructorIdentify.bizInstrIdntyPayYm.substring(4,6)::eq),
                        condition(((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getBizPbancType(), bizPbancMaster.bizPbancType::eq),
                        condition(((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getBizPbancRnd(), bizPbancMaster.bizPbancRnd::eq),
                        condition(((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getBizOrgAplyRgn(), bizOrganizationAply.bizOrgAplyRgn::eq),
                        searchContainText(requestObject, ((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getContainTextType(), ((BizInstructorIdentifyCalculateViewRequestVO) requestObject).getContainText()) };
            }
        } else if(requestObject instanceof MyInstructorStateViewRequestVO) { /** 강사 참여 현황 - 강의 현황 - 진행 중 */
            return new Predicate[] { condition(((MyInstructorStateViewRequestVO) requestObject).getBizOrgAplyRgn(), bizOrganizationAply.bizOrgAplyRgn::eq),
                    condition(((MyInstructorStateViewRequestVO) requestObject).getBizInstrAplyInstrId(), bizInstructorAply.bizInstrAplyInstrId::eq),
                    condition(((MyInstructorStateViewRequestVO) requestObject).getBizInstrAplyNo(), bizInstructorAply.bizInstrAplyNo::eq),
                    condition(((MyInstructorStateViewRequestVO) requestObject).getNotBizInstrAplyNo(), bizInstructorAply.bizInstrAplyNo::ne),
                    condition(((MyInstructorStateViewRequestVO) requestObject).getBizInstrAplyStts(), bizInstructorAply.bizInstrAplyStts::eq),
                    condition(((MyInstructorStateViewRequestVO) requestObject).getBizInstrIdntyYm().substring(0,4), bizOrganizationAplyDtl.bizOrgAplyLsnDtlYmd.substring(0,4)::eq),
                    condition(((MyInstructorStateViewRequestVO) requestObject).getBizInstrIdntyYm().substring(4,6), bizOrganizationAplyDtl.bizOrgAplyLsnDtlYmd.substring(5,7)::eq),
                    condition(((MyInstructorStateViewRequestVO) requestObject).getSearchYm(), bizOrganizationAplyDtl.bizOrgAplyLsnDtlYmd.substring(0,7)::eq),
                    checkPeriod(new BizOrganizationAply(), "dtl"),
                    searchContainText(requestObject, ((MyInstructorStateViewRequestVO) requestObject).getContainTextType(), ((MyInstructorStateViewRequestVO) requestObject).getContainText())};
        } else if(requestObject instanceof BizInstructorIdentifyDtlViewRequestVO) { /** 강의확인서 강의시간표 */
            return new Predicate[] {
                    condition(((BizInstructorIdentifyDtlViewRequestVO) requestObject).getBizInstrIdntyNo(), bizInstructorIdentifyDtl.bizInstrIdntyNo::eq) };
        } else if(requestObject instanceof BizInstructorIdentifyManageViewRequestVO) { /** 강의확인서 관리 */
            return new Predicate[] {
                    condition(((BizInstructorIdentifyManageViewRequestVO) requestObject).getBizInstrAplyNo(), bizInstructorAply.bizInstrAplyNo::eq),
                    condition(2, bizInstructorAply.bizInstrAplyStts::eq),
                    condition(((BizInstructorIdentifyManageViewRequestVO) requestObject).getUserId(), lmsUser.userId::contains),
                    condition(((BizInstructorIdentifyManageViewRequestVO) requestObject).getUserName(), lmsUser.userName::contains),
                    condition(((BizInstructorIdentifyManageViewRequestVO) requestObject).getYear(), bizInstructorIdentify.bizInstrIdntyYm.substring(0,4)::eq),
                    condition(((BizInstructorIdentifyManageViewRequestVO) requestObject).getMonth(), bizInstructorIdentify.bizInstrIdntyYm.substring(4,6)::eq),
                    searchContainText(requestObject, ((BizInstructorIdentifyManageViewRequestVO) requestObject).getContainTextType(), ((BizInstructorIdentifyManageViewRequestVO) requestObject).getContainText()) };
        } else if(requestObject instanceof BizSurveyViewRequestVO) { /** 상호평가 평가지 */
            return new Predicate[] { betweenTime(new BizSurvey(), requestObject.getStartDate(), requestObject.getEndDate()),
                    condition(((BizSurveyViewRequestVO) requestObject).getBizSurveyNo(), bizSurvey.bizSurveyNo::eq),
                    condition(((BizSurveyViewRequestVO) requestObject).getBizSurveyCtgr(), bizSurvey.bizSurveyCtgr::eq),
                    condition(((BizSurveyViewRequestVO) requestObject).getBizSurveyStts(), bizSurvey.bizSurveyStts::eq),
                    searchContainText(requestObject, ((BizSurveyViewRequestVO) requestObject).getContainTextType(), ((BizSurveyViewRequestVO) requestObject).getContainText()) };
        } else if(requestObject instanceof BizSurveyQitemViewRequestVO) { /** 상호평가 문항 */
            return new Predicate[] { betweenTime(new BizSurveyQitem(), requestObject.getStartDate(), requestObject.getEndDate()),
                    condition(((BizSurveyQitemViewRequestVO) requestObject).getBizSurveyQitemNo(), bizSurveyQitem.bizSurveyQitemNo::eq),
                    condition(((BizSurveyQitemViewRequestVO) requestObject).getBizSurveyQitemCategory(), bizSurveyQitem.bizSurveyQitemCategory::eq),
                    condition(((BizSurveyQitemViewRequestVO) requestObject).getBizSurveyQitemType(), bizSurveyQitem.bizSurveyQitemType::eq),
                    searchContainText(requestObject, ((BizSurveyQitemViewRequestVO) requestObject).getContainTextType(), ((BizSurveyQitemViewRequestVO) requestObject).getContainText()) };
        } else if(requestObject instanceof BizOrganizationRsltRptViewRequestVO) { /** 결과보고서 */
            return new Predicate[] { betweenTime(new BizOrganizationRsltRpt(), requestObject.getStartDate(), requestObject.getEndDate()),
                    condition(((BizOrganizationRsltRptViewRequestVO) requestObject).getBizOrgRsltRptNo(), bizOrganizationRsltRpt.bizOrgRsltRptNo::eq),
                    condition(((BizOrganizationRsltRptViewRequestVO) requestObject).getBizOrgRsltRptStts(), bizOrganizationRsltRpt.bizOrgRsltRptStts::eq),
                    condition(((BizOrganizationRsltRptViewRequestVO) requestObject).getBizPbancYr(), bizPbancMaster.bizPbancYr::eq),
                    condition(((BizOrganizationRsltRptViewRequestVO) requestObject).getBizPbancType(), bizPbancMaster.bizPbancType::eq),
                    searchContainText(requestObject, ((BizOrganizationRsltRptViewRequestVO) requestObject).getContainTextType(), ((BizOrganizationRsltRptViewRequestVO) requestObject).getContainText()) };
        } else if(requestObject instanceof BizSurveyAnsViewRequestVO) { /** 상호평가 답변 */
            return new Predicate[] { betweenTime(new BizSurveyAns(), requestObject.getStartDate(), requestObject.getEndDate()),
                    condition(((BizSurveyAnsViewRequestVO) requestObject).getBizSurveyNo(), bizSurveyAns.bizSurveyNo::eq),
                    searchContainText(requestObject, ((BizSurveyAnsViewRequestVO) requestObject).getContainTextType(), ((BizSurveyAnsViewRequestVO) requestObject).getContainText()) };
        } else if(requestObject instanceof BizInstructorClclnDdlnViewRequestVO) { /** 정산 마감일 */
            return new Predicate[] {
                    condition(((BizInstructorClclnDdlnViewRequestVO) requestObject).getBizInstrClclnDdlnNo(), bizInstructorClclnDdln.bizInstrClclnDdlnNo::eq),
                    searchContainText(requestObject, ((BizInstructorClclnDdlnViewRequestVO) requestObject).getContainTextType(), ((BizInstructorClclnDdlnViewRequestVO) requestObject).getContainText()) };
        } else if(requestObject instanceof BizInstructorDistCrtrAmtViewRequestVO) { /** 이동거리 기준단가 */
            return new Predicate[] {
                    condition(((BizInstructorDistCrtrAmtViewRequestVO) requestObject).getBizInstrDistCrtrAmtNo(), bizInstructorDistCrtrAmt.bizInstrDistCrtrAmtNo::eq),
                    condition(((BizInstructorDistCrtrAmtViewRequestVO) requestObject).getBizInstrDistCrtrAmtYr(), bizInstructorDistCrtrAmt.bizInstrDistCrtrAmtYr::eq),
                    searchContainText(requestObject, ((BizInstructorDistCrtrAmtViewRequestVO) requestObject).getContainTextType(), ((BizInstructorDistCrtrAmtViewRequestVO) requestObject).getContainText()) };
        } else if(requestObject instanceof BizInstructorDistCrtrAmtItemViewRequestVO) { /** 이동거리 기준단가 항목 */
            return new Predicate[] {
                    condition(((BizInstructorDistCrtrAmtItemViewRequestVO) requestObject).getBizInstrDistCrtrAmtItemNo(), bizInstructorDistCrtrAmtItem.bizInstrDistCrtrAmtItemNo::eq),
                    condition(((BizInstructorDistCrtrAmtItemViewRequestVO) requestObject).getBizInstrDistCrtrAmtNo(), bizInstructorDistCrtrAmtItem.bizInstrDistCrtrAmtNo::eq),
                    searchContainText(requestObject, ((BizInstructorDistCrtrAmtItemViewRequestVO) requestObject).getContainTextType(), ((BizInstructorDistCrtrAmtItemViewRequestVO) requestObject).getContainText()) };
        } else {
            return null;
        }
    }

    /** 조회 타입 별 포함 단어 조회 */
    private <T extends CSViewVOSupport> BooleanBuilder searchContainText(T requestObject, String containTextType, String containsText) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (hasText(containsText)) { /** 검색어가 존재할 경우 */
            if(requestObject instanceof BizPbancViewRequestVO) { /** 사업 공고 */
                if(containTextType.equals("1")) { /** 제목 + 내용 */
                    booleanBuilder.or(bizPbancMaster.bizPbancNm.contains(containsText));
                    booleanBuilder.or(bizPbancMaster.bizPbancCn.contains(containsText));
                } else if(containTextType.equals("2")) { /** 제목 */
                    booleanBuilder.or(bizPbancMaster.bizPbancNm.contains(containsText));
                } else if(containTextType.equals("3")) { /** 내용 */
                    booleanBuilder.or(bizPbancMaster.bizPbancCn.contains(containsText));
                }
            } else if(requestObject instanceof BizOrganizationAplyViewRequestVO) { /** 사업 공고 신청 */
                booleanBuilder.or(bizPbancMaster.bizPbancNm.contains(containsText));
                booleanBuilder.or(organizationInfo.organizationName.contains(containsText));
            } else if(requestObject instanceof BizAplyViewRequestVO) { /** 사업 공고 신청 - 언론인/기본형 */
                if (((BizAplyViewRequestVO) requestObject).getBizAplyType().equals("journalist")) {/** 언론인 */
                    booleanBuilder.or(bizPbancMaster.bizPbancNm.contains(containsText));
                    booleanBuilder.or(lmsUser.userName.contains(containsText));
                } else if (((BizAplyViewRequestVO) requestObject).getBizAplyType().equals("free")){/** 자유형 */
                    if(containTextType.equals("1")){
                        booleanBuilder.or(bizPbancMaster.bizPbancNm.contains(containsText));
                        booleanBuilder.or(organizationInfo.organizationName.contains(containsText));
                        booleanBuilder.or(bizAply.orgName.contains(containsText));
                        booleanBuilder.or(bizAply.bizAplyUserNm.contains(containsText));
                    }else if(containTextType.equals("2")){
                        booleanBuilder.or(bizPbancMaster.bizPbancNm.contains(containsText));
                    }else if(containTextType.equals("3")){
                        booleanBuilder.or(organizationInfo.organizationName.contains(containsText));
                        booleanBuilder.or(bizAply.orgName.contains(containsText));
                    }else if(containTextType.equals("4")){
                        booleanBuilder.or(bizAply.bizAplyUserNm.contains(containsText));
                    }
                }else {/** 기본형 */
                    booleanBuilder.or(bizPbancMaster.bizPbancNm.contains(containsText));
                    booleanBuilder.or(organizationInfo.organizationName.contains(containsText));
                }
            } else if(requestObject instanceof BizInstructorViewRequestVO) { /** 강사 모집 */
                if(containTextType.equals("1")) { /** 사업명 + 기관명 */
                    booleanBuilder.or(bizPbancMaster.bizPbancNm.contains(containsText));
                    booleanBuilder.or(organizationInfo.organizationName.contains(containsText));
                } else if(containTextType.equals("2")) { /** 사업명 */
                    booleanBuilder.or(bizPbancMaster.bizPbancNm.contains(containsText));
                } else if(containTextType.equals("3")) { /** 기관명 */
                    booleanBuilder.or(organizationInfo.organizationName.contains(containsText));
                }
            } else if(requestObject instanceof BizOrganizationAplyCustomViewRequestVO) { /** 강사 배정 */
                if(containTextType.equals("1")) { /** 사업명 */
                    booleanBuilder.or(bizPbancMaster.bizPbancNm.contains(containsText));
                }else if(containTextType.equals("2")) { /** 기관명 */
                    booleanBuilder.or(organizationInfo.organizationName.contains(containsText));
                }else if(containTextType.equals("3")){/** 강사명 */
                    booleanBuilder.or(bizInstructorAply.bizInstrAplyInstrNm.contains(containsText));
                }
            } else if(requestObject instanceof BizInstructorQuestionViewRequestVO) { /** 강사 문의 */
                booleanBuilder.or(bizPbancMaster.bizPbancNm.contains(containsText));
                booleanBuilder.or(organizationInfo.organizationName.contains(containsText));

            } else if(requestObject instanceof BizInstructorDistViewRequestVO) { /** 거리 증빙 */
                if(containTextType.equals("all")) { /** 기관명 + 강사명 + 사업명 */
                    booleanBuilder.or(bizInstructorAply.bizInstrAplyInstrNm.contains(containsText));
                    booleanBuilder.or(organizationInfo.organizationName.contains(containsText));
                    booleanBuilder.or(bizPbancMaster.bizPbancNm.contains(containsText));
                } else if(containTextType.equals("1")) { /** 기관명 */
                    booleanBuilder.or(organizationInfo.organizationName.contains(containsText));
                }else if(containTextType.equals("2")) { /** 강사명 */
                    booleanBuilder.or(bizInstructorAply.bizInstrAplyInstrNm.contains(containsText));
                } else if(containTextType.equals("3")) { /** 사업명 */
                    booleanBuilder.or(bizPbancMaster.bizPbancNm.contains(containsText));
                }

            } else if(requestObject instanceof BizSurveyViewRequestVO) { /** 상호평가 평가지 */
                if(containTextType.equals("1")) { /** 제목 + 내용 */
                    booleanBuilder.or(bizSurvey.bizSurveyNm.contains(containsText));
                    booleanBuilder.or(bizSurvey.bizSurveyCn.contains(containsText));
                } else if(containTextType.equals("2")) { /** 제목 */
                    booleanBuilder.or(bizSurvey.bizSurveyNm.contains(containsText));
                } else if(containTextType.equals("3")) { /** 내용 */
                    booleanBuilder.or(bizSurvey.bizSurveyCn.contains(containsText));
                }
            } else if(requestObject instanceof BizSurveyQitemViewRequestVO) { /** 상호평가 문항 */
                if(containTextType.equals("1")) { /** 제목 + 내용 */
                    booleanBuilder.or(bizSurveyQitem.bizSurveyQitemName.contains(containsText));
                    booleanBuilder.or(bizSurveyQitem.bizSurveyQitemContent.contains(containsText));
                } else if(containTextType.equals("2")) { /** 제목 */
                    booleanBuilder.or(bizSurveyQitem.bizSurveyQitemName.contains(containsText));
                } else if(containTextType.equals("3")) { /** 내용 */
                    booleanBuilder.or(bizSurveyQitem.bizSurveyQitemContent.contains(containsText));
                }
            } else if(requestObject instanceof BizInstructorIdentifyViewRequestVO
            || requestObject instanceof BizInstructorIdentifyDtlExcelRequestVO) { /** 강의확인서 */
                if(containTextType.equals("0")) { /** 강사명 */
                    booleanBuilder.or(lmsUser.userName.contains(containsText));
                    booleanBuilder.or(bizInstructorIdentify.registUserId.contains(containsText));
                    booleanBuilder.or(organizationInfo.organizationName.contains(containsText));
                } else if(containTextType.equals("1")) { /** 강사명 */
                    booleanBuilder.or(lmsUser.userName.contains(containsText));
                } else if(containTextType.equals("2")) { /** 강사아이디 */
                    booleanBuilder.or(bizInstructorIdentify.registUserId.contains(containsText));
                } else if(containTextType.equals("3")) { /** 수행기관 */
                    booleanBuilder.or(organizationInfo.organizationName.contains(containsText));
                }
            }else if(requestObject instanceof BizInstructorIdentifyCalculateViewRequestVO) { /** 강의확인서 */
                if(containTextType.equals("0")) { /** 전체 */
                    booleanBuilder.or(bizInstructorAply.bizInstrAplyInstrNm.contains(containsText));
                    booleanBuilder.or(bizInstructorAply.bizInstrAplyInstrId.contains(containsText));
                    booleanBuilder.or(organizationInfo.organizationName.contains(containsText));
                } else if(containTextType.equals("1")) { /** 강사명 */
                    booleanBuilder.or(bizInstructorAply.bizInstrAplyInstrNm.contains(containsText));
                } else if(containTextType.equals("2")) { /** 강사아이디 */
                    booleanBuilder.or(bizInstructorAply.bizInstrAplyInstrId.contains(containsText));
                } else if(containTextType.equals("3")) { /** 수행기관 */
                    booleanBuilder.or(organizationInfo.organizationName.contains(containsText));
                }
            } else if(requestObject instanceof BizOrganizationRsltRptViewRequestVO) { /** 결과보고서 */
                if(containTextType.equals("1")) { /** 사업명 */
                    booleanBuilder.or(bizPbancMaster.bizPbancNm.contains(containsText));
                } else if(containTextType.equals("2")) { /** 수행기관 */
                    booleanBuilder.or(organizationInfo.organizationName.contains(containsText));
                }
            }else if(requestObject instanceof BizInstructorIdentifyManageViewRequestVO) { /** 강의확인서 강의활동확인서 */
                if(containTextType.equals("1")) { /** 강사명 */
                    booleanBuilder.or(bizInstructorAply.bizInstrAplyInstrNm.contains(containsText));
                } else if(containTextType.equals("2")) { /** 사업명 */
                    booleanBuilder.or(bizPbancMaster.bizPbancNm.contains(containsText));
                } else if(containTextType.equals("3")) { /** 수행기관 */
                    booleanBuilder.or(organizationInfo.organizationName.contains(containsText));
                }
            }
        }
        return booleanBuilder;
    }

    /** 시간 대 검색 */
    private <T extends CSEntitySupport> BooleanExpression betweenTime(T value, String startDate, String endDate) {
        String startDateTime = Optional.ofNullable(startDate)
                .map(date -> new StringBuilder(date).append(" 00:00:00").toString())
                .orElse("");
        String endDateTime = Optional.ofNullable(endDate)
                .map(date -> new StringBuilder(date).append(" 23:59:59").toString())
                .orElse("");

        if((!StringUtils.isEmpty(startDateTime) && StringUtils.isEmpty(endDateTime))
                || (StringUtils.isEmpty(startDateTime) && !StringUtils.isEmpty(endDateTime))) {
            throw new KPFException(KPF_RESULT.ERROR9001, "조회 시작 일자 & 조회 종료 일자는 한가지만 존재할 수 없습니다.");
        } else if(!StringUtils.isEmpty(startDateTime) && !StringUtils.isEmpty(endDateTime)) {
            if(value instanceof BizPbancMaster) { /** 사업 공고 */
                return bizPbancMaster.createDateTime.between(startDateTime, endDateTime);
            } else if(value instanceof BizInstructor) { /** 강사 모집 */
                return bizInstructor.createDateTime.between(startDateTime, endDateTime);
            } else if(value instanceof BizSurvey) { /** 상호평가 평가지 */
                return bizSurvey.createDateTime.between(startDateTime, endDateTime);
            } else if(value instanceof BizOrganizationAply) { /** 사업 공고 신청 */
                return bizOrganizationAply.createDateTime.between(startDateTime, endDateTime);
            } else if(value instanceof BizInstructorAply) { /** 강사 신청 */
                return bizInstructorAply.createDateTime.between(startDateTime, endDateTime);
            } else if(value instanceof BizInstructorAsgnm) { /** 강사 모집 공고 배정 */
                return bizInstructorAsgnm.createDateTime.between(startDateTime, endDateTime);
            } else if(value instanceof BizInstructorIdentify) { /** 강의확인서 */
                return bizInstructorIdentify.createDateTime.between(startDateTime, endDateTime);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /** 시간 대 검색 */
    private <T extends CSEntitySupport> BooleanExpression checkDate(T value, String year, String month) {
        /** date = 2023-01, 2023-02, 2023-12 */
        /** 2023-12라면 */
        if (year != null && year.length() > 0 && month != null && month.length() > 0) {
            if (month.equals("12")) {
                DateExpression<Date> expressionCheck = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", year + "-" + month, "%Y-%m");
                DateExpression<Date> expressionCheck11 = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", year + "-" + "11", "%Y-%m");
                if (value instanceof BizInstructorIdentify) {
                    DateExpression<Date> expression = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", bizInstructorIdentify.bizInstrIdntyAprvDt, "%Y-%m");
                    return expression.eq(expressionCheck).or(expression.eq(expressionCheck11));
                }
            } else {
                DateExpression<Date> expressionCheck = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", year + "-" + month, "%Y-%m");
                if (value instanceof BizInstructorIdentify) {
                    DateExpression<Date> expression = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", bizInstructorIdentify.bizInstrIdntyAprvDt, "%Y-%m");
                    return expression.eq(expressionCheck);
                }
            }
        }
        return null;
    }

    /** 날짜 비교 */
    private <T extends CSEntitySupport> BooleanExpression checkPeriod(T value, String type) {
        LocalDate ld = LocalDate.now();
        Date d = java.sql.Date.valueOf(ld);
        DateExpression<Date> expressionNow = Expressions.dateTemplate(Date.class, "{0}", d);
        if (value instanceof BizOrganizationAply) {
            if (type.equals("dtl")) {/** expression <= expressionNow */
                DateExpression<Date> expression = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", bizOrganizationAplyDtl.bizOrgAplyLsnDtlYmd, "%Y-%m");
                return expression.loe(expressionNow);

            } else {
                return null;
            }
        } else if (value instanceof BizInstructorIdentify) {
            Integer month = Integer.valueOf(type);
            BizInstructorClclnDdln clclnDdln = jpaQueryFactory.selectFrom(bizInstructorClclnDdln)
                    .where(bizInstructorClclnDdln.bizInstrClclnDdlnMm.eq(month),
                            bizInstructorClclnDdln.bizInstrClclnDdlnYr.eq(ld.getYear()))
                    .fetchOne();
            if(clclnDdln!=null){
                Date dd = java.sql.Date.valueOf(clclnDdln.getBizInstrClclnDdlnValue());
                DateExpression<Date> expressionClcl = Expressions.dateTemplate(Date.class, "{0}", dd);
                DateExpression<Date> expression = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", bizInstructorIdentify.createDateTime, "%Y-%m-%d");
                return expression.loe(expressionClcl);
            }else{
                /** 기존코드 겸 정산마감일 없을 때 */
                NumberExpression<Integer> expressionMonth = Expressions.numberTemplate(Integer.class, Integer.valueOf(type).toString());
                NumberExpression<Integer> expression = bizInstructorIdentify.bizInstrIdntyYm.substring(4,6).castToNum(Integer.class);
                return expression.loe(expressionMonth);
            }
        } else if(value instanceof  BizInstructorClclnDdln){
            NumberExpression<Integer> expressionMonth = Expressions.numberTemplate(Integer.class, Integer.valueOf(type).toString());
            NumberExpression<Integer> expression = bizInstructorIdentify.bizInstrIdntyYm.substring(4,6).castToNum(Integer.class);
            return expression.loe(expressionMonth);
        } else return null;
    }

    /** 강의확인서 마감일 날짜비교 */
    private <T extends CSViewVOSupport> BooleanBuilder searchContainTextDate(String type) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        try{
            LocalDate ld = LocalDate.now();
            Integer month = Integer.valueOf(type);
            BizInstructorClclnDdln clclnDdln = jpaQueryFactory.selectFrom(bizInstructorClclnDdln)
                    .where(bizInstructorClclnDdln.bizInstrClclnDdlnMm.eq(month),
                            bizInstructorClclnDdln.bizInstrClclnDdlnYr.eq(ld.getYear()))
                    .fetchOne();
            if(clclnDdln != null){

                Date dd = java.sql.Date.valueOf(clclnDdln.getBizInstrClclnDdlnValue());

                DateExpression<Date> expressionClcl = Expressions.dateTemplate(Date.class, "{0}", dd);
                DateExpression<Date> expression = Expressions.dateTemplate(Date.class, "STR_TO_DATE({0},{1})", bizInstructorIdentify.bizInstrIdntyAprvDt, "%Y-%m-%d");
                /** 마감일 전에 작성 → 기관 승인 */
                booleanBuilder.or(expression.lt(expressionClcl));

                NumberExpression<Integer> expressionHour = Expressions.numberTemplate(Integer.class, clclnDdln.getBizInstrClclnDdlnTm().substring(0,2));
                NumberExpression<Integer> expressionH = bizInstructorIdentify.bizInstrIdntyAprvDt.substring(11,13).castToNum(Integer.class);
                NumberExpression<Integer> expressionMin= Expressions.numberTemplate(Integer.class, clclnDdln.getBizInstrClclnDdlnTm().substring(3,5));
                NumberExpression<Integer> expressionM = bizInstructorIdentify.bizInstrIdntyAprvDt.substring(14,16).castToNum(Integer.class);

                /** 마감일인데 마감시간 전에 작성 → 기관 승인 */
                booleanBuilder.or(expression.eq(expressionClcl).and(expressionH.lt(expressionHour)));
                /** 마감일인데 마감분 전에 혹은 동시에 작성 → 기관 승인 */
                booleanBuilder.or(expression.eq(expressionClcl).and(expressionH.eq(expressionHour)).and(expressionM.loe(expressionMin)));

            }else{
                return null;
            }
        }catch (Exception E){

        }
        return booleanBuilder;
    }
    @Override
    public String generateCode(String prefixCode) {
        if (prefixCode.equals("PAC")) {
            return jpaQueryFactory.selectFrom(bizPbancMaster)
                    .where(bizPbancMaster.bizPbancNo.like(prefixCode+"%"))
                    .orderBy(bizPbancMaster.bizPbancNo.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getBizPbancNo().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        }else if (prefixCode.equals("ISR")) {
            return jpaQueryFactory.selectFrom(bizInstructor)
                    .where(bizInstructor.bizInstrNo.like(prefixCode+"%"))
                    .orderBy(bizInstructor.bizInstrNo.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getBizInstrNo().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        }else if (prefixCode.equals("BOA")) {
            return jpaQueryFactory.selectFrom(bizOrganizationAply)
                    .where(bizOrganizationAply.bizOrgAplyNo.like(prefixCode+"%"))
                    .orderBy(bizOrganizationAply.bizOrgAplyNo.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getBizOrgAplyNo().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        }else if (prefixCode.equals("BOAD")) {
            return jpaQueryFactory.selectFrom(bizOrganizationAplyDtl)
                    .where(bizOrganizationAplyDtl.bizOrgAplyDtlNo.like(prefixCode+"%"))
                    .orderBy(bizOrganizationAplyDtl.bizOrgAplyDtlNo.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getBizOrgAplyDtlNo().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        }else if (prefixCode.equals("BORR")) {
            return jpaQueryFactory.selectFrom(bizOrganizationRsltRpt)
                    .where(bizOrganizationRsltRpt.bizOrgRsltRptNo.like(prefixCode+"%"))
                    .orderBy(bizOrganizationRsltRpt.bizOrgRsltRptNo.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getBizOrgRsltRptNo().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        }else if (prefixCode.equals("BSQ")) {
            return jpaQueryFactory.selectFrom(bizSurvey)
                    .where(bizSurvey.bizSurveyNo.like(prefixCode+"%"))
                    .orderBy(bizSurvey.bizSurveyNo.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getBizSurveyNo().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        }else if (prefixCode.equals("BSI")) {
            return jpaQueryFactory.selectFrom(bizSurveyQitem)
                    .where(bizSurveyQitem.bizSurveyQitemNo.like(prefixCode+"%"))
                    .orderBy(bizSurveyQitem.bizSurveyQitemNo.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getBizSurveyQitemNo().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        }else if (prefixCode.equals("BSII")) {
            return jpaQueryFactory.selectFrom(bizSurveyQitemItem)
                    .where(bizSurveyQitemItem.bizSurveyQitemItemNo.like(prefixCode+"%"))
                    .orderBy(bizSurveyQitemItem.bizSurveyQitemItemNo.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getBizSurveyQitemItemNo().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        }else if (prefixCode.equals("BSA")) {
            return jpaQueryFactory.selectFrom(bizSurveyAns)
                    .where(bizSurveyAns.bizSurveyAnsNo.like(prefixCode+"%"))
                    .orderBy(bizSurveyAns.bizSurveyAnsNo.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getBizSurveyNo().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        }else if (prefixCode.equals("BIA")) {
            return jpaQueryFactory.selectFrom(bizInstructorAply)
                    .where(bizInstructorAply.bizInstrAplyNo.like(prefixCode+"%"))
                    .orderBy(bizInstructorAply.bizInstrAplyNo.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getBizInstrAplyNo().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        }else if (prefixCode.equals("BIG")) {
            return jpaQueryFactory.selectFrom(bizInstructorAsgnm)
                    .where(bizInstructorAsgnm.bizInstrAsgnmNo.like(prefixCode+"%"))
                    .orderBy(bizInstructorAsgnm.bizInstrAsgnmNo.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getBizInstrAsgnmNo().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        }else if (prefixCode.equals("BII")) {
            return jpaQueryFactory.selectFrom(bizInstructorIdentify)
                    .where(bizInstructorIdentify.bizInstrIdntyNo.like(prefixCode+"%"))
                    .orderBy(bizInstructorIdentify.bizInstrIdntyNo.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getBizInstrIdntyNo().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        }else if (prefixCode.equals("BIID")) {
            return jpaQueryFactory.selectFrom(bizInstructorIdentifyDtl)
                    .where(bizInstructorIdentifyDtl.bizInstrIdntyDtlNo.like(prefixCode+"%"))
                    .orderBy(bizInstructorIdentifyDtl.bizInstrIdntyDtlNo.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getBizInstrIdntyDtlNo().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        }else if (prefixCode.equals("BIQ")) {
            return jpaQueryFactory.selectFrom(bizInstructorQuestion)
                    .where(bizInstructorQuestion.bizInstrQstnNo.like(prefixCode+"%"))
                    .orderBy(bizInstructorQuestion.bizInstrQstnNo.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getBizInstrQstnNo().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        }else if (prefixCode.equals("BIQA")) {
            return jpaQueryFactory.selectFrom(bizInstructorQuestionAnswer)
                    .where(bizInstructorQuestionAnswer.bizInstrQstnAnsNo.like(prefixCode+"%"))
                    .orderBy(bizInstructorQuestionAnswer.bizInstrQstnAnsNo.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getBizInstrQstnAnsNo().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        }else if (prefixCode.equals("BICD")) {
            return jpaQueryFactory.selectFrom(bizInstructorClclnDdln)
                    .where(bizInstructorClclnDdln.bizInstrClclnDdlnNo.like(prefixCode+"%"))
                    .orderBy(bizInstructorClclnDdln.bizInstrClclnDdlnNo.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getBizInstrClclnDdlnNo().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        }else if (prefixCode.equals("P0T")) {
            return jpaQueryFactory.selectFrom(bizPbancTmpl0)
                    .where(bizPbancTmpl0.bizPbancTmpl0No.like(prefixCode+"%"))
                    .orderBy(bizPbancTmpl0.bizPbancTmpl0No.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getBizPbancTmpl0No().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        }else if (prefixCode.equals("P0TI")) {
            return jpaQueryFactory.selectFrom(bizPbancTmpl0Item)
                    .where(bizPbancTmpl0Item.bizPbancTmpl0ItemNo.like(prefixCode+"%"))
                    .orderBy(bizPbancTmpl0Item.bizPbancTmpl0ItemNo.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getBizPbancTmpl0ItemNo().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        }else if (prefixCode.equals("P1T")) {
            return jpaQueryFactory.selectFrom(bizPbancTmpl1)
                    .where(bizPbancTmpl1.bizPbancTmpl1No.like(prefixCode+"%"))
                    .orderBy(bizPbancTmpl1.bizPbancTmpl1No.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getBizPbancTmpl1No().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        }else if (prefixCode.equals("P2T")) {
            return jpaQueryFactory.selectFrom(bizPbancTmpl2)
                    .where(bizPbancTmpl2.bizPbancTmpl2No.like(prefixCode+"%"))
                    .orderBy(bizPbancTmpl2.bizPbancTmpl2No.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getBizPbancTmpl2No().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        }else if (prefixCode.equals("P3T")) {
            return jpaQueryFactory.selectFrom(bizPbancTmpl3)
                    .where(bizPbancTmpl3.bizPbancTmpl3No.like(prefixCode+"%"))
                    .orderBy(bizPbancTmpl3.bizPbancTmpl3No.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getBizPbancTmpl3No().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        }else if (prefixCode.equals("P4T")) {
            return jpaQueryFactory.selectFrom(bizPbancTmpl4)
                    .where(bizPbancTmpl4.bizPbancTmpl4No.like(prefixCode+"%"))
                    .orderBy(bizPbancTmpl4.bizPbancTmpl4No.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getBizPbancTmpl4No().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        }else if (prefixCode.equals("BID")) {
            return jpaQueryFactory.selectFrom(bizInstructorDist)
                    .where(bizInstructorDist.bizInstrDistNo.like(prefixCode+"%"))
                    .orderBy(bizInstructorDist.bizInstrDistNo.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getBizInstrDistNo().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        }else if (prefixCode.equals("BIDA")) {
            return jpaQueryFactory.selectFrom(bizInstructorDistCrtrAmt)
                    .where(bizInstructorDistCrtrAmt.bizInstrDistCrtrAmtNo.like(prefixCode+"%"))
                    .orderBy(bizInstructorDistCrtrAmt.bizInstrDistCrtrAmtNo.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getBizInstrDistCrtrAmtNo().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        }else if (prefixCode.equals("BIAI")) {
            return jpaQueryFactory.selectFrom(bizInstructorDistCrtrAmtItem)
                    .where(bizInstructorDistCrtrAmtItem.bizInstrDistCrtrAmtItemNo.like(prefixCode+"%"))
                    .orderBy(bizInstructorDistCrtrAmtItem.bizInstrDistCrtrAmtItemNo.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getBizInstrDistCrtrAmtItemNo().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        }else if (prefixCode.equals("BEH")) {
            return jpaQueryFactory.selectFrom(bizEditHist)
                    .where(bizEditHist.bizEditHistNo.like(prefixCode+"%"))
                    .orderBy(bizEditHist.bizEditHistNo.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getBizEditHistNo().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        }else if (prefixCode.equals("PACR")) {
            return jpaQueryFactory.selectFrom(bizPbancResult)
                    .where(bizPbancResult.bizPbancRsltNo.like(prefixCode+"%"))
                    .orderBy(bizPbancResult.bizPbancRsltNo.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getBizPbancRsltNo().replace(prefixCode, "")) + 1), 7, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("0000000").toString());
        }else{
            return null;
        }
    }

    @Override
    public Integer generatePbancAutoIncrease(Integer bizPbancType, Integer bizPbancYr) {
        Optional<BizPbancMaster> rnd = jpaQueryFactory.selectFrom(bizPbancMaster)
                .where(bizPbancMaster.bizPbancType.eq(bizPbancType), bizPbancMaster.bizPbancYr.eq(bizPbancYr))
                .orderBy(bizPbancMaster.bizPbancRnd.desc())
                .fetch().stream().findFirst();

        if (!rnd.isEmpty()){
            return rnd.get().getBizPbancRnd() + 1;
        } else {
            return 1;
        }
    }

    @Override
    public Long generateInstrPbancAutoIncrease() {
        Optional<BizInstructorPbanc> rnd = jpaQueryFactory.selectFrom(bizInstructorPbanc)
                .orderBy(bizInstructorPbanc.sequenceNo.desc())
                .fetch().stream().findFirst();

        if (!rnd.isEmpty()){
            return rnd.get().getSequenceNo() + 1;
        } else {
            return Long.valueOf(1);
        }
    }
}
