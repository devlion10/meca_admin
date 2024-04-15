package kr.or.kpf.lms.biz.business.instructor.dist.crtramt.item.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 이동거리 기준단가 관련 응답 객체
 */
@Schema(name="BizInstructorDistViewResponseVO", description="거리 증빙 VIEW 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizInstructorDistCrtrAmtItemViewResponseVO extends CSResponseVOSupport {

    @Schema(description="이동거리 기준단가 항목 일련 번호", example="1")
    private String bizInstrDistCrtrAmtItemNo;
}