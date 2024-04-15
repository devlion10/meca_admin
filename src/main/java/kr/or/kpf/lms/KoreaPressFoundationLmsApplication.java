package kr.or.kpf.lms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.DispatcherServlet;

@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class KoreaPressFoundationLmsApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(KoreaPressFoundationLmsApplication.class, args);
		DispatcherServlet dispatcherServlet = (DispatcherServlet)ctx.getBean("dispatcherServlet");
		dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
	}
}
