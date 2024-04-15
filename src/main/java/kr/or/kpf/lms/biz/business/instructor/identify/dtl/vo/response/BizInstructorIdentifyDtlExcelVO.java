package kr.or.kpf.lms.biz.business.instructor.identify.dtl.vo.response;

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
public class BizInstructorIdentifyDtlExcelVO {

    @ExcelColumn(headerName = "강사명")
    @Schema(description="강의확인서 작성 강사명", required = true, example="홍길동")
    private String userName;

    @ExcelColumn(headerName = "강사ID")
    @Schema(description="강의확인서 작성 강사 id", required = true, example="홍길동")
    private String registUserId;

    @ExcelColumn(headerName = "제출처")
    @Schema(description="강사 출장 교육 지역", required = true, example="홍길동")
    private String bizOrgAplyRgn;

    @ExcelColumn(headerName = "사업명")
    @Schema(description="사업명", example="1")
    private String bizPbancNm;

    @ExcelColumn(headerName = "수행기관")
    @Schema(description="기관이름", required = false, example="1")
    private String organizationName;

    @ExcelColumn(headerName = "교육시작일")
    @Schema(description="수업 계획 - 교육기간 - 시작일", required = false, example="2022-12-01")
    private String bizOrgAplyLsnPlanBgng;

    @ExcelColumn(headerName = "교육종료일")
    @Schema(description="수업 계획 - 교육기간 - 종료일", required = false, example="2022-12-20")
    private String bizOrgAplyLsnPlanEnd;

    @ExcelColumn(headerName = "교육연월")
    @Schema(description="연월")
    private String bizInstrIdntyYm;

    @Schema(description="강의확인서 상태 (0: 임시저장, 1: 제출(접수), 2: 기관 승인, 3: 관리자 승인(지출 접수), 4: 지출 완료, 9: 반려)", example="")
    private Integer bizInstrIdntyStts;

    @ExcelColumn(headerName = "강의확인서 상태")
    private String state;

    @ExcelColumn(headerName = "수업계획서 주제")
    @Schema(description="수업계획서 주제", example="")
    private String bizOrgAplyLsnDtlTpic;

    @ExcelColumn(headerName = "강의확인서 주차별 주제")
    @Schema(description="강의확인서 주차별 주제", example="")
    private String bizInstrIdntyDtlTpic;

    @ExcelColumn(headerName = "강의확인서 주차별 인원")
    @Schema(description="강의확인서 주차별 인원", example="")
    private Integer bizInstrIdntyDtlNope;

}
