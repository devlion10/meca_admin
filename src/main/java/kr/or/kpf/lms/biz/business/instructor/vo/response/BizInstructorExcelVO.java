package kr.or.kpf.lms.biz.business.instructor.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Schema(name="BizInstructorExcelVO", description="강사배정 엑셀 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizInstructorExcelVO {

    @ExcelColumn(headerName = "사업년도")
    @Schema(description="사업년도", example="1")
    private Integer bizPbancYr;

    @ExcelColumn(headerName = "차수")
    @Schema(description="사업 공고 차수", example="1")
    private Integer bizPbancRnd;

    @ExcelColumn(headerName = "사업명")
    @Schema(description="사업명", example="1")
    private String bizPbancNm;

    @ExcelColumn(headerName = "신청기관")
    @Schema(description="기관이름", required = false, example="1")
    private String organizationName;

    @ExcelColumn(headerName = "담당자 이름")
    @Schema(description="사업 공고 신청 담당자 이름", required = true, example="홍길동")
    private String bizOrgAplyPicNm;

    @ExcelColumn(headerName = "담당자연락처")
    @Schema(description="사업 공고 신청 담당자 핸드폰번호", required = true, example="010-1234-1234")
    private String bizOrgAplyPicMpno;

    @ExcelColumn(headerName = "교육시작일")
    @Schema(description="수업 계획 - 교육기간 - 시작일", required = false, example="2022-12-01")
    private String bizOrgAplyLsnPlanBgng;

    @ExcelColumn(headerName = "교육종료일")
    @Schema(description="수업 계획 - 교육기간 - 종료일", required = false, example="2022-12-20")
    private String bizOrgAplyLsnPlanEnd;

    @ExcelColumn(headerName = "승인강사")
    @Schema(description="승인강사 이름", required = false, example="홍길동")
    private String bizInstr;

    private String bizOrgAplyNo;

    private String bizInstrNo;

}
