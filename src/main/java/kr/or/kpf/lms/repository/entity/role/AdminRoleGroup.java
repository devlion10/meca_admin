package kr.or.kpf.lms.repository.entity.role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.or.kpf.lms.common.support.CSEntitySupport;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 어드민 Role 그룹 테이블 Entity
 */
@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ADMIN_ROLE_GROUP")
@Access(value = AccessType.FIELD)
public class AdminRoleGroup extends CSEntitySupport implements Serializable {

	/** 권한 그룹 코드 */
	@Id
	@Column(name="ROLE_GROUP_CD")
	private String roleGroupCode;

	/** 권한 그룹명 */
	@Column(name="ROLE_GROUP_NM")
	private String roleGroupName;

	/** 권한 그룹 설명 */
	@Column(name="MEMO")
	private String memo;

	@Transient
	private List<AdminRoleMenu> adminRoleMenus;
}
