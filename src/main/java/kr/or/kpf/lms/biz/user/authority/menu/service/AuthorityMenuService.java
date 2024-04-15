package kr.or.kpf.lms.biz.user.authority.menu.service;

import kr.or.kpf.lms.biz.education.reference.vo.response.ReferenceRoomApiResponseVO;
import kr.or.kpf.lms.biz.user.authority.menu.vo.request.AuthorityMenuApiRequestVO;
import kr.or.kpf.lms.biz.user.authority.menu.vo.request.AuthorityMenuViewRequestVO;
import kr.or.kpf.lms.biz.user.authority.menu.vo.response.AuthorityMenuApiResponseVO;
import kr.or.kpf.lms.biz.user.authority.vo.request.AuthorityApiRequestVO;
import kr.or.kpf.lms.biz.user.authority.vo.request.AuthorityViewRequestVO;
import kr.or.kpf.lms.biz.user.authority.vo.response.AuthorityApiResponseVO;
import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.common.support.CSResponseVOSupport;
import kr.or.kpf.lms.common.support.CSServiceSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.repository.CommonUserRepository;
import kr.or.kpf.lms.repository.entity.BizSurveyQitemItem;
import kr.or.kpf.lms.repository.entity.education.CurriculumReferenceRoom;
import kr.or.kpf.lms.repository.entity.role.AdminRoleGroup;
import kr.or.kpf.lms.repository.entity.role.AdminRoleMenu;
import kr.or.kpf.lms.repository.role.AdminRoleConnectedToRoleGroupRepository;
import kr.or.kpf.lms.repository.role.AdminRoleGroupRepository;
import kr.or.kpf.lms.repository.role.AdminRoleMenuRepository;
import kr.or.kpf.lms.repository.role.AdminRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 사용자 관리 > 권한 관리 관련 Service
 */
@Service
@RequiredArgsConstructor
public class AuthorityMenuService extends CSServiceSupport {
    private final CommonUserRepository commonUserRepository;
    /** 어드민 권한 메뉴 */
    private final AdminRoleMenuRepository adminRoleMenuRepository;

    /**
     * 권한 조회
     *
     * @param requestObject
     * @return
     * @param <T>
     */
    public <T> Page<T> getList(AuthorityMenuViewRequestVO requestObject) {
        return (Page<T>) Optional.ofNullable(commonUserRepository.findEntityList(requestObject))
                .orElse(CSPageImpl.of(new ArrayList<>(), requestObject.getPageable(), 0));
    }

    /**
     * 권한 메뉴 생성
     *
     * @param requestVOs
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public AuthorityMenuApiResponseVO createInfo(List<AuthorityMenuApiRequestVO> requestVOs) {
        AuthorityMenuApiResponseVO result = AuthorityMenuApiResponseVO.builder().build();
        for (AuthorityMenuApiRequestVO requestVO : requestVOs) {
            AdminRoleMenu entity = AdminRoleMenu.builder().build();
            copyNonNullObject(requestVO, entity);
            BeanUtils.copyProperties(adminRoleMenuRepository.saveAndFlush(entity), result);
        }
        return result;
    }

    /**
     * 권한 메뉴 삭제
     *
     * @param requestVO
     * @return
     */
    @Transactional(rollbackOn = {Exception.class})
    public CSResponseVOSupport deleteInfo(AuthorityMenuApiRequestVO requestVO) {
        List<AdminRoleMenu> adminRoleMenus = adminRoleMenuRepository.findAll(Example.of(AdminRoleMenu.builder()
                .roleGroupCode(requestVO.getRoleGroupCode())
                .build()));
        adminRoleMenuRepository.deleteAll(adminRoleMenus);
        return CSResponseVOSupport.of(KPF_RESULT.SUCCESS);
    }
}
