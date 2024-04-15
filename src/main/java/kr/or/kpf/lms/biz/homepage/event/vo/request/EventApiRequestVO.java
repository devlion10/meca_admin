package kr.or.kpf.lms.biz.homepage.event.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.CreateArchiveClassGuide;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.UpdateArchiveClassGuide;
import kr.or.kpf.lms.biz.homepage.event.vo.CreateEvent;
import kr.or.kpf.lms.biz.homepage.event.vo.UpdateEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Schema(name="EventApiRequestVO", description="이벤트/설문")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventApiRequestVO {

    /** 고유키 */
    @Schema(description="고유키", required=false, example="")
    private Long sequenceNo;

    /** 타입 */
    @Schema(description="타입", required=true, example="")
    @NotEmpty(groups={CreateEvent.class, UpdateEvent.class}, message="타입은 필수 입니다.")
    private String type;

    /** 제목 */
    @Schema(description="제목", required=true, example="")
    @NotEmpty(groups={CreateEvent.class, UpdateEvent.class}, message="제목은 필수 입니다.")
    private String title;

    /** 내용 */
    @Schema(description="내용", required=true, example="")
    @NotEmpty(groups={CreateEvent.class, UpdateEvent.class}, message="내용은 필수 입니다.")
    private String contents;

    /** 썸네일 파일 경로 */
    @Schema(description="썸네일 파일 경로", required=true, example="")
    private String thumbFilePath;

    /** 시작 일시 */
    @Schema(description="시작 일시", required=true, example="")
    private String startDt;

    /** 종료 일시 */
    @Schema(description="종료 일시", required=true, example="")
    private String endDt;

    /** 상태 */
    @Schema(description="상태(0:종료, 1:진행)", required=true, example="1")
    @NotNull(groups={CreateEvent.class, UpdateEvent.class}, message="상태는 필수 입니다.")
    private int status;

}
