package kr.or.kpf.lms.repository.education;

import kr.or.kpf.lms.repository.entity.education.CurriculumEvaluate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 교육 과정 연계 설문 목록 테이블 Repository
 */
public interface CurriculumEvaluateRepository extends JpaRepository<CurriculumEvaluate, Long>, QueryByExampleExecutor<CurriculumEvaluate> {}
