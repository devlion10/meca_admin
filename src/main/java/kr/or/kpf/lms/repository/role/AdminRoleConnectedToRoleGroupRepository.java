package kr.or.kpf.lms.repository.role;

import kr.or.kpf.lms.repository.entity.role.AdminRoleConnectedToRoleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 어드민 메뉴 관련 ROLE & ROLE GROUP 매핑 테이블 Repository
 */
public interface AdminRoleConnectedToRoleGroupRepository extends JpaRepository<AdminRoleConnectedToRoleGroup, Long>, QueryByExampleExecutor<AdminRoleConnectedToRoleGroup> {}
