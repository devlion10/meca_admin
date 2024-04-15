package kr.or.kpf.lms.biz.user.organization.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="OrganizationViewRequestVO", description="기관 정보 관리 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OrganizationViewRequestVO extends CSViewVOSupport {

    /** 기관 타입*/
    private String organizationType;

    /** 기관 코드 */
    private String organizationCode;

    /** 소속 기관 명 */
    private String organizationName;
    /** 소속 기관 명 */
    private String organizationAddress1;

    /** 소속 기관 사업자 등록번호 */
    private String bizLicenseNumber;

    /** 사용 여부 */
    private Boolean isUsable;


    /** 기관 타입 not */
    private String notOrganizationType;
}
