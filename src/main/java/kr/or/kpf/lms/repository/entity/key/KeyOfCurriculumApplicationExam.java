package kr.or.kpf.lms.repository.entity.key;

import lombok.*;

import javax.persistence.Column;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KeyOfCurriculumApplicationExam implements Serializable {

    /** 교육 신청 일련 번호 */
    @Column(name="EDU_APLY_SN")
    private String applicationNo;

    /** 과정 코드 */
    @Column(name="CRCL_CD")
    private String curriculumCode;

    /** 시험 일련 번호 */
    @Column(name="EXAM_SN", nullable = false)
    private String examSerialNo;

    /** 로그인 아이디 */
    @Column(name="LGN_ID")
    private String userId;
}
