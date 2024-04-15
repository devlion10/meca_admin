package kr.or.kpf.lms.biz.contents.section.service;

import kr.or.kpf.lms.biz.contents.section.vo.request.SectionApiRequestVO;
import kr.or.kpf.lms.biz.contents.section.vo.request.SectionViewRequestVO;
import kr.or.kpf.lms.biz.contents.section.vo.response.SectionApiResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonContentsRepository;
import kr.or.kpf.lms.repository.contents.SectionMasterRepository;
import kr.or.kpf.lms.repository.entity.contents.SectionMaster;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 콘텐츠 관리 > 차시 관리 > 절 관련 Service
 */
@Service
@RequiredArgsConstructor
public class SectionService extends CSServiceSupport {

    private static final String PREFIX_SECTION = "SEC";

    /** 콘텐츠 관리 공통 */
    private final CommonContentsRepository commonContentsRepository;
    /** 섹션(절) */
    private final SectionMasterRepository sectionMasterRepository;

    /**
     * 섹션(절) 정보 조회
     *
     * @param <T>
     * @param requestObject
     * @return
     */
    public <T> Page<T> getList(SectionViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonContentsRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 섹션(절) 정보 생성
     *  
     * @param sectionApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public SectionApiResponseVO createSectionInfo(SectionApiRequestVO sectionApiRequestVO) {
        SectionMaster entity = SectionMaster.builder().build();
        copyNonNullObject(sectionApiRequestVO, entity);

        SectionApiResponseVO result = SectionApiResponseVO.builder().build();
        String sectionCode = commonContentsRepository.generateSectionCode(PREFIX_SECTION);
        entity.setSectionCode(sectionCode);
        /** 섹션(절) 정보 생성 */
        BeanUtils.copyProperties(sectionMasterRepository.saveAndFlush(entity), result);

        return result;
    }

    /**
     * 섹션(절) 정보 업데이트
     *  
     * @param sectionApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public SectionApiResponseVO updateSectionInfo(SectionApiRequestVO sectionApiRequestVO) {
        return sectionMasterRepository.findOne(Example.of(SectionMaster.builder()
                        .sectionCode(sectionApiRequestVO.getSectionCode())
                        .build()))
                .map(sectionMaster -> {

                    copyNonNullObject(sectionApiRequestVO, sectionMaster);

                    SectionApiResponseVO result = SectionApiResponseVO.builder().build();
                    BeanUtils.copyProperties(sectionMasterRepository.saveAndFlush(sectionMaster), result);

                    return result;

                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2081, "해당 섹션(절) 미존재"));
    }

    /**
     * 섹션(절) 정보 삭제
     *
     * @param sectionApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport deleteSectionInfo(SectionApiRequestVO sectionApiRequestVO) {
        sectionMasterRepository.delete(sectionMasterRepository.findOne(Example.of(SectionMaster.builder()
                        .chapterCode(sectionApiRequestVO.getChapterCode())
                        .sectionCode(sectionApiRequestVO.getSectionCode())
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR2081, "해당 섹션(절) 미존재")));
        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     * 섹션(절) 정보 수정 (생성 / 업데이트 / 삭제)
     *
     * @param sectionApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport modifySectionInfoList(SectionApiRequestVO sectionApiRequestVO) {
        List<String> sectionCodeList = sectionApiRequestVO.getSectionInfoList().stream().map(data -> {
            if(StringUtils.isEmpty(data.getSectionCode())) { /** 섹션(절) 코드가 존재하지 않는 경우는 생성건으로 판단 */
                return createSectionInfo(data).getSectionCode();
            } else { /** 섹션(절) 코드가 존재하는 경우는 수정건으로 판단 */
                return updateSectionInfo(data).getSectionCode();
            }
        }).collect(Collectors.toList());

        /** 요청 섹션(절) 정보에 해당하지 않는 섹션(절)이 존재할 경우... 관련 섹션(절) 데이터 삭제 */
        Optional.of(sectionMasterRepository.findAll(Example.of(SectionMaster.builder()
                        .chapterCode(sectionApiRequestVO.getChapterCode())
                        .build())))
                .filter(data -> data.size() > 0).ifPresent(data -> {
                    data.stream().filter(subData -> !sectionCodeList.contains(subData.getSectionCode()))
                            .forEach(subData -> {
                                SectionApiRequestVO deleteObject = SectionApiRequestVO.builder().build();
                                BeanUtils.copyProperties(subData, deleteObject);
                                deleteSectionInfo(deleteObject);
                            });
                });
        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

}