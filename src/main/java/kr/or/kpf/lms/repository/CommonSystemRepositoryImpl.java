package kr.or.kpf.lms.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.or.kpf.lms.biz.system.code.vo.request.CommonCodeViewRequestVO;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import kr.or.kpf.lms.repository.entity.system.CommonCodeMaster;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static kr.or.kpf.lms.repository.entity.system.QCommonCodeMaster.commonCodeMaster;

/**
 * 시스템 관리 공통 Repository 구현체
 */
@Repository
public class CommonSystemRepositoryImpl implements CommonSystemRepository {

    private final JPAQueryFactory jpaQueryFactory;
    public CommonSystemRepositoryImpl(JPAQueryFactory jpaQueryFactory){ this.jpaQueryFactory = jpaQueryFactory; }

    @Override
    public <T extends CSViewVOSupport> List<?> excelDownload(T requestObject) {
        return null;
    }

    @Override
    public List<CommonCodeMaster> findTopCommonCode(CommonCodeViewRequestVO requestObject) {
        return jpaQueryFactory.selectFrom(commonCodeMaster)
                .where(condition(requestObject.getCodeName(), commonCodeMaster.codeName::contains),
                        condition(requestObject.getUpIndividualCode(), commonCodeMaster.upIndividualCode::eq),
                        commonCodeMaster.codeDepth.eq(0))
                .orderBy(commonCodeMaster.code.asc(), commonCodeMaster.codeSort.asc())
                .fetch();
    }

    /**
     * 데이터가 있을 경우만, where절 추가
     *
     * @param value
     * @param function
     * @return
     * @param <T>
     */
    private <T> Predicate condition(T value, Function<T, Predicate> function) {
        return Optional.ofNullable(value).map(function).orElse(null);
    }

    /**
     * 일련 번호 생성
     */
    @Override
    public String generateCode(String prefixCode) {
        if (prefixCode.equals("CODE")) {/** 공통코드 */
            return jpaQueryFactory.selectFrom(commonCodeMaster)
                    .where(commonCodeMaster.individualCode.like(prefixCode+"%"))
                    .orderBy(commonCodeMaster.individualCode.desc())
                    .fetch().stream().findFirst().map(data -> new StringBuilder(prefixCode)
                            .append(StringUtils.leftPad(String.valueOf(Integer.parseInt(data.getIndividualCode().replace(prefixCode, "")) + 1), 5, "0"))
                            .toString())
                    .orElse(new StringBuilder(prefixCode).append("00000").toString());
        } else {
            return null;
        }
    }

    /** 자동 순서 생성(공통코드) */
    @Override
    public Integer generateOrderAutoIncrease(String upIndividualCode) {
        Optional<CommonCodeMaster> order = jpaQueryFactory.selectFrom(commonCodeMaster)
                .where(commonCodeMaster.upIndividualCode.eq(upIndividualCode))
                .orderBy(commonCodeMaster.codeSort.desc())
                .fetch().stream().findFirst();

        if (!order.isEmpty()){
            return order.get().getCodeSort() + 1;
        }
        return 1;
    }
}