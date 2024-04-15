package kr.or.kpf.lms.repository.entity.key;

import lombok.*;

import javax.persistence.Column;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KeyOfBizSurveyQitemItem implements Serializable {

    /** 상호평가 일련 번호 */
    @Column(name="BIZ_SURVEY_QITEM_ITEM_NO")
    private String bizSurveyQitemItemNo;

    /** 상호평가 문항 일련 번호 */
    @Column(name="BIZ_SURVEY_QITEM_NO")
    private String bizSurveyQitemNo;

}
