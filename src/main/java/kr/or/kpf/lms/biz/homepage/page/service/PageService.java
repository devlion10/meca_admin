package kr.or.kpf.lms.biz.homepage.page.service;

import kr.or.kpf.lms.biz.homepage.banner.vo.request.BannerApiRequestVO;
import kr.or.kpf.lms.biz.homepage.page.vo.request.PageApiRequestVO;
import kr.or.kpf.lms.biz.homepage.page.vo.request.PageViewRequestVO;
import kr.or.kpf.lms.biz.homepage.page.vo.response.PageApiResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.entity.homepage.Documents;
import kr.or.kpf.lms.repository.CommonHomepageRepository;
import kr.or.kpf.lms.repository.homepage.DocumentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

/**
 * 홈페이지 관리 > 문서 관련 Service
 */
@Service
@RequiredArgsConstructor
public class PageService extends CSServiceSupport {

    /** 홈페이지 관리 공통 Repository */
    private final CommonHomepageRepository commonHomepageRepository;
    /** 문서 Repository */
    private final DocumentsRepository documentsRepository;

    /**
     * 문서 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(PageViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonHomepageRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 문서 생성
     *
     * @param pageApiRequestVO
     * @return
     */
    public PageApiResponseVO createPage(PageApiRequestVO pageApiRequestVO) {
        Documents entity = Documents.builder().build();
        copyNonNullObject(pageApiRequestVO, entity);
        PageApiResponseVO result = PageApiResponseVO.builder().build();
        BeanUtils.copyProperties(documentsRepository.saveAndFlush(entity), result);
        return result;
    }

    /**
     * 문서 업데이트
     *
     * @param pageApiRequestVO
     * @return
     */
    public PageApiResponseVO updatePage(PageApiRequestVO pageApiRequestVO) {
        return documentsRepository.findOne(Example.of(Documents.builder().sequenceNo(pageApiRequestVO.getSequenceNo()).build()))
                .map(documents -> {

                    copyNonNullObject(pageApiRequestVO, documents);
                    PageApiResponseVO result = PageApiResponseVO.builder().build();
                    BeanUtils.copyProperties(documentsRepository.saveAndFlush(documents), result);

                    return result;
                })
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9009, "해당 문서 미존재"));
    }

    /**
     문서 정보 삭제
     */
    public CSResponseVOSupport deleteInfo(PageApiRequestVO requestObject) {
        documentsRepository.delete(documentsRepository.findById(requestObject.getSequenceNo())
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR9009, "해당 문서 미존재")));

        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

}
