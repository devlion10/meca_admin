package kr.or.kpf.lms.biz.education.reference.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * 자료실 관련 요청 객체
 */
@Schema(name="ReferenceRoomViewRequestVO", description="자료실 관련 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ReferenceRoomViewRequestVO  extends CSViewVOSupport {
    /** 검색어 범위 */
    private String containTextType;
    /** 검색에 포함할 단어 */
    private String containText;

    /** 교육과정 코드 */
    private String curriculumCode;
    /** 자료실 시퀀스 번호 */
    private BigInteger sequenceNo;
    /** 자료실 제목 */
    private String title;
    /** 관리자 명 */
    private String userName;
}
