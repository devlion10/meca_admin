package kr.or.kpf.lms.biz.statistics.privacy.service;

import kr.or.kpf.lms.biz.statistics.privacy.vo.request.PrivacyRequestVO;
import kr.or.kpf.lms.biz.statistics.privacy.vo.response.PrivacyResponseVO;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 통계 관리 > 개인정보 수정 이력 통계 관련 Service
 */
@Service
@RequiredArgsConstructor
public class PrivacyService extends CSServiceSupport {

    /** 콘텐츠 관리 공통 */
    private final CommonStatisticsRepository commonStatisticsRepository;

    /**
     * 개인정보 수정 이력 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(PrivacyRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonStatisticsRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 개인정보 수정 이력 엑셀 다운로드
     *
     * @param requestObject
     * @return
     */
    public List<PrivacyResponseVO> getExcel(PrivacyRequestVO requestObject) {
        return (List<PrivacyResponseVO>) commonStatisticsRepository.excelDownload(requestObject);
    }
}
