package kr.or.kpf.lms.biz.education.lecture.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.converter.DateToStringConverter;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Id;

@Schema(name="LectureApiResponseVO", description="일반 과정 관리 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureApiResponseVO extends CSResponseVOSupport {
    @Schema(description="강의 코드")
    private String lectureCode;

    @Schema(description="교육 과정 개설 코드")
    private String educationPlanCode;
}
