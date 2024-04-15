package kr.or.kpf.lms.biz.contents.section.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="SectionViewRequestVO", description="섹션(절) 관리 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SectionViewRequestVO extends CSViewVOSupport {

    @Schema(description="챕터 코드")
    private String chapterCode;

    @Schema(description="섹션(절) 코드")
    private String sectionCode;
}
