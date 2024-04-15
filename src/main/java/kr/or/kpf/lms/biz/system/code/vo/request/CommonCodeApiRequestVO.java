package kr.or.kpf.lms.biz.system.code.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.system.code.vo.CreateCommonCode;
import kr.or.kpf.lms.biz.system.code.vo.UpdateCommonCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(name="CommonCodeApiRequestVO", description="공통 코드 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonCodeApiRequestVO {

    @Schema(description="개별 코드", required = true, example="CODE00001")
    @NotEmpty(groups={UpdateCommonCode.class}, message="개별 코드는 필수 입니다.")
    private String individualCode;

    @Schema(description="코드", required = true, example="CONTENTS_CATEGORY")
    @NotEmpty(groups={CreateCommonCode.class, UpdateCommonCode.class}, message="코드는 필수 입니다.")
    private String code;

    @Schema(description="상위 개별 코드", required = true, example="CODE00001")
    @NotEmpty(groups={CreateCommonCode.class, UpdateCommonCode.class}, message="상위 개별 코드는 필수 입니다.")
    private String upIndividualCode;

    @Schema(description="코드 명", required = true, example="콘텐츠 카테코리")
    @NotEmpty(groups={CreateCommonCode.class, UpdateCommonCode.class}, message="코드 명은 필수 입니다.")
    private String codeName;

    @Schema(description="코드 관리 깊이", required = true, example="0")
    @NotNull(groups={CreateCommonCode.class, UpdateCommonCode.class}, message="코드 관리 깊이는 필수 입니다.")
    private Integer codeDepth;

    @Schema(description="코드 정렬 번호", required = true, example="0")
    @NotNull(groups={CreateCommonCode.class, UpdateCommonCode.class}, message="코드 정렬 번호는 필수 입니다.")
    private Integer codeSort;

    @Schema(description="코드 설명", required = true, example="콘텐츠 카테고리 코드입니다.")
    @NotEmpty(groups={CreateCommonCode.class, UpdateCommonCode.class}, message="코드 설명은 필수 입니다.")
    private String codeComments;
}
