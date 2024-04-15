package kr.or.kpf.lms.biz.user.instructor.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(name="InstructorCareerResponseVO", description="강사 이력")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstructorCareerResponseVO extends CSResponseVOSupport {
    @Schema(description="강사 배정 일련번호", required = false, example="1")
    private String bizInstrAsgnmNo;

    @Schema(description="강사 모집 일련번호", required = true, example="ISR0000001")
    private String bizInstrNo;

    @Schema(description="사업 공고 신청 일련번호", required = true, example="ISR0000001")
    private String bizOrgAplyNo;

    @Schema(description="사업 공고 신청 수업 계획서 일련번호", required = false, example="홍길동")
    private String bizOrgAplyDtlNo;

    @Schema(description="강사 모집 공고 신청 정보 일련번호", required = true, example="abc")
    private String bizInstrAplyNo;


    @Schema(description="차시 정보 일련번호", required = true, example="abc")
    private String lectureCode;

    @Schema(description="교육 정보 일련번호", required = true, example="abc")
    private String educationPlanCode;


    @Schema(description="구분", required = false, example="")
    private Integer category;

    @ExcelColumn(headerName = "구분")
    @Schema(description="구분명", required = false, example="")
    private String categoryNm;

    @ExcelColumn(headerName = "사업/교육명")
    @Schema(description="사업/교육명", required = true, example="")
    private String title;

    @ExcelColumn(headerName = "기간 시작일")
    @Schema(description="기간 시작일", required = true, example="")
    private String periodBgng;

    @ExcelColumn(headerName = "기간 종료일")
    @Schema(description="기간 종료일", required = true, example="")
    private String periodEnd;

    @Schema(description="진행일시", required = true, example="")
    private List<String> date;

    @Schema(description="금액", required = true, example="")
    private Integer pay;

    @Schema(description="기관 코드", required = true, example="")
    private String orgCd;

}
