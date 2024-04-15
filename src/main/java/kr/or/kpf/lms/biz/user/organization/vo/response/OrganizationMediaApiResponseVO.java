package kr.or.kpf.lms.biz.user.organization.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.converter.BooleanConverter;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Id;

@Schema(name="OrganizationMediaApiResponseVO", description="매체 API 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationMediaApiResponseVO extends CSResponseVOSupport {

    /** 매체코드 */
    @Schema(description="매체코드", example="")
    private String mediaCode;

    /** 매체사코드 */
    @Schema(description="매체사코드", example="")
    private String organizationCode;

    /** 매체사명 */
    @Schema(description="매체사명", example="")
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

    /** 전사매체사용여부 */
    @Schema(description="전사매체사용여부", example="")
    private Boolean mediaAbmYN;

    /** 전사매체 코드 */
    @Schema(description="전사매체 코드", example="")
    private String mediaAbmCode;

    /** 삭제 여부 */
    @Schema(description="사용 여부", example="")
    private Boolean isDeleted;

    /** 사용 여부 */
    @Schema(description="사용 여부", example="")
    private Boolean isUsable;
}
