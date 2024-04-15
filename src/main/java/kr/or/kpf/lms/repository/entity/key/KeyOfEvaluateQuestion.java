package kr.or.kpf.lms.repository.entity.key;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KeyOfEvaluateQuestion implements Serializable {

    /** 강의평가 일련 번호 */
    @Column(name="EVAL_SN")
    private String evaluateSerialNo;

    /** 강의평가 질문 일련 번호 */
    @Column(name="QUES_SN")
    private String questionSerialNo;
}