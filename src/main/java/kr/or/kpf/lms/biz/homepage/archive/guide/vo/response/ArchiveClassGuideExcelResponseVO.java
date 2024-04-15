package kr.or.kpf.lms.biz.homepage.archive.guide.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import lombok.*;

import java.math.BigInteger;

@Schema(name="ArchiveClassGuideExcelResponseVO", description="자료실 View 관련 응답 객체")
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveClassGuideExcelResponseVO {

    /** 수업 지도안 코드 */
    @Schema(description="수업 지도안 코드")
    private String classGuideCode;

    /** 수업 지도안 파일 코드 */
    @Schema(description="수업 지도안 파일 코드")
    private BigInteger sequenceNo;

    /** 수업 지도안 유형 */
    @ExcelColumn(headerName = "카테고리")
    @Schema(description="수업 지도안 유형")
    private String classGuideType;

    /** 수업 지도안 대상 */
    @ExcelColumn(headerName = "소분류")
    @Schema(description="수업 지도안 대상")
    private String target;

    /** 수업 지도안 제목 */
    @ExcelColumn(headerName = "수업지도안 주제")
    @Schema(description="수업 지도안 제목")
    private String title;

    /** 수업 지도안 e-NIE 여부 */
    @ExcelColumn(headerName = "E-nie 여부")
    @Schema(description="수업 지도안 e-NIE 여부")
    private String eNIEYn;

    /** 등록자 ID */
    @Schema(description="등록자 ID")
    private String registUserId;

    /** 작성자 */
    @ExcelColumn(headerName = "작성자")
    private String userName;

    /** 등록일시 */
    @ExcelColumn(headerName = "등록일시")
    @Schema(description="등록일시")
    private String createDateTime;

    /** 수업 지도안 파일명 */
    @ExcelColumn(headerName = "파일명")
    @Schema(description="수업 지도안 파일명")
    private String originalFileName;

    /** 수업 지도안 조회수 */
    @ExcelColumn(headerName = "조회수")
    @Schema(description="수업 지도안 조회수")
    private BigInteger viewCount;

    /** 수업 지도안 파일 다운로드수 */
    @ExcelColumn(headerName = "다운로드수")
    @Schema(description="수업 지도안 파일 다운로드수")
    private BigInteger downloadCount;
}
