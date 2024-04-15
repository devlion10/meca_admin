package kr.or.kpf.lms.biz.business.instructor.question.answer.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 강사 지원 문의 답변 관련 응답 객체
 */
@Schema(name="BizInstructorQuestionAnswerViewResponseVO", description="강사 지원 문의 답변 VIEW 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizInstructorQuestionAnswerViewResponseVO extends CSResponseVOSupport {

    @Schema(description="강사 지원 문의 답변 일련 번호", example="1")
    private String bizInstrQstnAnsNo;
}