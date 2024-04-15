package kr.or.kpf.lms.repository;

import kr.or.kpf.lms.common.support.CSViewVOSupport;
import kr.or.kpf.lms.framework.model.CSPageImpl;

import java.util.List;

/**
 * 홈페이지 관리 공통 Repository
 */
public interface CommonHomepageRepository {

    /**
     * 공통 리스트 조회
     *  
     * @param requestObject
     * @return
     */
    <T extends CSViewVOSupport> CSPageImpl<?> findEntityList(T requestObject);

    /** Excel 조회 */
    <T extends CSViewVOSupport> List<?> findEntityListExcel(T requestObject);

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
