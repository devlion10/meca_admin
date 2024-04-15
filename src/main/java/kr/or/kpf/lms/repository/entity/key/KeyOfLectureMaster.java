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
public class KeyOfLectureMaster implements Serializable {
    /** 강의 코드 */
    @Column(name="LECTURE_CD")
    private String lectureCode;

    /** 교육 계획 코드 */
    @Column(name="PLAN_CD")
    private String educationPlanCode;
}
