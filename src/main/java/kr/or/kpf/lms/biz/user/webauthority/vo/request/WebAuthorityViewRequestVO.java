package kr.or.kpf.lms.biz.user.webauthority.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Schema(name="WebAuthorityViewRequestVO", description="웹 회원 사업 참여 권한 관리 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class WebAuthorityViewRequestVO extends CSViewVOSupport {
    /** 일련 번호 */
    private BigInteger sequenceNo;
    /** 회원 ID */
    private String userId;
    /** 회원 명 */
    private String userName;
    /** 사업 참여 권한 */
    @Builder.Default
    private String businessAuthority = "INSTR";
    private String businessAuthoritys;
    /** 요청일 조회 일자 */
    private String applyBgngDate;
    private String applyEndDate;
    /** 처리일 조회 일자 */
    private String apprvBgngDate;
    private String apprvEndDate;
    /** 권한 승인 상태 */
    private String businessAuthorityApprovalState;
    /** 0 : organizationAuthorityHistory, 1 : individualAuthorityHistory */
    private int targetTable;
}
