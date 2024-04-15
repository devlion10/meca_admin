package kr.or.kpf.lms.biz.education.exam.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.education.exam.controller.ExamApiController;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(name="ExamApiRequestVO", description="시험 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExamApiRequestVO {

    /** 교육과정 코드 */
    @Schema(description="교육과정 코드", required = true, example="")
    @NotEmpty(groups={ExamApiController.CreateExam.class, ExamApiController.UpdateExam.class}, message="교육과정 코드는 필수 입니다.")
    private String curriculumCode;

    /** 시험 코드 */
    @Schema(description="시험 코드", required = true, example="")
    @NotEmpty(groups={ExamApiController.UpdateExam.class}, message="시험 코드는 필수 입니다.")
    private String examSerialNo;

    /** 시험명 */
    @Schema(description="시험명", required = true, example="")
    @NotEmpty(groups={ExamApiController.CreateExam.class, ExamApiController.UpdateExam.class}, message="시험명는 필수 입니다.")
    private String examName;

    /** 시험시간(분단위) */
    @Schema(description="시험시간(분단위)", required = true, example="")
    @NotNull(groups={ExamApiController.CreateExam.class, ExamApiController.UpdateExam.class}, message="시험시간(분단위)는 필수 입니다.")
    private Integer examMinute;

    /** 시험내용 */
    @Schema(description="시험내용", required = true, example="")
    @NotEmpty(groups={ExamApiController.CreateExam.class, ExamApiController.UpdateExam.class}, message="시험내용는 필수 입니다.")
    private String examContents;

    /** 집계 문제 수 */
    @Schema(description="집계 문제 수", required = true, example="")
    @NotNull(groups={ExamApiController.CreateExam.class, ExamApiController.UpdateExam.class}, message="집계 문제 수는 필수 입니다.")
    private Integer questionTotalCount;

    /** 등급 1 선택 문제 수 */
    @Schema(description="등급 1 선택 문제 수", required = true, example="")
    private Integer gradeFirstSelectQuestionCount;

    /** 등급 2 선택 문항 수 */
    @Schema(description="등급 2 선택 문항 수", required = true, example="")
    private Integer gradeSecondSelectQuestionCount;

    /** 등급 3 선택 문항 수 */
    @Schema(description="등급 3 선택 문항 수", required = true, example="")
    private Integer gradeThirdSelectQuestionCount;

    /** 등급 1 주관식 문항 수 */
    @Schema(description="등급 1 주관식 문항 수", required = true, example="")
    private Integer gradeFirstSubExQuestionCount;

    /** 등급 2 주관식 문항 수 */
    @Schema(description="등급 2 주관식 문항 수", required = true, example="")
    private Integer gradeSecondSubExQuestionCount;

    /** 등급 3 주관식 문항 수 */
    @Schema(description="등급 3 주관식 문항 수", required = true, example="")
    private Integer gradeThirdSubExQuestionCount;

    /** 등급 1 배점 점수 */
    @Schema(description="등급 1 배점 점수", required = true, example="")
    private Integer gradeFirstQuestionScore;

    /** 등급 2 배점 점수 */
    @Schema(description="등급 2 배점 점수", required = true, example="")
    private Integer gradeSecondQuestionScore;

    /** 등급 3 배점 점수 */
    @Schema(description="등급 3 배점 점수", required = true, example="")
    private Integer gradeThirdQuestionScore;

    /** 선택 문항 무작위 여부 */
    @Schema(description="선택 문항 무작위 여부", required = true, example="")
    @NotNull(groups={ExamApiController.CreateExam.class, ExamApiController.UpdateExam.class}, message="선택 문항 무작위 여부는 필수 입니다.")
    private Boolean isSelectQuestionRandom;

    /** 사용 여부 */
    @Schema(description="사용 여부", required = true, example="")
    @NotNull(groups={ExamApiController.CreateExam.class, ExamApiController.UpdateExam.class}, message="사용 여부는 필수 입니다.")
    private Boolean isUsable;

}
