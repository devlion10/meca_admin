package kr.or.kpf.lms.repository;

import kr.or.kpf.lms.repository.entity.BizAplyDtlFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 사업 공고 신청 - 언론인/기본형 파일 Repository
 */
public interface BizAplyDtlFileRepository extends JpaRepository<BizAplyDtlFile, Long>, QueryByExampleExecutor<BizAplyDtlFile> { }