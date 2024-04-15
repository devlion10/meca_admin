package kr.or.kpf.lms.biz.contents.evaluate.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="EvaluateApiResponseVO", description="강의 평가 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluateApiResponseVO extends CSResponseVOSupport {

    /** 강의 평가 일련 번호 */
    @Schema(description="강의 평가 일련 번호")
    private String evaluateSerialNo;

    /** 강의 평가 제목 */
    @Schema(description="강의 평가 제목")
    private String evaluateTitle;

    /** 강의 평가 타입 */
    @Schema(description="강의 평가 타입")
    private String evaluateType;

    /** 강의 평가 내용 */
    @Schema(description="강의 평가 내용")
    private String evaluateContents;

    /** 사용 여부 */
    @Schema(description="사용 여부")
    private Boolean isUsable;
}
