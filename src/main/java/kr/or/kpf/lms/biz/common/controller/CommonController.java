package kr.or.kpf.lms.biz.common.controller;

import javax.servlet.http.HttpServletRequest;

import kr.or.kpf.lms.biz.business.apply.vo.request.BizAplyViewRequestVO;
import kr.or.kpf.lms.common.encrypt.SecurityUtil;
import kr.or.kpf.lms.framework.exception.KPFException;
import kr.or.kpf.lms.framework.model.CSPageImpl;
import kr.or.kpf.lms.framework.model.CSSearchMap;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.or.kpf.lms.common.support.CSViewControllerSupport;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * 공통 Controller (View)
 */
@Controller
@RequiredArgsConstructor
public class CommonController extends CSViewControllerSupport {

	@GetMapping(path = {"/"})
	public ModelAndView loginForm(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();

		if (principal instanceof User) { /** 로그인이 되어 있다면 메인 홈 화면*/
			mv.setViewName("views/common/homeForm");
		} else { /** 로그인이 되어 있지 않다면 로그인 화면*/
			mv.setViewName("views/login/loginForm");			
		}
		return mv;
	}

	@GetMapping(path={"/access-denied"})
	public String pageAccessDenied() {
		return "views/error/errorAuth";
	}

}
