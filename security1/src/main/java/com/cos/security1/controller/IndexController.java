package com.cos.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

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
	@ResponseBody
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@ResponseBody
	@GetMapping("/join")
	public String join() {
		return "join";
	}
	
	@ResponseBody
	@GetMapping("/joinProc")
	public String joinProc() {
		return "회원가입 완료됨";
	}
}
