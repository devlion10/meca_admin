package kr.or.kpf.lms.biz.statistics.education.vo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="EducationStateResponseVO", description="학습 운영 통계 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EducationStateResponseVO extends CSViewVOSupport {

    @ExcelColumn(headerName = "교육 방식 전체")
    @Schema(description="교육 방식 - 화상")
    private Long eduType;

    @ExcelColumn(headerName = "교육 방식 - 화상")
    @Schema(description="교육 방식 - 화상")
    private Long eduTypeVideo;

    @ExcelColumn(headerName = "교육 방식 - 집합")
    @Schema(description="교육 방식 - 화상")
    private Long eduTypeOffline;

    @ExcelColumn(headerName = "교육 방식 - 이러닝")
    @Schema(description="교육 방식 - 화상")
    private Long eduTypeElearning;

    @ExcelColumn(headerName = "카테고리 전체")
    @Schema(description="카테고리 전체")
    private Long eduCategory;

    @ExcelColumn(headerName = "카테고리 - 1")
    @Schema(description="카테고리 - 1")
    private Long eduCategory1;

    @ExcelColumn(headerName = "카테고리- 2")
    @Schema(description="카테고리- 2")
    private Long eduCategory2;

    @ExcelColumn(headerName = "카테고리- 3")
    @Schema(description="카테고리- 3")
    private Long eduCategory3;

    @ExcelColumn(headerName = "예산 전체")
    @Schema(description="예산 전체")
    private Long eduExpense;

    @ExcelColumn(headerName = "예산 미지정")
    @Schema(description="예산 미지정")
    private Long eduExpense0;

    @ExcelColumn(headerName = "예산 법인")
    @Schema(description="예산 법인")
    private Long eduExpense1;

    @ExcelColumn(headerName = "예산 자발기금")
    @Schema(description="예산 자발기금")
    private Long eduExpense2;

}
