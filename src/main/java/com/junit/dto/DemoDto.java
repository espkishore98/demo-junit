package com.junit.dto;

public class DemoDto {

	private String name;
	private Integer score;

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

	@Override
	public String toString() {
		return "DemoDto [name=" + name + ", score=" + score + "]";
	}

	public DemoDto(String name, Integer score) {
		super();
		this.name = name;
		this.score = score;
	}

	public DemoDto() {
		super();
		// TODO Auto-generated constructor stub
	}

}
