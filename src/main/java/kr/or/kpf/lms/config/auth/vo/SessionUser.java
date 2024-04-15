package kr.or.kpf.lms.config.auth.vo;

import kr.or.kpf.lms.repository.entity.user.AdminUser;
import lombok.Getter;

import java.io.Serializable;

/**
 * 세션에 사용자 정보를 저장하기 위한 Dto 클래스
 */
@Getter
public class SessionUser implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String email;

	public SessionUser(AdminUser user) {
		this.name = user.getUserName();
		this.email = user.getEmail();
	}
}
