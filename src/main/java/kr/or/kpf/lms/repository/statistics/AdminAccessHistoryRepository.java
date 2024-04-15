package kr.or.kpf.lms.repository.statistics;

import kr.or.kpf.lms.repository.entity.statistics.AdminAccessHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 어드민 메뉴 접근 이력 Repository
 */
public interface AdminAccessHistoryRepository extends JpaRepository<AdminAccessHistory, Long>, QueryByExampleExecutor<AdminAccessHistory> {}
