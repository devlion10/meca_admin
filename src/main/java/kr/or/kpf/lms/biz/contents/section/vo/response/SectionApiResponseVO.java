package kr.or.kpf.lms.biz.contents.section.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 섹션(절) 관리 관련 응답 객체
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionApiResponseVO extends CSResponseVOSupport {

    @Schema(description="섹션(절) 코드")
    private String sectionCode;
}
