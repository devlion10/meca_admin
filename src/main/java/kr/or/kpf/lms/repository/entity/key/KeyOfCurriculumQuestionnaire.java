package kr.or.kpf.lms.repository.entity.key;

import lombok.*;

import javax.persistence.Column;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KeyOfCurriculumQuestionnaire implements Serializable {

    /** 과정 코드 */
    @Column(name="CRCL_CD")
    private String curriculumCode;

    /** 연계 설문 코드 */
    @Column(name="RFRN_QTN_CD")
    private String referenceQuestionnaireCode;
}
