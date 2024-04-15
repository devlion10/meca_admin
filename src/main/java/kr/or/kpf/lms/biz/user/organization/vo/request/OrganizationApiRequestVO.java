package kr.or.kpf.lms.biz.user.organization.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.user.organization.controller.OrganizationApiController;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(name="OrganizationApiRequestVO", description="기관 API 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrganizationApiRequestVO {

    @Schema(description="소속 기관 코드", example="")
    @NotEmpty(groups={OrganizationApiController.UpdateOrganization.class}, message="소속 기관 코드는 필수 입니다.")
    private String organizationCode;

    @Schema(description="소속 기관 명", example="")
    @NotEmpty(groups={OrganizationApiController.CreateOrganization.class}, message="소속 기관 명는 필수 입니다.")
    private String organizationName;

    @Schema(description="소속 기관 서브 기관 명", example="")
    private String organizationSubName;

    @Schema(description="기관 타입 (1: 매체사, 2: 기관, 3: 학교)", example="1")
    @NotEmpty(groups={OrganizationApiController.CreateOrganization.class}, message="기관 타입은 필수입니다.")
    private String organizationType;

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

    /** 소속 기관 홈페이지 주소 */
    @Schema(description="홈페이지 주소", example="www.naver.com")
    private String organizationHomepage;

    @Schema(description="사용 여부", example="")
    @NotNull(groups={OrganizationApiController.CreateOrganization.class}, message="사용 여부는 필수 입니다.")
    private Boolean isUsable;
}
