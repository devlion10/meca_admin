package kr.or.kpf.lms.repository.homepage;

import kr.or.kpf.lms.repository.entity.homepage.LmsData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 자료실 Repository
 */
public interface LmsDataRepository extends JpaRepository<LmsData, Long>, QueryByExampleExecutor<LmsData> {
}
