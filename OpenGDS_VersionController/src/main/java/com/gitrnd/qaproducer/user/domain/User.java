package com.gitrnd.qaproducer.user.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class User {
	private int idx;
	private String uid;
	private String pw;
	private String email;
	private String auth;
	private String fname;
	private String lname;
	private int aid;
	private Boolean active;
}
