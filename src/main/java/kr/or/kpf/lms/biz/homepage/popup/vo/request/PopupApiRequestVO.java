package kr.or.kpf.lms.biz.homepage.popup.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.homepage.banner.vo.CreateBanner;
import kr.or.kpf.lms.biz.homepage.banner.vo.UpdateBanner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(name="PopupApiRequestVO", description="팝업")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PopupApiRequestVO {

    /** 고유키 */
    @Schema(description="고유키", required=false, example="")
    private String popupSn;

    /** 제목 */
    @Schema(description="제목", required=true, example="")
    @NotEmpty(groups={CreateBanner.class, UpdateBanner.class}, message="제목은 필수 입니다.")
    private String title;

    /** 내용 */
    @Schema(description="내용", required=true, example="")
    @NotEmpty(groups={CreateBanner.class, UpdateBanner.class}, message="내용은 필수 입니다.")
    private String contents;

    /** 팝업 타입 */
    @Schema(description="팝업 타입", required=true, example="")
    @NotEmpty(groups={CreateBanner.class, UpdateBanner.class}, message="제목은 필수 입니다.")
    private String popupViewType;

    /** 시작 일시 */
    @Schema(description="시작 일시", required=true, example="")
    private String popupStartYmd;

    /** 종료 일시 */
    @Schema(description="종료 일시", required=true, example="")
    private String popupEndYmd;

    /** 팝업 링크 */
    @Schema(description="팝업 링크", required=true, example="")
    private String popupLink;

    /** 팝업 이미지 경로 */
    @Schema(description="팝업 이미지 경로", required=true, example="")
    private String popupImagePath;

    /** 윈도우 세로 (pixel) */
    @Schema(description="창 세로 사이즈", required=true, example="1")
    private Integer windowSizeV;

    /** 윈도우 가로 (pixel) */
    @Schema(description="창 가로 사이즈", required=true, example="1")
    private Integer windowSizeH;

    /** 창 위치 위부터 */
    @Schema(description="창 위치 위부터", required=true, example="1")
    private Integer windowTop;

    /** 창 위치 왼쪽부터 */
    @Schema(description="창 위치 왼쪽부터", required=true, example="1")
    private Integer windowLeft;

    /** 상태 */
    @Schema(description="상태(0:비활성, 1:활성)", required=true, example="1")
    @NotNull(groups={CreateBanner.class, UpdateBanner.class}, message="상태는 필수 입니다.")
    private int popupStts;

}
