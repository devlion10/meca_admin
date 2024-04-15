package kr.or.kpf.lms.biz.statistics.evaluation.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 강의 평가 - 설문지별 관련 요청 객체
 */
@Schema(name="EvaluateQuestionStatisticsRequestVO", description="강의 평가 - 설문지별 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EvaluateQuestionStatisticsRequestVO extends CSViewVOSupport {

    @Schema(description="제목", example="")
    private String evaluateTitle;

    @Schema(description="강의 평가 타입", example="")
    private String evaluateType;

    @Schema(description = "강의 평가 일련 번호", example = "")
    private String evaluateSerialNo;
}
