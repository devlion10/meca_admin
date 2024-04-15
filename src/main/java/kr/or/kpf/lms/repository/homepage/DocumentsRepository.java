package kr.or.kpf.lms.repository.homepage;

import kr.or.kpf.lms.repository.entity.homepage.Documents;
import kr.or.kpf.lms.repository.entity.homepage.MyQna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.math.BigInteger;

/**
 * 문서(개인정보 , 이용 약관 및 기타) Repository
 */
public interface DocumentsRepository extends JpaRepository<Documents, BigInteger>, QueryByExampleExecutor<Documents> {}
