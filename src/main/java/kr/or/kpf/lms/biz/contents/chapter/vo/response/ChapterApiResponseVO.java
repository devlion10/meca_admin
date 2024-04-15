package kr.or.kpf.lms.biz.contents.chapter.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 챕터 관리 관련 응답 객체
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChapterApiResponseVO extends CSResponseVOSupport {

    @Schema(description="챕터 코드", example="CHAP000001")
    private String chapterCode;
}
