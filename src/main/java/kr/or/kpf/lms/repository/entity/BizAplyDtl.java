package kr.or.kpf.lms.repository.entity;

import kr.or.kpf.lms.repository.entity.key.KeyOfBizAplyDtl;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "BIZ_APLY_DTL")
@Access(value = AccessType.FIELD)
@IdClass(value = KeyOfBizAplyDtl.class)
public class BizAplyDtl {
    /** 일련 번호 */
    @Id
    @Column(name = "SEQ_NO", nullable = false)
    private BigInteger sequenceNo;

    /** 템플릿 일련 번호 */
    @Id
    @Column(name="BIZ_PBANC_TMPL5_NO", nullable = false)
    private BigInteger bizPbancTmpl5No;

    /** 작성 내용 */
    @Column(name = "BIZ_APLY_DTL_CONT", nullable = true)
    private String bizAplyDtlCont;

    /** 첨부파일 일련 번호 */
    @Column(name="FILE_SN", nullable = true)
    private Long fileSn;

    @NotFound(action= NotFoundAction.IGNORE)
    @OneToOne
    @JoinColumn(name="FILE_SN", insertable=false, updatable=false)
    private BizAplyDtlFile bizAplyDtlFile;

    @Transient
    private BizPbancTmpl5 bizPbancTmpl5;

}
