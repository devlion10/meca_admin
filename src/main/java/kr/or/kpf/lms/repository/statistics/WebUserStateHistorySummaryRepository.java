package kr.or.kpf.lms.repository.statistics;

import kr.or.kpf.lms.repository.entity.statistics.WebUserStateHistorySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * LMS 회원 상태 내역 집계 테이블 Repository
 */
public interface WebUserStateHistorySummaryRepository extends JpaRepository<WebUserStateHistorySummary, Long>, QueryByExampleExecutor<WebUserStateHistorySummary> {}
