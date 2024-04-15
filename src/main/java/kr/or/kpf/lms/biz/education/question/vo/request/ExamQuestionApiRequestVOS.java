package kr.or.kpf.lms.biz.education.question.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

@Schema(name="ExamQuestionApiRequestVO", description="시험 문제 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExamQuestionApiRequestVOS {

    @Valid
    List<ExamQuestionApiRequestVO> examQuestions;
}
