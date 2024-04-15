package kr.or.kpf.lms.biz.contents.contents.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.contents.contents.vo.CreateContents;
import kr.or.kpf.lms.biz.contents.contents.vo.UpdateContents;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(name="ContentsViewRequestVO", description="콘텐츠 메뉴 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ContentsViewRequestVO extends CSViewVOSupport {
	
	@Schema(description="콘텐츠 코드", example="CTS000001")
    private String contentsCode;

	@Schema(description="콘텐츠 명", example="공개강좌 콘텐츠명")
    private String contentsName;

	@Schema(description="콘텐츠 카테고리 코드 (1: 언론인연수, 2: 미디어교육, 3: 공통)", example="1")
    private String categoryCode;

	@Schema(description="사용 여부", example="true")
    private Boolean isUsable;

	@Schema(description="콘텐츠 제공 업체 코드", example="")
    private String organizationCode;

    @Schema(description="파일 너비", example="1024")
    private Integer width;

    @Schema(description="파일 높이", example="1024")
    private Integer height;

    @Schema(description="교육 시간 (분단위)", example="90")
    private Integer educationPerMinute;

    @Schema(description="비고 내용", example="메모입니다.")
    private String memo;
}
