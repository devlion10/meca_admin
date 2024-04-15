package kr.or.kpf.lms.biz.contents.contents.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ContentsFolderInfo {
    /** 폴더 명 */
    @Schema(description="폴더 명", example="")
    private String folderName;

    /** 폴더 경로 */
    @Schema(description="폴더 경로", example="")
    private String folderPath;

    /** 하위 폴더 */
    private List<ContentsFolderInfo> subFolderList;

    /** 파일 */
    private List<ContentsFileInfo> contentsFileInfoList;
}
