package com.algaworks.brewer.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.algaworks.brewer.model.Venda;

@Component
public class Mailer {

	@Autowired
	private JavaMailSender mailSender;

	@Async
	public void enviar(Venda venda) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("joamarcelo@gmail.com");
		message.setTo("fortmarcelo@yahoo.com.br");
		message.setSubject("Venda efetuada com sucesso");
		message.setText("Obrigado, sua venda foi processada!");

		mailSender.send(message);
	}
}
