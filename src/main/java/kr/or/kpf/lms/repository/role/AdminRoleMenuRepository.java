package kr.or.kpf.lms.repository.role;

import kr.or.kpf.lms.repository.entity.role.AdminRoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.math.BigInteger;

/**
 * 어드민 Role 메뉴 테이블 Repository
 */
public interface AdminRoleMenuRepository extends JpaRepository<AdminRoleMenu, BigInteger>, QueryByExampleExecutor<AdminRoleMenu> {}
