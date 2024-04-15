package kr.or.kpf.lms.biz.statistics.report.service;

import kr.or.kpf.lms.biz.statistics.adminuser.vo.StatisticsAdminUserExcelVO;
import kr.or.kpf.lms.biz.statistics.adminuser.vo.request.StatisticsAdminUserViewRequestVO;
import kr.or.kpf.lms.biz.statistics.report.vo.request.ReportEducationRequestVO;
import kr.or.kpf.lms.biz.statistics.report.vo.request.ReportScheduleRequestVO;
import kr.or.kpf.lms.biz.statistics.report.vo.request.ReportUserRequestVO;
import kr.or.kpf.lms.biz.statistics.report.vo.response.ReportEducationResponseVO;
import kr.or.kpf.lms.biz.statistics.report.vo.response.ReportScheduleResponseVO;
import kr.or.kpf.lms.biz.statistics.report.vo.response.ReportUserResponseVO;
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
 * 통계 관리 > 경영평가 보고서 관련 Service
 */
@Service
@RequiredArgsConstructor
public class ReportService extends CSServiceSupport {

    private final CommonStatisticsRepository commonStatisticsRepository;

    /**
     * 통계 관리 > 경영평가 보고서 엑셀 다운로드
     *
     * @param requestObject
     * @return
     */
    /** 교육별 */
    public List<ReportEducationResponseVO> getEducation(ReportEducationRequestVO requestObject) {
        return (List<ReportEducationResponseVO>) commonStatisticsRepository.excelDownload(requestObject);
    }

    /** 교육 일정별 */
    public List<ReportScheduleResponseVO> getSchedule(ReportScheduleRequestVO requestObject) {
        return (List<ReportScheduleResponseVO>) commonStatisticsRepository.excelDownload(requestObject);
    }

    /** 신청자별 */
    public List<ReportUserResponseVO> getUser(ReportUserRequestVO requestObject) {
        return (List<ReportUserResponseVO>) commonStatisticsRepository.excelDownload(requestObject);
    }
}
