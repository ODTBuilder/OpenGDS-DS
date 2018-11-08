package com.gitrnd.qaproducer.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.gitrnd.qaproducer.user.domain.User;

@Mapper
public interface UserMapper {
	User retrieveUserById(String uid);

	User retrieveUserByIdx(int idx);

	void createUser(User user);

	User checkUserById(User user);

	User checkDuplicatedById(String uid);

	User checkDuplicatedByEmail(String email);

	User checkUserByEmail(User user);

	boolean setActiveUserById(User user);
}
