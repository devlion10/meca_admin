package kr.or.kpf.lms.repository;

import kr.or.kpf.lms.repository.entity.BizInstructorIdentify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 강의확인서 Repository
 */
public interface BizInstrIdentifyRepository extends JpaRepository<BizInstructorIdentify, String>, QueryByExampleExecutor<BizInstructorIdentify>{}