package com.junit.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.junit.entity.Users;
import com.junit.repository.UserRepository;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userName) {
		Users user = userRepository.findByEmailId(userName);
		UserPrincipal userPrincipal = new UserPrincipal();
		return userPrincipal.create(user);
	}

	public UserDetails loadUserByExternalId(String externalId) {
		// TODO Auto-generated method stub
		Users user = userRepository.findByExternalId(externalId);
		UserPrincipal userPrincipal = new UserPrincipal();
		return userPrincipal.create(user);
	}

}
