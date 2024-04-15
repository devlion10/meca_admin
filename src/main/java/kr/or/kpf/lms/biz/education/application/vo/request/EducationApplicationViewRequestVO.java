package kr.or.kpf.lms.biz.education.application.vo.request;

import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.*;

/**
 * 교육신청 관련 요청 객체
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class EducationApplicationViewRequestVO extends CSViewVOSupport {

    /** 교육 계획 코드 */
    private String educationPlanCode;

    /** 교육신청 코드 */
    private String applicationNo;

    /** 검색어 범위 */
    private String containTextType;

    /** 검색에 포함할 단어 */
    private String containText;

    /** 과정 명 */
    private String curriculumName;

    /** 교육 카테고리 */
    private String categoryCode;

    /** 교육 타입 */
    private String educationType;

    /** 운영타입 */
    private String operationType;

    /** 교육 계획 연도 */
    private String yearOfEducationPlan;

    /** 교육 계획 연도 단계 수 */
    private String yearOfEducationPlanStep;

    /** 관리자 승인 여부 */
    private String adminApprovalState;

    /** 유저명 */
    private String userName;

    /** 유저 ID */
    private String userId;

    /** 부서 */
    private String province;

    /** 수료일 조회 시작일 */
    private String completeBeginDate;

    /** 수료일 조회 종료일 */
    private String completeEndDate;

    /** 수료 여부 */
    private String isComplete;

    /** 엑셀 결과용 */
    private Boolean isApply;
}
