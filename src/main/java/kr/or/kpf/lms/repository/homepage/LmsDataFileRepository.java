package kr.or.kpf.lms.repository.homepage;

import kr.or.kpf.lms.repository.entity.homepage.LmsDataFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 파일 업로드 Repository
 */
public interface LmsDataFileRepository extends JpaRepository<LmsDataFile, Long>, QueryByExampleExecutor<LmsDataFile>{
}