package kr.or.kpf.lms.biz.user.organization.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.user.organization.controller.OrganizationApiController;
import kr.or.kpf.lms.common.converter.DateToStringConverter;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(name="OrganizationExcelVO", description="기관 정보 엑셀")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationExcelVO extends CSResponseVOSupport {

    @ExcelColumn(headerName = "기관명")
    @Schema(description="소속 기관 명", example="")
    private String organizationName;

    @Schema(description="기관 타입 (1: 매체사, 2: 기관, 3: 학교)", example="1")
    private String organizationType;

    @ExcelColumn(headerName = "구분")
    @Schema(description="기관 타입 (1: 매체사, 2: 기관, 3: 학교)", example="1")
    private String organizationTypeString;

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

    @ExcelColumn(headerName = "등록일시")
    @Schema(description="등록 일시", example="")
    @Convert(converter= DateToStringConverter.class)
    private String createDateTime;

    @ExcelColumn(headerName = "수정일시")
    @Schema(description="수정일시", example="")
    @Convert(converter= DateToStringConverter.class)
    private String updateDateTime;

}
