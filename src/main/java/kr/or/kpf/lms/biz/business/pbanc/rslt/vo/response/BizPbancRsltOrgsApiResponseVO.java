package kr.or.kpf.lms.biz.business.pbanc.rslt.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 사업 공고 관련 응답 객체
 */
@Schema(name="BizPbancRsltOrgsApiResponseVO", description="사업공모 결과 API 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizPbancRsltOrgsApiResponseVO extends CSResponseVOSupport {

    @Schema(description="기관명", example="")
    private String organizationName;

    @Schema(description="수업 계획 - 교육기간 - 시작일 ", example="")
    private String bizOrgAplyLsnPlanBgng;

    @Schema(description="수업 계획 - 교육기간 - 종료일 ", example="")
    private String bizOrgAplyLsnPlanEnd;

    @Schema(description="사업 공고 신청 총 시간", example="")
    private double bizOrgAplyTime;

    @Schema(description="사업 공고 신청 담당자 이름", example="")
    private String bizOrgAplyPicNm;
}