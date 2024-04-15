package kr.or.kpf.lms.repository.education;

import kr.or.kpf.lms.repository.entity.education.LectureMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 일반 교육 강의 마스터 Repository
 */
public interface LectureMasterRepository extends JpaRepository<LectureMaster, String>, QueryByExampleExecutor<LectureMaster>{}