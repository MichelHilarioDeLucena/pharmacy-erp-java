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

	// GET — usuário clica no link do e-mail
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String token = request.getParameter("token");
		EntityManager em = JPAUtil.getEntityManager();

		try {
			Usuario u = em.createQuery("SELECT u FROM Usuario u WHERE u.tokenRecuperacao = :token", Usuario.class)
					.setParameter("token", token).getSingleResult();

			if (u.getTokenExpiracao().isBefore(LocalDateTime.now())) {
				request.setAttribute("mensagem", "Este link de recuperação expirou.");
				request.getRequestDispatcher("/mensagem.jsp").forward(request, response);
				return;
			}

			request.setAttribute("token", token);
			request.getRequestDispatcher("/novaSenha.jsp").forward(request, response);

		} catch (NoResultException e) {
			request.setAttribute("mensagem", "Link de recuperação inválido.");
			request.getRequestDispatcher("/mensagem.jsp").forward(request, response);
		} finally {
			em.close();
		}
	}

	// POST — usuário envia o formulário com a nova senha
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String token = request.getParameter("token");
		String novaSenha = request.getParameter("senha");
		EntityManager em = JPAUtil.getEntityManager();

		try {
			em.getTransaction().begin();

			Usuario u;
			try {
				u = em.createQuery("SELECT u FROM Usuario u WHERE u.tokenRecuperacao = :token", Usuario.class)
						.setParameter("token", token).getSingleResult();
			} catch (NoResultException e) {
				// [CORREÇÃO] Catch específico para token inválido.
				// Antes, NoResultException caía no catch genérico e mostrava
				// "Erro ao redefinir senha" — mensagem enganosa para o usuário.
				em.getTransaction().rollback();
				request.setAttribute("mensagem", "Link de recuperação inválido.");
				request.getRequestDispatcher("/mensagem.jsp").forward(request, response);
				return;
			}

			u.setSenhaHash(HashBCrypt.criptografarSenha(novaSenha));
			u.setTokenRecuperacao(null);
			u.setTokenExpiracao(null);
			em.getTransaction().commit();

			request.setAttribute("mensagem", "Senha alterada com sucesso! Você já pode fazer login.");
			request.getRequestDispatcher("/mensagem.jsp").forward(request, response);

		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			request.setAttribute("mensagem", "Erro ao redefinir senha.");
			request.getRequestDispatcher("/mensagem.jsp").forward(request, response);
		} finally {
			em.close();
		}
	}
}