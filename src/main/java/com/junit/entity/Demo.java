package com.junit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "demo")
public class Demo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "name")
	@NotBlank(message = "name can't be null")
	private String name;

	@Column(name = "score")
	private Integer score;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {

		this.name = name;

	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Demo(Long id, String name, Integer score) {
		this.id = id;
		this.name = name;
		this.score = score;
	}

	public Demo(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Demo() {
		super();
		// TODO Auto-generated constructor stub
	}

}
