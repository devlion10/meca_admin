package kr.or.kpf.lms.repository.entity.homepage;

import kr.or.kpf.lms.common.converter.BooleanConverter;
import kr.or.kpf.lms.common.converter.DateYMDToStringConverter;
import kr.or.kpf.lms.common.support.CSEntitySupport;
import kr.or.kpf.lms.repository.entity.FileMaster;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

/**
 * LMS DATA (자료) 테이블 Entity
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "LMS_DATA")
@Access(value = AccessType.FIELD)
public class LmsData extends CSEntitySupport implements Serializable {

    /** 시퀀스 번호 */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="SEQ_NO", nullable = false)
    private Long sequenceNo;

    /** 팀 구분(0: 기타, 1: 미디어교육, 2: 언론인연수, 3: 미디어지원) */
    @Column(name = "TEAM_CTGR")
    private String teamCategory;

    /** 자료 구분(0: 기타 자료, 1: 교육 자료, 2: 연구/통계 자료, 3: 영상 자료, 4: 사업결과물, 5: 미디어리터러시) */
    @Column(name="MTRL_CTGR")
    private String materialCategory;

    /** 자료 파트 타입 */
    @Column(name="MTRL_TYPE", nullable=false)
    private String materialType;

    /** 제목 */
    @Column(name="TITLE", nullable=false)
    private String title;

    /** 내용 */
    @Column(name="CONTENTS", nullable=false)
    private String contents;

    /** 저자 */
    @Column(name="AUTHOR", nullable=false)
    private String author;

    /** 첨부 파일 크기 */
    @Column(name="ATCH_FILE_SIZE")
    private Long atchFileSize;

    /** 첨부 파일 경로 */
    @Column(name="ATCH_FILE_PATH")
    private String atchFilePath;

    /** 썸네일 파일 경로 */
    @Column(name="THUMB_FILE_PATH")
    private String thumbFilePath;

    /** 영상 링크 */
    @Column(name="MEDIA_FILE_PATH")
    private String mediaFilePath;

    /** 조회수 */
    @Column(name="VIEW_CNT")
    private Long viewCnt;

    /** 사용 여부 */
    @Column(name="USE_YN")
    @Convert(converter= BooleanConverter.class)
    private Boolean isUse;

    /** 파일 리스트 */
    @NotFound(action= NotFoundAction.IGNORE)
    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name="SEQ_NO", insertable=false, updatable=false)
    private List<LmsDataFile> lmsDataFiles;

}
