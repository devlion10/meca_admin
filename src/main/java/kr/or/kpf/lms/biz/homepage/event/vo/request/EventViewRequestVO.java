package kr.or.kpf.lms.biz.homepage.event.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.*;

@Schema(name="EventViewRequestVO", description="이벤트/설문 View 관련 요청 객체")
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventViewRequestVO extends CSViewVOSupport {

    /** 검색어 범위 */
    @Schema(description="검색어 범위")
    private String containTextType;

    /** 검색에 포함할 단어 */
    @Schema(description="검색에 포함할 단어")
    private String containText;

    /** 고유키 */
    @Schema(description="고유키")
    private Long sequenceNo;

    /** 상태 (0:종료, 1:진행) */
    @Schema(description="상태 코드")
    private Integer status;

    /** 타입(1: 이벤트, 2: 설문) */
    @Schema(description="타입(1: 이벤트, 2: 설문)")
    private String type;

    /** 이벤트 시작 일시 */
    @Schema(description="이벤트 시작 일시")
    private String startDt;

    /** 이벤트 종료 일시 */
    @Schema(description="이벤트 종료 일시")
    private String endDt;

}
