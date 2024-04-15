package kr.or.kpf.lms.biz.business.instructor.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 강사 모집 관련 응답 객체
 */
@Schema(name="BizInstructorApiResponseVO", description="강사 모집 API 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizInstructorApiResponseVO extends CSResponseVOSupport {

    @Schema(description="강사 모집 일련 번호", example="1")
    private String bizInstrNo;
    @Schema(description="강사 모집 사업 중복확인 코드 중복이면 3602", example="1")
    private String bizPbacnCode;
}