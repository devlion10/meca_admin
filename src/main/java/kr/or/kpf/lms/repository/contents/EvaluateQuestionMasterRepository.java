package kr.or.kpf.lms.repository.contents;

import kr.or.kpf.lms.repository.entity.contents.EvaluateQuestionMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 강의 평가 관리 테이블 Repository
 */
public interface EvaluateQuestionMasterRepository extends JpaRepository<EvaluateQuestionMaster, Long>, QueryByExampleExecutor<EvaluateQuestionMaster> {}
