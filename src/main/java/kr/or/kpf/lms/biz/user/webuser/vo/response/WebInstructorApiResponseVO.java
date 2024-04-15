package kr.or.kpf.lms.biz.user.webuser.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.converter.BooleanConverter;
import kr.or.kpf.lms.common.converter.DateToStringConverter;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.repository.entity.BizInstructorIdentify;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(name="WebInstructorApiResponseVO", description="강사 관리 API 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebInstructorApiResponseVO extends CSResponseVOSupport {

    @Schema(description="웹 회원 일련 번호", example="")
    private Long userSerialNo;

    @Schema(description="회원 아이디", example="")
    private String userId;

    @Schema(description="웹 회원 명", example="")
    private String userName;

    @Schema(description="웹 회원 생년월일", example="")
    private String birthDay;

    @Schema(description="권한 그룹", example="")
    private String roleGroup;

    @Schema(description="사업 참여 권한", example="")
    private String businessAuthority;

    @Schema(description="회원 연락처", example="")
    private String phone;

    @Schema(description="SMS 수신 여부", example="")
    private Boolean isSmsAgree;

    @Schema(description="이메일", example="")
    private String email;

    @Schema(description="이메일 수신 여부", example="")
    private Boolean isEmailAgree;

    @Schema(description="성별 코드", example="")
    private String gender;

    @Schema(description="인증 DN 값", example="")
    private String certValue;

    @Schema(description="DI 값", example="")
    private String di;

    @Schema(description="CI 값", example="")
    private String ci;

    @Schema(description="비밀번호 변경 일자", example="")
    private String passwordChangeDate;

    @Schema(description="최근 로그인 일시", example="")
    private String lastLoginDateTime;

    @Schema(description="휴면 계정 전환 일자", example="")
    private String dormancyDate;

    @Schema(description="탈퇴 일자", example="")
    private String withDrawDate;

    @Schema(description="잠금 수", example="")
    private Integer lockCount;

    @Schema(description="잠금 일시", example="")
    private LocalDateTime lockDateTime;

    @Schema(description="잠금 여부", example="")
    @Convert(converter = BooleanConverter.class)
    private Boolean isLock;

    @Schema(description="회원 상태", example="")
    private String state;

    @Schema(description="등록 일시", example="")
    @Convert(converter= DateToStringConverter.class)
    private String createDateTime;

    @Schema(description="등록자 유저 ID", example="")
    private String registUserId;

    @Schema(description="수정 일시", example="")
    @Convert(converter=DateToStringConverter.class)
    private String updateDateTime;

    @Schema(description="수정자 유저 ID", example="")
    private String modifyUserId;

    @Transient
    @Schema(description="강의확인서 리스트(강의 이력 표시용)", example="")
    private List<BizInstructorIdentify> bizInstructorIdentifyList = new ArrayList<>();
}
