package kr.or.kpf.lms.biz.statistics.webuser.service;

import kr.or.kpf.lms.biz.statistics.adminuser.vo.StatisticsAdminUserExcelVO;
import kr.or.kpf.lms.biz.statistics.adminuser.vo.request.StatisticsAdminUserViewRequestVO;
import kr.or.kpf.lms.biz.statistics.webuser.vo.request.UserStateRequestVO;
import kr.or.kpf.lms.biz.statistics.webuser.vo.response.UserStateResponseVO;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonStatisticsRepository;
import kr.or.kpf.lms.repository.entity.statistics.WebUserStateHistorySummary;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 통계 관리 > 이용 통계 관리 관련 Service
 */
@Service
@RequiredArgsConstructor
public class UserStateService extends CSServiceSupport {

    /** 통계관리 공통 */
    private final CommonStatisticsRepository commonStatisticsRepository;

    /**
     * 이용 통계 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(UserStateRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonStatisticsRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 통계 관리 > 이용 통계 엑셀 다운로드
     *
     * @param requestObject
     * @return
     */
    public List<WebUserStateHistorySummary> getExcel(UserStateRequestVO requestObject) {
        return (List<WebUserStateHistorySummary>) commonStatisticsRepository.excelDownload(requestObject);
    }
}
