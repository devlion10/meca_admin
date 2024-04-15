package kr.or.kpf.lms.config;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import kr.or.kpf.lms.config.security.vo.SecurityDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

/**
 * Spring Application Config (YAML)의 app-config 속성
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app-config")
@Validated
public class AppConfig implements ApplicationRunner {

	private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

	/** Spring Security 관련 설정 */
	@Valid private AppConfigSecurityProperties security;
	/** HTTP 통신 관련 설정 */
	@Valid private HttpWebConnectionProperties webConnection;
	/** 업로드 파일 관련 설정 */
	@Valid private UploadFileProperties uploadFile;
	/** 비즈뿌리오 관련 설정 */
	@Valid private BizPPurioProperties bizPPurio;
	/** 웹 서버 도메인 */
	@Valid private String webDomain;
	/** QR코드 생성 URL */
	@Valid private String qrCodePath;

	/** Spring Security 관련 설정 */
	@Data
	public static class AppConfigSecurityProperties {
		/** Spring Security Debug 모드 활성화 */
		private boolean debug = false;
		/** Spring Security 비밀번호 암호화 키 */
		@NotNull @NotEmpty
		private String encryptKey;

		public void buildLog(StringBuilder sb, String prefix) {
			sb.append(prefix).append("Debug 모드 활성화: ").append(debug).append(System.lineSeparator()).append(prefix)
			.append("비밀번호 암호화 키: ").append(encryptKey).append(System.lineSeparator());
		}
	}

	/** HTTP 통신 관련 설정 */
	@Data
	public static class HttpWebConnectionProperties {
		/** connectionTimeout */
		private Integer httpConnectTimeout;
		/** readTimeout */
		private Integer httpReadTimeout;
		
		public void buildLog(StringBuilder sb, String prefix) {
			sb.append(prefix).append("웹통신 연결 대기 가능 시간: ").append(httpConnectTimeout).append(System.lineSeparator()).append(prefix)
			.append("웹통신 통신 유지 가능 시간: ").append(httpReadTimeout).append(System.lineSeparator());
		}
	}

	@Data
	public static class BizPPurioProperties {
		/** 비즈뿌리오 URL */
		@NotNull @NotEmpty
		private String url;
		/** 비즈뿌리오 ID */
		@NotNull @NotEmpty
		private String id;
		/** 비즈뿌리오 PWD */
		@NotNull @NotEmpty
		private String password;

		public void buildLog(StringBuilder sb, String prefix) {
			sb.append(prefix).append("URL: ").append(url).append(System.lineSeparator()).append(prefix)
					.append("Id: ").append(id).append(System.lineSeparator()).append(prefix)
					.append("Password: ").append(password).append(System.lineSeparator());
		}
	}

	/** 업로드 파일 관련 설정 */
	@Data
	public static class UploadFileProperties {
		/** 파일 저장 기본 경로 */
		@NotNull @NotEmpty
		private String uploadContextPath;
		/** 콘텐츠 파일 저장 경로 */
		@NotNull @NotEmpty
		private String contentsContextPath;
		/** 자료실 관련 폴더 */
		@NotNull @NotEmpty
		private String referenceFolder;
		/** 회원 관련 폴더 */
		@NotNull @NotEmpty
		private String userFolder;
		/** 강사 관련 폴더 */
		@NotNull @NotEmpty
		private String instrFolder;
		/** 교육 과정 신청서 폴더 */
		@NotNull @NotEmpty
		private String applicationFolder;
		/** 1:1 문의 첨부파일 폴더 */
		@NotNull @NotEmpty
		private String myQnaFolder;
		/** 공지사항 첨부파일 폴더 */
		@NotNull @NotEmpty
		private String noticeFolder;
		/** 자료실 첨부파일 폴더 */
		@NotNull @NotEmpty
		private String lmsDataFolder;
		/** 이벤트 썸네일 첨부파일 폴더 */
		@NotNull @NotEmpty
		private String eventFolder;
		/** 이벤트 썸네일 첨부파일 폴더 */
		@NotNull @NotEmpty
		private String pressFolder;
		/** 교육 장소 신청 이력 첨부파일 폴더 */
		@NotNull @NotEmpty
		private String eduPlaceFolder;
		/** 배너/팝업 이미지 첨부파일 폴더 */
		@NotNull @NotEmpty
		private String bannerFolder;
		/** 콘텐츠 썸네일 폴더 */
		@NotNull @NotEmpty
		private String thumbnailFolder;
		/** 출석 QR 코드 폴더 */
		@NotNull @NotEmpty
		private String qrCodeFolder;
		/** 시험 폴더 */
		@NotNull @NotEmpty
		private String examFolder;
		/** 교육 과제 폴더 */
		@NotNull @NotEmpty
		private String assignmentFolder;
		/** 첨부파일 폴더*/
		@NotNull @NotEmpty
		private String uploadFolder;
		/** 수업지도안 폴더*/
		@NotNull @NotEmpty
		private String classGuideFolder;
		/** 사업 공고 신청 (신청서) - 언론인/기본형 첨부파일 폴더*/
		@NotNull @NotEmpty
		private String bizAplyFolder;
		/** 사업 공고 신청 (신청서) 첨부파일 폴더*/
		@NotNull @NotEmpty
		private String bizOrganizationAplyFolder;
		/** 사업 공고 결과 공고 첨부파일 폴더*/
		@NotNull @NotEmpty
		private String bizPbancResultFolder;
		/** 강사 모집 공고 첨부파일 폴더*/
		@NotNull @NotEmpty
		private String bizInstrFolder;
		/** 거리 증빙 지도 첨부파일 폴더*/
		@NotNull @NotEmpty
		private String bizInstrDistFolder;
		/** 결과보고서 첨부파일 폴더*/
		@NotNull @NotEmpty
		private String bizOrganizationRsltRptFolder;

		public void buildLog(StringBuilder sb, String prefix) {
			sb.append(prefix).append("업로드 파일 Context 경로: ").append(uploadContextPath).append(System.lineSeparator()).append(prefix)
					.append("콘텐츠 파일 Context 경로: ").append(contentsContextPath).append(System.lineSeparator()).append(prefix)
					.append("회원 관련 폴더: ").append(userFolder).append(System.lineSeparator()).append(prefix)
					.append("강사 관련 폴더: ").append(instrFolder).append(System.lineSeparator()).append(prefix)
					.append("교육 과정 신청서 폴더: ").append(applicationFolder).append(System.lineSeparator()).append(prefix)
					.append("1:1 문의 첨부파일 폴더: ").append(myQnaFolder).append(System.lineSeparator()).append(prefix)
					.append("공지사항 첨부파일 폴더: ").append(noticeFolder).append(System.lineSeparator()).append(prefix)
					.append("자료실 첨부파일 폴더: ").append(lmsDataFolder).append(System.lineSeparator()).append(prefix)
					.append("이벤트 썸네일 폴더: ").append(eventFolder).append(System.lineSeparator()).append(prefix)
					.append("교육 장소 신청 첨부파일 폴더: ").append(eduPlaceFolder).append(System.lineSeparator()).append(prefix)
					.append("행사소개 폴더: ").append(pressFolder).append(System.lineSeparator()).append(prefix)
					.append("배너/팝업 이미지 폴더: ").append(bannerFolder).append(System.lineSeparator()).append(prefix)
					.append("콘텐츠 썸네일 폴더: ").append(thumbnailFolder).append(System.lineSeparator()).append(prefix)
					.append("출석 QR 코드 폴더: ").append(qrCodeFolder).append(System.lineSeparator()).append(prefix)
					.append("시험 폴더: ").append(examFolder).append(System.lineSeparator())
					.append("과제 폴더: ").append(assignmentFolder).append(System.lineSeparator())
					.append("첨부파일 폴더: ").append(uploadFolder).append(System.lineSeparator())
					.append("수업지도안 폴더: ").append(classGuideFolder).append(System.lineSeparator())
					.append("사업 공고 신청 (신청서) - 언론인/기본형 첨부파일 폴더: ").append(bizAplyFolder).append(System.lineSeparator())
					.append("사업 공고 신청 (신청서) 첨부파일 폴더: ").append(bizOrganizationAplyFolder).append(System.lineSeparator())
					.append("사업 공고 결과 첨부파일 폴더: ").append(bizPbancResultFolder).append(System.lineSeparator())
					.append("강사 모집 공고 결과 첨부파일 폴더: ").append(bizInstrFolder).append(System.lineSeparator())
					.append("거리 증빙 지도 첨부파일 폴더: ").append(bizInstrDistFolder).append(System.lineSeparator())
					.append("결과보고서 첨부파일 폴더: ").append(bizOrganizationRsltRptFolder).append(System.lineSeparator());
		}
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		StringBuilder sb = new StringBuilder(System.lineSeparator());
		this.getSecurity().buildLog(sb, "[Spring Security]");
		this.getWebConnection().buildLog(sb, "[HTTP 통신]");
		this.getUploadFile().buildLog(sb, "[파일 업로드]");
		this.getBizPPurio().buildLog(sb, "[비즈뿌리오]");
		logger.info(sb.toString());
	}
}
