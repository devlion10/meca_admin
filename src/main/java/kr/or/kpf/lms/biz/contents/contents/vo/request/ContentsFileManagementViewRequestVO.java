package kr.or.kpf.lms.biz.contents.contents.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="ContentsFileManagementViewRequestVO", description="콘텐츠 폴더 관리 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ContentsFileManagementViewRequestVO extends CSViewVOSupport {

    @Schema(description="콘텐츠 코드", example="CTS000001")
    private String contentsCode;

    @Schema(description="폴더 경로", example="/chapter1/first")
    private String folderPath;

}
