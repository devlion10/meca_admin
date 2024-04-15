package kr.or.kpf.lms.biz.business.apply.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.converter.DateToStringConverter;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;

@Schema(name="BizAplyExcelVO", description="언론인 사업공고 신청 엑셀 데이터 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizAplyExcelVO extends CSResponseVOSupport {

    @ExcelColumn(headerName = "사업년도")
    @Schema(description="사업년도", example="1")
    private Integer bizPbancYr;

    @ExcelColumn(headerName = "차수")
    @Schema(description="사업 공고 차수", example="1")
    private Integer bizPbancRnd;

    @ExcelColumn(headerName = "사업명")
    @Schema(description="사업명", example="1")
    private String bizPbancNm;

    @ExcelColumn(headerName = "신청자 아이디")
    @Schema(description="사업 공고 신청 담당자 아이디", required = true, example="홍길동")
    private String userId;

    @ExcelColumn(headerName = "이름")
    @Schema(description="사업 공고 신청 담당자 이름", required = true, example="홍길동")
    private String userName;

    @ExcelColumn(headerName = "기관명")
    @Schema(description="사업 공고 신청 기관명", required = true, example="홍길동")
    private String organizationName;

    @ExcelColumn(headerName = "연락처")
    @Schema(description="사업 공고 신청 담당자 연락처", required = true, example="홍길동")
    private String phone;

    @ExcelColumn(headerName = "이메일")
    @Schema(description="사업 공고 신청 담당자 이메일", required = true, example="홍길동")
    private String email;

    @ExcelColumn(headerName = "신청일시")
    @Schema(description="등록 일시", example="")
    @Convert(converter= DateToStringConverter.class)
    private String createDateTime;


}
