package kr.or.kpf.lms.biz.business.apply.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.converter.DateToStringConverter;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.repository.entity.BizAplyDtl;
import kr.or.kpf.lms.repository.entity.BizPbancMaster;
import kr.or.kpf.lms.repository.entity.user.LmsUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Transient;
import java.math.BigInteger;
import java.util.List;

@Schema(name="BizAplyExcelVO", description="언론인 사업공고 신청 엑셀 데이터 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizAplyFreeExcelVO extends CSResponseVOSupport {

    @ExcelColumn(headerName = "사업년도")
    @Schema(description="사업년도", example="1")
    private Integer bizPbancYr;

    @ExcelColumn(headerName = "구분")
    @Schema(description="사업 공고 구분", example="1")
    private String ctgr;

    @Schema(description="사업 공고 구분", example="1")
    private Integer bizPbancCtgr;

    @ExcelColumn(headerName = "차수")
    @Schema(description="사업 공고 차수", example="1")
    private Integer bizPbancRnd;

    @ExcelColumn(headerName = "사업명")
    @Schema(description="사업명", example="1")
    private String bizPbancNm;

    @Schema(description="사업코드", example="1")
    private String bizPbancNo;


    @Schema(description="신청서 일련번호", required = true, example="홍길동")
    private BigInteger sequenceNo;

    @ExcelColumn(headerName = "신청자 아이디")
    @Schema(description="사업 공고 신청 담당자 아이디", required = true, example="홍길동")
    private String bizAplyUserID;

    @ExcelColumn(headerName = "이름")
    @Schema(description="사업 공고 신청 담당자 이름", required = true, example="홍길동")
    private String bizAplyUserNm;

    @ExcelColumn(headerName = "기관명")
    @Schema(description="사업 공고 신청 기관명", required = true, example="홍길동")
    private String organizationName;

    private String orgName;

    /** 사업 공고 신청 담당자 부서 */
    @ExcelColumn(headerName = "담당자 부서")
    @Schema(description="사업 공고 구분", example="1")
    private String bizAplyUserDptm;
    /** 사업 공고 신청 담당자 직위 */
    @ExcelColumn(headerName = "담당자 직위")
    @Schema(description="사업 공고 구분", example="1")
    private String bizAplyUserJbgd;


    @ExcelColumn(headerName = "연락처")
    @Schema(description="사업 공고 신청 담당자 연락처", required = true, example="홍길동")
    private String bizAplyUserTelno;

    @ExcelColumn(headerName = "이메일")
    @Schema(description="사업 공고 신청 담당자 이메일", required = true, example="홍길동")
    private String bizAplyUserEml;

    @ExcelColumn(headerName = "상태")
    @Schema(description="사업 공고 신청 담당자 이메일", required = true, example="홍길동")
    private String stts;

    private Integer bizAplyStts;

    @ExcelColumn(headerName = "등록 일시")
    @Schema(description="등록 일시", example="")
    @Convert(converter= DateToStringConverter.class)
    private String createDateTime;

    @ExcelColumn(headerName = "수정 일시")
    @Schema(description="수정 일시", example="")
    @Convert(converter= DateToStringConverter.class)
    private String updateDateTime;

    @Transient
    private List<BizAplyDtl> bizAplyDtls;

    @Transient
    private BizPbancMaster bizPbancMaster;

    @Transient
    private LmsUser lmsUser;

}
