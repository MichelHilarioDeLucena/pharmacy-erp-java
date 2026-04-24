package controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.JPAUtil;
import model.Usuario;
import service.EmailService;

@WebServlet("/EsqueciSenhaServlet")
public class EsqueciSenhaServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Pega o e-mail digitado no HTML
		String email = request.getParameter("email");
		EntityManager em = JPAUtil.getEntityManager();

		try {
			em.getTransaction().begin();

			Usuario u = null;
			try {
				// Tenta achar o usuário pelo e-mail usando Hibernate (JPA)
				u = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
						.setParameter("email", email).getSingleResult();
			} catch (NoResultException e) {
				// Se não achar, o 'u' continua null e ignoramos silenciosamente
			}

			// Só processa se o usuário existir E estiver ativo!
			if (u != null && u.isAtivo()) {

				// 1. Gera o Token (UUID) e a validade (30 minutos a partir de agora)
				String token = UUID.randomUUID().toString();
				u.setTokenRecuperacao(token);
				u.setTokenExpiracao(LocalDateTime.now().plusMinutes(30));

				// 2. Prepara o link mágico (VERIFIQUE SE A SUA PORTA É A 8080 MESMO)
				String link = "http://localhost:8080/erp/RedefinirSenhaServlet?token=" + token;

				String textoEmail = "Olá " + u.getNome() + ",\n\n"
						+ "Você solicitou a recuperação de senha no nosso sistema.\n"
						+ "Clique no link abaixo para criar uma nova senha. Este link expira em 30 minutos.\n\n" + link;

				// 3. Chama o nosso carteiro
				EmailService carteiro = new EmailService();
				carteiro.enviarEmail(u.getEmail(), "Recuperação de Senha - ERP", textoEmail);
			}

			// Salva as alterações no banco (o UUID e a hora)
			em.getTransaction().commit();

			// 4. Nossa filosofia MVC: Passa a mensagem e delega o visual para um JSP
			request.setAttribute("mensagem",
					"Se o e-mail estiver cadastrado em nossa base, você receberá um link com instruções.");
			request.getRequestDispatcher("/mensagem.jsp").forward(request, response);

		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			e.printStackTrace();
			throw new ServletException("Erro ao processar recuperação de senha", e);
		} finally {
			em.close();
		}
	}
}