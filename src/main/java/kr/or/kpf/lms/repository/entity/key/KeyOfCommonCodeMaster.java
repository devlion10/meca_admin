package kr.or.kpf.lms.repository.entity.key;

import lombok.*;

import javax.persistence.Column;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KeyOfCommonCodeMaster implements Serializable {
    /** 개별 코드 */
    @Column(name="INDIV_CD")
    private String individualCode;

    /** 코드 */
    @Column(name="CD")
    private String code;
}
