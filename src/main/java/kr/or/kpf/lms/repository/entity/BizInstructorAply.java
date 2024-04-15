package kr.or.kpf.lms.repository.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSEntitySupport;
import kr.or.kpf.lms.repository.entity.user.LmsUser;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 강사 모집 공고 신청 정보(수업계획서) Entity
 */

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "BIZ_INSTR_APLY")
@Access(value = AccessType.FIELD)
public class BizInstructorAply extends CSEntitySupport implements Serializable {

    /** 일련 번호 */
    @Id
    @Column(name = "BIZ_INSTR_APLY_NO", nullable = false)
    private String bizInstrAplyNo;

    /** 강사 모집 공고 일련 번호 */
    @Column(name = "BIZ_INSTR_NO", nullable = false)
    private String bizInstrNo;

    /** 사업 공고 신청서 일련 번호 */
    @Column(name = "BIZ_ORG_APLY_NO", nullable = false)
    private String bizOrgAplyNo;

    /** 신청 강사명 */
    @Column(name = "BIZ_INSTR_APLY_INSTR_NM", nullable = true)
    private String bizInstrAplyInstrNm;

    /** 신청 강사 아이디 */
    @Column(name = "BIZ_INSTR_APLY_INSTR_ID", nullable = false)
    private String bizInstrAplyInstrId;

    /** 지원 조건 - 순위 */
    @Column(name = "BIZ_INSTR_APLY_CNDT_ORDR", nullable = true)
    private Integer bizInstrAplyCndtOrdr;

    /** 지원 조건 - 거리  */
    @Column(name = "BIZ_INSTR_APLY_CNDT_DIST", nullable = true)
    private float bizInstrAplyCndtDist;

    /** 강사 모집 공고 신청 강사 코멘트 */
    @Column(name = "BIZ_INSTR_APLY_CMNT", nullable = true)
    private String bizInstrAplyCmnt;

    /** 상태(0: 임시저장, 1: 접수, 2: 승인, 3: 반려, 9: 임시 승인) */
    @Column(name = "BIZ_INSTR_APLY_STTS", nullable = false)
    private Integer bizInstrAplyStts;

    @Transient
    private BizOrganizationAply bizOrganizationAply;

    @Transient
    private List<BizInstructorIdentify> bizInstructorIdentifies = new ArrayList<>();

    @Transient
    private BizInstructorDist bizInstructorDist;

    @Transient
    private LmsUser lmsUser;

    @Transient
    private List<String> dtlDate = new ArrayList<>();

    @Transient
    private Integer bizInstrAsgmnTimeRest;
    @Transient
    private Integer bizInstrAsgmnTimeAll;
    @Transient
    private Integer bizInstrIdntyAmtAll;
}