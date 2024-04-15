package kr.or.kpf.lms.biz.business.organization.aply.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.business.organization.aply.vo.CreateBizOrganizationAply;
import kr.or.kpf.lms.biz.business.organization.aply.vo.UpdateBizOrganizationAply;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 사업 공고 신청 응답 객체
 */
@Schema(name="BizOrganizationAplyApiResponseVO", description="사업 공고 신청 API 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizOrganizationAplyApiResponseVO extends CSResponseVOSupport {

    @Schema(description="사업 공고 신청 일련 번호", example="1")
    private String bizOrgAplyNo;

    @Schema(description="사업 공고 일련 번호", required = true, example="PAC000001")
    private String bizPbancNo;

    @Schema(description="기관 일련 번호", required = true, example="ORG000001")
    private String orgCd;

    @ExcelColumn(headerName = "대표자명")
    @Schema(description="사업 공고 신청 기관 대표자명", required = true, example="ORG000001")
    private String bizOrgAplyRprsvNm;

    @ExcelColumn(headerName = "총 신청 시간")
    @Schema(description="사업 공고 신청 총 시간", required = false, example="")
    private double bizOrgAplyTime;

    @Schema(description="사업 공고 신청 최초 총 시간", required = false, example="")
    private double bizOrgAplyTimeFrst;

    @ExcelColumn(headerName = "지역")
    @Schema(description="사업 공고 신청 지역", required = true, example="홍길동")
    @NotEmpty(groups={CreateBizOrganizationAply.class, UpdateBizOrganizationAply.class}, message="사업 공고 신청 지역은 필수 입니다.")
    private String bizOrgAplyRgn;

    @ExcelColumn(headerName = "담당자명")
    @Schema(description="사업 공고 신청 담당자 이름", required = true, example="홍길동")
    @NotEmpty(groups={CreateBizOrganizationAply.class, UpdateBizOrganizationAply.class}, message="사업 공고 신청 담당자 이름은 필수 입니다.")
    private String bizOrgAplyPicNm;

    @ExcelColumn(headerName = "담당자 직위")
    @Schema(description="사업 공고 신청 담당자 직위", required = true, example="과장")
    @NotEmpty(groups={CreateBizOrganizationAply.class, UpdateBizOrganizationAply.class}, message="사업 공고 신청 담당자 직위은 필수 입니다.")
    private String bizOrgAplyPicJbgd;

    @ExcelColumn(headerName = "담당자 이메일")
    @Schema(description="사업 공고 신청 담당자 이메일", required = true, example="hong@naver.com")
    @NotEmpty(groups={CreateBizOrganizationAply.class, UpdateBizOrganizationAply.class}, message="사업 공고 신청 담당자 이메일은 필수 입니다.")
    private String bizOrgAplyPicEml;

    @ExcelColumn(headerName = "담당자 번호")
    @Schema(description="사업 공고 신청 담당자 번호", required = true, example="02-123-1234")
    @NotEmpty(groups={CreateBizOrganizationAply.class, UpdateBizOrganizationAply.class}, message="사업 공고 신청 담당자 번호은 필수 입니다.")
    private String bizOrgAplyPicTelno;

    @ExcelColumn(headerName = "담당자 핸드폰번호")
    @Schema(description="사업 공고 신청 담당자 핸드폰번호", required = true, example="010-1234-1234")
    @NotEmpty(groups={CreateBizOrganizationAply.class, UpdateBizOrganizationAply.class}, message="사업 공고 신청 담당자 핸드폰번호은 필수 입니다.")
    private String bizOrgAplyPicMpno;

    @ExcelColumn(headerName = "상태(0: 임시저장, 1: 신청, 2: 승인, 3: 반려, 7: 종료, 9: 가승인)")
    @Schema(description="사업공고 신청 접수 상태 - 0: 임시저장, 1: 신청, 2: 승인, 3: 반려, 7: 종료, 9: 가승인", required = true, example="0")
    @NotNull(groups={CreateBizOrganizationAply.class, UpdateBizOrganizationAply.class}, message="사업 공고 신청 접수 상태은 필수 입니다.")
    private Integer bizOrgAplyStts;

    @ExcelColumn(headerName = "상태 변경 사유")
    @Schema(description="상태 변경 사유", required = false, example="-")
    private String bizOrgAplySttsCmnt;

    @ExcelColumn(headerName = "변경 가능 여부(0: 불가능, 1: 가능)")
    @Schema(description="변경 가능 여부", required = true, example="-")
    private Integer bizOrgAplyChgYn;

    @ExcelColumn(headerName = "운영 구분")
    @Schema(description="운영 구분", required = true, example="모집")
    private String bizOrgAplyOperCtgr;

    @ExcelColumn(headerName = "수업 형태")
    @Schema(description="수업 형태(운영 방법)", required = false, example="1")
    private String bizOrgAplyOperMeth;

    @ExcelColumn(headerName = "교육기간 시작일")
    @Schema(description="수업 계획 - 교육기간 - 시작일", required = false, example="2022-12-01")
    private String bizOrgAplyLsnPlanBgng;

    @ExcelColumn(headerName = "교육기간 종료일")
    @Schema(description="수업 계획 - 교육기간 - 종료일", required = false, example="2022-12-20")
    private String bizOrgAplyLsnPlanEnd;

    @ExcelColumn(headerName = "수업 대상")
    @Schema(description="수업 대상", required = false, example="1")
    private String bizOrgAplyLsnPlanTrgt;

    @ExcelColumn(headerName = "회차당 교육 인원")
    @Schema(description="회차당 교육 인원", required = false, example="1")
    private Integer bizOrgAplyLsnPlanNope;

    @ExcelColumn(headerName = "교육의 방향성 및 신청한 이유")
    @Schema(description="설명1(교육의 방향성 및 신청한 이유)", required = false, example="1")
    private String bizOrgAplyLsnPlanDscr1;

    @ExcelColumn(headerName = "미디어교육 운영 현황(최근 3년 이내 중심)")
    @Schema(description="설명2(미디어교육 운영 현황(최근 3년 이내 중심))", required = false, example="1")
    private String bizOrgAplyLsnPlanDscr2;

    @ExcelColumn(headerName = "향후 활용계획 및 수업을 통한 기대효과")
    @Schema(description="설명3(향후 활용계획 및 수업을 통한 기대효과)", required = false, example="1")
    private String bizOrgAplyLsnPlanDscr3;

    @ExcelColumn(headerName = "희망강사")
    @Schema(description="건의사항 - 희망강사", required = false, example="1")
    private String bizOrgAplyLsnPlanEtcInstr;

    @ExcelColumn(headerName = "기타 의견")
    @Schema(description="기타 의견", required = false, example="1")
    private String bizOrgAplyLsnPlanEtc;

    @ExcelColumn(headerName = "")
    @Schema(description="신문 신청 여부(없음/0: N, 1: Y)", required = false, example="1")
    private Integer bizOrgAplyPeprYn;

    @ExcelColumn(headerName = "사업 공고 신청 첨부파일")
    @Schema(description="사업 공고 신청 첨부파일 (신청서)", required = false, example="1")
    private String bizOrgAplyFile;
}