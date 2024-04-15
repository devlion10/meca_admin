package kr.or.kpf.lms.biz.contents.evaluate.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.contents.evaluate.controller.EvaluateApiController;
import kr.or.kpf.lms.common.converter.BooleanConverter;
import kr.or.kpf.lms.common.support.OrderOfValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(name="EvaluateApiRequestVO", description="강의 평가 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EvaluateApiRequestVO {
    
    /** 강의 평가 일련 번호 */
    @Schema(description="강의 평가 일련 번호", required = true, example="")
    @NotEmpty(groups={EvaluateApiController.UpdateEvaluate.class}, message="강의 평가 일련 번호는 필수 입니다.")
    private String evaluateSerialNo;

    /** 강의 평가 제목 */
    @Schema(description="강의 평가 제목", required = true, example="")
    @NotEmpty(groups={EvaluateApiController.CreateEvaluate.class, EvaluateApiController.UpdateEvaluate.class}, message="강의 평가 제목는 필수 입니다.")
    private String evaluateTitle;

    /** 강의 평가 타입 */
    @Schema(description="강의 평가 타입", required = true, example="")
    @NotEmpty(groups={EvaluateApiController.CreateEvaluate.class, EvaluateApiController.UpdateEvaluate.class}, message="강의 평가 타입는 필수 입니다.")
    private String evaluateType;

    /** 강의 평가 내용 */
    @Schema(description="강의 평가 내용", required = false, example="")
    private String evaluateContents;

    /** 사용 여부 */
    @Schema(description="사용 여부", required = true, example="")
    @NotNull(groups={EvaluateApiController.CreateEvaluate.class, EvaluateApiController.UpdateEvaluate.class}, message="사용 여부는 필수 입니다.")
    @Builder.Default
    private Boolean isUsable = true;

    /** 기타의견 사용 여부 */
    @Schema(description="기타의견 사용 여부", required = true, example="")
    @NotNull(groups={EvaluateApiController.CreateEvaluate.class, EvaluateApiController.UpdateEvaluate.class}, message="기타의견 사용 여부는 필수 입니다.")
    @Builder.Default
    private Boolean isUsableOtherComments = true;

    @Schema(description="강의 평가 질문 문항", required = true, example="")
    @NotEmpty(groups={EvaluateApiController.CreateEvaluate.class, EvaluateApiController.UpdateEvaluate.class}, message="강의 평가 질문은 필수 입니다.")
    List<OrderOfValue> questionList;
}
