package kr.or.kpf.lms.biz.education.curriculum.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.education.curriculum.controller.CurriculumApiController;
import kr.or.kpf.lms.common.support.OrderOfValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.List;

@Schema(name="CurriculumApiRequestVO", description="교육 과정 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CurriculumApiRequestVO {

    /** 과정 코드 */
    @Schema(description="과정 코드", required = true, example="CRCL000001")
    @NotEmpty(groups={CurriculumApiController.UpdateGeneral.class, CurriculumApiController.UpdateElearning.class}, message="과정 코드는 필수 입니다.")
    private String curriculumCode;

    /** 과정 명 */
    @Schema(description="과정 명", required = true, example="교육과정1")
    @NotEmpty(groups={CurriculumApiController.CreateGeneral.class, CurriculumApiController.CreateElearning.class,
            CurriculumApiController.UpdateGeneral.class, CurriculumApiController.UpdateElearning.class}, message="과정 명는 필수 입니다.")
    private String curriculumName;

    /** 교육 타입 (1: 화상 교육, 2: 집합 교육, 3: 이러닝 교육) */
    @Schema(description="과정 타입 (1: 화상 교육, 2: 집합 교육, 3: 이러닝 교육)", required = true, example="1")
    @NotEmpty(groups={CurriculumApiController.CreateGeneral.class, CurriculumApiController.CreateElearning.class,
            CurriculumApiController.UpdateGeneral.class, CurriculumApiController.UpdateElearning.class}, message="교육 타입 (1: 화상 교육, 2: 집합 교육, 3: 이러닝 교육)는 필수 입니다.")
    private String educationType;

    /** 교육 과정 카테고리 */
    @Schema(description="교육 과정 카테고리", required = true, example="1")
    @NotEmpty(groups={CurriculumApiController.CreateGeneral.class, CurriculumApiController.CreateElearning.class,
            CurriculumApiController.UpdateGeneral.class, CurriculumApiController.UpdateElearning.class}, message="교육 과정 카테고리는 필수 입니다.")
    private String categoryCode;

    /** 과정 분야 코드 */
    @Schema(description="과정 분야 코드", required = false, example="1")
    private String curriculumAreaCode;

    /** 과정 상세 분야 코드 */
    @Schema(description="과정 상세 분야 코드", required = false, example="1")
    private String curriculumAreaCodeDetail;

    /** 교육 시간(시간단위) */
    @Schema(description="교육 시간(시간단위)")
    @NotNull(groups={CurriculumApiController.CreateGeneral.class, CurriculumApiController.UpdateGeneral.class,
            CurriculumApiController.CreateElearning.class, CurriculumApiController.UpdateElearning.class}, message="교육 시간(시간단위)는 필수 입니다.")
    private Integer educationPerHour;

    /** 교육 시간(분단위) */
    @Schema(description="교육 시간(분단위)")
    @NotNull(groups={CurriculumApiController.CreateGeneral.class, CurriculumApiController.UpdateGeneral.class,
            CurriculumApiController.CreateElearning.class, CurriculumApiController.UpdateElearning.class}, message="교육 시간(분단위)는 필수 입니다.")
    private Integer educationPerMinute;

    /** 담당자 명 */
    @Schema(description="담당자 명")
    private String managerName;

    /** 담당자 아이디 */
    @Schema(description="담당자 아이디")
    private String managerDepartment;

    /** 집행 타입 */
    @Schema(description="집행 타입")
    private String enforcementType;

    /** 사용 여부 */
    @Schema(description="사용 여부", required = true, example="true")
    @NotNull(groups={CurriculumApiController.CreateGeneral.class, CurriculumApiController.UpdateGeneral.class,
            CurriculumApiController.CreateElearning.class, CurriculumApiController.UpdateElearning.class}, message="사용 여부는 필수 입니다.")
    private Boolean isUsable;

    /** 관리자 승인 여부(N: 미승인, Y: 승인) */
    @Schema(description="관리자 승인 여부(N: 미승인, Y: 승인)")
    private Boolean isAdminApproval;

    /** 교육 목표 */
    @Schema(description="교육 목표", required = true, example="")
    @NotEmpty(groups={CurriculumApiController.CreateGeneral.class, CurriculumApiController.UpdateGeneral.class,
            CurriculumApiController.CreateElearning.class, CurriculumApiController.UpdateElearning.class}, message="교육 목표는 필수 입니다.")
    private String educationGoal;

    /** 교육 대상 (1: 일반인, 2: 언론인, 3: 교원, 4: 학생, 5: 학부모) */
    @Schema(description="교육 대상 (1: 일반인, 2: 언론인, 3: 교원, 4: 학생, 5: 학부모)", required = true, example="3")
    @NotEmpty(groups={CurriculumApiController.CreateGeneral.class, CurriculumApiController.UpdateGeneral.class,
            CurriculumApiController.CreateElearning.class, CurriculumApiController.UpdateElearning.class}, message="교육 대상 (1: 일반인, 2: 언론인, 3: 교원, 4: 학생, 5: 학부모)은 필수 입니다.")
    private String educationTarget;

    /** 교육 대상 상세 설명 */
    @Schema(description="교육 대상 상세 설명", required = true, example="3")
    private String educationTargetDescription;

    /** 교육 내용 */
    @Schema(description="교육 내용", required = true, example="")
    @NotEmpty(groups={CurriculumApiController.CreateGeneral.class, CurriculumApiController.UpdateGeneral.class,
            CurriculumApiController.CreateElearning.class, CurriculumApiController.UpdateElearning.class}, message="교육 내용은 필수 입니다.")
    private String educationContent;

    /** 시험 여부 */
    @Schema(description="시험 여부")
    @NotNull(groups={CurriculumApiController.CreateGeneral.class, CurriculumApiController.UpdateGeneral.class}, message="시험 여부는 필수 입니다.")
    private Boolean isExam;

    /** 결과 보고 여부 */
    @Schema(description="결과 보고 여부")
    @NotNull(groups={CurriculumApiController.CreateGeneral.class, CurriculumApiController.UpdateGeneral.class}, message="결과 보고는 필수 입니다.")
    private Boolean isResult;

    /** 완료 진도 비율 */
    @Schema(description="완료 진도 비율", required = true, example="100")
    @NotNull(groups={CurriculumApiController.CreateElearning.class, CurriculumApiController.UpdateElearning.class}, message="완료 진도 비율은 필수 입니다.")
    private Double progressRateOfEnd;

    /** 완료 시험 비율 */
    @Schema(description="완료 시험 비율", required = true, example="100")
    @NotNull(groups={CurriculumApiController.CreateElearning.class, CurriculumApiController.UpdateElearning.class}, message="완료 시험 비율은 필수 입니다.")
    private Double examRateOfEnd;

    /** 완료 과제 비율 */
    @Schema(description="완료 과제 비율", required = false, example="100")
    private Double assignmentRateOfEnd;

    /** 완료 진도 점수 */
    @Schema(description="완료 진도 점수", required = true, example="100")
    @NotNull(groups={CurriculumApiController.CreateElearning.class, CurriculumApiController.UpdateElearning.class}, message="완료 진도 점수는 필수 입니다.")
    private Double progressScoreOfEnd;

    /** 완료 시험 점수 */
    @Schema(description="완료 시험 점수", required = true, example="1000")
    @NotNull(groups={CurriculumApiController.CreateElearning.class, CurriculumApiController.UpdateElearning.class}, message="완료 시험 점수는 필수 입니다.")
    private Double examScoreOfEnd;

    /** 완료 과제 점수 */
    @Schema(description="완료 과제 점수", required = false, example="100")
    private Double assignmentScoreOfEnd;

    /** 완료 집계 점수 */
    @Schema(description="완료 집계 점수", required = true, example="100")
    @NotNull(groups={CurriculumApiController.CreateElearning.class, CurriculumApiController.UpdateElearning.class}, message="완료 집계 점수는 필수 입니다.")
    private Double totalScore;

    /** 차시 진도 기준 */
    @Schema(description="차시 진도 기준", required = true, example="100")
    @NotNull(groups={CurriculumApiController.CreateElearning.class, CurriculumApiController.UpdateElearning.class}, message="차시 진도 기준은 필수 입니다.")
    private Double progressRate;

    /** 시험 시작 가능 학습 경과일 */
    @Schema(description="시험 시작 가능 학습 경과일", required = true, example="10")
    @NotNull(groups={CurriculumApiController.CreateElearning.class, CurriculumApiController.UpdateElearning.class}, message="시험 시작 가능 학습 경과일은 필수 입니다.")
    private Integer startExamTerm;

    /** 시험 시작 가능 진도율 */
    @Schema(description="시험 시작 가능 진도율", required = true, example="50")
    @NotNull(groups={CurriculumApiController.CreateElearning.class, CurriculumApiController.UpdateElearning.class}, message="시험 시작 가능 진도율은 필수 입니다.")
    private Double startExamRate;

    /** 순차 학습 적용 여부 */
    @Schema(description="순차 학습 적용 여부", required = true, example="true")
    @NotNull(groups={CurriculumApiController.CreateElearning.class, CurriculumApiController.UpdateElearning.class}, message="순차 학습 적용 여부는 필수 입니다.")
    private Boolean isSequentialEducation;

    /** 수료 유형 코드 (1: 수료 기준 달성, 2: 교육 종료일 이후) */
    @Schema(description="수료 유형 코드 (1: 수료 기준 달성, 2: 교육 종료일 이후)", required = true, example="1")
    @NotEmpty(groups={CurriculumApiController.CreateElearning.class, CurriculumApiController.UpdateElearning.class}, message="수료 유형 코드는 필수 입니다.")
    private String completionType;

    /** 수료 번호 발급 여부 */
    @Schema(description="수료 번호 발급 여부", required = true, example="true")
    @NotNull(groups={CurriculumApiController.CreateGeneral.class, CurriculumApiController.UpdateGeneral.class,
            CurriculumApiController.CreateElearning.class, CurriculumApiController.UpdateElearning.class}, message="수료 번호 발급 여부는 필수 입니다.")
    private Boolean isIssueCertificateOfCompletion;

    /** 복습 기간(월단위) */
    @Schema(description="복습 기간(월단위)", required = true, example="3")
    @NotNull(groups={CurriculumApiController.CreateElearning.class, CurriculumApiController.UpdateElearning.class}, message="복습 기간(월단위)은 필수 입니다.")
    private Integer reEducationTerm;

    /** 복습제공 여부 */
    @Schema(description="복습제공 여부", required = true, example="3")
    @NotNull(groups={CurriculumApiController.CreateGeneral.class, CurriculumApiController.UpdateGeneral.class,
            CurriculumApiController.CreateElearning.class, CurriculumApiController.UpdateElearning.class}, message="복습제공 여부은 필수 입니다.")
    private Boolean isReview;

    /** 시험 응시 가능 횟수 */
    @Schema(description="시험 응시 가능 횟수", required = true, example="0")
    @NotNull(groups={CurriculumApiController.CreateElearning.class, CurriculumApiController.UpdateElearning.class}, message="시험 응시 가능 횟수는 필수 입니다.")
    private Integer reExamCount;

    /** 조회 수 */
    @Schema(description="조회 수", required = true, example="0")
    private BigInteger viewCount;

    /** 신청 승인 유형 코드 (1: 신청 즉시 승인, 2: 관리자 승인) */
    @Schema(description="신청 승인 유형 코드 (1: 신청 즉시 승인, 2: 관리자 승인)", required = true, example="1")
    @NotEmpty(groups={CurriculumApiController.CreateGeneral.class, CurriculumApiController.UpdateGeneral.class,
            CurriculumApiController.CreateElearning.class, CurriculumApiController.UpdateElearning.class}, message="신청 승인 유형 코드 (1: 신청 즉시 승인, 2: 관리자 승인)는 필수 입니다.")
    private String applyApprovalType;

    /** 교육 장소 */
    @Schema(description="교육 장소", example="")
    private String educationPlace;

    /** 교육 운영 지사 */
    @Schema(description="교육 운영 지사", example="")
    private String province;

    /** CP 코드 */
    @Schema(description="CP 코드", example="")
    private String contentsProvider;

    /** 설문 코드 */
    @Schema(description="설문 코드", example="")
    private String questionnaireCode;

    /** 신청서 작성 여부 */
    @Schema(description="신청서 작성 여부", example="")
    @NotNull(groups={CurriculumApiController.CreateGeneral.class, CurriculumApiController.UpdateGeneral.class}, message="신청서 작성 여부는 필수 입니다.")
    private Boolean isApplicationForm;

    /** 신청서 파일 경로 */
    @Schema(description="신청서 파일 경로", example="")
    private String applicationFormFilePath;

    /** 비고 내용 */
    @Schema(description="비고 내용", example="")
    private String memo;

    /** 인원수 */
    @Schema(description="인원수", required = true, example="50")
    @NotNull(groups={CurriculumApiController.CreateElearning.class, CurriculumApiController.UpdateElearning.class}, message="인원수는 필수 입니다.")
    private Integer numberOfPeople;

    /** 연결 콘텐츠 */
    @Schema(description = "연결 콘텐츠")
    @NotEmpty(groups={CurriculumApiController.CreateElearning.class, CurriculumApiController.UpdateElearning.class}, message="연결 콘텐츠는 필수 입니다.")
    private List<OrderOfValue> contentsList;

    /** 연결 강의평가 */
    @Schema(description="연결 강의평가")
    private List<OrderOfValue> evaluateList;

    /** 연계 과정 */
    private List<OrderOfValue> curriculumCollaborationList;

}
