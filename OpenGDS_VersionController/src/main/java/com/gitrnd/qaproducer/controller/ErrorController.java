package com.gitrnd.qaproducer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gitrnd.qaproducer.common.exception.ValidationAuthException;
import com.gitrnd.qaproducer.common.security.LoginUser;


@Controller
@RequestMapping(value = "/error")
public class ErrorController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);

	@RequestMapping(value = "/404")
	public ModelAndView error404(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LOGGER.info("error 404");
		ModelAndView mav = new ModelAndView();
		mav.addObject("errorcode", "404 Not found");
		mav.addObject("errormessage", "페이지를 찾을 수 없습니다.");
		mav.setViewName("/error/error");
		return mav;
	}

	@RequestMapping(value = "/403")
	public ModelAndView error403(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LOGGER.info("error 403");
		ModelAndView mav = new ModelAndView();
		mav.addObject("errorcode", "403 Forbidden");
		mav.addObject("errormessage", "페이지 접근 권한이 없습니다. 관리자에게 문의하세요.");
		mav.setViewName("/error/error");
		return mav;
	}

	@RequestMapping(value = "/401")
	public ModelAndView error401(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LOGGER.info("error 401");
		ModelAndView mav = new ModelAndView();
		mav.addObject("errorcode", "401 Unauthorized");
		mav.addObject("errormessage", "페이지 접근 권한이 없습니다. 관리자에게 문의하세요.");
		mav.setViewName("/error/error");
		return mav;
	}

	@RequestMapping(value = "/500")
	public ModelAndView error500(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LOGGER.info("error 500");
		ModelAndView mav = new ModelAndView();
		mav.addObject("errorcode", "500 Internal server error");
		mav.addObject("errormessage", "요청을 처리중 오류가 발생했습니다. 다시 시도해 주세요.");
		mav.setViewName("/error/error");
		return mav;
	}

	@ExceptionHandler(ValidationAuthException.class)
	public void errorValidationAuth(HttpServletRequest request, HttpServletResponse response,
			@AuthenticationPrincipal LoginUser loginUser, Exception exception) throws Exception {
		LOGGER.info("error validation auth");
		response.setStatus(500);
		response.sendError(500, "해당 검수의 요청 권한이 없습니다.");
	}
}
