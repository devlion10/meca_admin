package kr.or.kpf.lms.repository;

import kr.or.kpf.lms.biz.system.code.vo.request.CommonCodeViewRequestVO;
import kr.or.kpf.lms.common.support.CSViewVOSupport;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.entity.system.CommonCodeMaster;

import java.util.List;

/**
 * 시스템 관리 공통 Repository
 */
public interface CommonSystemRepository {

    /**
     * 공통 엑셀 다운로드
     *
     * @param requestObject
     * @return
     */
    <T extends CSViewVOSupport> List<?> excelDownload(T requestObject);

    /** 시스템 관리 > 공통 코드 관리 목록 조회 */
    List<CommonCodeMaster> findTopCommonCode(CommonCodeViewRequestVO requestObject);

    /**
     * 일련 번호 생성
     *
     * @param prefixCode
     * @return
     */
    String generateCode(String prefixCode);

    /**
     * 자동 순서 생성
     *
     * @param upIndividualCode
     * @return
     */
    Integer generateOrderAutoIncrease(String upIndividualCode);
}