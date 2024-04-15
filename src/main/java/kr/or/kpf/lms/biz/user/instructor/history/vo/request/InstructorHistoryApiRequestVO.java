package kr.or.kpf.lms.biz.user.instructor.history.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.business.instructor.aply.vo.CreateBizInstructorAply;
import kr.or.kpf.lms.biz.business.instructor.aply.vo.UpdateBizInstructorAply;
import kr.or.kpf.lms.biz.user.instructor.controller.InstructorApiController;
import kr.or.kpf.lms.biz.user.instructor.history.controller.InstructorHistoryApiController;
import kr.or.kpf.lms.common.converter.DateYMDToStringConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(name="InstructorHistoryApiRequestVO", description="강사 관리 주요 이력 API 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InstructorHistoryApiRequestVO {

    @Schema(description="강사 주요 이력 일련 번호", example="")
    private Long sequenceNo;

    @Schema(description="강사 일련 번호", example="")
    @NotNull(groups={InstructorHistoryApiController.CreateInstructor.class, InstructorHistoryApiController.UpdateInstructor.class}, message="강사 일련 번호는 필수 입니다.")
    private Long instrSerialNo;

    /** 주요 이력 연도 */
    @Schema(description="주요 이력 연도", example="")
    private String instrHistYr;

    /** 주요 이력 기관명 */
    @Schema(description="주요 이력 기관명", example="")
    private String instrOrgNm;

    /** 주요 이력 내용 */
    @Schema(description="주요 이력 내용", example="")
    private String instrCn;
}
