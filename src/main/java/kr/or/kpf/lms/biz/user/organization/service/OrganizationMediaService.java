package kr.or.kpf.lms.biz.user.organization.service;

import kr.or.kpf.lms.biz.user.organization.vo.request.OrganizationApiRequestVO;
import kr.or.kpf.lms.biz.user.organization.vo.request.OrganizationMediaApiRequestVO;
import kr.or.kpf.lms.biz.user.organization.vo.request.OrganizationMediaViewRequestVO;
import kr.or.kpf.lms.biz.user.organization.vo.request.OrganizationViewRequestVO;
import kr.or.kpf.lms.biz.user.organization.vo.response.OrganizationApiResponseVO;
import kr.or.kpf.lms.biz.user.organization.vo.response.OrganizationExcelVO;
import kr.or.kpf.lms.biz.user.organization.vo.response.OrganizationMediaApiResponseVO;
import kr.or.kpf.lms.biz.user.organization.vo.response.OrganizationMediaViewResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonUserRepository;
import kr.or.kpf.lms.repository.entity.user.OrganizationInfo;
import kr.or.kpf.lms.repository.entity.user.OrganizationInfoMedia;
import kr.or.kpf.lms.repository.user.OrganizationInfoRepository;
import kr.or.kpf.lms.repository.user.OrganizationMediaInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizationMediaService extends CSServiceSupport {

    private static final String PREFIX_ORGANIZATION = "MDA";

    /** 사용자 관리 공통 */
    private final CommonUserRepository commonUserRepository;
    /** 기관 관리 */
    private final OrganizationMediaInfoRepository organizationInfoMediaRepository;

    /**
     * 기관 정보 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(OrganizationMediaViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonUserRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 기관 정보 생성
     *
     * @param organizationMediaApiRequestVO
     * @return
     */
    public OrganizationMediaApiResponseVO createOrganizationInfo(OrganizationMediaApiRequestVO organizationMediaApiRequestVO) {
        OrganizationInfoMedia entity = OrganizationInfoMedia.builder().build();
        BeanUtils.copyProperties(organizationMediaApiRequestVO, entity);
        if (organizationInfoMediaRepository.findOne(Example.of(OrganizationInfoMedia.builder()
                        .mediaName(organizationMediaApiRequestVO.getMediaName())
                .build())).isPresent()) {
            throw new KPFException(KPF_RESULT.ERROR1203, "동일 기관명 존재");
        } else {
            OrganizationMediaApiResponseVO result = OrganizationMediaApiResponseVO.builder().build();
            entity.setMediaCode(commonUserRepository.generateMediaCode(PREFIX_ORGANIZATION));
            BeanUtils.copyProperties(organizationInfoMediaRepository.saveAndFlush(entity), result);
            return result;
        }
    }

    /**
     * 기관 정보 업데이트
     *
     * @param organizationApiRequestVO
     * @return
     */
    public OrganizationMediaApiResponseVO updateOrganizationInfo(OrganizationMediaApiRequestVO organizationApiRequestVO) {
        return organizationInfoMediaRepository.findOne(Example.of(OrganizationInfoMedia.builder()
                        .mediaCode(organizationApiRequestVO.getMediaCode())
                        .build()))
                .map(organizationInfo -> {

                    copyNonNullObject(organizationApiRequestVO, organizationInfo);

                    OrganizationMediaApiResponseVO result = OrganizationMediaApiResponseVO.builder().build();
                    BeanUtils.copyProperties(organizationInfoMediaRepository.saveAndFlush(organizationInfo), result);
                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1204, "해당 회원 정보 미존재"));
    }

    /**
     기관정보 엑셀 다운로드
     */
    /*
    public <T> List<OrganizationExcelVO> getExcel(OrganizationViewRequestVO requestObject) {
        List<OrganizationExcelVO> instructorExcelVOList = (List<OrganizationExcelVO>) commonUserRepository.findEntityListExcel(requestObject);
        return instructorExcelVOList;
    }
     */

}
