package kr.or.kpf.lms.repository.homepage;

import kr.or.kpf.lms.repository.entity.homepage.PressRelease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 행사소개 Repository
 */
public interface PressReleaseRepository extends JpaRepository<PressRelease, Long>, QueryByExampleExecutor<PressRelease> {
}
