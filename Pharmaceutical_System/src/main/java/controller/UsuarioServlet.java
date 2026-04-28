package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/UsuarioServlet")
public class UsuarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		javax.persistence.EntityManager em = config.JPAUtil.getEntityManager();
		String acao = request.getParameter("acao");
		String idParam = request.getParameter("id");

		try {
			// 1. Processa a ação (se o botão ativar/desativar for clicado)
			if ("alternar".equals(acao) && idParam != null) {
				em.getTransaction().begin();
				int id = Integer.parseInt(idParam);
				model.Usuario u = em.find(model.Usuario.class, id);

				if (u != null) {
					u.setAtivo(!u.isAtivo()); // Inverte o status
				}
				em.getTransaction().commit();
			}

			// 2. Busca a lista atualizada no banco
			java.util.List<model.Usuario> lista = em.createQuery("from Usuario", model.Usuario.class).getResultList();

			// 3. Coloca a lista na "mochila" (request) para o JSP poder ler
			request.setAttribute("listaUsuarios", lista);

			// 4. Redireciona silenciosamente para o JSP fazer o visual
			request.getRequestDispatcher("/listaUsuarios.jsp").forward(request, response);

		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw new ServletException("Erro ao processar usuários", e);
		} finally {
			em.close();
		}
	}
}