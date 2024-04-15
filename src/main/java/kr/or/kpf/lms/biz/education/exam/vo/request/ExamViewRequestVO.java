package kr.or.kpf.lms.biz.education.exam.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 시험 정보 관련 요청 객체
 */
@Schema(name="ExamViewRequestVO", description="시험 정보 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExamViewRequestVO extends CSViewVOSupport {
    /** 교육과정 코드 */
    private String curriculumCode;
    /** 시험 일련 번호 */
    private String examSerialNo;
    /** 시험 명 */
    private String examName;
}
