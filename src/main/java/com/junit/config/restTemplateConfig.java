package com.junit.config;

import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@Configuration
public class restTemplateConfig {

	@Bean(name = "weather")
	@Primary
	public RestTemplate weatherRestTemplate(RestTemplateBuilder builder) {

		return builder.setConnectTimeout(Duration.ofSeconds(500)).setReadTimeout(Duration.ofSeconds(500)).build();
	}

	@Bean(name = "test")
	public RestTemplate testRestTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
}
