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
 강의평가 통계 - 설문지별 관련 요청 객체
 */
@Schema(name="EvaluateQuestionStatisticsViewResponseVO", description="강의평가 통계 - 설문지별 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EvaluateQuestionStatisticsViewResponseVO extends CSViewVOSupport {

    @Schema(description="강의 평가 일련 번호")
    private String evaluateSerialNo;

    @ExcelColumn(headerName = "강의 평가 제목")
    @Schema(description="강의 평가 제목")
    private String evaluateTitle;

    @ExcelColumn(headerName = "강의 평가 타입")
    @Schema(description="강의 평가 타입")
    private String evaluateType;

    @Schema(description="기타의견 사용 여부")
    private Boolean isUsableOtherComments;

    @ExcelColumn(headerName = "응답자 수")
    private Long countAnswer;
}