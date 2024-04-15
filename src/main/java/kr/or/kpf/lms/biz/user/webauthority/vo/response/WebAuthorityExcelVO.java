package kr.or.kpf.lms.biz.user.webauthority.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.converter.DateToStringConverter;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;

@Schema(name="OrganizationExcelVO", description="기관 정보 엑셀")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebAuthorityExcelVO extends CSResponseVOSupport {
    @ExcelColumn(headerName = "아이디")
    @Schema(description="회원 아이디", example="")
    private String userId;

    @ExcelColumn(headerName = "유형")
    @Schema(description="회원 유형", example="")
    private String roleGroup;

    @ExcelColumn(headerName = "이름")
    @Schema(description="회원 명", example="")
    private String userName;

    @ExcelColumn(headerName = "전화번호")
    @Schema(description="회원 전화번호", example="")
    private String phone;

    @ExcelColumn(headerName = "이메일")
    @Schema(description="회원 이메일", example="")
    private String email;

    @Schema(description="사업 참여 권한 타입", example="")
    private String businessAuthority;

    @ExcelColumn(headerName = "구분")
    @Schema(description="사업 참여 권한 타입", example="")
    private String type;

    @Schema(description="사업 참여 권한 신청 상태", example="")
    private String businessAuthorityApprovalState;

    @ExcelColumn(headerName = "상태")
    @Schema(description="사업 참여 권한 신청 상태", example="")
    private String state;

    @ExcelColumn(headerName = "소속기관/학교")
    @Schema(description="기관이름", example="")
    private String organizationName;

    @ExcelColumn(headerName = "신청일시")
    @Schema(description="등록 일시", example="")
    @Convert(converter= DateToStringConverter.class)
    private String createDateTime;

    @ExcelColumn(headerName = "수정일시")
    @Schema(description="수정일시", example="")
    @Convert(converter= DateToStringConverter.class)
    private String updateDateTime;
}
