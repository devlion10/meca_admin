package kr.or.kpf.lms.biz.homepage.popup.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.*;

@Schema(name="PopupViewRequestVO", description="팝업 View 관련 요청 객체")
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PopupViewRequestVO extends CSViewVOSupport {

    /** 검색어 범위 */
    @Schema(description="검색어 범위")
    private String containTextType;

    /** 검색에 포함할 단어 */
    @Schema(description="검색에 포함할 단어")
    private String containText;

    /** 고유키 */
    @Schema(description="고유키")
    private String popupSn;

    /** 스크롤 상태 (0:비활성, 1:활성) */
    @Schema(description="상태 코드")
    private Integer scrollStatus;

    /** 상태 (0:비활성, 1:활성) */
    @Schema(description="상태 코드")
    private Integer status;

    /** 배너 시작 일시 */
    @Schema(description="이벤트 시작 일시")
    private String startDt;

    /** 배너 종료 일시 */
    @Schema(description="이벤트 종료 일시")
    private String endDt;

}
