package kr.or.kpf.lms.biz.homepage.banner.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.*;

@Schema(name="BannerViewRequestVO", description="배너 View 관련 요청 객체")
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BannerViewRequestVO extends CSViewVOSupport {

    /** 검색어 범위 */
    @Schema(description="검색어 범위")
    private String containTextType;

    /** 검색에 포함할 단어 */
    @Schema(description="검색에 포함할 단어")
    private String containText;

    /** 고유키 */
    @Schema(description="고유키")
    private String bannerSn;

    /** 상태 (0:종료, 1:진행) */
    @Schema(description="상태 코드")
    private Integer status;
}
