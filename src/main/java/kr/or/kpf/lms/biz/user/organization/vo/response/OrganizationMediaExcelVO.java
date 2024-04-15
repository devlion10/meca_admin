package kr.or.kpf.lms.biz.user.organization.vo.response;

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

@Schema(name="OrganizationExcelVO", description="기관 정보 엑셀")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationMediaExcelVO extends CSResponseVOSupport {

    @ExcelColumn(headerName = "매체사명")
    @Schema(description="매체사명", example="")
    private String organizationName;

    @ExcelColumn(headerName = "매체명")
    @Schema(description="매체명", example="")
    private String mediaName;

    @ExcelColumn(headerName = "매체 대분류")
    @Schema(description="매체 대분류", example="")
    private String mediaClsName1;

    @ExcelColumn(headerName = "매체 중분류")
    @Schema(description="매체 중분류", example="")
    private String mediaClsName2;

    @ExcelColumn(headerName = "지역")
    @Schema(description="지역", example="")
    private String mediaArea;

    @ExcelColumn(headerName = "사업자등록번호")
    @Schema(description="소속 기관 사업자 등록번호", example="")
    private String bizLicenseNumber;

    @ExcelColumn(headerName = "우편번호")
    @Schema(description="소속 기관 우편번호", example="")
    private String organizationZipCode;

    @ExcelColumn(headerName = "주소")
    @Schema(description="소속 기관 주소1", example="")
    private String organizationAddress1;

    @ExcelColumn(headerName = "상세주소")
    @Schema(description="소속 기관 주소2", example="")
    private String organizationAddress2;

    @ExcelColumn(headerName = "연락처")
    @Schema(description="소속 기관 연락처", example="")
    private String organizationTelNumber;

    @ExcelColumn(headerName = "팩스")
    @Schema(description="소속 기관 팩스 번호", example="")
    private String organizationFaxNumber;

    @ExcelColumn(headerName = "홈페이지")
    @Schema(description="홈페이지 주소", example="www.naver.com")
    private String organizationHomepage;

    @Schema(description="사용 여부", example="")
    private Boolean isUsable;

    @ExcelColumn(headerName = "상태")
    @Schema(description="사용 여부", example="")
    private String isUsableString;

}
