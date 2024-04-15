package kr.or.kpf.lms.repository.homepage;

import kr.or.kpf.lms.repository.entity.homepage.SmsSendMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * SMS 발송 이력 (마스터) Repository
 */
public interface SmsSendMasterRepository extends JpaRepository<SmsSendMaster, Long>, QueryByExampleExecutor<SmsSendMaster>{}