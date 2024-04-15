package kr.or.kpf.lms.biz.business.instructor.identify.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="BizInstructorAmtTimeVO", description="정산금액 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizInstructorAmtTimeVO {
    
    @Schema(description="강의확인서 금액", required = false, example="1")
    private String bizInstrIdntyAmt;

    @Schema(description="강의확인서 시간", required = false, example="1")
    private String bizInstrIdntyTime;

}
