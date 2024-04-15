package kr.or.kpf.lms.biz.education.reference.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.education.reference.controller.ReferenceRoomApiController;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Schema(name="ReferenceRoomApiRequestVO", description="자료실 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ReferenceRoomApiRequestVO {

    @Schema(description="자료실 시퀀스 번호")
    @NotNull(groups={ReferenceRoomApiController.UpdateReferenceRoom.class}, message="시퀀스 번호는 필수 입니다.")
    private BigInteger sequenceNo;

    @Schema(description="교육과정 코드", required = true, example="")
    @NotEmpty(groups={ReferenceRoomApiController.CreateReferenceRoom.class, ReferenceRoomApiController.UpdateReferenceRoom.class}, message="교육과정 코드는 필수 입니다.")
    private String curriculumCode;

    @Schema(description="자료실 제목", required = true, example="")
    @NotEmpty(groups={ReferenceRoomApiController.CreateReferenceRoom.class, ReferenceRoomApiController.UpdateReferenceRoom.class}, message="자료실 제목은 필수 입니다.")
    private String title;

    @Schema(description="자료실 내용", required = true, example="")
    @NotEmpty(groups={ReferenceRoomApiController.CreateReferenceRoom.class, ReferenceRoomApiController.UpdateReferenceRoom.class}, message="자료실 내용은 필수 입니다.")
    private String contents;

    @Schema(description="파일 삭제 여부")
    @Builder.Default
    private Boolean isDeleteFile = false;
}
