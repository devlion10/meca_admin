package kr.or.kpf.lms.biz.contents.question.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="EvaluateQuestionViewRequestVO", description="강의 평가 질문 메뉴 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EvaluateQuestionViewRequestVO extends CSViewVOSupport {

    @Schema(description="강의 평가 질문 일련 번호")
    private String questionSerialNo;

    @Schema(description="강의 평가 질문 카테고리")
    private String questionCategory;

    @Schema(description="강의 평가 질문 유형 코드")
    private String questionType;

    @Schema(description="강의 평가 질문 제목")
    private String questionTitle;
}
