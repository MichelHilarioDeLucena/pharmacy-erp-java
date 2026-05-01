<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Usuario"%>
<%
// Recupera o usuário logado para verificar permissões
Usuario logado = (Usuario) session.getAttribute("usuarioLogado");

// Proteção básica: se não estiver logado, volta para o login
if (logado == null) {
	response.sendRedirect("${pageContext.request.contextPath}/login.jsp");
	return;
}

// Verifica se o usuário tem nível de acesso para ver esta lista
boolean temAcessoMaster = logado.getPerfil() == model.Perfil.ADMIN || logado.getPerfil() == model.Perfil.GERENTE;
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Gerenciar Usuários - ERP Farmácia</title>
<link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
	<header
		style="display: flex; justify-content: space-between; align-items: center; padding: 10px 20px; background: #eee; border-bottom: 2px solid #ccc;">
		<h2>Gestão de Usuários</h2>
		<div>
			<span>Logado como: <strong><%=logado.getNome()%></strong> (<%=logado.getPerfil()%>)
			</span> <a href="LogoutServlet"
				style="margin-left: 15px; color: red; text-decoration: none;">Sair</a>
		</div>
	</header>

	<main style="padding: 20px;">
		<table>
			<thead>
				<tr>
					<th>ID</th>
					<th>Nome</th>
					<th>E-mail</th>
					<th>Perfil</th>
					<th>Status</th>
					<th>Ações</th>
				</tr>
			</thead>
			<tbody>
				<%
				List<Usuario> lista = (List<Usuario>) request.getAttribute("listaUsuarios");
				if (lista != null && !lista.isEmpty()) {
					for (Usuario u : lista) {
						String statusClass = u.isAtivo() ? "status-ativo" : "status-inativo";
						String statusTexto = u.isAtivo() ? "Ativo" : "Desativado";
						String textoBotaoStatus = u.isAtivo() ? "Desativar" : "Ativar";
						String classeBotaoStatus = u.isAtivo() ? "btn btn-desativar" : "btn btn-ativar";
				%>
				<tr>
					<td><%=u.getId()%></td>
					<td><%=u.getNome()%></td>
					<td><%=u.getEmail()%></td>
					<td><%=u.getPerfil()%></td>
					<td class="<%=statusClass%>"><%=statusTexto%></td>
					<td>
						<%
						if (temAcessoMaster) {
						%>
						<div style="display: flex; gap: 5px;">
							<a href="UsuarioServlet?acao=carregar&id=<%=u.getId()%>"
								class="btn btn-editar" title="Editar dados do usuário"> ✎
								Editar </a> <a href="UsuarioServlet?acao=alternar&id=<%=u.getId()%>"
								class="<%=classeBotaoStatus%>"
								onclick="return confirmarAcao('<%=textoBotaoStatus%>', '<%=u.getNome()%>');">
								<%=textoBotaoStatus%>
							</a>
						</div> <%
 } else {
 %> <span style="color: #666; font-size: 0.9em;">Somente leitura</span>
						<%
						}
						%>
					</td>
				</tr>
				<%
				}
				} else {
				%>
				<tr>
					<td colspan="6" style="text-align: center;">Nenhum usuário
						cadastrado no sistema.</td>
				</tr>
				<%
				}
				%>
			</tbody>
		</table>

		<div style="margin-top: 20px; display: flex; gap: 10px;">
			<a href="home.jsp" class="btn">Voltar para Início</a>
			<%
			if (temAcessoMaster) {
			%>
			<a href="UsuarioServlet?acao=abrirCadastro" class="btn btn-novo">➕
				Novo Usuário</a>
			<%
			}
			%>
		</div>
	</main>

	<script src="js/script.js"></script>
</body>
</html>