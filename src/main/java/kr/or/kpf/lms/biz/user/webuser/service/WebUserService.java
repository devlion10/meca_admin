package kr.or.kpf.lms.biz.user.webuser.service;

import kr.or.kpf.lms.biz.user.organization.vo.request.OrganizationViewRequestVO;
import kr.or.kpf.lms.biz.user.webuser.vo.request.WebUserApiRequestVO;
import kr.or.kpf.lms.biz.user.webuser.vo.request.WebUserViewRequestVO;
import kr.or.kpf.lms.biz.user.webuser.vo.response.WebUserApiResponseVO;
import kr.or.kpf.lms.biz.user.webuser.vo.response.WebUserExcelVO;
import kr.or.kpf.lms.common.encrypt.SecurityUtil;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.common.support.Code;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonUserRepository;
import kr.or.kpf.lms.repository.entity.role.IndividualAuthorityHistory;
import kr.or.kpf.lms.repository.entity.role.OrganizationAuthorityHistory;
import kr.or.kpf.lms.repository.entity.system.CommonCodeMaster;
import kr.or.kpf.lms.repository.entity.user.InstructorInfo;
import kr.or.kpf.lms.repository.role.IndividualAuthorityHistoryRepository;
import kr.or.kpf.lms.repository.role.OrganizationAuthorityHistoryRepository;
import kr.or.kpf.lms.repository.user.InstructorInfoRepository;
import kr.or.kpf.lms.repository.user.LmsUserRepository;
import kr.or.kpf.lms.repository.entity.user.LmsUser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WebUserService extends CSServiceSupport {

    /** 사용자 관리 공통 */
    private final CommonUserRepository commonUserRepository;
    /** 웹 회원 */
    private final LmsUserRepository lmsUserRepository;
    private final InstructorInfoRepository instructorInfoRepository;
    private final IndividualAuthorityHistoryRepository individualAuthorityHistoryRepository;
    private final OrganizationAuthorityHistoryRepository organizationAuthorityHistoryRepository;

    private static final String PROOF_EMPLOYMENT_IMG_TAG = "_PROOF_EMPLOYMENT";
    /**
     * 웹 회원 정보 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(WebUserViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonUserRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    public LmsUser getUserInfoById(String userId) {
        return lmsUserRepository.findOne(Example.of(LmsUser.builder().userId(userId).build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1004, "존재하지 않는 ID"));
    }

    /**
     * 웹 회원 정보 업데이트
     *
     * @param webUserApiRequestVO
     * @return
     */
    public WebUserApiResponseVO updateUserInfo(WebUserApiRequestVO webUserApiRequestVO) {
        return lmsUserRepository.findOne(Example.of(LmsUser.builder()
                        .userId(webUserApiRequestVO.getUserId())
                        .build()))
                .map(userMaster -> {
                    /** Validation 추가 Check */
                    Code.WEB_USER_ROLE.valueOfEnum(webUserApiRequestVO.getRoleGroup());
                    Code.BIZ_AUTH.valueOfEnum(webUserApiRequestVO.getBusinessAuthority());
                    Code.GNDR_CD.valueOfEnum(webUserApiRequestVO.getGender());
                    Code.USER_STATE.valueOfEnum(webUserApiRequestVO.getState());

                    WebUserApiResponseVO result = WebUserApiResponseVO.builder().build();
                    copyNonNullObject(webUserApiRequestVO, userMaster);

                    if(webUserApiRequestVO.getState().equals(Code.USER_STATE.WITHDRAWAL.enumCode)){
                        SimpleDateFormat str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date now = new Date();
                        String nowStr = str.format(now);
                        userMaster.setWithDrawDate(nowStr);
                        userMaster.setState(Code.USER_STATE.WITHDRAWAL.enumCode);
                        userMaster.setDi(null);
                        userMaster.setEmail(null);

                        String nm = userMaster.getUserName();
                        String phon = userMaster.getPhone();
                        String birth = userMaster.getBirthDay();
                        StringBuffer sbNm = new StringBuffer(nm);
                        StringBuffer sbPhon = new StringBuffer(phon);
                        StringBuffer sbBirth = new StringBuffer(birth);
                        String reverseNm = sbNm.reverse().toString();
                        String reversePhon = sbPhon.reverse().toString();
                        String reverseBirth = sbBirth.reverse().toString();
                        userMaster.setUserName(reverseNm);
                        userMaster.setPhone(reversePhon);
                        userMaster.setBirthDay(reverseBirth);
                    } else {
                        /** 승인 유효일자 초기화 */
                        if(webUserApiRequestVO.getApproFlagDate() == null){
                            userMaster.setApproFlagDate(null);
                        }

                        /** 미사용 회원 인증 정보 삭제 */
                        if (webUserApiRequestVO.getState().equals(Code.USER_STATE.NONE.enumCode)) {
                            userMaster.setDi(null);
                        }
                    }

                    BeanUtils.copyProperties(lmsUserRepository.saveAndFlush(userMaster), result);
                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1003, "해당 회원 정보 미존재"));
    }

    /**
     * 비밀번호 변경 (비밀번호 분실 시...)
     *
     * @param webUserApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport changePassword(WebUserApiRequestVO webUserApiRequestVO) {
        return lmsUserRepository.findOne(Example.of(LmsUser.builder()
                        .userId(webUserApiRequestVO.getUserId())
                        .build()))
                .map(userMaster -> {
                    /** 비밀번호 암호화 */
                    userMaster.setPassword(SecurityUtil.hashPassword(String.valueOf(webUserApiRequestVO.getPassword()), webUserApiRequestVO.getUserId()));/** 비밀번호 변경 날짜 이력 */
                    /** 비밀번호 변경 일자 */
                    LocalDate now = LocalDate.now();
                    /** 포맷 정의 */
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                    /** 포맷 정의 */
                    String formatedNow = now.format(formatter);
                    userMaster.setPasswordChangeDate(formatedNow);

                    lmsUserRepository.saveAndFlush(userMaster);

                    return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1003, "해당 회원 정보 미존재"));
    }

    /**
     * 웹 회원 정보 권한 삭제
     *
     * @param webUserApiRequestVO
     * @return
     */
    public WebUserApiResponseVO removeUserAuth(WebUserApiRequestVO webUserApiRequestVO) {
        return lmsUserRepository.findOne(Example.of(LmsUser.builder()
                        .userId(webUserApiRequestVO.getUserId())
                        .build()))
                .map(userMaster -> {
                    if (userMaster.getBusinessAuthority().equals(Code.BIZ_AUTH.INSTR.enumCode)) {
                        IndividualAuthorityHistory entity = new IndividualAuthorityHistory();
                        IndividualAuthorityHistory individualAuthorityHistory = individualAuthorityHistoryRepository.findAll(Example.of(IndividualAuthorityHistory.builder()
                                .userId(webUserApiRequestVO.getUserId())
                                .businessAuthorityApprovalState(Code.BIZ_AUTH_STATE.APPROVAL.enumCode)
                                .build()))
                                .stream().sorted(Comparator.comparing(IndividualAuthorityHistory::getCreateDateTime).reversed()).collect(Collectors.toList())
                                .get(0);

                        BeanUtils.copyProperties(individualAuthorityHistory, entity);
                        entity.setSequenceNo(null);
                        entity.setBusinessAuthorityApprovalState(Code.BIZ_AUTH_STATE.CANCEL_APPROVAL.enumCode);
                        individualAuthorityHistoryRepository.saveAndFlush(entity);

                        InstructorInfo instructorInfo = instructorInfoRepository.findAll(Example.of(InstructorInfo.builder()
                                .userId(webUserApiRequestVO.getUserId())
                                .build()))
                                .stream().sorted(Comparator.comparing(InstructorInfo::getCreateDateTime).reversed()).collect(Collectors.toList())
                                .get(0);

                        instructorInfo.setInstrStts(0);
                        instructorInfoRepository.saveAndFlush(instructorInfo);
                    } else {
                        OrganizationAuthorityHistory entity = new OrganizationAuthorityHistory();
                        OrganizationAuthorityHistory organizationAuthorityHistory = organizationAuthorityHistoryRepository.findAll(Example.of(OrganizationAuthorityHistory.builder()
                                        .userId(webUserApiRequestVO.getUserId())
                                        .businessAuthorityApprovalState(Code.BIZ_AUTH_STATE.APPROVAL.enumCode)
                                        .build()))
                                .stream().sorted(Comparator.comparing(OrganizationAuthorityHistory::getCreateDateTime).reversed()).collect(Collectors.toList())
                                .get(0);

                        BeanUtils.copyProperties(organizationAuthorityHistory, entity);
                        entity.setSequenceNo(null);
                        entity.setBusinessAuthorityApprovalState(Code.BIZ_AUTH_STATE.CANCEL_APPROVAL.enumCode);
                        organizationAuthorityHistoryRepository.saveAndFlush(entity);
                    }

                    WebUserApiResponseVO result = WebUserApiResponseVO.builder().build();
                    userMaster.setBusinessAuthority(null);
                    BeanUtils.copyProperties(lmsUserRepository.saveAndFlush(userMaster), result);
                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1003, "해당 회원 정보 미존재"));
    }

    /**
     * 웹 회원 잠금 해제
     *
     * @param userId
     * @return
     */
    public WebUserApiResponseVO unlockUserInfo(String userId) {
        return lmsUserRepository.findOne(Example.of(LmsUser.builder()
                        .userId(userId)
                        .build()))
                .map(userMaster -> {
                    WebUserApiResponseVO result = WebUserApiResponseVO.builder().build();
                    userMaster.setIsLock(false);
                    userMaster.setLockCount(0);
                    BeanUtils.copyProperties(lmsUserRepository.saveAndFlush(userMaster), result);

                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1003, "해당 회원 정보 미존재"));
    }

    /**
     * 기관 정보 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getOrganizationInfo(OrganizationViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonUserRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     회원정보 엑셀 다운로드
     */
    public <T> List<WebUserExcelVO> getExcel(WebUserViewRequestVO requestObject) {
        List<WebUserExcelVO> webUserExcelVOList = (List<WebUserExcelVO>) commonUserRepository.findEntityListExcel(requestObject);
        return webUserExcelVOList;
    }


    /**
     * 재직증명서 업로드
     *
     * @param userId
     * @param attachFile
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport fileUpload(String userId, MultipartFile attachFile) {
        /** 회원 확인 */
        LmsUser lmsUser = lmsUserRepository.findOne(Example.of(LmsUser.builder()
                        .userId(userId)
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1004, "해당 회원 미존재."));

        /** 파일 저장 및 파일 경로 셋팅 */
        Optional.ofNullable(attachFile)
                .ifPresent(file -> {
                    Path directoryPath = Paths.get(new StringBuilder(appConfig.getUploadFile().getUploadContextPath()).append(appConfig.getUploadFile().getUserFolder()).toString());
                    try {
                        Files.createDirectories(directoryPath);
                        try {
                            String imageSequence = new StringBuilder("_").append(new SimpleDateFormat("yyMMddHHmmss").format(new Date())).toString();

                            String attachFilepath = new StringBuilder(appConfig.getUploadFile().getUploadContextPath())
                                    .append(appConfig.getUploadFile().getUserFolder())
                                    .append("/")
                                    .append(userId + PROOF_EMPLOYMENT_IMG_TAG + imageSequence + "." + StringUtils.substringAfter(file.getOriginalFilename(), ".")).toString();
                            file.transferTo(new File(attachFilepath));
                            lmsUser.setAttachFilePath(attachFilepath.replace(appConfig.getUploadFile().getUploadContextPath(), ""));
                            lmsUser.setFileSize(file.getSize());
                        } catch (IOException e) {
                            throw new KPFException(KPF_RESULT.ERROR9005, "파일 업로드 실패");
                        }
                    } catch (IOException e2) {
                        throw new KPFException(KPF_RESULT.ERROR9005, "파일 경로 확인 또는, 생성 실패");
                    }
                });

        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

}
