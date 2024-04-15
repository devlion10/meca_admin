package kr.or.kpf.lms.biz.business.pbanc.master.template.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Schema(name="BizPbancTmpl5ApiResponseVO", description="사업공모 템플릿 5 API 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizPbancTmpl5ApiResponseVO {
    /** 사업 공고 일련 번호 */
    @Schema(description="사업 공고 일련 번호", example="P4T0000001")
    private String bizPbancNo;

    private BigInteger bizPbancTmpl5No;

    /** 사업신청서 항목 순서 */
    @Schema(description="사업신청서 항목 순서", example="1")
    private Integer bizPbancTmpl5Ordr;

    /** 사업신청서 항목 명 */
    @Schema(description="사업신청서 항목 명", example="희망시간")
    private String bizPbancTmpl5Name;

    /** 필수입력여부 */
    @Schema(description="필수입력여부", example="Y")
    private String bizPbancTmpl5Notnull;

    /** 입력 타입 1단답형 2서술형 3선택형 4날짜 5파일*/
    @Schema(description="입력 타입", example="1")
    private String bizPbancTmpl5Type;

    /** 선택형 선택지 */
    @Schema(description="선택형 선택지", example="서울,경기")
    private String bizPbancTmpl5TypeSlct;

    /** 입력안내문 */
    @Schema(description="입력안내문", example="주의사항 숫자만 입력해 주세요")
    private String bizPbancTmpl5Etc;
}
