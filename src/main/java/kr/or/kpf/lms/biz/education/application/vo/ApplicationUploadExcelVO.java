package kr.or.kpf.lms.biz.education.application.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ApplicationUploadExcelVO {

	@Schema(description="회원 ID", example="")
	private String userId;
}
