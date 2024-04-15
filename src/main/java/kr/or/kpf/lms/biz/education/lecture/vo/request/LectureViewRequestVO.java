package kr.or.kpf.lms.biz.education.lecture.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="LectureViewRequestVO", description="일반 교육 강의 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LectureViewRequestVO extends CSViewVOSupport {

    /** 교육 과정 개설 코드 */
    private String educationPlanCode;

}
