package kr.or.kpf.lms.biz.education.question.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="ExamQuestionViewRequestVO", description="시험 문제 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExamQuestionViewRequestVO extends CSViewVOSupport {

    /** 검색어 범위 */
    private String containTextType;

    /** 검색에 포함할 단어 */
    private String containText;

    /** 교육과정 코드 */
    private String curriculumCode;

    /** 교육과정 시험 문제 코드 */
    private String questionSerialNo;

    /** 난이도 */
    private String questionLevel;

    /** 상태 */
    private Boolean isUsable;

    /** 문제 유형 */
    private String questionType;
}
