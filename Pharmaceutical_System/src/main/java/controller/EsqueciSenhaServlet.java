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
			// Buscamos o usuário usando o DAO (Mantenha seu buscarPorEmail no DAO)
			Usuario u = usuarioDAO.buscarPorEmail(email);

			// Iniciamos a transação apenas se precisarmos gravar o token
			if (u != null && u.isAtivo()) {
				em.getTransaction().begin();

				// 1. Geração do Token UUID
				String token = UUID.randomUUID().toString();
				u.setTokenRecuperacao(token);
				u.setTokenExpiracao(LocalDateTime.now().plusMinutes(30));

				// 2. Persistência do token no banco
				usuarioDAO.salvar(u);
				em.getTransaction().commit();

				// 3. Construção da URL de recuperação
				String urlBase = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
						+ request.getContextPath();
				String linkRecuperacao = urlBase + "/redefinirSenha.jsp?token=" + token;

				// 4. Montagem do corpo em HTML (Muito mais profissional)
				String corpoEmail = "<html><body>" + "<h2>Recuperação de Senha - ERP Farmácia</h2>" + "<p>Olá, <b>"
						+ u.getNome() + "</b>.</p>"
						+ "<p>Você solicitou a redefinição de sua senha. Clique no botão abaixo para prosseguir:</p>"
						+ "<div style='margin: 20px 0;'>" + "<a href='" + linkRecuperacao
						+ "' style='background-color: #28a745; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>Redefinir Minha Senha</a>"
						+ "</div>" + "<p>Este link é válido por <b>30 minutos</b>.</p>"
						+ "<p>Se você não solicitou esta alteração, por favor ignore este e-mail.</p>"
						+ "<hr><small>Este é um e-mail automático, não responda.</small>" + "</body></html>";

				// 5. Envio do e-mail
				emailService.enviarEmail(u.getEmail(), "Recuperação de Senha - ERP", corpoEmail);
			}

			// SEGURANÇA: Independente de o usuário existir ou estar ativo,
			// a resposta para o navegador é sempre a mesma para evitar "pesca de e-mails".
			request.setAttribute("mensagem",
					"Se o e-mail informado estiver cadastrado, você receberá um link de recuperação em instantes.");

			// Usamos o login.jsp para que o pop-up JavaScript que criamos apareça lá
			request.getRequestDispatcher("/login.jsp").forward(request, response);

		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			e.printStackTrace();
			request.setAttribute("mensagem", "Erro interno ao processar a recuperação.");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		} finally {
			if (em.isOpen()) {
				em.close();
			}
		}
	}
}