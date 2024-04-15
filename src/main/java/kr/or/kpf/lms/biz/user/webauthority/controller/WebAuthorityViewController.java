package kr.or.kpf.lms.biz.user.webauthority.controller;

import io.swagger.v3.oas.annotations.Parameter;
import kr.or.kpf.lms.biz.user.webauthority.service.WebAuthorityService;
import kr.or.kpf.lms.biz.user.webauthority.vo.request.WebAuthorityViewRequestVO;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.common.support.Code;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import kr.or.kpf.lms.repository.entity.user.LmsUser;
import kr.or.kpf.lms.repository.user.LmsUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * 사용자 관리 > 권한 관리 View 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/user/web-authority")
public class WebAuthorityViewController extends CSViewControllerSupport {
    private static final String AUTHORITY = "views/user/web/";
    private final WebAuthorityService webAuthorityService;
    private final LmsUserRepository lmsUserRepository;

    /**
     * 사용자 관리 > 권한 관리 > 회원 권한 (사업 참여 권한) 목록 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path = {"", "/page"})
    public String getList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            Long countAgency = lmsUserRepository.findAll(Example.of(LmsUser.builder()
                    .businessAuthority(Code.BIZ_AUTH.AGENCY.enumCode)
                    .build())).stream().count();
            model.addAttribute("countAgency", countAgency);

            Long countSchool = lmsUserRepository.findAll(Example.of(LmsUser.builder()
                    .businessAuthority(Code.BIZ_AUTH.SCHOOL.enumCode)
                    .build())).stream().count();
            model.addAttribute("countSchool", countSchool);

            Long countInstructor = lmsUserRepository.findAll(Example.of(LmsUser.builder()
                    .businessAuthority(Code.BIZ_AUTH.INSTR.enumCode)
                    .build())).stream().count();
            model.addAttribute("countInstructor", countInstructor);

            modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                .map(searchMap -> webAuthorityService.getList((WebAuthorityViewRequestVO) params(WebAuthorityViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("CON_DATE_TYPE", "BIZ_AUTH", "BIZ_AUTH_STATE"));
            return new StringBuilder(AUTHORITY).append("userAuthority").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }
    
    /**
     * 사용자 관리 > 권한 관리 > 회원 권한 (사업 참여 권한) 목록 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path = {"/{sequenceNo}/{userId}"})
    public String getForm(HttpServletRequest request, Pageable pageable, Model model,
                          @Parameter(description = "일련 번호") @PathVariable(value = "sequenceNo", required = true) BigInteger sequenceNo,
                          @Parameter(description = "사용자 아이디") @PathVariable(value = "userId", required = true) String userId) {
        try {
            CSSearchMap requestParam = CSSearchMap.of(request);
            requestParam.put("sequenceNo", sequenceNo);
            requestParam.put("userId", userId);
            modelSetting(model, Optional.ofNullable(requestParam)
                .map(searchMap -> webAuthorityService.getList((WebAuthorityViewRequestVO) params(WebAuthorityViewRequestVO.class, searchMap, pageable)))
                .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)), Arrays.asList("CON_DATE_TYPE", "BIZ_AUTH", "BIZ_AUTH_STATE"));
            return new StringBuilder(AUTHORITY).append("userAuthorityForm").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }
}
