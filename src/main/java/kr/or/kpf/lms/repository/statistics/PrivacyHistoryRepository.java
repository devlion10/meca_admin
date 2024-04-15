package kr.or.kpf.lms.repository.statistics;

import kr.or.kpf.lms.repository.entity.statistics.AdminAccessHistory;
import kr.or.kpf.lms.repository.entity.statistics.PrivacyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 개인정보 수정이력 Repository
 */
public interface PrivacyHistoryRepository extends JpaRepository<PrivacyHistory, Long>, QueryByExampleExecutor<PrivacyHistory> {}
