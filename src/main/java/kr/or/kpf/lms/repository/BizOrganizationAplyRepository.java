package kr.or.kpf.lms.repository;

import kr.or.kpf.lms.repository.entity.BizOrganizationAply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * 사업 공고 신청 Repository
 */
public interface BizOrganizationAplyRepository extends JpaRepository<BizOrganizationAply, String>, QueryByExampleExecutor<BizOrganizationAply> {
    public BizOrganizationAply findByBizOrgAplyNo(String bizOrgNo);
}