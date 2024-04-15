package kr.or.kpf.lms.biz.business.instructor.clcln.ddln.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.business.instructor.clcln.ddln.vo.CreateBizInstructorClclnDdln;
import kr.or.kpf.lms.biz.business.instructor.clcln.ddln.vo.UpdateBizInstructorClclnDdln;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Schema(name="BizInstructorDistExcelVO", description="거리증빙 엑셀 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizInstructorClclnDdlnExcelVO {

    @ExcelColumn(headerName = "정산기간(년)")
    private Integer bizInstrClclnDdlnYr;

    @ExcelColumn(headerName = "정산기간(월)")
    private Integer bizInstrClclnDdlnMm;

    @ExcelColumn(headerName = "정산마감(일)")
    @Schema(description="정산 마감일 연월일", required = true, example="1")
    private String bizInstrClclnDdlnValue;

    @ExcelColumn(headerName = "정산마감(시간)")
    @Schema(description="정산 마감일 시각", required = true, example="11:00")
    private String bizInstrClclnDdlnTm;

    @ExcelColumn(headerName = "강의확인서 개수")
    @Schema(description="정산 마감일 시각", required = true, example="11:00")
    private Integer bizInstrIdentifyCount;

    @ExcelColumn(headerName = "금액(총합)")
    @Schema(description="정산 마감일 시각", required = true, example="11:00")
    private Integer amount;

    private String bizInstrClclnDdlnNo;

}
