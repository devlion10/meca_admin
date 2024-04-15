package kr.or.kpf.lms.repository.homepage;

import kr.or.kpf.lms.repository.entity.homepage.SmsSendDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * SMS 발송 세부 정보 Repository
 */
public interface SmsSendDetailRepository extends JpaRepository<SmsSendDetail, Long>, QueryByExampleExecutor<SmsSendDetail>{}