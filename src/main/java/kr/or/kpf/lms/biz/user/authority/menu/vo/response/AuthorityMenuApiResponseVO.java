package kr.or.kpf.lms.biz.user.authority.menu.vo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Schema(name="AuthorityApiResponseVO", description="권한 관리 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AuthorityMenuApiResponseVO extends CSResponseVOSupport {

    @Schema(description="일련 번호", example="")
    private BigInteger sequenceNo;
}
