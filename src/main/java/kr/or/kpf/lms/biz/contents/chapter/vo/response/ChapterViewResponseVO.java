package kr.or.kpf.lms.biz.contents.chapter.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 챕터 관리 View 관련 응답 객체
 */
@Schema(name="ChapterViewResponseVO", description="챕터 관리 View 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChapterViewResponseVO  extends CSResponseVOSupport {
    /** 챕터 코드 */
    @Schema(description="챕터 코드")
    private String chapterCode;

    /** 챕터 명 */
    @Schema(description="챕터 명")
    private String chapterName;

    /** 사용 여부 */
    @Schema(description="사용 여부")
    private Boolean isUsable;

    /** 비고 내용 */
    @Schema(description="비고 내용")
    private String memo;
}
