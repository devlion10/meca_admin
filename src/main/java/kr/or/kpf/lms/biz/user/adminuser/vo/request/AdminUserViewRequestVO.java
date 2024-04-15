package kr.or.kpf.lms.biz.user.adminuser.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Schema(name="AdminUserViewRequestVO", description="어드민 계정 관리 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AdminUserViewRequestVO  extends CSViewVOSupport {
    /** 검색어 범위 */
    @ApiModelProperty(name="containTextType", value="검색어 범위", required=false, dataType="string", position=10, example = "")
    private String containTextType;

    /** 검색에 포함할 단어 */
    @ApiModelProperty(name="containText", value="검색에 포함할 단어", required=false, dataType="string", position=20, example = "")
    private String containText;

    /** 사용자 일련 번호 */
    private Long userSerialNo;
    /** 권한 그룹 */
    private String roleGroup;
    /** 권한 그룹(검색용) */
    private String roleGroups;
    /** 잠금 여부 */
    private Boolean isLock;
    /** 계정명 */
    private String userName;
    /** 아이디 */
    private String userId;
    /** 휴대전화 */
    private String phone;
    /** 관리자 상태 (0: 미사용, 1: 사용) */
    private String state;


    /** 팝업 구분 */
    private String manager;
}
