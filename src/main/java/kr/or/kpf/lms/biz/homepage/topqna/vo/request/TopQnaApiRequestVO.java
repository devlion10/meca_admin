package kr.or.kpf.lms.biz.homepage.topqna.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.homepage.topqna.controller.TopQnaApiController;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Schema(name="TopQnaApiRequestVO", description="자주묻는 질문 API 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TopQnaApiRequestVO {

    /** 시퀀스 번호 */
    @Schema(description="시퀀스 번호", required=true, example="")
    @NotNull(groups={TopQnaApiController.UpdateTopQna.class}, message="시퀀스 번호는 필수 입니다.")
    private BigInteger sequenceNo;

    /** 질문 */
    @Schema(description="질문", required=true, example="")
    @NotEmpty(groups={TopQnaApiController.CreateTopQna.class}, message="질문는 필수 입니다.")
    private String question;

    /** 답변 */
    @Schema(description="답변", required=true, example="")
    @NotEmpty(groups={TopQnaApiController.UpdateTopQna.class}, message="답변는 필수 입니다.")
    private String answer;
}
