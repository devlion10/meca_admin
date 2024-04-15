package kr.or.kpf.lms.biz.contents.evaluate.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.converter.DateToStringConverter;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;

@Schema(name="EvaluateExcelVO", description="강의 평가 관련 엑셀 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluateExcelVO extends CSResponseVOSupport {

    @ExcelColumn(headerName = "제목")
    @Schema(description="강의 평가 제목")
    private String evaluateTitle;

    @ExcelColumn(headerName = "타입")
    @Schema(description="강의 평가 타입")
    private String evaluateType;

    @ExcelColumn(headerName = "내용")
    @Schema(description="강의 평가 내용")
    private String evaluateContents;

    @ExcelColumn(headerName = "상태")
    @Schema(description="사용 여부")
    private Boolean isUsable;

    @ExcelColumn(headerName = "등록자 ID")
    @Schema(description="등록자 ID", example="")
    private String registUserId;

    @ExcelColumn(headerName = "등록일시")
    @Schema(description="등록 일시", example="")
    @Convert(converter= DateToStringConverter.class)
    private String createDateTime;

    @ExcelColumn(headerName = "수정자 ID")
    @Schema(description="등록자 ID", example="")
    private String modifyUserId;

    @ExcelColumn(headerName = "수정일시")
    @Schema(description="수정일시", example="")
    @Convert(converter= DateToStringConverter.class)
    private String updateDateTime;


}
