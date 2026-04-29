package controller;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import config.JPAUtil;
import model.Usuario;
import util.HashBCrypt;

@WebServlet("/LoginServlet")

public class LoginServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String login = request.getParameter("login");
		String senhaPura = request.getParameter("senha");

		EntityManager em = JPAUtil.getEntityManager();

		try {
			// 1. Busca o usuário (se não existir, cai no NoResultException)
			Usuario u = em.createQuery("SELECT u FROM Usuario u WHERE u.login = :login", Usuario.class)
					.setParameter("login", login).getSingleResult();

			String ip = request.getRemoteAddr(); // Captura o IP do usuário

			// Inicia transação para atualizar tentativas ou status
			em.getTransaction().begin();

			// 2. Verifica se a conta já está bloqueada
			if (!u.isAtivo()) {
				registrarLog(u, "LOGIN", ip, "FALHA - CONTA BLOQUEADA", em);
				em.getTransaction().commit(); // Salva o log
				request.setAttribute("mensagem", "Sua conta está bloqueada. Procure o administrador.");
				request.getRequestDispatcher("/mensagem.jsp").forward(request, response);
				return;
			}

			// 3. Verifica a senha
			boolean senhaBate = HashBCrypt.verificarSenha(senhaPura, u.getSenhaHash());

			if (senhaBate) {
				// SUCESSO: Reseta tentativas e loga
				u.setTentativasFalhas(0);
				registrarLog(u, "LOGIN", ip, "SUCESSO", em);
				em.getTransaction().commit();

				HttpSession session = request.getSession();
				session.setAttribute("usuarioLogado", u);
				response.sendRedirect("home.jsp");
				return;

			} else {
				// FALHA DE SENHA: Incrementa contador
				int falhas = u.getTentativasFalhas() + 1;
				u.setTentativasFalhas(falhas);

				String resultado = "FALHA - TENTATIVA " + falhas;

				if (falhas >= 3) {
					u.setAtivo(false); // Bloqueia o usuário
					resultado = "FALHA - CONTA BLOQUEADA AGORA";
				}

				registrarLog(u, "LOGIN", ip, resultado, em);
				em.getTransaction().commit();

				request.setAttribute("mensagem", "Senha incorreta. Tentativa " + falhas + " de 3.");
				request.getRequestDispatcher("/mensagem.jsp").forward(request, response);
				return;
			}

		} catch (NoResultException e) {
			// Caso o login nem exista no banco
			request.setAttribute("mensagem", "Usuário não encontrado.");
			request.getRequestDispatcher("/mensagem.jsp").forward(request, response);
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			if (em.isOpen())
				em.close();
		}
	} // Fecha doPost

	private void registrarLog(Usuario u, String acao, String ip, String resultado, EntityManager em) {
		// Aqui você pode fazer um insert direto via SQL ou usar uma Entity LogAcesso
		// Exemplo com SQL Nativo (mais rápido para logs):
		em.createNativeQuery(
				"INSERT INTO log_acessos (usuario_id, acao, ip, resultado, data_hora) VALUES (?, ?, ?, ?, NOW())")
				.setParameter(1, u.getId()).setParameter(2, acao).setParameter(3, ip).setParameter(4, resultado)
				.executeUpdate();
	}

}
