package kr.or.kpf.lms.biz.business.instructor.question.vo.request;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.business.instructor.question.vo.CreateBizInstructorQuestion;
import kr.or.kpf.lms.biz.business.instructor.question.vo.UpdateBizInstructorQuestion;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

/**
 강사 지원 문의 관련 요청 객체
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class BizInstructorQuestionViewRequestVO extends CSViewVOSupport {

    /** 검색어 범위 */
    @ApiModelProperty(name="containTextType", value="검색어 범위", required=false, dataType="string", position=10, example = "")
    private String containTextType;

    /** 검색에 포함할 단어 */
    @ApiModelProperty(name="containText", value="검색에 포함할 단어", required=false, dataType="string", position=20, example = "")
    private String containText;

    /** 강사 지원 문의 일련 번호 */
    private String bizInstrQstnNo;

    /** 사업 공고 신청 일련 번호 */
    private String bizOrgAplyNo;

}
