package kr.or.kpf.lms.biz.homepage.archive.data.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.contents.contents.vo.ContentsFileInfo;
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
public class PublicationFolderInfo {
    /** 폴더 명 */
    @Schema(description="폴더 명", example="")
    private String folderName;

    /** 폴더 경로 */
    @Schema(description="폴더 경로", example="")
    private String folderPath;

    /** 하위 폴더 */
    private List<PublicationFolderInfo> publicationFolderInfos;

    /** 파일 */
    private List<PublicationFileInfo> publicationFileInfos;
}
