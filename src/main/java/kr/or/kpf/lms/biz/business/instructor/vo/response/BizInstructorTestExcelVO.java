package kr.or.kpf.lms.biz.business.instructor.vo.response;


import io.swagger.models.auth.In;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.business.organization.aply.vo.CreateBizOrganizationAply;
import kr.or.kpf.lms.biz.business.organization.aply.vo.UpdateBizOrganizationAply;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import kr.or.kpf.lms.repository.entity.BizInstructorAply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;


@Schema(name="BizInstructorTestExcelVO", description="강사배정 시뮬레이션 엑셀 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizInstructorTestExcelVO {
    @Schema(description="사업명", example="1")
    private String bizInstrNm;

    @Schema(description="상태", example="1")
    private Integer bizOrgAplyStts;

    @Schema(description="사업명", example="1")
    private String bizPbancNm;

    @Schema(description="기관이름", required = false, example="1")
    private String organizationName;

    @Schema(description="사업 공고 신청 지역", required = true, example="경기도")
    private String bizOrgAplyRgn;

    @Schema(description="사업 공고 신청 지역", required = true, example="성남시")
    private String organizationAddress1;

    @Schema(description="사업 공고 신청 총 시간", required = false, example="")
    private double bizOrgAplyTime;

    @Schema(description="기타 의견", required = false, example="1")
    private String bizOrgAplyLsnPlanEtc;

    @Schema(description="희망강사", required = false, example="1")
    private String bizOrgAplyLsnPlanEtcInstr;

    @ExcelColumn(headerName = "배정강사")
    @Schema(description="배정강사", required = false, example="1")
    private String bizOrgAplyLsnEtcInstr;

    @ExcelColumn(headerName = "신청강사(수)")
    @Schema(description="신청강사(수)", required = false, example="1")
    private Integer instrNumber;

    private List<BizInstructorAply> instrAply;

    private List<BizInstructorAply> instrAplyAll;


    private String bizOrgAplyNo;


    /** 사업 공고 코드 */
    private Integer bizPbancType;

    /** 사업 공고 수행 년도 */
    private Integer bizPbancYr;


    @ExcelColumn(headerName = "담당자 이름")
    @Schema(description="사업 공고 신청 담당자 이름", required = true, example="홍길동")
    private String bizOrgAplyPicNm;

    @ExcelColumn(headerName = "담당자 직위")
    @Schema(description="사업 공고 신청 담당자 직위", required = true, example="과장")
    private String bizOrgAplyPicJbgd;


    @ExcelColumn(headerName = "담당자 직통번호")
    @Schema(description="사업 공고 신청 담당자 번호", required = true, example="02-123-1234")
    private String bizOrgAplyPicTelno;


    @ExcelColumn(headerName = "담당자 핸드폰번호")
    @Schema(description="사업 공고 신청 담당자 핸드폰번호", required = true, example="010-1234-1234")
    private String bizOrgAplyPicMpno;

    @ExcelColumn(headerName = "담당자 이메일")
    @Schema(description="사업 공고 신청 담당자 이메일", required = true, example="hong@naver.com")
    private String bizOrgAplyPicEml;

    @ExcelColumn(headerName = "수업 대상")
    @Schema(description="수업 대상", required = false, example="1")
    private String bizOrgAplyLsnPlanTrgt;


}
