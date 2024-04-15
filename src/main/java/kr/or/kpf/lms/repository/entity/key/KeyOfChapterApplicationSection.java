package kr.or.kpf.lms.repository.entity.key;

import lombok.*;

import javax.persistence.Column;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KeyOfChapterApplicationSection implements Serializable {

    /** 교육 신청 일련 번호 */
    @Column(name="EDU_APLY_SN")
    private String applicationNo;

    /** 과정 코드 */
    @Column(name="CRCL_CD")
    private String curriculumCode;

    /** 콘텐츠 코드 */
    @Column(name="CTS_CD")
    private String contentsCode;

    /** 챕터 코드 */
    @Column(name="CHAP_CD")
    private String chapterCode;

    /** 절 코드 */
    @Column(name="SEC_CD")
    private String sectionCode;

    /** 로그인 아이디 */
    @Column(name="LGN_ID")
    private String userId;
}
