package kr.or.kpf.lms.common.support;

import kr.or.kpf.lms.common.converter.DateToStringConverter;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.config.security.vo.KoreaPressFoundationUserDetails;
import kr.or.kpf.lms.config.security.vo.RoleGroup;
import kr.or.kpf.lms.framework.exception.KPFException;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.thymeleaf.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
@Getter
public class CSEntitySupport {

    /** 등록 일시 */
    @CreatedDate
    @Column(name = "REG_DT", updatable=false, nullable=false)
    @Convert(converter= DateToStringConverter.class)
    private String createDateTime;

    /** 등록자 유저 ID */
    @Column(name = "RGTR_LGN_ID")
    private String registUserId;

    /** 수정 일시 */
    @LastModifiedDate
    @Column(name = "MDFCN_DT")
    @Convert(converter=DateToStringConverter.class)
    private String updateDateTime;

    /** 수정자 유저 ID */
    @Column(name = "MDFR_LGN_ID")
    private String modifyUserId;

    @PrePersist @PreUpdate
    public void preInsertAndUpdate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof KoreaPressFoundationUserDetails) {
            this.modifyUserId = ((KoreaPressFoundationUserDetails) authentication.getPrincipal()).getUserId();
            if(StringUtils.isEmpty(this.registUserId))
                this.registUserId = ((KoreaPressFoundationUserDetails) authentication.getPrincipal()).getUserId();
        } else {
            if (StringUtils.isEmpty(this.registUserId))
                this.registUserId = RoleGroup.ANONYMOUS.name();
        }
    }
}
