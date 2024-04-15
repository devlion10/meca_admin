package kr.or.kpf.lms.biz.user.history.vo;

import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.*;

/**
 * 수강이력 화면 검색 객체
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class JournalismschoolViewRequestVO extends CSViewVOSupport {
    /** 강의명 */
    private String crsNm;
    /** 강의대분류 */
    private String crsCls1Nm;
    /** 강의소분류 */
    private String crsCls2Nm;
    /** 신청자 이름 */
    private String userNm;
    /** 신청자 아이디 */
    private String userId;
    /** 매체사명 */
    private String mdiaNm;
    /** 매체명 */
    private String mdiaDNm;

    /** 수료일자 */
    private String cmpltDt2;
}
