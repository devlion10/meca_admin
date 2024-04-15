package kr.or.kpf.lms.biz.business.organization.aply.vo.request;

import io.swagger.annotations.ApiModelProperty;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.*;

import javax.persistence.Column;

/**
 사업 공고 신청 요청 객체
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class BizOrganizationAplyViewRequestVO extends CSViewVOSupport {

    /** 검색어 범위 */
    @ApiModelProperty(name="containTextType", value="검색어 범위", required=false, dataType="string", position=10, example = "")
    private String containTextType;

    /** 검색에 포함할 단어 */
    @ApiModelProperty(name="containText", value="검색에 포함할 단어", required=false, dataType="string", position=20, example = "")
    private String containText;

    /** 사업 공고 신청 일련 번호 */
    private String bizOrgAplyNo;

    /** 사업공고 신청 접수 상태 - 0: 임시저장, 1: 신청, 2: 승인, 3: 반려, 7: 종료, 9: 가승인 */
    private Integer bizOrgAplyStts;

    /** 사업 템플릿 유형 */
    private Integer bizPbancType;

    /** 사업 공고 연도 */
    private Integer bizPbancYr;

    /** 사업 공고 차수 */
    private Integer bizPbancRnd;

    private Integer isDist;

    /** 사업 공고 일련 번호 */
    private String bizPbancNo;

    /** 사업 공고 임시/승인 기관 목록 구분 */
    private Integer bizOrgAplyPbancList;
}
