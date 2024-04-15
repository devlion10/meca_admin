package kr.or.kpf.lms.biz.contents.section.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.contents.section.controller.SectionApiController;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(name="SectionApiRequestVO", description="섹션(절) 관리 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SectionApiRequestVO {

    @Schema(description="챕터(장) 코드")
    @NotEmpty(groups={SectionApiController.UpdateSection.class}, message="챕터(장) 코드는 필수 입니다.")
    private String chapterCode;

    @Schema(description="섹션(절) 코드")
    @NotEmpty(groups={SectionApiController.CreateSection.class, SectionApiController.UpdateSection.class}, message="섹션(절) 코드는 필수 입니다.")
    private String sectionCode;

    @Schema(description="섹션(절) 제목")
    @NotEmpty(groups={SectionApiController.CreateSection.class}, message="섹션(절) 제목은 필수 입니다.")
    private String sectionName;

    @Schema(description="영상/페이지 링크")
    private String link;

    @Schema(description="교육 시간 (분단위)")
    @NotNull(groups={SectionApiController.CreateSection.class}, message="교육 시간 (분단위)는 필수 입니다.")
    private Integer educationPerMinute;

    @Schema(description="챕터 정렬 번호")
    @NotNull(groups={SectionApiController.CreateSection.class}, message="챕터 정렬 번호는 필수 입니다.")
    private Integer sortNo;

    @Schema(description="섹션(절) 요청 리스트(다건 처리)")
    private List<SectionApiRequestVO> sectionInfoList;
}
