package kr.or.kpf.lms.common.support;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderOfValue {
    /** 정렬 순서 */
    @Schema(description="정렬 순서", example="")
    private Integer sortNo;

    /** 코드 or 기타 값 */
    @Schema(description="코드 or 기타 값", example="")
    private String value;
}
