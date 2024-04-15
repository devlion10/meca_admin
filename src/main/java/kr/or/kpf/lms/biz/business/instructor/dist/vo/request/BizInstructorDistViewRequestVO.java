package kr.or.kpf.lms.biz.business.instructor.dist.vo.request;

import io.swagger.annotations.ApiModelProperty;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.*;

import javax.persistence.Column;

/**
 거리 증빙 관련 요청 객체
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class BizInstructorDistViewRequestVO extends CSViewVOSupport {

    /** 검색어 범위 */
    @ApiModelProperty(name="containTextType", value="검색어 범위", required=false, dataType="string", position=10, example = "")
    private String containTextType;

    /** 검색에 포함할 단어 */
    @ApiModelProperty(name="containText", value="검색에 포함할 단어", required=false, dataType="string", position=20, example = "")
    private String containText;

    /** 사업 공고 일련 번호 */
    private String bizPbancNo;

    /** 사업 템플릿 유형 */
    private Integer bizPbancType;

    /** 사업 년도 */
    private Integer bizPbancYr;
    
    /** 사업 차수 */
    private Integer bizPbancRnd;

    /** 거리 증빙 일련 번호 */
    private String bizInstrDistNo;

    /** 거리 증빙 상태 */
    private Integer bizDistStts;
}
