package controller;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import config.AppPaths;
import config.JPAUtil;
import dao.UsuarioDAO;
import model.Usuario;

@WebServlet("/oauth2callback")
public class OAuthCallbackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String code = request.getParameter("code");

		if (code == null || code.isEmpty()) {
			response.sendRedirect(request.getContextPath() + AppPaths.LOGIN_PAGE + "?error=OAuth_Failed");
			return;
		}

		EntityManager em = JPAUtil.getEntityManager();

		try {
			GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(),
					GsonFactory.getDefaultInstance(), "https://oauth2.googleapis.com/token",
					System.getenv("GOOGLE_CLIENT_ID"), System.getenv("GOOGLE_CLIENT_SECRET"), code,
					System.getenv("GOOGLE_REDIRECT_URI")).execute();

			GoogleIdToken idToken = tokenResponse.parseIdToken();
			GoogleIdToken.Payload payload = idToken.getPayload();

			String emailGoogle = payload.getEmail();

			UsuarioDAO usuarioDAO = new UsuarioDAO(em);
			Usuario usuario = usuarioDAO.buscarPorEmail(emailGoogle);

			if (usuario != null && usuario.isAtivo()) {
				HttpSession session = request.getSession();
				session.setAttribute("usuarioLogado", usuario);

				response.sendRedirect(request.getContextPath() + AppPaths.HOME_PAGE);
			} else {
				response.sendRedirect(request.getContextPath() + AppPaths.LOGIN_PAGE + "?error=Usuario_Nao_Cadastrado");
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect(request.getContextPath() + AppPaths.LOGIN_PAGE + "?error=Erro_Tecnico_OAuth");
		} finally {
			em.close();
		}
	}
}