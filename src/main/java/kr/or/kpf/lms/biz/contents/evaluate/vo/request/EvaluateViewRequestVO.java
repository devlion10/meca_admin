package kr.or.kpf.lms.biz.contents.evaluate.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 강의 평가 관련 요청 객체
 */
@Schema(name="EvaluateViewRequestVO", description="강의 평가 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EvaluateViewRequestVO extends CSViewVOSupport {

    @Schema(description="제목", example="")
    private String evaluateTitle;

    @Schema(description="강의 평가 타입", example="")
    private String evaluateType;

    @Schema(description="강의 평가 일련 번호")
    private String evaluateSerialNo;

    @Schema(description="사용 여부")
    private Boolean isUsable;

}
