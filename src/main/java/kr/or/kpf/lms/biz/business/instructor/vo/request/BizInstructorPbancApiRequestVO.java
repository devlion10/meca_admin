package kr.or.kpf.lms.biz.business.instructor.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.business.instructor.vo.CreateBizInstructor;
import kr.or.kpf.lms.biz.business.instructor.vo.UpdateBizInstructor;
import kr.or.kpf.lms.common.converter.DateYMDToStringConverter;
import kr.or.kpf.lms.repository.entity.BizInstructorPbanc;
import lombok.*;

import javax.persistence.Convert;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 강사 모집 관련 요청 객체
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class BizInstructorPbancApiRequestVO {
    @Schema(description="강사 모집 공고 해당 사업 공고 일련 번호", required = true, example="1")
    private Long sequenceNo;

    @Schema(description="강사 모집 일련변호", required = false, example="1")
    private String bizInstrNo;

    @Schema(description="사업 공고 일련 번호", required = true, example="1")
    private String bizPbancNo;

    @Schema(description="강사 모집 기간 - 시작일", required = true, example="2022-12-01")
    private String bizInstrRcptBgng;

    @Schema(description="강사 모집 기간 - 종료일", required = true, example="2022-12-01")
    private String bizInstrRcptEnd;

    @Schema(description="강사 모집 상태", required = true, example="0")
    private Integer bizInstrPbancStts;

}
