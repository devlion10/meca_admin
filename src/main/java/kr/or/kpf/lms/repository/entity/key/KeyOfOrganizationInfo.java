package kr.or.kpf.lms.repository.entity.key;

import lombok.*;

import javax.persistence.Column;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KeyOfOrganizationInfo implements Serializable {

    /** 소속 기관 코드 */
    @Column(name = "ORG_CD", nullable = false)
    private String organizationCode;

    /** 소속 기관 사업자 등록번호(나이스 번호) */
    @Column(name="BIZ_LICENSE_NO")
    private String bizLicenseNumber;
}
