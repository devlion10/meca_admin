package kr.or.kpf.lms.biz.system.menu.vo.request;

import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.*;

/**
 * 메뉴 관리 관련 요청 객체
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class MenuViewRequestVO extends CSViewVOSupport {

    /** 상위 메뉴 일련번호 */
    private Long topSequenceNo;
}
