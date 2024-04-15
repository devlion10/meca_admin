package kr.or.kpf.lms.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.or.kpf.lms.biz.system.code.vo.request.CommonCodeViewRequestVO;
import kr.or.kpf.lms.common.support.CSRepositorySupport;
import kr.or.kpf.lms.repository.entity.system.CommonCodeMaster;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.or.kpf.lms.repository.entity.system.QCommonCodeMaster.commonCodeMaster;

/**
 * 공통 Repository 구현체
 */
@Repository
public class CommonRepositoryImpl extends CSRepositorySupport implements CommonRepository  {

    private final JPAQueryFactory jpaQueryFactory;
    public CommonRepositoryImpl(JPAQueryFactory jpaQueryFactory){ this.jpaQueryFactory = jpaQueryFactory; }

    @Override
    public List<CommonCodeMaster> findTopCommonCode(CommonCodeViewRequestVO requestObject) {
        return jpaQueryFactory.selectFrom(commonCodeMaster)
                .where(condition(requestObject.getCodeName(), commonCodeMaster.codeName::contains),
                        condition(requestObject.getUpIndividualCode(), commonCodeMaster.upIndividualCode::eq),
                        commonCodeMaster.codeDepth.eq(0))
                .orderBy(commonCodeMaster.code.asc(), commonCodeMaster.codeSort.asc())
                .offset(requestObject.getPageable().getOffset())/** 페이지 번호 */
                .limit(requestObject.getPageable().getPageSize())/** 페이지 사이즈 */
                .fetch();
    }
}
