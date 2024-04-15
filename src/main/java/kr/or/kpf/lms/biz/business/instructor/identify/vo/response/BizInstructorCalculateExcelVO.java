package kr.or.kpf.lms.biz.business.instructor.identify.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="BizInstructorCalculateExcelVO", description="정산금액 엑셀 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizInstructorCalculateExcelVO {

    @ExcelColumn(headerName = "강사명")
    @Schema(description="강사 모집 공고 신청 강사명", required = true, example="홍길동")
    private String userName;

    @ExcelColumn(headerName = "기관이름")
    @Schema(description="기관이름", required = false, example="1")
    private String organizationName;

    @ExcelColumn(headerName = "생년월일")
    @Schema(description="생년월일", required = false, example="1")
    private String birthDay;

    @ExcelColumn(headerName = "교육시작일")
    @Schema(description="수업 계획 - 교육기간 - 시작일", required = false, example="2022-12-01")
    private String bizOrgAplyLsnPlanBgng;

    @ExcelColumn(headerName = "교육종료일")
    @Schema(description="수업 계획 - 교육기간 - 종료일", required = false, example="2022-12-20")
    private String bizOrgAplyLsnPlanEnd;

    @ExcelColumn(headerName = "배정시간")
    @Schema(description="강의시간")
    private Integer bizPlanTime;

    @ExcelColumn(headerName = "출강회수")
    @Schema(description="강의시간")
    private Integer bizDtlCount;

    @ExcelColumn(headerName = "거리")
    @Schema(description="거리 증빙 거리", required = false, example="1")
    private Double bizDistValue;

    @ExcelColumn(headerName = "강의료(시간당)")
    @Schema(description="거리 증빙 금액", required = false, example="1")
    private Integer bizDistAmt;

    @ExcelColumn(headerName = "강의시간")
    @Schema(description="거리 증빙 금액", required = false, example="1")
    private Integer bizTime;

    @ExcelColumn(headerName = "강의료(합계)")
    @Schema(description="거리 증빙 금액", required = false, example="1")
    private Integer bizDistAmtAll;

    @ExcelColumn(headerName = "지급액")
    @Schema(description="거리 증빙 금액", required = false, example="1")
    private Integer bizDistAmtPaid;

    @ExcelColumn(headerName = "잔여시간")
    @Schema(description="거리 증빙 금액", required = false, example="1")
    private Integer bizRestTime;

    @ExcelColumn(headerName = "잔액")
    @Schema(description="거리 증빙 금액", required = false, example="1")
    private Integer bizDistAmtRest;

}
