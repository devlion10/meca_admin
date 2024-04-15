package kr.or.kpf.lms.biz.homepage.topqna.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigInteger;

/**
 * 자주묻는 질문 관련 요청 객체
 */
@Schema(name="TopQnaViewRequestVO", description="자주묻는 질문 View 관련 요청 객체")
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class TopQnaViewRequestVO extends CSViewVOSupport {
    /** 검색어 범위 */
    @Schema(description="검색어 범위")
    private String containTextType;

    /** 검색에 포함할 단어 */
    @Schema(description="검색에 포함할 단어")
    private String containText;

    @Schema(description="시퀀스 번호")
    private BigInteger sequenceNo;
}
