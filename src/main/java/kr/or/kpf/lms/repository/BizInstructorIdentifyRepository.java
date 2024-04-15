package kr.or.kpf.lms.repository;

import kr.or.kpf.lms.repository.entity.BizInstructorIdentify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;
import java.util.Optional;
import java.util.stream.DoubleStream;

/**
 * 강의확인서 Repository
 */
public interface BizInstructorIdentifyRepository extends JpaRepository<BizInstructorIdentify, String>, QueryByExampleExecutor<BizInstructorIdentify>{

    @Override
    Optional<BizInstructorIdentify> findById(String bizInstIdentifyNo);

    List<BizInstructorIdentify> findAllByBizInstrIdntyPayYmAndBizInstrIdntyStts(String bizInstrIdntyYm, Integer bizInstrIdntyStts);
}