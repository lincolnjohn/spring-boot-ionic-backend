package br.thot.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import br.thot.cursomc.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);

}
