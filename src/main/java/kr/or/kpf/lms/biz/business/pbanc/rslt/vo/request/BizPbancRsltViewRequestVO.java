package kr.or.kpf.lms.biz.business.pbanc.rslt.vo.request;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.*;

/**
 사업 공고 결과 관련 요청 객체
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class BizPbancRsltViewRequestVO extends CSViewVOSupport {

    /** 검색어 범위 */
    @ApiModelProperty(name="containTextType", value="검색어 범위", required=false, dataType="string", position=10, example = "")
    private String containTextType;

    /** 검색에 포함할 단어 */
    @ApiModelProperty(name="containText", value="검색에 포함할 단어", required=false, dataType="string", position=20, example = "")
    private String containText;

    /** 사업 공고 결과 코드 */
    private String bizPbancRsltNo;

    /** 사업 공고 코드 */
    private String bizPbancNo;

    /** 사업 공고 수행 년도 */
    private Integer bizPbancYr;
}
