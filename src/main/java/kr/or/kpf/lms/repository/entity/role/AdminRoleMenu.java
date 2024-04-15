package kr.or.kpf.lms.repository.entity.role;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * 어드민 Role 메뉴 테이블 Entity
 */
@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ADMIN_ROLE_MENU")
@Access(value = AccessType.FIELD)
public class AdminRoleMenu implements Serializable {

	/** 일련 번호 */
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="SEQ_NO")
	private BigInteger sequenceNo;

	/** 권한 코드 */
	@Column(name="ROLE_GROUP_CD")
	private String roleGroupCode;

	/** 메뉴 일련 번호 */
	@Column(name="MENU_SEQ_NO")
	private Long menuSequenceNo;

	/** 메뉴 URI */
	@Column(name="URI")
	private String uri;
}
