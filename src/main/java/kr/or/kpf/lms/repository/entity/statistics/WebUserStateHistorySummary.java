package kr.or.kpf.lms.repository.entity.statistics;

import kr.or.kpf.lms.common.converter.DateToStringConverter;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * LMS 회원 상태 내역 집계 테이블 Entity
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "LMS_USER_HISTORY_SUMMARY")
@Access(value = AccessType.FIELD)
public class WebUserStateHistorySummary implements Serializable {

    /** 집계일자(YYYYMMDD) */
    @Id
    @Column(name="SUM_DATE")
    @ExcelColumn(headerName = "기준일")
    private String summaryDate;

    /** 가입 회원 수 */
    @Column(name="JOIN_CNT")
    @ExcelColumn(headerName = "회원가입자수")
    private Long joinCount;

    /** 탈퇴 회원 수 */
    @Column(name="WDL_CNT")
    @ExcelColumn(headerName = "회원탈퇴자수")
    private Long withDrawalCount;

    /** 접속 회원 수 */
    @Column(name="LOGIN_CNT")
    @ExcelColumn(headerName = "접속자수")
    private Long loginCount;

    /** 휴면 전환 회원 수 */
    @Column(name="DMC_CNT")
    @ExcelColumn(headerName = "휴면전환수")
    private Long dormancyCount;

    /** 등록 일시 */
    @CreatedDate
    @Column(name = "REG_DT", updatable=false, nullable=false)
    @Convert(converter= DateToStringConverter.class)
    private String createDateTime;

    /** 등록자 유저 ID */
    @Column(name = "RGTR_LGN_ID")
    @Builder.Default
    private String registUserId = "SYSTEM";

    /** 수정 일시 */
    @LastModifiedDate
    @Column(name = "MDFCN_DT")
    @Convert(converter=DateToStringConverter.class)
    private String updateDateTime;

    /** 수정자 유저 ID */
    @Column(name = "MDFR_LGN_ID")
    @Builder.Default
    private String modifyUserId = "SYSTEM";
}
