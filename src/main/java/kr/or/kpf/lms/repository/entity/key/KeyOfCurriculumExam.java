package kr.or.kpf.lms.repository.entity.key;

import lombok.*;

import javax.persistence.Column;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KeyOfCurriculumExam implements Serializable {

    /** 과정 코드 */
    @Column(name="CRCL_CD")
    private String curriculumCode;

    /** 연계 시험 일련 번호 */
    @Column(name="EXAM_SN")
    private String examSerialNo;
}
