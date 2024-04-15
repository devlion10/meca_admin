package kr.or.kpf.lms.biz.statistics.privacy.vo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="PrivacyResponseVO", description="개인정보 수정 이력 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PrivacyResponseVO extends CSViewVOSupport {

    @ExcelColumn(headerName = "권한명")
    @Schema(description="권한명")
    private String roleGroup;

    @ExcelColumn(headerName = "취급자명")
    @Schema(description="취급자명")
    private String userName;

    @ExcelColumn(headerName = "취급자 아이디")
    @Schema(description="취급자 아이디")
    private String userId;

    @Schema(description="접근 메뉴 경로")
    private String uri;

    @ExcelColumn(headerName = "접속 ip")
    @Schema(description="접속 ip")
    private String remoteIp;

    @ExcelColumn(headerName = "접속 일시")
    @Schema(description="접속 일시")
    private String createDateTime;

    @ExcelColumn(headerName = "접근 메뉴 명")
    private String menuName;

    @ExcelColumn(headerName = "대상정보 이름")
    private String targetName;

    @ExcelColumn(headerName = "대상정보 아이디")
    private String targetId;

    @ExcelColumn(headerName = "대상정보 수행방식")
    @Schema(description="대상정보 수행방식")
    private String httpMethod;

    @Schema(description="대상정보 수행내역")
    private String queryParameter;
}
