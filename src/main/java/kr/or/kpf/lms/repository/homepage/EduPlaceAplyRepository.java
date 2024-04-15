package kr.or.kpf.lms.repository.homepage;

import kr.or.kpf.lms.repository.entity.homepage.EduPlaceAply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 교육장 신청 이력 Repository
 */
public interface EduPlaceAplyRepository extends JpaRepository<EduPlaceAply, Long>, QueryByExampleExecutor<EduPlaceAply> {
}
