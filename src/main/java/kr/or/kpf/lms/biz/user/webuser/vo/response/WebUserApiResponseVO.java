package kr.or.kpf.lms.biz.user.webuser.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="WebUserApiResponseVO", description="웹 회원 관리 API 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebUserApiResponseVO extends CSResponseVOSupport {

    @Schema(description="회원 아이디", example="")
    private String userId;

    @Schema(description="권한 그룹", example="")
    private String roleGroup;

    @Schema(description="사업 참여 권한", example="")
    private String businessAuthority;

    @Schema(description="성별 코드", example="")
    private String gender;

    @Schema(description="회원 연락처", example="")
    private String phone;

    @Schema(description="SMS 수신 여부", example="")
    private Boolean isSmsAgree;

    @Schema(description="이메일", example="")
    private String email;

    @Schema(description="이메일 수신 여부", example="")
    private Boolean isEmailAgree;

    @Schema(description="회원 상태", example="")
    private String state;

    @Schema(description="회원승인여부", example="")
    private String approFlag;
}
