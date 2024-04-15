package kr.or.kpf.lms.biz.homepage.archive.data.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(name="PublicationFileManagementApiRequestVO", description="출간물 폴더/파일 관리 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PublicationFileManagementApiRequestVO {

    @Schema(description="새폴더 경로", example="/chapter1/first")
    private String newFolderPath;

    @Schema(description="변경 전 폴더 경로", example="/chapter1/first")
    private String beforeFolderPath;

    @Schema(description="이동 후 폴더 경로(폴더명 변경)", example="/chapter1/second")
    private String renameFolderPath;

    @Schema(description="상위 폴더 경로", example="/chapter1/second")
    private String moveFolderPath;

    @Schema(description="삭제할 폴더 경로", example="/chapter1/first")
    private String deleteFolderPath;

    @Schema(description="삭제할 파일명", example="")
    private List<String> deleteFileList;
}
