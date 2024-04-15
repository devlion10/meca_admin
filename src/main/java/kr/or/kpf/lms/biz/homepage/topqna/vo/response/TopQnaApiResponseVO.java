package kr.or.kpf.lms.biz.homepage.topqna.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.*;

import java.math.BigInteger;

@Schema(name="TopQnaApiResponseVO", description="자주묻는 질문 API 관련 응답 객체")
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TopQnaApiResponseVO extends CSResponseVOSupport {

    /** 시퀀스 번호 */
    @Schema(description="시퀀스 번호", example="")
    private BigInteger sequenceNo;

    /** 질문 */
    @Schema(description="질문", example="")
    private String question;

    /** 답변 */
    @Schema(description="답변", example="")
    private String answer;
}
