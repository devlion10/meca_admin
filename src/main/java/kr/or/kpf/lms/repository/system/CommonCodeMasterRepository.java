package kr.or.kpf.lms.repository.system;

import kr.or.kpf.lms.repository.entity.system.CommonCodeMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 공통 코드 Repository
 */
public interface CommonCodeMasterRepository extends JpaRepository<CommonCodeMaster, String>, QueryByExampleExecutor<CommonCodeMaster>{}