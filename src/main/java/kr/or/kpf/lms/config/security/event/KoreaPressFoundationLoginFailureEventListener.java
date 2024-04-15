package kr.or.kpf.lms.config.security.event;

import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.repository.entity.user.AdminUser;
import kr.or.kpf.lms.repository.user.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Example;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import kr.or.kpf.lms.common.support.CSComponentSupport;

/**
 * 로그인 실패 이벤트 리스너
 */
@Component
public class KoreaPressFoundationLoginFailureEventListener extends CSComponentSupport implements ApplicationListener<AbstractAuthenticationFailureEvent> {
	@Autowired
	private AdminUserRepository adminUserRepository;

	@Override
	public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
		try {
			handleAuthenticationFailureEvent(event);
		} catch(Exception ex) {
			logger.error("AuthenticationFailureEvent Handler ERROR: {}-{}", ex.getClass().getCanonicalName(), ex.getMessage(), ex);
		}
	}
	
	private void handleAuthenticationFailureEvent(AbstractAuthenticationFailureEvent event) {
		/** 인증오류 */
		AuthenticationException ex = event.getException();
		String user = event.getAuthentication().getPrincipal().toString();

		AdminUser adminUser = adminUserRepository.findOne(Example.of(AdminUser.builder()
				.userId(user)
				.build())).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR0001, "유효하지 않은 회원입니다."));
		/** 비밀번호 실패 횟수 + 1 */
		adminUser.setPasswordFailureCount(adminUser.getPasswordFailureCount() + 1);
		/** 비밀번호 실패 횟수가 5회 이상일 경우... 잠금 처리... */
		if(adminUser.getPasswordFailureCount() >= 5) {
			adminUser.setIsLock(true);
		}
		adminUserRepository.saveAndFlush(adminUser);

		logger.debug("로그인실패 user: {} - 오류: {} - {}", user, ex.getClass().getCanonicalName(), ex.getMessage(), ex);
	}
}
