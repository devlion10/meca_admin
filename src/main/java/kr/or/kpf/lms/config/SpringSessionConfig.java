package kr.or.kpf.lms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

/**
 * Spring Session을 사용하는 테이블명 변경
 */
@Configuration
@EnableJdbcHttpSession(tableName = "LMS_ADMIN_SESSION")
public class SpringSessionConfig {}
