package kr.or.kpf.lms.biz.contents.chapter.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.converter.BooleanConverter;
import lombok.Data;

@Data
public class ChapterUploadExcelVO {

	@Schema(description="콘텐츠 코드")
	private String contentsCode;

	@Schema(description="장(챕터) 번호")
	private String chapterName;

	@Schema(description="장(챕터) 제목")
	private String chapterTitle;

	@Schema(description="장(챕터) 정렬 번호")
	private String chapterSortNo;

	@Schema(description="절 제목")
	private String sectionName;

	@Schema(description="절 정렬 번호")
	private String sectionSortNo;

	@Schema(description="파일 주소")
	private String link;

}
