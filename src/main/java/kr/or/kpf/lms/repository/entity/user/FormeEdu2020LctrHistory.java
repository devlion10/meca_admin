package kr.or.kpf.lms.repository.entity.user;

import kr.or.kpf.lms.common.converter.DateYMDToStringConverter;
import kr.or.kpf.lms.common.support.CSEntitySupport;
import kr.or.kpf.lms.repository.entity.BizInstructorAply;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 강사 정보 테이블 Entity
 */
@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "FORME_EDU2020_LCTR_HISTORY")
@Access(value = AccessType.FIELD)
public class FormeEdu2020LctrHistory implements Serializable {

    /** 강사 정보 일련 번호 */
    @Id
    @Column(name = "LCTR_NO")
    private String lctrNo;

    @Column(name = "LECTR_NO")
    private Integer lectrNo;

    @Column(name = "LECTR_NM")
    private String lectrNm;

    @Column(name = "CRS_CLS1_NM")
    private String crsCls1Nm;

    @Column(name = "CRS_CLS2_NM")
    private String crsCls2Nm;

    @Column(name = "CRS_CLS1")
    private String crsCls1;

    @Column(name = "CRS_CLS2")
    private String crsCls2;

    @Column(name = "EXE_CLS_NM")
    private String exeClsNm;

    @Column(name = "CRS_YN")
    private String crsYn;

    @Column(name = "CRS_NO")
    private String crsNo;

    @Column(name = "CRS_NM")
    private String crsNm;

    @Convert(converter= DateYMDToStringConverter.class)
    @Column(name = "CRS_FRM_DT")
    private String crsFrmDt;

    @Convert(converter= DateYMDToStringConverter.class)
    @Column(name = "CRS_TO_DT")
    private String crsToDt;

    @Column(name = "CRS_FRMTO_DT")
    private String crsFrmtoDt;

    @Column(name = "CRS_LOCAL")
    private String crsLocal;

    @Column(name = "EDU_PLC")
    private String eduPlc;

    @Column(name = "EDU_RECPT")
    private String eduRecpt;

    @Column(name = "CRS_MEM_CNT")
    private Integer crsMemCnt;

    @Column(name = "CRS_RQST_FRM_DT")
    private String crsRqstFrmDt;

    @Column(name = "CRS_RQST_TO_DT")
    private String crsRqstToDt;

    @Column(name = "CRS_RQST_FRMTO_DT")
    private String crsRqstFrmtoDt;

    @Column(name = "USE_YN")
    private String useYn;

    @Convert(converter= DateYMDToStringConverter.class)
    @Column(name = "TMC_INS_DT2")
    private String tmcInsDt2;

    @Column(name = "LCTR_NM")
    private String lctrNm;

    @Convert(converter= DateYMDToStringConverter.class)
    @Column(name = "LCTR_DT")
    private String lctrDt;

    @Column(name = "LCTR_FRM_TIME")
    private String lctrFrmTime;

    @Column(name = "LCTR_TO_TIME")
    private String lctrToTime;

    @Column(name = "LCTR_TIME")
    private String lctrTime;

    @Column(name = "LCTR_FRM_TO_TIME")
    private String lctrFrmToTime;

    @Column(name = "STATE_CLS")
    private String stateCls;

    @Column(name = "LCTR_HOUR")
    private float lctrHour;

    @Column(name = "LCTR_PAY")
    private Integer lctrPay;

    @Column(name = "LCTR_GRADE")
    private Integer lctrGrade;

    @Column(name = "RMK")
    private String rmk;

    @Column(name = "EDU_PLACE")
    private String eduPlace;

    @Column(name = "CORP_NM")
    private String corpNm;

    @Column(name = "JOB_NM")
    private String jobNm;

    @Column(name = "STATE_CLS_TXT")
    private String stateClsTxt;

    @Convert(converter= DateYMDToStringConverter.class)
    @Column(name = "INS_DT")
    private String insDt;


    @Column(name = "INS_DT2")
    private String insDt2;

    @Column(name = "CHRG_EMP_NM")
    private String chrgEmpNm;

    @Column(name = "DEPT_NM")
    private String deptNm;
}