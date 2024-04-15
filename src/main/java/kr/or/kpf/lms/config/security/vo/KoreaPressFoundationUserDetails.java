package kr.or.kpf.lms.config.security.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.or.kpf.lms.repository.entity.system.AdminMenu;
import kr.or.kpf.lms.repository.entity.user.AdminUser;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.List;

/**
 * 일반 로그인 사용자 정보 (Spring Security UserDetails)
 */
@EqualsAndHashCode(callSuper=false)
public class KoreaPressFoundationUserDetails extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = 686650356820843365L;

	/** 로그인 회원 정보 DT */
	@JsonIgnore
	private final AdminUser user;

	public String getUserId() { return user != null ? user.getUserId() : null; }
	public String getName() { return user != null ? user.getUserName() : null; }
	public String getEmail() { return user != null ? user.getEmail() : null; }
	public String getRoleGroup() { return user != null ? user.getRoleGroup() : null; }
	/* 접근 메뉴 리스트 */
	public List<AdminMenu> getAccessMenuList() { return user!=null ? user.getAdminMenus() : null; }

	/**
	 * 생성자
	 * @param user 로그인 회원.
	 * @param enabled 회원 활성화상태 여부
	 * @param accountNonExpired 계정이 만료되지 않았는지 여부
	 * @param credentialsNonExpired 암호가 만료되지 않았는지 여부
	 * @param accountNonLocked 계정이 잠기지 않았는지 여부
	 * @param authorities 회원 권한목록
	 */
	public KoreaPressFoundationUserDetails(AdminUser user,
			boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(user.getUserName(), user.getPasswordInfo(), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.user = user;
	}

	/**
	 * SecurityContextHolder에서 현재 로그인정보 획득
	 * @return 현재 로그인 KoreaPressFoundationUserDetails 정보
	 */
	public static KoreaPressFoundationUserDetails current() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication.getPrincipal() instanceof KoreaPressFoundationUserDetails) {
			return (KoreaPressFoundationUserDetails) authentication.getPrincipal();
		}
		return null;
	}

	@Override
	public String toString() {
		return "KoreaPressFoundationUserDetails [user=" + user.toString() + "]";
	}
}
