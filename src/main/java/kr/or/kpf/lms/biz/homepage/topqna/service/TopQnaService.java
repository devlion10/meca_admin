package kr.or.kpf.lms.biz.homepage.topqna.service;

import kr.or.kpf.lms.biz.homepage.topqna.vo.request.TopQnaApiRequestVO;
import kr.or.kpf.lms.biz.homepage.topqna.vo.request.TopQnaViewRequestVO;
import kr.or.kpf.lms.biz.homepage.topqna.vo.response.TopQnaApiResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.entity.homepage.TopQna;
import kr.or.kpf.lms.repository.CommonHomepageRepository;
import kr.or.kpf.lms.repository.homepage.TopQnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Optional;

/**
 * 홈페이지 관리 > 자주묻는 질문 관련 Service
 */
@Service
@RequiredArgsConstructor
public class TopQnaService extends CSServiceSupport {

    /** 홈페이지 관리 공통 Repository */
    private final CommonHomepageRepository commonHomepageRepository;
    /** 자주묻는 질문 Repository */
    private final TopQnaRepository topQnaRepository;

    /**
     * 고객센터 > 자주묻는 질문 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getTopQnaList(TopQnaViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonHomepageRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 자주묻는 질문 생성
     *
     * @param topQnaApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public TopQnaApiResponseVO createTopQna(TopQnaApiRequestVO topQnaApiRequestVO) {
        TopQna entity = TopQna.builder().build();
        copyNonNullObject(topQnaApiRequestVO, entity);

        TopQnaApiResponseVO result = TopQnaApiResponseVO.builder().build();

        BeanUtils.copyProperties(topQnaRepository.saveAndFlush(entity), result);
        return result;
    }

    /**
     * 자주묻는 질문 업데이트
     *
     * @param topQnaApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public TopQnaApiResponseVO updateTopQna(TopQnaApiRequestVO topQnaApiRequestVO) {
        return topQnaRepository.findOne(Example.of(TopQna.builder().sequenceNo(topQnaApiRequestVO.getSequenceNo()).build()))
                .map(topQna -> {

                    copyNonNullObject(topQnaApiRequestVO, topQna);

                    TopQnaApiResponseVO result = TopQnaApiResponseVO.builder().build();
                    BeanUtils.copyProperties(topQnaRepository.saveAndFlush(topQna), result);

                    return result;
                })
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR7062, "해당 자주묻는 질문 미존재"));
    }

    /**
     * 자주묻는 질문 삭제
     *
     * @param sequenceNo
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport deleteTopQna(BigInteger sequenceNo) {
        topQnaRepository.delete(TopQna.builder().sequenceNo(sequenceNo).build());
        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }
}
