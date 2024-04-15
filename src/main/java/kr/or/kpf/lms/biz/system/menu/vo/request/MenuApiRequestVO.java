package kr.or.kpf.lms.biz.system.menu.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.system.menu.controller.MenuApiController;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Schema(name="MenuApiRequestVO", description="메뉴 API 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuApiRequestVO {
    /** 일련 번호 */
    @Schema(description="일련 번호")
    @NotNull(groups={MenuApiController.UpdateMenu.class}, message="메뉴 일련 번호는 필수입니다.")
    private Long sequenceNo;
    /** 상위 메뉴 일련번호 */
    @Schema(description="상위 메뉴 일련번호")
    private Long topSequenceNo;
    /** 메뉴 Depth */
    @Schema(description="메뉴 Depth")
    private Integer depth;
    /** 순서 */
    @Schema(description="순서")
    @Min(value = 1, groups = {MenuApiController.CreateMenu.class, MenuApiController.UpdateMenu.class}, message = "최소 순서는 1입니다.")
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
