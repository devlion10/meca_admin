package kr.or.kpf.lms.config.security.event;


import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import kr.or.kpf.lms.common.support.CSComponentSupport;
import kr.or.kpf.lms.config.security.vo.KoreaPressFoundationUserDetails;

/**
 * 로그인 성공 이벤트 리스너
 */
@Component
public class KoreaPressFoundationLoginSuccessEventListener extends CSComponentSupport implements ApplicationListener<AuthenticationSuccessEvent> {	

	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		try {
			handleAuthenticationSuccessEvent(event);
		} catch (Exception ex) {
			logger.error("AuthenticationSuccessEvent Handler ERROR: {}-{}", ex.getClass().getCanonicalName(), ex.getMessage(), ex);
		}
	}

	private void handleAuthenticationSuccessEvent(AuthenticationSuccessEvent event) {
		KoreaPressFoundationUserDetails user = (KoreaPressFoundationUserDetails) event.getAuthentication().getPrincipal();
		WebAuthenticationDetails details = (WebAuthenticationDetails) event.getAuthentication().getDetails();
		logger.debug("로그인성공 User: {}", user);
	}
}
