package kr.or.kpf.lms.repository.entity;

import io.swagger.models.auth.In;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.converter.DateToStringConverter;
import kr.or.kpf.lms.common.support.CSEntitySupport;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 강의확인서 정보(강의확인서) Entity
 */

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "BIZ_INSTR_IDNTY")
@Access(value = AccessType.FIELD)
public class BizInstructorIdentify extends CSEntitySupport {
    /** 강의확인서 일련 번호 */
    @Id
    @Column(name = "BIZ_INSTR_IDNTY_NO", nullable = false)
    private String bizInstrIdntyNo;

    /** 기관 신청 일련 번호 */
    @Column(name = "BIZ_ORG_APLY_NO", nullable = false)
    private String bizOrgAplyNo;

    /** 강의확인서 강의시간표 -> 날짜 조회 방식 변경 */
    @Column(name = "BIZ_INSTR_IDNTY_YM", nullable = false)
    private String bizInstrIdntyYm;

    /** 강의확인서 상태 (0: 임시저장, 1: 제출(접수), 2: 기관 승인, 3: 관리자 승인(지출 접수), 4: 지출 완료, 9: 반려) */
    @Column(name = "BIZ_INSTR_IDNTY_STTS", nullable = false)
    private Integer bizInstrIdntyStts;

    /** 강의확인서 수업지도안 */
    @Column(name = "BIZ_INSTR_IDNTY_LSN_FILE", nullable = true)
    private String bizInstrIdntyLsnFile;

    /** 강의확인서 수업지도안 크기 */
    @Column(name = "BIZ_INSTR_IDNTY_LSN_FILE_SIZE", nullable = true)
    private Long bizInstrIdntyLsnFileSize;

    /** 강의확인서 수업지도안 원본명 */
    @Column(name = "BIZ_INSTR_IDNTY_LSN_FILE_ORGN", nullable = true)
    private String bizInstrIdntyLsnFileOrgn;

    /** 강의확인서 수업자료안 */
    @Column(name = "BIZ_INSTR_IDNTY_ATCH_FILE", nullable = true)
    private String bizInstrIdntyAtchFile;

    /** 강의확인서 수업자료안 크기 */
    @Column(name = "BIZ_INSTR_IDNTY_ATCH_FILE_SIZE", nullable = true)
    private Long bizInstrIdntyAtchFileSize;

    /** 강의확인서 수업자료안 원본명 */
    @Column(name = "BIZ_INSTR_IDNTY_ATCH_FILE_ORGN", nullable = true)
    private String bizInstrIdntyAtchFileOrgn;

    /** 강의확인서 강의 시간 */
    @Column(name = "BIZ_INSTR_IDNTY_TIME", nullable = true)
    private String bizInstrIdntyTime;

    /** 강의확인서 승인 일시 */
    @Convert(converter= DateToStringConverter.class)
    @Column(name = "BIZ_INSTR_IDNTY_APRV_DT", nullable = true)
    private String bizInstrIdntyAprvDt;

    /** 강의확인서 지출 년월 */
    @Column(name = "BIZ_INSTR_IDNTY_PAY_YM", nullable = true)
    private String bizInstrIdntyPayYm;

    /** 강의확인서 최종 금액 */
    @Column(name = "BIZ_INSTR_IDNTY_AMT", nullable = true)
    private String bizInstrIdntyAmt;

    @NotFound(action= NotFoundAction.IGNORE)
    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name="BIZ_INSTR_IDNTY_NO", insertable=false, updatable=false)
    private List<BizInstructorIdentifyDtl> bizInstructorIdentifyDtls = new ArrayList<>();

    @Transient
    private String userName;

    @Transient
    private String userTel;

    @Transient
    private String instrNm;

    @Transient
    private String organizationName;

    @Transient
    private BizInstructorDist bizInstructorDist;

    @Transient
    private BizOrganizationAply bizOrganizationAply;

    @Transient
    private BizPbancMaster bizPbancMaster;

}
