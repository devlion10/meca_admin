package kr.or.kpf.lms.repository.homepage;

import kr.or.kpf.lms.repository.entity.homepage.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 공지사항 Repository
 */
public interface NoticeRepository extends JpaRepository<Notice, Long>, QueryByExampleExecutor<Notice> {}
