package kr.or.kpf.lms.biz.homepage.press.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.*;

@Schema(name="PressApiResponseVO", description="행사소개 관련 응답 객체")
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PressApiResponseVO extends CSResponseVOSupport {

    /** 시퀀스 번호 */
    @Schema(description="시퀀스 번호")
    private Long sequenceNo;

}
