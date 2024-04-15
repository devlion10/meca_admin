package kr.or.kpf.lms.biz.user.history.vo;

import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.*;

import javax.persistence.Column;

/**
 * 강의이력 화면 검색 객체
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class InstructorLctrViewRequestVO extends CSViewVOSupport {
    private String lectrNm;
    private String lctrNm;
    private String crsCls1Nm;
    private String crsCls2Nm;
    private String crsNm;
    private String crsNo;

}
