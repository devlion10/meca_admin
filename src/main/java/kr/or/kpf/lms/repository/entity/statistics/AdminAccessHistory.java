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
@Table(name = "ADMIN_ACCESS_HISTORY")
@Access(value = AccessType.FIELD)
public class AdminAccessHistory extends CSEntitySupport implements Serializable {

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

    /** 쿼리 파라미터 */
    @Column(name="QUERY_PARAM")
    private String queryParameter;

    /** 접속 IP */
    @Column(name="REMOTE_IP")
    private String remoteIp;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="RGTR_LGN_ID", referencedColumnName = "LGN_ID", insertable=false, updatable=false)
    @NotFound(action = NotFoundAction.IGNORE)
    private AdminUser adminUser;

    @Transient
    private String menuFullName;
}
