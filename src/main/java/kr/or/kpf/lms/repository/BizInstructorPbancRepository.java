package kr.or.kpf.lms.repository;

import io.swagger.models.auth.In;
import kr.or.kpf.lms.repository.entity.BizInstructorPbanc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 강사모집 공고의 대상 사업 공고 Repository
 */
public interface BizInstructorPbancRepository extends JpaRepository<BizInstructorPbanc, Long>, QueryByExampleExecutor<BizInstructorPbanc>{
    Optional<BizInstructorPbanc> findBizInstructorPbancBySequenceNo(Long sequenceNo);

    List<BizInstructorPbanc> findByBizInstrRcptEndBeforeAndBizInstrPbancStts(LocalDate endDate, Integer status);
    default List<BizInstructorPbanc> findByBizInstrRcptEndBeforeAndBizInstrPbancStts(String endDate, Integer status) {
        LocalDate endDateTime = LocalDate.parse(endDate);
        return findByBizInstrRcptEndBeforeAndBizInstrPbancStts(endDateTime, status);
    }
}