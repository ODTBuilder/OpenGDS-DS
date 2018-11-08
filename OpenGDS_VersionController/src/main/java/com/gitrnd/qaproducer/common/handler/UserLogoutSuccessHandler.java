package com.gitrnd.qaproducer.common.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class UserLogoutSuccessHandler implements LogoutSuccessHandler {
	@Override
	public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Authentication authentication) throws IOException, ServletException {
		if (authentication != null && authentication.getDetails() != null) {
			try {
				httpServletRequest.getSession().invalidate();
				System.out.println("User Successfully Logout");
				// you can add more codes here when the user successfully logs
				// out,
				// such as updating the database for last active.

				// Geoserver Session 삭제
				HttpSession httpSession = httpServletRequest.getSession();
				httpSession.removeAttribute("geoserver");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String URL = httpServletRequest.getContextPath() + "/main.do";
		httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		httpServletResponse.sendRedirect(URL);
	}
}
