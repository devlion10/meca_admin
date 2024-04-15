package kr.or.kpf.lms.biz.homepage.suggestion.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.homepage.suggestion.controller.SuggestionApiController;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Schema(name="SuggestionApiRequestVO", description="교육 주제 제안 API 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuggestionApiRequestVO {

    /** 시퀀스 번호 */
    @Schema(description="시퀀스 번호", required=true, example="")
    @NotNull(groups={SuggestionApiController.UpdateSuggestion.class, SuggestionApiController.CommentSuggestion.class}, message="시퀀스 번호는 필수 입니다.")
    private BigInteger sequenceNo;

    /** 제안 내용 */
    @Schema(description="제안 내용", required=true, example="")
    @NotNull(groups={SuggestionApiController.UpdateSuggestion.class}, message="제안 내용은 필수 입니다.")
    private String contents;

    /** 댓글 */
    @Schema(description="댓글", required=true, example="")
    @NotNull(groups={SuggestionApiController.CommentSuggestion.class}, message="댓글은 필수 입니다.")
    private String comment;

    /** 비밀댓글 여부 */
    @Schema(description="비밀댓글 여부", required=false, example="")
    private Boolean commentSecurity;
}
