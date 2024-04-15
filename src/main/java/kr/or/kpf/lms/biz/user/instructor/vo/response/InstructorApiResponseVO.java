package kr.or.kpf.lms.biz.user.instructor.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="InstructorApiResponseVO", description="강사 관리 API 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstructorApiResponseVO extends CSResponseVOSupport {

    @Schema(description="강사 일련 번호", example="")
    private Long instrSerialNo;

    @Schema(description="회원 아이디", example="")
    private String userId;

    @Schema(description="강사 명", example="")
    private String instrNm;

    @Schema(description="강사 구분", example="")
    private String instrCtgr;

    @Schema(description="강사 상태", example="")
    private String instrStts;
}
