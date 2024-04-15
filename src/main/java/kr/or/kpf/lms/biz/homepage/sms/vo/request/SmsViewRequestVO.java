package kr.or.kpf.lms.biz.homepage.sms.vo.request;

import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.*;

/**
 * SMS 관리 관련 요청 객체
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class SmsViewRequestVO extends CSViewVOSupport {
    /** 발신인 */
    private String sender;
    /** 내용 */
    private String content;
}
