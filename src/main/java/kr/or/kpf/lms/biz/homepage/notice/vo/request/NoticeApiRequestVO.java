package kr.or.kpf.lms.biz.homepage.notice.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.homepage.notice.controller.NoticeApiController;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(name="NoticeApiRequestVO", description="공지사항 API 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoticeApiRequestVO {

    /** 공지사항 일련 번호 */
    @Schema(description="공지사항 일련 번호", required=true, example="")
    @NotEmpty(groups={NoticeApiController.UpdateNotice.class}, message="공지사항 일련 번호는 필수 입니다.")
    private String noticeSerialNo;

    /** 공지사항 타입 */
    @Schema(description="공지사항 타입", required=true, example="")
    @NotEmpty(groups={NoticeApiController.CreateNotice.class}, message="공지사항 타입는 필수 입니다.")
    private String noticeType;

    /** 제목 */
    @Schema(description="제목", required=true, example="")
    @NotEmpty(groups={NoticeApiController.CreateNotice.class}, message="제목는 필수 입니다.")
    private String title;

    /** 내용 */
    @Schema(description="내용", required=true, example="")
    @NotEmpty(groups={NoticeApiController.CreateNotice.class}, message="내용는 필수 입니다.")
    private String contents;

    /** 상위 노출 여부 */
    @Schema(description="상위 노출 여부", required=true, example="")
    @NotNull(groups={NoticeApiController.CreateNotice.class}, message="상위 노출 여부는 필수 입니다.")
    private Boolean isTop;
}
