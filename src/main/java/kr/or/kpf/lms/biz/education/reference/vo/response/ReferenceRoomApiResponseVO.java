package kr.or.kpf.lms.biz.education.reference.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="ReferenceRoomApiResponseVO", description="자료실 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReferenceRoomApiResponseVO extends CSResponseVOSupport {
    /** 과정 코드 */
    private String curriculumCode;
    /** 자료실 제목 */
    private String title;
    /** 자료실 내용 */
    private String contents;
    /** 파일 경로 */
    private String filePath;
    /** 파일 사이즈 */
    private Long fileSize;
}
