package kr.or.kpf.lms.repository.entity.key;

import lombok.*;

import javax.persistence.Column;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KeyOfCurriculumEvaluate implements Serializable {

    /** 과정 코드 */
    @Column(name="CRCL_CD")
    private String curriculumCode;

    /** 교육 평가 코드 */
    @Column(name="EVAL_SN")
    private String evaluateSerialNo;
}
