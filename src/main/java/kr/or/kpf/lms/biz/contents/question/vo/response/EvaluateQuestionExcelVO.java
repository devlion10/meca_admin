package kr.or.kpf.lms.biz.contents.question.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.contents.question.controller.EvaluateQuestionApiController;
import kr.or.kpf.lms.common.converter.DateToStringConverter;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;
import javax.validation.constraints.NotEmpty;

@Schema(name="EvaluateQuestionExcelVO", description="강의 평가 관련 엑셀 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluateQuestionExcelVO extends CSResponseVOSupport {

    @ExcelColumn(headerName = "질문 제목")
    @Schema(description="질문 제목", required = true, example="어제의 날씨는 어땠나요?")
    private String questionTitle;

    @ExcelColumn(headerName = "카테고리(1: 강사평가, 2: 기관평가)")
    @Schema(description="강의평가 질문 카테고리", required = true, example="1")
    private String questionCategory;

    @ExcelColumn(headerName = "유형(1:단일, 2:다중, 3:단답, 4:서술형)")
    @Schema(description="질문 유형 코드", required = true, example="1")
    private String questionType;

    @ExcelColumn(headerName = "질문 내용")
    @Schema(description="질문 내용")
    private String questionContents;

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
