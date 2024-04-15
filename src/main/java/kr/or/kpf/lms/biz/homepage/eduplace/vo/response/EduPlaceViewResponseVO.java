package kr.or.kpf.lms.biz.homepage.eduplace.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(name="EduPlaceViewResponseVO", description="교육장 신청 이력 관련 응답 객체")
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class EduPlaceViewResponseVO {

    /** 시퀀스 번호 */
    @Schema(description="시퀀스 번호")
    private Long sequenceNo;

}
