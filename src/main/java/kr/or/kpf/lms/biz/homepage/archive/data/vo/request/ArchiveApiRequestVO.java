package kr.or.kpf.lms.biz.homepage.archive.data.vo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.homepage.archive.data.vo.CreateArchive;
import kr.or.kpf.lms.biz.homepage.archive.data.vo.UpdateArchive;
import kr.or.kpf.lms.common.converter.BooleanConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.validation.constraints.NotEmpty;
import java.math.BigInteger;

@Schema(name="ArchiveApiRequestVO", description="자료실")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArchiveApiRequestVO {

    /** 고유키 */
    @Schema(description="고유키", required=true, example="")
    private Long sequenceNo;

    /** 팀 구분(0: 기타, 1: 미디어교육, 2: 언론인연수, 3: 미디어지원) */
    @Schema(description="팀 구분(0: 기타, 1: 미디어교육, 2: 언론인연수, 3: 미디어지원)", required=true, example="")
    @NotEmpty(groups={CreateArchive.class, UpdateArchive.class}, message="팀 구분은 필수 입니다.")
    private String teamCategory;

    /** 자료 구분(1: 교육 자료, 2: 연구/통계 자료, 3: 영상 자료) */
    @Schema(description="자료 구분(1: 교육 자료, 2: 연구/통계 자료, 3: 영상 자료)", required=true, example="")
    @NotEmpty(groups={CreateArchive.class, UpdateArchive.class}, message="자료 구분은 필수 입니다.")
    private String materialCategory;

    /** 자료 유형 */
    @Schema(description="자료 유형", required=true, example="")
    @NotEmpty(groups={CreateArchive.class, UpdateArchive.class}, message="자료 유형은 필수 입니다.")
    private String materialType;

    /** 제목 */
    @Schema(description="제목", required=true, example="")
    @NotEmpty(groups={CreateArchive.class, UpdateArchive.class}, message="제목은 필수 입니다.")
    private String title;

    /** 내용 */
    @Schema(description="내용", required=true, example="")
    @NotEmpty(groups={CreateArchive.class, UpdateArchive.class}, message="내용은 필수 입니다.")
    private String contents;

    /** 저자 */
    @Schema(description="저자", required=false, example="")
    @NotEmpty(groups={CreateArchive.class, UpdateArchive.class}, message="저자은 필수 입니다.")
    private String author;

    /** 첨부 파일 크기 */
    @Schema(description="첨부 파일 크기", required=false, example="")
    private Long atchFileSize;

    /** 첨부 파일 경로 */
    @Schema(description="첨부 파일 경로", required=false, example="")
    private String atchFilePath;

    /** 썸네일 파일 경로 */
    @Schema(description="썸네일 파일 경로", required=false, example="")
    private String thumbFilePath;

    /** 영상 링크 */
    @Schema(description="영상 링크", required=false, example="")
    private String mediaFilePath;

    /** 조회수 */
    @Schema(description="조회수", required=false, example="")
    private Long viewCnt;

    /** 사용 여부 */
    @Schema(description="사용 여부", required=false, example="")
    @Convert(converter= BooleanConverter.class)
    private Boolean isUse;

}
