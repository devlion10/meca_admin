package kr.or.kpf.lms.framework.interceptor;

import com.google.gson.Gson;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import kr.or.kpf.lms.repository.entity.statistics.AdminAccessHistory;
import kr.or.kpf.lms.repository.entity.statistics.PrivacyHistory;
import kr.or.kpf.lms.repository.entity.user.AdminUser;
import kr.or.kpf.lms.repository.entity.user.InstructorInfo;
import kr.or.kpf.lms.repository.entity.user.LmsUser;
import kr.or.kpf.lms.repository.statistics.AdminAccessHistoryRepository;
import kr.or.kpf.lms.repository.statistics.PrivacyHistoryRepository;
import kr.or.kpf.lms.repository.user.AdminUserRepository;
import kr.or.kpf.lms.repository.user.InstructorInfoRepository;
import kr.or.kpf.lms.repository.user.LmsUserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.Random;

/**
 * Http 통신 인터셉터
 */
@Component
@RequiredArgsConstructor
public class HttpInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(HttpInterceptor.class);

	private final String MDC_CLIENT_TRACE_ID = "X-B3-TraceId";
	private final String MDC_CLIENT_SPAN_ID = "X-B3-SpanId";

	private final AdminAccessHistoryRepository adminAccessHistoryRepository;
	private final LmsUserRepository lmsUserRepository;
	private final AdminUserRepository adminUserRepository;
	private final InstructorInfoRepository instructorInfoRepository;
	private final PrivacyHistoryRepository privacyHistoryRepository;

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	public void writeRequestPosaAudit(ResettableStreamHttpServletRequest wrappedRequest) throws IOException {
		String ipAddress = wrappedRequest.getRemoteAddr();
		if (wrappedRequest instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest = wrappedRequest;
			String xForwardedFor = httpServletRequest.getHeader("X-FORWARDED-FOR");
			if(xForwardedFor!=null && xForwardedFor.length()>0) {
				ipAddress = xForwardedFor; /** client, proxy1, proxy2 */
				if(ipAddress.indexOf(',') >= 0) {
					ipAddress = ipAddress.substring(0, ipAddress.indexOf(','));
				}
			}
			MDC.put("client.httpMethod", httpServletRequest.getMethod());
			MDC.put("client.requestURI", httpServletRequest.getRequestURI());
		}

		if(ipAddress.equals("0:0:0:0:0:0:0:1")) {
			ipAddress = "127.0.0.1";
		}

		if(ipAddress.length()>0) {
			MDC.put("client.accessIP", ipAddress);
		}
		String spanId = Long.toHexString(new Random().nextLong());
		MDC.put(MDC_CLIENT_TRACE_ID, spanId);
		MDC.put(MDC_CLIENT_SPAN_ID, spanId);
		Optional.ofNullable(wrappedRequest.getHeader(MDC_CLIENT_TRACE_ID))
				.filter(StringUtils::isNotBlank)
				.ifPresent(traceId -> {
					MDC.put(MDC_CLIENT_TRACE_ID, traceId);
				});
		/** 어드민 메뉴 접근 이력 저장 */
		if(!wrappedRequest.getRequestURI().contains("/app/") &&
				!wrappedRequest.getRequestURI().contains("/css/") &&
				!wrappedRequest.getRequestURI().contains("/favicon.ico") &&
				!wrappedRequest.getRequestURI().contains("/assets/")) {

			String requestParameter = "{}";

			if (wrappedRequest.getContentType() != null && !wrappedRequest.getContentType().contains("multipart/form-data")) {
				String requestBody = org.apache.commons.io.IOUtils.toString(wrappedRequest.getReader());
				requestParameter = StringUtils.isEmpty(requestBody) ? new Gson().toJson(CSSearchMap.of(wrappedRequest)) : requestBody;
			}

			adminAccessHistoryRepository.saveAndFlush(AdminAccessHistory.builder()
					.traceId(String.valueOf(MDC.get(MDC_CLIENT_TRACE_ID)))
					.httpMethod(wrappedRequest.getMethod())
					.uri(wrappedRequest.getRequestURI())
					.queryParameter(requestParameter)
					.remoteIp(ipAddress)
					.build());
		}
	}
}