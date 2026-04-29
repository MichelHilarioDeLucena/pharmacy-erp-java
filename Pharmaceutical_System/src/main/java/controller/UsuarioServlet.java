package controller;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import config.JPAUtil;
import model.Usuario;

@WebServlet("/UsuarioServlet")
public class UsuarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processarRequisicao(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processarRequisicao(request, response);
	}

	private void processarRequisicao(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		EntityManager em = JPAUtil.getEntityManager();
		String acao = request.getParameter("acao");
		String idParam = request.getParameter("id");

		// Verificação de Segurança em nível de Servidor
		HttpSession session = request.getSession();
		Usuario logado = (Usuario) session.getAttribute("usuarioLogado");
		boolean ehMaster = logado != null
				&& (logado.getPerfil().toString().equals("ADMIN") || logado.getPerfil().toString().equals("GERENTE"));

		try {
			// AÇÕES QUE ALTERAM O BANCO OU REQUISITAM PÁGINAS ESPECÍFICAS
			if (ehMaster && acao != null) {
				switch (acao) {

				case "alternar":
					em.getTransaction().begin();
					Usuario uAlt = em.find(Usuario.class, Integer.parseInt(idParam));
					if (uAlt != null) {
						uAlt.setAtivo(!uAlt.isAtivo());
					}
					em.getTransaction().commit();
					response.sendRedirect("UsuarioServlet?acao=listar");
					return; // Impede que continue para o forward no fim do método

				case "carregar":
					Usuario uEdit = em.find(Usuario.class, Integer.parseInt(idParam));
					request.setAttribute("usuario", uEdit);
					request.getRequestDispatcher("/editar_usuario.jsp").forward(request, response);
					return; // Para aqui, pois já despachou para a JSP de edição

				case "atualizar":
					em.getTransaction().begin();
					Usuario uAtualizar = em.find(Usuario.class, Integer.parseInt(request.getParameter("id")));
					if (uAtualizar != null) {
						uAtualizar.setNome(request.getParameter("nome"));
						uAtualizar.setEmail(request.getParameter("email"));
						// Converte a String do HTML para o seu novo Enum Perfil
						uAtualizar.setPerfil(model.Perfil.valueOf(request.getParameter("perfil")));
					}
					em.getTransaction().commit();
					response.sendRedirect("UsuarioServlet?acao=listar");
					return;

				case "salvar":
					em.getTransaction().begin();
					model.Usuario novoU = new model.Usuario();
					novoU.setNome(request.getParameter("nome"));
					novoU.setEmail(request.getParameter("email"));
					novoU.setLogin(request.getParameter("login"));
					novoU.setAtivo(true);

					// Define o Perfil usando o Enum
					novoU.setPerfil(model.Perfil.valueOf(request.getParameter("perfil")));

					// Criptografa a senha
					String senhaPlana = request.getParameter("senha");
					String senhaHash = util.HashBCrypt.criptografarSenha(senhaPlana);
					novoU.setSenhaHash(senhaHash);

					em.persist(novoU);
					em.getTransaction().commit();

					response.sendRedirect("UsuarioServlet?acao=listar");
					return;

				case "abrirCadastro":
					request.getRequestDispatcher("/cadastro_usuario.jsp").forward(request, response);
					return;
				}
			}

			// LISTAGEM PADRÃO (Executa se a acao for 'listar' ou se não entrar em nenhum
			// case acima)
			List<Usuario> lista = em.createQuery("from Usuario", Usuario.class).getResultList();
			request.setAttribute("listaUsuarios", lista);
			request.getRequestDispatcher("/listaUsuarios.jsp").forward(request, response);

		} catch (Exception e) {
			// Se houver erro e a transação estiver aberta, desfaz a alteração para não
			// "sujar" o banco
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			e.printStackTrace();
			throw new ServletException("Erro ao processar operação de usuário", e);
		}
	}
}