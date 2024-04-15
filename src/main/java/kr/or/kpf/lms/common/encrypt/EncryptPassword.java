package kr.or.kpf.lms.common.encrypt;

import com.google.gson.Gson;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

/**
 * 비밀번호 일치여부 확인
 */
public class EncryptPassword implements PasswordEncoder {
	
	public EncryptPassword() {}

	@Override
	public String encode(CharSequence rawPassword) {
		return null;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		Map<String,String> encryptInfo = new Gson().fromJson(encodedPassword, Map.class);
		return new String(encryptInfo.get("password")).equals(SecurityUtil.hashPassword(String.valueOf(rawPassword), encryptInfo.get("userId")));
	}
}


