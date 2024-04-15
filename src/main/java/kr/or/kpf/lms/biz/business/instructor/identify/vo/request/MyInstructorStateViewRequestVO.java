package kr.or.kpf.lms.biz.business.instructor.identify.vo.request;

import io.swagger.annotations.ApiModelProperty;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import kr.or.kpf.lms.repository.entity.BizInstructorClclnDdln;
import lombok.*;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class MyInstructorStateViewRequestVO extends CSViewVOSupport {

    /** 검색어 범위 */
    @ApiModelProperty(name="containTextType", value="검색어 범위", required=false, dataType="string", position=10, example = "")
    private String containTextType;

    /** 검색에 포함할 단어 */
    @ApiModelProperty(name="containText", value="검색에 포함할 단어", required=false, dataType="string", position=20, example = "")
    private String containText;

    /** 사업 신청 지역 */
    private String bizOrgAplyRgn;

    /** 강사 id */
    private String bizInstrAplyInstrId;

    private String bizInstrAplyNo;

    private String notBizInstrAplyNo;

    private String bizInstrIdntyNo;

    private String searchYm;

    private Integer bizInstrIdntyStts;

    /** 강사 모집 공고 신청 강사 정보 상태 0: 임시 저장, 1: 신청, 2: 승인, 3: 반려, 9: 임시 승인 */
    private Integer bizInstrAplyStts;

    private String bizInstrIdntyYm;

}