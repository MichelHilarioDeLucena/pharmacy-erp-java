package controller;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import config.AppPaths;
import config.JPAUtil;
import dao.UsuarioDAO;
import model.Usuario;
import service.LogService; // Usando o Service agora
import util.HashBCrypt;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String login = request.getParameter("login");
		String senhaPura = request.getParameter("senha");
		String ip = request.getRemoteAddr();

		EntityManager em = JPAUtil.getEntityManager();
		UsuarioDAO usuarioDAO = new UsuarioDAO(em);

		try {
			// 1. ESCUDO DE IP (Conectividade Segura)
			// Se o IP já errou muito, barramos aqui antes de qualquer consulta de usuário
			if (LogService.ipBloqueado(ip, em)) {
				request.setAttribute("mensagem", "IP bloqueado temporariamente por excesso de tentativas.");
				request.getRequestDispatcher(AppPaths.LOGIN_PAGE).forward(request, response);
				return;
			}

			em.getTransaction().begin();
			// 2. BUSCA DO USUÁRIO
			Usuario u = usuarioDAO.buscarPorLogin(login);

			// 3. TRATAMENTO DE LOGIN INEXISTENTE
			if (u == null) {
				LogService.registrar(null, "LOGIN", ip, "FALHA - USUARIO INEXISTENTE", "", em);
				em.getTransaction().commit();

				request.setAttribute("mensagem", "Login ou senha incorretos.");
				request.getRequestDispatcher("/login.jsp").forward(request, response);
				return;
			}
			// 4. VERIFICAÇÃO DE STATUS (CONTA BLOQUEADA)
			if (!u.isAtivo()) {
				LogService.registrar(u, "LOGIN", ip, "FALHA - CONTA BLOQUEADA", "", em);
				em.getTransaction().commit();

				request.setAttribute("mensagem", "Sua conta está bloqueada. Procure o administrador.");
				request.getRequestDispatcher("/login.jsp").forward(request, response);
				return;
			}

			// 5. VALIDAÇÃO DE SENHA
			boolean senhaBate = HashBCrypt.check(senhaPura, u.getSenhaHash());

			if (senhaBate) {
				// SUCESSO
				u.setTentativasFalhas(0);
				LogService.registrar(u, "LOGIN", ip, "SUCESSO", "", em);
				em.getTransaction().commit();

				HttpSession session = request.getSession(true);
				session.setAttribute("usuarioLogado", u);
				response.sendRedirect(request.getContextPath() + "/home.jsp");

			} else {
				// FALHA DE SENHA

				int falhas = u.getTentativasFalhas() + 1;
				u.setTentativasFalhas(falhas);
				String resultado = "FALHA - TENTATIVA " + falhas;
				if (falhas >= 3) {
					u.setAtivo(false); // Bloqueio lógico do usuário
					resultado = "FALHA - USUARIO BLOQUEADO";
				}

				LogService.registrar(u, "LOGIN", ip, resultado, "", em);
				em.getTransaction().commit();

				request.setAttribute("mensagem", "Login ou senha incorretos. Tentativa " + falhas + " de 3.");
				request.getRequestDispatcher("/login.jsp").forward(request, response);
			}

		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			e.printStackTrace();
			request.setAttribute("mensagem", "Erro técnico ao tentar logar. Tente mais tarde.");
			request.getRequestDispatcher(AppPaths.LOGIN_PAGE).forward(request, response);
		} finally {
			if (em.isOpen())
				em.close();
		}
	}
}