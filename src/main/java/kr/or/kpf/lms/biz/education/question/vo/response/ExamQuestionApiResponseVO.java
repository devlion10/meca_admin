package kr.or.kpf.lms.biz.education.question.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Schema(name="ExamQuestionApiResponseVO", description="시험 문제 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamQuestionApiResponseVO extends CSResponseVOSupport {

	@Schema(description="시험 문제 일련 번호", example="1")
	private String questionSerialNo;

	@Schema(description="문제 유형 코드", example="1")
	private String questionType;

	@Schema(description="문제 제목", example="어제의 날씨는 어땠나요?")
	private String questionTitle;

	@Schema(description="등급 번호", example="1")
	private Integer grade;

	@Schema(description="정답", example="맑음")
	private String correctAnswer;

	@Schema(description="사용 여부", example="1")
	@Builder.Default
	private Boolean isUsable = false;

	@Schema(description="문제 내용", example="")
	private String questionContents;

	@Schema(description="문제 파일 일련 번호", example="")
	private Integer questionFile;

	@Schema(description="정답 설명", example="")
	private String correctAnswerDescription;

	@Schema(description="시험 문제 문항", example="")
	List<String> questionItemList = new ArrayList<>();
}
