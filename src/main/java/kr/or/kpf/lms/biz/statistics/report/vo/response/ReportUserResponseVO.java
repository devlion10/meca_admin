package kr.or.kpf.lms.biz.statistics.report.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="ReportUserResponseVO", description="경영평가 보고서 - 신청자별 엑셀 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportUserResponseVO {

    @Schema(description="과정 코드")
    private String curriculumCode;

    @Schema(description="회계구분")
    private String enforcementType;

    @Schema(description="담당자 아이디")
    private String managerDepartment;

    @Schema(description="본/지사 코드")
    private String province;

    @Schema(description="담당자")
    private String managerName;

    @Schema(description="강의 구분")
    private String categoryCode;

    @Schema(description="교육 과정 유형 (1: 화상 교육, 2: 집합 교육, 3: 이러닝 교육)")
    private String educationType;

    @Schema(description="교육 과정명")
    private String curriculumName;

    @Schema(description="교육시작일")
    private String operationBeginDateTime;

    @Schema(description="교육종료일")
    private String operationEndDateTime;

    @Schema(description="매체명")
    private String mediaName;

    @Schema(description="매체 구분1")
    private String mediaClsName1;

    @Schema(description="매체 구분2")
    private String mediaClsName2;

    @Schema(description="성명")
    private String userName;

    @Schema(description="핸드폰")
    private String phone;

    @Schema(description="이메일")
    private String email;

    @Schema(description="부서명")
    private String department;

    @Schema(description="직위")
    private String rank;

    @Schema(description="생년월일")
    private String birthDay;

    @Schema(description="성별")
    private String gender;

}
