package kr.or.kpf.lms.biz.user.webauthority.service;

import kr.or.kpf.lms.biz.user.webauthority.vo.request.WebAuthorityApiRequestVO;
import kr.or.kpf.lms.biz.user.webauthority.vo.request.WebAuthorityOrgApiVO;
import kr.or.kpf.lms.biz.user.webauthority.vo.request.WebAuthorityViewRequestVO;
import kr.or.kpf.lms.biz.user.webauthority.vo.response.WebAuthorityApiResponseVO;
import kr.or.kpf.lms.biz.user.webauthority.vo.response.WebAuthorityExcelVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.common.support.Code;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonUserRepository;
import kr.or.kpf.lms.repository.entity.user.InstructorInfo;
import kr.or.kpf.lms.repository.role.IndividualAuthorityHistoryRepository;
import kr.or.kpf.lms.repository.user.InstructorInfoRepository;
import kr.or.kpf.lms.repository.user.LmsUserRepository;
import kr.or.kpf.lms.repository.entity.role.IndividualAuthorityHistory;
import kr.or.kpf.lms.repository.entity.role.OrganizationAuthorityHistory;
import kr.or.kpf.lms.repository.entity.user.LmsUser;
import kr.or.kpf.lms.repository.role.OrganizationAuthorityHistoryRepository;
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
 * 사용자 관리 > 권한 관리 관련 Service
 */
@Service
@RequiredArgsConstructor
public class WebAuthorityService extends CSServiceSupport {

    /** 사용자 관리 공통 */
    private final CommonUserRepository commonUserRepository;
    /** 사업 참여 권한 신청 이력 (기관) */
    private final OrganizationAuthorityHistoryRepository organizationAuthorityHistoryRepository;
    /** 사업 참여 권한 신청 이력 (개인) */
    private final IndividualAuthorityHistoryRepository individualAuthorityHistoryRepository;
    /** 웹 회원 */
    private final LmsUserRepository lmsUserRepository;
    /** 강사 */
    private final InstructorInfoRepository instructorInfoRepository;

    /**
     * 권한 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(WebAuthorityViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonUserRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 사업 참여 권한 승인 상태 업데이트
     *
     * @param webAuthorityApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public WebAuthorityApiResponseVO updateBusinessAuthorityApprovalState(WebAuthorityApiRequestVO webAuthorityApiRequestVO) {
        switch (Code.BIZ_AUTH.enumOfCode(webAuthorityApiRequestVO.getBusinessAuthority())) {
            case AGENCY: case SCHOOL:
                return organizationAuthorityHistoryRepository.findOne(Example.of(OrganizationAuthorityHistory.builder()
                                .sequenceNo(webAuthorityApiRequestVO.getSequenceNo())
                                .userId(webAuthorityApiRequestVO.getUserId())
                                .businessAuthority(webAuthorityApiRequestVO.getBusinessAuthority())
                                .build()))
                        .map(organizationAuthorityHistory -> {
                            WebAuthorityApiResponseVO result = WebAuthorityApiResponseVO.builder().build();
                            organizationAuthorityHistory.setBusinessAuthorityApprovalState(webAuthorityApiRequestVO.getBusinessAuthorityApprovalState());

                            BeanUtils.copyProperties(organizationAuthorityHistoryRepository.saveAndFlush(organizationAuthorityHistory), result);
                            return result;
                        })
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1063, "사업 참여 권한 신청 이력 미존재."));
            case INSTR:
                return individualAuthorityHistoryRepository.findOne(Example.of(IndividualAuthorityHistory.builder()
                                .sequenceNo(webAuthorityApiRequestVO.getSequenceNo())
                                .userId(webAuthorityApiRequestVO.getUserId())
                                .businessAuthority(webAuthorityApiRequestVO.getBusinessAuthority())
                                .build()))
                        .map(individualAuthorityHistory -> {
                            WebAuthorityApiResponseVO result = WebAuthorityApiResponseVO.builder().build();
                            individualAuthorityHistory.setBusinessAuthorityApprovalState(Code.BIZ_AUTH_STATE.enumOfCode(webAuthorityApiRequestVO.getBusinessAuthorityApprovalState()).enumCode);

                            BeanUtils.copyProperties(individualAuthorityHistoryRepository.saveAndFlush(individualAuthorityHistory), result);
                            return result;
                        })
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1063, "사업 참여 권한 신청 이력 미존재."));
            default:
                throw new KPFException(KPF_RESULT.ERROR9002, "유효하지 않은 사업 참여 권한 코드");
        }
    }

    /**
     * 사업 참여 권한 해제
     *
     * @param webAuthorityApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public WebAuthorityApiResponseVO removeBusinessAuthority(WebAuthorityApiRequestVO webAuthorityApiRequestVO) {
        LmsUser lmsUser = lmsUserRepository.findOne(Example.of(LmsUser.builder()
                        .userId(webAuthorityApiRequestVO.getUserId())
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1004, "회원 정보 미존재."));

        WebAuthorityApiResponseVO result = WebAuthorityApiResponseVO.builder().build();

        switch (Code.BIZ_AUTH.enumOfCode(webAuthorityApiRequestVO.getBusinessAuthority())) {
            case SCHOOL: case AGENCY:
                organizationAuthorityHistoryRepository.findOne(Example.of(OrganizationAuthorityHistory.builder()
                                .sequenceNo(webAuthorityApiRequestVO.getSequenceNo())
                                .userId(webAuthorityApiRequestVO.getUserId())
                                .businessAuthority(webAuthorityApiRequestVO.getBusinessAuthority())
                                .build()))
                        .map(organizationAuthorityHistory -> {
                            WebAuthorityApiResponseVO vo = WebAuthorityApiResponseVO.builder().build();
                            organizationAuthorityHistory.setBusinessAuthorityApprovalState(Code.BIZ_AUTH_STATE.enumOfCode(webAuthorityApiRequestVO.getBusinessAuthorityApprovalState()).enumCode);

                            BeanUtils.copyProperties(organizationAuthorityHistoryRepository.saveAndFlush(organizationAuthorityHistory), vo);
                            return vo;
                        })
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1063, "사업 참여 권한 신청 이력 미존재."));

                /** 사업 참여 권한 제거 */
                lmsUser.setBusinessAuthority(null);
                lmsUserRepository.saveAndFlush(lmsUser);
                break;

            case INSTR:
                individualAuthorityHistoryRepository.findOne(Example.of(IndividualAuthorityHistory.builder()
                                .sequenceNo(webAuthorityApiRequestVO.getSequenceNo())
                                .userId(webAuthorityApiRequestVO.getUserId())
                                .businessAuthority(webAuthorityApiRequestVO.getBusinessAuthority())
                                .build()))
                        .map(individualAuthorityHistory -> {
                            WebAuthorityApiResponseVO vo = WebAuthorityApiResponseVO.builder().build();
                            individualAuthorityHistory.setBusinessAuthorityApprovalState(Code.BIZ_AUTH_STATE.enumOfCode(webAuthorityApiRequestVO.getBusinessAuthorityApprovalState()).enumCode);

                            BeanUtils.copyProperties(individualAuthorityHistoryRepository.saveAndFlush(individualAuthorityHistory), vo);
                            return vo;
                        })
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1063, "사업 참여 권한 신청 이력 미존재."));

                List<InstructorInfo> instructorInfos = instructorInfoRepository.findAll(Example.of(InstructorInfo.builder()
                        .userId(webAuthorityApiRequestVO.getUserId())
                        .build())).stream().sorted(Comparator.comparing(InstructorInfo::getCreateDateTime).reversed()).collect(Collectors.toList());

                for (InstructorInfo instructorInfo : instructorInfos) {
                    instructorInfo.setInstrStts(0);
                    instructorInfoRepository.saveAndFlush(instructorInfo);
                }

                /** 사업 참여 권한 제거 */
                lmsUser.setBusinessAuthority(null);
                lmsUserRepository.saveAndFlush(lmsUser);
                break;
        }
        return result;
    }

    /**
     기관정보 엑셀 다운로드
     */
    public <T> List<WebAuthorityExcelVO> getExcel(WebAuthorityViewRequestVO requestObject) {
        List<WebAuthorityExcelVO> webAuthorityExcelVOList = (List<WebAuthorityExcelVO>) commonUserRepository.findEntityListExcel(requestObject);
        return webAuthorityExcelVOList;
    }

    /**
     * 사업 참여 권한 승인 상태 업데이트
     *
     * @param webAuthorityOrgApiVO
     * @return
     */
    public WebAuthorityApiResponseVO updateBusinessAuthority(WebAuthorityOrgApiVO webAuthorityOrgApiVO) {

        WebAuthorityApiResponseVO result = WebAuthorityApiResponseVO.builder().build();
        switch (Code.BIZ_AUTH.enumOfCode(webAuthorityOrgApiVO.getBusinessAuthority())) {
            case AGENCY:
            case SCHOOL:
                organizationAuthorityHistoryRepository.findOne(Example.of(OrganizationAuthorityHistory.builder()
                                .sequenceNo(webAuthorityOrgApiVO.getSequenceNo())
                                .userId(webAuthorityOrgApiVO.getUserId())
                                .build()))
                        .map(organizationAuthorityHistory -> {
                            copyNonNullObject(webAuthorityOrgApiVO, organizationAuthorityHistory);
                            BeanUtils.copyProperties(organizationAuthorityHistoryRepository.saveAndFlush(organizationAuthorityHistory), result);

                            /** Lms 유저에도 변경값 반영 */
                            LmsUser lmsUser = lmsUserRepository.findOne(Example.of(LmsUser.builder()
                                            .userId(organizationAuthorityHistory.getUserId())
                                            .build()))
                                    .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1004, "회원 정보 미존재."));
                            if(lmsUser != null){
                                lmsUser.setOrganizationCode(organizationAuthorityHistory.getOrganizationCode());
                                lmsUserRepository.saveAndFlush(lmsUser);
                            }

                            return result;
                        })
                        .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1063, "사업 참여 권한 신청 이력 미존재."));
        }
        return result;
    }
}
