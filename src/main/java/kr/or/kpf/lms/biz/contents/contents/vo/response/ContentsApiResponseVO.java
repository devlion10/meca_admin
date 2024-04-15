package kr.or.kpf.lms.biz.contents.contents.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 콘텐츠 메뉴 관련 응답 객체
 */
@Schema(name="ContentsApiResponseVO", description="콘텐츠 메뉴 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentsApiResponseVO extends CSResponseVOSupport {

	@Schema(description="콘텐츠 코드", example="CTS000001")
	private String contentsCode;
}
