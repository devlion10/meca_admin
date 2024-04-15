package kr.or.kpf.lms.repository.entity.key;

import lombok.*;

import javax.persistence.Column;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KeyOfContentsOfCurriculum implements Serializable {

    /** 과정 코드 */
    @Column(name="CRCL_CD")
    private String curriculumCode;

    /** 콘텐츠 코드 */
    @Column(name="CTS_CD")
    private String contentsCode;
}
