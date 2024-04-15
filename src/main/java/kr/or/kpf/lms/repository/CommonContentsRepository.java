package kr.or.kpf.lms.repository;

import kr.or.kpf.lms.common.support.CSViewVOSupport;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.List;

/**
 * 콘텐츠 관리 공통 Repository
 */
public interface CommonContentsRepository {

    /**
     * 공통 리스트 조회
     *
     * @param requestObject
     * @return
     */
    <T extends CSViewVOSupport> CSPageImpl<?> findEntityList(T requestObject);

    <T extends CSViewVOSupport> List<?> findEntityListExcel(T requestObject);
    /**
     * 콘텐츠 코드 생성
     */
    String generateContentsCode(String prefixContents);

    /**
     * 챕터 코드 생성
     */
    String generateChapterCode(String prefixChapter);

    /**
     * 강의 평가 일련 번호 생성
     */    
    String generateEvaluateSerialNo(String prefixEvaluate);

    /**
     * 강의 평가 질문 일련 번호 생성
     */
    String generateQuestionSerialNo(String prefixQuestion);

    /**
     * 강의 평가 질문 문항 일련 번호 생성
     */
    String generateQuestionItemSerialNo(String prefixItem);

    /**
     * 섹션(절) 일련 번호 생성
     */
    String generateSectionCode(String prefixSection);
}