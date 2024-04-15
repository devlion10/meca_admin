package kr.or.kpf.lms.biz.business.instructor.identify.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.CreateBizInstructorIdentify;
import kr.or.kpf.lms.biz.business.instructor.identify.vo.UpdateBizInstructorIdentify;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 강의확인서 관련 요청 객체
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class BizInstructorIdentifyApiRequestVO {
    @Schema(description="강의확인서 일련변호", required = false, example="1")
    private String bizInstrIdntyNo;

    @Schema(description="기관 신청 일련 번호", required = true, example="1")
    @NotEmpty(groups={CreateBizInstructorIdentify.class, UpdateBizInstructorIdentify.class}, message="기관 신청 일련 번호은 필수 입니다.")
    private String bizOrgAplyNo;

    @Schema(description="강의확인서 강의시간표 -> 날짜 조회 방식 변경", required = true, example="202212")
    @NotEmpty(groups={CreateBizInstructorIdentify.class, UpdateBizInstructorIdentify.class}, message="강의확인서 강의시간표은 필수 입니다.")
    private String bizInstrIdntyYm;

    @Schema(description="강의확인서 상태 (0: 임시저장, 1: 제출(접수), 2: 기관 승인, 3: 관리자 승인(지출 접수), 4: 지출 완료, 9: 반려)", required = true, example="1")
    @NotNull(groups={CreateBizInstructorIdentify.class, UpdateBizInstructorIdentify.class}, message="강의확인서 상태는 필수 입니다.")
    private Integer bizInstrIdntyStts;

    @Schema(description="강의확인서 수업지도안", required = false, example="0")
    private String bizInstrIdntyLsnFile;

    @Schema(description="강의확인서 수업자료안", required = false, example="0")
    private String bizInstrIdntyAtchFile;

    @Schema(description="강의확인서 강의 시간", required = false, example="0")
    private String bizInstrIdntyTime;

    @Schema(description="강의확인서 승인 일시", required = false, example="0")
    private String bizInstrIdntyAprvDt;

    @Schema(description="지출연월")
    private String bizInstrIdntyPayYm;

    @Schema(description="강의확인서 최종 금액", required = false, example="0")
    private String bizInstrIdntyAmt;

    @Schema(description="강의확인서 일련변호", required = false, example="0")
    private List<String> IdntyNo;

}
