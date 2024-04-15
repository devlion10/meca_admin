package kr.or.kpf.lms.biz.business.survey.ans.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.business.survey.ans.vo.UpdateBizSurveyAns;
import kr.or.kpf.lms.biz.business.survey.ans.vo.CreateBizSurveyAns;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

/**
 상호평가 - 응답 관련 요청 객체
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class BizSurveyAnsApiRequestVO {

    @Schema(description="상호평가 일련 번호", required = true, example="1")
    @NotEmpty(groups={UpdateBizSurveyAns.class}, message="상호평가 일련 번호는 필수 입니다.")
    private String bizSurveyAnsNo;

    @Schema(description="상호평가 문항 일련 번호", required = true, example="1")
    @NotEmpty(groups={CreateBizSurveyAns.class, UpdateBizSurveyAns.class}, message="상호평가 문항 일련 번호는 필수 입니다.")
    private String bizSurveyQitemNo;

    @Schema(description="상호평가 일련 번호", required = true, example="1")
    @NotEmpty(groups={CreateBizSurveyAns.class, UpdateBizSurveyAns.class}, message="상호평가 일련 번호는 필수 입니다.")
    private String bizSurveyNo;

    @Schema(description="사업 공고 신청 일련번호", example="1")
    @NotEmpty(groups={CreateBizSurveyAns.class, UpdateBizSurveyAns.class}, message="사업 공고 신청 일련번호는 필수 입니다.")
    private String bizOrgAplyNo;

    @Schema(description="상호평가 문항 답변", required = true, example="기관")
    @NotEmpty(groups={CreateBizSurveyAns.class, UpdateBizSurveyAns.class}, message="상호평가 문항 답변은 필수 입니다.")
    private String bizSurveyAnsCn;

    @Schema(description="상호평가 기타 항목 답변", required = false, example="기관평가")
    private String bizSurveyAnsEtc;

}
