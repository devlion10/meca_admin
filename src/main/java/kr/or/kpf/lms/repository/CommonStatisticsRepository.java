package kr.or.kpf.lms.repository;

import kr.or.kpf.lms.common.support.CSViewVOSupport;
import kr.or.kpf.lms.framework.model.CSPageImpl;

import java.util.List;

/**
 * 통계 관리 공통 Repository
 */
public interface CommonStatisticsRepository {

    /**
     * 공통 리스트 조회
     *
     * @param requestObject
     * @return
     */
    <T extends CSViewVOSupport> CSPageImpl<?> findEntityList(T requestObject);

    /**
     * 공통 엑셀 다운로드
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    <T extends CSViewVOSupport> List<?> excelDownload(T requestObject);
}
