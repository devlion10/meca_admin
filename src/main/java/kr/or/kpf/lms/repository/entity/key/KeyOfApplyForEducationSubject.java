package kr.or.kpf.lms.repository.entity.key;

import lombok.*;

import javax.persistence.Column;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KeyOfApplyForEducationSubject implements Serializable {

    /** 사용자 일련 번호 */
    @Column(name = "USER_SN", nullable = false)
    private Long userSerialNo;

    /** 과목 요청 시각 */
    @Column(name = "CRS_DMND_TM", nullable = false)
    private String applyCurriculumTime;

    /** 과목 코드 */
    @Column(name="SBJC_CD", nullable = false)
    private String subjectCode;
}
