package kr.or.kpf.lms.biz.business.instructor.identify.vo.request;

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
public class BizInstructorIdentifyCustomViewRequestVO extends CSViewVOSupport {

    /** 검색어 범위 */
    @ApiModelProperty(name="containTextType", value="검색어 범위", required=false, dataType="string", position=10, example = "")
    private String containTextType;

    /** 검색에 포함할 단어 */
    @ApiModelProperty(name="containText", value="검색에 포함할 단어", required=false, dataType="string", position=20, example = "")
    private String containText;

    /** 사업 템플릿 유형 */
    private Integer bizPbancType;

    /** 사업 차수 */
    private Integer bizPbancRnd;

    /** 강의확인서 일련 번호 */
    private String bizInstrIdntyNo;

    /** 강의확인서 연월 */
    private Integer bizInstrIdntyYm;

    /** 기관 신청 일련 번호 */
    private String bizOrgAplyNo;

    /** 기관 신청 지역 */
    private String bizOrgAplyRgn;

    /** 강의확인서 지출 상태 구분 */
    private Integer sttsType;

    /** 강의확인서 상태 (0: 임시저장, 1: 제출(접수), 2: 기관 승인, 3: 관리자 승인(지출 접수), 4: 지출 완료, 9: 반려) */
    private Integer bizInstrIdntyStts;

    /** 등록자 id */
    private String registUserId;

    /** 연도 */
    private String year;

    /** 월 */
    private String month;
}
