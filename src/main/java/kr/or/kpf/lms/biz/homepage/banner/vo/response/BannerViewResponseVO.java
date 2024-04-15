package kr.or.kpf.lms.biz.homepage.banner.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(name="BannerViewResponseVO", description="배너 관련 응답 객체")
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BannerViewResponseVO {

    /** 시퀀스 번호 */
    @Schema(description="시퀀스 번호")
    private String bannerSn;

}
