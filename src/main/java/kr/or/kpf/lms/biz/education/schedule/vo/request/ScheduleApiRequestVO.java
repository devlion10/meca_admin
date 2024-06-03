package kr.or.kpf.lms.biz.education.schedule.vo.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.education.schedule.controller.ScheduleApiController;
import kr.or.kpf.lms.biz.education.schedule.vo.LectureInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(name="ScheduleApiRequestVO", description="교육 일정 관리 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ScheduleApiRequestVO {

    @Schema(description="과정 타입")
    @NotEmpty(groups={ScheduleApiController.CreateEducationSchedule.class, ScheduleApiController.UpdateEducationSchedule.class}, message="과정 타입은 필수 입니다.")
    private String educationType;

    @Schema(description="과정 개설 코드")
    @NotEmpty(groups={ScheduleApiController.UpdateEducationSchedule.class}, message="과정 개설 코드는 필수 입니다.")
    private String educationPlanCode;

    @Schema(description="과정 코드", example="CRCL000001")
    @NotEmpty(groups={ScheduleApiController.CreateEducationSchedule.class, ScheduleApiController.UpdateEducationSchedule.class}, message="과정 코드는 필수 입니다.")
    private String curriculumCode;

    @Schema(description="과정 이수 코드", example="false")
    private String programNumber;

    @Schema(description="운영 타입(1: 기수 운영, 2: 상시 운영)", example="false")
    private String operationType;

    @Schema(description="교육 계획 연도", example="2022")
    private String yearOfEducationPlan;

    @Schema(description="교육 계획 연도 단계", example="01")
    private String yearOfEducationPlanStep;

    @Schema(description="교육 운영 지사", example="")
    private String province;

    @Schema(description="상시 교육 기한 수", example="0")
    private Integer alwaysEducationTerm;

    @Schema(description="사용 여부", example="true")
    @NotNull(groups={ScheduleApiController.CreateEducationSchedule.class, ScheduleApiController.UpdateEducationSchedule.class}, message="사용 여부는 필수 입니다.")
    private Boolean isUsable;

    @Schema(description="상단 고정 여부", example="true")
    @NotNull(groups={ScheduleApiController.CreateEducationSchedule.class, ScheduleApiController.UpdateEducationSchedule.class}, message="상단 고정 여부는 필수 입니다.")
    private Boolean isTop;

    @Schema(description="신청 시작 일자", example="2022-09-01")
    private String applyBeginDate;

    @Schema(hidden = true)
    @JsonIgnore
    private String applyBeginDateTime;

    public void setApplyBeginDate(String applyBeginDate) {
        this.applyBeginDate = applyBeginDate;
        if(!StringUtils.isEmpty(applyBeginDate)) {
            this.applyBeginDateTime = new StringBuilder(applyBeginDate).append(" 00:00:00").toString();
        }
    }

    @Schema(description="신청 종료 일자", example="2022-09-30")
    private String applyEndDate;

    @Schema(hidden = true)
    @JsonIgnore
    private String applyEndDateTime;

    public void setApplyEndDate(String applyEndDate) {
        this.applyEndDate = applyEndDate;
        if(!StringUtils.isEmpty(applyEndDate)) {
            this.applyEndDateTime = new StringBuilder(applyEndDate).append(" 23:59:59").toString();
        }
    }

    @Schema(description="취소 시작 일자", example="2022-09-01")
    private String cancelBeginDate;

    @Schema(hidden = true)
    @JsonIgnore
    private String cancelBeginDateTime;

    public void setCancelBeginDate(String cancelBeginDate) {
        this.cancelBeginDate = cancelBeginDate;
        if(!StringUtils.isEmpty(cancelBeginDate)) {
            this.cancelBeginDateTime = new StringBuilder(cancelBeginDate).append(" 00:00:00").toString();
        }
    }

    @Schema(description="취소 종료 일자", example="2022-09-30")
    private String cancelEndDate;

    @Schema(hidden = true)
    @JsonIgnore
    private String cancelEndDateTime;

    public void setCancelEndDate(String cancelEndDate) {
        this.cancelEndDate = cancelEndDate;
        if(!StringUtils.isEmpty(cancelEndDate)) {
            this.cancelEndDateTime = new StringBuilder(cancelEndDate).append(" 23:59:59").toString();
        }
    }

    @Schema(description="운영 시작 일시", example="2022-09-01 00:00:00")
    private String operationBeginDateTime;

    @Schema(description="운영 종료 일시", example="2022-09-01 23:59:59")
    private String operationEndDateTime;

    @Schema(description="교육 장소", example="")
    private String educationPlace;

    @Schema(description="정원(화상교육)", example="")
    private Integer numberOfPeople;

    @Schema(description="정원(집합교육)", example="")
    private Integer numberOfPeopleParallel;

    @Schema(description="숙박 제공")
    private Boolean isAccommodation;

    @Schema(description="복습제공 여부")
    private Boolean isReview;

    @Schema(description="복습 기간(월단위)", example="")
    private Integer availableReviewTerm;

    @Schema(description="복습 영상 경로", example="")
    private String reviewPath;

    @Schema(description="화상 강의 진행 여부", example="")
    private Boolean isVideoLecture;

    @Schema(description="화상 강의 경로", example="")
    private String videoLecturePath;

    @Schema(description="담당자(아이디)", example="")
    private String eduPlanPic;

    @Schema(description="담당자 연락처", example="")
    private String picTel;

    @Schema(description="출석 QR 파일 경로", example="")
    private String qrPath;

    @Schema(description = "강사(강의) 정보")
    private List<LectureInfo> lectureInfoList;
}
