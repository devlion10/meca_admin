package kr.or.kpf.lms.repository;

import kr.or.kpf.lms.common.support.CSViewVOSupport;
import kr.or.kpf.lms.framework.model.CSPageImpl;

import java.util.List;

/**
 * 교육 신청 공통 Repository
 */
public interface CommonEducationRepository {
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
     */
    <T extends CSViewVOSupport> List<?> excelDownload(T requestObject);

    /**
     * 교육 과정 일련 번호 생성
     */
    String generateCurriculumCode(String prefixCode);

    /**
     * 시험 일련 번호 생성
     */
    String generateExamSerialNo(String prefixCode);

    /**
     * 시험 문제 일련 번호 생성
     */
    String generateQuestionSerialNo(String prefixCode);

    /**
     * 시험 문제 문항 일련 번호 생성
     */
    String generateQuestionItemSerialNo(String prefixCode);

    /**
     * 교육신청 일련 번호 생성
     */
    String generateApplicationNo(String prefixCode);

    /**
     * 교육 계획 코드 생성
     */
    String generateEducationPlanCode(String prefixCode);

    /**
     * 일반 교육 강의 코드 생성
     */
    String generateLectureCode(String prefixLecture);
}