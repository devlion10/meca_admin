package kr.or.kpf.lms.biz.user.authority.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.user.authority.controller.AuthorityApiController;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;

@Schema(name="AuthorityApiRequestVO", description="권한 관리 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AuthorityApiRequestVO {

    @Schema(description="권한 그룹 코드", required = true, example="")
    @NotEmpty(groups={AuthorityApiController.CreateAuthorityGroup.class, AuthorityApiController.UpdateAuthorityGroup.class}, message="권한 그룹 코드는 필수 입니다.")
    private String roleGroupCode;

    @Schema(description="권한 그룹 명", required = true, example="")
    @NotEmpty(groups={AuthorityApiController.CreateAuthorityGroup.class, AuthorityApiController.UpdateAuthorityGroup.class}, message="권한 그룹 명은 필수 입니다.")
    private String roleGroupName;

    @Schema(description="메모", example="")
    private String memo;
}
