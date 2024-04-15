package kr.or.kpf.lms.biz.homepage.banner.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.homepage.banner.vo.CreateBanner;
import kr.or.kpf.lms.biz.homepage.banner.vo.UpdateBanner;
import kr.or.kpf.lms.biz.homepage.event.vo.CreateEvent;
import kr.or.kpf.lms.biz.homepage.event.vo.UpdateEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(name="BannerApiRequestVO", description="배너")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BannerApiRequestVO {

    /** 고유키 */
    @Schema(description="고유키", required=false, example="")
    private String bannerSn;

    /** 제목 */
    @Schema(description="제목", required=true, example="")
    @NotEmpty(groups={CreateBanner.class, UpdateBanner.class}, message="제목은 필수 입니다.")
    private String title;

    /** 시작 일시 */
    @Schema(description="시작 일시", required=true, example="")
    private String bannerStartYmd;

    /** 종료 일시 */
    @Schema(description="종료 일시", required=true, example="")
    private String bannerEndYmd;

    /** 배너 링크 */
    @Schema(description="배너 링크", required=true, example="")
    private String bannerLink;

    /** 배너 파일 경로 */
    @Schema(description="배너 파일 경로", required=true, example="")
    private String bannerImagePath;

    /** 상태 */
    @Schema(description="상태(0:종료, 1:진행)", required=true, example="1")
    @NotNull(groups={CreateBanner.class, UpdateBanner.class}, message="상태는 필수 입니다.")
    private int bannerStts;

}
