package kr.or.kpf.lms.biz.homepage.archive.data.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.*;

import java.math.BigInteger;

@Schema(name="ArchiveApiResponseVO", description="자료실 관련 응답 객체")
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveApiResponseVO extends CSResponseVOSupport {

    /** 시퀀스 번호 */
    @Schema(description="시퀀스 번호")
    private Long sequenceNo;

}
