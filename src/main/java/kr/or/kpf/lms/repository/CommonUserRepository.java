package kr.or.kpf.lms.repository;

import kr.or.kpf.lms.common.support.CSViewVOSupport;
import kr.or.kpf.lms.framework.model.CSPageImpl;

import java.util.List;

/**
 * 유저 공통 Repository
 */
public interface CommonUserRepository {

    /**
     * 공통 리스트 조회
     *
     * @param requestObject
     * @return
     */
    <T extends CSViewVOSupport> CSPageImpl<?> findEntityList(T requestObject);
    <T extends CSViewVOSupport> List<?> findEntityListExcel(T requestObject);
    /**
     * 기관 일련 번호 생성
     */
    String generateOrganizationCode(String prefixCode);
    String generateMediaCode(String prefixCode);
}