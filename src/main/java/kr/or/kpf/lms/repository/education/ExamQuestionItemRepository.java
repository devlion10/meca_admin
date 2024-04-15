package kr.or.kpf.lms.repository.education;

import kr.or.kpf.lms.repository.entity.education.ExamQuestionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 시험 문제 문항 Repository
 */
public interface ExamQuestionItemRepository extends JpaRepository<ExamQuestionItem, Long>, QueryByExampleExecutor<ExamQuestionItem> {}
