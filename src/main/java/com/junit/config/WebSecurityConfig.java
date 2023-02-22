package com.junit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.junit.security.CustomUserDetailsService;
import com.junit.security.JwtAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private static final String[] AUTH_WHITELIST = { "/", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**",
			"/register", "/login", "**/v2/api-docs" };

	private CustomUserDetailsService customUserDetailsService;
	private JwtAuthenticationEntryPoint unauthorizedHandler;

//	@Autowired
//	public WebSecurityConfig(CustomUserDetailsService customUserDetailsService,
//			JwtAuthenticationEntryPoint unauthorizedHandler) {
//		this.customUserDetailsService = customUserDetailsService;
//		this.unauthorizedHandler = unauthorizedHandler;
//	}
//
//	public WebSecurityConfig(boolean disableDefaults, CustomUserDetailsService customUserDetailsService,
//			JwtAuthenticationEntryPoint unauthorizedHandler) {
//		super(disableDefaults);
//		this.customUserDetailsService = customUserDetailsService;
//		this.unauthorizedHandler = unauthorizedHandler;
//	}
//
//	@Bean
//	public JwtAuthenticationFilter jwtAuthenticationFilter() {
//		return new JwtAuthenticationFilter();
//	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.inMemoryAuthentication().withUser("kishore").password("{noop}Kishore1998*")
				.roles("USER").and().withUser("admin").password("{noop}Kishore1998*").credentialsExpired(false)
				.accountExpired(false).accountLocked(false).authorities("WRITE_PRIVILEGES", "READ_PRIVILEGES")
				.roles("ADMIN");
		// .userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
	}

//	@Bean(BeanIds.AUTHENTICATION_MANAGER)
//	@Override
//	public AuthenticationManager authenticationManagerBean() throws Exception {
//		return super.authenticationManagerBean();
//	}
//
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll().antMatchers("/demo/**")
//				.hasAnyAuthority("USER", "ADMIN").antMatchers("/admin").hasAuthority("ADMIN").anyRequest()
//				.authenticated().and().httpBasic();
		http.csrf().disable().authorizeRequests().antMatchers("/demo/**").hasAnyRole("USER").anyRequest()
				.authenticated().and().httpBasic().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

//	@Override
//	public void configure(WebSecurity web) throws Exception {
//		web.ignoring().antMatchers("/static/css/**, /static/js/**, *.ico");
//
//		// swagger
//		web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security",
//				"/swagger-ui.html", "/webjars/**", "/swagger/**", "/register", "/login");
//	}

}
