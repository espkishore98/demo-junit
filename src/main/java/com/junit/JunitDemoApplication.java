package com.junit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JunitDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(JunitDemoApplication.class, args);
	}

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(JunitDemoApplication.class);
//	}

}
