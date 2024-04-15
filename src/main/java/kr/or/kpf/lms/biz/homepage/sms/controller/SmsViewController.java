package kr.or.kpf.lms.biz.homepage.sms.controller;

import kr.or.kpf.lms.biz.homepage.sms.service.SmsService;
import kr.or.kpf.lms.biz.homepage.sms.vo.request.SmsViewRequestVO;
import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Optional;

/**
 * 홈페이지 관리 > SMS 발송 관리 View 관련 Controller
 */
@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/homepage/sms")
public class SmsViewController extends CSViewControllerSupport {

    private static final String SMS = "views/homepage/";

    private final SmsService smsService;

    /**
     * 홈페이지 관리 > SMS발송 관리 조회
     *
     * @param request
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(path={"", "/page"})
    public String getSmsList(HttpServletRequest request, Pageable pageable, Model model) {
        try {
            modelSetting(model, Optional.of(CSSearchMap.of(request))
                    .map(searchMap -> smsService.getList((SmsViewRequestVO) params(SmsViewRequestVO.class, searchMap, pageable)))
                    .orElse(CSPageImpl.of(new ArrayList<>(), pageable, 0)));
            return new StringBuilder(SMS).append("sms").toString();
        } catch (KPFException e1) {
            model.addAttribute("errorMessage", e1.getMessage());
            return "views/error/errorForm";
        } catch (Exception e2) {
            model.addAttribute("errorMessage", "알 수 없는 에러가 발생하였습니다.");
            return "views/error/errorForm";
        }
    }

    /**
     * 홈페이지 관리 > SMS발송 관리 등록
     */
    @GetMapping(path={"/"})
    public String getWrite() {
        return new StringBuilder(SMS).append("smsForm").toString();
    }
}
