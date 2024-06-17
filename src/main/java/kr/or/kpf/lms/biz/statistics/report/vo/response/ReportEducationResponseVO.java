package kr.or.kpf.lms.biz.statistics.report.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="ReportEducationResponseVO", description="경영평가 보고서 - 교육별 엑셀 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportEducationResponseVO {

    @Schema(description="개설 과정 코드")
    private String educationPlanCode;

    @Schema(description="회계구분")
    private String enforcementType;

    @Schema(description="본/지사")
    private String managerDepartment;

    @Schema(description="본/지사 코드")
    private String province;

    @Schema(description="담당자")
    private String managerName;

    @Schema(description="강의 구분")
    private String categoryCode;

    @Schema(description="교육 과정 유형 (1: 화상 교육, 2: 집합 교육, 3: 이러닝 교육)")
    private String educationType;

    @Schema(description="교육 과정명")
    private String curriculumName;

    @Schema(description="교육시작일")
    private String operationBeginDateTime;

    @Schema(description="교육종료일")
    private String operationEndDateTime;

    /** 수강 인원 */
    private Long countUser;

    /** 수료 인원 */
    private Long countEndUser;

    /** 병행교육시 집합강의 인원 */
    private Long countUserOfParallel;

}
