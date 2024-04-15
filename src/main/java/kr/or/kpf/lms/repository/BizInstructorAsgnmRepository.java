package kr.or.kpf.lms.repository;

import kr.or.kpf.lms.repository.entity.BizInstructorAsgnm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.Optional;

/**
 * 강사모집 강사 배정 Repository
 */
public interface BizInstructorAsgnmRepository extends JpaRepository<BizInstructorAsgnm, String>, QueryByExampleExecutor<BizInstructorAsgnm>{

    @Override
    Optional<BizInstructorAsgnm> findById(String bizInstAsgnmNo);
}