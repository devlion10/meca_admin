package kr.or.kpf.lms.repository.homepage;

import kr.or.kpf.lms.repository.entity.homepage.EducationReview;
import kr.or.kpf.lms.repository.entity.homepage.MyQna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.math.BigInteger;

/**
 * 교육 후기방 Repository
 */
public interface EducationReviewRepository extends JpaRepository<EducationReview, BigInteger>, QueryByExampleExecutor<EducationReview> {}
