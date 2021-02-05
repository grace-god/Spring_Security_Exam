package com.cos.security1.config;
//oauth2 과정
//1.코드받기(인증), 2.엑세스토큰(권한), 3.사용자프로필 정보를 가져오고, 
//4-1그 정보를 토대로 자동으로 회원가입을 자동으로 진행시키기도 함
//4-2(이메일, 전화번호, 이름, 아이디) 쇼핑몰 -> (집주소), 백화점 ->(vip등급, 일반등급)

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cos.security1.config.oauth.PrincipalOauth2sUserService;

import ch.qos.logback.core.pattern.color.BoldCyanCompositeConverter;

@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터 체인에 등록이 된다.
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true) // secured 어노테이션 활성화, preAuthorize 언노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private PrincipalOauth2sUserService principalOauth2sUserService;
	
	//해당 method의 return되는 Object를 ioC로 등록해준다
	@Bean
	public BCryptPasswordEncoder encodePw() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/user/**").authenticated() //인증만 되면 들어갈수있는 주소
			.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
			.anyRequest().permitAll()
			.and()
			.formLogin()
			.loginPage("/login")
			.loginProcessingUrl("/loginProc") // /login 주소가 호출되면 시큐리티가 낚아채서 대신 로그인을 진행합니다
			.defaultSuccessUrl("/")
			.and()
			.oauth2Login()
			.loginPage("/login")// 구글로그인이 완료된 뒤의 후처리가 필요함.  
			.userInfoEndpoint()
			.userService(principalOauth2sUserService);//Tip. 구글로그인을 하면 코드X, (엑세스토큰+사용자프로필정보O)
	}
}
