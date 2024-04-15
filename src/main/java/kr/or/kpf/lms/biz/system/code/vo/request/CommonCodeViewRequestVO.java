package kr.or.kpf.lms.biz.system.code.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="CommonCodeViewRequestVO", description="시스템 관리 > 공통 코드 관리 메뉴 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonCodeViewRequestVO extends CSViewVOSupport {

    /** 상위 코드 */
    private String upIndividualCode;

    /** 상위 코드 명 */
    private String codeName;

}
