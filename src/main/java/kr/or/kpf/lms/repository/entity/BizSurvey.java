package kr.or.kpf.lms.repository.entity;

import kr.or.kpf.lms.common.support.CSEntitySupport;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 상호평가 - 평가지 테이블 Entity
 */

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "BIZ_SURVEY")
@Access(value = AccessType.FIELD)
public class BizSurvey extends CSEntitySupport implements Serializable {
    /** 상호평가 평가지 일련 번호 */
    @Id
    @Column(name = "BIZ_SURVEY_NO", nullable = false)
    private String bizSurveyNo;

    /** 상호평가 평가지 구분  */
    @Column(name = "BIZ_SURVEY_CTGR", nullable = false)
    private Integer bizSurveyCtgr;

    /** 상호평가 평가지명 */
    @Column(name = "BIZ_SURVEY_NM", nullable = false)
    private String bizSurveyNm;

    /** 상호평가 평가지 내용 */
    @Column(name = "BIZ_SURVEY_CN", nullable = false)
    private String bizSurveyCn;

    /** 상호평가 평가지 상태 */
    @Column(name = "BIZ_SURVEY_STTS", nullable = false)
    private Integer bizSurveyStts;

    @Transient
    private List<BizSurveyQitem> bizSurveyQitems = new ArrayList<>();

}