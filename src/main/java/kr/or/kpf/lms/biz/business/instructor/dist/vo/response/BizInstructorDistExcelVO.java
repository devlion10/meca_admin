package kr.or.kpf.lms.biz.business.instructor.dist.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Schema(name="BizInstructorDistExcelVO", description="거리증빙 엑셀 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizInstructorDistExcelVO {
    @ExcelColumn(headerName = "번호")
    @Schema(description="거리 증빙 일련 번호", required = true, example="1")
    private String bizInstrDistNo;

    @ExcelColumn(headerName = "사업명")
    @Schema(description="사업명", example="1")
    private String bizPbancNm;

    @ExcelColumn(headerName = "수행기관")
    @Schema(description="기관이름", required = false, example="1")
    private String organizationName;

    @ExcelColumn(headerName = "교육시작일")
    @Schema(description="수업 계획 - 교육기간 - 시작일", required = false, example="2022-12-01")
    private String bizOrgAplyLsnPlanBgng;

    @ExcelColumn(headerName = "교육종료일")
    @Schema(description="수업 계획 - 교육기간 - 종료일", required = false, example="2022-12-20")
    private String bizOrgAplyLsnPlanEnd;

    @ExcelColumn(headerName = "강사명")
    @Schema(description="강사 모집 공고 신청 강사명", required = false, example="홍길동")
    private String bizInstrAplyInstrNm;

    @ExcelColumn(headerName = "아이디")
    @Schema(description="강사 모집 공고 신청 아이디", required = true, example="abc")
    private String bizInstrAplyInstrId;

    @ExcelColumn(headerName = "출발지명")
    @Schema(description="거리 증빙 출발지명", required = true, example="경기")
    private String bizDistBgngNm;

    @ExcelColumn(headerName = "출발지주소")
    @Schema(description="거리 증빙 출발지 주소", required = true, example="2022-12-01")
    private String bizDistBgngAddr;

    @ExcelColumn(headerName = "도착지명")
    @Schema(description="거리 증빙 도착지명", required = true, example="2022-12-01")
    private String bizDistEndNm;

    @ExcelColumn(headerName = "도착지주소")
    @Schema(description="거리 증빙 도착지 주소", required = true, example="3")
    private String bizDistEndAddr;

    @ExcelColumn(headerName = "거리")
    @Schema(description="거리 증빙 거리", required = false, example="1")
    private Double bizDistValue;

    @ExcelColumn(headerName = "강의료")
    @Schema(description="거리 증빙 금액", required = false, example="1")
    private Integer bizDistAmt;

    @Schema(description="거리 증빙 상태", required = true, example="0")
    private Integer bizDistStts;

    @ExcelColumn(headerName = "상태")
    @Schema(description="거리 증빙 상태", required = true, example="0")
    private String state;

}
