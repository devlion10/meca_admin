package kr.or.kpf.lms.biz.user.authority.service;

import kr.or.kpf.lms.biz.user.authority.vo.request.AuthorityApiRequestVO;
import kr.or.kpf.lms.biz.user.authority.vo.request.AuthorityViewRequestVO;
import kr.or.kpf.lms.biz.user.authority.vo.response.AuthorityApiResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonUserRepository;
import kr.or.kpf.lms.repository.entity.role.AdminRole;
import kr.or.kpf.lms.repository.entity.role.AdminRoleConnectedToRoleGroup;
import kr.or.kpf.lms.repository.entity.role.AdminRoleGroup;
import kr.or.kpf.lms.repository.role.AdminRoleConnectedToRoleGroupRepository;
import kr.or.kpf.lms.repository.role.AdminRoleGroupRepository;
import kr.or.kpf.lms.repository.role.AdminRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 사용자 관리 > 권한 관리 관련 Service
 */
@Service
@RequiredArgsConstructor
public class AuthorityService extends CSServiceSupport {

    /** 사용자 관리 공통 */
    private final CommonUserRepository commonUserRepository;
    /** 어드민 권한 */
    private final AdminRoleRepository adminRoleRepository;
    /** 어드민 권한 그룹 */
    private final AdminRoleGroupRepository adminRoleGroupRepository;
    /** 어드민 권한 그룹 & 권한 매핑 */
    private final AdminRoleConnectedToRoleGroupRepository adminRoleConnectedToRoleGroupRepository;

    /**
     * 권한 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(AuthorityViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonUserRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 권한 그룹 생성
     *
     * @param authorityApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public AuthorityApiResponseVO createAuthorityGroup(AuthorityApiRequestVO authorityApiRequestVO) {
        AdminRoleGroup entity = AdminRoleGroup.builder().build();
        entity.setRoleGroupCode(authorityApiRequestVO.getRoleGroupCode());

        if(adminRoleGroupRepository.findOne(Example.of(entity)).isPresent()) {
            throw new KPFException(KPF_RESULT.ERROR8002, "이미 존재하는 권한 그룹 코드.");
        } else {
            AuthorityApiResponseVO result = AuthorityApiResponseVO.builder().build();
            copyNonNullObject(authorityApiRequestVO, entity);

            BeanUtils.copyProperties(adminRoleGroupRepository.saveAndFlush(entity), result);
            return result;
        }
    }

    /**
     * 권한 그룹 업데이트
     *
     * @param authorityApiRequestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public AuthorityApiResponseVO updateAuthorityGroup(AuthorityApiRequestVO authorityApiRequestVO) {
        return adminRoleGroupRepository.findOne(Example.of(AdminRoleGroup.builder()
                        .roleGroupCode(authorityApiRequestVO.getRoleGroupCode())
                        .build()))
                .map(adminRoleGroup -> {
                    AuthorityApiResponseVO result = AuthorityApiResponseVO.builder().build();
                    copyNonNullObject(authorityApiRequestVO, adminRoleGroup);

                    BeanUtils.copyProperties(adminRoleGroupRepository.saveAndFlush(adminRoleGroup), result);
                    return result;
                }).orElseThrow(() -> new KPFException(KPF_RESULT.ERROR8004, "존재하지 않는 권한 그룹 정보."));
    }
}
