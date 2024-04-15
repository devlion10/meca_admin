package kr.or.kpf.lms.biz.business.instructor.vo.request;

import io.swagger.annotations.ApiModelProperty;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.*;

/**
 강사 모집 관련 요청 객체
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class BizInstructorViewRequestVO extends CSViewVOSupport {

    /** 강사 모집 일련 번호 */
    private String bizInstrNo;

    /** 강사 모집 제목 */
    private String bizInstrNm;

    /** 강사 모집 상태 */
    private Integer bizInstrStts;
}
