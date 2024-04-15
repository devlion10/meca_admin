package kr.or.kpf.lms.repository.homepage;

import kr.or.kpf.lms.repository.entity.homepage.TopQna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 자주하는 질문 테이블 Repository
 */
public interface TopQnaRepository extends JpaRepository<TopQna, Long>, QueryByExampleExecutor<TopQna> {}
