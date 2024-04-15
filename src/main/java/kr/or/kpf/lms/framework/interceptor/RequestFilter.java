package kr.or.kpf.lms.framework.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 인터셉터 필터
 */
@Component
public class RequestFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(RequestFilter.class);

	@Autowired
	HttpInterceptor httpInterceptor;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpSession session = ((HttpServletRequest) request).getSession();
		session.setMaxInactiveInterval(60*60);

		/** LOG REQUEST */
		ResettableStreamHttpServletRequest wrappedRequest = null;
		ResettableStreamHttpServletResponse wrappedResponse = null;
		try {
			wrappedRequest = new ResettableStreamHttpServletRequest((HttpServletRequest) request);
			wrappedResponse = new ResettableStreamHttpServletResponse((HttpServletResponse) response);
			httpInterceptor.writeRequestPosaAudit(wrappedRequest);
		} catch (Exception e) {
			logger.error("Fail to wrap request and response", e);
		}
		chain.doFilter(wrappedRequest, wrappedResponse);
	}
}
