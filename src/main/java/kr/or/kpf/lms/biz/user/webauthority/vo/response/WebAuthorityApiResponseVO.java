package kr.or.kpf.lms.biz.user.webauthority.vo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Schema(name="WebAuthorityApiResponseVO", description="웹 회원 사업 참여 권한 관리 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class WebAuthorityApiResponseVO extends CSResponseVOSupport {

    @Schema(description="시퀀스 번호", example="")
    private BigInteger sequenceNo;

    @Schema(description="회원 아이디", example="")
    private String userId;

    @Schema(description="사업 참여 권한 타입", example="")
    private String businessAuthority;

    @Schema(description="사업 참여 권한 신청 상태", example="")
    private String businessAuthorityApprovalState;
}
