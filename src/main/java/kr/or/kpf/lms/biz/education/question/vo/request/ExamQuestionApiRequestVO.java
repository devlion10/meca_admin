package kr.or.kpf.lms.biz.education.question.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.education.question.controller.ExamQuestionApiController;
import kr.or.kpf.lms.biz.education.question.vo.ExamQuestionItemVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(name="ExamQuestionApiRequestVO", description="시험 문제 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExamQuestionApiRequestVO {

    @Schema(description="교육과정 코드", required = true, example="")
    @NotEmpty(groups={ExamQuestionApiController.CreateExamQuestion.class, ExamQuestionApiController.UpdateExamQuestion.class}, message="교육과정 코드는 필수 입니다.")
    private String curriculumCode;

    @Schema(description="시험 문제 일련 번호", required = true, example="1")
    @NotEmpty(groups={ExamQuestionApiController.UpdateExamQuestion.class}, message="시험 문제 일련 번호는 필수 입니다.")
    private String questionSerialNo;

    @Schema(description="문제 유형 코드", required = true, example="1")
    @NotEmpty(groups={ExamQuestionApiController.CreateExamQuestion.class, ExamQuestionApiController.UpdateExamQuestion.class}, message="문제 유형 코드는 필수 입니다.")
    private String questionType;

    @Schema(description="문제 제목", required = true, example="어제의 날씨는 어땠나요?")
    @NotEmpty(groups={ExamQuestionApiController.CreateExamQuestion.class, ExamQuestionApiController.UpdateExamQuestion.class}, message="문제 제목은 필수 입니다.")
    private String questionTitle;

    @Schema(description="등급 번호", required = true, example="1")
    @NotNull(groups={ExamQuestionApiController.CreateExamQuestion.class, ExamQuestionApiController.UpdateExamQuestion.class}, message="등급 번호는 필수 입니다.")
    private String questionLevel;

    @Schema(description="사용 여부", required = true, example="1")
    @NotNull(groups={ExamQuestionApiController.CreateExamQuestion.class, ExamQuestionApiController.UpdateExamQuestion.class}, message="사용 여부는 필수 입니다.")
    private Boolean isUsable;

    @Schema(description="문제 내용", required = true, example="")
    private String questionContents;

    @Schema(description="정답 설명", required = true, example="")
    private String correctAnswerDescription;

    @Schema(description="시험 문제 문항", required = true, example="")
    @NotEmpty(groups={ExamQuestionApiController.CreateExamQuestion.class, ExamQuestionApiController.UpdateExamQuestion.class}, message="시험 문제 문항는 필수 입니다.")
    List<ExamQuestionItemVO> examQuestionItemList;
}
