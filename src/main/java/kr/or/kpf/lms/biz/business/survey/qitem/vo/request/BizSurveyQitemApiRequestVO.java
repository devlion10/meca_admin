package kr.or.kpf.lms.biz.business.survey.qitem.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.business.survey.qitem.vo.CreateBizSurveyQitem;
import kr.or.kpf.lms.biz.business.survey.qitem.vo.UpdateBizSurveyQitem;
import kr.or.kpf.lms.repository.entity.BizSurveyQitemItem;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 상호평가 문항 관련 요청 객체
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class BizSurveyQitemApiRequestVO {
    @Schema(description="상호평가 문항 일련변호", required = true, example="1")
    @NotEmpty(groups={CreateBizSurveyQitem.class, UpdateBizSurveyQitem.class}, message="상호평가 문항 일련 번호는 필수 입니다.")
    private String bizSurveyQitemNo;

    @Schema(description="상호평가 문항 구분", required = true, example="기관")
    @NotNull(groups={CreateBizSurveyQitem.class, UpdateBizSurveyQitem.class}, message="상호평가 문항 구분은 필수 입니다.")
    private Integer bizSurveyQitemCategory;

    @Schema(description="상호평가 문항 유형", required = true, example="기관")
    @NotNull(groups={CreateBizSurveyQitem.class, UpdateBizSurveyQitem.class}, message="상호평가 문항 유형은 필수 입니다.")
    private Integer bizSurveyQitemType;

    @Schema(description="상호평가 문항명", required = true, example="기관평가")
    @NotEmpty(groups={CreateBizSurveyQitem.class, UpdateBizSurveyQitem.class}, message="상호평가 문항명은 필수 입니다.")
    private String bizSurveyQitemName;

    @Schema(description="상호평가 문항 내용", required = true, example="기관평가 입니다.")
    @NotEmpty(groups={CreateBizSurveyQitem.class, UpdateBizSurveyQitem.class}, message="상호평가 문항 내용은 필수 입니다.")
    private String bizSurveyQitemContent;

    @Schema(description="상호평가 문항 기타 항목 여부", required = true, example="")
    @NotNull(groups={CreateBizSurveyQitem.class, UpdateBizSurveyQitem.class}, message="상호평가 문항 기타 항목 여부는 필수 입니다.")
    private Integer bizSurveyQitemEtc;

    @Schema(description="상호평가 문항 상태", required = true, example="1")
    @NotEmpty(groups={CreateBizSurveyQitem.class, UpdateBizSurveyQitem.class}, message="상호평가 문항 상태는 필수 입니다.")
    private String bizSurveyQitemStatus;

    @Schema(description="상호평가 문항 선택 문항 리스트", required = true, example="1")
    private List<BizSurveyQitemItem> bizSurveyQitemItems;

}
