package com.junit.dto;

import java.util.HashSet;
import java.util.Set;

public class UserRequestDTO {

	private String externalId;
	private String userName;
	private String password;
	private String emailId;

	private Long phoneNumber;

	private Set<Long> roles = new HashSet<>();

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Set<Long> getRoles() {
		return roles;
	}

	public void setRoles(Set<Long> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "UserRequestDTO [externalId=" + externalId + ", userName=" + userName + ", password=" + password
				+ ", emailId=" + emailId + ", phoneNumber=" + phoneNumber + ", roles=" + roles + "]";
	}

	public UserRequestDTO(String externalId, String userName, String password, String emailId, Long phoneNumber,
			Set<Long> roles) {
		super();
		this.externalId = externalId;
		this.userName = userName;
		this.password = password;
		this.emailId = emailId;
		this.phoneNumber = phoneNumber;
		this.roles = roles;
	}

}
