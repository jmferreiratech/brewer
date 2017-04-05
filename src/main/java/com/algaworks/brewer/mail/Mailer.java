package com.algaworks.brewer.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.model.ItemVenda;
import com.algaworks.brewer.model.Venda;
import com.algaworks.brewer.storage.FotoStorage;

@Component
public class Mailer {

	private static Logger logger = LoggerFactory.getLogger(Mailer.class);

	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private TemplateEngine thymeleaf;
	@Autowired
	private FotoStorage fotoStorage;

	@Async
	public void enviar(Venda venda) {
		Context context = new Context();
		context.setVariable("venda", venda);
		context.setVariable("logo", "logo");

		for (ItemVenda item : venda.getItens()) {
			Cerveja cerveja = item.getCerveja();
			String cid = "foto-" + cerveja.getCodigo();
			context.setVariable(cid, cid);
		}
		try {
			String email = thymeleaf.process("mail/ResumoVenda", context);
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			helper.setFrom("joamarcelo@gmail.com");
			helper.setTo(venda.getCliente().getEmail());
			helper.setSubject("Brewer - Venda realizada");
			helper.setText(email, true);
			helper.addInline("logo", new ClassPathResource("static/images/logo-gray.png"));
			for (ItemVenda item : venda.getItens()) {
				Cerveja cerveja = item.getCerveja();
				String cid = "foto-" + cerveja.getCodigo();
				byte[] foto = fotoStorage.recuperarThumbnail(cerveja.getFotoOuMock());
				helper.addInline(cid, new ByteArrayResource(foto), cerveja.getContentType());
			}
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			logger.error("Erro enviando e-mail", e);
		}
	}
}
