package kr.or.kpf.lms.repository.education;

import kr.or.kpf.lms.repository.entity.education.CurriculumContents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 교육 과정 콘텐츠 목록 테이블 Repository
 */
public interface CurriculumContentsRepository extends JpaRepository<CurriculumContents, Long>, QueryByExampleExecutor<CurriculumContents> {}
