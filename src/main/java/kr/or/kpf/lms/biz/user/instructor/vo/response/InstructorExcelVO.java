package kr.or.kpf.lms.biz.user.instructor.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.converter.DateYMDToStringConverter;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;

@Schema(name="InstructorExcelVO", description="회원 정보 엑셀")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstructorExcelVO extends CSResponseVOSupport {
    @ExcelColumn(headerName = "아이디")
    @Schema(description="웹 회원 아이디", required = false, example="")
    private String userId;

    @ExcelColumn(headerName = "유형")
    @Schema(description="강사 구분", required = true, example="")
    private String instrCtgr;

    @ExcelColumn(headerName = "이름")
    @Schema(description="강사 명", required = true, example="")
    private String instrNm;

    @ExcelColumn(headerName = "생년월일")
    @Schema(description="강사 생년월일", required = true, example="")
    @Convert(converter= DateYMDToStringConverter.class)
    private String instrBrdt;

    @ExcelColumn(headerName = "연락처")
    @Schema(description="강사 연락처(핸드폰 번호)", required = true, example="")
    private String instrTel;

    @ExcelColumn(headerName = "이메일")
    @Schema(description="강사 이메일", required = true, example="")
    private String instrEml;

    @Schema(description="강사 상태 (0: 미사용, 1: 사용)", required = true, example="")
    private Integer instrStts;

    @ExcelColumn(headerName = "상태")
    @Schema(description="강사 상태 (0: 미사용, 1: 사용)", required = true, example="")
    private String instrSttsString;

    @ExcelColumn(headerName = "강의가능 지역1")
    @Schema(description="강사 강의가능 지역1 (0: 선택 없음)", required = true, example="")
    private String instrLctRgn1;

    @ExcelColumn(headerName = "강의가능 지역2")
    @Schema(description="강사 강의가능 지역2 (0: 선택 없음)", required = true, example="")
    private String instrLctRgn2;

    @ExcelColumn(headerName = "우편번호")
    @Schema(description="우편번호", required = true, example="")
    private String instrZipCd;

    @ExcelColumn(headerName = "주소")
    @Schema(description="주소", required = true, example="")
    private String instrAddr1;

    @ExcelColumn(headerName = "상세주소")
    @Schema(description="상세주소", required = true, example="")
    private String instrAddr2;

    @ExcelColumn(headerName = "졸업 학교명")
    @Schema(description="강사 최종학력 학교명", required = true, example="")
    private String instrAcbgSchlNm;

    @ExcelColumn(headerName = "전공")
    @Schema(description="강사 최종학력 전공", required = true, example="")
    private String instrAcbgMjr;

    @ExcelColumn(headerName = "최종학력")
    @Schema(description="강사 최종학력 학위", required = true, example="")
    private String instrAcbgDgr;

    @ExcelColumn(headerName = "소속명")
    @Schema(description="강사 소속명", required = false, example="")
    private String orgName;

    @ExcelColumn(headerName = "부서/직급")
    @Schema(description="강사 부서/직급", required = false, example="")
    private String department;

    @ExcelColumn(headerName = "은행")
    @Schema(description="강사 은행", required = true, example="")
    private String instrBank;

    @ExcelColumn(headerName = "계좌")
    @Schema(description="강사 계좌번호", required = true, example="")
    private String instrActno;

    @ExcelColumn(headerName = "튜터여부")
    @Schema(description="튜터여부", example="")
    private String tutorYn;

    @ExcelColumn(headerName = "나이스번호")
    private String personalNiceNo;

}
