package service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import io.github.cdimascio.dotenv.Dotenv;

public class EmailService {

	private String usuario;
	private String senha;

	public EmailService() {
		// Carrega as credenciais do seu .env
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		this.usuario = dotenv.get("EMAIL_USER");
		this.senha = dotenv.get("EMAIL_PASS");
	}

	public void enviarEmail(String destinatario, String assunto, String mensagemTexto) {
		// Configurações do Servidor SMTP do Google
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

		// Cria a sessão de login no Gmail
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(usuario, senha);
			}
		});

		try {
			// Cria a mensagem de e-mail
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(usuario)); // Seu e-mail
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
			message.setSubject(assunto);
			message.setText(mensagemTexto);

			// Envia de fato!
			Transport.send(message);

			System.out.println("=== [EmailService] E-mail enviado com sucesso para: " + destinatario + " ===");

		} catch (MessagingException e) {
			System.err.println("=== [EmailService] ERRO ao enviar e-mail: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}
}