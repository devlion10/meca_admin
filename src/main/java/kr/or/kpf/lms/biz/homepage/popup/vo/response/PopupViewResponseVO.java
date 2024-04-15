package kr.or.kpf.lms.biz.homepage.popup.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigInteger;

@Schema(name="PopupViewResponseVO", description="팝업 관련 응답 객체")
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PopupViewResponseVO {

    /** 시퀀스 번호 */
    @Schema(description="시퀀스 번호")
    private String popupSn;

}
