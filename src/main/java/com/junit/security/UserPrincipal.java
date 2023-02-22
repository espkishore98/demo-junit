package com.junit.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.junit.entity.Roles;
import com.junit.entity.Users;

public class UserPrincipal implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Users users;
	private String emailId;
	private String userName;
	@JsonIgnore
	private String password;

//	private static Collection<? extends GrantedAuthority> authority;
	private Collection<? extends GrantedAuthority> authorities;

	public UserPrincipal(Long id, String emailId, String password, Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.emailId = emailId;
		this.password = password;
		this.authorities = authorities;
	}

	public UserPrincipal() {
		super();
	}

	public UserDetails create(Users user) {
		this.users = user;
//		return new UserPrincipal(user.getId(), user.getEmailId(), user.getPassword(), getAuthorities());

		return new User(user.getEmailId(), user.getPassword(), getAuthorities());
	}

	public Long getId() {
		return id;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Roles> roles = users.getRoles();
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();

		for (Roles role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
		}
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserPrincipal that = (UserPrincipal) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {

		return Objects.hash(id);
	}
}
