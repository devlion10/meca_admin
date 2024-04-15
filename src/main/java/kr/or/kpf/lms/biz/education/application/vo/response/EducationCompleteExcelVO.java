package kr.or.kpf.lms.biz.education.application.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Schema(name="EducationCompleteExcelVO", description="교육수료 엑셀 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EducationCompleteExcelVO {

    @ExcelColumn(headerName = "과정명")
    @Schema(description="과정 명", example="")
    private String curriculumName;

    @ExcelColumn(headerName = "운영구분")
    @Schema(description="운영 타입(1: 기수 운영, 2: 상시 운영)", example="false")
    private String operationType;

    @ExcelColumn(headerName = "기수(연)")
    @Schema(description="기수(연)", example="false")
    private String yearOfEducationPlan;

    @ExcelColumn(headerName = "기수")
    @Schema(description="기수", example="false")
    private String yearOfEducationPlanStep;

    @Schema(description="담당부서", example="false")
    private String province;

    @ExcelColumn(headerName = "담당부서명")
    private String provinceName;

    @Schema(description="담당자 아이디", example="false")
    private String eduPlanPic;

    @ExcelColumn(headerName = "담당자명")
    private String adminName;

    @ExcelColumn(headerName = "이름")
    @Schema(description="이름", required = true, example="")
    private String userName;

    @ExcelColumn(headerName = "아이디")
    @Schema(description="아이디", required = true, example="")
    private String userId;

    @ExcelColumn(headerName = "생년월일")
    @Schema(description="생년월일", required = true, example="")
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

    @ExcelColumn(headerName = "지역")
    private String region;

    @Schema(description="지역", example="")
    private String organizationCode;

    @ExcelColumn(headerName = "NEIS 개인 번호")
    private String personalNiceNo;

    @ExcelColumn(headerName = "수업상태")
    @Schema(description="수업상태")
    private String educationState;

    @Schema(description="수료 여부")
    private String isComplete;

    @ExcelColumn(headerName = "수료")
    @Schema(description="수료")
    private String state;

    @Schema(description="0000000")
    private String programCompleteNumber;

    @Schema(description="0000-00-00 00:00:00")
    private String createDateTime;

    @ExcelColumn(headerName = "이수번호(교원)")
    private String completeNumber;
    
    @ExcelColumn(headerName = "진도율")
    @Schema(description="진도율")
    private Double progressRate;

    @ExcelColumn(headerName = "진도점수")
    @Schema(description="진도점수")
    private Integer progressScore;

    @ExcelColumn(headerName = "과제점수")
    @Schema(description="과제점수")
    private Integer assignmentScore;

    @ExcelColumn(headerName = "시험점수")
    @Schema(description="시험점수")
    private Integer examScore;

    @ExcelColumn(headerName = "교육 시작")
    @Schema(description="운영 시작 일시", example="2022-09-01 00:00:00")
    private String operationBeginDateTime;

    @ExcelColumn(headerName = "교육 종료")
    @Schema(description="운영 종료 일시", example="2022-09-01 23:59:59")
    private String operationEndDateTime;

    @ExcelColumn(headerName = "수료 일시")
    @Schema(description="수료 일시", example="2022-09-01 23:59:59")
    private String completeDateTime;


}
