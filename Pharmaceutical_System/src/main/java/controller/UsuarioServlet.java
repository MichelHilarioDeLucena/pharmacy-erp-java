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
import dao.UsuarioDAO;
import model.Perfil;
import model.Usuario;
import util.HashBCrypt;

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

		// [CORREÇÃO 1] Verificação de sessão ANTES de qualquer operação.
		// Antes, um visitante sem sessão chegava até a listagem de usuários.
		HttpSession session = request.getSession(false);
		Usuario logado = (session != null) ? (Usuario) session.getAttribute("usuarioLogado") : null;

		if (logado == null) {
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}

		// [CORREÇÃO 2] Verificação de perfil protegida contra NullPointerException.
		// getPerfil() poderia ser null e .toString() derrubava o servidor com 500.
		Perfil perfil = logado.getPerfil();
		boolean ehMaster = (perfil == Perfil.ADMIN || perfil == Perfil.GERENTE);

		EntityManager em = JPAUtil.getEntityManager();
		UsuarioDAO usuarioDAO = new UsuarioDAO(em);
		String acao = request.getParameter("acao");
		String idParam = request.getParameter("id");

		try {
			if (ehMaster && acao != null) {
				switch (acao) {

				case "alternar":
					em.getTransaction().begin();
					Usuario uAlt = usuarioDAO.buscarPorId(Integer.parseInt(idParam));
					if (uAlt != null) {
						uAlt.setAtivo(!uAlt.isAtivo());
						usuarioDAO.salvar(uAlt);
					}
					em.getTransaction().commit();
					response.sendRedirect("UsuarioServlet?acao=listar");
					return;

				case "carregar":
					Usuario uEdit = usuarioDAO.buscarPorId(Integer.parseInt(idParam));
					request.setAttribute("usuario", uEdit);
					request.getRequestDispatcher("/editar_usuario.jsp").forward(request, response);
					return;

				case "atualizar":
					em.getTransaction().begin();
					Usuario uAtualizar = usuarioDAO.buscarPorId(Integer.parseInt(idParam));
					if (uAtualizar != null) {
						uAtualizar.setNome(request.getParameter("nome"));
						uAtualizar.setEmail(request.getParameter("email"));
						uAtualizar.setPerfil(Perfil.valueOf(request.getParameter("perfil")));
						usuarioDAO.salvar(uAtualizar);
					}
					em.getTransaction().commit();
					response.sendRedirect("UsuarioServlet?acao=listar");
					return;

				case "salvar":
					String loginNovo = request.getParameter("login");

					// [CORREÇÃO 3] Verifica login duplicado antes de persistir.
					// Sem isso, se o banco não tiver UNIQUE, cria dois usuários com
					// o mesmo login. Se tiver, lança exceção crua do Hibernate.
					if (usuarioDAO.buscarPorLogin(loginNovo) != null) {
						request.setAttribute("mensagem", "Este login já está em uso.");
						request.getRequestDispatcher("/cadastro_usuario.jsp").forward(request, response);
						return;
					}

					em.getTransaction().begin();
					Usuario novoU = new Usuario();
					novoU.setNome(request.getParameter("nome"));
					novoU.setEmail(request.getParameter("email"));
					novoU.setLogin(loginNovo);
					novoU.setPerfil(Perfil.valueOf(request.getParameter("perfil")));
					novoU.setSenhaHash(HashBCrypt.criptografarSenha(request.getParameter("senha")));
					novoU.setAtivo(true);
					usuarioDAO.salvar(novoU);
					em.getTransaction().commit();
					response.sendRedirect("UsuarioServlet?acao=listar");
					return;

				case "abrirCadastro":
					request.getRequestDispatcher("/cadastro_usuario.jsp").forward(request, response);
					return;
				}
			}

			// Listagem padrão — só chega aqui quem já passou pela verificação de sessão
			// acima
			List<Usuario> lista = usuarioDAO.listarTodos();
			request.setAttribute("listaUsuarios", lista);
			request.getRequestDispatcher("/listaUsuarios.jsp").forward(request, response);

		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			e.printStackTrace();
			throw new ServletException("Erro ao processar operação de usuário", e);
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
	}
}