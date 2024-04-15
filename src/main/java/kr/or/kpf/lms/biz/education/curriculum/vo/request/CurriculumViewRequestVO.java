package kr.or.kpf.lms.biz.education.curriculum.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 교육 과정 관련 요청 객체
 */
@Schema(name="CurriculumApiRequestVO", description="교육 과정 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CurriculumViewRequestVO extends CSViewVOSupport {

    /** 교육 유형 */
    private String educationType;

    /** 교육 카테고리 */
    private String categoryCode;

    /** 교육 대분류 */
    private String curriculumAreaCode;

    /** 교육 소분류 */
    private String curriculumAreaCodeDetail;

    /** 사용 여부 */
    private Boolean isUsable;

    /** 교육 과정 명 */
    private String curriculumName;

    /** 교육 과정 코드 */
    private String curriculumCode;
}
