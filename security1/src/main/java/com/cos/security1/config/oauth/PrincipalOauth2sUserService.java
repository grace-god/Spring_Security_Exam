package com.cos.security1.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2sUserService extends DefaultOAuth2UserService{
	
	//구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("getClientRegistration : " + userRequest.getClientRegistration());//registrationId로 어떤 Oauth로 로그인 했는지 확인 가능
		System.out.println("getAccessToken : " + userRequest.getAccessToken().getTokenValue());
		//구글로그인 버큰 클릭 -> 구글로그인창 -> 로그인 완료 -> code 리턴(Oauth-Client라이브러리) -> AccessToken요청
		//userRequest정보 -> 회원프로필 받아야함 - loadUser함수 호출 -> 구글로부터 회원프로필 받아준다.
		System.out.println("getAttributes : " + super.loadUser(userRequest).getAttributes());
		
		OAuth2User oauth2User =super.loadUser(userRequest);
		return super.loadUser(userRequest);
	}
}

//google로 회원가입
//username = google_115079614818387887771(google_+구글sub:프라임키,중복X)
//password = 암호화(겟인데어):어짜피 구글로 로그인 할꺼라서 아무거나 상관 X
//email=zsemko93@gmail.com(구글에 저장된 email)
//role = "ROLE_USER"
//provider = google
//providerId = 115079614818387887771(구글sub:프라임키,중복X)