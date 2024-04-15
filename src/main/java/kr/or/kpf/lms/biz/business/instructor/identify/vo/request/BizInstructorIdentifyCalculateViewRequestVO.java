package kr.or.kpf.lms.biz.business.instructor.identify.vo.request;

import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.*;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class BizInstructorIdentifyCalculateViewRequestVO extends CSViewVOSupport {

    /** 검색어 범위 */
    private String containTextType;

    /** 검색에 포함할 단어 */
    private String containText;

    /** 강의확인서 일련 번호 */
    private String bizInstrIdntyNo;

    /** 기관 신청 일련 번호 */
    private String bizOrgAplyNo;

    /** 강의확인서 상태 (0: 임시저장, 1: 제출(접수), 2: 기관 승인, 3: 관리자 승인(지출 접수), 4: 지출 완료, 9: 반려) */
    private Integer bizInstrIdntyStts;

    /** 등록자 id */
    private String registUserId;

    /** 연도 */
    private String year;

    /** 월 */
    private String month;

    /** 타입 (0:기본형, 1:평생교실, 2:운영학교, 3:자유학기제, 4:팩트교실) */
    private Integer bizPbancType;

    /** 차수 */
    private Integer bizPbancRnd;

    /** 제출처 */
    private String bizOrgAplyRgn;

    /** 제출처 */
    private String groupType;

    public Integer bizInstrAplyStts;
}
