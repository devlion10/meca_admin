package kr.or.kpf.lms.biz.homepage.page.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.*;

import java.math.BigInteger;

/**
 * 페이지 VIEW 관련 응답 객체
 */
@Schema(name="PageViewResponseVO", description="페이지 VIEW 관련  ")
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PageViewResponseVO extends CSResponseVOSupport {

    /** 시퀀스 번호 */
    @Schema(description="시퀀스 번호")
    private BigInteger sequenceNo;
}
