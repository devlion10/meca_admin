package kr.or.kpf.lms.repository.entity.role;

import kr.or.kpf.lms.common.support.CSEntitySupport;
import kr.or.kpf.lms.repository.entity.key.KeyOfAdminRoleConnectedToRoleGroup;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 어드민 메뉴 관련 ROLE & ROLE GROUP 매핑 테이블 Entity
 */
@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ADMIN_ROLE_GROUP_MAP")
@Access(value = AccessType.FIELD)
@IdClass(KeyOfAdminRoleConnectedToRoleGroup.class)
public class AdminRoleConnectedToRoleGroup extends CSEntitySupport implements Serializable {

	/** 권한 그룹 코드 */
	@Id
	@Column(name="ROLE_GROUP_CD")
	private String roleGroupCode;

	/** 권한 코드 */
	@Id
	@Column(name="ROLE_CD")
	private String roleCode;

	@NotFound(action= NotFoundAction.IGNORE)
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ROLE_GROUP_CD", insertable=false, updatable=false)
	private AdminRoleGroup adminRoleGroup;

	@NotFound(action= NotFoundAction.IGNORE)
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ROLE_CD", insertable=false, updatable=false)
	private AdminRole adminRole;
}
