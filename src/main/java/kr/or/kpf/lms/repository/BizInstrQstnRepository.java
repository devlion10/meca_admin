package kr.or.kpf.lms.repository;

import kr.or.kpf.lms.repository.entity.BizInstructorQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 강사 지원 문의 Repository
 */
public interface BizInstrQstnRepository extends JpaRepository<BizInstructorQuestion, String>, QueryByExampleExecutor<BizInstructorQuestion>{ }