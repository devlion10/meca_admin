package kr.or.kpf.lms.biz.business.organization.rpt.vo.request;

import io.swagger.annotations.ApiModelProperty;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.*;

/**
 결과보고서 요청 객체
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class BizOrganizationRsltRptViewRequestVO extends CSViewVOSupport {
    /** 검색어 범위 */
    @ApiModelProperty(name="containTextType", value="검색어 범위", required=false, dataType="string", position=10, example = "")
    private String containTextType;

    /** 검색에 포함할 단어 */
    @ApiModelProperty(name="containText", value="검색에 포함할 단어", required=false, dataType="string", position=20, example = "")
    private String containText;

    /** 결과보고서 일련 번호 */
    private String bizOrgRsltRptNo;

    /** 결과보고서 상태 */
    private Integer bizOrgRsltRptStts;

    /** 사업 공고 연도 */
    private Integer bizPbancYr;

    /** 사업 템플릿 유형 */
    private Integer bizPbancType;


}
