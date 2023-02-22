package com.junit.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junit.constants.Constants;
import com.junit.dto.AuthUser;
import com.junit.dto.JwtAuthenticationResponse;
import com.junit.dto.LoginRequest;
import com.junit.dto.UserRequestDTO;
import com.junit.entity.Roles;
import com.junit.entity.Users;
import com.junit.repository.RolesRepository;
import com.junit.repository.UserRepository;
import com.junit.security.JwtTokenProvider;
import com.nimbusds.jwt.SignedJWT;

@Service
public class UserService implements IUserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	private AuthenticationManager authenticationManager;
	private PasswordEncoder passwordEncoder;
	private JwtTokenProvider tokenProvider;
	@Autowired
	UserRepository userRepo;
	@Autowired
	RolesRepository rolesRepo;

//	@Autowired
//	public UserService(AuthenticationManager authenticationManager, UserRepository userRepository,
//			PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
//		this.authenticationManager = authenticationManager;
//		this.userRepo = userRepository;
//		this.passwordEncoder = passwordEncoder;
//		this.tokenProvider = tokenProvider;
//	}

	public static String getUserExternalId(String authToken) {

		// String token = resolveToken(authToken);
		String token = authToken;
		if (token == null) {
			return Constants.UserConstants.UNAUTHORIZED_USER;
		}
		SignedJWT signedJWT;
		ObjectMapper mapper = new ObjectMapper();
		try {
			signedJWT = SignedJWT.parse(token);
			String object = signedJWT.getPayload().toJSONObject().toJSONString();
			AuthUser user = mapper.readValue(object, AuthUser.class);
			if (System.currentTimeMillis() > user.getSub()) {
				return Constants.UserConstants.UNAUTHORIZED_USER;
			} else {
				return user.getJti();
			}
		} catch (ParseException | JsonProcessingException jpe) {
			LOGGER.info(jpe.getMessage() + " at " + Calendar.getInstance().getTime());
			LOGGER.error(jpe.getMessage(), jpe);

		} catch (Exception e) {
			LOGGER.info(e.getMessage() + " at " + Calendar.getInstance().getTime());
			LOGGER.error(e.getMessage(), e);
		}

		return Constants.UserConstants.UNAUTHORIZED_USER;
	}

	public static String getUserExternalId(HttpServletRequest authToken) {
		LOGGER.info("get user data from token");
		String token = resolveToken(authToken);
		if (token == null) {
			return Constants.UserConstants.UNAUTHORIZED_USER;
		}
		SignedJWT signedJWT;
		ObjectMapper mapper = new ObjectMapper();
		try {
			signedJWT = SignedJWT.parse(token);
			String object = signedJWT.getPayload().toJSONObject().toJSONString();
			LOGGER.info("jwt payload", object);
			AuthUser user = mapper.readValue(object, AuthUser.class);
			if (System.currentTimeMillis() > user.getSub()) {
				return Constants.UserConstants.UNAUTHORIZED_USER;
			} else {
				return user.getJti();
			}
		} catch (ParseException | JsonProcessingException jpe) {
			LOGGER.info(jpe.getMessage() + " at " + Calendar.getInstance().getTime());
			LOGGER.error(jpe.getMessage(), jpe);

		} catch (Exception e) {
			LOGGER.info(e.getMessage() + " at " + Calendar.getInstance().getTime());
			LOGGER.error(e.getMessage(), e);
		}

		return Constants.UserConstants.UNAUTHORIZED_USER;
	}

	public static String resolveToken(HttpServletRequest req) {
		String bearerToken = req.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}

	@Override
	public ResponseEntity<String> register(UserRequestDTO user) {
		if (user.getEmailId() == null) {
			return new ResponseEntity<String>(Constants.UserConstants.USER_EMAIL_MANDATORY, HttpStatus.BAD_REQUEST);
		}
		if (user.getUserName() == null) {
			return new ResponseEntity<String>(Constants.UserConstants.USER_NAME_MANDATORY, HttpStatus.BAD_REQUEST);
		}
		if (user.getPassword() == null) {
			return new ResponseEntity<String>(Constants.UserConstants.PASSWORD_MANDATORY, HttpStatus.BAD_REQUEST);
		}
		if (user.getEmailId() != null) {
			Users fetchedUser = userRepo.findByEmailId(user.getEmailId());
			if (fetchedUser != null) {
				return new ResponseEntity<String>(Constants.UserConstants.USER_WITH_EMAIL_ALREADY_PRESENT,
						HttpStatus.BAD_REQUEST);
			}
		}
		try {
			Users userDtls = null;
			if (user.getExternalId() != null) {
				userDtls = userRepo.getByExternalId(user.getExternalId());
			} else {
				userDtls = new Users();
				userDtls.setExternalId(UUID.randomUUID().toString());
			}

			Set<Roles> roles = user.getRoles().stream().map(role -> rolesMap(role)).collect(Collectors.toSet());
			userDtls.setRoles(roles);
			userDtls.setEmailId(user.getEmailId());
			userDtls.setPassword(passwordEncoder.encode(user.getPassword()));
			userDtls.setPhoneNumber(user.getPhoneNumber());
			userDtls.setUserName(user.getUserName());
			Users saveduser = userRepo.saveAndFlush(userDtls);

		} catch (Exception e) {
			LOGGER.error("error while saving", e.getMessage());
			return new ResponseEntity<String>(Constants.ExceptionConstants.SOMETHING_WENT_WRONG,
					HttpStatus.INTERNAL_SERVER_ERROR);

		}

		return new ResponseEntity<String>(Constants.SuccessConstants.REGISTRATION_SUCCESS, HttpStatus.OK);
	}

	private Roles rolesMap(Long role) {
		Roles roleDtls = rolesRepo.getById(role);
		if (roleDtls != null) {
			return roleDtls;
		}
		return null;
	}

	@Override
	public ResponseEntity<Object> loginUser(LoginRequest request) {
		LOGGER.info("login user started----->");
		Users user = userRepo.findByEmailId(request.getEmailId());

		String jwt = "";
		try {
			if (user == null) {
				return new ResponseEntity<Object>(Constants.UserConstants.USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
			}
			if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
				return new ResponseEntity<Object>("Invalid Password", HttpStatus.BAD_REQUEST);
			}
			List<SimpleGrantedAuthority> authorities = new ArrayList<>();

			for (Roles role : user.getRoles()) {
				authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
			}

			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(user.getEmailId(), request.getPassword(), authorities));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			jwt = tokenProvider.generateToken(authentication, user);
			LOGGER.info("login ended ===>", jwt);
		} catch (Exception e) {
			LOGGER.error("something went wrong ===>", e.getMessage());
			return new ResponseEntity<Object>("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Object>(
				new JwtAuthenticationResponse(jwt, "Bearer", user.getUserName(), user.getExternalId()), HttpStatus.OK);

	}

}
