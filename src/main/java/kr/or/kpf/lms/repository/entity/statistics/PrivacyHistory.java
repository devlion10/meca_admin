package kr.or.kpf.lms.repository.entity.statistics;

import kr.or.kpf.lms.common.support.CSEntitySupport;
import kr.or.kpf.lms.repository.entity.user.AdminUser;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 어드민 메뉴 접근 이력 테이블 Entity
 */
@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "PRIVACY_HISTORY")
@Access(value = AccessType.FIELD)
public class PrivacyHistory extends CSEntitySupport implements Serializable {

    /** 일련 번호 */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "SEQ_NO")
    private Long sequenceNo;

    /** TRACE_ID */
    @Column(name="TRACE_ID")
    private String traceId;

    /** 요청 HTTP METHOD */
    @Column(name="METHOD")
    private String httpMethod;

    /** 요청 URI */
    @Column(name="URI")
    private String uri;

    /** 대상 아이디 */
    @Column(name="TARGET_ID")
    private String targetId;

    /** 대상 이름 */
    @Column(name="TARGET_NAME")
    private String targetName;

    /** 등록자 유저명 */
    @Column(name="RGTR_LGN_NM")
    private String registUserName;

    /** REMOTE IP */
    @Column(name="REMOTE_IP")
    private String remoteIp;

    @Transient
    private String menuFullName;
}
