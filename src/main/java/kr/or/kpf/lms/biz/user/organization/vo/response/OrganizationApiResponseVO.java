package kr.or.kpf.lms.biz.user.organization.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Schema(name="OrganizationApiResponseVO", description="기관 API 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationApiResponseVO extends CSResponseVOSupport {

    @Schema(description="소속 기관 코드", example="")
    private String organizationCode;

    @Schema(description="소속 기관 명", example="")
    private String organizationName;

    @Schema(description="소속 기관 서브 기관 명", example="")
    private String organizationSubName;

    @Schema(description="소속 기관 사업자 등록번호", example="")
    private String bizLicenseNumber;

    @Schema(description="소속 기관 우편번호", example="")
    private String organizationZipCode;

    @Schema(description="소속 기관 주소1", example="")
    private String organizationAddress1;

    @Schema(description="소속 기관 주소2", example="")
    private String organizationAddress2;

    @Schema(description="소속 기관 연락처", example="")
    private String organizationTelNumber;

    @Schema(description="소속 기관 팩스 번호", example="")
    private String organizationFaxNumber;

    @Schema(description="소속 기관 대표자명", example="")
    private String organizationRepresentativeName;

    /** 소속 기관 홈페이지 주소 */
    @Schema(description="홈페이지 주소", example="www.naver.com")
    private String organizationHomepage;

    @Schema(description="사용 여부", example="")
    private Boolean isUsable;
}
