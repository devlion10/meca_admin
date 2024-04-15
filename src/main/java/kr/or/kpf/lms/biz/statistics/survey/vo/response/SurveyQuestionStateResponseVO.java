package kr.or.kpf.lms.biz.statistics.survey.vo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import kr.or.kpf.lms.repository.entity.BizSurveyQitem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Schema(name="SurveyQuestionStateResponseVO", description="상호평가 통계 - 평가지별 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SurveyQuestionStateResponseVO extends CSViewVOSupport {

    @Schema(description="상호평가 평가지 일련 번호")
    private String bizSurveyNo;

    @Schema(description="상호평가 평가지 구분")
    private Integer bizSurveyCtgr;

    @ExcelColumn(headerName = "구분")
    @Schema(description="상호평가 평가지 구분명")
    private String bizSurveyCtgrNm;

    @ExcelColumn(headerName = "평가지명")
    @Schema(description="상호평가 평가지명")
    private String bizSurveyNm;

    @Schema(description="상호평가 평가지 상태")
    private Integer bizSurveyStts;

    @ExcelColumn(headerName = "평가자 아이디")
    @Schema(description="상호평가 평가자 아이디")
    private String evaluatorId;

    @ExcelColumn(headerName = "평가자명")
    @Schema(description="상호평가 평가자명")
    private String evaluatorNm;

    @ExcelColumn(headerName = "평가 대상")
    @Schema(description="상호평가 평가 대상")
    private String evaluated;

    @ExcelColumn(headerName = "평점")
    private double score;

    @ExcelColumn(headerName = "코멘트")
    @Schema(description="상호평가 코멘트")
    private String comment;
}
