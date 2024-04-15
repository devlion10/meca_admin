package kr.or.kpf.lms.repository.system;

import kr.or.kpf.lms.repository.entity.BizInstructorIdentify;
import kr.or.kpf.lms.repository.entity.system.AdminMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;

/**
 * 어드민 메뉴 정보 Repository
 */
public interface AdminMenuRepository extends JpaRepository<AdminMenu, Long>, QueryByExampleExecutor<AdminMenu> { }
