package kr.or.kpf.lms.biz.homepage.notice.vo.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * 공지사항 관련 요청 객체
 */
@Schema(name="NoticeViewRequestVO", description="공지사항 View 관련 요청 객체")
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class NoticeViewRequestVO extends CSViewVOSupport {

    /** 공지사항 일련 번호 */
    @Schema(description="공지사항 일련 번호")
    private String noticeSerialNo;

    /** 공지사항 구분 */
    @Schema(description="공지사항 구분")
    private String noticeType;

    /** 검색어 범위 */
    @Schema(description="검색어 범위")
    private String containTextType;

    /** 검색에 포함할 단어 */
    @Schema(description="검색에 포함할 단어")
    private String containText;
}
