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

		String email = request.getParameter("email");
		EntityManager em = JPAUtil.getEntityManager();

		try {
			em.getTransaction().begin();

			Usuario u = null;
			try {
				u = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
						.setParameter("email", email).getSingleResult();
			} catch (NoResultException e) {
				// Usuário não encontrado — u permanece null
			}

			if (u != null && u.isAtivo()) {
				String token = UUID.randomUUID().toString();
				u.setTokenRecuperacao(token);
				u.setTokenExpiracao(LocalDateTime.now().plusMinutes(30));

				String urlBase = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
						+ request.getContextPath();
				String linkRecuperacao = urlBase + "/RedefinirSenhaServlet?token=" + token;

				String corpoEmail = "Olá " + u.getNome() + ",\n\n"
						+ "Você solicitou a recuperação de senha no ERP Farmácia.\n"
						+ "Clique no link abaixo para criar uma nova senha. Este link expira em 30 minutos:\n\n"
						+ linkRecuperacao + "\n\n" + "Se você não solicitou isso, apenas ignore este e-mail.";

				// [CORREÇÃO] E-mail enviado ANTES do commit.
				// Antes, o commit ocorria antes do envio: se o EmailService falhasse,
				// o token era salvo no banco mas o e-mail nunca chegava.
				// Agora, se o envio falhar, o catch faz rollback e o banco fica limpo.
				EmailService carteiro = new EmailService();
				carteiro.enviarEmail(u.getEmail(), "Recuperação de Senha - ERP", corpoEmail);

				em.getTransaction().commit();
			} else {
				em.getTransaction().rollback();
			}

			// Mensagem genérica em ambos os casos — não revela se o e-mail existe
			request.setAttribute("mensagem",
					"Se o e-mail existir em nossa base, você receberá as instruções em instantes.");
			request.getRequestDispatcher("/mensagem.jsp").forward(request, response);

		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao processar recuperação.");
		} finally {
			em.close();
		}
	}
}