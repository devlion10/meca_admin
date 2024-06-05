package kr.or.kpf.lms.biz.user.webuser.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(name="WebUserViewRequestVO", description="웹 회원 관리 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class WebUserViewRequestVO extends CSViewVOSupport {
    /** 검색어 범위 */
    @ApiModelProperty(name="containTextType", value="검색어 범위", required=false, dataType="string", position=10, example = "")
    private String containTextType;

    /** 검색에 포함할 단어 */
    @ApiModelProperty(name="containText", value="검색에 포함할 단어", required=false, dataType="string", position=20, example = "")
    private String containText;

    /** 회원 코드 */
    private Long userSerialNo;
    /** 회원 ID */
    private String userId;
    /** 회원 명 */
    private String userName;

    /** 회원 유형 */
    private String roleGroup;
    /** 사업 참여 권한 */
    private String businessAuthority;

    /** 회원 유형 (S) */
    private String roleGroups;
    /** 사업 참여 권한 (S) */
    private String businessAuthoritys;
    /** 튜터 권한 */
    private String tutorYn;
    /** 소속명 */
    private String organizationName;
    /** 회원 상태 */
    private String state;


    /**
     * 아래는 회원 상태 집계 스케쥴러에서 사용
     */
    /** 회원 가입 일자 */
    private String joinDate;
    /** 휴면 계정 전환 일자 */
    private String dormancyDate;
    /** 탈퇴 일자 */
    private String withDrawDate;
    /** 최근 접속 일자 */
    private String lastLoginDate;


    /** 회원 팝업 구분값 */
    private String manager;
    /** 소속 */
    private String organizationCode;
}
