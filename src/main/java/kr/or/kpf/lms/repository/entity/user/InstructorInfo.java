package kr.or.kpf.lms.repository.entity.user;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Table(name = "INSTRUCTOR_INFO")
@Access(value = AccessType.FIELD)
public class InstructorInfo extends CSEntitySupport implements Serializable {

    /** 강사 정보 일련 번호 */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "INSTR_SN", nullable = false)
    private Long instrSerialNo;

    /** 웹 회원 시리얼넘버 */
    @Column(name = "USER_SN")
    private String userSN;

    /** 웹 회원 아이디 */
    @Column(name = "USER_ID")
    private String userId;

    /** 강사 구분 */
    @Column(name = "INSTR_CTGR", nullable = false)
    private String instrCtgr;

    /** 강사 명 */
    @Column(name = "INSTR_NM")
    private String instrNm;

    /** 강사 생년월일 */
    @Convert(converter= DateYMDToStringConverter.class)
    @Column(name = "INSTR_BRDT")
    private String instrBrdt;

    /** 성별 코드 */
    @Column(name = "GNDR_CD")
    private String gender;

    /** 강사 연락처(핸드폰 번호) */
    @Column(name = "INSTR_TEL")
    private String instrTel;

    /** 강사 이메일 */
    @Column(name = "INSTR_EML")
    private String instrEml;

    /** 강사 상태 (0: 미사용, 1: 사용) */
    @Column(name = "INSTR_STTS")
    private Integer instrStts;

    /** 강사 사진 파일 주소 */
    @Column(name = "INSTR_PCTR")
    private String instrPctr;

    /** 강사 사진 파일 크기 */
    @Column(name = "INSTR_PCTR_SIZE")
    private Long instrPctrSize;

    /** 강사 서명/도장 파일 주소 */
    @Column(name = "INSTR_SGNTR")
    private String instrSgntr;

    /** 강사 서명/도장 파일 크기 */
    @Column(name = "INSTR_SGNTR_SIZE")
    private Long instrSgntrSize;

    /** 강사 자택 우편번호 */
    @Column(name = "INSTR_ZIP_CD")
    private String instrZipCd;

    /** 강사 자택 주소1 */
    @Column(name = "INSTR_ADDR1")
    private String instrAddr1;

    /** 강사 자택 주소2 */
    @Column(name = "INSTR_ADDR2")
    private String instrAddr2;

    /** 강사 강의가능 지역1(0: 선택 없음) */
    @Column(name = "INSTR_LCT_RGN1")
    private String instrLctRgn1;

    /** 강사 강의가능 지역2(0: 선택 없음) */
    @Column(name = "INSTR_LCT_RGN2")
    private String instrLctRgn2;

    /** 강사 최종학력 연도 */
    @Column(name = "INSTR_ACBG_YR")
    private Integer instrAcbgYr;

    /** 강사 최종학력 학교명 */
    @Column(name = "INSTR_ACBG_SCHL_NM")
    private String instrAcbgSchlNm;

    /** 강사 최종학력 전공 */
    @Column(name = "INSTR_ACBG_MJR")
    private String instrAcbgMjr;

    /** 강사 최종학력 학위 */
    @Column(name = "INSTR_ACBG_DGR")
    private String instrAcbgDgr;

    /** 강사 강의 주요 내용 */
    @Column(name = "INSTR_MAIN_CN")
    private String instrMainCn;

    /** 강사 강의분야 대분류 */
    @Column(name = "INSTR_RELM_FRST")
    private String instrRelmFrst;

    /** 강사 강의분야 소분류 */
    @Column(name = "INSTR_RELM_LAST")
    private String instrRelmLast;

    /** 강사 은행 */
    @Column(name = "INSTR_BANK")
    private String instrBank;

    /** 강사 계좌번호 */
    @Column(name = "INSTR_ACTNO")
    private String instrActno;

    /** 강사 소속명 */
    @Column(name = "ORG_NAME", nullable = true)
    private String orgName;

    /** 강사 부서/직급 */
    @Column(name = "DEPARTMENT", nullable = true)
    private String department;

    /** 언론인교육센터 강사일련번호 */
    @Column(name = "LECTR_NO")
    private Integer lectrNo;

    /** 미디어강사 정보 */
    @NotFound(action= NotFoundAction.IGNORE)
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="USER_ID", referencedColumnName = "LGN_ID", insertable=false, updatable=false)
    private LmsUser lmsUser;

    /** 강사 주요 이력 */
    @NotFound(action= NotFoundAction.IGNORE)
    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="INSTR_SN", insertable=false, updatable=false)
    private List<InstructorHist> instructorHists = new ArrayList<>();

    /** 강사 자격증 */
    @NotFound(action= NotFoundAction.IGNORE)
    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="INSTR_SN", insertable=false, updatable=false)
    private List<InstructorQlfc> instructorQlfcs = new ArrayList<>();

    @Transient
    private List<BizInstructorAply> bizInstructorAplies = new ArrayList<>();
}