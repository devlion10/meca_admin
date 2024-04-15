package kr.or.kpf.lms.biz.business.organization.aply.vo.request;

import io.swagger.annotations.ApiModelProperty;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.*;

/**
 강의확인서 관련 요청 객체
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class BizOrganizationAplyCustomViewRequestVO extends CSViewVOSupport {

    /** 검색어 범위 */
    @ApiModelProperty(name="containTextType", value="검색어 범위", required=false, dataType="string", position=10, example = "")
    private String containTextType;

    /** 검색에 포함할 단어 */
    @ApiModelProperty(name="containText", value="검색에 포함할 단어", required=false, dataType="string", position=20, example = "")
    private String containText;

    /** 사업 템플릿 유형 */
    private Integer bizPbancType;

    /** 사업 년도 */
    private Integer bizPbancYr;

    /** 사업 차수 */
    private Integer bizPbancRnd;

    /** 기관 신청 일련 번호 */
    private String bizOrgAplyNo;

    /** 강의확인서 일련 번호 */
    private String bizInstrIdntyNo;

    /** 강의확인서 상태 (0: 임시저장, 1: 제출(접수), 2: 기관 승인, 3: 관리자 승인(지출 접수), 4: 지출 완료, 9: 반려) */
    private Integer bizInstrIdntyStts;

    /** 등록자 id */
    private String registUserId;
}
