package com.junit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.junit.entity.Demo;

import java.util.List;

public interface DemoRepository extends JpaRepository<Demo, Long> {

    Demo findByName(String sai);
}
