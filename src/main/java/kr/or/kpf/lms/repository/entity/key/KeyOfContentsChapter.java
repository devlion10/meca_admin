package kr.or.kpf.lms.repository.entity.key;

import lombok.*;

import javax.persistence.Column;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KeyOfContentsChapter implements Serializable {

    /** 콘텐츠 코드 */
    @Column(name="CTS_CD")
    private String contentsCode;

    /** 챕터 코드 */
    @Column(name="CHAP_CD")
    private String chapterCode;
}
