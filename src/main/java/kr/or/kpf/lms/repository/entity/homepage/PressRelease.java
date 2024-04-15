package kr.or.kpf.lms.repository.entity.homepage;

import kr.or.kpf.lms.common.support.CSEntitySupport;
import kr.or.kpf.lms.repository.entity.user.LmsUser;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Press Release (행사소개) 테이블 Entity
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "PRESS_RELEASE")
@Access(value = AccessType.FIELD)
public class PressRelease extends CSEntitySupport implements Serializable {

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

    /** 첨부 파일 경로 */
    @Column(name="ATCH_FILE_PATH")
    private String atchFilePath;

    /** 첨부 파일 크기 */
    @Column(name="ATCH_FILE_SIZE")
    private Long atchFileSize;

    /** 첨부 파일 원본명 */
    @Column(name="ATCH_FILE_ORGN")
    private String atchFileOrigin;

    /** 조회수 */
    @Column(name="VIEW_CNT", nullable=false)
    private Long viewCount;

    /** 회원 */
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="RGTR_LGN_ID", referencedColumnName = "LGN_ID", insertable=false, updatable=false)
    private LmsUser lmsUser;

}
