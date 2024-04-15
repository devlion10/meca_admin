package kr.or.kpf.lms.biz.user.organization.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.user.organization.controller.OrganizationApiController;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(name="OrganizationMediaApiRequestVO", description="매체 API 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrganizationMediaApiRequestVO {

    @Schema(description="매체 기관 코드", example="")
    @NotEmpty(groups={OrganizationApiController.UpdateOrganization.class}, message="매체 코드는 필수 입니다.")
    private String mediaCode;

    @Schema(description="소속 기관 명", example="")
    @NotEmpty(groups={OrganizationApiController.CreateOrganization.class}, message="매체 명는 필수 입니다.")
    private String mediaName;

    @Schema(description="매체사 코드 우편번호", example="")
    private String organizationCode;

    @Schema(description="매체 대분류", example="1")
    private String mediaClsName1;

    @Schema(description="매체 중분류", example="1")
    private String mediaClsName2;

    @Schema(description="매체 지역", example="")
    private String mediaArea;

    @Schema(description="사용 여부", example="")
    @NotNull(groups={OrganizationApiController.CreateOrganization.class}, message="사용 여부는 필수 입니다.")
    private Boolean isUsable;
}
