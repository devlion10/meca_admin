package kr.or.kpf.lms.biz.homepage.page.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.converter.DateToStringConverter;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Id;
import java.math.BigInteger;

/**
 * 페이지 View 관련 요청 객체
 */
@Schema(name="PageViewRequestVO", description="페이지 View 관련 요청 객체")
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class PageViewRequestVO extends CSViewVOSupport {
    /** 일련 번호 */
    @Schema(description="일련 번호")
    private BigInteger sequenceNo;

    /** 문서(개인정보 , 이용 약관 및 기타) 유형 (0: 일반, 1: 이용 약관, 2: 개인정보 처리 방침, 3: 개인정보 동의서) */
    @Schema(description="문서(개인정보 , 이용 약관 및 기타) 유형 (0: 일반, 1: 이용 약관, 2: 개인정보 처리 방침, 3: 개인정보 동의서)")
    private String documentType;

    /** 등록 일시 */
    @Schema(description="등록 일시")
    @Convert(converter= DateToStringConverter.class)
    private String createDateTime;
}
