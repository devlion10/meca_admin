package kr.or.kpf.lms.biz.user.organization.service;

import kr.or.kpf.lms.biz.user.organization.vo.request.OrganizationApiRequestVO;
import kr.or.kpf.lms.biz.user.organization.vo.request.OrganizationMediaViewRequestVO;
import kr.or.kpf.lms.biz.user.organization.vo.request.OrganizationViewRequestVO;
import kr.or.kpf.lms.biz.user.organization.vo.response.OrganizationApiResponseVO;
import kr.or.kpf.lms.biz.user.organization.vo.response.OrganizationExcelVO;
import kr.or.kpf.lms.biz.user.organization.vo.response.OrganizationMediaExcelVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonUserRepository;
import kr.or.kpf.lms.repository.entity.user.OrganizationInfo;
import kr.or.kpf.lms.repository.user.OrganizationInfoRepository;
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
public class OrganizationService extends CSServiceSupport {

    private static final String PREFIX_ORGANIZATION = "ORG";

    /** 사용자 관리 공통 */
    private final CommonUserRepository commonUserRepository;
    /** 기관 관리 */
    private final OrganizationInfoRepository organizationInfoRepository;

    /**
     * 기관 정보 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(OrganizationViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonUserRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 기관 정보 생성
     *
     * @param organizationApiRequestVO
     * @return
     */
    public OrganizationApiResponseVO createOrganizationInfo(OrganizationApiRequestVO organizationApiRequestVO) {
        OrganizationInfo entity = OrganizationInfo.builder().build();
        BeanUtils.copyProperties(organizationApiRequestVO, entity);
        if (organizationInfoRepository.findOne(Example.of(OrganizationInfo.builder()
                        .organizationName(organizationApiRequestVO.getOrganizationName())
                .build())).isPresent()) {
            throw new KPFException(KPF_RESULT.ERROR1203, "동일 기관명 존재");
        } else {
            OrganizationApiResponseVO result = OrganizationApiResponseVO.builder().build();
            entity.setOrganizationCode(commonUserRepository.generateOrganizationCode(PREFIX_ORGANIZATION));
            if (organizationApiRequestVO.getOrganizationTelNumber().equals("0"))
                entity.setOrganizationTelNumber(null);
            BeanUtils.copyProperties(organizationInfoRepository.saveAndFlush(entity), result);
            return result;
        }
    }

    /**
     * 기관 정보 업데이트
     *
     * @param organizationApiRequestVO
     * @return
     */
    public OrganizationApiResponseVO updateOrganizationInfo(OrganizationApiRequestVO organizationApiRequestVO) {
        return organizationInfoRepository.findOne(Example.of(OrganizationInfo.builder()
                        .organizationCode(organizationApiRequestVO.getOrganizationCode())
                        .build()))
                .map(organizationInfo -> {
                    copyNonNullObject(organizationApiRequestVO, organizationInfo);

                    if (organizationApiRequestVO.getOrganizationTelNumber().equals("0"))
                        organizationInfo.setOrganizationTelNumber(null);

                    OrganizationApiResponseVO result = OrganizationApiResponseVO.builder().build();
                    BeanUtils.copyProperties(organizationInfoRepository.saveAndFlush(organizationInfo), result);

                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1204, "해당 회원 정보 미존재"));
    }

    /**
     기관정보 엑셀 다운로드
     */
    public <T> List<OrganizationExcelVO> getExcel(OrganizationViewRequestVO requestObject) {
        List<OrganizationExcelVO> excelVOList = (List<OrganizationExcelVO>) commonUserRepository.findEntityListExcel(requestObject);
        return excelVOList;
    }

    /**
     매체정보 엑셀 다운로드
     */
    public <T> List<OrganizationMediaExcelVO> getMediaExcel(OrganizationMediaViewRequestVO requestObject) {
        List<OrganizationMediaExcelVO> excelVOList = (List<OrganizationMediaExcelVO>) commonUserRepository.findEntityListExcel(requestObject);
        return excelVOList;
    }

}
