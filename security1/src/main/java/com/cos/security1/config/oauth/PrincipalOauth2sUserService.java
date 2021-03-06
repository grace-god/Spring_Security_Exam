package com.cos.security1.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

import ch.qos.logback.core.pattern.color.BoldCyanCompositeConverter;

@Service
public class PrincipalOauth2sUserService extends DefaultOAuth2UserService{
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	//구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("getClientRegistration : " + userRequest.getClientRegistration());//registrationId로 어떤 Oauth로 로그인 했는지 확인 가능
		System.out.println("getAccessToken : " + userRequest.getAccessToken().getTokenValue());
				
		OAuth2User oauth2User =super.loadUser(userRequest);
		//구글로그인 버큰 클릭 -> 구글로그인창 -> 로그인 완료 -> code 리턴(Oauth-Client라이브러리) -> AccessToken요청
		//userRequest정보 -> 회원프로필 받아야함 - loadUser함수 호출 -> 구글로부터 회원프로필 받아준다.
		System.out.println("getAttributes : " + super.loadUser(userRequest).getAttributes());
		
		String provider = userRequest.getClientRegistration().getClientId();// google
		String providerId = oauth2User.getAttribute("sub");
		String username = provider+"_"+providerId;// google_1231241241( => sub=...)
		String password = bCryptPasswordEncoder.encode("겟인데어");
		String email = oauth2User.getAttribute("email");
		String role = "ROLE_USER";
		
		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity == null) {
			userEntity=User.builder()
					.username(username)
					.password(password)
					.email(email)
					.role(role)
					.provider(provider)
					.providerId(providerId)
					.build();
			userRepository.save(userEntity);
		}
		return new PrincipalDetails(userEntity, oauth2User.getAttributes());
	}
}

//google로 회원가입
//username = google_115079614818387887771(google_+구글sub:프라임키,중복X)
//password = 암호화(겟인데어):어짜피 구글로 로그인 할꺼라서 아무거나 상관 X
//email=zsemko93@gmail.com(구글에 저장된 email)
//role = "ROLE_USER"
//provider = google
//providerId = 115079614818387887771(구글sub:프라임키,중복X)