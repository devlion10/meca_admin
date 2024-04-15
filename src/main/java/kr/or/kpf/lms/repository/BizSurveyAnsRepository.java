package kr.or.kpf.lms.repository;

import kr.or.kpf.lms.repository.entity.BizSurveyAns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 상호평가 - 응답 Repository
 */
public interface BizSurveyAnsRepository extends JpaRepository<BizSurveyAns, String>, QueryByExampleExecutor<BizSurveyAns>{}