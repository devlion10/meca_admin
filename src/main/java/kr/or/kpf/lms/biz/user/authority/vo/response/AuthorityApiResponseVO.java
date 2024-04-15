package kr.or.kpf.lms.biz.user.authority.vo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(name="AuthorityApiResponseVO", description="권한 관리 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AuthorityApiResponseVO extends CSResponseVOSupport {

    @Schema(description="권한 코드", example="")
    private String roleCode;

    @Schema(description="권한 명", example="")
    private String roleName;

    @Schema(description="권한 그룹 코드", example="")
    private String roleGroupCode;

    @Schema(description="권한 그룹 명", example="")
    private String roleGroupName;

    @Schema(description="권한 코드 리스트", example="")
    private List<String> roleCodeList;

    @Schema(description="메모", example="")
    private String memo;
}
