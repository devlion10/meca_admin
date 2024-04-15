package kr.or.kpf.lms.biz.education.exam.service;

import kr.or.kpf.lms.biz.education.exam.vo.request.ExamApiRequestVO;
import kr.or.kpf.lms.biz.education.exam.vo.request.ExamViewRequestVO;
import kr.or.kpf.lms.biz.education.exam.vo.response.ExamApiResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonEducationRepository;
import kr.or.kpf.lms.repository.education.CurriculumExamRepository;
import kr.or.kpf.lms.repository.education.CurriculumMasterRepository;
import kr.or.kpf.lms.repository.education.ExamMasterRepository;
import kr.or.kpf.lms.repository.entity.contents.ContentsChapter;
import kr.or.kpf.lms.repository.entity.contents.ContentsMaster;
import kr.or.kpf.lms.repository.entity.education.CurriculumExam;
import kr.or.kpf.lms.repository.entity.education.CurriculumMaster;
import kr.or.kpf.lms.repository.entity.education.ExamMaster;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 교육 관리 > 교육 과정 관리 API 관련 Service
 */
@Service
@RequiredArgsConstructor
public class ExamService extends CSServiceSupport {

    private static final String PREFIX_EXAM = "EXAM";

    /** 교육 관리 공통 */
    private final CommonEducationRepository commonEducationRepository;
    /** 시험 관리 */
    private final ExamMasterRepository examMasterRepository;
    /** 교육과정 관리 */
    private final CurriculumMasterRepository curriculumMasterRepository;
    /** 교육 과정 연계 시험 목록 */
    private final CurriculumExamRepository curriculumExamRepository;

    /**
     * 시험 정보 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(ExamViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonEducationRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 특정 교육 과정 조회
     *
     * @param curriculumCode
     * @return
     */
    public CurriculumMaster getCurriculumInfo(String curriculumCode) {
        return curriculumMasterRepository.findOne(Example.of(CurriculumMaster.builder().curriculumCode(curriculumCode).build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3004, "존재하지 않는 교육과정"));
    }

    /**
     * 시험 생성
     *
     * @param examApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public ExamApiResponseVO createExamInfo(ExamApiRequestVO examApiRequestVO) {
        ExamMaster entity = ExamMaster.builder().build();
        copyNonNullObject(examApiRequestVO, entity);
        if (examMasterRepository.findOne(Example.of(ExamMaster.builder()
                        .examName(examApiRequestVO.getExamName())
                        .build()))
                .isPresent()) { /** 기존 등록된 시험 정보 확인 */
            throw new KPFException(KPF_RESULT.ERROR3042, "동일 시험 정보 존재(시험 생성 시 RM 적용 검토 필요). 단순 시험 명으로 Validation 체크는 문제가 있음.");
        } else {
            ExamApiResponseVO result = ExamApiResponseVO.builder().build();
            String examSerialNo = commonEducationRepository.generateExamSerialNo(PREFIX_EXAM);
            entity.setExamSerialNo(examSerialNo);
            /** 시험 생성 시 강제로 미사용 처리 */
            entity.setIsUsable(false);
            /** 시험 정보 생성 */
            BeanUtils.copyProperties(examMasterRepository.saveAndFlush(entity), result);
            /** 요청 교육과정 코드 확인 후 유효한 교육과정 코드일 경우 연결 시험 목록 생성 */
            Optional.ofNullable(examApiRequestVO.getCurriculumCode())
                    .ifPresent(data -> {
                        if(Optional.ofNullable(curriculumMasterRepository.findOne(Example.of(CurriculumMaster.builder().curriculumCode(data).build()))
                                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3004, "요청 교육과정 코드 유효하지 않음.(" + data + ")"))).isPresent()) {
                            /** 교육과정 연계 시험 정보 생성 */
                            curriculumExamRepository.saveAndFlush(CurriculumExam.builder()
                                    .curriculumCode(examApiRequestVO.getCurriculumCode())
                                    .examSerialNo(examSerialNo)
                                    .sortNo(curriculumExamRepository.findAll(Example.of(CurriculumExam.builder().curriculumCode(data).build())).size())
                                    .build());
                        }
                    });

            return result;
        }
    }

    /**
     * 시험 정보 수정
     *
     * @param examApiRequestVO
     * @return
     */
    public ExamApiResponseVO updateExamInfo(ExamApiRequestVO examApiRequestVO) {
        return examMasterRepository.findOne(Example.of(ExamMaster.builder()
                        .examSerialNo(examApiRequestVO.getExamSerialNo())
                        .build()))
                .map(examMaster -> {
                    copyNonNullObject(examApiRequestVO, examMaster);

                    ExamApiResponseVO result = ExamApiResponseVO.builder().build();

                    if (examApiRequestVO.getIsUsable() &&
                            examMasterRepository.findAll(Example.of(ExamMaster.builder()
                                    .isUsable(true)
                                    .build())).stream().anyMatch(data -> !data.getExamSerialNo().equals(examMaster.getExamSerialNo()))) {
                        throw new KPFException(KPF_RESULT.ERROR3045, "이미 사용 상태인 시험 존재. 1개의 과정 당 사용 가능 시험 1개.");
                    }

                    BeanUtils.copyProperties(examMasterRepository.saveAndFlush(examMaster), result);

                    Optional.ofNullable(examApiRequestVO.getCurriculumCode())
                            .ifPresent(data -> {
                                if (Optional.ofNullable(curriculumMasterRepository.findOne(Example.of(CurriculumMaster.builder().curriculumCode(data).build()))
                                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3004, "요청 교육과정 코드 유효하지 않음.(" + data + ")"))).isPresent()) {
                                    /**
                                     * 교육과정 연계 시험 정보 업데이트
                                     *
                                     * 기존에 존재하는 시험정보 목록이라면 업데이트, 없는 시험정보 목록 요청이라면 새로 생성
                                     */
                                    curriculumExamRepository.saveAndFlush(curriculumExamRepository.findOne(Example.of(CurriculumExam.builder()
                                                    .curriculumCode(examApiRequestVO.getCurriculumCode())
                                                    .examSerialNo(examApiRequestVO.getExamSerialNo())
                                                    .build()))
                                            .orElseGet(() -> CurriculumExam.builder()
                                                    .curriculumCode(examApiRequestVO.getCurriculumCode())
                                                    .examSerialNo(examApiRequestVO.getExamSerialNo())
                                                    .sortNo(curriculumExamRepository.findAll(Example.of(CurriculumExam.builder().curriculumCode(data).build())).size())
                                                    .build()));
                                }
                            });

                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR3044, "해당 시험 정보 미존재"));
    }
}


