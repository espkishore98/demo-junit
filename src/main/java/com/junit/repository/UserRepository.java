package com.junit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.junit.entity.Users;

public interface UserRepository extends JpaRepository<Users, Long> {

	Users findByEmailId(String userName);

	Users findByExternalId(String externalId);

	Users getByExternalId(String externalId);

}
