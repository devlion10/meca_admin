package kr.or.kpf.lms.repository.education;

import kr.or.kpf.lms.repository.entity.education.ExamQuestionMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 시험 문제 마스터 Repository
 */
public interface ExamQuestionMasterRepository extends JpaRepository<ExamQuestionMaster, Long>, QueryByExampleExecutor<ExamQuestionMaster> {}
