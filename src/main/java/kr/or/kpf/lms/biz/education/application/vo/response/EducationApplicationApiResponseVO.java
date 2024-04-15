package kr.or.kpf.lms.biz.education.application.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(name="EducationApplicationApiResponseVO", description="교육 신청 API 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EducationApplicationApiResponseVO extends CSResponseVOSupport {

    @Schema(description="로그인 아이디")
    private String userId;

    @Schema(description="로그인 이름")
    private String userName;

    @Schema(description="과정 코드")
    private String curriculumCode;

    @Schema(description="과정명")
    private String curriculumName;

    @Schema(description="관리자 승인 상태")
    private String adminApprovalState;

    @Schema(description="숙박 여부")
    private Boolean isAccommodation;

    @Schema(description="교육 과정 신청 지원서 파일 경로")
    private String applicationFilePath;

    private List<EducationApplicationApiResponseVO> applicationList;
}
