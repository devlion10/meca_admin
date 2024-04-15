package kr.or.kpf.lms.repository.entity.key;

import lombok.*;

import javax.persistence.Column;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KeyOfCurriculumApplicationEvaluate implements Serializable {

    /** 교육 신청 일련 번호 */
    @Column(name="EDU_APLY_SN")
    private String applicationNo;

    /** 과정 코드 */
    @Column(name="CRCL_CD")
    private String curriculumCode;

    /** 강의 평가 일련 번호 */
    @Column(name="EVAL_SN")
    private String evaluateSerialNo;

    /** 강의 평가 질문 일련 번호 */
    @Column(name="QUES_SN")
    private String questionSerialNo;

    /** 로그인 아이디 */
    @Column(name = "LGN_ID")
    private String userId;
}
