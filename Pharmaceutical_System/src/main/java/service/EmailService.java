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

	private static final String USUARIO;
	private static final String SENHA;
	private static final Properties PROPS;

	static {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		USUARIO = dotenv.get("EMAIL_USER");
		SENHA = dotenv.get("EMAIL_PASS");

		PROPS = new Properties();
		PROPS.put("mail.smtp.auth", "true");
		PROPS.put("mail.smtp.starttls.enable", "true");
		PROPS.put("mail.smtp.host", "smtp.gmail.com");
		PROPS.put("mail.smtp.port", "587");
		PROPS.put("mail.smtp.ssl.trust", "smtp.gmail.com");
	}

	public void enviarEmail(String destinatario, String assunto, String mensagemTexto) {
		Session session = Session.getInstance(PROPS, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USUARIO, SENHA);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(USUARIO));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
			message.setSubject(assunto);
			message.setText(mensagemTexto);

			Transport.send(message);
			System.out.println("=== [EmailService] E-mail enviado com sucesso para: " + destinatario + " ===");

		} catch (MessagingException e) {
			System.err.println("=== [EmailService] ERRO ao enviar e-mail: " + e.getMessage());
			throw new RuntimeException("Falha ao enviar e-mail de recuperação", e);
		}
	}
}