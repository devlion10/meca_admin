package kr.or.kpf.lms.repository.entity.key;

import lombok.*;

import javax.persistence.Column;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KeyOfBizInstructorQuestion implements Serializable {
    /** 강사 지원 문의 일련 번호 */
    @Column(name="BIZ_INSTR_QSTN_NO")
    private String bizInstrQstnNo;

    /** 강사 지원 문의 답변 일련 번호 */
    @Column(name="BIZ_INSTR_QSTN_ANS_NO")
    private String bizInstrQstnAnsNo;
}
