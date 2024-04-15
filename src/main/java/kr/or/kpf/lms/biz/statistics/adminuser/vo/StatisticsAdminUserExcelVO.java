package kr.or.kpf.lms.biz.statistics.adminuser.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="StatisticsAdminUserExcelVO", description="관리자 접속 이력 엑셀 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsAdminUserExcelVO {

    @ExcelColumn(headerName = "관리자 아이디")
    private String userId;

    @ExcelColumn(headerName = "관리자 명")
    private String userName;

    @ExcelColumn(headerName = "접근 메뉴 URI")
    private String uri;

    @ExcelColumn(headerName = "접근 메뉴 명")
    private String menuFullName;

    @ExcelColumn(headerName = "접속 IP")
    private String remoteIp;

}
