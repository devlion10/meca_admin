package kr.or.kpf.lms.biz.statistics.education.service;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.or.kpf.lms.biz.education.schedule.vo.request.ScheduleViewRequestVO;
import kr.or.kpf.lms.biz.statistics.education.vo.request.EducationStateRequestVO;
import kr.or.kpf.lms.biz.statistics.education.vo.response.EducationStateResponseVO;
import kr.or.kpf.lms.biz.statistics.privacy.vo.request.PrivacyRequestVO;
import kr.or.kpf.lms.biz.statistics.privacy.vo.response.PrivacyResponseVO;
import kr.or.kpf.lms.common.exceldownload.ExcelColumn;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.common.support.Code;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonEducationRepository;
import kr.or.kpf.lms.repository.CommonStatisticsRepository;
import kr.or.kpf.lms.repository.education.EducationPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 통계 관리 > 학습 운영 통계 관련 Service
 */
@Service
@RequiredArgsConstructor
public class EducationStateService extends CSServiceSupport {

    /** 학습 운영 통계 공통 */
    private final CommonStatisticsRepository commonStatisticsRepository;

    /**
     * 학습 운영 통계 조회
     *
     * @param requestObject
     * @return
     */
    public EducationStateResponseVO getData(EducationStateRequestVO requestObject) {
        Long eduType = commonStatisticsRepository.excelDownload(requestObject).stream().count();
        requestObject.setEducationType(Code.EDU_TYPE.VIDEO.enumCode);
        Long eduTypeVideo = commonStatisticsRepository.excelDownload(requestObject).stream().count();
        requestObject.setEducationType(Code.EDU_TYPE.CONVOCATION.enumCode);
        Long eduTypeOffline = commonStatisticsRepository.excelDownload(requestObject).stream().count();
        requestObject.setEducationType(Code.EDU_TYPE.E_LEARNING.enumCode);
        Long eduTypeElearning = commonStatisticsRepository.excelDownload(requestObject).stream().count();
        requestObject.setEducationType(null);

        Long eduCategory = commonStatisticsRepository.excelDownload(requestObject).stream().count();
        requestObject.setCategoryCode(Code.CTS_CTGR.JOURNAL.enumCode);
        Long eduCategory1 = commonStatisticsRepository.excelDownload(requestObject).stream().count();
        requestObject.setCategoryCode(Code.CTS_CTGR.MEDIA.enumCode);
        Long eduCategory2 = commonStatisticsRepository.excelDownload(requestObject).stream().count();
        requestObject.setCategoryCode(Code.CTS_CTGR.ETC.enumCode);
        Long eduCategory3 = commonStatisticsRepository.excelDownload(requestObject).stream().count();
        requestObject.setCategoryCode(null);

        Long eduExpense = commonStatisticsRepository.excelDownload(requestObject).stream().count();
        requestObject.setEnforcementType("0");
        Long eduExpense0 = commonStatisticsRepository.excelDownload(requestObject).stream().count();
        requestObject.setEnforcementType("1");
        Long eduExpense1 = commonStatisticsRepository.excelDownload(requestObject).stream().count();
        requestObject.setEnforcementType("2");
        Long eduExpense2 = commonStatisticsRepository.excelDownload(requestObject).stream().count();

        return EducationStateResponseVO.builder()
                .eduType(eduType)
                .eduTypeVideo(eduTypeVideo)
                .eduTypeOffline(eduTypeOffline)
                .eduTypeElearning(eduTypeElearning)
                .eduCategory(eduCategory)
                .eduCategory1(eduCategory1)
                .eduCategory2(eduCategory2)
                .eduCategory3(eduCategory3)
                .eduExpense(eduExpense)
                .eduExpense0(eduExpense0)
                .eduExpense1(eduExpense1)
                .eduExpense2(eduExpense2)
                .build();
    }
}
