package kr.or.kpf.lms.biz.user.instructor.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.user.instructor.controller.InstructorApiController;
import kr.or.kpf.lms.biz.user.instructor.history.vo.request.InstructorHistoryApiRequestVO;
import kr.or.kpf.lms.biz.user.instructor.qualification.vo.request.InstructorQualificationApiRequestVO;
import kr.or.kpf.lms.common.converter.DateYMDToStringConverter;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.*;

import javax.persistence.Convert;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class InstructorLctrApiRequestVO extends CSViewVOSupport {

    @Schema(description="강사일련번호", required = false, example="")
    private Integer lectrNo;

}
