package kr.or.kpf.lms.repository.role;

import kr.or.kpf.lms.repository.entity.role.AdminRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 어드민 Role 테이블 Repository
 */
public interface AdminRoleRepository extends JpaRepository<AdminRole, Long>, QueryByExampleExecutor<AdminRole> {}
