package kr.or.kpf.lms.repository.entity;

import kr.or.kpf.lms.common.support.CSEntitySupport;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "BIZ_EDIT_HIST")
@Access(value = AccessType.FIELD)
public class BizEditHist extends CSEntitySupport implements Serializable {

    /** 일련 번호 */
    @Id
    @Column(name = "BIZ_EDIT_HIST_NO", nullable = false)
    private String bizEditHistNo;

    /** 해당 내용 일련 번호 */
    @Column(name = "BIZ_EDIT_HIST_TRGT_NO", nullable = false)
    private String bizEditHistTrgtNo;

    /** 유형(0: 삭제, 1: 추가, 2: 수정) */
    @Column(name = "BIZ_EDIT_HIST_TYPE", nullable = false)
    private Integer bizEditHistType;

    /** 이전 내역 */
    @Column(name = "BIZ_EDIT_HIST_BFR", nullable = false)
    private String bizEditHistBfr;

    /** 이후 내역 */
    @Column(name = "BIZ_EDIT_HIST_AFTR", nullable = false)
    private String bizEditHistAftr;

    /** 상태 (0: 미승인, 1: 승인, 2: 반려) */
    @Column(name = "BIZ_EDIT_HIST_STTS", nullable = false)
    private Integer bizEditHistStts;

}
