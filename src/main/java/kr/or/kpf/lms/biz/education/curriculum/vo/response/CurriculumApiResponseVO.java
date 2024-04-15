package kr.or.kpf.lms.biz.education.curriculum.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="CurriculumApiRequestVO", description="교육과정 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurriculumApiResponseVO extends CSResponseVOSupport {

    /** 과정 코드 */
    @Schema(description="과정 코드", example="")
    private String curriculumCode;

    /** 과정 명 */
    @Schema(description="과정 명", example="")
    private String curriculumName;

    /** 교육 타입 (1: 화상 교육, 2: 집합 교육, 3: 이러닝 교육) */
    @Schema(description="과정 타입 (1: 화상 교육, 2: 집합 교육, 3: 이러닝 교육)", example="")
    private String educationType;

    /** 교육 과정 카테고리 */
    @Schema(description="교육 과정 카테고리", example="")
    private String educationCategory;

    /** 교육 시간 수 */
    @Schema(description="교육 시간 수", example="")
    private Integer educationPerHour;

    /** 사용 여부 */
    @Schema(description="사용 여부", example="")
    private Boolean isUsable;

    /** 교육 목표 */
    @Schema(description="교육 목표", example="")
    private String educationGoal;

    /** 교육 대상 (1: 언론인, 2: 교원, 3: 학생, 4: 학부모) */
    @Schema(description="교육 대상 (1: 언론인, 2: 교원, 3: 학생, 4: 학부모)", example="")
    private String educationTarget;

    /** 교육 내용 */
    @Schema(description="교육 내용", example="")
    private String educationContent;

    /** 완료 진도 비율 */
    @Schema(description="완료 진도 비율", example="")
    private Double progressRateOfEnd;

    /** 완료 시험 비율 */
    @Schema(description="완료 시험 비율", example="")
    private Double examRateOfEnd;

    /** 완료 과제 비율 */
    @Schema(description="완료 과제 비율", example="")
    private Double assignmentRateOfEnd;

    /** 완료 진도 점수 */
    @Schema(description="완료 진도 점수", example="")
    private Double progressScoreOfEnd;

    /** 완료 시험 점수 */
    @Schema(description="완료 시험 점수", example="")
    private Double examScoreOfEnd;

    /** 완료 과제 점수 */
    @Schema(description="완료 과제 점수", example="")
    private Double assignmentScoreOfEnd;

    /** 완료 집계 점수 */
    @Schema(description="완료 집계 점수", example="")
    private Double totalScore;

    /** 차시 진도 기준 */
    @Schema(description="차시 진도 기준", example="")
    private Double progressScore;

    /** 시험 시작 가능 학습 경과일 */
    @Schema(description="시험 시작 가능 학습 경과일", example="")
    private Integer startExamTerm;

    /** 시험 시작 가능 진도율 */
    @Schema(description="시험 시작 가능 진도율", example="")
    private Double startExamRate;

    /** 순차 학습 적용 여부 */
    @Schema(description="순차 학습 적용 여부", example="")
    private Boolean isSequentialEducation;

    /** 수료 유형 코드 (1: 수료 기준 달성, 2: 교육 종료일 이후) */
    @Schema(description="수료 유형 코드 (1: 수료 기준 달성, 2: 교육 종료일 이후)", example="")
    private String completionType;

    /** 수료 번호 발급 여부 */
    @Schema(description="수료 번호 발급 여부", example="")
    private Boolean isIssueCertificateOfCompletion;

    /** 재 교육 기한 수 */
    @Schema(description="재 교육 기한 수", example="")
    private Integer reEducationTerm;

    /** 시험 응시 가능 횟수 */
    @Schema(description="시험 응시 가능 횟수", example="")
    private Integer reExamCount;

    /** 조회 수 */
    @Schema(description="조회 수", example="")
    private Integer hitCount;

    /** 신청 승인 유형 코드 (1: 신청 즉시 승인, 2: 관리자 승인) */
    @Schema(description="신청 승인 유형 코드 (1: 신청 즉시 승인, 2: 관리자 승인)", example="")
    private String applyApprovalType;

    /** 교육 장소 */
    @Schema(description="교육 장소", example="")
    private String educationPlace;

    /** 지점 */
    @Schema(description="지점", example="")
    private String branchCode;

    /** CP 코드 */
    @Schema(description="CP 코드", example="")
    private String contentsProvider;

    /** 비고 내용 */
    @Schema(description="비고 내용", example="")
    private String memo;

    /** 인원수 */
    @Schema(description="인원수", example="")
    private Integer numberOfPeople;
}
