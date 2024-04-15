package kr.or.kpf.lms.repository.contents;

import kr.or.kpf.lms.repository.entity.contents.ContentsApplicationChapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 콘텐츠 신청 챕터 목록 Repository
 */
public interface ContentsApplicationChapterRepository extends JpaRepository<ContentsApplicationChapter, Long>, QueryByExampleExecutor<ContentsApplicationChapter> {}
