package kr.or.kpf.lms.biz.homepage.archive.data.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="PublicationFileManagementViewRequestVO", description="출간물 폴더 관리 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PublicationFileManagementViewRequestVO extends CSViewVOSupport {

    @Schema(description="출간물 코드", example="CTS000001")
    private Long sequenceNo;

    @Schema(description="폴더 경로", example="/chapter1/first")
    private String folderPath;

}
