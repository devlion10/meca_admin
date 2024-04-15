package kr.or.kpf.lms.biz.contents.question.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.contents.question.controller.EvaluateQuestionApiController;
import kr.or.kpf.lms.common.support.OrderOfValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Schema(name="EvaluateQuestionApiRequestVO", description="강의 평가 질문 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EvaluateQuestionApiRequestVO {
    
    @Schema(description="강의 평가 질문 일련 번호", required = true, example="1")
    @NotEmpty(groups={EvaluateQuestionApiController.UpdateEvaluateQuestion.class}, message="강의 평가 질문 일련 번호는 필수 입니다.")
    private String questionSerialNo;

    @Schema(description="강의평가 질문 카테고리", required = true, example="1")
    @NotEmpty(groups={EvaluateQuestionApiController.CreateEvaluateQuestion.class, EvaluateQuestionApiController.UpdateEvaluateQuestion.class}, message="강의평가 질문 카테고리는 필수 입니다.")
    private String questionCategory;

    @Schema(description="질문 유형 코드", required = true, example="1")
    @NotEmpty(groups={EvaluateQuestionApiController.CreateEvaluateQuestion.class, EvaluateQuestionApiController.UpdateEvaluateQuestion.class}, message="질문 유형 코드는 필수 입니다.")
    private String questionType;

    @Schema(description="질문 제목", required = true, example="어제의 날씨는 어땠나요?")
    @NotEmpty(groups={EvaluateQuestionApiController.CreateEvaluateQuestion.class, EvaluateQuestionApiController.UpdateEvaluateQuestion.class}, message="질문 제목은 필수 입니다.")
    private String questionTitle;

    @Schema(description="질문 내용")
    private String questionContents;

    @Schema(description="강의 평가 질문 문항")
    List<OrderOfValue> questionItemList;
}
