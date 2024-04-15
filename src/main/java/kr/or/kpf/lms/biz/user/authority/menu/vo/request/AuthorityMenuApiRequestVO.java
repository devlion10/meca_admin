package kr.or.kpf.lms.biz.user.authority.menu.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.user.authority.menu.controller.AuthorityMenuApiController;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.math.BigInteger;

@Schema(name="AuthorityMenuApiRequestVO", description="권한 관리 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AuthorityMenuApiRequestVO {
    
    @Schema(description="권한 그룹 코드", required = true, example="")
    @NotEmpty(groups={AuthorityMenuApiController.CreateAuthorityMenu.class}, message="권한 그룹 코드는 필수 입니다.")
    private String roleGroupCode;

    @Schema(description="메뉴 일련 번호", required = false, example="")
    @NotEmpty(groups={AuthorityMenuApiController.CreateAuthorityMenu.class}, message="메뉴 일련 번호는 필수 입니다.")
    private Long menuSequenceNo;

    @Schema(description="메뉴 링크", required = false, example="")
    private String uri;
}
