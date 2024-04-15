package kr.or.kpf.lms.biz.homepage.press.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigInteger;

@Schema(name="PressViewResponseVO", description="행사소개 관련 응답 객체")
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PressViewResponseVO {

    /** 시퀀스 번호 */
    @Schema(description="시퀀스 번호")
    private Long sequenceNo;

}
