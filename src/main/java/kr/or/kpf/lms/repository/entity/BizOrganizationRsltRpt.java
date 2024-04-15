package kr.or.kpf.lms.repository.entity;

import kr.or.kpf.lms.common.support.CSEntitySupport;
import kr.or.kpf.lms.repository.entity.user.OrganizationInfo;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 결과보고서 Entity
 */

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "BIZ_ORG_RSLT_RPT")
@Access(value = AccessType.FIELD)
public class BizOrganizationRsltRpt extends CSEntitySupport implements Serializable {
    /** 결과보고서 일련 번호 */
    @Id
    @Column(name = "BIZ_ORG_RSLT_RPT_NO", nullable = false)
    private String bizOrgRsltRptNo;

    /** 사업 공고 신청 일련 번호 */
    @Column(name = "BIZ_ORG_APLY_NO", nullable = false)
    private String bizOrgAplyNo;

    /** 기관 코드 */
    @Column(name = "ORG_CD", nullable = false)
    private String orgCd;

    /** 결과보고서 사업 성과 */
    @Column(name = "BIZ_ORG_RSLT_RPT_RSLT", nullable = false)
    private String bizOrgRsltRptRslt;

    /** 결과보고서 우수 사례 */
    @Column(name = "BIZ_ORG_RSLT_RPT_EXMP", nullable = false)
    private String bizOrgRsltRptExmp;

    /** 결과보고서 요청사항(건의사항 및 개선사항) */
    @Column(name = "BIZ_ORG_RSLT_RPT_DMND", nullable = false)
    private String bizOrgRsltRptDmnd;

    /** 결과보고서 상태 */
    @Column(name = "BIZ_ORG_RSLT_RPT_STTS", nullable = false)
    private Integer bizOrgRsltRptStts;

    /** 결과보고서 첨부파일 */
    @Column(name = "BIZ_ORG_RSLT_RPT_FILE", nullable = false)
    private String bizOrgRsltRptFile;

    /** 결과보고서 첨부파일 사이즈 */
    @Column(name = "BIZ_ORG_RSLT_RPT_FILE_SIZE", nullable = false)
    private Long bizOrgRsltRptFileSize;

    /** 결과보고서 첨부파일 원본명 */
    @Column(name = "BIZ_ORG_RSLT_RPT_FILE_ORGN", nullable = false)
    private String bizOrgRsltRptFileOrigin;

    /** 기관명 */
    @Transient
    private OrganizationInfo organizationInfo;

    /** 강의확인서 정보 */
    @Transient
    private List<BizInstructorIdentify> bizInstructorIdentifies;

    /** 공고 신청 정보 */
    @Transient
    private BizOrganizationAply bizOrganizationAply;

}