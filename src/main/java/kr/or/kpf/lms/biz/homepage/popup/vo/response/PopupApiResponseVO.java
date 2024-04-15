package kr.or.kpf.lms.biz.homepage.popup.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.*;

@Schema(name="PopupApiResponseVO", description="팝업 관련 응답 객체")
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PopupApiResponseVO extends CSResponseVOSupport {

    /** 시퀀스 번호 */
    @Schema(description="시퀀스 번호")
    private String popupSn;

}
