package kr.or.kpf.lms.biz.business.instructor.dist.crtramt.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.business.instructor.dist.crtramt.vo.CreateBizInstructorDistCrtrAmt;
import kr.or.kpf.lms.biz.business.instructor.dist.crtramt.vo.UpdateBizInstructorDistCrtrAmt;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 이동거리 기준단가 관련 요청 객체
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class BizInstructorDistCrtrAmtApiRequestVO {
    @Schema(description="이동거리 기준단가 일련 번호", required = true, example="1")
    private String bizInstrDistCrtrAmtNo;

    @Schema(description="이동거리 기준단가 연도", required = true, example="1")
    @NotNull(groups={CreateBizInstructorDistCrtrAmt.class, UpdateBizInstructorDistCrtrAmt.class}, message="사업 공고 일련 번호는 필수 입니다.")
    private Integer bizInstrDistCrtrAmtYr;

    @Schema(description="이동거리 기준단가 값", required = true, example="1")
    @NotNull(groups={CreateBizInstructorDistCrtrAmt.class, UpdateBizInstructorDistCrtrAmt.class}, message="강사 일련 번호은 필수 입니다.")
    private Integer bizInstrDistCrtrAmtValue;

}
