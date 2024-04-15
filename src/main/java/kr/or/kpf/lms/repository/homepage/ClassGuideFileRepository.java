package kr.or.kpf.lms.repository.homepage;

import kr.or.kpf.lms.repository.entity.homepage.ClassGuideFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.math.BigInteger;

/**
 * 수업지도안 파일 테이블 Repository
 */
public interface ClassGuideFileRepository extends JpaRepository<ClassGuideFile, BigInteger>, QueryByExampleExecutor<ClassGuideFile> {}
