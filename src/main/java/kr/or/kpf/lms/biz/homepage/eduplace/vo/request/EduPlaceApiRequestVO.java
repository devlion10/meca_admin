package kr.or.kpf.lms.biz.homepage.eduplace.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.homepage.event.vo.CreateEvent;
import kr.or.kpf.lms.biz.homepage.event.vo.UpdateEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Schema(name="EduPlaceApiRequestVO", description="교육장 신청")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EduPlaceApiRequestVO {

    /** 고유키 */
    @Schema(description="고유키", required=false, example="")
    private Long sequenceNo;

    /** 제목 */
    @Schema(description="제목", required=true, example="")
    @NotEmpty(groups={CreateEvent.class, UpdateEvent.class}, message="제목은 필수 입니다.")
    private String title;

    /** 내용 */
    @Schema(description="내용", required=true, example="")
    @NotEmpty(groups={CreateEvent.class, UpdateEvent.class}, message="내용은 필수 입니다.")
    private String contents;

    /** 첨부 파일 경로 */
    @Schema(description="첨부 파일 경로", required=true, example="")
    private String atchFilePath;

    /** 신청자 */
    @Schema(description="신청자", required=true, example="")
    private String aplyUserNm;

    /** 신청자 연락처 */
    @Schema(description="신청자 연락처", required=true, example="")
    private String aplyPhone;

    /** 신청자 이메일 주소 */
    @Schema(description="신청자 이메일 주소", required=true, example="")
    private String aplyEmlAddr;



}
