package kr.or.kpf.lms.biz.education.reference.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.education.reference.controller.ReferenceRoomApiController;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.repository.entity.user.AdminUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Schema(name="ReferenceRoomApiResponseVO", description="자료실 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReferenceRoomExcelVO extends CSResponseVOSupport {
    @ExcelColumn(headerName = "제목")
    @Schema(description="제목", required = true, example="")
    private String title;

    @ExcelColumn(headerName = "내용")
    @Schema(description="내용", required = true, example="")
    private String contents;

    /** 파일 경로 */
    @Column(name="FILE_PATH")
    private String filePath;

    @ExcelColumn(headerName = "파일명")
    private String fileName;

    @ExcelColumn(headerName = "파일 크기")
    @Schema(description="파일 크기", required = true, example="")
    private Long fileSize;

    @ExcelColumn(headerName = "등록자명")
    @Schema(description="등록자명", required = true, example="")
    private String userName;
}
