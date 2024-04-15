package kr.or.kpf.lms.biz.homepage.review.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.*;

import java.math.BigInteger;

/**
 * 교육 후기 관련 요청 객체
 */
@Schema(name="ReviewViewRequestVO", description="교육 후기 View 관련 요청 객체")
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class ReviewViewRequestVO extends CSViewVOSupport {

    /** 검색어 범위 */
    @Schema(description="검색어 범위")
    private String containTextType;

    /** 검색에 포함할 단어 */
    @Schema(description="검색에 포함할 단어")
    private String containText;

    /** 시퀀스 번호 */
    @Schema(description="시퀀스 번호")
    private BigInteger sequenceNo;

    /** 구분 */
    @Schema(description="구분")
    private String category;

    /** 구분 */
    @Schema(description="구분")
    private String type;

    /** 구분 목록 */
    @Schema(description="구분 목록")
    private String types;

    /** 작성자 ID */
    @Schema(description="작성자 ID")
    private String userId;

    /** 작성자 명 */
    @Schema(description="작성자 명")
    private String userName;
}
