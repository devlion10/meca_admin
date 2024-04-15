package kr.or.kpf.lms.biz.system.menu.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="MenuApiResponseVO", description="메뉴 API 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuApiResponseVO extends CSResponseVOSupport {
    /** 상위 메뉴 일련번호 */
    @Schema(description="상위 메뉴 일련번호")
    private Long topSequenceNo;
    /** 메뉴 Depth */
    @Schema(description="메뉴 Depth")
    private Integer depth;
    /** 순서 */
    @Schema(description="순서")
    private Integer sort;
    /** 메뉴 URI */
    @Schema(description="메뉴 URI")
    private String uri;
    /** 메뉴명 1 */
    @Schema(description="메뉴명 1")
    private String menuName;
    /** 메뉴명 2 */
    @Schema(description="메뉴명 2")
    private String menuFullName;
    /** 사용 여부 */
    @Schema(description="사용 여부")
    private Boolean isUsable;
}
