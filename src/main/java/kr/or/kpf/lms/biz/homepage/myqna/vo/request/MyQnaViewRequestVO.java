package kr.or.kpf.lms.biz.homepage.myqna.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.*;

import java.math.BigInteger;

/**
 * 1:1 문의 View 관련 요청 객체
 */
@Schema(name="MyQnaViewRequestVO", description="1:1 문의 View 관련 요청 객체")
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class MyQnaViewRequestVO extends CSViewVOSupport {

    /** 일련 번호 */
    @Schema(description="일련 번호")
    private BigInteger sequenceNo;

    /** 회원 ID */
    @Schema(description="회원 ID")
    private String userId;

    /** 회원 명 */
    @Schema(description="회원 명")
    private String userName;

    /** 검색어 범위 */
    @Schema(description="검색어 범위")
    private String containTextType;

    /** 검색에 포함할 단어 */
    @Schema(description="검색에 포함할 단어")
    private String containText;
}
