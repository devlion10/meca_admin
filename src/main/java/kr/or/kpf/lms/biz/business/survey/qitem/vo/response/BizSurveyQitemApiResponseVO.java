package kr.or.kpf.lms.biz.business.survey.qitem.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.repository.entity.BizSurveyQitemItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 상호평가 문항 관련 응답 객체
 */
@Schema(name="BizSurveyQitemApiResponseVO", description="상호평가 문항 API 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizSurveyQitemApiResponseVO extends CSResponseVOSupport {

    @Schema(description="상호평가 문항 일련변호", example="1")
    private String bizSurveyQitemNo;

    @Schema(description="상호평가 문항 구분", example="기관")
    private Integer bizSurveyQitemCategory;

    @Schema(description="상호평가 문항 유형", example="기관")
    private Integer bizSurveyQitemType;

    @Schema(description="상호평가 문항명", example="기관평가")
    private String bizSurveyQitemName;

    @Schema(description="상호평가 문항 내용", example="기관평가")
    private String bizSurveyQitemContent;

    @Schema(description="상호평가 문항 기타 항목 여부", required = true, example="기관평가 입니다.")
    private Integer bizSurveyQitemEtc;

    @Schema(description="상호평가 문항 상태", example="1")
    private String bizSurveyQitemStatus;

    @Schema(description="상호평가 문항 선택 문항 리스트", required = false)
    private List<BizSurveyQitemItem> bizSurveyQitemItems;

}