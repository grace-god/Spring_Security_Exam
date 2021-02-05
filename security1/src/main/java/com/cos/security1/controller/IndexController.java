package com.cos.security1.controller;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

@Controller
public class IndexController {
	
	@Autowired 
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;

	@GetMapping({"","/"})
	public String index() {
		//머스테치 기본폴더 src/main/resources
		//뷰리졸버 설정 :  prefix: /templates/, suffix: .mustache 생략가능
		return "index"; //src/main/resources/templates/index.mustache
	}
	@ResponseBody
	@GetMapping("/user")
	public String user() {
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
