package kr.or.kpf.lms.repository.entity.role;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import kr.or.kpf.lms.common.support.CSEntitySupport;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 어드민 Role 테이블 Entity
 */
@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ADMIN_ROLE")
@Access(value = AccessType.FIELD)
public class AdminRole extends CSEntitySupport implements Serializable {

	/** 권한 코드 */
	@Id
	@Column(name="ROLE_CD")
	private String roleCode;

	/** 권한명 */
	@Column(name="ROLE_NM")
	private String roleName;

	/** 권한 설명 */
	@Column(name="MEMO")
	private String memo;
}
