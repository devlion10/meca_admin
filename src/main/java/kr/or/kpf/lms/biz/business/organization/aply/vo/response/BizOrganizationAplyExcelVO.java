package kr.or.kpf.lms.biz.business.organization.aply.vo.response;

import io.swagger.models.auth.In;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.business.organization.aply.vo.CreateBizOrganizationAply;
import kr.or.kpf.lms.biz.business.organization.aply.vo.UpdateBizOrganizationAply;
import kr.or.kpf.lms.common.converter.DateToStringConverter;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.validation.constraints.NotEmpty;

@Schema(name="BizOrganizationAplyExcelVO", description="사업 공고 신청 엑셀 데이터 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizOrganizationAplyExcelVO extends CSResponseVOSupport {

    @ExcelColumn(headerName = "번호")
    @Schema(description="사업 공고 신청 일련 번호", example="1")
    private String bizOrgAplyNo;

    @Schema(description="사업명", example="1")
    private Integer bizPbancType;

    @ExcelColumn(headerName = "구분")
    @Schema(description="사업명", example="1")
    private String bizPbancTypeString;

    @ExcelColumn(headerName = "사업명")
    @Schema(description="사업명", example="1")
    private String bizPbancNm;

    @ExcelColumn(headerName = "지역")
    @Schema(description="사업 공고 신청 지역", required = true, example="홍길동")
    private String bizOrgAplyRgn;

    @ExcelColumn(headerName = "시군")
    private String organizationLocation;

    @Schema(description = "기관 유형")
    private String organizationType;

    @ExcelColumn(headerName = "학교급")
    private String organizationTypeName;

    @ExcelColumn(headerName = "신청기관")
    @Schema(description="기관이름", required = false, example="1")
    private String organizationName;

    @ExcelColumn(headerName = "수업형태")
    @Schema(description="수업 형태(운영 방법)", required = false, example="1")
    private String bizOrgAplyOperMeth;

    @ExcelColumn(headerName = "담당자 아이디")
    @Schema(description="사업 공고 신청 담당자 아이디", required = true, example="홍길동")
    private String registUserId;

    @Schema(description="사업 공고 신청 담당자 아이디", required = true, example="홍길동")
    private String bizOrgAplyPic;

    @ExcelColumn(headerName = "담당자 이름")
    @Schema(description="사업 공고 신청 담당자 이름", required = true, example="홍길동")
    private String bizOrgAplyPicNm;

    @ExcelColumn(headerName = "담당자 직위")
    @Schema(description="사업 공고 신청 담당자 직위", required = true, example="과장")
    private String bizOrgAplyPicJbgd;

    @ExcelColumn(headerName = "담당자 직통번호")
    @Schema(description="사업 공고 신청 담당자 번호", required = true, example="02-123-1234")
    private String bizOrgAplyPicTelno;

    @ExcelColumn(headerName = "담당자 연락처")
    @Schema(description="사업 공고 신청 담당자 핸드폰번호", required = true, example="010-1234-1234")
    private String bizOrgAplyPicMpno;

    @ExcelColumn(headerName = "담당자 이메일")
    @Schema(description="사업 공고 신청 담당자 이메일", required = true, example="hong@naver.com")
    private String bizOrgAplyPicEml;

    @Schema(description="사업공고 신청 접수 상태 - 0: 임시저장, 1: 신청, 2: 승인, 3: 반려, 7: 종료, 9: 가승인", required = true, example="0")
    private Integer bizOrgAplyStts;

    @ExcelColumn(headerName = "상태")
    @Schema(description="사업공고 신청 접수 상태 - 0: 임시저장, 1: 신청, 2: 승인, 3: 반려, 7: 종료, 9: 가승인", required = true, example="0")
    private String state;

    @ExcelColumn(headerName = "반려 사유")
    @Schema(description="상태 변경 사유", required = false, example="-")
    private String bizOrgAplySttsCmnt;

    @Schema(description="변경 가능 여부", required = true, example="-")
    private Integer bizOrgAplyChgYn;

    @ExcelColumn(headerName = "변경가능여부")
    @Schema(description="변경 가능 여부", required = true, example="-")
    private String change;

    @ExcelColumn(headerName = "교육기간 시작일")
    @Schema(description="수업 계획 - 교육기간 - 시작일", required = false, example="2022-12-01")
    private String bizOrgAplyLsnPlanBgng;

    @ExcelColumn(headerName = "교육기간 종료일")
    @Schema(description="수업 계획 - 교육기간 - 종료일", required = false, example="2022-12-20")
    private String bizOrgAplyLsnPlanEnd;

    @ExcelColumn(headerName = "총 교육 시간")
    @Schema(description="사업 공고 신청 총 시간", required = false, example="")
    private double bizOrgAplyTime;

    @ExcelColumn(headerName = "최초 총 교육 시간")
    @Schema(description="사업 공고 신청 최초 총 시간", required = false, example="")
    private double bizOrgAplyTimeFrst;

    @ExcelColumn(headerName = "회차당 교육 인원")
    @Schema(description="회차당 교육 인원", required = false, example="1")
    private Integer bizOrgAplyLsnPlanNope;

    @ExcelColumn(headerName = "교육 대상")
    @Schema(description="수업 대상", required = false, example="1")
    private String bizOrgAplyLsnPlanTrgt;

    @ExcelColumn(headerName = "우편번호")
    @Schema(description="소속 기관 우편번호", example="")
    private String organizationZipCode;

    @ExcelColumn(headerName = "주소")
    @Schema(description="소속 기관 주소1", example="")
    private String organizationAddress1;

    @ExcelColumn(headerName = "주소(상세)")
    @Schema(description="소속 기관 주소2", example="")
    private String organizationAddress2;

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

    @ExcelColumn(headerName = "확정강사")
    private String bizOrgAplyInstr;

    @ExcelColumn(headerName = "기타 의견")
    @Schema(description="기타 의견", required = false, example="1")
    private String bizOrgAplyLsnPlanEtc;

    @Schema(description="신문 신청 여부(없음/0: N, 1: Y)", required = false, example="1")
    private Integer bizOrgAplyPeprYn;

    @ExcelColumn(headerName = "신문신청")
    @Schema(description="신문 신청 여부(없음/0: N, 1: Y)", required = false, example="1")
    private String paper;

    @ExcelColumn(headerName = "등록일시")
    @Schema(description="등록 일시", example="")
    @Convert(converter= DateToStringConverter.class)
    private String createDateTime;

    @ExcelColumn(headerName = "수정일시")
    @Schema(description="수정일시", example="")
    @Convert(converter= DateToStringConverter.class)
    private String updateDateTime;

}