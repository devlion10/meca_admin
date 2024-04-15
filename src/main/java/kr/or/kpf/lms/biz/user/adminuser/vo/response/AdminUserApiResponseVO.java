package kr.or.kpf.lms.biz.user.adminuser.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="AdminUserApiResponseVO", description="유저 API 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserApiResponseVO extends CSResponseVOSupport {

    @Schema(description="아이디", example="")
    private String userId;
}
