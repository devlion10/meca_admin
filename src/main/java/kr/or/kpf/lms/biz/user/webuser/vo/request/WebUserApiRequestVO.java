package kr.or.kpf.lms.biz.user.webuser.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.user.webuser.controller.WebUserApiController;
import kr.or.kpf.lms.biz.user.webuser.vo.ChangePassword;
import kr.or.kpf.lms.biz.user.webuser.vo.CreateWebUser;
import kr.or.kpf.lms.biz.user.webuser.vo.DeleteWebUser;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;

@Schema(name="WebUserApiRequestVO", description="웹 회원 관리 API 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude={"password"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebUserApiRequestVO {

    @Schema(description="회원 아이디", required = true, example="")
    @NotEmpty(groups={WebUserApiController.UpdateWebUser.class}, message="회원 아이디는 필수 입니다.")
    private String userId;

    @Schema(description="비밀번호", required = true, example="")
    @NotEmpty(groups={ChangePassword.class}, message="비밀번호는 필수 입니다.")
    private String password;

    @Schema(description="권한 그룹", required = true, example="")
    private String roleGroup;

    @Schema(description="신청 사업 참여 권한", required = false, example="")
    private String businessAuthority;

    @Schema(description="성별 코드", required = true, example="")
    private String gender;

    @Schema(description="회원 연락처", required = true, example="")
    private String phone;

    @Schema(description="SMS 수신 여부", required = true, example="")
    private Boolean isSmsAgree;

    @Schema(description="이메일", required = true, example="")
    private String email;

    @Schema(description="이메일 수신 여부", required = true, example="")
    private Boolean isEmailAgree;

    @Schema(description="회원 상태", required = true, example="")
    private String state;

    @Schema(description="소속 기관 명", required = true, example="")
    private String organizationName;

    @Schema(description="소속 기관 서브 기관 명", required = true, example="")
    private String organizationSubName;

    @Schema(description="매체 코드", example="")
    private String mediaCode;

    @Schema(description="소속 기관 우편번호", required = true, example="")
    private String organizationZipCode;

    @Schema(description="소속 기관 주소1", required = true, example="")
    private String organizationAddress1;

    @Schema(description="소속 기관 주소2", required = true, example="")
    private String organizationAddress2;

    @Schema(description="소속 기관 연락처", required = true, example="")
    private String organizationTelNumber;

    @Schema(description="소속 기관 팩스 번호", required = true, example="")
    private String organizationFaxNumber;

    @Schema(description="소속 기관 코드", required = true, example="")
    private String organizationCode;

    @Schema(description="소속 부서 ", required = true, example="")
    private String department;

    @Schema(description="직급", required = true, example="")
    private String rank;

    @Schema(description="직책", required = true, example="")
    private String position;

    @Schema(description="개인 나이스 번호(교원)", example="")
    private String personalNiceNo;

    @Schema(description="개인주소우편번호", example="")
    private String userZipCode;

    @Schema(description="개인집주소", example="")
    private String userAddress1;

    @Schema(description="개인집주소상세", example="")
    private String userAddress2;

    @Schema(description="회원승인여부", example="")
    private String approFlag;

    @Schema(description="회원승인 유효기한", example="")
    private String approFlagDate;

    @Schema(description="튜터 권한", example="")
    private String tutorYn;
}
