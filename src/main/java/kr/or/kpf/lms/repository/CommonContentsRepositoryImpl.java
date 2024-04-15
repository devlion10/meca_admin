package kr.or.kpf.lms.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.or.kpf.lms.biz.contents.chapter.vo.request.ChapterViewRequestVO;
import kr.or.kpf.lms.biz.contents.contents.vo.request.ContentsViewRequestVO;
import kr.or.kpf.lms.biz.contents.contents.vo.response.ContentsExcelVO;
import kr.or.kpf.lms.biz.contents.evaluate.vo.request.EvaluateViewRequestVO;
import kr.or.kpf.lms.biz.contents.evaluate.vo.response.EvaluateExcelVO;
import kr.or.kpf.lms.biz.contents.question.vo.request.EvaluateQuestionViewRequestVO;
import kr.or.kpf.lms.biz.contents.question.vo.response.EvaluateQuestionExcelVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSRepositorySupport;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.ResponseSummary;
import kr.or.kpf.lms.repository.entity.contents.ContentsChapter;
import kr.or.kpf.lms.repository.entity.contents.EvaluateQuestion;
import kr.or.kpf.lms.repository.entity.contents.SectionMaster;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kr.or.kpf.lms.repository.entity.QBizInstructorDist.bizInstructorDist;
import static kr.or.kpf.lms.repository.entity.QBizOrganizationAply.bizOrganizationAply;
import static kr.or.kpf.lms.repository.entity.QBizPbancMaster.bizPbancMaster;
import static kr.or.kpf.lms.repository.entity.contents.QChapterMaster.chapterMaster;
import static kr.or.kpf.lms.repository.entity.contents.QContentsChapter.contentsChapter;
import static kr.or.kpf.lms.repository.entity.contents.QContentsMaster.contentsMaster;
import static kr.or.kpf.lms.repository.entity.contents.QEvaluateMaster.evaluateMaster;
import static kr.or.kpf.lms.repository.entity.contents.QEvaluateQuestionItem.evaluateQuestionItem;
import static kr.or.kpf.lms.repository.entity.contents.QEvaluateQuestionMaster.evaluateQuestionMaster;
import static kr.or.kpf.lms.repository.entity.contents.QSectionMaster.sectionMaster;
import static kr.or.kpf.lms.repository.entity.user.QOrganizationInfo.organizationInfo;
import static org.springframework.util.StringUtils.hasText;

/**
 * 콘텐츠 관리 공통 Repository 구현체
 */
@Repository
public class CommonContentsRepositoryImpl extends CSRepositorySupport implements CommonContentsRepository {

    private final JPAQueryFactory jpaQueryFactory;
    public CommonContentsRepositoryImpl(JPAQueryFactory jpaQueryFactory){ this.jpaQueryFactory = jpaQueryFactory; }

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
        if(requestObject instanceof EvaluateViewRequestVO) { /** 강의 평가 관리 */
            return jpaQueryFactory.select(evaluateMaster.count())
                    .from(evaluateMaster)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof EvaluateQuestionViewRequestVO) { /** 강의 평가 문항 관리 */
            return jpaQueryFactory.select(evaluateQuestionMaster.count())
                    .from(evaluateQuestionMaster)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof ContentsViewRequestVO) { /** 콘텐츠 관리 */
            return jpaQueryFactory.select(contentsMaster.count())
                    .from(contentsMaster)
                    .where(getQuery(requestObject))
                    .fetchOne();
        } else if(requestObject instanceof ChapterViewRequestVO) { /** 차시(챕터) 관리 */
            return jpaQueryFactory.select(contentsChapter.count()).distinct()
                    .from(contentsChapter)
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
        if(requestObject instanceof EvaluateViewRequestVO) { /** 강의 평가 관리 */
            return jpaQueryFactory.selectFrom(evaluateMaster).distinct()
                    .where(getQuery(requestObject))
                    .orderBy(evaluateMaster.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch().stream()
                    .peek(data -> data.getEvaluateQuestionList().sort(Comparator.comparing(EvaluateQuestion::getSortNo)))
                    .collect(Collectors.toList());
        } else if(requestObject instanceof EvaluateQuestionViewRequestVO) { /** 강의 평가 문항 관리 */
            return jpaQueryFactory.selectFrom(evaluateQuestionMaster).distinct()
                    .where(getQuery(requestObject))
                    .orderBy(evaluateQuestionMaster.createDateTime.desc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        } else if(requestObject instanceof ContentsViewRequestVO) { /** 콘텐츠 관리 */
            return jpaQueryFactory.selectFrom(contentsMaster).distinct()
                    .where(getQuery(requestObject))
                    .orderBy(contentsMaster.createDateTime.desc(), contentsMaster.contentsChapterList.any().sortNo.asc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch().stream()
                    .peek(data -> {
                        data.getContentsChapterList().stream().forEach(subData -> subData.getChapterMaster().getSectionMasterList().sort(Comparator.comparing(SectionMaster::getSortNo)));
                        data.getContentsChapterList().sort(Comparator.comparing(ContentsChapter::getSortNo));
                    })
                    .collect(Collectors.toList());
        } else if(requestObject instanceof ChapterViewRequestVO) { /** 차시(챕터) 관리 */
            return jpaQueryFactory.selectFrom(contentsChapter).distinct()
                    .where(getQuery(requestObject))
                    .orderBy(contentsChapter.sortNo.asc())
                    .offset(requestObject.getPageable().getOffset())
                    .limit(requestObject.getPageable().getPageSize())
                    .fetch();
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * excel 다운로드
     *
     */
    private <T extends CSViewVOSupport> List<?> getEntityListExcel(T requestObject) {
        if(requestObject instanceof EvaluateViewRequestVO) { /** 강의 평가 관리 */
            List<EvaluateExcelVO> dtos =  jpaQueryFactory.select(Projections.fields(
                            EvaluateExcelVO.class,
                            evaluateMaster.evaluateTitle,
                            evaluateMaster.evaluateType,
                            evaluateMaster.evaluateContents,
                            evaluateMaster.isUsable,
                            evaluateMaster.registUserId,
                            evaluateMaster.createDateTime,
                            evaluateMaster.modifyUserId,
                            evaluateMaster.updateDateTime
                            ))
                    .from(evaluateMaster)
                    .where(getQuery(requestObject))
                    .orderBy(evaluateMaster.createDateTime.asc())
                    .fetch();
            return dtos;
        } else if(requestObject instanceof EvaluateQuestionViewRequestVO) { /** 강의 평가 문항 관리 */
            List<EvaluateQuestionExcelVO> dtos =  jpaQueryFactory.select(Projections.fields(
                            EvaluateQuestionExcelVO.class,
                            evaluateQuestionMaster.questionTitle,
                            evaluateQuestionMaster.questionCategory,
                            evaluateQuestionMaster.questionType,
                            evaluateQuestionMaster.questionContents,
                            evaluateQuestionMaster.registUserId,
                            evaluateQuestionMaster.createDateTime,
                            evaluateQuestionMaster.modifyUserId,
                            evaluateQuestionMaster.updateDateTime
                    ))
                    .from(evaluateQuestionMaster)
                    .where(getQuery(requestObject))
                    .orderBy(evaluateQuestionMaster.createDateTime.asc())
                    .fetch();
            return dtos;
        } else if(requestObject instanceof ContentsViewRequestVO) { /** 콘텐츠 관리 */
            List<ContentsExcelVO> dtos =  jpaQueryFactory.select(Projections.fields(
                            ContentsExcelVO.class,
                            contentsMaster.contentsName,
                            contentsMaster.categoryCode,
                            contentsMaster.isUsable,
                            organizationInfo.organizationName,
                            contentsMaster.educationPerMinute,
                            contentsMaster.createDateTime,
                            contentsMaster.memo
                    ))
                    .from(contentsMaster)
                    .where(getQuery(requestObject))
                    .leftJoin(organizationInfo).on(organizationInfo.organizationCode.eq(contentsMaster.organizationCode))
                    .orderBy(contentsMaster.createDateTime.asc())
                    .fetch();
            return dtos;
        } else {
            return new ArrayList<>();
        }
    }
    /**
     * where 절
     */
    private <T extends CSViewVOSupport> Predicate[] getQuery(T requestObject) {
        if(requestObject instanceof EvaluateViewRequestVO) { /** 강의 평가 관리 */
            return new Predicate[] { condition(((EvaluateViewRequestVO) requestObject).getEvaluateTitle(), evaluateMaster.evaluateTitle::contains),
                    condition(((EvaluateViewRequestVO) requestObject).getEvaluateType(), evaluateMaster.evaluateType::eq),
                    condition(((EvaluateViewRequestVO) requestObject).getEvaluateSerialNo(), evaluateMaster.evaluateSerialNo::eq),
                    condition(((EvaluateViewRequestVO) requestObject).getIsUsable(), evaluateMaster.isUsable::eq),
                    betweenTime(requestObject) };
        } else if(requestObject instanceof EvaluateQuestionViewRequestVO) { /** 강의 평가 문항 관리 */
            return new Predicate[] { betweenTime(requestObject),
                    condition(((EvaluateQuestionViewRequestVO) requestObject).getQuestionCategory(), evaluateQuestionMaster.questionCategory::eq),
                    condition(((EvaluateQuestionViewRequestVO) requestObject).getQuestionType(), evaluateQuestionMaster.questionType::eq),
                    condition(((EvaluateQuestionViewRequestVO) requestObject).getQuestionSerialNo(), evaluateQuestionMaster.questionSerialNo::eq),
                    condition(((EvaluateQuestionViewRequestVO) requestObject).getQuestionTitle(), evaluateQuestionMaster.questionTitle::contains)};
        } else if(requestObject instanceof ContentsViewRequestVO) { /** 콘텐츠 관리 */
            return new Predicate[] { betweenTime(requestObject),
                    condition(((ContentsViewRequestVO) requestObject).getContentsCode(), contentsMaster.contentsCode::eq),
                    condition(((ContentsViewRequestVO) requestObject).getContentsName(), contentsMaster.contentsName::contains),
                    condition(((ContentsViewRequestVO) requestObject).getIsUsable(), contentsMaster.isUsable::eq),
                    condition(((ContentsViewRequestVO) requestObject).getCategoryCode(), contentsMaster.categoryCode::eq)};
        } else if(requestObject instanceof ChapterViewRequestVO) { /** 차시(챕터) 관리 */
            return new Predicate[] { betweenTime(requestObject),
                    condition(((ChapterViewRequestVO) requestObject).getContentsCode(), contentsChapter.contentsCode::eq),
                    condition(((ChapterViewRequestVO) requestObject).getChapterCode(), contentsChapter.chapterCode::eq)};
        } else {
            return null;
        }
    }

    /** 조회 타입 별 포함 단어 조회 */
    private <T extends CSViewVOSupport> BooleanBuilder searchContainText(T requestObject, String containTextType, String containsText) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (hasText(containsText)) { /** 검색어가 존재할 경우 */
            if(requestObject instanceof EvaluateViewRequestVO) { /** 강의 평가 관리 */

            } else if(requestObject instanceof ContentsViewRequestVO) { /** 콘텐츠 관리 */

            } else if(requestObject instanceof ChapterViewRequestVO) { /** 차시(챕터) 관리 */

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
            if(requestObject instanceof EvaluateViewRequestVO) { /** 강의 평가 관리 */
                return evaluateMaster.createDateTime.between(startDateTime, endDateTime);
            } else if(requestObject instanceof ContentsViewRequestVO) { /** 콘텐츠 관리 */
                return contentsMaster.createDateTime.between(startDateTime, endDateTime);
            } else if(requestObject instanceof ChapterViewRequestVO) { /** 차시(챕터) 관리 */
                return contentsChapter.createDateTime.between(startDateTime, endDateTime);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 콘텐츠 코드 생성
     */
    @Override
    public String generateContentsCode(String prefixCode) {
        return jpaQueryFactory.selectFrom(contentsMaster)
                .where(contentsMaster.contentsCode.like(prefixCode+"%"))
                .orderBy(contentsMaster.contentsCode.desc())
                .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                        .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getContentsCode().replace(prefixCode, "")) + 1), 7, "0"))
                        .toString())
                .orElse(new StringBuilder(prefixCode).append("0000000").toString());
    }

    /**
     * 챕터 코드 생성
     */
    @Override
    public String generateChapterCode(String prefixCode) {
        return jpaQueryFactory.selectFrom(chapterMaster)
                .where(chapterMaster.chapterCode.like(prefixCode+"%"))
                .orderBy(chapterMaster.chapterCode.desc())
                .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                        .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getChapterCode().replace(prefixCode, "")) + 1), 7, "0"))
                        .toString())
                .orElse(new StringBuilder(prefixCode).append("0000000").toString());
    }

    /**
     * 강의 평가 일련 번호 생성
     */
    @Override
    public String generateEvaluateSerialNo(String prefixCode) {
        return jpaQueryFactory.selectFrom(evaluateMaster)
                .where(evaluateMaster.evaluateSerialNo.like(prefixCode+"%"))
                .orderBy(evaluateMaster.evaluateSerialNo.desc())
                .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                        .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getEvaluateSerialNo().replace(prefixCode, "")) + 1), 7, "0"))
                        .toString())
                .orElse(new StringBuilder(prefixCode).append("0000000").toString());
    }

    /**
     * 강의 평가 질문 일련 번호 생성
     */
    @Override
    public String generateQuestionSerialNo(String prefixCode) {
        return jpaQueryFactory.selectFrom(evaluateQuestionMaster)
                .where(evaluateQuestionMaster.questionSerialNo.like(prefixCode+"%"))
                .orderBy(evaluateQuestionMaster.questionSerialNo.desc())
                .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                        .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getQuestionSerialNo().replace(prefixCode, "")) + 1), 7, "0"))
                        .toString())
                .orElse(new StringBuilder(prefixCode).append("0000000").toString());
    }

    /**
     * 강의 평가 질문 문항 일련 번호 생성
     */
    @Override
    public String generateQuestionItemSerialNo(String prefixCode) {
        return jpaQueryFactory.selectFrom(evaluateQuestionItem)
                .where(evaluateQuestionItem.questionItemSerialNo.like(prefixCode+"%"))
                .orderBy(evaluateQuestionItem.questionItemSerialNo.desc())
                .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                        .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getQuestionItemSerialNo().replace(prefixCode, "")) + 1), 7, "0"))
                        .toString())
                .orElse(new StringBuilder(prefixCode).append("0000000").toString());
    }

    /**
     * 섹션(절) 일련 번호 생성
     */
    @Override
    public String generateSectionCode(String prefixCode) {
        return jpaQueryFactory.selectFrom(sectionMaster)
                .where(sectionMaster.sectionCode.like(prefixCode+"%"))
                .orderBy(sectionMaster.sectionCode.desc())
                .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                        .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getSectionCode().replace(prefixCode, "")) + 1), 7, "0"))
                        .toString())
                .orElse(new StringBuilder(prefixCode).append("0000000").toString());
    }
}