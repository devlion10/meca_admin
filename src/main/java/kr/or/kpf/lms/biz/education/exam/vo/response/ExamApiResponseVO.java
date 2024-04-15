package kr.or.kpf.lms.biz.education.exam.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="ExamApiResponseVO", description="시험 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamApiResponseVO extends CSResponseVOSupport {

    /** 시험 일련 번호 */
    @Schema(description="EXAM_SN", example="")
    private String examSerialNo;

    /** 시험명 */
    @Schema(description="시험명", example="")
    private String examName;

    /** 시험시간 */
    @Schema(description="시험시간", example="")
    private Integer examHour;

    /** 시험내용 */
    @Schema(description="시험내용", example="")
    private String examContents;

    /** 집계 문제 수 */
    @Schema(description="집계 문제 수", example="")
    private Integer questionCount;

    /** 등급 1 선택 문제 수 */
    @Schema(description="등급 1 선택 문제 수", example="")
    private Integer gradeFirstSelectQuestionCount;

    /** 등급 2 선택 문항 수 */
    @Schema(description="등급 2 선택 문항 수", example="")
    private Integer gradeSecondSelectQuestionCount;

    /** 등급 3 선택 문항 수 */
    @Schema(description="등급 3 선택 문항 수", example="")
    private Integer gradeThirdSelectQuestionCount;

    /** 등급 1 주관식 문항 수 */
    @Schema(description="등급 1 주관식 문항 수", example="")
    private Integer gradeFirstSubExQuestionCount;

    /** 등급 2 주관식 문항 수 */
    @Schema(description="등급 2 주관식 문항 수", example="")
    private Integer gradeSecondSubExQuestionCount;

    /** 등급 3 주관식 문항 수 */
    @Schema(description="등급 3 주관식 문항 수", example="")
    private Integer gradeThirdSubExQuestionCount;

    /** 등급 1 배점 점수 */
    @Schema(description="등급 1 배점 점수", example="")
    private Integer gradeFirstQuestionScore;

    /** 등급 2 배점 점수 */
    @Schema(description="등급 2 배점 점수", example="")
    private Integer gradeSecondQuestionScore;

    /** 등급 3 배점 점수 */
    @Schema(description="등급 3 배점 점수", example="")
    private Integer gradeThirdQuestionScore;

    /** 선택 문항 무작위 여부 */
    @Schema(description="선택 문항 무작위 여부", example="")
    private Boolean isSelectQuestionRandom;

    /** 사용 여부 */
    @Schema(description="사용 여부", example="")
    private Boolean isUsable;

    /** 재 응시 수 */
    @Schema(description="재 응시 수", example="")
    private Integer reExamCount;
}
