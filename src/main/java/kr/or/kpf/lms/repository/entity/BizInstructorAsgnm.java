package kr.or.kpf.lms.repository.entity;

import kr.or.kpf.lms.common.support.CSEntitySupport;
import kr.or.kpf.lms.repository.entity.user.LmsUser;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 강사 모집 공고 강사 배정 Entity
 */

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "BIZ_INSTR_ASGNM")
@Access(value = AccessType.FIELD)
public class BizInstructorAsgnm extends CSEntitySupport implements Serializable {

    /** 일련 번호 */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "SEQ_NO", nullable = false)
    private Long sequenceNo;

    @Column(name = "BIZ_INSTR_ASGNM_NO", nullable = false)
    private String bizInstrAsgnmNo;

    /** 강사 모집 공고 일련 번호 */
    @Column(name = "BIZ_INSTR_NO", nullable = false)
    private String bizInstrNo;

    /** 사업 공고 신청 일련 번호 */
    @Column(name = "BIZ_ORG_APLY_NO", nullable = false)
    private String bizOrgAplyNo;

    /** 사업 공고 신청 강의계획서 일련 번호 */
    @Column(name = "BIZ_ORG_APLY_DTL_NO", nullable = true)
    private String bizOrgAplyDtlNo;

    /** 강사 모집 공고 신청 정보 일련 번호 */
    @Column(name = "BIZ_INSTR_APLY_NO", nullable = false)
    private String bizInstrAplyNo;

    @NotFound(action= NotFoundAction.IGNORE)
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="BIZ_INSTR_APLY_NO", insertable=false, updatable=false)
    private BizInstructorAply bizInstructorAply;

    @PostPersist
    public void generateApplicationNo() {
        String prefix = "BIG";
        String paddedSequence = String.format("%0" + 7 + "d", this.sequenceNo);
        this.bizInstrAsgnmNo = prefix + paddedSequence;
    }

}