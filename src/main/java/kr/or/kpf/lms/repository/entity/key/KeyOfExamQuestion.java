package kr.or.kpf.lms.repository.entity.key;

import lombok.*;

import javax.persistence.Column;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KeyOfExamQuestion implements Serializable {
    /** 교육과정 코드 */
    @Column(name="CRCL_CD")
    private String curriculumCode;

    /** 시험 일련번호 */
    @Column(name="EXAM_SN")
    private String examSerialNo;

    /** 시험 문제 일련번호 */
    @Column(name="QUES_SN")
    private String questionSerialNo;
}