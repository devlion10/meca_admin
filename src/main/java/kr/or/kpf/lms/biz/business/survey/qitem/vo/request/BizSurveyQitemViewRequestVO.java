package kr.or.kpf.lms.biz.business.survey.qitem.vo.request;

import io.swagger.annotations.ApiModelProperty;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import kr.or.kpf.lms.repository.entity.BizSurveyQitemItem;
import lombok.*;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 상호평가 문항 관련 요청 객체
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class BizSurveyQitemViewRequestVO extends CSViewVOSupport {

    /** 검색어 범위 */
    @ApiModelProperty(name="containTextType", value="검색어 범위", required=false, dataType="string", position=10, example = "")
    private String containTextType;

    /** 검색에 포함할 단어 */
    @ApiModelProperty(name="containText", value="검색에 포함할 단어", required=false, dataType="string", position=20, example = "")
    private String containText;

    /** 상호평가 문항 일련 번호 */
    private String bizSurveyQitemNo;

    /** 상호평가 문항 구분 */
    private Integer bizSurveyQitemCategory;

    /** 상호평가 문항 유형 */
    private Integer bizSurveyQitemType;

    /** 상호평가 문항 상태 */
    private String bizSurveyQitemStatus;
}
