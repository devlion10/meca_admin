package kr.or.kpf.lms.biz.business.pbanc.master.vo.response;

import io.swagger.models.auth.In;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.converter.DateToStringConverter;
import kr.or.kpf.lms.common.converter.DateYMDToStringConverter;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.repository.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import java.util.List;

/**
 * 사업 공고 관련 응답 객체
 */
@Schema(name="BizPbancApiResponseVO", description="사업공모 API 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizPbancCustomApiResponseVO extends CSResponseVOSupport {

    @Schema(description="사업 공고 일련 번호", example="1")
    private String bizPbancNo;

    @Schema(description="사업 공고 템플릿 유형", example="1")
    private Integer bizPbancType;

    @Schema(description="사업 구분", example="1")
    private Integer bizPbancCtgr;

    @Schema(description="기본형 세부 구분(1사회미디어, 2학교미디어)", example="1")
    private Integer bizPbancCtgrSub;

    @ExcelColumn(headerName = "사업명")
    @Schema(description="사업명", example="1")
    private String bizPbancNm;

    @Schema(description="최대 시간", example="1")
    private Integer bizPbancMaxTm;

    @ExcelColumn(headerName = "사업년도")
    @Schema(description="사업년도", example="1")
    private Integer bizPbancYr;

    @ExcelColumn(headerName = "사업 공고 차수")
    @Schema(description="사업 공고 차수", example="1")
    private Integer bizPbancRnd;

    @Schema(description="최대 사업참여 기관", example="1")
    private Integer bizPbancMaxInst;

    @Schema(description="사업 선정 방식", example="1")
    private Integer bizPbancSlctnMeth;

    @Schema(description="강사 배정 방식", example="1")
    private Integer bizPbancInstrSlctnMeth;

    @ExcelColumn(headerName = "사업 모집 상태(0: 모집중, 1: 모집마감)")
    @Schema(description="사업 상태", example="1")
    private Integer bizPbancStts;

    @ExcelColumn(headerName = "사업 접수기간 - 시작일")
    @Schema(description="사업 접수기간 - 시작일", example="1")
    @Convert(converter= DateYMDToStringConverter.class)
    private String bizPbancRcptBgng;

    @ExcelColumn(headerName = "사업 접수기간 - 종료일")
    @Schema(description="사업 접수기간 - 종료일", example="1")
    @Convert(converter= DateYMDToStringConverter.class)
    private String bizPbancRcptEnd;

    @Schema(description="사업 지원기간 - 시작일", example="1")
    @Convert(converter= DateYMDToStringConverter.class)
    private String bizPbancSprtBgng;

    @Schema(description="사업 지원기간 - 종료일", example="1")
    @Convert(converter= DateYMDToStringConverter.class)
    private String bizPbancSprtEnd;

    @Convert(converter= DateYMDToStringConverter.class)
    @Schema(description="사업 공고 결과발표일(없을 시 미정)", example="1")
    private String bizPbancRsltYmd;

    @Schema(description="사업 내용", example="1")
    private String bizPbancCn;

    @Schema(description="사업공고 신문 신청 가능 여부(없음/0: N, 1: Y)", required = false, example="사업공고 신문 신청 가능 여부 입니다.")
    private Integer bizPbancPeprYn;

    @Schema(description="사업 공고 담당자 연락처")
    private String bizPbancPicTel;

    @ExcelColumn(headerName = "선정결과공고(값 존재 여부)")
    @Schema(description="사업 공고 결과공고", example="1")
    private String bizPbancRsltNo;

    @Schema(description="사업 공고 첨부파일 리스트", example="1")
    private List<FileMaster> fileMasters;

    @Schema(description="사용자 명", example="홍길동")
    private String userName;

    @Schema(description="신규 공고 여부")
    private Boolean isNew = false;

    @Schema(description="사업 공고 신청 공고 리스트(공고 상태 임시)", example="1")
    private List<BizOrganizationAply> bizOrganizationAplies;

    @Schema(description="사업 공고 신청 공고 리스트(공고 상태 승인)", example="1")
    private List<BizOrganizationAply> bizOrganizationAprvs;

    @Schema(description="사업 공고 신청 공고 리스트(공고 상태 임시)", example="1")
    private Long bizOrganizationAplyCount;

    @Schema(description="사업 공고 신청 공고 리스트(공고 상태 승인)", example="1")
    private Long bizOrganizationAprvCount;

    @Schema(description="상위 노출 여부")
    private String isTop;

    @Convert(converter= DateToStringConverter.class)
    private String createDateTime;

    private String registUserId;

    @Convert(converter=DateToStringConverter.class)
    private String updateDateTime;

    private String modifyUserId;

    private BizPbancTmpl0 bizPbancTmpl0;
    private BizPbancTmpl1 bizPbancTmpl1;
    private BizPbancTmpl2 bizPbancTmpl2;
    private BizPbancTmpl3 bizPbancTmpl3;
    private BizPbancTmpl4 bizPbancTmpl4;
    private List<BizPbancTmpl5> bizPbancTmpl5;

    @Schema(description="사업 공고 구분명", example="")
    private String bizPbancCtgrNm;
}