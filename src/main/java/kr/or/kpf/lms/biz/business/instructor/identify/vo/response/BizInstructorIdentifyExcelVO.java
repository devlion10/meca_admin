package kr.or.kpf.lms.biz.business.instructor.identify.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.converter.DateToStringConverter;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;

@Schema(name="BizInstructorIdentifyExcelVO", description="강의확인서 엑셀 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizInstructorIdentifyExcelVO {

    @ExcelColumn(headerName = "강사명")
    @Schema(description="강사 모집 공고 신청 강사명", required = true, example="홍길동")
    private String userName;

    @ExcelColumn(headerName = "강사명")
    @Schema(description="강사 정보 강사명(동명이인구분용)", required = true, example="홍길동")
    private String instrNm;

    @ExcelColumn(headerName = "강사ID")
    @Schema(description="강사 모집 공고 신청 강사명", required = true, example="홍길동")
    private String registUserId;

    @ExcelColumn(headerName = "제출처")
    @Schema(description="강사 모집 공고 신청 강사명", required = true, example="홍길동")
    private String bizOrgAplyRgn;

    @ExcelColumn(headerName = "사업명")
    @Schema(description="사업명", example="1")
    private String bizPbancNm;

    @ExcelColumn(headerName = "수행기관")
    @Schema(description="기관이름", required = false, example="1")
    private String organizationName;

    @ExcelColumn(headerName = "담당자 이름")
    @Schema(description="사업 공고 신청 담당자 이름", required = true, example="홍길동")
    private String bizOrgAplyPicNm;

    @ExcelColumn(headerName = "담당자연락처")
    @Schema(description="사업 공고 신청 담당자 핸드폰번호", required = true, example="010-1234-1234")
    private String bizOrgAplyPicMpno;

    @ExcelColumn(headerName = "교육시작일")
    @Schema(description="수업 계획 - 교육기간 - 시작일", required = false, example="2022-12-01")
    private String bizOrgAplyLsnPlanBgng;

    @ExcelColumn(headerName = "교육종료일")
    @Schema(description="수업 계획 - 교육기간 - 종료일", required = false, example="2022-12-20")
    private String bizOrgAplyLsnPlanEnd;

    @ExcelColumn(headerName = "강의시간")
    @Schema(description="강의시간")
    private String bizInstrIdntyTime;

    @ExcelColumn(headerName = "강의료")
    @Schema(description="강의료")
    private String bizInstrIdntyAmt;

    @ExcelColumn(headerName = "교육연월")
    @Schema(description="연월")
    private String bizInstrIdntyYm;

    @ExcelColumn(headerName = "지출연월")
    @Schema(description="지출연월")
    private String bizInstrIdntyPayYm;

    private Integer bizInstrIdntyStts;

    @ExcelColumn(headerName = "승인")
    @Schema(description="강의확인서 상태 (0: 임시저장, 1: 제출(접수), 2: 기관 승인, 3: 관리자 승인(지출 접수), 4: 지출 완료, 9: 반려)", example="")
    private String state;

    @ExcelColumn(headerName = "언택트 강의")
    @Schema(description="언택트강의", example="")
    private String bizOnline;

    private Integer bizOnlineCount;

    @ExcelColumn(headerName = "작성일시")
    @Schema(description="등록 일시", example="")
    @Convert(converter= DateToStringConverter.class)
    private String createDateTime;

    @ExcelColumn(headerName = "승인 일시")
    @Schema(description="승인 일시", example="")
    @Convert(converter= DateToStringConverter.class)
    private String bizInstrIdntyAprvDt;

    private String bizInstrIdntyNo;

}
