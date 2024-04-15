package kr.or.kpf.lms.biz.homepage.archive.data.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.*;

import java.math.BigInteger;

@Schema(name="ArchiveViewRequestVO", description="자료실 View 관련 요청 객체")
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArchiveViewRequestVO extends CSViewVOSupport {

    /** 검색어 범위 */
    @Schema(description="검색어 범위")
    private String containTextType;

    /** 검색에 포함할 단어 */
    @Schema(description="검색에 포함할 단어")
    private String containText;

    /** 시퀀스 번호 */
    @Schema(description="시퀀스 번호")
    private Long sequenceNo;

    /** 팀 구분 */
    @Schema(description="팀 구분", required=true, example="")
    private String teamCategory;

    /** 자료 구분 */
    @Schema(description="자료 구분", required=true, example="")
    private String materialCategory;

    /** 자료 유형(검색용) */
    @Schema(description="자료 유형(검색용)")
    private String types;

    /** 자료 유형 */
    @Schema(description="자료 유형")
    private String materialType;

}
