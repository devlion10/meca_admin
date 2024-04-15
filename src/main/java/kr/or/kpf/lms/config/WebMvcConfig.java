package kr.or.kpf.lms.config;

import kr.or.kpf.lms.framework.interceptor.HttpInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * WebMvc Configuraion
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Autowired protected AppConfig appConfig;

	/** ResourceHandlers */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		/* 캐싱정책 */
		registry.addResourceHandler("/favicon.ico").addResourceLocations("/");
		registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
		registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/static/assets/");
		registry.addResourceHandler("/image/**").addResourceLocations("classpath:/static/image/");
		registry.addResourceHandler("/app/**").addResourceLocations("classpath:/static/app/");
		/** 외부 접근 가능 파일 리소스 등록(샘플 파일 다운로드 등...) */
		registry.addResourceHandler("/download/**").addResourceLocations("classpath:/doc/admin/sample/");
		/** 외부 접근 가능 이미지 리소스 등록 (썸네일 파일 등...) */
		String imageContextPath = appConfig.getUploadFile().getUploadContextPath();
		registry.addResourceHandler("/Public/**")
				.addResourceLocations("file://" + (imageContextPath.startsWith("/") ? imageContextPath + "/" : "/" + imageContextPath + "/"));
		/** 외부 접근 가능 이미지 리소스 등록 (콘텐츠 파일 등...) */
		String contentsContextPath = appConfig.getUploadFile().getContentsContextPath();
		registry.addResourceHandler("/Contents/**")
				.addResourceLocations("file://" + (contentsContextPath.startsWith("/") ? contentsContextPath + "/" : "/" + contentsContextPath + "/"));
	}
}
