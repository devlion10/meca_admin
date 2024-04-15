package kr.or.kpf.lms.biz.contents.contents.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.contents.contents.vo.CreateContents;
import kr.or.kpf.lms.biz.contents.contents.vo.UpdateContents;
import kr.or.kpf.lms.common.support.OrderOfValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Schema(name="ContentsApiRequestVO", description="콘텐츠 메뉴 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ContentsApiRequestVO {
	
	@Schema(description="콘텐츠 코드", example="CTS000001")
	@NotEmpty(groups={UpdateContents.class}, message="콘텐츠 코드는 필수 입니다.")
    private String contentsCode;

	@Schema(description="콘텐츠 명", example="공개강좌 콘텐츠명")
	@NotEmpty(groups={CreateContents.class, UpdateContents.class}, message="콘텐츠 명은 필수 입니다.")
    private String contentsName;

	@Schema(description="콘텐츠 카테고리 코드 (1: 공개 강좌, 2: 직무 강좌)", example="1", allowableValues = {"1", "2"})
	@NotEmpty(groups={CreateContents.class, UpdateContents.class}, message="카테코리는 필수 입니다.")
    private String categoryCode;

	@Schema(description="사용 여부", defaultValue = "false")
    @NotNull(groups={UpdateContents.class}, message="사용 여부는 필수 입니다.")
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

    @Schema(description="챕터 목록", example="")
    private List<OrderOfValue> chapterList;
}
