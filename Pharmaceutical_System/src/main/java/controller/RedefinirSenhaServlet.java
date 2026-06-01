package controller;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.JPAUtil;
import model.Usuario;
import util.HashBCrypt;

@WebServlet("/RedefinirSenhaServlet")
public class RedefinirSenhaServlet extends HttpServlet {

	private static final String LOGIN_MSG_JSP = "/views/common/mensagem.jsp";
	private static final String NOVA_SENHA_JSP = "/views/auth/novaSenha.jsp";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String token = request.getParameter("token");
		System.out.println("TOKEN GET: " + token);
		EntityManager em = JPAUtil.getEntityManager();

		try {
			Usuario u = em.createQuery("SELECT u FROM Usuario u WHERE u.tokenRecuperacao = :token", Usuario.class)
					.setParameter("token", token).getSingleResult();

			if (u.getTokenExpiracao().isBefore(LocalDateTime.now())) {
				request.setAttribute("mensagem", "Este link de recuperação expirou.");
				request.getRequestDispatcher(LOGIN_MSG_JSP).forward(request, response);
				return;
			}

			request.setAttribute("token", token);
			request.getRequestDispatcher(NOVA_SENHA_JSP).forward(request, response);

		} catch (NoResultException e) {
			request.setAttribute("mensagem", "Link de recuperação inválido.");
			request.getRequestDispatcher(LOGIN_MSG_JSP).forward(request, response);
		} finally {
			em.close();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String token = request.getParameter("token");
		String novaSenha = request.getParameter("senha");
		EntityManager em = JPAUtil.getEntityManager();
		System.out.println("TOKEN POST: " + token);

		try {
			em.getTransaction().begin();

			Usuario u;
			try {
				u = em.createQuery("SELECT u FROM Usuario u WHERE u.tokenRecuperacao = :token", Usuario.class)
						.setParameter("token", token).getSingleResult();
			} catch (NoResultException e) {
				em.getTransaction().rollback();
				request.setAttribute("mensagem", "Link de recuperação inválido.");
				request.getRequestDispatcher(LOGIN_MSG_JSP).forward(request, response);
				return;
			}

			u.setSenhaHash(HashBCrypt.hash(novaSenha));
			u.setTokenRecuperacao(null);
			u.setTokenExpiracao(null);

			em.getTransaction().commit();

			request.setAttribute("mensagem", "Senha alterada com sucesso! Você já pode fazer login.");
			request.getRequestDispatcher(LOGIN_MSG_JSP).forward(request, response);

		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			request.setAttribute("mensagem", "Erro ao redefinir senha.");
			request.getRequestDispatcher(LOGIN_MSG_JSP).forward(request, response);
		} finally {
			em.close();
		}
	}
}