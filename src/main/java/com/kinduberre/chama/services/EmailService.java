package com.kinduberre.chama.services;

import org.springframework.stereotype.Service;

import com.kinduberre.chama.models.messaging.EmailDetails;

@Service
public interface EmailService {
	
	// To send a simple email
	String sendSimpleMail(EmailDetails details);
	
	// To send an email with attachment
    String sendMailWithAttachment(EmailDetails details);
}
