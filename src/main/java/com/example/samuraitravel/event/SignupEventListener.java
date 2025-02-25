package com.example.samuraitravel.event;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.service.VerificationTokenService;

@Component
public class SignupEventListener {
	private final VerificationTokenService verificationTokenService;
	private final JavaMailSender javaMailSender;
	
	@Value("${application.base-url:http://localhost:8080}") //変更箇所
	private String baseUrl;
	
	public SignupEventListener(VerificationTokenService verificationTokenService, JavaMailSender mailSender) {
		this.verificationTokenService = verificationTokenService;
		this.javaMailSender = mailSender;
	}
	
	@EventListener
	private void onSignupEvent(SignupEvent signupEvent) { //SignupEventクラスから通知を受けた時に実行される処理
		User user = signupEvent.getUser();
		String token = UUID.randomUUID().toString();
		verificationTokenService.create(user, token);
		
		String senderAddress = "springboot.samuraitravel@example.com";
		String recipientAddress = user.getEmail();
		String subject = "メール認証";
		String confirmationUrl = baseUrl + signupEvent.getRequestUrl() + "/verify?token=" + token; //変更箇所
		String message = "以下のリンクをクリックして会員登録を完了してください。";
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(senderAddress);
		mailMessage.setTo(recipientAddress);
		mailMessage.setSubject(subject);
		mailMessage.setText(message + "\n" + confirmationUrl);
		javaMailSender.send(mailMessage);
	}
}
