package kr.or.kpf.lms.biz.contents.contents.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.converter.DateToStringConverter;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;

@Schema(name="ContentsExcelVO", description="콘텐츠 엑셀 데이터 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentsExcelVO extends CSResponseVOSupport {

    @ExcelColumn(headerName = "콘텐츠명")
    @Schema(description="콘텐츠 명", example="공개강좌 콘텐츠명")
    private String contentsName;

    @ExcelColumn(headerName = "구분(1: 직무, 2: 자율, 3:공개)")
    @Schema(description="콘텐츠 카테고리 코드 (1: 공개 강좌, 2: 직무 강좌)", example="1", allowableValues = {"1", "2"})
    private String categoryCode;

    @ExcelColumn(headerName = "상태(true: 사용, false: 사용안함)")
    @Schema(description="사용 여부", defaultValue = "false")
    private Boolean isUsable;

    @ExcelColumn(headerName = "등록기관")
    @Schema(description="콘텐츠 제공 업체 코드", example="")
    private String organizationName;

    @ExcelColumn(headerName = "시간(분단위)")
    @Schema(description="교육 시간 (분단위)", example="90")
    private Integer educationPerMinute;

    @ExcelColumn(headerName = "등록일시")
    @Schema(description="등록 일시", example="")
    @Convert(converter= DateToStringConverter.class)
    private String createDateTime;

    @ExcelColumn(headerName = "비고")
    @Schema(description="비고 내용", example="메모입니다.")
    private String memo;

}
