package kr.or.kpf.lms.biz.education.schedule.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.repository.entity.education.LectureLecturer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LectureInfo {
    @Schema(description="강의 코드")
    private String lectureCode;

    @Schema(description="교육 강사 장소")
    private String lecturerPlace;

    @Schema(description="강의 명")
    private String lectureTitle;

    @Schema(description="강의 내용")
    private String lectureDetail;

    @Schema(description="교육 시작 일시")
    private String operationBeginDateTime;

    @Schema(description="교육 종료 일시")
    private String operationEndDateTime;

    @Schema(description = "강사 목록")
    private List<LectureLecturer> lectureLecturerList;
}
