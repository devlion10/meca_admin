package kr.or.kpf.lms.biz.user.instructor.qualification.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.user.instructor.qualification.controller.InstructorQualificationApiController;
import kr.or.kpf.lms.common.converter.DateYMDToStringConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.validation.constraints.NotNull;

@Schema(name="InstructorQualificationApiRequestVO", description="강사 관리 자격증 API 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InstructorQualificationApiRequestVO {

    @Schema(description="강사 자격증 일련 번호", example="")
    private Long sequenceNo;

    @Schema(description="강사 일련 번호", example="")
    @NotNull(groups={InstructorQualificationApiController.CreateInstructor.class, InstructorQualificationApiController.UpdateInstructor.class}, message="강사 일련 번호는 필수 입니다.")
    private Long instrSerialNo;

    /** 자격증 취득 일자 */
    @Convert(converter= DateYMDToStringConverter.class)
    @Schema(description="자격증 취득 일자", example="")
    private String instrQlfcYmd;

    /** 자격증 명 */
    @Schema(description="자격증 명", example="")
    private String instrQlfcNm;

    /** 자격증 번호 */
    @Schema(description="자격증 번호", example="")
    private String instrQlfcNo;

    /** 자격증 발급기관 */
    @Schema(description="자격증 발급기관", example="")
    private String instrQlfcInst;
}
