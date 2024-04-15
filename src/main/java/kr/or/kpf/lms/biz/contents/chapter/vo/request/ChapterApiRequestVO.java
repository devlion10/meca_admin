package kr.or.kpf.lms.biz.contents.chapter.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.contents.chapter.controller.ChapterApiController;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(name="ChapterApiRequestVO", description="챕터 관리 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ChapterApiRequestVO {

    @Schema(description="콘텐츠 코드", example="CTS000001")
    @NotEmpty(groups={ChapterApiController.CreateChapter.class, ChapterApiController.UpdateChapter.class}, message="콘텐츠 코드는 필수 입니다.")
    private String contentsCode;

    @Schema(description="챕터 코드", example="CHAP000001")
    @NotEmpty(groups={ChapterApiController.UpdateChapter.class}, message="챕터 코드는 필수 입니다.")
    private String chapterCode;

    @Schema(description="챕터 명", example="")
    @NotEmpty(groups={ChapterApiController.CreateChapter.class}, message="챕터 명은 필수 입니다.")
    private String chapterName;

    @Schema(description="챕터 제목", example="")
    @NotEmpty(groups={ChapterApiController.CreateChapter.class}, message="챕터 제목은 필수 입니다.")
    private String chapterTitle;

    @Schema(description="강사", example="")
    private String lecturer;

    @Schema(description="교육 장소", example="")
    private String educationPlace;

    @Schema(description="챕터 정렬 번호", example="false")
    @NotNull(groups={ChapterApiController.CreateChapter.class}, message="챕터 정렬 번호는 필수 입니다.")
    private Integer sortNo;

    @Schema(description="사용 여부", example="false")
    @NotNull(groups={ChapterApiController.CreateChapter.class}, message="사용 여부는 필수 입니다.")
    @Builder.Default
    private Boolean isUsable = false;

    @Schema(description="메모", example="")
    private String memo;

    @Schema(description="챕터 요청 리스트(다건 처리)")
    private List<ChapterApiRequestVO> chapterInfoList;
}
