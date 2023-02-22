package com.junit.service;

import org.springframework.http.ResponseEntity;

import com.junit.dto.LoginRequest;
import com.junit.dto.UserRequestDTO;

public interface IUserService {

	ResponseEntity<String> register(UserRequestDTO user);

	ResponseEntity<Object> loginUser(LoginRequest request);

}
