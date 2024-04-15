package kr.or.kpf.lms.biz.user.instructor.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.*;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class InstructorCareerRequestVO extends CSViewVOSupport {

    @Schema(description="강사 구분", required = true, example="")
    private String instrCtgr;

    @Schema(description="강사일련번호", required = false, example="")
    private Long instrSerialNo;

    @Schema(description="강사 아이디(미디어강사)", required = false, example="")
    private String userId;


    /** 강의 구분(instr: 사업, reader: 교육) */
    @Schema(description="강의 구분", required = false, example="")
    private String category;

    /** 연도 */
    @Schema(description="연도", required = false, example="")
    private Integer year;

}
