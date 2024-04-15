package kr.or.kpf.lms.biz.statistics.webuser.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.*;

@Schema(name="UserStateRequestVO", description="이용 통계 관리 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserStateRequestVO extends CSViewVOSupport {
    /** 조회 연 */
    private String searchYear;
    /** 조회 월 */
    private String searchMonth;
}
