package kr.or.kpf.lms.repository;

import kr.or.kpf.lms.repository.entity.BizInstructorAsgnm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 강사 배정 Repository
 */
public interface BizInstrAsgnmRepository extends JpaRepository<BizInstructorAsgnm, String>, QueryByExampleExecutor<BizInstructorAsgnm>{}