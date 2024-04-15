package kr.or.kpf.lms.biz.education.lecture.service;

import kr.or.kpf.lms.biz.education.lecture.vo.request.LectureApiRequestVO;
import kr.or.kpf.lms.biz.education.lecture.vo.request.LectureViewRequestVO;
import kr.or.kpf.lms.biz.education.lecture.vo.response.LectureApiResponseVO;
import kr.or.kpf.lms.biz.homepage.notice.vo.request.NoticeApiRequestVO;
import kr.or.kpf.lms.biz.homepage.notice.vo.response.NoticeApiResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonEducationRepository;
import kr.or.kpf.lms.repository.education.LectureMasterRepository;
import kr.or.kpf.lms.repository.entity.education.LectureMaster;
import kr.or.kpf.lms.repository.entity.homepage.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

/**
 * 교육 관리 > 교육 개설 API 관련 Service
 */
@Service
@RequiredArgsConstructor
public class LectureService extends CSServiceSupport {

    /** 교육 관리 공통 */
    private final CommonEducationRepository commonEducationRepository;
    private final LectureMasterRepository lectureMasterRepository;

    /**
     * 일반 교육 강의 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(LectureViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonEducationRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 일반 교육 강의 업데이트
     *
     * @param lectureCode
     * @return
     */
    public LectureApiResponseVO deleteLecture(String lectureCode) {
        return lectureMasterRepository.findOne(Example.of(LectureMaster.builder().lectureCode(lectureCode).build()))
                .map(lecture -> {
                    lecture.setEducationPlanCode("0");
                    LectureApiResponseVO result = LectureApiResponseVO.builder().build();
                    BeanUtils.copyProperties(lectureMasterRepository.saveAndFlush(lecture), result);
                    return result;
                })
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9009, "해당 정보 미존재"));
    }
}
