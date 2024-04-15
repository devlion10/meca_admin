package kr.or.kpf.lms.repository.education;

import kr.or.kpf.lms.repository.entity.education.CurriculumApplicationLecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 일반 교육 과정 출석 테이블 Repository
 */
public interface CurriculumApplicationLectureRepository extends JpaRepository<CurriculumApplicationLecture, Long>, QueryByExampleExecutor<CurriculumApplicationLecture> {}
