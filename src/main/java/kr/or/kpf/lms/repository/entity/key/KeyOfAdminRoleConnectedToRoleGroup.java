package kr.or.kpf.lms.repository.entity.key;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KeyOfAdminRoleConnectedToRoleGroup implements Serializable {

	private static final long serialVersionUID = -5084186395085435713L;

	@Column(name="ROLE_GROUP_CD")
	private String roleGroupCode;

	@Id
	@Column(name="ROLE_CD")
	private String roleCode;
}
