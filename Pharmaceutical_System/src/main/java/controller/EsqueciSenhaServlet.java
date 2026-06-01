package controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.AppPaths;
import config.JPAUtil;
import dao.UsuarioDAO;
import model.Usuario;
import service.EmailService;

@WebServlet("/EsqueciSenhaServlet")
public class EsqueciSenhaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = request.getParameter("email");
		EntityManager em = JPAUtil.getEntityManager();
		UsuarioDAO usuarioDAO = new UsuarioDAO(em);
		EmailService emailService = new EmailService();

		try {
			Usuario u = usuarioDAO.buscarPorEmail(email);

			// Criação de Token mediante existência de usuário ativo
			if (u != null && u.isAtivo()) {
				em.getTransaction().begin();

				// 1. Geração do Token UUID
				String token = UUID.randomUUID().toString();
				u.setTokenRecuperacao(token);
				u.setTokenExpiracao(LocalDateTime.now().plusMinutes(30));

				// 2. Persistência do token no banco
				usuarioDAO.salvar(u);
				em.getTransaction().commit();

				// URL Dinâmica
				String urlBase = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
						+ request.getContextPath();
				String linkRecuperacao = urlBase + "/views/auth/novaSenha.jsp?token=" + token;

				String corpoEmail = "Recuperação de Senha - ERP Farmácia.\n" + "Olá, " + u.getNome()
						+ "Você solicitou a redefinição de sua senha. Clique no botão abaixo para prosseguir: "
						+ linkRecuperacao + " Este link é válido por 30 minutos"
						+ "Se você não solicitou esta alteração, por favor ignore este e-mail."
						+ " Este é um e-mail automático, não responda.";

				// Envio de E-mail
				emailService.enviarEmail(u.getEmail(), "Recuperação de Senha - ERP", corpoEmail);
			}

			// Mensagem genérica para evitar "Pesca de E-mails"
			request.setAttribute("mensagem",
					"Se o e-mail informado estiver cadastrado, você receberá um link de recuperação em instantes.");

			// Pop-up
			request.getRequestDispatcher(AppPaths.LOGIN_PAGE).forward(request, response);

		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			e.printStackTrace();
			request.setAttribute("mensagem", "Erro interno ao processar a recuperação.");
			request.getRequestDispatcher(AppPaths.LOGIN_PAGE).forward(request, response);
		} finally {
			if (em.isOpen()) {
				em.close();
			}
		}
	}
}