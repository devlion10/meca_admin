package kr.or.kpf.lms.biz.education.curriculum.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import lombok.*;

import javax.persistence.Column;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class CurriculumExcelVO {

    /** 교육 과정 유형 (1: 화상 교육, 2: 집합 교육, 3: 화상 교육 + 집합 교육, 4: 이러닝 교육) */
    @ExcelColumn(headerName = "유형")
    private String educationType;

    /** 교육 과정 카테고리 */
    @ExcelColumn(headerName = "카테고리")
    private String categoryCode;

    /** 과정 명 */
    @ExcelColumn(headerName = "과정명")
    private String curriculumName;


    /** 교육 시간(시간단위) */
    private Integer educationPerHour;

    /** 교육 시간(분단위) */
    private Integer educationPerMinute;

    @ExcelColumn(headerName = "교육 시간")
    private String eduHr;

    /** 담당자 명 */
    @ExcelColumn(headerName = "담당자 명")
    private String managerName;

    /** 담당자 아이디 */
    @ExcelColumn(headerName = "담당자 아이디")
    private String managerDepartment;

    /** 교육 대상 (1: 언론인, 2: 교원, 3: 학생, 4: 학부모) */
    @ExcelColumn(headerName = "교육 대상")
    private String educationTarget;


    /** 등록 일시 */
    @ExcelColumn(headerName = "등록일")
    private String createDateTime;

    /** 사용 여부 */
    private Boolean isUsable;

    /** 사용 여부 */
    @ExcelColumn(headerName = "상태")
    private String isUsableString;


}
