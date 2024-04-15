package kr.or.kpf.lms.biz.statistics.education.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="EducationStateRequestVO", description="학습 운영 통계 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EducationStateRequestVO extends CSViewVOSupport {

    @Schema(description="교육 방식", example="")
    private String educationType;

    @Schema(description="카테고리", example="")
    private String categoryCode;

    @Schema(description="예산", example="")
    private String enforcementType;
}
