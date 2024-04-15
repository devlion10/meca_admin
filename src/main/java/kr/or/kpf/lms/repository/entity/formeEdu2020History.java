package kr.or.kpf.lms.repository.entity;

import kr.or.kpf.lms.common.converter.DateToStringConverter;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "FORME_EDU2020_HISTORY")
@Access(value = AccessType.FIELD)
public class formeEdu2020History implements Serializable {
    @Id
    @Column(name="CRS_RQST_SEQ")
    private int crsRqstSeq;

    @Column(name="USER_ID")
    private String userId;

    @Column(name="MDIA_CD")
    private String mdiaCd;

    @Column(name="MDIA_NM")
    private String mdiaNm;

    @Column(name="MDIA_D_CD")
    private String mdiaDCd;

    @Column(name="MDIA_D_NM")
    private String mdiaDNm;

    @Column(name="DEPT_NM")
    private String deptNm;

    @Column(name="USER_NM")
    private String userNm;

    @Column(name="GRD_NM")
    private String grdNm;

    @Column(name="DUTY_NM")
    private String dutyNm;

    @Column(name="BIRTH")
    private String birth;

    @Column(name="GENDER")
    private String gender;

    @Column(name="GENDER_NM")
    private String genderNm;

    @Column(name="USER_MOBILE")
    private String userMobile;

    @Column(name="OFFC_TEL_NO")
    private String offcTelNo;

    @Column(name="USER_EMAIL")
    private String userEmail;

    @Column(name="CRS_RQST_DT")
    private String crsRqstDt;

    @Column(name="CRS_RQST_DT2")
    private String crsRqstDt2;

    @Column(name="ACCO_APPL_YN")
    private String accoApplYn;

    @Column(name="ACCO_APPL_YN_NM")
    private String accoApplYnNm;

    @Column(name="CRS_RQST_STAT")
    private String crsRqstStat;

    @Column(name="CRS_RQST_STAT_NM")
    private String crsRqstStatNm;

    @Column(name="CRS_CONFIRM_DT")
    private String crsConfirmDt;

    @Column(name="CRS_CONFIRM_DT2")
    @Convert(converter= DateToStringConverter.class)
    private String crsConfirmDt2;

    @Column(name="CRS_CLS1")
    private String crsCls1;

    @Column(name="CRS_CLS2")
    private String crsCls2;

    @Column(name="CRS_CLS1_NM")
    private String crsCls1Nm;

    @Column(name="CRS_CLS2_NM")
    private String crsCls2Nm;

    @Column(name="CRS_NO")
    private String crsNo;

    @Column(name="CRS_NM")
    private String crsNm;

    @Column(name="CRS_FRMTO_DT")
    private String crsFrmtoDt;

    @Column(name="CRS_FRM_DT")
    private String crsFrmDt;

    @Column(name="CRS_TO_DT")
    private String crsToDt;

    @Column(name="MEM_ID")
    private String memId;

    @Column(name="CRS_DESP")
    private String crsDesp;

    @Column(name="EDU_RECPT")
    private String eduRecpt;

    @Column(name="CMPLT_DT")
    private String cmpltDt;

    @Column(name="CMPLT_DT2")
    private String cmpltDt2;

    @Column(name="CHRG_EMP_NM")
    private String chrgEmpNm;

    @Column(name="CRS_RQST_FRM_DT")
    private String crsRqstFrmDt;

    @Column(name="CRS_RQST_TO_DT")
    private String crsRqstToDt;

    @Column(name="EMAIL_AGREE_YN")
    private String emailAgreeYn;

    @Column(name="SMS_AGREE_YN")
    private String smsAgreeYn;

    @Column(name="CRS_MEM_CNT")
    private int crsMemCnt;

    @Column(name="RMK")
    private String rmk;

    @Column(name="CRS_RQST_STATE_TEMP")
    private String crsRqstStateTemp;

    @Column(name="CRS_RQST_CNT")
    private int crsRqstCnt;

    @Column(name="INS_DT")
    @Convert(converter= DateToStringConverter.class)
    private String insDt;
}
