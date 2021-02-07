package com.cos.security1.controller;

import javax.annotation.Resource;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

@Controller
public class IndexController {
	
	@Autowired 
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;

	@ResponseBody
	@GetMapping("/test/login")
	public String loginTest(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails) {//DI(의존성 주입)
		System.out.println("/test/login===================");
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		System.out.println("authentication : " + principalDetails.getUser());
		System.out.println("userDetails : " + userDetails.getUser());
		return "세션정보확인";
	}
	
	@ResponseBody
	@GetMapping("/test/oauth/login")
	public String testOauthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth) {//DI(의존성 주입)
		System.out.println("/test/login===================");
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		System.out.println("authentication : " + oAuth2User.getAttributes());
		System.out.println("oAuth2User : " + oauth.getAttributes());
		return "세션정보확인";
	}
	
	@GetMapping({"","/"})
	public String index() {
		//머스테치 기본폴더 src/main/resources
		//뷰리졸버 설정 :  prefix: /templates/, suffix: .mustache 생략가능
		return "index"; //src/main/resources/templates/index.mustache
	}
	
	// OAuth 로그인을 해도 PrincipalDetails로 받음
	// 일반로그인을 해도 PrincipalDetails로 받음
	@ResponseBody
	@GetMapping("/user")
	public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		System.out.println("PrincipalDetails : " + principalDetails);
		return "user";
	}
	@ResponseBody
	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}
	@ResponseBody
	@GetMapping("/manager")
	public String manager() {
		return "manager";
	}
	
	//스프링시큐리티에서 해달주소를 낚아채버림 - SecurityConfig 파일 작성후 작동 안함
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/join")
	public String join() {
		return "join";
	}
	

	@PostMapping("/join")
	public String joinProc(User user) {
		System.out.println(user);
		user.setRole("ROLE_USER");
		//비밀번호 암호화
		String rawPw = user.getPassword();
		String encPw = bcrypt.encode(rawPw);
		user.setPassword(encPw);
		userRepository.save(user); //회원가입 잘됨. 비밀번호 : 1234 => 시큐리티로 로그인 할수없음. 이유는 패스워드가 암호화가 안되었기 때문
		
		return "redirect:/login";
	}
	
	@ResponseBody
	@Secured("ROLE_ADMIN")
	@GetMapping("/info")
	public String info() {
		return "개인정보";
	}
	
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
	@GetMapping("/data")
	public String data() {
		return "데이터정보";
	}
}
