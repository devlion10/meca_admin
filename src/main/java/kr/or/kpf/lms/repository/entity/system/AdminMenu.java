package kr.or.kpf.lms.repository.entity.system;

import kr.or.kpf.lms.common.converter.BooleanConverter;
import kr.or.kpf.lms.common.support.CSEntitySupport;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 어드민 메뉴 정보 테이블 Entity
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ADMIN_MENU")
@Access(value = AccessType.FIELD)
public class AdminMenu extends CSEntitySupport implements Serializable {

    /** 일련 번호 */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "SEQ_NO")
    private Long sequenceNo;

    /** 상위 메뉴 일련번호 */
    @Column(name="TOP_SEQ_NO")
    private Long topSequenceNo;

    /** 메뉴 Depth */
    @Column(name="DEPTH")
    private Integer depth;

    /** 순서 */
    @Column(name="SORT")
    private Integer sort;

    /** 메뉴 URI */
    @Column(name="URI")
    private String uri;

    /** 메뉴명 */
    @Column(name="MENU_NM")
    private String menuName;

    /** 경로 및 메뉴명 */
    @Column(name="MENU_FULL_NM")
    private String menuFullName;

    /** 사용 여부 */
    @Column(name="USE_YN")
    @Convert(converter = BooleanConverter.class)
    private Boolean isUsable;

    @Transient
    private List<AdminMenu> subAdminMenu = new ArrayList<>();
}
