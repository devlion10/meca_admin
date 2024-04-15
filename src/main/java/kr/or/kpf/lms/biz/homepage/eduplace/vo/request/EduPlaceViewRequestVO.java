package kr.or.kpf.lms.biz.homepage.eduplace.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.*;

@Schema(name="EduPlaceViewRequestVO", description="교육장 장소 신청 View 관련 요청 객체")
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EduPlaceViewRequestVO extends CSViewVOSupport {

    /** 검색어 범위 */
    @Schema(description="검색어 범위")
    private String containTextType;

    /** 검색에 포함할 단어 */
    @Schema(description="검색에 포함할 단어")
    private String containText;

    /** 고유키 */
    @Schema(description="고유키")
    private Long sequenceNo;

    /** 신청자 */
    @Schema(description="신청자")
    private String aplyUserNm;

    /** 신청자 아이디 */
    @Schema(description="신청자 아이디")
    private String userId;

    /** 신청자 연락처 */
    @Schema(description="신청자 연락처")
    private String aplyPhone;

}
