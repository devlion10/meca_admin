package kr.or.kpf.lms.biz.user.authority.menu.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Schema(name="AuthorityViewRequestVO", description="권한 관리 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AuthorityMenuViewRequestVO extends CSViewVOSupport {
    /** 일련 번호 */
    private Long sequenceNo;
    /** 권한 그룹 코드 */
    private String roleGroupCode;
    /** 메뉴 일련 번호 */
    private Long menuSequenceNo;
}
