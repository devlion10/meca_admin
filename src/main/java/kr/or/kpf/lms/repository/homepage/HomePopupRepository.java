package kr.or.kpf.lms.repository.homepage;

import kr.or.kpf.lms.repository.entity.homepage.HomePopup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 배너 Repository
 */
public interface HomePopupRepository extends JpaRepository<HomePopup, String>, QueryByExampleExecutor<HomePopup> {
}
