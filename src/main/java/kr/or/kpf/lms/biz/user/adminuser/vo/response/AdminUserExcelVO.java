package kr.or.kpf.lms.biz.user.adminuser.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.converter.DateToStringConverter;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;


@Schema(name="WebUserExcelVO", description="회원 정보 엑셀")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserExcelVO extends CSResponseVOSupport {

    @ExcelColumn(headerName = "아이디")
    @Schema(description="로그인 아이디", required = true, example="")
    private String userId;

    @ExcelColumn(headerName = "사용자명")
    @Schema(description="사용자 명", required = true, example="")
    private String userName;

    @ExcelColumn(headerName = "권한")
    @Schema(description="권한 그룹", required = true, example="GENERAL")
    private String roleGroup;

    @ExcelColumn(headerName = "연락처")
    @Schema(description="연락처", example="")
    private String phone;

    @ExcelColumn(headerName = "이메일")
    @Schema(description="사용자 이메일 주소", example="")
    private String email;

    @ExcelColumn(headerName = "허용 IP")
    @Schema(description="허용 IP", example="")
    private String availableIp;

    @Schema(description="상태", example="")
    private String state;

    @ExcelColumn(headerName = "상태")
    @Schema(description="상태", example="")
    private String stateString;

    @Schema(description="잠금여부", example="")
    private Boolean isLock;

    @ExcelColumn(headerName = "잠금")
    @Schema(description="잠금여부", example="")
    private String isLockString;

    @ExcelColumn(headerName = "비밀번호 실패수")
    @Schema(description="비밀번호 실패수", example="")
    private Integer passwordFailureCount;

    @ExcelColumn(headerName = "부서")
    @Schema(description="소속 부서", example="")
    private String department;

    @ExcelColumn(headerName = "직급")
    @Schema(description="직급", example="")
    private String rank;

    @ExcelColumn(headerName = "직책")
    @Schema(description="직책", example="")
    private String position;

    @ExcelColumn(headerName = "등록일시")
    @Schema(description="등록 일시", example="")
    @Convert(converter= DateToStringConverter.class)
    private String createDateTime;


    @ExcelColumn(headerName = "수정일시")
    @Schema(description="수정일시", example="")
    @Convert(converter= DateToStringConverter.class)
    private String updateDateTime;


}
