package kr.or.kpf.lms.biz.system.code.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.system.code.vo.CreateCommonCode;
import kr.or.kpf.lms.biz.system.code.vo.UpdateCommonCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(name="CommonCodeViewResponseVO", description="공통 코드 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonCodeViewResponseVO {

    @Schema(description="개별 코드", example="CODE00001")
    private String individualCode;

    @Schema(description="코드", example="CONTENTS_CATEGORY")
    private String code;

    @Schema(description="상위 개별 코드", example="CODE00001")
    private String upIndividualCode;

    @Schema(description="코드 명", example="콘텐츠 카테코리")
    private String codeName;

    @Schema(description="코드 관리 깊이", example="0")
    private Integer codeDepth;

    @Schema(description="코드 정렬 번호", example="0")
    private Integer codeSort;

    @Schema(description="코드 설명", example="콘텐츠 카테고리 코드입니다.")
    private String codeComments;

    @Schema(description="등록 일시", example="2022-09-07T00:00:00")
    private LocalDateTime createDateTime;

    @Schema(description="수정 일시", example="2022-09-07T23:59:59")
    private LocalDateTime updateDateTime;
}
