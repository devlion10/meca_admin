package kr.or.kpf.lms.biz.user.webuser.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.converter.DateToStringConverter;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;


@Schema(name="WebUserExcelVO", description="회원 정보 엑셀")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebUserExcelVO extends CSResponseVOSupport {

    @ExcelColumn(headerName = "아이디")
    @Schema(description="회원 아이디", example="")
    private String userId;

    @ExcelColumn(headerName = "이름")
    @Schema(description="웹 회원 명", example="")
    private String userName;

    @ExcelColumn(headerName = "연락처")
    @Schema(description="회원 연락처", example="")
    private String phone;

    @ExcelColumn(headerName = "이메일")
    @Schema(description="이메일", example="")
    private String email;

    @Schema(description="권한 그룹", example="")
    private String roleGroup;

    @ExcelColumn(headerName = "권한")
    @Schema(description="권한 그룹", example="")
    private String roleGroupString;

    @Schema(description="회원 상태", example="")
    private String state;

    @ExcelColumn(headerName = "상태")
    @Schema(description="회원 상태", example="")
    private String stateString;

    @ExcelColumn(headerName = "최근 로그인 일시")
    @Schema(description="최근 로그인 일시", example="")
    private String lastLoginDateTime;

    @ExcelColumn(headerName = "가입일시")
    @Schema(description="등록 일시", example="")
    @Convert(converter= DateToStringConverter.class)
    private String createDateTime;

    @ExcelColumn(headerName = "소속 기관/학교 지역")
    private String organizationRegion;

    @Schema(description = "소속 기관/학교 주소")
    private String organizationAddress1;

    @ExcelColumn(headerName = "소속 기관/학교")
    @Schema(description="소속 기관 명", example="")
    private String organizationName;

    @ExcelColumn(headerName = "소속부서")
    @Schema(description="소속부서", example="")
    private String department;

    @ExcelColumn(headerName = "직급")
    @Schema(description="직급", example="")
    private String rank;

    @ExcelColumn(headerName = "직책")
    @Schema(description="직책", example="")
    private String position;

    @ExcelColumn(headerName = "우편번호")
    @Schema(description="우편번호", example="")
    private String userZipCode;

    @ExcelColumn(headerName = "주소")
    @Schema(description="우편번호", example="")
    private String userAddress1;

    @ExcelColumn(headerName = "상세주소")
    @Schema(description="우편번호", example="")
    private String userAddress2;

    @ExcelColumn(headerName = "튜터여부")
    @Schema(description="튜터여부", example="")
    private String tutorYn;

    @ExcelColumn(headerName = "나이스번호")
    @Schema(description="나이스번호", example="")
    private String personalNiceNo;


}
