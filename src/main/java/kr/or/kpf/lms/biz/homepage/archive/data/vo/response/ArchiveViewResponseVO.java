package kr.or.kpf.lms.biz.homepage.archive.data.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.homepage.archive.data.vo.PublicationFolderInfo;
import kr.or.kpf.lms.common.converter.BooleanConverter;
import kr.or.kpf.lms.repository.entity.homepage.LmsDataFile;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.annotation.Transient;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;

@Schema(name="ArchiveViewResponseVO", description="자료실 관련 응답 객체")
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveViewResponseVO {
    
    @Schema(description="시퀀스 번호")
    private Long sequenceNo;
    
    @Schema(description="팀 구분(0: 기타, 1: 미디어교육, 2: 언론인연수, 3: 미디어지원)")
    private String teamCategory;

    @Schema(description="자료 구분(0: 기타 자료, 1: 교육 자료, 2: 연구/통계 자료, 3: 영상 자료, 4: 사업결과물, 5: 미디어리터러시)")
    private String materialCategory;

    @Schema(description="자료 파트 타입")
    private String materialType;

    @Schema(description="제목")
    private String title;

    @Schema(description="내용")
    private String contents;

    @Schema(description="AUTHOR")
    private String author;

    @Schema(description="썸네일 파일 경로")
    private String thumbFilePath;

    @Schema(description="조회수")
    private Long viewCnt;

    @Schema(description="사용 여부")
    @Convert(converter= BooleanConverter.class)
    private Boolean isUse;

    /** 하위 폴더 */
    @Transient
    private List<PublicationFolderInfo> publicationFolderInfos;

}
