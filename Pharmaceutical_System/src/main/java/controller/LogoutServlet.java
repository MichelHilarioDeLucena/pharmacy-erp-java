package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String login_directory = "/views/login.jsp";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 1. Pega a sessão atual, se existir
		HttpSession session = request.getSession(false);

		if (session != null) {
			// 2. Destrói a sessão e limpa os dados
			session.invalidate();
		}

		// 3. Redireciona para a tela de login com uma mensagem opcional
		response.sendRedirect(login_directory);
	}
}