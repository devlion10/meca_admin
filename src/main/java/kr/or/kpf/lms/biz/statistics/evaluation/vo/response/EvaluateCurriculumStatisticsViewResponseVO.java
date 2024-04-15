package kr.or.kpf.lms.biz.statistics.evaluation.vo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 강의평가 통계 - 강의(과정)별 관련 요청 객체
 */
@Schema(name="EvaluateCurriculumStatisticsViewResponseVO", description="강의평가 통계 - 강의(과정)별 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EvaluateCurriculumStatisticsViewResponseVO extends CSViewVOSupport {

    @Schema(description="강의 평가 일련 번호")
    private String evaluateSerialNo;

    @Schema(description="교육 과정 코드", example="")
    private String curriculumCode;

    @ExcelColumn(headerName = "과정명")
    @Schema(description="교육 과정명", example="")
    private String curriculumName;

    @ExcelColumn(headerName = "교육 유형")
    @Schema(description="교육 과정 유형 (1: 화상 교육, 2: 집합 교육, 3: 이러닝 교육)", example="")
    private String educationType;

    @ExcelColumn(headerName = "카테고리")
    @Schema(description="교육 과정 카테고리", example="")
    private String categoryCode;

    @ExcelColumn(headerName = "수료자수")
    private Long countEnd;

    @ExcelColumn(headerName = "응답자수")
    private Long countAnswer;

    @ExcelColumn(headerName = "응답률")
    private String rateAnswer;
}