<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.Usuario"%>
<%
Usuario u = (Usuario) request.getAttribute("usuario");
if (u == null) {
	response.sendRedirect("UsuarioServlet?acao=listar");
	return;
}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Editar Usuário - ERP Farmácia</title>
<link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
	<div class="container">
		<h2>Editar Informações do Usuário</h2>
		<form action="UsuarioServlet?acao=atualizar" method="post">
			<input type="hidden" name="id" value="<%=u.getId()%>"> <label>Nome:</label>
			<input type="text" name="nome" value="<%=u.getNome()%>" required>

			<label>E-mail:</label> <input type="email" name="email"
				value="<%=u.getEmail()%>" required> <label>Perfil
				de Acesso:</label> <select name="perfil">
				<option value="ADMIN"
					<%=(u.getPerfil() != null && u.getPerfil().name().equals("ADMIN")) ? "selected" : ""%>>Administrador</option>
				<option value="GERENTE"
					<%=(u.getPerfil() != null && u.getPerfil().name().equals("GERENTE")) ? "selected" : ""%>>Gerente</option>
				<option value="OPERADOR"
					<%=(u.getPerfil() != null && u.getPerfil().name().equals("OPERADOR")) ? "selected" : ""%>>Operador</option>
			</select>

			<div style="margin-top: 20px;">
				<button type="submit" class="btn btn-salvar">Salvar
					Alterações</button>
				<a href="UsuarioServlet?acao=listar" class="btn">Cancelar</a>
			</div>
		</form>
	</div>
</body>
</html>