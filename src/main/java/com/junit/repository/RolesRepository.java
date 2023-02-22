package com.junit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.junit.entity.Roles;

public interface RolesRepository extends JpaRepository<Roles, Long> {

}
