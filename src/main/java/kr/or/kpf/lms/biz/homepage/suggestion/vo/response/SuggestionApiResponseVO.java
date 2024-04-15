package kr.or.kpf.lms.biz.homepage.suggestion.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.converter.BooleanConverter;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Convert;
import java.math.BigInteger;

@Schema(name="SuggestionApiResponseVO", description="교육 주제 제안 API 관련 응답 객체")
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SuggestionApiResponseVO extends CSResponseVOSupport {

    /** 시퀀스 번호 */
    @Schema(description="시퀀스 번호")
    private BigInteger sequenceNo;

    /** 교육 주제 제안 타입(1: 언론인, 2: 시민) */
    @Schema(description="교육 주제 제안 타입(1: 언론인, 2: 시민)")
    private String suggestionType;

    /** 제안 내용 */
    @Schema(description="제안 내용")
    private String contents;

    /** 비밀글 여부 */
    @Schema(description="비밀글 여부")
    private Boolean isSecurity;

    /** 댓글 */
    @Schema(description="댓글")
    private String comment;

    /** 비밀댓글 여부 */
    @Schema(description="비밀댓글 여부")
    private Boolean commentSecurity;
}
