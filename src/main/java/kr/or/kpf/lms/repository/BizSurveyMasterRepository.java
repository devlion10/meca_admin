package kr.or.kpf.lms.repository;

import kr.or.kpf.lms.repository.entity.BizSurvey;
import kr.or.kpf.lms.repository.entity.BizSurveyMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 상호평가 Repository
 */
public interface BizSurveyMasterRepository extends JpaRepository<BizSurveyMaster, Long>, QueryByExampleExecutor<BizSurveyMaster>{}