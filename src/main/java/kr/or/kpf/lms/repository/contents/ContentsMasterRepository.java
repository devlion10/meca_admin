package kr.or.kpf.lms.repository.contents;

import kr.or.kpf.lms.repository.entity.contents.ContentsMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Optional;

/**
 * 콘텐츠 마스터 Repository
 */
public interface ContentsMasterRepository extends JpaRepository<ContentsMaster, Long>, QueryByExampleExecutor<ContentsMaster>{}