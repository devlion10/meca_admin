package kr.or.kpf.lms.biz.homepage.review.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.Column;
import java.math.BigInteger;

/**
 * 교육 후기 관련 요청 객체
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class ReviewApiRequestVO {

    /** 시퀀스 번호 */
    @Schema(description="시퀀스 번호")
    private BigInteger sequenceNo;

    /** 구분(팀) */
    @Schema(description="구분(팀)")
    private String category;

    /** 유형 */
    @Schema(description="유형")
    private String type;

    /** 제목 */
    @Schema(description="제목")
    private String title;

    /** 내용 */
    @Schema(description="내용")
    private String contents;
}
