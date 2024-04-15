package kr.or.kpf.lms.repository.homepage;

import kr.or.kpf.lms.repository.entity.homepage.ClassGuide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 수업지도안 테이블 Repository
 */
public interface ClassGuideRepository extends JpaRepository<ClassGuide, String>, QueryByExampleExecutor<ClassGuide> {}
