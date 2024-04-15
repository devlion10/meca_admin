package kr.or.kpf.lms.biz.business.instructor.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 강사 모집 관련 응답 객체
 */
@Schema(name="BizInstructorPbancApiResponseVO", description="강사 모집 API 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizInstructorPbancApiResponseVO extends CSResponseVOSupport {

    @Schema(description="강사 모집 공고 일련 번호", example="")
    private Long sequenceNo;

    @Schema(description="강사 모집 일련 번호", example="")
    private String bizInstrNo;

    @Schema(description="사업 일련 번호", example="")
    private String bizPbancNo;

    @Schema(description="강사 모집 기간 - 시작일", example="1")
    private String bizInstrRcptBgng;

    @Schema(description="강사 모집 기간 - 종료일", example="1")
    private String bizInstrRcptEnd;

    @Schema(description="강사 모집 상태 (0: 마감, 1: 모집, 2: 추가모집)", example="1")
    private Integer bizInstrPbancStts;

}