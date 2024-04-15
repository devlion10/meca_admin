package kr.or.kpf.lms.repository.user;

import kr.or.kpf.lms.repository.entity.user.LmsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;

/**
 * 웹 회원 테이블 Repository
 */
public interface LmsUserRepository extends JpaRepository<LmsUser, Long>, QueryByExampleExecutor<LmsUser> {
    List<LmsUser> findByApproFlagDateIsNotNull();
}
