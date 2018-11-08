package com.gitrnd.qaproducer.user.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gitrnd.qaproducer.user.domain.User;
import com.gitrnd.qaproducer.user.mapper.UserMapper;

@Repository
public class UserRepository {

	@Autowired
	private UserMapper userMapper;

	public User retrieveUserById(String uid) {
		return userMapper.retrieveUserById(uid);
	}

	public User retrieveUserByIdx(int idx) {
		return userMapper.retrieveUserByIdx(idx);
	}

	public void createUser(User user) {
		userMapper.createUser(user);
	}

	public User checkUserById(User user) {
		return userMapper.checkUserById(user);
	}

	public User checkDuplicatedById(String uid) {
		return userMapper.checkDuplicatedById(uid);
	}

	public User checkUserByEmail(User user) {
		return userMapper.checkUserByEmail(user);
	}

	public User checkDuplicatedByEmail(String email) {
		return userMapper.checkDuplicatedByEmail(email);
	}

	public boolean setActiveUserById(User user) {
		return userMapper.setActiveUserById(user);
	}
}
