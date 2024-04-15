package kr.or.kpf.lms.biz.business.survey.qitem.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.converter.DateToStringConverter;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;


@Schema(name="BizSurveyExcelVO", description="상호설문 질문 엑셀 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizSurveyQitemExcelVO {

    @Schema(description="상호평가 문항 일련변호", example="1")
    private String bizSurveyQitemNo;

    @Schema(description="상호평가 문항 구분", example="기관")
    private Integer bizSurveyQitemCategory;

    @ExcelColumn(headerName = "구분")
    @Schema(description="상호평가 문항 구분", example="기관")
    private String category;

    @Schema(description="상호평가 문항 유형", example="기관")
    private Integer bizSurveyQitemType;

    @ExcelColumn(headerName = "유형")
    @Schema(description="상호평가 문항 유형", example="기관")
    private String type;

    @ExcelColumn(headerName = "문항명")
    @Schema(description="상호평가 문항명", example="기관평가")
    private String bizSurveyQitemName;

    @ExcelColumn(headerName = "문항내용")
    @Schema(description="상호평가 문항 내용", example="기관평가")
    private String bizSurveyQitemContent;

    @Schema(description="상호평가 문항 기타 항목 여부", required = true, example="기관평가 입니다.")
    private Integer bizSurveyQitemEtc;

    @ExcelColumn(headerName = "기타항목")
    private String etc;

    @ExcelColumn(headerName = "구분")
    @Schema(description="상호평가 문항 상태", example="1")
    private String bizSurveyQitemStatus;


    @ExcelColumn(headerName = "등록일시")
    @Schema(description="등록 일시", example="")
    @Convert(converter= DateToStringConverter.class)
    private String createDateTime;

    @ExcelColumn(headerName = "수정일시")
    @Schema(description="수정일시", example="")
    @Convert(converter= DateToStringConverter.class)
    private String updateDateTime;

}
