package kr.or.kpf.lms.repository.education;

import kr.or.kpf.lms.repository.entity.education.ExamMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 시험 마스터 Repository
 */
public interface ExamMasterRepository extends JpaRepository<ExamMaster, Long>, QueryByExampleExecutor<ExamMaster> {}
