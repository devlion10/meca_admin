package kr.or.kpf.lms.biz.statistics.privacy.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="PrivacyRequestVO", description="개인정보 수정 이력 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PrivacyRequestVO extends CSViewVOSupport {
    /** 검색어 범위 */
    @ApiModelProperty(name="containTextType", value="검색어 범위", required=false, dataType="string", position=10, example = "")
    private String containTextType;
    /** 검색에 포함할 단어 */
    @ApiModelProperty(name="containText", value="검색에 포함할 단어", required=false, dataType="string", position=20, example = "")
    private String containText;

    @Schema(description="대상자", example="")
    private String target;

    @Schema(description="취급자", example="")
    private String user;
}
