package kr.or.kpf.lms.biz.system.code.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="CommonCodeApiRequestVO", description="공통 코드 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonCodeApiResponseVO extends CSResponseVOSupport {

    @Schema(description="개별 코드", required = true, example="CODE00001")
    private String individualCode;
}
