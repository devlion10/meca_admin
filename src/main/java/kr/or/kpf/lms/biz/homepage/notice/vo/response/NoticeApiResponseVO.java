package kr.or.kpf.lms.biz.homepage.notice.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.*;

import javax.persistence.Column;

@Schema(name="NoticeApiResponseVO", description="공지사항 API 관련 응답 객체")
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class NoticeApiResponseVO extends CSResponseVOSupport {

    /** 공지사항 일련 번호 */
    @Schema(description="공지사항 일련 번호", example="")
    private String noticeSerialNo;

    /** 공지사항 타입 */
    @Schema(description="공지사항 타입", example="")
    private String noticeType;

    /** 제목 */
    @Schema(description="제목", example="")
    private String title;

    /** 내용 */
    @Schema(description="내용", example="")
    private String contents;

    /** 첨부 파일 경로 */
    @Schema(description="첨부 파일 경로")
    private String attachFilePath;

    /** 첨부 파일 사이즈 */
    @Schema(description="첨부 파일 사이즈")
    private Long attachFileSize;

    /** 상위 노출 여부 */
    @Schema(description="상위 노출 여부", example="")
    private Boolean isTop;

    /** 신규 공지사항 여부 */
    @Schema(description="신규 공지사항 여부", example="")
    private Boolean isNew;

}
