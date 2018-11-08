package com.gitrnd.qaproducer.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gitrnd.qaproducer.user.domain.User;
import com.gitrnd.qaproducer.user.repository.UserRepository;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Transactional(readOnly = true)
	public User retrieveUserById(String uid) {
		return userRepository.retrieveUserById(uid);
	}

	@Transactional(readOnly = true)
	public User retrieveUserByIdx(int idx) {
		return userRepository.retrieveUserByIdx(idx);
	}

	@Transactional
	public void createUser(User user) {
		userRepository.createUser(user);
	}

	@Transactional(readOnly = true)
	public User checkUserById(User user) {
		return userRepository.checkUserById(user);
	}

	@Transactional(readOnly = true)
	public User checkUserByEmail(User user) {
		return userRepository.checkUserByEmail(user);
	}

	@Transactional(readOnly = true)
	public User checkDuplicatedById(String uid) {
		return userRepository.checkDuplicatedById(uid);
	}

	@Transactional(readOnly = true)
	public User checkDuplicatedByEmail(String email) {
		return userRepository.checkDuplicatedByEmail(email);
	}

	@Transactional
	public boolean setActiveUserById(User user) {
		return userRepository.setActiveUserById(user);
	}

}
