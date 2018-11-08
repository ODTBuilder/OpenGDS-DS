package com.gitrnd.qaproducer.common.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gitrnd.qaproducer.common.security.LoginUser;
import com.gitrnd.qaproducer.user.domain.User;
import com.gitrnd.qaproducer.user.service.UserService;

@Service
public class LoginUserDetailsService implements UserDetailsService {

	@Autowired
	UserService userService;

	@Override
	public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {
		// 기존해시와 신규해시가 다를경우 이런식으로 받아 처리할 수 있음.
		// 위 @Autowired HttpServletRequest request;
		// request 처리

		User user = userService.retrieveUserById(uid);

		if (user == null) {
			// 계정이 존재하지 않음
			throw new UsernameNotFoundException("login fail");
		}

		return new LoginUser(user);
	}

}
