package com.gitrnd.qaproducer.common.security;

import org.springframework.security.core.authority.AuthorityUtils;

import lombok.Getter;
import lombok.Setter;

public class LoginUser extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private int idx;
	@Getter
	@Setter
	private String email;
	@Getter
	@Setter
	private String fname;
	@Getter
	@Setter
	private String lname;
	@Getter
	@Setter
	private int aid;
	@Getter
	@Setter
	private Boolean active;

	public LoginUser(com.gitrnd.qaproducer.user.domain.User user) {
		super(user.getUid(), user.getPw(), AuthorityUtils.createAuthorityList(user.getAuth()));
		this.idx = user.getIdx();
		this.active = user.getActive();
		this.email = user.getEmail();
		this.fname = user.getFname();
		this.lname = user.getLname();
		this.aid = user.getAid();
	}
}
