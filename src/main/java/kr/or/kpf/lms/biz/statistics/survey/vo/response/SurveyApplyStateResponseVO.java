package kr.or.kpf.lms.biz.statistics.survey.vo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="SurveyApplyStateResponseVO", description="상호평가 통계 - 신청서별 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SurveyApplyStateResponseVO extends CSViewVOSupport {

    @Schema(description="상호평가 평가지 일련 번호")
    private String bizSurveyNo;

    @Schema(description="상호평가 평가 대상 일련 번호")
    private String bizSurveyTrgtNo;

    @Schema(description="상호평가 평가지 구분")
    private Integer bizSurveyCtgr;

    @ExcelColumn(headerName = "구분")
    @Schema(description="상호평가 평가지 구분명")
    private String bizSurveyCtgrNm;

    @ExcelColumn(headerName = "평가지명")
    @Schema(description="상호평가 평가지명")
    private String bizSurveyNm;

    @ExcelColumn(headerName = "사업명")
    @Schema(description="상호평가 신청서 사업명")
    private String bizPbancNm;

    @Schema(description="상호평가 평가지 상태")
    private Integer bizSurveyStts;

    @ExcelColumn(headerName = "평가자 아이디")
    @Schema(description="상호평가 평가자 아이디")
    private String evaluatorId;

    @ExcelColumn(headerName = "평가자명")
    @Schema(description="상호평가 평가자명")
    private String evaluatorNm;

    @ExcelColumn(headerName = "평가 대상")
    private String evaluated;

    @ExcelColumn(headerName = "평가 내용")
    private String content;

    @ExcelColumn(headerName = "평점")
    private double score;

    @ExcelColumn(headerName = "코멘트")
    @Schema(description="상호평가 코멘트")
    private String comment;

    @Schema(description="정렬 구분 생성일자")
    private String createDateTime;

}
