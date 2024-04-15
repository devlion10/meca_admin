package kr.or.kpf.lms.biz.business.pbanc.master.template.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.business.pbanc.master.template.vo.CreateBizPbancTmpl0;
import kr.or.kpf.lms.biz.business.pbanc.master.template.vo.CreateBizPbancTmpl4;
import kr.or.kpf.lms.biz.business.pbanc.master.template.vo.UpdateBizPbancTmpl0;
import kr.or.kpf.lms.biz.business.pbanc.master.template.vo.UpdateBizPbancTmpl4;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Schema(name="BizPbancTmpl4ApiRequestVO", description="사업 공고 템플릿 4 API 관련 요청 객체")
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class BizPbancTmpl4ApiRequestVO {
    @Schema(description="사업 공고 기본형 템플릿 일련 번호", required = true, example="P4T0000001")
    private String bizPbancTmpl4No;

    @Schema(description="사업 공고 일련 번호", required = true, example="PAC0000001")
    private String bizPbancNo;

    @Schema(description="자유학기제 사용여부", required = true, example="0")
    private Integer bizPbancTmpl4FrdmSmstUseYn;

    @Schema(description="자유학기제 최대시간", required = true, example="10")
    private Integer bizPbancTmpl4FrdmSmstMaxHr;

}
