package kr.or.kpf.lms.repository.role;

import kr.or.kpf.lms.repository.entity.role.AdminRoleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 어드민 Role 그룹 테이블 Repository
 */
public interface AdminRoleGroupRepository extends JpaRepository<AdminRoleGroup, Long>, QueryByExampleExecutor<AdminRoleGroup> {}
