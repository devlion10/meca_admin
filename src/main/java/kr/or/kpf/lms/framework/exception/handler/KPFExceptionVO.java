package kr.or.kpf.lms.framework.exception.handler;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 서버 오류 응답 처리
 */
@Data
@Builder
@NoArgsConstructor
public class KPFExceptionVO {
	/** 결과 코드 */
	private String resultCode;
	/** 결과 메세지 */
	private String resultMessage;
	/** 개발용 메세지 */
	private String verboseMessage;
	
	public KPFExceptionVO(String resultErrorCode, String resultErrorMsg, String verboseMessage) {
		this.resultCode = resultErrorCode;
		this.resultMessage = resultErrorMsg;
		this.verboseMessage = verboseMessage;
	}
}
