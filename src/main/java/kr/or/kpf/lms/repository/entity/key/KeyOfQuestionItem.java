package kr.or.kpf.lms.repository.entity.key;

import lombok.*;

import javax.persistence.Column;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KeyOfQuestionItem implements Serializable {

    /** 일련 번호 */
    @Column(name="QUES_SN")
    private String questionSerialNo;

    /** 시험 문제 문항 일련 번호 */
    @Column(name="ITEM_SN")
    private String questionItemSerialNo;
}
