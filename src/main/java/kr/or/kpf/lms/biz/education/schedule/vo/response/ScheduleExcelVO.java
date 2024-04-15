package kr.or.kpf.lms.biz.education.schedule.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.converter.DateToStringConverter;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;

@Schema(name="ScheduleExcelVO", description="과정개설 엑셀 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleExcelVO {

    @ExcelColumn(headerName = "운영구분")
    @Schema(description="운영 타입(1: 기수 운영, 2: 상시 운영)", example="false")
    private String operationType;

    @ExcelColumn(headerName = "과정 유형")
    @Schema(description="과정 유형 (1: 화상 교육, 2: 집합 교육, 3: 이러닝 교육)", example="")
    private String educationType;

    @ExcelColumn(headerName = "과정카테고리")
    @Schema(description="교육 과정 카테고리", example="")
    private String categoryCode;

    @ExcelColumn(headerName = "과정명")
    @Schema(description="과정 명", example="")
    private String curriculumName;

    @ExcelColumn(headerName = "연도")
    @Schema(description="교육 계획 연도", example="2022")
    private String yearOfEducationPlan;

    @ExcelColumn(headerName = "연도 단계")
    @Schema(description="교육 계획 연도 단계", example="01")
    private String yearOfEducationPlanStep;

    @ExcelColumn(headerName = "운영 지사")
    @Schema(description="교육 운영 지사", example="")
    private String province;

    @ExcelColumn(headerName = "기한")
    @Schema(description="상시 교육 기한 수", example="0")
    private Integer alwaysEducationTerm;

    @ExcelColumn(headerName = "사용 여부")
    @Schema(description="사용 여부", example="true")
    private String state;

    @Schema(description="사용 여부", example="true")
    private Boolean isUsable;

    @ExcelColumn(headerName = "신청 시작")
    @Schema(description="신청 시작 일자", example="2022-09-01")
    private String applyBeginDateTime;

    @ExcelColumn(headerName = "신청 종료")
    @Schema(description="신청 종료 일자", example="2022-09-30")
    private String applyEndDateTime;

    @ExcelColumn(headerName = "취소 시작")
    @Schema(description="취소 시작 일자", example="2022-09-01")
    private String cancelBeginDateTime;

    @ExcelColumn(headerName = "취소 종료")
    @Schema(description="취소 종료 일자", example="2022-09-30")
    private String cancelEndDateTime;

    @ExcelColumn(headerName = "운영 시작")
    @Schema(description="운영 시작 일시", example="2022-09-01 00:00:00")
    private String operationBeginDateTime;

    @ExcelColumn(headerName = "운영 종료")
    @Schema(description="운영 종료 일시", example="2022-09-01 23:59:59")
    private String operationEndDateTime;

    @ExcelColumn(headerName = "교육 장소")
    @Schema(description="교육 장소", example="")
    private String educationPlace;

    @ExcelColumn(headerName = "정원")
    @Schema(description="정원", example="")
    private Integer numberOfPeople;

    @ExcelColumn(headerName = "숙박")
    @Schema(description="숙박 제공")
    private String isAccommodationString;

    @Schema(description="숙박 제공")
    private Boolean isAccommodation;

    @Schema(description="복습제공 여부")
    private Boolean isReview;

    @ExcelColumn(headerName = "복습제공")
    @Schema(description="복습제공 여부")
    private String isReviewString;

    @ExcelColumn(headerName = "복습기간(월단위)")
    @Schema(description="복습 기간(월단위)", example="")
    private Integer availableReviewTerm;

    @ExcelColumn(headerName = "복습 영상")
    @Schema(description="복습 영상 경로", example="")
    private String reviewPath;

    @Schema(description="화상 강의 진행 여부", example="")
    private Boolean isVideoLecture;

    @ExcelColumn(headerName = "화상 강의")
    @Schema(description="화상 강의 진행 여부", example="")
    private String isVideoLectureString;

    @ExcelColumn(headerName = "화상 강의 경로")
    @Schema(description="화상 강의 경로", example="")
    private String videoLecturePath;

    @ExcelColumn(headerName = "출석 QR 경로")
    @Schema(description="출석 QR 파일 경로", example="")
    private String qrPath;


    @ExcelColumn(headerName = "등록일시")
    @Schema(description="등록 일시", example="")
    private String createDateTime;


    @ExcelColumn(headerName = "수정일시")
    @Schema(description="수정일시", example="")
    private String updateDateTime;

}
