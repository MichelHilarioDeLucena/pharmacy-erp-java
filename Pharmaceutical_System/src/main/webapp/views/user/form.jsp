<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ page import="model.Usuario, model.Perfil"%>
<%
    // 1. PROTEÇÃO DE SESSÃO
    Usuario logado = (Usuario) session.getAttribute("usuarioLogado");
    Perfil perfilLogado = (logado != null) ? logado.getPerfil() : null;
    boolean ehMaster = (perfilLogado == Perfil.ADMIN || perfilLogado == Perfil.GERENTE);

    if (!ehMaster) {
        // No Java (Scriptlet), usamos request.getContextPath() em vez de ${...}
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // 2. DESCOBRIR SE É CADASTRO OU EDIÇÃO
    Usuario u = (Usuario) request.getAttribute("usuario");
    boolean isEdit = (u != null && u.getId() != null && u.getId() > 0);

    // 3. PREPARAR AS VARIÁVEIS (Evita NullPointerException no HTML)
    String nome = isEdit ? u.getNome() : "";
    String email = isEdit ? u.getEmail() : "";
    String login = isEdit ? u.getLogin() : "";
    Perfil perfilAtual = isEdit ? u.getPerfil() : null;
    String acao = isEdit ? "atualizar" : "salvar";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= isEdit ? "Editar" : "Novo" %> Usuário - ERP Farmácia</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h2><%= isEdit ? "Editar Informações do Usuário" : "Cadastrar Novo Usuário" %></h2>

        <% String mensagem = (String) request.getAttribute("mensagem");
           if (mensagem != null) { %>
            <p class="erro"><%= mensagem %></p>
        <% } %>

        <form action="${pageContext.request.contextPath}/UsuarioServlet?acao=<%= acao %>" method="post">
            
            <%-- Se for edição, precisamos enviar o ID escondido para o banco saber quem atualizar --%>
            <% if (isEdit) { %>
                <input type="hidden" name="id" value="<%= u.getId() %>">
            <% } %>

            <label>Nome Completo:</label>
            <input type="text" name="nome" value="<%= nome %>" placeholder="Ex: João Silva" required>

            <label>E-mail:</label>
            <input type="email" name="email" value="<%= email %>" placeholder="email@farmacia.com" required>

            <label>Login (Usuário):</label>
            <% if (isEdit) { %>
                <input type="text" value="<%= login %>" disabled title="Não é possível alterar o login de um usuário existente.">
            <% } else { %>
                <input type="text" name="login" value="<%= login %>" placeholder="joao.silva" required>
            <% } %>

            <%-- A senha só aparece na hora de cadastrar. --%>
            <% if (!isEdit) { %>
                <label>Senha Inicial:</label>
                <input type="password" name="senha" required>
            <% } %>

            <label>Perfil de Acesso:</label>
            <select name="perfil">
                <option value="OPERADOR"  <%= (perfilAtual == Perfil.OPERADOR)  ? "selected" : "" %>>Operador</option>
                <option value="GERENTE"   <%= (perfilAtual == Perfil.GERENTE)   ? "selected" : "" %>>Gerente</option>
                <option value="ADMIN"     <%= (perfilAtual == Perfil.ADMIN)     ? "selected" : "" %>>Administrador</option>
                <option value="CAIXA"     <%= (perfilAtual == Perfil.CAIXA)     ? "selected" : "" %>>Caixa</option>
            </select>

            <div style="margin-top: 20px;">
                <button type="submit" class="btn btn-salvar"><%= isEdit ? "Salvar Alterações" : "Cadastrar" %></button>
                <a href="${pageContext.request.contextPath}/UsuarioServlet?acao=listar" class="btn"><%= isEdit ? "Cancelar" : "Voltar" %></a>
            </div>
        </form>
    </div>
</body>
</html>