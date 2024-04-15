package kr.or.kpf.lms.biz.statistics.report.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="ReportEducationResponseVO", description="경영평가 보고서 - 교육별 엑셀 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportScheduleResponseVO {

    @Schema(description="개설 과정 코드")
    private String educationPlanCode;

    @Schema(description="담당자 아이디")
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

    @Schema(description="수업 코드")
    private String lectureCode;

    @Schema(description="수업명")
    private String lectureTitle;

    @Schema(description="강사명")
    private String instrNm;

    @Schema(description="강사소속")
    private String orgName;

    @Schema(description="강사코드")
    private Long instrSerialNo;

    /** 수강 인원 */
    private Long countUser;

    /** 교육일 */
    private String lectureDate;

    /** 교육시간 */
    private String lectureTime;

    /** 수업 시간 */
    private double eachTime;

    /** 총 시수 */
    private double totalTime;

}
