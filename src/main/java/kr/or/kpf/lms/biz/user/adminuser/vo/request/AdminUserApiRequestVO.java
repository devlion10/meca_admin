package kr.or.kpf.lms.biz.user.adminuser.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.user.adminuser.controller.AdminUserApiController;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;

@Schema(name="StatisticsAdminUserApiRequestVO", description="유저 API 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude={"password"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminUserApiRequestVO {

    @Schema(description="로그인 아이디", required = true, example="")
    @NotEmpty(groups={AdminUserApiController.CreateAdminUser.class, AdminUserApiController.UpdateAdminUser.class, AdminUserApiController.ChangeAdminPassword.class, AdminUserApiController.DeleteAdminUser.class}, message="로그인 아이디는 필수 입니다.")
    private String userId;

    @Schema(description="비밀번호", required = true, example="")
    @NotEmpty(groups={AdminUserApiController.CreateAdminUser.class, AdminUserApiController.ChangeAdminPassword.class, AdminUserApiController.DeleteAdminUser.class}, message="비밀번호는 필수 입니다.")
    @Null(groups={AdminUserApiController.UpdateAdminUser.class}, message="비밀번호 변경 기능을 이용해 주세요.")
    private String password;

    @NotEmpty(groups={AdminUserApiController.ChangeAdminPassword.class}, message="새로운 비밀번호는 필수 입니다.")
    private String newPassword;

    @Schema(description="사용자 명", required = true, example="")
    @NotEmpty(groups={AdminUserApiController.CreateAdminUser.class, AdminUserApiController.UpdateAdminUser.class}, message="사용자 명는 필수 입니다.")
    private String userName;

    @Schema(description="권한 그룹", required = true, example="GENERAL")
    @NotEmpty(groups={AdminUserApiController.CreateAdminUser.class, AdminUserApiController.UpdateAdminUser.class}, message="권한 그룹는 필수 입니다.")
    private String roleGroup;

    @Schema(description="사용자 이메일 주소", example="")
    private String email;

    @Schema(description="연락처", example="")
    private String telNo;

    @Schema(description="연락처", example="")
    private String phone;

    @Schema(description="본/지사", example="")
    private String department;

    @Schema(description="팀", example="")
    private String position;

    @Schema(description="직급", example="")
    private String rank;

    @Schema(description="허용 IP", example="")
    private String availableIp;

    @Schema(description="잠금 여부", example="")
    private boolean isLock;

    @Schema(description="상태", example="")
    private String state;
}
