package kr.or.kpf.lms.biz.education.schedule.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="ScheduleApiResponseVO", description="교육 일정 관리 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleApiResponseVO extends CSResponseVOSupport {
    /** 교육 과정 개설 코드 */
    @Schema(description="교육 과정 개설 코드")
    private String educationPlanCode;
    /** 과정 코드 */
    @Schema(description="과정 코드")
    private String curriculumCode;
}
