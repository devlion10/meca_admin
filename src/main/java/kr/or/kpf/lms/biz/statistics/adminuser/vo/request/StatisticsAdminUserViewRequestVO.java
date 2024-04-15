package kr.or.kpf.lms.biz.statistics.adminuser.vo.request;

import io.swagger.annotations.ApiModelProperty;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import lombok.*;

/**
 * 통계 관리 > 관리자 접속 이력 관련 요청 객체
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsAdminUserViewRequestVO extends CSViewVOSupport {
    /** 검색어 범위 */
    @ApiModelProperty(name="containTextType", value="검색어 범위", required=false, dataType="string", position=10, example = "")
    private String containTextType;
    /** 검색에 포함할 단어 */
    @ApiModelProperty(name="containText", value="검색에 포함할 단어", required=false, dataType="string", position=20, example = "")
    private String containText;
}
