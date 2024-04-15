package kr.or.kpf.lms.repository;

import kr.or.kpf.lms.repository.entity.BizPbancTmpl5;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import javax.transaction.Transactional;

public interface BizPbancTmpl5Repository extends JpaRepository<BizPbancTmpl5, String>, QueryByExampleExecutor<BizPbancTmpl5> {
    @Transactional
    void deleteAllByBizPbancNo(String bizPbancNo);
}