package kr.or.kpf.lms.biz.education.application.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.education.application.controller.EducationApplicaitonApiController;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(name="EducationStateApiRequestVO", description="교육 현황 API 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EducationStateApiRequestVO {

    /** 교육 신청 일련번호 */
    @Schema(description="교육 신청 일련번호")
    @NotNull(groups={EducationApplicaitonApiController.SubmitSectionProgress.class}, message="교육 신청 일련번호는 필수 입니다.")
    private String applicationNo;

    /** 차시 코드 */
    @Schema(description="차시 코드")
    @NotNull(groups={EducationApplicaitonApiController.SubmitSectionProgress.class}, message="차시 코드는 필수 입니다.")
    private String chapterCode;

    /** 절 코드 */
    @Schema(description="절 코드")
    @NotNull(groups={EducationApplicaitonApiController.SubmitSectionProgress.class}, message="절 코드는 필수 입니다.")
    private String sectionCode;

    /** 절 진도율 */
    @Schema(description="절 진도율")
    @NotNull(groups={EducationApplicaitonApiController.SubmitSectionProgress.class}, message="절 진도율은 필수 입니다.")
    private Double sectionProgressRate;
}
