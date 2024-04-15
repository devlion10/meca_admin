package kr.or.kpf.lms.repository.entity.key;

import lombok.*;

import javax.persistence.Column;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KeyOfApplyForEducationCurriculum implements Serializable {

    /** 사용자 일련 번호 */
    @Column(name = "USER_SN", nullable = false)
    private Long userSerialNo;

    /** 과정 요청 시각 */
    @Column(name = "CRS_DMND_TM", nullable = false)
    private String applyCurriculumTime;
}
