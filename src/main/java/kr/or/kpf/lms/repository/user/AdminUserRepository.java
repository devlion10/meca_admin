package kr.or.kpf.lms.repository.user;

import kr.or.kpf.lms.repository.entity.user.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 어드민 사용자 Repository
 */
public interface AdminUserRepository extends JpaRepository<AdminUser, Long>, QueryByExampleExecutor<AdminUser>{}