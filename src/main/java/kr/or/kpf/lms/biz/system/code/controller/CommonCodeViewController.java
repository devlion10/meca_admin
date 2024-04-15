package kr.or.kpf.lms.biz.system.code.controller;

import kr.or.kpf.lms.biz.system.code.service.CommonCodeService;
import kr.or.kpf.lms.biz.system.code.vo.request.CommonCodeViewRequestVO;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.config.security.KoreaPressFoundationUser;
import kr.or.kpf.lms.config.security.vo.KoreaPressFoundationUserDetails;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Optional;

/**
 * 시스템 관리 > 공통 코드 관리 View 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/system/common-code")
public class CommonCodeViewController extends CSViewControllerSupport {

    private static final String SYSTEM = "views/system/";

    private final CommonCodeService commonCodeService;

    /**
     * 시스템 관리 > 공통 코드 관리 목록 조회
     * 
     * @param request
     * @param generalUser
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path={"", "/page"})
    public String getList(HttpServletRequest request,
                          @KoreaPressFoundationUser KoreaPressFoundationUserDetails generalUser,
                          Pageable pageable, Model model) {
        try{
            modelSetting(model, Optional.ofNullable(CSSearchMap.of(request))
                    .map(searchMap -> commonCodeService.getList((CommonCodeViewRequestVO) params(CommonCodeViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
            return new StringBuilder(SYSTEM).append("commonCode").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }
}
