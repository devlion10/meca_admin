package kr.or.kpf.lms.biz.education.application.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.education.application.vo.CreateEducationApplication;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.math.BigInteger;
import java.util.List;

@Schema(name="EducationApplicationApiRequestVO", description="교육 신청 API 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EducationApplicationApiRequestVO {
    @Schema(description="교육 신청 일련 번호", example="")
    private BigInteger sequenceNo;
    /** 교육 신청 일련 번호 */
    @Schema(description="교육 신청 일련 번호", required = true, example="")
    private String applicationNo;

    /** 교육 계획 코드 */
    @Schema(description="교육 계획 코드", required = true, example="")
    private String educationPlanCode;

    /** 과정 코드 */
    @Schema(description="과정 코드", required = true, example="CRCL000001")
    @NotEmpty(groups={CreateEducationApplication.class}, message="과정 코드는 필수 입니다.")
    private String curriculumCode;

    /** 콘텐츠 코드 */
    @Schema(description="콘텐츠 코드", required = true, example="")
    private String contentsCode;

    /** 챕터 코드 */
    @Schema(description="챕터 코드", required = true, example="")
    private String chapterCode;

    /** 강의 코드 */
    @Schema(description="강의 코드", required = true, example="")
    private String lectureCode;

    /** 출석 여부 */
    @Schema(description="강의 출석 여부", required = true, example="")
    private Boolean isAttend;

    /** 회원 ID */
    @Schema(description="회원 ID", required = true, example="")
    @NotEmpty(groups={CreateEducationApplication.class}, message="회원 ID는 필수 입니다.")
    private String userId;

    /** 신청 교육 유형 */
    @Schema(description="신청 교육 유형", required = false, example="")
    private String setEducationType;

    /** 숙박 여부 */
    @Schema(description="숙박 여부", required = false, example="")
    private Boolean isAccommodation;

    /** 관리자 승인 상태 */
    @Schema(description="관리자 승인 상태", required = true, example="")
    private String adminApprovalState;

    /** 교육 상태(1: 신청 심사 중, 2: 교육 진행 중, 3: 교육 완료) */
    @Schema(description="교육 상태(1: 신청 심사 중, 2: 교육 진행 중, 3: 교육 완료)", required = true, example="")
    private String educationState;

    /** 수강 교육 과정 진도 완료 여부 */
    @Schema(description="수강 교육 과정 진도 완료 여부", required = true, example="")
    private String isComplete;

    /** 현재 진도 점수 */
    private Integer progressScore;

    /** 현재 시험 점수 */
    private Integer examScore;

    /** 신청서 파일 경로 */
    @Schema(description="신청서 파일 경로", hidden = true)
    private String applicationFilePath;

    /** 다건 처리 요청일 경우 사용 */
    private List<EducationApplicationApiRequestVO> applicationList;
}
