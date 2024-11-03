package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Value("$(spring.mail.username)")//
	private String fromEmailId; //

	@Autowired
	private JavaMailSender javaMailSender;

	public void sendRegistrationEmail(String to, String username, String password) {
		String subject = "Your Account has been created on GKP";
		String body =
						  "Thanks for creating an account on GKP.\r\n" + "\r\n" +
						  "Your username is "+username+"\r\n" + "\r\n" +
						  "Your password is: "+password+"\r\n" + "\r\n" ;
				
		sendEmail(to, subject, body);

	}

	public void sendEmail(String to, String subject, String body) {
		// Mimemessage
		MimeMessage message = javaMailSender.createMimeMessage();
		try {
		MimeMessageHelper helper = new MimeMessageHelper(message,true);
			helper.setTo(to.trim().replaceAll("\\s", ""));
			helper.setSubject(subject.trim());
			helper.setText(body.trim());
			helper.setFrom(fromEmailId.trim()); //
			javaMailSender.send(message); 
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

}
