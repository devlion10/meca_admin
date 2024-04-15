package kr.or.kpf.lms.biz.education.question.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamQuestionItemVO {

    /** 시험 문제 일련 번호 */
    private String questionSerialNo;

    /** 시험 문제 문항 일련 번호 */
    private String questionItemSerialNo;

    /** 시험 문제 문항 순서 */
    private Integer questionSortNo;

    /** 시험 문제 문항 */
    private String questionItemValue;

    /** 정답 여부 */
    private Boolean isCorrectAnswer;
}
