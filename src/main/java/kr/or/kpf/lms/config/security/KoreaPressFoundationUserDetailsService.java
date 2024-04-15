package kr.or.kpf.lms.config.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.or.kpf.lms.biz.system.menu.service.MenuService;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.Code;
import kr.or.kpf.lms.config.security.vo.KoreaPressFoundationUserDetails;
import kr.or.kpf.lms.framework.exception.CSNotFound404Exception;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.repository.entity.role.AdminRoleGroup;
import kr.or.kpf.lms.repository.entity.user.AdminUser;
import kr.or.kpf.lms.repository.role.AdminRoleGroupRepository;
import kr.or.kpf.lms.repository.role.AdminRoleMenuRepository;
import kr.or.kpf.lms.repository.user.AdminUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Spring Security의 User Details Service
 */
public class KoreaPressFoundationUserDetailsService implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(KoreaPressFoundationUserDetailsService.class);
	@Autowired private HttpServletRequest request;

	@Autowired private AdminUserRepository adminUserRepository;
	@Autowired private AdminRoleGroupRepository adminRoleGroupRepository;
	@Autowired private MenuService menuService;

	/**
	 * 사용자 객체를 로드
	 *
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username) {
		logger.debug("username: {}", username);
		/** 회원 정보 조회 */
		AdminUser employee = adminUserRepository.findOne(Example.of(AdminUser.builder().userId(username).build()))
											.orElseThrow(() -> new CSNotFound404Exception("존재하지 않는 관리자 ID 입니다."));

		/**
		 * 잠김 정보가 존재할 경우...
		 */
		if(employee.getLockDateTime() != null
				|| (employee.getIsLock() != null && employee.getIsLock())
				|| (employee.getPasswordFailureCount() != null && employee.getPasswordFailureCount() >= 5)
		) {
			throw new KPFException(KPF_RESULT.ERROR0001, "잠김 회원(비밀번호 5회 불일치)입니다. 시스템 관리자에게 문의 바랍니다.");
		}

		/**
		 * 미사용 계정일 경우
		 */
		if(Code.USER_STATE.NONE.enumCode.equals(employee.getState())) {
			throw new KPFException(KPF_RESULT.ERROR0001, "미사용 관리자입니다.");
		}

		Map<String,String> userInfoMap = new HashMap<>();
		userInfoMap.put("userId", username);
		userInfoMap.put("password", employee.getPassword());
		try {
			String passwordInfo = new ObjectMapper().writeValueAsString(userInfoMap);
			employee.setPasswordInfo(passwordInfo);
		} catch (JsonProcessingException e) {
			logger.error("{}- {}", e.getClass().getCanonicalName(), e.getMessage(), e);
		}

		/** 메뉴 권한 설정 */
		List<String> roleList = adminRoleGroupRepository.findAll(Example.of(AdminRoleGroup.builder()
						.roleGroupCode(employee.getRoleGroup())
						.build())).stream()
				.map(AdminRoleGroup::getRoleGroupCode)
				.collect(Collectors.toList());
		/** 권한 획득 */
		List<GrantedAuthority> authorities = getAuthoritiesByMenuRoleMapping(employee, roleList);

		/** 접근 메뉴 */
		employee.setAdminMenus(menuService.allMenu());

		/** 회원 반환 */
		return new KoreaPressFoundationUserDetails(employee, true/* enabled */, true/* accountNonExpired */, true/* credentialsNonExpired */, true/* accountNonLocked */, authorities);
	}

	/**
	 * 스프링 시큐리티에 롤 권한 셋팅
	 * 
	 * @param employee
	 * @param roleList
	 * @return
	 */
	private @NonNull List<GrantedAuthority> getAuthoritiesByMenuRoleMapping(@NonNull AdminUser employee, List<String> roleList) {
		/** 사용자 권한 및 접근 메뉴 설정 */
		List<GrantedAuthority> authorities = new ArrayList<>();
		roleList.stream().forEach(role -> {
			/** 권한 설정 */
			if (!authorities.contains(new SimpleGrantedAuthority(role))) {
				authorities.add(new SimpleGrantedAuthority(role));
			}
		});
		return authorities;
	}
}