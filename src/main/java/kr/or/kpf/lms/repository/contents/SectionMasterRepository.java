package kr.or.kpf.lms.repository.contents;

import kr.or.kpf.lms.repository.entity.contents.SectionMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 절 마스터 테이블 Repository
 */
public interface SectionMasterRepository extends JpaRepository<SectionMaster, Long>, QueryByExampleExecutor<SectionMaster> {}
