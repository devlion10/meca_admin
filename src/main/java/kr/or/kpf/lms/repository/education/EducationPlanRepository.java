package kr.or.kpf.lms.repository.education;

import kr.or.kpf.lms.repository.entity.education.EducationPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * 교육 계획 목록 Repository
 */
public interface EducationPlanRepository extends JpaRepository<EducationPlan, Long>, QueryByExampleExecutor<EducationPlan> {

    List<EducationPlan> findByOperationBeginDateTimeBetween(String startDate, String endDate);
}