package com.mypet.email;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.util.UriComponentsBuilder;

import com.mypet.domain.MemberVO;

public class RegistrationNotifierService {
	@Autowired
	private JavaMailSender mailSender;	
	
	public void sendMail(MemberVO vo,String auth_token) throws Exception /*//MessagingException,UnsupportedEncodingException*/{
		MimeMessage message = mailSender.createMimeMessage();		
		MimeMessageHelper messageHelper = new MimeMessageHelper(message,true,"utf-8");
		
		messageHelper.setSubject("[MyPET] 회원 가입 인증 안내");			
		String htmlContent = vo.getUser_id()+"님의 이메일 인증(아래 링크를 클릭해주세요.)\n"
				+ "<a href='http://localhost:8080/user/confirm_verification"+makeQuery(vo.getUser_id(),auth_token)
				+"'>인증하기</a>";
		System.out.println(htmlContent);
		messageHelper.setText(htmlContent,true);
		messageHelper.setFrom("admin@mypet.com","admin");
		messageHelper.setTo(new InternetAddress(vo.getUser_email(),vo.getUser_id(),"utf-8"));			
		mailSender.send(message);
	}
	
	private String makeQuery(String user_id,String auth_token) {
		return	UriComponentsBuilder.newInstance()
						.queryParam("user_id",user_id)
						.queryParam("auth_token",auth_token)
						.build()
						.toUriString();
	}
}
