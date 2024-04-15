package kr.or.kpf.lms.repository.entity.homepage;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.or.kpf.lms.common.converter.DateToStringConverter;
import kr.or.kpf.lms.common.support.CSEntitySupport;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * SMS 발송 이력 (마스터)
 */
@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "SMS_SEND_MASTER")
@Access(value = AccessType.FIELD)
public class SmsSendMaster extends CSEntitySupport implements Serializable {

    /** 시퀀스 번호 */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "SEQ_NO")
    private Long sequenceNo;

    /** SMS 내용 */
    @Column(name = "CONTENT")
    private String content;

    /** 발신 번호 */
    @Column(name = "SENDER")
    private String sender;

    /** 발신 일시 */
    @Column(name = "SEND_DT")
    @Convert(converter= DateToStringConverter.class)
    private String sendDateTime;

    /** 요청 건수 */
    @Column(name = "TOTAL_CNT")
    private Integer totalCount;

    /** 성공 건수 */
    @Column(name = "SUCCESS_CNT")
    private Integer successCount;

    /** 실패 건수 */
    @Column(name = "FAIL_CNT")
    private Integer failureCount;

    @OneToMany
    @JoinColumn(name = "MASTER_SEQ_NO", referencedColumnName = "SEQ_NO", insertable = false, updatable = false)
    private List<SmsSendDetail> smsSendDetailList;
}
