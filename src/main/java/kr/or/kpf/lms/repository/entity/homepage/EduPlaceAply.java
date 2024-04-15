package kr.or.kpf.lms.repository.entity.homepage;

import kr.or.kpf.lms.common.support.CSEntitySupport;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Edu Place Aply (교육장 신청 이력) 테이블 Entity
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "EDU_PLACE_APLY")
@Access(value = AccessType.FIELD)
public class EduPlaceAply extends CSEntitySupport implements Serializable {

    /** 시퀀스 번호 */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="SEQ_NO", nullable = false)
    private Long sequenceNo;

    /** 제목 */
    @Column(name="TITLE", nullable=false)
    private String title;

    /** 내용 */
    @Column(name="CONTENTS", nullable=false)
    private String contents;

    /** 썸네일 파일 경로 */
    @Column(name="ATCH_FILE_PATH")
    private String atchFilePath;

    /** 신청자 */
    @Column(name="APLY_USER_NM")
    private String aplyUserNm;

    /** 신청자 연락처 */
    @Column(name="APLY_PHONE")
    private String aplyPhone;

    /** 신청자 이메일 주소 */
    @Column(name="APLY_EML_ADDR")
    private String aplyEmlAddr;

}
