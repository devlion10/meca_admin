package kr.or.kpf.lms.biz.statistics.evaluation.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 강의 평가 - 강좌(과정)별 관련 요청 객체
 */
@Schema(name="EvaluateCurriculumStatisticsRequestVO", description="강의 평가 - 강좌(과정)별 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EvaluateCurriculumStatisticsRequestVO extends CSViewVOSupport {

    @Schema(description="교육 과정 유형 (1: 화상 교육, 2: 집합 교육, 3: 이러닝 교육)", example="")
    private String educationType;

    @Schema(description="교육 과정 카테고리", example="")
    private String categoryCode;

    @Schema(description="과정 코드")
    private String curriculumCode;

    @Schema(description="과정명", example="")
    private String curriculumName;
}
