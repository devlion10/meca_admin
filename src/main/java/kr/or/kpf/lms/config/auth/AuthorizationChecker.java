package kr.or.kpf.lms.config.auth;

import kr.or.kpf.lms.common.result.KPF_RESULT;
import kr.or.kpf.lms.config.security.vo.KoreaPressFoundationUserDetails;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.repository.entity.role.AdminRoleMenu;
import kr.or.kpf.lms.repository.entity.user.AdminUser;
import kr.or.kpf.lms.repository.role.AdminRoleMenuRepository;
import kr.or.kpf.lms.repository.user.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthorizationChecker {
    @Autowired private AdminRoleMenuRepository adminRoleMenuRepository;
    @Autowired private AdminUserRepository adminUserRepository;

    public boolean check(HttpServletRequest request, Authentication authentication) {
        /** 비로그인 */
        if (authentication.getPrincipal().equals("anonymousUser")) {
            return false;
        }

        /** 사용자 정보 생성 여부 확인 */
        KoreaPressFoundationUserDetails user = (KoreaPressFoundationUserDetails) authentication.getPrincipal();
        if (!(user instanceof KoreaPressFoundationUserDetails)) {
            return false;
        }

        /** 권한별 접근 메뉴 리스트 호출 및 AntPathMatcher로 패턴 비교 하여 권한 리스트 생성 */
        String authority = null;
        List<AdminRoleMenu> adminRoleMenus = adminRoleMenuRepository.findAll(Example.of(AdminRoleMenu.builder()
                .roleGroupCode(user.getRoleGroup())
                .build())).stream().filter(data -> data.getUri() != null).collect(Collectors.toList());
        for (AdminRoleMenu adminRoleMenu : adminRoleMenus) {
            String[] uris = new String[2];
            String uri = "0";

            if (adminRoleMenu.getUri().contains("?")) {
                uris = adminRoleMenu.getUri().split("\\?");
                uri = new StringBuilder(uris[0]).append("/**").toString();
            } else {
                uri = new StringBuilder(adminRoleMenu.getUri()).append("/**").toString();
            }

            if (new AntPathMatcher().match(uri, request.getRequestURI())) {
                authority = adminRoleMenu.getRoleGroupCode();
                break;
            }
        }

        /** 권한 부여 */
        String userId = user.getUserId();
        AdminUser loggedUser = adminUserRepository.findOne(Example.of(AdminUser.builder()
                        .userId(userId)
                        .build()))
                .orElseThrow(() -> new KPFException(KPF_RESULT.ERROR1004, "회원 정보 조회에 실패하였습니다."));

        List<String> authorities = new ArrayList<>();
        authorities.add(loggedUser.getRoleGroup());
        if (authority == null || !authorities.contains(authority)) {
            return false;
        }
        return true;
    }
}
