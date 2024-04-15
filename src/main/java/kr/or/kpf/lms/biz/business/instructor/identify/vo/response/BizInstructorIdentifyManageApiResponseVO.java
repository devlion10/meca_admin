package kr.or.kpf.lms.biz.business.instructor.identify.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.repository.entity.BizInstructorIdentify;
import kr.or.kpf.lms.repository.entity.BizOrganizationAply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.List;

/**
 * 강의확인서 관련 응답 객체
 */
@Schema(name="BizInstructorIdentifyManageApiResponseVO", description="강의확인서 API 관련 응답 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BizInstructorIdentifyManageApiResponseVO extends CSResponseVOSupport {

    /** 강사 모집 공고 신청 정보 일련 번호 */
    private String bizInstrAplyNo;

    /** 강사 모집 공고 일련 번호 */
    private String bizInstrNo;

    /** 사업 공고 신청 일련 번호 */
    private String bizOrgAplyNo;

    /** 강사 신청자 명 */
    private String bizInstrAplyInstrNm;

    /** 강사 신청자 아이디 */
    private String bizInstrAplyInstrId;

    /** 강사 모집 공고 신청 상태 */
    private Integer bizInstrAplyStts;

    private String phone;

    @Transient
    private BizOrganizationAply bizOrganizationAply;

    @Transient
    private List<BizInstructorIdentify> bizInstructorIdentifies;
}