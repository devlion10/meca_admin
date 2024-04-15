package kr.or.kpf.lms.biz.education.application.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Schema(name="EducationApplicationExcelVO", description="교육신청서 엑셀 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EducationApplicationExcelVO {
    @ExcelColumn(headerName = "과정명")
    @Schema(description="과정 명", example="")
    private String curriculumName;

    @ExcelColumn(headerName = "교육 유형")
    @Schema(description="교육 유형", example="")
    private String educationType;

    @ExcelColumn(headerName = "선택 교육 유형")
    @Schema(description="선택 교육 유형", example="")
    private String setEducationType;

    @ExcelColumn(headerName = "운영구분")
    @Schema(description="운영 타입(1: 기수 운영, 2: 상시 운영)", example="false")
    private String operationType;

    @ExcelColumn(headerName = "기수(연)")
    @Schema(description="기수(연)", example="false")
    private String yearOfEducationPlan;

    @ExcelColumn(headerName = "기수")
    @Schema(description="기수", example="false")
    private String yearOfEducationPlanStep;

    @ExcelColumn(headerName = "신청상태")
    @Schema(description="관리자 승인 상태", required = true, example="")
    private String adminApprovalState;

    @ExcelColumn(headerName = "이름")
    @Schema(description="이름", required = true, example="")
    private String userName;

    @ExcelColumn(headerName = "아이디")
    @Schema(description="아이디", required = true, example="")
    private String userId;

    @Schema(description="성별", required = true, example="")
    private String gender;

    @ExcelColumn(headerName = "성별")
    private String genderString;

    @ExcelColumn(headerName = "생년월일")
    @Schema(description="생년월일", example="")
    private String birthDay;

    @ExcelColumn(headerName = "매체사/기관명")
    private String organizationName;

    @ExcelColumn(headerName = "매체명")
    private String mediaName;

    @ExcelColumn(headerName = "부서")
    @Schema(description="부서", example="")
    private String department;

    @ExcelColumn(headerName = "직급")
    @Schema(description="직급", example="")
    private String rank;

    @ExcelColumn(headerName = "직책")
    @Schema(description="직책", example="")
    private String position;

    @ExcelColumn(headerName = "연락처")
    @Schema(description="회원 연락처", example="")
    private String phone;

    @ExcelColumn(headerName = "이메일")
    @Schema(description="이메일", example="")
    private String email;

    @ExcelColumn(headerName = "NEIS 개인 번호")
    private String personalNiceNo;


    @ExcelColumn(headerName = "신청기간 시작")
    @Schema(description="운영 시작 일시", example="2022-09-01 00:00:00")
    private String applyBeginDateTime;

    @ExcelColumn(headerName = "신청기간 종료")
    @Schema(description="운영 종료 일시", example="2022-09-01 23:59:59")
    private String applyEndDateTime;

    @ExcelColumn(headerName = "신청일시")
    @Schema(description="등록 일시", example="")
    private String createDateTime;
}
