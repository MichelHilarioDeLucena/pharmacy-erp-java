package controller;

import java.io.IOException;
import java.io.PrintWriter;

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
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		// 1. Inicia o EntityManager (O "gerente" do Hibernate)
		javax.persistence.EntityManager em = config.JPAUtil.getEntityManager();

		out.println("<html><body><h2>Lista via Hibernate (JPA)</h2><table border='1'>");
		out.println("<tr><th>ID</th><th>Nome</th><th>Status</th></tr>");

		try {
			// 2. Consulta usando JPQL (A linguagem do Hibernate - foca no Objeto, não na
			// tabela)
			java.util.List<model.Usuario> lista = em.createQuery("from Usuario", model.Usuario.class).getResultList();

			for (model.Usuario u : lista) {
				String status = u.isAtivo() ? "Ativo" : "Inativo";
				out.println("<tr>");
				out.println("<td>" + u.getId() + "</td>");
				out.println("<td>" + u.getNome() + "</td>");
				out.println("<td>" + status + "</td>");
				out.println("</tr>");
			}
		} catch (Exception e) {
			out.println("Erro: " + e.getMessage());
		} finally {
			em.close();
		}

		out.println("</table><br><a href='index.html'>Voltar</a></body></html>");
	}
}