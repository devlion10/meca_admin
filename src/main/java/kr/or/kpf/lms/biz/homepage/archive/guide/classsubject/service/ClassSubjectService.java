package kr.or.kpf.lms.biz.homepage.archive.guide.classsubject.service;

import kr.or.kpf.lms.biz.homepage.archive.guide.classsubject.vo.request.ClassSubjectApiRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.guide.classsubject.vo.request.ClassSubjectViewRequestVO;
import kr.or.kpf.lms.biz.homepage.archive.guide.classsubject.vo.response.ClassSubjectApiResponseVO;
import kr.or.kpf.lms.biz.homepage.archive.guide.vo.request.ArchiveClassGuideApiRequestVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.entity.BizInstructorDist;
import kr.or.kpf.lms.repository.entity.homepage.ClassSubject;
import kr.or.kpf.lms.repository.homepage.ClassSubjectRepository;
import kr.or.kpf.lms.repository.CommonHomepageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 수업지도안 과목 관련 Service
 */
@Service
@RequiredArgsConstructor
public class ClassSubjectService extends CSServiceSupport {

    private static final String PREFIX_SUBJECT = "SBJT";

    private final CommonHomepageRepository commonHomepageRepository;
    private final ClassSubjectRepository classSubjectRepository;

    /**
     * 수업지도안 교과 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(ClassSubjectViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonHomepageRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 수업지도안 교과 생성
     *
     * @param requestObject
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public ClassSubjectApiResponseVO createClassSubject(ClassSubjectApiRequestVO requestObject) {
        ClassSubject entity = ClassSubject.builder().build();
        copyNonNullObject(requestObject, entity);

        ClassSubjectApiResponseVO result = ClassSubjectApiResponseVO.builder().build();
        entity.setIndividualCode(commonHomepageRepository.generateCode(PREFIX_SUBJECT));

        String upIndividualCode = requestObject.getUpIndividualCode();
        entity.setOrder(commonHomepageRepository.generateOrderAutoIncrease(upIndividualCode));

        BeanUtils.copyProperties(classSubjectRepository.saveAndFlush(entity), result);
        return result;

    }

    /**
     * 수업지도안 교과 업데이트
     *
     * @param requestObject
     * @return
     */
    public ClassSubjectApiResponseVO updateInfo(ClassSubjectApiRequestVO requestObject) {
        return classSubjectRepository.findOne(Example.of(ClassSubject.builder().individualCode(requestObject.getIndividualCode()).build()))
                .map(classSubject -> {

                    copyNonNullObject(requestObject, classSubject);
                    ClassSubjectApiResponseVO result = ClassSubjectApiResponseVO.builder().build();
                    BeanUtils.copyProperties(classSubjectRepository.saveAndFlush(classSubject), result);

                    return result;
                })
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR4005, "해당 수업지도안 교과 미존재"));
    }

    /**
     * 수업지도안 교과 순서 변경
     *
     * @param requestObject
     * @return
     */
    public ClassSubjectApiResponseVO changeOrder(ClassSubjectApiRequestVO requestObject) {
        return classSubjectRepository.findOne(Example.of(ClassSubject.builder().individualCode(requestObject.getIndividualCode()).build()))
                .map(classSubject -> {
                    /** 순서 변경이 실행될 때, 순서가 기존 순서와 다른 경우 */
                    if (requestObject.getOrder() != classSubject.getOrder() && !requestObject.getOrder().equals(classSubject.getOrder())) {
                        /** 상위 코드와 깊이가 같은 코드들을 찾아서 순서 기준 정렬한 리스트 생성 */
                        List<ClassSubject> subjects = classSubjectRepository.findAll(Example.of(ClassSubject.builder()
                                .upIndividualCode(requestObject.getUpIndividualCode())
                                .depth(requestObject.getDepth())
                                .build())).stream().sorted(Comparator.comparing(ClassSubject::getOrder)).collect(Collectors.toList());

                        for (ClassSubject subject : subjects) {
                            if (requestObject.getOrder() <= classSubject.getOrder()) {
                                /** 리스트의 객체 중 순서가 같거나 큰 객체 찾기 */
                                if (subject.getOrder() >= requestObject.getOrder()) {
                                    subject.setOrder(subject.getOrder() + 1);
                                    classSubjectRepository.saveAndFlush(subject);
                                }
                            } else {
                                /** 리스트의 객체 중 순서가 같거나 작은 객체 찾기 */
                                if (subject.getOrder() < requestObject.getOrder()) {
                                    subject.setOrder(subject.getOrder() - 1);
                                    classSubjectRepository.saveAndFlush(subject);
                                }
                            }
                        }
                    }

                    copyNonNullObject(requestObject, classSubject);
                    ClassSubjectApiResponseVO result = ClassSubjectApiResponseVO.builder().build();
                    BeanUtils.copyProperties(classSubjectRepository.saveAndFlush(classSubject), result);

                    return result;
                })
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR4005, "해당 수업지도안 교과 미존재"));
    }

    /**
     수업지도안 교과 삭제
     */
    public CSResponseVOSupport deleteInfo(ClassSubjectApiRequestVO requestObject) {
        classSubjectRepository.delete(classSubjectRepository.findById(requestObject.getIndividualCode())
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR4007, "이미 삭제된 수업지도안 교과 입니다.")));

        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }
}
