package kr.or.kpf.lms.biz.business.survey.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.converter.DateToStringConverter;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import kr.or.kpf.lms.repository.entity.BizSurveyQitem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Schema(name="BizSurveyExcelVO", description="상호설문 엑셀 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizSurveyExcelVO {

    @Schema(description="일련 번호", required = true, example="1")
    private String bizSurveyNo;

    private Integer bizSurveyCtgr;

    @ExcelColumn(headerName = "구분")
    @Schema(description="구분", required = true, example="1")
    private String category;

    @ExcelColumn(headerName = "평가지명")
    @Schema(description="평가지명", required = true, example="1")
    private String bizSurveyNm;

    @ExcelColumn(headerName = "평가지내용")
    @Schema(description="평가지내용", required = true, example="1")
    private String bizSurveyCn;

    private Integer bizSurveyStts;

    @ExcelColumn(headerName = "상태")
    @Schema(description="일련 번호", required = true, example="1")
    private String state;

    @ExcelColumn(headerName = "질문")
    @Schema(description="일련 번호", required = true, example="1")
    private String qitem;


    @ExcelColumn(headerName = "등록일시")
    @Schema(description="등록 일시", example="")
    @Convert(converter= DateToStringConverter.class)
    private String createDateTime;

    @ExcelColumn(headerName = "수정일시")
    @Schema(description="수정일시", example="")
    @Convert(converter= DateToStringConverter.class)
    private String updateDateTime;


}
