package kr.or.kpf.lms.repository.user;

import kr.or.kpf.lms.repository.entity.user.InstructorHist;
import kr.or.kpf.lms.repository.entity.user.InstructorInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 강사 정보 테이블 Repository
 */
public interface InstructorHistRepository extends JpaRepository<InstructorHist, Long>, QueryByExampleExecutor<InstructorHist>{}