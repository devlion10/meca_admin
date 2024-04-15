package kr.or.kpf.lms.repository.contents;

import kr.or.kpf.lms.repository.entity.contents.ContentsChapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 콘텐츠 챕터 목록 테이블 Repository
 */
public interface ContentsChapterRepository extends JpaRepository<ContentsChapter, Long>, QueryByExampleExecutor<ContentsChapter> {}
