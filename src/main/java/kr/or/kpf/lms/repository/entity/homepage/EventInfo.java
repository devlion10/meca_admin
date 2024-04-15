package kr.or.kpf.lms.repository.entity.homepage;

import kr.or.kpf.lms.common.converter.DateToStringConverter;
import kr.or.kpf.lms.common.converter.DateYMDToStringConverter;
import kr.or.kpf.lms.common.support.CSEntitySupport;
import kr.or.kpf.lms.repository.entity.user.AdminUser;
import kr.or.kpf.lms.repository.entity.user.LmsUser;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * Event Info (이벤트/설문) 테이블 Entity
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "EVENT_INFO")
@Access(value = AccessType.FIELD)
public class EventInfo extends CSEntitySupport implements Serializable {

    /** 시퀀스 번호 */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="SEQ_NO", nullable = false)
    private Long sequenceNo;

    /** 타입(1: 이벤트, 2: 설문) */
    @Column(name="TYPE", nullable=false)
    private String type;

    /** 제목 */
    @Column(name="TITLE", nullable=false)
    private String title;

    /** 내용 */
    @Column(name="CONTENTS", nullable=false)
    private String contents;

    /** 썸네일 파일 경로 */
    @Column(name="THUMB_FILE_PATH", nullable=false)
    private String thumbFilePath;

    /** 시작 일시 */
    @Column(name="START_DT")
    @Convert(converter= DateYMDToStringConverter.class)
    private String startDt;

    /** 종료 일시 */
    @Column(name="END_DT")
    @Convert(converter=DateYMDToStringConverter.class)
    private String endDt;

    /** 상태 (0:종료, 1:진행) */
    @Column(name="STATUS", nullable=false)
    private int status;

    /** 게시글 등록자(관리자) */
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="RGTR_LGN_ID", referencedColumnName = "LGN_ID", insertable=false, updatable=false)
    private AdminUser adminUser;
}
