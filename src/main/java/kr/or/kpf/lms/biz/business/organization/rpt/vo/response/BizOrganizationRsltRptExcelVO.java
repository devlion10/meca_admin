package kr.or.kpf.lms.biz.business.organization.rpt.vo.response;


import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="BizOrganizationRsltRptExcelVO", description="결과보고서 엑셀 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizOrganizationRsltRptExcelVO {
    @ExcelColumn(headerName = "사업명")
    @Schema(description="사업명", example="1")
    private String bizPbancNm;

    @ExcelColumn(headerName = "신청기관")
    @Schema(description="기관이름", required = false, example="1")
    private String organizationName;

    @ExcelColumn(headerName = "작성자명")
    @Schema(description="등록자 ID", example="")
    private String userName;

    @ExcelColumn(headerName = "작성자 아이디")
    @Schema(description="등록자 ID", example="")
    private String userId;

    @ExcelColumn(headerName = "작성자 연락처")
    @Schema(description="등록자 ID", example="")
    private String phone;

    @ExcelColumn(headerName = "교육시작일")
    @Schema(description="수업 계획 - 교육기간 - 시작일", required = false, example="2022-12-01")
    private String bizOrgAplyLsnPlanBgng;

    @ExcelColumn(headerName = "교육종료일")
    @Schema(description="수업 계획 - 교육기간 - 종료일", required = false, example="2022-12-20")
    private String bizOrgAplyLsnPlanEnd;

    @ExcelColumn(headerName = "승인강사")
    @Schema(description="승인강사 이름", required = false, example="홍길동")
    private String bizInstr;

    @Schema(description="상태(0임시저장, 1접수 2승인 9반려)")
    private Integer bizOrgRsltRptStts;

    @ExcelColumn(headerName = "상태")
    @Schema(description="상태", required = false, example="홍길동")
    private String state;

    private String bizOrgAplyNo;

}
