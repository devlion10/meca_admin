package kr.or.kpf.lms.biz.homepage.archive.data.vo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.contents.contents.vo.ContentsFolderInfo;
import kr.or.kpf.lms.biz.homepage.archive.data.vo.PublicationFolderInfo;
import kr.or.kpf.lms.common.converter.BooleanConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import java.util.List;

@Schema(name="PublicationFileManagementViewResponseVO", description="출간물 폴더 관리 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PublicationFileManagementViewResponseVO {

    /** 출간물 코드 */
    @Schema(description="출간물 코드")
    private Long sequenceNo;

    /** 출간물 명 */
    @Schema(description="출간물 명")
    private String title;

    /** 썸네일 파일 경로 */
    @Schema(description="썸네일 파일 경로")
    private String thumbFilePath;

    /** 조회수 */
    @Schema(description="조회수")
    private Long viewCnt;

    /** 사용 여부 */
    @Schema(description="사용 여부")
    @Convert(converter= BooleanConverter.class)
    private Boolean isUse;

    /** 하위 폴더 */
    private List<PublicationFolderInfo> publicationFolderInfos;
}
