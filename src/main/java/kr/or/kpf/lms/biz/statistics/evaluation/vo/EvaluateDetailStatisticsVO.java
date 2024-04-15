package kr.or.kpf.lms.biz.statistics.evaluation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluateDetailStatisticsVO {

    /** 강의 평가 일련 번호 */
    private String evaluateSerialNo;

    /** 강의 평가 제목 */
    private String evaluateTitle;

    /** 강의 평가 타입 */
    private String evaluateType;

    /** 강의 평가 질문 유형 코드 (1: 단일선택, 2: 다중선택, 3: 단답형, 4: 서술형) */
    private String questionType;

    /** 강의 평가 질문 일련 번호 */
    private String questionSerialNo;

    /** 강의 평가 질문 제목 */
    private String questionTitle;

    /** 강의 평가 질문 선택 문항 정보 */
    private List<EvaluateDetailStatisticsItemVO> evaluateStatisticsItems;
}
