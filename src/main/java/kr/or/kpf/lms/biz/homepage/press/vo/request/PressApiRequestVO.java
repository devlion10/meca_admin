package kr.or.kpf.lms.biz.homepage.press.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.homepage.press.vo.CreatePress;
import kr.or.kpf.lms.biz.homepage.press.vo.UpdatePress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Schema(name="PressApiRequestVO", description="행사소개")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PressApiRequestVO {

    /** 고유키 */
    @Schema(description="고유키", required=false, example="")
    private Long sequenceNo;

    /** 제목 */
    @Schema(description="제목", required=true, example="")
    @NotEmpty(groups={CreatePress.class, UpdatePress.class}, message="제목은 필수 입니다.")
    private String title;

    /** 내용 */
    @Schema(description="내용", required=true, example="")
    @NotEmpty(groups={CreatePress.class, UpdatePress.class}, message="내용은 필수 입니다.")
    private String contents;

    /** 썸네일 파일 경로 */
    @Schema(description="썸네일 파일 경로", required=true, example="")
    private String atchFilePath;

    /** 조회수 */
    @Schema(description="조회수", required=true, example="")
    private Long viewCount;

}
