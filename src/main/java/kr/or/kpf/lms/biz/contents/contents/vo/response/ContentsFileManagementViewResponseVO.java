package kr.or.kpf.lms.biz.contents.contents.vo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.contents.contents.vo.ContentsFolderInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(name="ContentsFileManagementViewResponseVO", description="콘텐츠 폴더 관리 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ContentsFileManagementViewResponseVO {

    /** 콘텐츠 코드 */
    @Schema(description="콘텐츠 코드")
    private String contentsCode;

    /** 콘텐츠 명 */
    @Schema(description="콘텐츠 명")
    private String contentsName;

    /** 콘텐츠 카테고리 코드 */
    @Schema(description="콘텐츠 카테고리 코드")
    private String categoryCode;

    /** 하위 폴더 */
    private List<ContentsFolderInfo> subFolderList;
}
