package kr.or.kpf.lms.biz.statistics.evaluation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluateDetailStatisticsItemVO {

    /** 강의 평가 질문 문항 */
    private String questionItemValue;

    /** 문항 선택 수 */
    private Integer count;
}
