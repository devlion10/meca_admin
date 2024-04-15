package kr.or.kpf.lms.repository.homepage;

import kr.or.kpf.lms.repository.entity.homepage.EventInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 이벤트/설문 Repository
 */
public interface EventRepository extends JpaRepository<EventInfo, Long>, QueryByExampleExecutor<EventInfo> {
}
