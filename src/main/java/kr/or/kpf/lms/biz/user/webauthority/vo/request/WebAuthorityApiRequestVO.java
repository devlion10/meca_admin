package kr.or.kpf.lms.biz.user.webauthority.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.user.webauthority.controller.WebAuthorityApiController;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Schema(name="WebAuthorityApiRequestVO", description="웹 회원 사업 참여 권한 관리 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class WebAuthorityApiRequestVO {

    @Schema(description="시퀀스 번호", required = true, example="")
    @NotNull(groups={WebAuthorityApiController.UpdateWebAuthority.class}, message="시퀀스 번호은 필수 입니다.")
    private BigInteger sequenceNo;

    @Schema(description="회원 아이디", required = true, example="")
    @NotEmpty(groups={WebAuthorityApiController.UpdateWebAuthority.class, WebAuthorityApiController.DeleteWebAuthority.class}, message="회원 아이디은 필수 입니다.")
    private String userId;

    @Schema(description="사업 참여 권한 타입", required = true, example="")
    @NotEmpty(groups={WebAuthorityApiController.UpdateWebAuthority.class, WebAuthorityApiController.DeleteWebAuthority.class}, message="사업 참여 권한 타입은 필수 입니다.")
    private String businessAuthority;

    @Schema(description="사업 참여 권한 신청 상태", required = true, example="")
    @NotEmpty(groups={WebAuthorityApiController.UpdateWebAuthority.class}, message="사업 참여 권한 신청 상태은 필수 입니다.")
    private String businessAuthorityApprovalState;
}
