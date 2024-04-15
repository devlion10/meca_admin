package kr.or.kpf.lms.biz.education.schedule.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="ScheduleViewRequestVO", description="교육 과정 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ScheduleViewRequestVO extends CSViewVOSupport {
    /** 검색어 범위 */
    private String containTextType;
    /** 검색에 포함할 단어 */
    private String containText;


    /** 교육 과정 개설 코드 */
    private String educationPlanCode;

    /** 교육 유형 */
    private String educationType;

    /** 교육 유형 검색 */
    private String eduType;

    /** 과정 카테고리 */
    private String categoryCode;

    /** 과정 명 */
    private String curriculumName;

    /** 강의 명 */
    private String lectureName;

    /** 강사 명 */
    private String lecturer;

    /** 운영 타입 */
    private String operationType;

    /** 지사 */
    private String province;

    /** 교육 계획 연도 */
    private String yearOfEducationPlan;

    /** 교육 계획 연도 단계 수 */
    private String yearOfEducationPlanStep;

    /** 교육 과정 신청 가능 여부(1: 신청 불가, 2: 신청 마감, 3: 신청 가능, 4: 신청 완료) */
    private String availableApplicationType;
}
