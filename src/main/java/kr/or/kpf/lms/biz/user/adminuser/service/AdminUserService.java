package kr.or.kpf.lms.biz.user.adminuser.service;

import kr.or.kpf.lms.biz.user.adminuser.vo.request.AdminUserApiRequestVO;
import kr.or.kpf.lms.biz.user.adminuser.vo.request.AdminUserViewRequestVO;
import kr.or.kpf.lms.biz.user.adminuser.vo.response.AdminUserApiResponseVO;
import kr.or.kpf.lms.biz.user.adminuser.vo.response.AdminUserExcelVO;
import kr.or.kpf.lms.common.encrypt.SecurityUtil;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonUserRepository;
import kr.or.kpf.lms.repository.user.AdminUserRepository;
import kr.or.kpf.lms.repository.entity.user.AdminUser;
import kr.or.kpf.lms.repository.entity.role.AdminRoleGroup;
import kr.or.kpf.lms.repository.role.AdminRoleGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 유저 정보 API 관련 Controller
 */
@Service
@RequiredArgsConstructor
public class AdminUserService extends CSServiceSupport {

    /** 사용자 관리 공통 */
    private final CommonUserRepository commonUserRepository;
    /** 어드민 계정 */
    private final AdminUserRepository adminUserRepository;
    /** 어드민 권한 그룹 */
    private final AdminRoleGroupRepository adminRoleGroupRepository;

    /**
     * 어드민 계정 정보 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(AdminUserViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonUserRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 회원 정보 생성
     *
     * @param adminUserApiRequestVO
     * @return
     */
    public AdminUserApiResponseVO createUserInfo(AdminUserApiRequestVO adminUserApiRequestVO) {
        AdminUser entity = AdminUser.builder().build();
        BeanUtils.copyProperties(adminUserApiRequestVO, entity);
        if (adminUserRepository.findOne(Example.of(AdminUser.builder()
                .userId(adminUserApiRequestVO.getUserId())
                .build())).isPresent()) {
            throw new KPFException(KPF_RESULT.ERROR1002, "동일 ID 존재");
        } else {
            AdminUserApiResponseVO result = AdminUserApiResponseVO.builder().build();

            if(!adminRoleGroupRepository.findOne(Example.of(AdminRoleGroup.builder().roleGroupCode(adminUserApiRequestVO.getRoleGroup()).build())).isPresent())
                throw new KPFException(KPF_RESULT.ERROR8004, "유효하지 않은 권한 그룹 코드");

            /** 비밀번호 암호화 */
            entity.setPassword(SecurityUtil.hashPassword(String.valueOf(adminUserApiRequestVO.getPassword()), adminUserApiRequestVO.getUserId()));
            BeanUtils.copyProperties(adminUserRepository.saveAndFlush(entity), result);
            return result;
        }
    }

    /**
     * 회원 정보 업데이트
     *
     * @param adminUserApiRequestVO
     * @return
     */
    public AdminUserApiResponseVO updateUserInfo(AdminUserApiRequestVO adminUserApiRequestVO) {
        return adminUserRepository.findOne(Example.of(AdminUser.builder()
                        .userId(adminUserApiRequestVO.getUserId())
                        .build()))
                .map(userMaster -> {
                    if(!adminRoleGroupRepository.findOne(Example.of(AdminRoleGroup.builder().roleGroupCode(adminUserApiRequestVO.getRoleGroup()).build())).isPresent())
                        throw new KPFException(KPF_RESULT.ERROR8004, "유효하지 않은 권한 그룹 코드");

                    copyNonNullObject(adminUserApiRequestVO, userMaster);

                    if (!adminUserApiRequestVO.isLock()) {
                        userMaster.setLockDateTime(null);
                        userMaster.setIsLock(false);
                        userMaster.setPasswordFailureCount(0);
                    }

                    AdminUserApiResponseVO result = AdminUserApiResponseVO.builder().build();
                    BeanUtils.copyProperties(adminUserRepository.saveAndFlush(userMaster), result);
                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1003, "해당 회원 정보 미존재"));
    }

    /**
     * 회원 정보 삭제(회원 탈퇴)
     *
     * @param adminUserApiRequestVO
     * @return
     */
    public CSResponseVOSupport deleteUserInfo(AdminUserApiRequestVO adminUserApiRequestVO) {
        adminUserRepository.delete(adminUserRepository.findOne(Example.of(AdminUser.builder()
                        .userId(adminUserApiRequestVO.getUserId())
                        .password(adminUserApiRequestVO.getPassword())
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1004, "탈퇴 대상 회원 조회 실패")));
        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }

    /**
     * 비밀번호 변경 (비밀번호 분실 시...)
     *
     * @param adminUserApiRequestVO
     * @return
     */
    public CSResponseVOSupport changePassword(AdminUserApiRequestVO adminUserApiRequestVO) {
        return adminUserRepository.findOne(Example.of(AdminUser.builder()
                        .userId(adminUserApiRequestVO.getUserId())
                        .build()))
                .map(userMaster -> {
                    /** 비밀번호 암호화 */
                    userMaster.setPassword(SecurityUtil.hashPassword(String.valueOf(adminUserApiRequestVO.getPassword()), adminUserApiRequestVO.getUserId()));
                    adminUserRepository.saveAndFlush(userMaster);

                    return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1003, "해당 회원 정보 미존재"));
    }

    /**
     관리자 정보 엑셀 다운로드
     */
    public <T> List<AdminUserExcelVO> getExcel(AdminUserViewRequestVO requestObject) {
        List<AdminUserExcelVO> adminUserExcelVOList = (List<AdminUserExcelVO>) commonUserRepository.findEntityListExcel(requestObject);
        return adminUserExcelVOList;
    }
}
