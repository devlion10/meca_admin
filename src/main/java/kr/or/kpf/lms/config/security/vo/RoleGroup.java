package kr.or.kpf.lms.config.security.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleGroup {

	ANONYMOUS("ANONYMOUS", "비인증 관리자 그룹"),
	OPERATE("OPERATE", "운영 관리자 그룹"),
	GENERAL("GENERAL", "일반 관리자 그룹"),
	SUPER("SUPER", "슈퍼 관리자 그룹");

	private final String key;
	private final String title;
}