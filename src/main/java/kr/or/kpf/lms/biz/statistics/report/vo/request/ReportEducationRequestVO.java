package kr.or.kpf.lms.biz.statistics.report.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.*;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class ReportEducationRequestVO extends CSViewVOSupport {

    @Schema(description="개설 과정 코드")
    private String educationPlanCode;

    @Schema(description="지사 구분")
    private String province;

    @Schema(description="교육 구분")
    private String category;

    @Schema(description="교육 유형")
    private String type;

    @Schema(description="과정명")
    private String cName;

    @Schema(description="담당자")
    private String mName;

    @Schema(description="연도")
    private String year;
}
