package kr.or.kpf.lms.biz.contents.question.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Schema(name="EvaluateQuestionApiResponseVO", description="강의 평가 질문 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluateQuestionApiResponseVO extends CSResponseVOSupport {

    @Schema(description="강의 평가 질문 일련 번호", example="1")
    private String questionSerialNo;

    @Schema(description="강의 평가 질문 유형 코드", example="1")
    private String questionType;

    @Schema(description="강의 평가 질문 제목", example="")
    private String questionTitle;

    @Schema(description="강의 평가 질문 내용", example="")
    private String questionContents;

    @Schema(description="강의 평가 질문 문항", example="")
    List<String> questionItemList = new ArrayList<>();
}
