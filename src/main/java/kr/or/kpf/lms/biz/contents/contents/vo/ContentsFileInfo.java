package kr.or.kpf.lms.biz.contents.contents.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContentsFileInfo {
    /** 파일 명 */
    private String fileName;
    /** 파일 사이즈 */
    private String fileSize;
    /** 업데이트 일시 */
    private String updateDateTime;
}
