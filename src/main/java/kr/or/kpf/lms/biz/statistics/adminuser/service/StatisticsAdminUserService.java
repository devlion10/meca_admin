package kr.or.kpf.lms.biz.statistics.adminuser.service;

import kr.or.kpf.lms.biz.statistics.adminuser.vo.StatisticsAdminUserExcelVO;
import kr.or.kpf.lms.biz.statistics.adminuser.vo.request.StatisticsAdminUserViewRequestVO;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonStatisticsRepository;
import kr.or.kpf.lms.repository.entity.statistics.AdminAccessHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 통계 관리 > 관리자 접속 이력 관련 Service
 */
@Service
@RequiredArgsConstructor
public class StatisticsAdminUserService extends CSServiceSupport {

    private final CommonStatisticsRepository commonStatisticsRepository;

    /**
     * 통계 관리 > 관리자 접속 이력 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(StatisticsAdminUserViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonStatisticsRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 통계 관리 > 관리자 접속 이력 엑셀 다운로드
     *
     * @param requestObject
     * @return
     */
    public List<StatisticsAdminUserExcelVO> getExcel(StatisticsAdminUserViewRequestVO requestObject) {
        return (List<StatisticsAdminUserExcelVO>) commonStatisticsRepository.excelDownload(requestObject);
    }
}
