package kr.or.kpf.lms.biz.homepage.event.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigInteger;

@Schema(name="EventViewResponseVO", description="이벤트/설문 관련 응답 객체")
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class EventViewResponseVO {

    /** 시퀀스 번호 */
    @Schema(description="시퀀스 번호")
    private Long sequenceNo;

}
