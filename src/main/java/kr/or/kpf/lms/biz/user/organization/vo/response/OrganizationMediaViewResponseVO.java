package kr.or.kpf.lms.biz.user.organization.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="OrganizationMediaViewResponseVO", description="매체 View 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationMediaViewResponseVO extends CSResponseVOSupport {

    /** 매체사 코드 */
    @Schema(description="매체사 코드", example="")
    private String organizationCode;

    /** 매체사명 */
    @Schema(description="매체사명", example="")
    private String organizationName;

    /** 매체 코드 */
    @Schema(description="매체 코드", example="")
    private String mediaCode;

    /** 매체명 */
    @Schema(description="매체명", example="")
    private String mediaName;

    /** 매체 대분류 */
    @Schema(description="매체 대분류", example="")
    private String mediaClsName1;

    /** 매체 중분류 */
    @Schema(description="매체 중분류", example="")
    private String mediaClsName2;

    /** 지역 */
    @Schema(description="지역", example="")
    private String mediaArea;

    /** 등록일시 */
    @Schema(description="등록일시", example="")
    private String createDateTime;

    /** 등록자 아이디 */
    @Schema(description="등록자 아이디", example="")
    private String registUserId;

    /** 사용 여부 */
    @Schema(description="사용 여부", example="")
    private Boolean isUsable;
}
