package kr.or.kpf.lms.repository.education;

import kr.or.kpf.lms.repository.entity.education.CurriculumApplicationMaster;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;
import java.util.Optional;

/**
 * 교육 과정 신청 이력 테이블 Repository
 */
public interface CurriculumApplicationMasterRepository extends JpaRepository<CurriculumApplicationMaster, Long>, QueryByExampleExecutor<CurriculumApplicationMaster> {

    CurriculumApplicationMaster findByEducationPlanCodeAndUserId(String educationPlanCode, String userId);

    List<CurriculumApplicationMaster> findByEducationPlanCodeAndAdminApprovalStateIn(String educationPlanCode, List<String> enumCode);
}
