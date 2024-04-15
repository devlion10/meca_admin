package kr.or.kpf.lms.biz.user.instructor.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="InstructorViewRequestVO", description="강사 관리 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class InstructorCustomViewRequestVO extends CSViewVOSupport {
    /** 강사 일련 번호 */
    private Long instrSerialNo;
    /** 강사 ID */
    private String userId;
    /** 강사 명 */
    private String instrNm;
    /** 강사 구분 */
    private String instrCtgr;
    /** 강사 상태 */
    private Integer instrStts;
}
