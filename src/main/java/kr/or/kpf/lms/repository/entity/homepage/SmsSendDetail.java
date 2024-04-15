package kr.or.kpf.lms.repository.entity.homepage;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.or.kpf.lms.common.converter.BooleanConverter;
import kr.or.kpf.lms.common.support.CSEntitySupport;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * SMS 발송 세부 정보
 */
@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "SMS_SEND_DETAIL")
@Access(value = AccessType.FIELD)
public class SmsSendDetail extends CSEntitySupport implements Serializable {

    /** 시퀀스 번호 */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "SEQ_NO")
    private Long sequenceNo;

    /** SMS 발송 이력 시퀀스 번호 */
    @Column(name = "MASTER_SEQ_NO")
    private Long masterSequenceNo;

    /** 발신 번호 */
    @Column(name = "SENDER")
    private String sender;

    /** 수신 번호 */
    @Column(name = "RECEIVER")
    private String receiver;

    /** 성공 여부 */
    @Column(name = "SUCCESS_YN")
    @Convert(converter = BooleanConverter.class)
    private Boolean isSuccess;
}
