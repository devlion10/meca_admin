package kr.or.kpf.lms.biz.business.pbanc.rslt.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.business.pbanc.rslt.vo.CreateBizPbancRslt;
import kr.or.kpf.lms.biz.business.pbanc.rslt.vo.UpdateBizPbancRslt;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(name="BizPbancRsltApiRequestVO", description="사업 공고 결과 API 관련 요청 객체")
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class BizPbancRsltApiRequestVO {

    @Schema(description="사업 공고 결과공고 일련 번호", required = true, example="PACR000001")
    @NotEmpty(groups={CreateBizPbancRslt.class, UpdateBizPbancRslt.class}, message="사업 공고 결과 일련 번호는 필수 입니다.")
    private String bizPbancRsltNo;

    @Schema(description="사업 공고 일련변호", required = true, example="PAC0000003")
    @NotEmpty(groups={CreateBizPbancRslt.class, UpdateBizPbancRslt.class}, message="사업 공고 일련 번호는 필수 입니다.")
    private String bizPbancNo;

    @Schema(description="선정 결과 내용", required = true, example="선정 결과 내용")
    @NotEmpty(groups={CreateBizPbancRslt.class, CreateBizPbancRslt.class}, message="선정 결과 내용은 필수 입니다.")
    private String bizPbancRsltCn;

    @Schema(description="공지 여부", required = false, example="평생교실")
    private Integer bizPbancNtcYn;

    @Schema(description="결과공고 첨부파일", required = false, example="")
    private String bizPbancRsltFile;

}
